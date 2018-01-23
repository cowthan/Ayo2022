package org.ayo.list.adapter;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by qiaoliang on 2017/8/3.
 */

public interface OnItemClickCallback<T> {

    void onItemClick(ViewGroup listableView, View itemView, int position, T model);

}
