package org.ayo.ui.sample.master;

import android.app.Activity;
import android.os.Bundle;

import org.ayo.component.Master;
import org.ayo.component.MasterFragment;
import org.ayo.component.tmpl.TmplBaseActivity;

/**
 * Created by Administrator on 2017/1/4 0004.
 */

public abstract class MasterPage extends MasterFragment {

    /**
     * 所有页面通过这个方法来打开
     * @param a
     * @param pageClass
     * @param bundle
     * @param tmplClass
     */
    public static void startPage(Activity a, Class<? extends MasterPage> pageClass, Bundle bundle, Class<? extends TmplBaseActivity> tmplClass){
        Master.startPage(a, pageClass, bundle, tmplClass);
        //如果extends MasterActivity，则这里换成：Master.startActivity(a, pageClass, bundle);
    }
    /**
     * 所有页面通过这个方法来打开
     * @param a
     * @param pageClass
     * @param bundle
     */
    public static void startPage(Activity a, Class<? extends MasterPage> pageClass, Bundle bundle){
        Master.startPage(a, pageClass, bundle);
        //如果extends MasterActivity，则这里换成：Master.startActivity(a, pageClass, bundle);
    }

}
