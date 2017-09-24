package org.ayo.ui.sample.template.recycler;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import org.ayo.sample.R;
import org.ayo.template.status.DefaultStatus;
import org.ayo.ui.sample.base.AyoActivity;

/**
 * Created by Administrator on 2016/8/21.
 */
public class DemoRecyclerViewActivity extends AyoActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.ac_demo_recycler;
    }

    @Override
    protected void onCreate2(View view, @Nullable Bundle bundle) {
        final DemoTopListFragment frag = new DemoTopListFragment();
        frag.enableInitAfterViewCreated(true);
        getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, frag).commit();

        View btn_refresh = findViewById(R.id.btn_refresh);
        View btn_refresh2 = findViewById(R.id.btn_refresh2);
        View btn_netoff = findViewById(R.id.btn_netoff);
        View btn_server_error = findViewById(R.id.btn_server_error);
        View btn_logic_fail = findViewById(R.id.btn_logic_fail);
        View btn_local_error = findViewById(R.id.btn_local_error);
        View btn_empty = findViewById(R.id.btn_empty);

        btn_refresh.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                frag.refreshAuto();
            }
        });

        btn_refresh2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                frag.refreshWithLoadingStatus();
            }
        });
        btn_empty.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                frag.onLoadOk(null);
            }
        });

        btn_netoff.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                frag.onLoadFail(true, DefaultStatus.STATUS_NETOFF, "", 1);
            }
        });

        btn_server_error.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                frag.onLoadFail(true, DefaultStatus.STATUS_SERVER_ERROR, "500, 404, timeout, host无法解析", 1);
            }
        });

        btn_logic_fail.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                frag.onLoadFail(true, DefaultStatus.STATUS_LOGIC_FAIL, "手机号不符合规范", 20001);
            }
        });
        btn_local_error.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                frag.onLoadFail(true, DefaultStatus.STATUS_lOCAL_ERROR, "解析json出错", -12);
            }
        });
    }

    @Override
    protected void onDestroy2() {

    }

    @Override
    protected void onPageVisibleChanged(boolean b, boolean b1, @Nullable Bundle bundle) {

    }

}
