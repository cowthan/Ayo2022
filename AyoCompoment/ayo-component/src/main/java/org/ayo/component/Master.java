package org.ayo.component;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import org.ayo.component.tmpl.TmplSingleTopActivity;
import org.ayo.component.tmpl.TmplSingleInstanceActivity;
import org.ayo.component.tmpl.TmplSingleTaskActivity;
import org.ayo.component.tmpl.TmplStarndardActivity;

/**
 * Created by Administrator on 2017/1/4 0004.
 */

public class Master {

    public static final int LAYOUT_TAB_PAGE_TOP_OVERLAY = R.layout.ayo_page_tab;
    public static final int LAYOUT_TAB_PAGE_TOP_NONE_OVERLAY = R.layout.ayo_page_tab_page_top_none_overlay;
    public static final int LAYOUT_TAB_PAGE_BOTTOM_OVERLAY = R.layout.ayo_page_tab_page_bottom_overlay;
    public static final int LAYOUT_TAB_PAGE_BOTTOM_NONE_OVERLAY = R.layout.ayo_page_tab_page_bottom_none_overlay;

    public static final int LAYOUT_PAGER_PAGE_TOP_OVERLAY = R.layout.ayo_page_viewpager;
    public static final int LAYOUT_PAGER_PAGE_TOP_NONE_OVERLAY = R.layout.ayo_page_viewpager_page_top_none_overlay;
    public static final int LAYOUT_PAGER_PAGE_BOTTOM_OVERLAY = R.layout.ayo_page_viewpager_page_bottom_overlay;
    public static final int LAYOUT_PAGER_PAGE_BOTTOM_NONE_OVERLAY = R.layout.ayo_page_viewpager_page_bottom_none_overlay;

    public static void startActivity(Activity a, Class<?> clazz, Bundle b){
        Intent intent = new Intent(a, clazz);
        intent.putExtra("data", b == null ? new Bundle() : b);
        a.startActivity(intent);
    }

    public static void startPage(Activity a, Class<? extends MasterFragment> clazz, Bundle b){
        startPage(a, clazz, b, TmplStarndardActivity.class);
    }

    public static void startPageStandard(Activity a, Class<? extends MasterFragment> clazz, Bundle b){
        startPage(a, clazz, b, TmplStarndardActivity.class);
    }

    public static void startPageSingleTask(Activity a, Class<? extends MasterFragment> clazz, Bundle b){
        startPage(a, clazz, b, TmplSingleTaskActivity.class);
    }

    public static void startPageSingleTop(Activity a, Class<? extends MasterFragment> clazz, Bundle b){
        startPage(a, clazz, b, TmplSingleTopActivity.class);
    }

    public static void startPageSingleInstance(Activity a, Class<? extends MasterFragment> clazz, Bundle b){
        startPage(a, clazz, b, TmplSingleInstanceActivity.class);
    }

    public static void startPageForResult(Activity a, Class<? extends MasterFragment> clazz, Bundle b, int requestCode){
        startPageForResult(a, clazz, b, TmplStarndardActivity.class, requestCode);
    }

    public static void startPageStandardForResult(Activity a, Class<? extends MasterFragment> clazz, Bundle b, int requestCode){
        startPageForResult(a, clazz, b, TmplStarndardActivity.class, requestCode);
    }

    public static void startPageSingleTaskForResult(Activity a, Class<? extends MasterFragment> clazz, Bundle b, int requestCode){
        startPageForResult(a, clazz, b, TmplSingleTaskActivity.class, requestCode);
    }

    public static void startPageSingleTopForResult(Activity a, Class<? extends MasterFragment> clazz, Bundle b, int requestCode){
        startPageForResult(a, clazz, b, TmplSingleTopActivity.class, requestCode);
    }

    public static void startPageSingleInstanceForResult(Activity a, Class<? extends MasterFragment> clazz, Bundle b, int requestCode){
        startPageForResult(a, clazz, b, TmplSingleInstanceActivity.class, requestCode);
    }

    public static void startPage(Activity a, Class<? extends MasterFragment> clazz, Bundle b, Class<? extends Activity> tmplActvity){
        Intent intent = new Intent(a, tmplActvity);
        intent.putExtra("data", b);
        intent.putExtra("page", clazz.getName());
        a.startActivity(intent);
    }

    public static void startPageForResult(Activity a, Class<? extends MasterFragment> clazz, Bundle b, Class<? extends Activity> tmplActvity, int requestCode){
        Intent intent = new Intent(a, tmplActvity);
        intent.putExtra("data", b);
        intent.putExtra("page", clazz.getName());
        a.startActivityForResult(intent, requestCode);
    }

}
