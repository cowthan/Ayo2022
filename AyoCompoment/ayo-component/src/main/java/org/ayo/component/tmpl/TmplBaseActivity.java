package org.ayo.component.tmpl;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import org.ayo.component.FragmentFactory;
import org.ayo.component.MasterActivity;
import org.ayo.component.MasterFragment;
import org.ayo.component.R;
import org.ayo.component.StateModel;

import java.util.Set;

/**
 * 作为模板的Activity
 */

public abstract class TmplBaseActivity extends MasterActivity {

    private String fragmentClassName;
    private Bundle fragmentBundle;
    private MasterFragment fragment;

    @Override
    protected int getLayoutId() {
        return R.layout.ayo_compo_fragment_container;
    }

    @Override
    protected StateModel createStateModel() {
        return null;
    }

    @Override
    protected void onCreate2(View contentView, @Nullable Bundle savedInstanceState) {
        if(savedInstanceState == null){
            if(getIntent() != null){
                if(getIntent().hasExtra("page")){
                    fragmentClassName = getIntent().getStringExtra("page");
                    fragmentBundle = getIntent().getBundleExtra("data");
                    Log.e("FragmentFactory", fragmentClassName + ", " + fragmentBundle);
                    fragment = FragmentFactory.getDefault().newFragment(fragmentClassName, fragmentBundle);
                    loadRootFragment(R.id.ayo_compo_fragment_container, fragment);
                    //getSupportFragmentManager().beginTransaction().add(R.id.ayo_compo_fragment_container, fragment).commitAllowingStateLoss();
                }else{
                    ///可能是从schema启动的
                    parseSchema();
                    fragment = FragmentFactory.getDefault().newFragment(fragmentClassName, fragmentBundle);
                    Log.e("FragmentFactory", fragmentClassName + ", " + fragmentBundle);
                    loadRootFragment(R.id.ayo_compo_fragment_container, fragment);
                    //getSupportFragmentManager().beginTransaction().add(R.id.ayo_compo_fragment_container, fragment).commitAllowingStateLoss();
                }
            }else{
                throw new RuntimeException("无论是普通方式，还是Schema方式，都得有个intent");
            }
        }else{
            //内存重启
            //fragment已经自动保存和恢复了，包括给的bundle
        }
    }

    @Override
    protected void onDestroy2() {

    }

    @Override
    protected void onPageVisibleChanged(boolean visible, boolean isFirstTimeVisible, @Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        fragment.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    protected void parseSchema(){
        //ayo://tmpl-base-activity-standard?page=com.aa.bb.DetailFragment&id=1
        String tSchema = getIntent().getScheme(); //ayo
        Uri myURI = getIntent().getData(); //
        fragmentBundle = new Bundle();
        if(myURI != null){
            Set<String> names = myURI.getQueryParameterNames();
            for(String name: names){
                if(name.equals("page")){
                    fragmentClassName = myURI.getQueryParameter(name);
                }else{
                    fragmentBundle.putString(name, myURI.getQueryParameter(name));
                }
            }
        }
        if(fragmentClassName == null){
            throw new RuntimeException("没有有效的Fragment");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        String s = "模板Activity--来了->" + getClass().getSimpleName();
        Log.i("MainActivity", s);
    }

    @Override
    protected void onPause() {
        String s = "模板Activity--走了->" + getClass().getSimpleName();
        Log.i("MainActivity", s);
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
