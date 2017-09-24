package org.ayo.ui.sample.nano;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import org.ayo.component.MasterFragment;
import org.ayo.sample.R;

/**
 * Created by qiaoliang on 2017/6/4.
 */

public class NanoHttpServerDemo extends MasterFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.ac_demo_nano_server;
    }

    TextView btn_start, tv_info, tv_http;

    @Override
    protected void onCreate2(View view, @Nullable Bundle bundle) {
        btn_start = id(R.id.btn_start);
        tv_info = id(R.id.tv_info);
        tv_http = id(R.id.tv_http);


        btn_start.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(FakeHttpd.getDefault().isRunning()){
                    FakeHttpd.getDefault().stop();
                    tv_info.setText("服务器未运行");
                    btn_start.setText("start");
                }else{
                    int port = 8092;
                    FakeHttpd.getDefault().start(port);
                    tv_info.setText("http://" + FakeHttpd.getDefault().getLocalIpAddress() + ":" + port);
                    btn_start.setText("stop");
                }

            }
        });

        FakeHttpdController.getDefault().initRoutes();

    }


    @Override
    protected void onDestroy2() {
        FakeHttpd.getDefault().stop();
    }

    @Override
    protected void onPageVisibleChanged(boolean b, boolean b1, @Nullable Bundle bundle) {

    }
}
