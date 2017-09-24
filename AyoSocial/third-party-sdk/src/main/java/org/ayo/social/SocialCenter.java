package org.ayo.social;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.SparseArray;

import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;

import org.ayo.social.callback.ShareCallback;
import org.ayo.social.model.ShareArticle;


/**
 * Created by qiaoliang on 2017/6/21.
 *
 * 社交中心
 */

public class SocialCenter {



    private SocialCenter(){}
    private static final class H{
        private static final SocialCenter instance = new SocialCenter();
    }
    public static SocialCenter getDefault(){
        return H.instance;
    }


    public static final int PLATFORM_WB = 1;
    public static final int PLATFORM_QQ = 2;
    public static final int PLATFORM_QZONG = 3;
    public static final int PLATFORM_WX_SESSION = 4;
    public static final int PLATFORM_WX_TIMELINE = 5;

    private static SparseArray<String> platformNames = new SparseArray<>();
    static{
        platformNames.put(PLATFORM_WB, "weibo");
        platformNames.put(PLATFORM_QQ, "qq");
        platformNames.put(PLATFORM_QZONG, "qzong");
        platformNames.put(PLATFORM_WX_SESSION, "wechat");
        platformNames.put(PLATFORM_WX_TIMELINE, "moment");
    }

    public String getPlatformName(int platform){
        return platformNames.get(platform) == null ? "" : platformNames.get(platform);
    }

    //================================================
    // 管理平台注册信息
    //================================================
    public String appIdQQ;
    public String appIdWx;
    public String wxSecret;
    public String wxScope;
    public String wxState;

    public String appIdWb;
    public String wbRedirectUrl;
    public String wbScope;

    public void setAppIdQQ(String appid){
        appIdQQ = appid;
    }


    public void setAppIdWx(String appid){
        appIdWx = appid;
    }

    public void setPlatformInfoWx(String secret, String state, String scope){
        this.wxSecret = secret;
        this.wxState = state;
        this.wxScope = scope;
    }


    public void setAppIdWb(String appid){
        appIdWb = appid;
    }

    public void setPlatformInfoWb(String redirectUrl, String scope){
        this.wbRedirectUrl = redirectUrl;
        this.wbScope = scope;
    }

    //================================================
    // 管理分享上下文信息
    //================================================
    /** 分享的上下文信息，便于统计，总是会被下一次分享覆盖 */
    public static class ShareContextInfo{
        public int platform;
        public String where;
        public String what;
        public Object extra;
    }

    private ShareContextInfo currentShareInfo;

    public void setShareInfo(ShareContextInfo shareInfo){
        this.currentShareInfo = shareInfo;
    }

    public ShareContextInfo getShareInfo(){
        return currentShareInfo;
    }

    //================================================
    // 管理分享回调信息
    //================================================
    private ShareCallback shareCallback;

    public void setShareCallback(ShareCallback callback){
        this.shareCallback = callback;
    }

    public ShareCallback getShareCallback(){
        return shareCallback;
    }

    ///QQ回调单独处理
    private Tencent tencent;
    private IUiListener mQQIUListener;
    private SsoHandler ssoHandler;
    public void setQQCallbackInfo(Tencent tencent, IUiListener iUiListener){
        this.tencent = tencent;
        this.mQQIUListener = iUiListener;
    }

    public void setWbCallbackInfo(SsoHandler ssoHandler){
        this.ssoHandler = ssoHandler;
    }

    public void tryToGetQQCallback(int requestCode, int resultCode, Intent data){
        if (tencent != null && mQQIUListener != null){
            tencent.onActivityResultData(requestCode, resultCode, data, mQQIUListener);
        }
    }

    public void tryToGetWbCallback(int requestCode, int resultCode, Intent data){
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    //================================================
    // 管理分享行为
    //================================================
    public void shareArticle(int platform, Activity context, ShareArticle article, ShareCallback callback){

        SocialPlatform pt = getPlatform(platform);
        if(pt == null){
            //Toaster.toastShort("不支持的社交平台");
        }else{
            setShareCallback(callback);
            pt.shareArticle(context, article);
        }


    }
    public void shareImage(int platform, Activity context, String path, ShareCallback callback){
        SocialPlatform pt = getPlatform(platform);
        if(pt == null){
            //Toaster.toastShort("不支持的社交平台");
        }else{
            setShareCallback(callback);
            pt.shareImage(context, path);
        }
    }

    public void login(int platform, Activity context, ShareCallback callback){
        SocialPlatform pt = getPlatform(platform);
        if(pt == null){
            //Toaster.toastShort("不支持的社交平台");
        }else{
            setShareCallback(callback);
            pt.login(context);
        }
    }

    private SocialPlatform getPlatform(int platform){
        SocialPlatform pt = null;
        if(platform == PLATFORM_WB){
            pt = PlatformSinaWeibo.getDefault();
        }else if(platform == PLATFORM_WX_SESSION){
            pt = PlatformWx.getDefault();
        }else if(platform == PLATFORM_WX_TIMELINE){
            pt = PlatformWxTimeline.getDefault();
        }else if(platform == PLATFORM_QQ){
            pt = PlatformQQ.getDefault();
        }else if(platform == PLATFORM_QZONG){
            pt = PlatformQzong.getDefault();
        }else{

        }
        return pt;
    }

    //================================================
    // 微信登录，抓取用户info
    //================================================
    public void fetchWechatUserInfo(Context context, String code){
        String path = "https://api.weixin.qq.com/sns/oauth2/access_token?" + "appid="
                + appIdWx + "&secret=" + wxSecret + "&code=" + code
                + "&grant_type=authorization_code";

//        GsonRequest<WeiChatAccessTokenEntity> gsonRequest = new GsonRequest<WeiChatAccessTokenEntity>(
//                path, WeiChatAccessTokenEntity.class, null,
//                new Response.Listener<WeiChatAccessTokenEntity>() {
//                    @Override
//                    public void onResponse(WeiChatAccessTokenEntity response) {
//                        if (response != null) {
//                            EventBus.getDefault()
//                                    .post(new WeiChatAuthSuccessEvent(response.getAccess_token(),
//                                            response.getExpires_in(), response.getOpenid()));
//                            if(SocialCenter.getDefault().getShareCallback() != null){
//                                SocialAccountInfo info = new SocialAccountInfo();
//                                info.platform = SocialCenter.PLATFORM_WX_SESSION;
//                                info.accessToken = response.getAccess_token();
//                                info.expiresIn = response.getExpires_in() + "";
//                                info.openid = response.getOpenid();
//                                SocialCenter.getDefault().getShareCallback().onLoginSuccess(info);
//                                SocialCenter.getDefault().setShareCallback(null);
//                            }
//                        }else{
//                            if(SocialCenter.getDefault().getShareCallback() != null){
//                                SocialCenter.getDefault().getShareCallback().onFail(null, "获取用户信息失败");
//                                SocialCenter.getDefault().setShareCallback(null);
//                            }
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                if(SocialCenter.getDefault().getShareCallback() != null){
//                    SocialCenter.getDefault().getShareCallback().onFail(error, error.getMessage());
//                    SocialCenter.getDefault().setShareCallback(null);
//                }
//            }
//        });
//        HttpTools.getDefault().addToRequestQueue(gsonRequest, SocialUtils.REQUEST_TAG);
    }

    public static void cancelRequest(){
        //HttpTools.getDefault().cancelPendingRequests(SocialUtils.REQUEST_TAG);
    }

}
