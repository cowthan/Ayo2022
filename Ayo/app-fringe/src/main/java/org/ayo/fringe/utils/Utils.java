package org.ayo.fringe.utils;

import org.ayo.social.model.SocialAccountInfo;
import org.ayo.fringe.App;
import org.ayo.fringe.Config;


/**
 * Created by Administrator on 2016/4/20.
 */
public class Utils {

    public static int getColor(int colorId){
        return App.app.getResources().getColor(colorId);
    }


    /**
     * 获取授权微博用户的token
     * @return
     */
    public static String getWeiboToken(){
        SocialAccountInfo info = Config.weibo.getLoginInfo();
        if(info != null){
            return info.accessToken;
        }else{
            return "";
        }
    }

    /**
     * 获取授权微博用户的uid
     * @return
     */
    public static String getCurrentWeiboUserUid(){
        SocialAccountInfo info = Config.weibo.getLoginInfo();
        if(info != null){
            return info.openid;
        }else{
            return "";
        }
    }

}
