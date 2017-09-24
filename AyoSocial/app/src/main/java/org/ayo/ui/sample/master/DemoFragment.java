package org.ayo.ui.sample.master;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import org.ayo.notify.toaster.Toaster;
import org.ayo.sample.R;

/**
 * Created by Administrator on 2017/1/4 0004.
 */

public class DemoFragment extends MasterPage {


    @Override
    protected int getLayoutId() {
        return R.layout.ac_demo_component;
    }

    @Override
    protected void onCreate2(View contentView, @Nullable Bundle savedInstanceState) {
        Toaster.toastShort(getActivity().getClass().getName());
        TextView title = (TextView) contentView.findViewById(R.id.title);
        TextView title2 = (TextView) contentView.findViewById(R.id.title2);

        title.setText(getActivity().getClass().getName());
        title2.setText("haha = " + getArguments().get("haha"));
    }

    @Override
    protected void onDestroy2() {

    }

    @Override
    protected void onPageVisibleChanged(boolean b, boolean b1, @Nullable Bundle bundle) {

    }
}
