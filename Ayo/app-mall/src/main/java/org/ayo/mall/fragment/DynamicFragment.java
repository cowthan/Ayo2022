package org.ayo.mall.fragment;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import org.ayo.mall.R;
import org.ayo.mall.adapter.MineDynamicAdapter;
import org.ayo.mall.bean.MineDynamicBean;
import org.ayo.mall.fragment.base.LazyFragment;
import org.ayo.mall.view.NormalDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * @Created SiberiaDante
 * @Describe：
 * @CreateTime: 2017/12/14
 * @UpDateTime:
 * @Email: 2654828081@qq.com
 * @GitHub: https://github.com/SiberiaDante
 */

public class DynamicFragment extends LazyFragment {

    RecyclerView recyclerView;

    private MineDynamicAdapter adapter;

    public static DynamicFragment getInstance() {
        return new DynamicFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.pn_fr_dynamic;
    }

    @Override
    protected void initData() {

    }

    @Override
    public void lazyInitView(View view, Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.recycler_view);
        final List<MineDynamicBean> data = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            final MineDynamicBean questionBean = new MineDynamicBean();
            questionBean.setContent("使用NestedScrollView+ViewPager+RecyclerView+SmartRefreshLayout打造酷炫下拉视差效果并解决各种滑动冲突" + i);
            if (i % 1 == 0) {
                questionBean.setType(0);
            }
            if (i % 2 == 0) {
                questionBean.setType(1);
            }
            if (i % 3 == 0) {
                questionBean.setType(2);
            }
            data.add(questionBean);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        recyclerView.addItemDecoration(new NormalDecoration(ContextCompat.getColor(mActivity, R.color.mainGrayF8), (int) mActivity.getResources().getDimension(R.dimen.one)));

        adapter = new MineDynamicAdapter(mActivity);
        recyclerView.setAdapter(adapter);
        adapter.addAll(data);
        adapter.setNoMore(R.layout.pn_view_no_more);
        adapter.setMore(R.layout.pn_view_more, new RecyclerArrayAdapter.OnMoreListener() {
            @Override
            public void onMoreShow() {
                Log.d(TAG, "----onMoreShow");
                adapter.addAll(data);
            }

            @Override
            public void onMoreClick() {

            }
        });
    }
}
