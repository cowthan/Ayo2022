package org.ayo.fringe.ui.base;

import android.app.Activity;
import android.os.Bundle;

import org.ayo.component.Master;
import org.ayo.component.MasterFragment;

/**
 * Created by Administrator on 2017/1/14 0014.
 */

public class Pages {

    public static void startWithSwipeback(Activity a, Class<? extends MasterFragment> c, Bundle b){
        Master.startPage(a, c, b);
    }
    public static void start(Activity a, Class<? extends MasterFragment> c, Bundle b){
        Master.startPage(a, c, b, CustomMasterTmplActivity.class);
    }
}
