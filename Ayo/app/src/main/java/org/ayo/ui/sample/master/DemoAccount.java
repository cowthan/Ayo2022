package org.ayo.ui.sample.master;

import org.ayo.sample.menu.DemoMenuFragment;

/**
 * Created by Administrator on 2017/1/4.
 */

public class DemoAccount extends DemoMenuFragment {
    @Override
    public String getDemoName() {
        return "账号模块模板";
    }

    @Override
    public DemoInfo[] getDemoMenus() {
        return null;
//        return new DemoInfo[]{
//                new DemoInfo("登录页", new View.OnClickListener(){
//                    @Override
//                    public void onClick(View v) {
//                        MasterAccount.startPage(getActivity(), LoginActivity.class, null);
//                    }
//                })
//        };
    }
}
