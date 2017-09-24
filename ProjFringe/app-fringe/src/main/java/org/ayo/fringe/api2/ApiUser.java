package org.ayo.fringe.api2;

import org.ayo.http.callback.BaseHttpCallback;
import org.ayo.http.converter.TypeToken;
import org.ayo.fringe.Config;
import org.ayo.fringe.model.LoginUserInfo;

/**
 * Created by Administrator on 2016/4/21.
 */
public class ApiUser {


    /**
     * 获取当前登录用户的信息
     * @param flag
     * @param access_token
     * @param uid
     * @param screen_name
     * @param callback
     */
    public static void getLoginUserInfo(String flag,
                                          String access_token,
                                          String uid,
                                          String screen_name,
                                          BaseHttpCallback<LoginUserInfo> callback) {
        WeiboApi.getAyoRequest().tag(flag)
                .url(Config.API.HOST_WEIBO + "users/show.json")
                .actionGet()
                .param("access_token", access_token)
                .param("uid", uid)
                //.param("screen_name", screen_name)
                .callback(callback, new TypeToken<LoginUserInfo>(){})
                .fire();
    }
}
