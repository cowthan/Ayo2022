package org.ayo.ui.sample.notify;

import android.view.View;

import org.ayo.notify.toaster.Toaster;
import org.ayo.sample.menu.DemoMenuFragment;

/**
 * Created by Administrator on 2016/8/21.
 */
public class DemoToastActivity extends DemoMenuFragment {
    @Override
    public String getDemoName() {
        return "Toast";
    }

    @Override
    public DemoInfo[] getDemoMenus() {
        return new DemoInfo[]{
                new DemoInfo("Toaster", new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Toaster.toastShort("能够被顶掉的Toaster，不依赖于系统--" + System.currentTimeMillis());
                    }
                })
        };
    }
}
