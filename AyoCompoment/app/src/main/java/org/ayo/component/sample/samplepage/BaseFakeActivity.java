package org.ayo.component.sample.samplepage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.View;

import org.ayo.component.FragmentFactory;
import org.ayo.component.MasterActivity;
import org.ayo.component.sample.R;
import org.ayo.lang.Lang;

/**
 * Created by Administrator on 2017/5/21.
 */

public abstract class BaseFakeActivity extends MasterActivity {
    public static void start(Activity a, boolean newTask){
        Intent intent = new Intent(a, BaseFakeActivity.class);
        if(newTask) intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        a.startActivity(intent);
    }
    @Override
    protected int getLayoutId() {
        return R.layout.ac_fragment_container;
    }

    protected abstract String getTopTitle();

    @Override
    protected void onCreate2(View contentView, @Nullable Bundle savedInstanceState) {
        Lang.printIntent(getIntent());
        if(savedInstanceState == null){
            Bundle args = new Bundle();
            args.putString("title", Lang.snull(getTopTitle()));
            FakeFragment f = FragmentFactory.getDefault().newFragment(FakeFragment.class, args);
            loadRootFragment(R.id.body, f);
        }else{
            // 如果不做这层判断，fragment会被add两次，会导致：
            // 再配合SupportActivity的后退处理，需要后退两次才能退出Activity了
        }

    }

    @Override
    protected void onDestroy2() {

    }

    @Override
    protected void onPageVisibleChanged(boolean visible, boolean isFirstTimeVisible, @Nullable Bundle savedInstanceState) {

    }


}
