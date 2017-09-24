package org.ayo.ui.sample.notify;

import android.view.View;

import org.ayo.notify.actionsheet.ActionSheetDialog;
import org.ayo.notify.actionsheet.ActionSheetUtils;
import org.ayo.notify.toaster.Toaster;
import org.ayo.sample.menu.DemoMenuFragment;

/**
 * Created by Administrator on 2016/8/21.
 */
public class DemoActionSheetActivity extends DemoMenuFragment {
    @Override
    public String getDemoName() {
        return "ActionSheet";
    }

    @Override
    public DemoInfo[] getDemoMenus() {
        return new DemoInfo[]{
                new DemoInfo("ActionSheet", new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        ActionSheetUtils.showActionSheet(getActivity(), new String[]{"相机", "相册"}, new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                Toaster.toastShort("点击--" + which);
                            }
                        });
                    }
                })
        };
    }
}
