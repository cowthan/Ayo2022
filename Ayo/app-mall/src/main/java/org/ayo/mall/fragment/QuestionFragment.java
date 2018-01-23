package org.ayo.mall.fragment;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import org.ayo.mall.R;
import org.ayo.mall.adapter.MineQuestionAdapter;
import org.ayo.mall.bean.MineQuestionBean;
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

public class QuestionFragment extends LazyFragment {
    public static final String TAG = QuestionFragment.class.getSimpleName();

    RecyclerView recyclerView;
    //    @BindView(R.id.refreshLayout)
//    SmartRefreshLayout refreshLayout;
    private MineQuestionAdapter adapter;

    public static QuestionFragment getInstance() {
        return new QuestionFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.pn_fr_question;
    }

    @Override
    public void lazyInitView(View view, Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.recycler_view);

        final List<MineQuestionBean> data = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            final MineQuestionBean questionBean = new MineQuestionBean();
            questionBean.setContent("使用NestedScrollView+ViewPager+RecyclerView+SmartRefreshLayout打造酷炫下拉视差效果并解决各种滑动冲突" + i);
            data.add(questionBean);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        recyclerView.addItemDecoration(new NormalDecoration(ContextCompat.getColor(mActivity, R.color.mainGrayF8), (int) mActivity.getResources().getDimension(R.dimen.eight)));

        adapter = new MineQuestionAdapter(mActivity);
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
    @Override
    protected void initData() {

    }
}
