package org.ayo.ui.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import org.ayo.component.MasterFragment;
import org.ayo.sample.R;


/**
 * Created by Administrator on 2016/11/29.
 */

public class DemoFragment extends MasterFragment {

    private static final String ARG_NAME = "name";

    public static DemoFragment newInstance(String name) {
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        DemoFragment fragment = new DemoFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.frag_demo;
    }

    @Override
    protected void onCreate2(View view, @Nullable Bundle bundle) {
        String name = getArguments().getString(ARG_NAME);
        TextView tv = (TextView) findViewById(R.id.tv);
        tv.setText(name);
    }

    @Override
    protected void onDestroy2() {

    }

    @Override
    protected void onPageVisibleChanged(boolean b, boolean b1, @Nullable Bundle bundle) {

    }

}
