package com.xujun.contralayout.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xujun.contralayout.R;

/**
 * @ explain:
 * @ author：xujun on 2016/10/18 16:42
 * @ email：gdutxiaoxu@163.com
 */
public class ItemAdapter2 extends BaseAdapter {


    @Override
    public int getCount() {
        return 40;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(viewGroup.getContext(), R.layout.item_string, null);
        TextView  tv= (TextView) v.findViewById(R.id.tv);
        tv.setText("" + i);
        return v;
    }
}
