package org.ayo.component.sample.master;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.ayo.component.StateModel;
import org.ayo.component.sample.R;
import org.ayo.sample.menu.notify.ToasterDebug;


/**
 * Created by Administrator on 2017/1/4 0004.
 */

public class DemoFragment extends MasterPage {


    @Override
    protected int getLayoutId() {
        return R.layout.ac_demo_component;
    }

    @Override
    protected StateModel createStateModel() {
        return null;
    }

    @Override
    protected void onCreate2(View contentView,  @Nullable Bundle savedInstanceState) {
        ToasterDebug.toastShort(getActivity().getClass().getName());
        TextView title = (TextView) contentView.findViewById(R.id.title);
        TextView title2 = (TextView) contentView.findViewById(R.id.title2);

        title.setText(getActivity().getClass().getName());
        title2.setText("haha = " + getArguments().get("haha"));

        getAgent().renderSystemBar(Color.YELLOW, Color.GREEN);
        getAgent().enableSystemBarTakenByContent(false);
    }

    @Override
    protected void onDestroy2() {

    }

    @Override
    protected void onPageVisibleChanged(boolean visible, boolean isFirstTimeVisible, @Nullable Bundle savedInstanceState) {
        String s = "onPageVisibleChanged--{dd}->" + getClass().getSimpleName() + ", " + (isFirstTimeVisible ? "是" : "非") + "第一次";
        s = s.replace("{dd}", visible ? "来了" : "走了");
        Log.i("PermissionMainActivity", s);
    }

}
