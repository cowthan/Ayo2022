package org.ayo.ui.sample.notify;

import android.view.View;

import org.ayo.notify.loading.LoadingDialog;
import org.ayo.notify.loading.LoadingTopDialog;
import org.ayo.sample.menu.DemoMenuFragment;

/**
 * Created by Administrator on 2016/8/21.
 */
public class DemoLoadingDialogActivity extends DemoMenuFragment {
    @Override
    public String getDemoName() {
        return "LoadingDialog";
    }

    @Override
    public DemoInfo[] getDemoMenus() {
        return new DemoInfo[]{
                new DemoInfo("LoadingDialog--center", new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        LoadingDialog loadingDialog = new LoadingDialog(getActivity());
                        loadingDialog.setTitle("正在提交...");
                        loadingDialog.show();
                    }
                }),
                new DemoInfo("LoadingDialog--top", new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        LoadingTopDialog loadingDialog = new LoadingTopDialog(getActivity());
                        loadingDialog.setTitle("正在登录...");
                        loadingDialog.show();
                    }
                })
        };
    }
}
