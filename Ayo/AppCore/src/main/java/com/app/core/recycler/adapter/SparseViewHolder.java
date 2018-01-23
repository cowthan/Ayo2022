
package com.app.core.recycler.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

/**
 * 通用ViewHolder ViewHolder用类的成员变量存控件 Map可以用键值对来存控件 都一样
 */
public class SparseViewHolder extends RecyclerView.ViewHolder {
    private SparseViewHolder(View itemView) {
        super(itemView);
        viewHolder = new SparseArray<View>();
        view = itemView;
    }

    private SparseArray<View> viewHolder;

    private View view;

    public View findViewById(int id) {
        View holdedView = viewHolder.get(id);
        if (holdedView == null) {
            holdedView = view.findViewById(id);
            viewHolder.put(id, holdedView);
        }
        return holdedView;
    }

    public <T> T id(int id) {
        return (T)findViewById(id);
    }

    public static SparseViewHolder bind(View view) {
        Object viewTag = view.getTag();
        if (viewTag != null && viewTag instanceof SparseViewHolder) {
            return (SparseViewHolder)viewTag;
        } else {
            viewTag = new SparseViewHolder(view);
            view.setTag(viewTag);
            return (SparseViewHolder)viewTag;
        }
    }

    public View root() {
        return view;
    }

}
