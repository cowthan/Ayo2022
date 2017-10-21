package org.ayo.component.sample.master;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import org.ayo.component.StateModel;
import org.ayo.component.sample.R;
import org.ayo.component.tmpl.TmplSingleInstanceActivity;
import org.ayo.component.tmpl.TmplSingleTaskActivity;
import org.ayo.component.tmpl.TmplSingleTopActivity;
import org.ayo.sample.menu.DemoMenuFragment;
import org.ayo.sample.menu.notify.ToasterDebug;

import java.util.List;

/**
 * Created by Administrator on 2017/1/4 0004.
 */

public class DemoPage extends DemoMenuFragment {

    @Override
    public String getDemoName() {
        return "免manifest框架";
    }

    @Override
    protected StateModel createStateModel() {
        return null;
    }

    @Override
    public DemoInfo[] getDemoMenus() {
        final Bundle bundle = new Bundle();
        bundle.putString("haha", "haha-value");
        return new DemoInfo[]{
                new DemoInfo("standard模式", new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        MasterPage.startPage(getActivity(), DemoFragment.class, bundle);
                        getActivity().overridePendingTransition(R.anim.base_slide_in_from_bottom, R.anim.base_slide_out_to_top);
                    }
                }),
                new DemoInfo("single task模式", new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        MasterPage.startPage(getActivity(), DemoFragment.class, bundle, TmplSingleTaskActivity.class);
                    }
                }),
                new DemoInfo("single top模式", new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        MasterPage.startPage(getActivity(), DemoFragment.class, bundle, TmplSingleTopActivity.class);
                    }
                }),
                new DemoInfo("single instance模式", new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        MasterPage.startPage(getActivity(), DemoFragment.class, bundle, TmplSingleInstanceActivity.class);
                    }
                }),
                new DemoInfo("自定义模板Activity", new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        MasterPage.startPage(getActivity(), DemoFragment.class, bundle, CustomTmplActivity.class);
                    }
                }),
                new DemoInfo("------------", null),
                new DemoInfo("schema访问--standard模式", new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        startBrowser(getActivity(), "ayo://page/standard?page=org.ayo.component.sample.master.DemoFragment&haha=77880");
                    }
                }),
                new DemoInfo("schema访问--single task模式", new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        startBrowser(getActivity(), "ayo://page/singletask?page=org.ayo.component.sample.master.DemoFragment&haha=7ddg80");
                    }
                }),
                new DemoInfo("schema访问--single top模式", new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        startBrowser(getActivity(), "ayo://page/singletop?page=org.ayo.component.sample.master.DemoFragment&haha=4d880");
                    }
                }),
                new DemoInfo("schema访问--single instance模式", new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        startBrowser(getActivity(), "ayo://page/singleinstance?page=org.ayo.component.sample.master.DemoFragment&haha=sdfg3");
                    }
                }),
                new DemoInfo("schema访问--自定义模板Activity", new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        startBrowser(getActivity(), "ayo://page/custom1?page=org.ayo.component.sample.master.DemoFragment&haha=ccee1");
                    }
                }),
        };
    }

    public static void startBrowser(Context c, String url){
        if(!isValidUri(c, url)){
            ToasterDebug.toastShort("找不到对应的Activity：" + url);
            return;
        }else{
            ToasterDebug.toastShort(url);
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        if(c instanceof Activity){
            c.startActivity(intent);
        }else{
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            c.startActivity(intent);
        }
    }

    public static boolean isValidUri(Context c, String uri){
        PackageManager packageManager = c.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
        boolean isValid = !activities.isEmpty();
        return isValid;
    }
}
