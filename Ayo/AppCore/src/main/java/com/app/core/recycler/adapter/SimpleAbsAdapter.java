
package com.app.core.recycler.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by qiaoliang on 2017/3/24.
 */

public abstract class SimpleAbsAdapter<T> extends BaseAdapter {

    protected Activity mActivity;

    protected List<T> list;

    public SimpleAbsAdapter(Activity a, List<T> data) {
        mActivity = a;
        list = data;
    }

    protected abstract int getLayoutId(int viewType);

    protected abstract void onBind(SparseViewHolder holder, T model, int postion);

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SparseViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(mActivity, getLayoutId(getItemViewType(position)), null);
            holder = SparseViewHolder.bind(convertView);
            convertView.setTag(holder);
        } else {
            holder = (SparseViewHolder)convertView.getTag();
        }
        onBind(holder, list.get(position), position);
        return convertView;
    }

    public void notifyDataSetChanged(List<T> list) {
        this.list = list;
        this.notifyDataSetChanged();
    }
}
