package org.ayo.ui.sample.recycler.animator;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.ayo.sample.R;

/**
 * Created by Administrator on 2017/1/23 0023.
 */

class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView textView;
    public LinearLayout container;

    public MyViewHolder(View v) {
        super(v);
        container = (LinearLayout) v;
        textView = (TextView) v.findViewById(R.id.textview);
    }

    @Override
    public String toString() {
        return super.toString() + " \"" + textView.getText() + "\"";
    }
}
