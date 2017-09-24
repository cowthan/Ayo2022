/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.moduth.blockcanary.ui;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.github.moduth.blockcanary.BlockCanaryContext;
import com.github.moduth.blockcanary.BlockCanaryInternals;
import com.github.moduth.blockcanary.LogWriter;

import org.ayo.hover.wrapper.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static android.text.format.DateUtils.FORMAT_SHOW_DATE;
import static android.text.format.DateUtils.FORMAT_SHOW_TIME;

/**
 * Display blocks.
 */
public class DisplayView extends FrameLayout {

    private static final String TAG = "DisplayActivity";

    // empty until it's been first loaded.
    private List<BlockInfoEx> mBlockInfoEntries = new ArrayList<>();
    private String mBlockStartTime;

    private ListView mListView;
    private TextView mFailureView;
    private Button mActionButton;
    private int mMaxStoredBlockCount;

    public DisplayView(Context context) {
        super(context);
        init();
    }

    public DisplayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DisplayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DisplayView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        View v = View.inflate(getContext(), R.layout.block_canary_display_leak, null);
        this.addView(v);

        mListView = (ListView) findViewById(R.id.__leak_canary_display_leak_list);
        mFailureView = (TextView) findViewById(R.id.__leak_canary_display_leak_failure);
        mActionButton = (Button) findViewById(R.id.__leak_canary_action);

        mMaxStoredBlockCount = getResources().getInteger(R.integer.block_canary_max_stored_count);

        updateUi();
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        LoadBlocks.load(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        LoadBlocks.forgetActivity();
    }

    public void backToList(){
        if (mBlockStartTime != null) {
            mBlockStartTime = null;
            updateUi();
            LoadBlocks.load(this);
        }else{
            LoadBlocks.load(this);
        }
    }

    public void updateUi() {
        final BlockInfoEx blockInfo = getBlock(mBlockStartTime);
        if (blockInfo == null) {
            mBlockStartTime = null;
        }

        // Reset to defaults
        mListView.setVisibility(VISIBLE);
        mFailureView.setVisibility(GONE);

        if (blockInfo != null) {
            renderBlockDetail(blockInfo);
        } else {
            renderBlockList();
        }


    }

    private void renderBlockList() {
        ListAdapter listAdapter = mListView.getAdapter();
        if (listAdapter instanceof BlockListAdapter) {
            ((BlockListAdapter) listAdapter).notifyDataSetChanged();
        } else {
            BlockListAdapter adapter = new BlockListAdapter();
            mListView.setAdapter(adapter);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mBlockStartTime = mBlockInfoEntries.get(position).timeStart;
                    updateUi();
                }
            });
            mActionButton.setText(R.string.block_canary_delete_all);
            mActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LogWriter.deleteAll();
                    mBlockInfoEntries = Collections.emptyList();
                    updateUi();

                }
            });
        }
        mActionButton.setVisibility(mBlockInfoEntries.isEmpty() ? GONE : VISIBLE);
    }

    private void renderBlockDetail(final BlockInfoEx blockInfo) {
        ListAdapter listAdapter = mListView.getAdapter();
        final DetailAdapter adapter;
        if (listAdapter instanceof DetailAdapter) {
            adapter = (DetailAdapter) listAdapter;
        } else {
            adapter = new DetailAdapter();
            mListView.setAdapter(adapter);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    adapter.toggleRow(position);
                }
            });
            mActionButton.setVisibility(VISIBLE);
            mActionButton.setText(R.string.block_canary_delete);
        }
        mActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (blockInfo != null) {
                    blockInfo.logFile.delete();
                    mBlockStartTime = null;
                    mBlockInfoEntries.remove(blockInfo);
                    updateUi();
                }
            }
        });
        adapter.update(blockInfo);
    }

    private BlockInfoEx getBlock(String startTime) {
        if (mBlockInfoEntries == null || TextUtils.isEmpty(startTime)) {
            return null;
        }
        for (BlockInfoEx blockInfo : mBlockInfoEntries) {
            if (blockInfo.timeStart != null && startTime.equals(blockInfo.timeStart)) {
                return blockInfo;
            }
        }
        return null;
    }

    class BlockListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mBlockInfoEntries.size();
        }

        @Override
        public BlockInfoEx getItem(int position) {
            return mBlockInfoEntries.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext())
                        .inflate(R.layout.block_canary_block_row, parent, false);
            }
            TextView titleView = (TextView) convertView.findViewById(R.id.__leak_canary_row_text);
            TextView timeView = (TextView) convertView.findViewById(R.id.__leak_canary_row_time);
            BlockInfoEx blockInfo = getItem(position);

            String index;
            if (position == 0 && mBlockInfoEntries.size() == mMaxStoredBlockCount) {
                index = "MAX. ";
            } else {
                index = (mBlockInfoEntries.size() - position) + ". ";
            }

            String keyStackString = BlockCanaryUtils.concernStackString(blockInfo);
            String title = index + keyStackString + " " +
                    getContext().getResources().getString(R.string.block_canary_class_has_blocked, blockInfo.timeCost);
            titleView.setText(title);
            String time = DateUtils.formatDateTime(getContext(),
                    blockInfo.logFile.lastModified(), FORMAT_SHOW_TIME | FORMAT_SHOW_DATE);
            timeView.setText(time);
            return convertView;
        }
    }

    static class LoadBlocks implements Runnable {

        static final List<LoadBlocks> inFlight = new ArrayList<>();
        static final Executor backgroundExecutor = Executors.newSingleThreadExecutor();
        private DisplayView activityOrNull;
        private final Handler mainHandler;

        LoadBlocks(DisplayView activity) {
            this.activityOrNull = activity;
            mainHandler = new Handler(Looper.getMainLooper());
        }

        static void load(DisplayView activity) {
            LoadBlocks loadBlocks = new LoadBlocks(activity);
            inFlight.add(loadBlocks);
            backgroundExecutor.execute(loadBlocks);
        }

        static void forgetActivity() {
            for (LoadBlocks loadBlocks : inFlight) {
                loadBlocks.activityOrNull = null;
            }
            inFlight.clear();
        }

        @Override
        public void run() {
            final List<BlockInfoEx> blockInfoList = new ArrayList<>();
            File[] files = BlockCanaryInternals.getLogFiles();
            if (files != null) {
                for (File blockFile : files) {
                    try {
                        BlockInfoEx blockInfo = BlockInfoEx.newInstance(blockFile);
                        if (!BlockCanaryUtils.isBlockInfoValid(blockInfo)) {
                            throw new BlockInfoCorruptException(blockInfo);
                        }

                        boolean needAddToList = true;

                        if (BlockCanaryUtils.isInWhiteList(blockInfo)) {
                            if (BlockCanaryContext.get().deleteFilesInWhiteList()) {
                                blockFile.delete();
                                blockFile = null;
                            }
                            needAddToList = false;
                        }

                        blockInfo.concernStackString = BlockCanaryUtils.concernStackString(blockInfo);
                        if (BlockCanaryContext.get().filterNonConcernStack() &&
                                TextUtils.isEmpty(blockInfo.concernStackString)) {
                            needAddToList = false;
                        }

                        if (needAddToList && blockFile != null) {
                            blockInfoList.add(blockInfo);
                        }
                    } catch (Exception e) {
                        // Probably blockFile corrupts or format changes, just delete it.
                        blockFile.delete();
                        Log.e(TAG, "Could not read block log file, deleted :" + blockFile, e);
                    }
                }
                Collections.sort(blockInfoList, new Comparator<BlockInfoEx>() {
                    @Override
                    public int compare(BlockInfoEx lhs, BlockInfoEx rhs) {
                        return Long.valueOf(rhs.logFile.lastModified())
                                .compareTo(lhs.logFile.lastModified());
                    }
                });
            }
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    inFlight.remove(LoadBlocks.this);
                    if (activityOrNull != null) {
                        activityOrNull.mBlockInfoEntries = blockInfoList;
                        Log.d(TAG, "load block entries: " + blockInfoList.size());
                        activityOrNull.updateUi();
                    }
                }
            });
        }
    }
}