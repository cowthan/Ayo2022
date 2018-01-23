package com.app.core;

import android.content.SharedPreferences;

import com.app.core.model.UserModel;

import org.ayo.AppCore;
import org.ayo.UserCenter;

/**
 * 用户管理
 * Created by Mr.LongFace on 2017/7/28.
 */
public class UserManager {

    private static UserInfo userInfo;

    private static UserModel currentUser;

    public static UserModel currentUser(){
        if(currentUser == null){
            currentUser = UserCenter.getDefault().getCurrentUser(UserModel.class);
        }
        return currentUser;
    }

    public static String currentUserId(){
        if(currentUser == null){
            currentUser = UserCenter.getDefault().getCurrentUser(UserModel.class);
        }
        return currentUser == null ? "" : currentUser.user_id;
    }

    public static String currentUserToken(){
        if(currentUser == null){
            currentUser = UserCenter.getDefault().getCurrentUser(UserModel.class);
        }
        return currentUser == null ? "" : currentUser.token;
    }

    public static void refreshUserInfo(){
        currentUser = null;
    }

    /**
     * 获取用户信息
     * @return
     */
    public static UserInfo getUserInfo(){

        UserModel user = UserCenter.getDefault().getCurrentUser(UserModel.class);

        userInfo = new UserInfo();
        if(user != null){
            userInfo.userID = user.user_id;
            userInfo.userHead = user.avatar;
            userInfo.userName = user.name;
            userInfo.token = user.token;
        }else{
            userInfo.userID = "";
            userInfo.userHead = "";
            userInfo.userName = "";
            userInfo.token = "";
        }


//        if (userInfo == null) {
//            SharedPreferences vwindow = App.AppContext.getSharedPreferences("vwindowUser", 0);
//            userInfo = new UserInfo();
//            userInfo.userID = vwindow.getString("userID" , "");
//            userInfo.userHead = vwindow.getString("userHead" , "");
//            userInfo.userName = vwindow.getString("userName" , "");
//        }
        return userInfo;
    }
    // 保存用户信息
    public static void saveUserInfo(String userID , String userHead , String userName){
        SharedPreferences vwindow = AppCore.app().getSharedPreferences("vwindowUser", 0);
        if (userID != null){
            vwindow.edit().putString("userID" , userID).apply();
        }
        if (userHead != null){
            vwindow.edit().putString("userHead" , userHead).apply();
        }
        if (userName != null) {
            vwindow.edit().putString("userName" , userName).apply();
        }
    }
    // 清除用户信息
    public static void cleanUserInfo(){
        SharedPreferences vwindow = AppCore.app().getSharedPreferences("vwindowUser", 0);
        vwindow.edit().putString("userID" , "").apply();
        vwindow.edit().putString("userHead" , "").apply();
        vwindow.edit().putString("userName" , "").apply();
        userInfo = null;
        currentUser = null;
        setAutoLogin(false);
    }

    /**
     * 是否自动登录
     */
    public static boolean isAutoLogin(){
        SharedPreferences vwindow = AppCore.app().getSharedPreferences("vwindowUser", 0);
        return vwindow.getBoolean("autoLogin" , false);
    }
    public static void setAutoLogin(boolean login){
        SharedPreferences vwindow = AppCore.app().getSharedPreferences("vwindowUser", 0);
        vwindow.edit().putBoolean("autoLogin" , login).apply();
    }

    /**
     * 自定义的用户信息
     */
    public static class UserInfo{
        public String userID = "";
        public String token = "";
        public String userHead = "";
        public String userName = "";
    }
}
