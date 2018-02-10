
package com.app.core.recycler.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by qiaoliang on 2017/3/24.
 */

public abstract class SimpleRecyclerAdapter<T> extends RecyclerView.Adapter<SparseViewHolder> {

    protected Activity mActivity;

    protected List<T> list;

    public SimpleRecyclerAdapter(Activity a, List<T> data) {
        mActivity = a;
        list = data;
    }

    protected abstract int getLayoutId(int viewType);

    protected abstract void onBind(SparseViewHolder holder, T model, int postion);

    @Override
    final public SparseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mActivity).inflate(getLayoutId(viewType), parent, false);
        SparseViewHolder vh = SparseViewHolder.bind(v);
        return vh;
    }

    @Override
    final public void onBindViewHolder(SparseViewHolder holder, int position) {
        onBind(holder, list.get(position), position);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public void notifyDataSetChanged(List<T> list) {
        this.list = list;
        this.notifyDataSetChanged();
    }
}
