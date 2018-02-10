package org.ayo.component;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;

import org.parceler.Parcels;


/**
 * 真正的Activity，也可以通过schema打开，不需要传page了
 * ayo://tmpl-base-activity-standard?id=1
 *
 */

public abstract class MasterActivity extends me.yokeyword.fragmentation.SupportActivity {

    private View _contentView;
    private Bundle bundle;
//    private MasterDelegate agent;
    private boolean isFirstVisible = true;

    protected abstract int getLayoutId();

    protected <T> T fid(int id){
        return (T) findViewById(id);
    }


    protected View getLayoutView(){
        return null;
    }

    protected abstract void onCreate2(View contentView, @Nullable Bundle savedInstanceState);
    protected abstract void onDestroy2();
    protected abstract void onPageVisibleChanged(boolean visible, boolean isFirstTimeVisible, @Nullable Bundle savedInstanceState);


//    public MasterDelegate getAgent(){
//        return agent;
//    }

//    @Override
//    public boolean isSwipebackEnabled() {
//        return true;
//    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        agent = new MasterDelegate();
//        agent.attach(this);
        View contentView = null;
        if(getLayoutId() == 0){
            contentView = getLayoutView();
        }else{
            contentView = View.inflate(this, getLayoutId(), null);
        }
        setContentView(contentView);

        if(savedInstanceState != null && savedInstanceState.containsKey("__state__model__")){
            Parcelable p = savedInstanceState.getParcelable("__state__model__");
            if(p != null) stateModel = Parcels.unwrap(p);
        }else{
            stateModel = createStateModel();
        }
        onCreate2(contentView, savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        onPageVisibleChanged(true, isFirstVisible, null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        onPageVisibleChanged(true, isFirstVisible, null);
        isFirstVisible = false;
    }

    @Override
    protected void onDestroy() {
        onDestroy2();
        super.onDestroy();
    }

    public <T> T id(int id){
        return (T) _contentView.findViewById(id);
    }

    public Activity getActivity(){
        return this;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.e("activity---栈", getClass().getSimpleName() + " onNewIntent");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("activity---栈", getClass().getSimpleName() + " onRestart");
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        Log.e("activity---栈", getClass().getSimpleName() + " onTrimMemory--" + level);
    }

    // getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);时触发
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            Log.e("activity---栈", getClass().getSimpleName() + " onConfigurationChanged--横向");
        } else {
            Log.e("activity---栈", getClass().getSimpleName() + " onConfigurationChanged--纵向");
        }

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.e("activity---栈", getClass().getSimpleName() + " onRestoreInstanceState");
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        StateModel model = getStateModel();
        if(model != null){
            Parcelable wrapped = Parcels.wrap(model);
            outState.putParcelable("__state__model__", wrapped);
        }
    }

    private StateModel stateModel;
    protected StateModel getStateModel(){
        return stateModel;
    }

    protected StateModel createStateModel(){
        return null;
    }

    protected MasterActivity  getMyself(){
        return this;
    }

}
