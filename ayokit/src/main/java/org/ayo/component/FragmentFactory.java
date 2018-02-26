package org.ayo.component;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

/**
 * Created by Administrator on 2017/1/4 0004.
 */

public class FragmentFactory {

    private static final FragmentFactory instance = new FragmentFactory();

    public static FragmentFactory getDefault(){
        return instance;
    }
    private FragmentFactory(){

    }
    public <T extends MasterFragment> T newFragment(Class<T> fragmentClassName, Bundle bundle){

        try {
            Fragment frag = (Fragment) fragmentClassName.newInstance();
            if(frag instanceof MasterFragment){
                if(bundle != null) frag.setArguments(bundle);
                return (T) frag;
            }else{
                Log.e("FragmentFactory", fragmentClassName + "不是MasterFragment子类", new RuntimeException("不是MasterFragment子类"));
            }
        }  catch (InstantiationException e) {
            Log.e("FragmentFactory", "无法创建Fragment，可能没有无参构造方法：" + fragmentClassName, e);
        } catch (IllegalAccessException e) {
            Log.e("FragmentFactory", "无法创建Fragment，可能无权访问构造方法：" + fragmentClassName, e);
        }
        return null;
    }
    public <T extends MasterFragment> T newFragment(String fragmentClassName, Bundle bundle){

        try {
            Class<T> clazz = (Class<T>) Class.forName(fragmentClassName);
            return newFragment(clazz, bundle);
        } catch (ClassNotFoundException e) {
            Log.e("FragmentFactory", "找不到Fragment--" + fragmentClassName, e);
        } catch (Exception e) {
            Log.e("FragmentFactory", "创建Fragment出错--" + fragmentClassName, e);
        }
        return null;
    }
}
