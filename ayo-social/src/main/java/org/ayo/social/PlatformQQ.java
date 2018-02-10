package org.ayo.social;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.tencent.connect.UserInfo;
import com.tencent.connect.share.QQShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.ayo.AppCore;
import org.ayo.core.Lang;
import org.ayo.log.Trace;
import org.ayo.social.callback.FetchUserInfoCallback;
import org.ayo.social.model.ShareArticle;
import org.ayo.social.model.SocialAccountInfo;
import org.ayo.social.model.SocialUserInfo;
import org.ayo.social.utils.SocialUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by qiaoliang on 2017/6/20.
 */

public class PlatformQQ extends SocialPlatform {

    private PlatformQQ(){}
    private static final class H{
        private static final PlatformQQ instance = new PlatformQQ();
    }
    public static PlatformQQ getDefault(){
        return H.instance;
    }


    @Override
    public void init(Context context) {

    }

    @Override
    public void shareArticle(Activity context, ShareArticle article) {
        Tencent tencent = Tencent.createInstance(SocialCenter.getDefault().appIdQQ, context);
        final Bundle params = new Bundle();

        params.putString(QQShare.SHARE_TO_QQ_TITLE, article.title);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, article.desc);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, article.redirectUrl);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, context.getPackageName());
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,
                Lang.isNotEmpty(article.imageUrl) ? article.imageUrl : SocialUtils.backupImageUrl);
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        SocialCenter.getDefault().setQQCallbackInfo(tencent, iUiListener);
        tencent.shareToQQ(context, params, iUiListener);
    }

    @Override
    public void shareImage(final Activity context, final String path) {
        if(path.startsWith("http")){
            SocialUtils.getImageFromUrl(context, path, new SocialUtils.OnDownloadCallback() {
                @Override
                public void onFinish(String path2) {
                    if(Lang.isNotEmpty(path2)){
                        shareImageLocal(context, path2);
                    }else{
                        Trace.e("share", "无效图片地址：" + path);
                    }
                }
            });
        }else{
            shareImageLocal(context, path);
        }
    }

    public void shareImageLocal(Activity context, String path) {
        if(path.startsWith("http")){
            Trace.e("share", "只能分享本地图片啊，现在是：" + path);
            return;
        }
        Tencent tencent = Tencent.createInstance(SocialCenter.getDefault().appIdQQ, context);
        final Bundle params = new Bundle();

        params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, path);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, context.getPackageName());
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);

        SocialCenter.getDefault().setQQCallbackInfo(tencent, iUiListener);
        tencent.shareToQQ(context, params, iUiListener);
    }

    /**  */
    private IUiListener iUiListener = new IUiListener() {
        @Override
        public void onComplete(Object o) {
            if(SocialCenter.getDefault().getShareCallback() != null){
                SocialCenter.getDefault().getShareCallback().onSuccess();
                SocialCenter.getDefault().setShareCallback(null);
                SocialCenter.getDefault().setQQCallbackInfo(null, null);
            }
        }

        @Override
        public void onError(UiError uiError) {
            if(SocialCenter.getDefault().getShareCallback() != null){
                Trace.e("social", "出错：" + uiError.errorCode + ", " + uiError.errorMessage);
                SocialCenter.getDefault().setShareCallback(null);
                SocialCenter.getDefault().setQQCallbackInfo(null, null);
            }
        }

        @Override
        public void onCancel() {
            if(SocialCenter.getDefault().getShareCallback() != null){
                SocialCenter.getDefault().getShareCallback().onCancel();
                SocialCenter.getDefault().setShareCallback(null);
                SocialCenter.getDefault().setQQCallbackInfo(null, null);
            }
        }
    };

    @Override
    public void login(Activity context) {
        SocialUtils.logoutSina(context);
        Tencent mTencent = Tencent.createInstance(SocialCenter.getDefault().appIdQQ, context);
        mTencent.logout(context);
        LoginUiListener uiListener = new LoginUiListener();
        mTencent.login(context, "get_simple_userinfo", uiListener);
        SocialCenter.getDefault().setQQCallbackInfo(mTencent, uiListener);
    }

    public void getUserInfo(FetchUserInfoCallback callback){
        Tencent mTencent = Tencent.createInstance(SocialCenter.getDefault().appIdQQ, AppCore.app());
        if (mTencent.isSessionValid()) {
            UserInfo userInfo = new UserInfo(AppCore.app(),mTencent.getQQToken());
            userInfo.getUserInfo(new FetchUserInfoUiListener(mTencent, callback));
        }
    }


    private class LoginUiListener implements IUiListener {

        @Override
        public void onComplete(Object o) {
            if (o instanceof JSONObject) {
                JSONObject value = (JSONObject) o;
                try {
                    SocialAccountInfo info = new SocialAccountInfo();
                    info.platform = SocialCenter.PLATFORM_QQ;
                    if (value.getString("access_token") != null) {
                        info.accessToken = (value.getString("access_token"));
                    } else if (value.getString("access_key") != null) {
                        info.accessToken = (value.getString("access_key"));
                    }
                    info.openid = (value.getString("openid"));
                    info.expiresIn = value.getString("expires_in");
                    SocialCenter.getDefault().setAccountInfo(info);
                    Tencent tencent = Tencent.createInstance(SocialCenter.getDefault().appIdQQ, AppCore.app());
                    tencent.setOpenId(info.openid);
                    tencent.setAccessToken(info.accessToken, info.expiresIn);
                    if(SocialCenter.getDefault().getShareCallback() != null){
                        SocialCenter.getDefault().getShareCallback().onLoginSuccess(info);
                        SocialCenter.getDefault().setShareCallback(null);
                        SocialCenter.getDefault().setQQCallbackInfo(null, null);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    if(SocialCenter.getDefault().getShareCallback() != null){
                        SocialCenter.getDefault().getShareCallback().onFail(e, e.getMessage());
                        SocialCenter.getDefault().setShareCallback(null);
                        SocialCenter.getDefault().setQQCallbackInfo(null, null);
                    }
                }
            } else {
                Trace.e("social", "QQ登录返回的不是JSONObject啊");
            }
        }

        @Override
        public void onError(UiError e) {
            if(SocialCenter.getDefault().getShareCallback() != null){
                Trace.e("social", "出错：" + e.errorCode + ", " + e.errorMessage);
                SocialCenter.getDefault().getShareCallback().onFail(null, e.errorDetail);
                SocialCenter.getDefault().setShareCallback(null);
                SocialCenter.getDefault().setQQCallbackInfo(null, null);
            }
        }

        @Override
        public void onCancel() {
            if(SocialCenter.getDefault().getShareCallback() != null){
                SocialCenter.getDefault().getShareCallback().onCancel();
                SocialCenter.getDefault().setShareCallback(null);
                SocialCenter.getDefault().setQQCallbackInfo(null, null);
            }
        }
    }

    /**
     * 获取用户信息的回调
     */
    private class FetchUserInfoUiListener implements IUiListener {

        private FetchUserInfoCallback callback;
        private Tencent tencent;

        public FetchUserInfoUiListener(Tencent t, FetchUserInfoCallback c){
            this.callback = c;
            tencent = t;
        }

        @Override
        public void onComplete(Object o) {
            JSONObject object = (JSONObject) o;
            try {
                int ret = object.getInt("ret");
                if (ret == 0) {
                    SocialUserInfo u = new SocialUserInfo();
                    u.nickname = object.getString("nickname");
                    u.portrait = object.getString("figureurl_qq_2");
                    u.openid = tencent.getOpenId();
                    callback.onFinish(u);
                } else {
                    callback.onFinish(null);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                callback.onFinish(null);
            }
        }

        @Override
        public void onError(UiError uiError) {
            callback.onFinish(null);
        }

        @Override
        public void onCancel() {
            callback.onFinish(null);
        }
    }

}
