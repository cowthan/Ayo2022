package org.ayo.component.sample.samplepage;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import org.ayo.component.MasterFragment;
import org.ayo.component.sample.R;
import org.ayo.lang.Lang;

/**
 * Created by Administrator on 2017/5/21.
 */

public class FakeFragment extends MasterFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.ac_common_fake;
    }

    @Override
    protected void onCreate2(View contentView, @Nullable Bundle savedInstanceState) {
        TextView tv_title = id(R.id.tv_title);
        String title = Lang.rstring(getArguments(), "title", getClass().getSimpleName());
        tv_title.setText(title);

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        });

        findViewById(R.id.btn00).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
               PureTranslusentActivity.start(getActivity(), false);
            }
        });

        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                FakeStandardActivity.start(getActivity(), false);
            }
        });

        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                FakeStandardActivity_T.start(getActivity(), false);
            }
        });

        findViewById(R.id.btn3).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                FakeSingleTopActivity.start(getActivity(), false);
            }
        });

        findViewById(R.id.btn4).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                FakeSingleTopActivity_T.start(getActivity(), false);
            }
        });

        findViewById(R.id.btn5).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                FakeSingleTaskActivity.start(getActivity(), false);
            }
        });

        findViewById(R.id.btn6).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                FakeSingleTaskActivity_T.start(getActivity(), false);
            }
        });

        findViewById(R.id.btn7).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                FakeSingleInstanceActivity.start(getActivity(), false);
            }
        });

        findViewById(R.id.btn8).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                FakeSingleInstanceActivity_T.start(getActivity(), false);
            }
        });

    }

    @Override
    protected void onDestroy2() {

    }

    @Override
    protected void onPageVisibleChanged(boolean visible, boolean isFirstTimeVisible, @Nullable Bundle savedInstanceState) {

    }
}
