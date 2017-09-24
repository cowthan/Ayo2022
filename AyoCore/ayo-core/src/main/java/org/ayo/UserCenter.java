package org.ayo;

import android.app.Application;

import org.ayo.sp.UserDefault;
import org.ayo.sp.DbSharedPreferences;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qiaoliang on 2017/6/13.
 *
 * 用户管理，
 * 1 所有module统一从这里拿登录用户信息，
 * 2 登录和登出也在这里报备
 * 3 所有module在这里注册自己要在登录和登出时干什么
 */

public class UserCenter {

    private Application app;

    private Object currentUser;

    private UserCenter(){
        app = AppCore.app();
    }

    private static final class H{
        private static final UserCenter instance = new UserCenter();
    }

    public static UserCenter getDefault(){
        return H.instance;
    }

    private final List<OnLoginStatusChangeListener> listeners = new ArrayList<>();

    public interface OnLoginStatusChangeListener{
        void onLogin(Object user);
        void onLogout(Object user);
    }

    public void addLoginStatusListener(OnLoginStatusChangeListener ol){
        if(!listeners.contains(ol)){
            listeners.add(ol);
        }
    }

    public void removeLoginStatusListener(OnLoginStatusChangeListener ol){
        if(listeners.contains(ol)){
            listeners.remove(ol);
        }
    }

    public void notifyLoginOk(Object user){
        setCurrentUser(user);
        for(OnLoginStatusChangeListener listener: listeners){
            listener.onLogin(user);
        }
    }

    public void notifyLogout(Object user){
        clearCurrentUser();
        for(OnLoginStatusChangeListener listener: listeners){
            listener.onLogout(user);
        }
    }

    public <T> T getCurrentUser(Class<T> clazz){
        if(currentUser == null){
            currentUser = UserDefault.getObject("current-user-20179878", clazz);
        }
        return (T) currentUser;
    }

    public boolean isLogin(){
        return DbSharedPreferences.getInstance(AppCore.app()).contains("current-user-20179878");
    }

    private void setCurrentUser(Object user){
        currentUser = user;
        UserDefault.putObject("current-user-20179878", user);
    }

    private void clearCurrentUser(){
        currentUser = null;
        DbSharedPreferences.getInstance(AppCore.app()).edit().remove("current-user-20179878").commit();
    }

}
