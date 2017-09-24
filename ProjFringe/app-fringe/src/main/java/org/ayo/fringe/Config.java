package org.ayo.fringe;


import org.ayo.social.model.SocialAccountInfo;
import org.ayo.sp.UserDefault;

/**
 * Created by Administrator on 2016/4/21.
 */
public class Config {


    public static class API{
        public static final boolean USE_RETROFIT = false;
        public static final String HOST_WEIBO = "https://api.weibo.com/2/";
    }


    public static class DIR{
        public static final String APP_DIR = "app";
        public static final String USER_DIR = "user";
    }

    public static class weibo{
        public static void saveLoginInfo(SocialAccountInfo accountInfo){
            UserDefault.putObject("weibo-info", accountInfo);
        }

        public static SocialAccountInfo getLoginInfo(){
            return UserDefault.getObject("weibo-info", SocialAccountInfo.class);
        }
    }

}
