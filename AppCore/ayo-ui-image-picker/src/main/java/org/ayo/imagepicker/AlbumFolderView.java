
package org.ayo.imagepicker;


import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;


public class AlbumFolderView extends RelativeLayout {

    private Context mContext;

    private MyRecyclerView mRecyclerView;

    private AlbumFolderAdapter mAdapter;

    private boolean isShow;

    private OnFolderSelectListener mSelectListener;

    private ArrayList<AlbumFolderModel> mData;

    private View.OnClickListener mPopSelectListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int pos = mRecyclerView.getChildAdapterPosition(view);
            AlbumFolderModel model = mAdapter.getItem(pos);
            if (model == null) return;
            mSelectListener.onSelect(pos, model);
            setSelectedFolder(pos);
        }
    };

    public AlbumFolderView(Context context) {
        this(context, null);
    }

    public AlbumFolderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mRecyclerView = (MyRecyclerView) findViewById(R.id.pop_recyclerView);
    }

    public void setupView() {
        //设置布局管理器
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new AlbumFolderAdapter(mContext, mPopSelectListener);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void setData(ArrayList<AlbumFolderModel> models) {
        this.mData = models;
        mAdapter.setData(models);
        mAdapter.notifyDataSetChanged();
    }

    public boolean isShow() {
        return isShow;
    }

    public void show() {
        isShow = true;
        mRecyclerView.setVisibility(VISIBLE);
    }

    public void hide() {
        isShow = false;
        mRecyclerView.setVisibility(GONE);
    }

    public void setFolderSelectListener(OnFolderSelectListener selectListener) {
        this.mSelectListener = selectListener;
    }

    public void setSelectedFolder(int position) {
        int size = mData.size();
        for (int i = 0; i < size; i++) {
            if (i == position) {
                mData.get(i).setSelected(true);
            } else {
                mData.get(i).setSelected(false);
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    public interface OnFolderSelectListener {
        /**
         * 相册文件夹切换回调函数
         */
        void onSelect(int position, AlbumFolderModel model);
    }

}
