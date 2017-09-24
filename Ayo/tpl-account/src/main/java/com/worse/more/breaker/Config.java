package com.worse.more.breaker;


import org.ayo.sp.UserDefault;

/**
 * Created by Administrator on 2016/7/28.
 */
public class Config {

    /** 开发环境 */
    public static final int APP_ENV_DEV = 1;
    /** beta环境 */
    public static final int APP_ENV_BETA = 2;
    /** 线上环境 */
    public static final int APP_ENV_RELEASE = 3;
    /** 当前环境 */
    public static int APP_ENV = APP_ENV_RELEASE;

    public static boolean useLog = true;

    public static class api{
        public static final String HOST_DEV = "http://chuanyue.iwomedia.cn/";
        public static final String HOST_BETA = "http://api.beta.carguide.com.cn/";
        public static final String HOST_RELEASE = "http://api.daogou.bjzzcb.com/";

        public static String host(){


            if(APP_ENV == APP_ENV_DEV){
                return api.HOST_DEV;
            }else if(APP_ENV == APP_ENV_BETA){
                return api.HOST_BETA;
            }else if(APP_ENV == APP_ENV_RELEASE){
                return api.HOST_RELEASE;
            }else{
                return api.HOST_RELEASE;
            }

        }
    }

    public static class account{

        public static void saveLoginInfo(String username, String pwd){
            UserDefault.getInstance().put("username", username);
            UserDefault.getInstance().put("pwd", pwd);
        }

        public static void saveLoginInfo(String avatarUrl){
            UserDefault.getInstance().put("avatarUrl", avatarUrl);
        }

        public static String getUsername(){
            return UserDefault.getInstance().get("username", "");
        }

        public static String getPassword(){
            return UserDefault.getInstance().get("pwd", "");
        }

        public static String getAvatar(){
            return UserDefault.getInstance().get("avatarUrl", "");
        }

    }




}
