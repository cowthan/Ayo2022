package com.xujun.contralayout.UI;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.xujun.contralayout.R;
import com.xujun.contralayout.adapter.ItemAdapter2;
import com.xujun.contralayout.base.BaseFragment;
import com.xujun.contralayout.recyclerView.divider.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * @ explain:
 * @ author：xujun on 2016/10/18 17:21
 * @ email：gdutxiaoxu@163.com
 */
public class ListFragment2 extends BaseFragment {

    ListView mRecyclerView;
    private static final String KEY = "key";
    private String title = "测试";

    List<String> mDatas = new ArrayList<>();
    private ItemAdapter2 mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public static ListFragment2 newInstance(String title) {
        ListFragment2 fragment = new ListFragment2();
        Bundle bundle = new Bundle();
        bundle.putString(KEY, title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initView(View view) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            title = arguments.getString(KEY);
        }
        mRecyclerView = (ListView) view.findViewById(R.id.recyclerView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setEnabled(false);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(mContext,
                LinearLayoutManager.VERTICAL);

        for (int i = 0; i < 50; i++) {
            String s = String.format("我是第%d个" + title, i);
            mDatas.add(s);
        }

        mAdapter = new ItemAdapter2();
        mRecyclerView.setAdapter(mAdapter);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(mContext, "刷新完成", Toast.LENGTH_SHORT).show();
                    }
                }, 1200);
            }
        });


    }

    public void tooglePager(boolean isOpen) {
        if (isOpen) {
            setRefreshEnable(false);
            scrollToFirst(false);
        } else {
            setRefreshEnable(true);
        }
    }

    public void scrollToFirst(boolean isSmooth) {
        if (mRecyclerView == null) {
            return;
        }
        if (isSmooth) {
            mRecyclerView.smoothScrollToPosition(0);
        } else {
//            mRecyclerView.scrollToPosition(0);
        }
    }

    public void setRefreshEnable(boolean enabled) {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setEnabled(enabled);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_list2;
    }

    @Override
    public void fetchData() {

    }
}
