package org.ayo.ui.sample.recycler;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.ayo.sample.R;
import org.ayo.ui.sample.BasePage;
import org.ayo.ui.sample.recycler.animator.FlipColorItemAnimator;
import org.ayo.ui.sample.recycler.animator.RecyclerAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static org.ayo.ui.sample.recycler.animator.RecyclerAdapter.generateColor;

/**
 * Created by Administrator on 2017/1/23 0023.
 */

public class RecyclerViewDemo2 extends BasePage {


    @BindView(R.id.mRecyclerView) RecyclerView mRecyclerView;
    @BindView(R.id.tv_refresh) TextView tv_refresh;
    @BindView(R.id.tv_insert) TextView tv_insert;
    @BindView(R.id.tv_remove) TextView tv_remove;
    @BindView(R.id.tv_change) TextView tv_change;
    @BindView(R.id.tv_move) TextView tv_move;
    @BindView(R.id.tv_insert_range) TextView tv_insert_range;
    @BindView(R.id.tv_remove_range) TextView tv_remove_range;
    @BindView(R.id.tv_change_range) TextView tv_change_range;

    List<Integer> list;
    RecyclerAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.frag_recycler_list_anim;
    }


    @Override
    protected void onCreate2(View view, @Nullable Bundle bundle) {
        ButterKnife.bind(this, view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new RecyclerAdapter(getActivity(), mRecyclerView);
        list = mAdapter.getData();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new FlipColorItemAnimator());

        tv_refresh.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                list.clear();
                for (int i = 0; i < 100; ++i) {
                    list.add(generateColor());
                }
                mAdapter.notifyDataSetChanged();
            }
        });

        tv_insert.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                list.add(generateColor());
                mAdapter.notifyItemInserted(3);
            }
        });

        tv_remove.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                list.remove(3);
                mAdapter.notifyItemRemoved(3);
            }
        });

        tv_change.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                list.set(2, Integer.valueOf(generateColor()));
                mAdapter.notifyItemChanged(2);
            }
        });

        tv_move.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mAdapter.notifyItemMoved(3, 5);
            }
        });

        tv_insert_range.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                list.add(generateColor());
                list.add(generateColor());
                list.add(generateColor());
                list.add(generateColor());
                mAdapter.notifyItemRangeInserted(3, 4);
            }
        });

        tv_remove_range.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                list.remove(3);
                list.remove(3);
                list.remove(3);
                list.remove(3);
                mAdapter.notifyItemRangeRemoved(3, 4);
            }
        });

        tv_change_range.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                list.set(2, Integer.valueOf(generateColor()));
                list.set(3, Integer.valueOf(generateColor()));
                list.set(4, Integer.valueOf(generateColor()));
                list.set(5, Integer.valueOf(generateColor()));
                mAdapter.notifyItemRangeChanged(2, 4);
            }
        });
    }

    @Override
    protected void onDestroy2() {

    }

    @Override
    protected void onPageVisibleChanged(boolean b, boolean b1, @Nullable Bundle bundle) {

    }
}
