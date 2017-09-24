package org.ayo.social;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.utils.Utility;

import org.ayo.core.Lang;
import org.ayo.log.Trace;
import org.ayo.social.model.ShareArticle;
import org.ayo.social.model.SocialAccountInfo;
import org.ayo.social.utils.SocialUtils;

/**
 * Created by qiaoliang on 2017/6/20.
 */

public class PlatformSinaWeibo extends SocialPlatform {

    private PlatformSinaWeibo(){}
    private static final class H{
        private static final PlatformSinaWeibo instance = new PlatformSinaWeibo();
    }
    public static PlatformSinaWeibo getDefault(){
        return H.instance;
    }

    @Override
    public void init(Context context) {

    }

    @Override
    public void shareArticle(final Activity context, final ShareArticle article) {

        SocialUtils.getImageFromUrl(context, article.imageUrl, new SocialUtils.OnDownloadCallback() {
            @Override
            public void onFinish(String path) {
                if(Lang.isNotEmpty(path)){
                    WeiboMultiMessage msg = new WeiboMultiMessage();
                    String identify = Utility.generateGUID();
                    TextObject textObject = new TextObject();
//        String text = article.desc;
//        if (text.length() > 100)
//            text = text.substring(0, 99) + "...";
//        textObject.text = text + " " + mUrl + getString(R.string.share_from_at);
                    textObject.text = article.desc;
                    textObject.actionUrl = article.redirectUrl;

//        (!TextUtils.isEmpty(mTitle) && mTitle.length() > 30) ? mTitle
//                .substring(0, 29) : mTitle
                    textObject.title = article.title;
                    textObject.identify = identify;

                    Bitmap bitmap = SocialUtils.getBitmapFromPath(path);
                    textObject.setThumbImage(bitmap);
                    msg.textObject = textObject;

                    ImageObject imageObject = new ImageObject();
                    imageObject.setImageObject(bitmap);
                    imageObject.imagePath = path;
                    imageObject.identify = identify;
                    msg.imageObject = imageObject;


                    // 2. 初始化从第三方到微博的消息请求
                    SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
                    // 用transaction唯一标识一个请求
                    request.transaction = String.valueOf(System.currentTimeMillis());
                    request.multiMessage = msg;
                    // mWeibo.registerApp();
                    // 3. 发送请求消息到微博，唤起微博分享界面
                    IWeiboShareAPI mWeibo = WeiboShareSDK.createWeiboAPI(context, SocialCenter.getDefault().appIdWb);
                    mWeibo.registerApp();
                    mWeibo.sendRequest(context, request);
                }else{
                    //Toaster.toastShort("分享失败，图片url无法获取");
                }
            }
        });


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
                        //Toaster.toastShort("获取图片失败");
                    }
                }
            });
        }else{
            shareImageLocal(context, path);
        }
    }

    private void shareImageLocal(Activity context, String path) {
        Trace.e("share", "微博分享，图片地址：" + path);
        WeiboMultiMessage msg = new WeiboMultiMessage();
        String identify = Utility.generateGUID();
        ImageObject imageObject = new ImageObject();
        imageObject.imagePath = path;
        imageObject.identify = identify;
        msg.imageObject = imageObject;

        // 2. 初始化从第三方到微博的消息请求
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        // 用transaction唯一标识一个请求
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.multiMessage = msg;
        // mWeibo.registerApp();
        // 3. 发送请求消息到微博，唤起微博分享界面
        IWeiboShareAPI mWeibo = WeiboShareSDK.createWeiboAPI(context, SocialCenter.getDefault().appIdWb);
        mWeibo.registerApp();
        mWeibo.sendRequest(context, request);
    }

    @Override
    public void login(Activity context) {
        IWeiboShareAPI mWeibo = WeiboShareSDK.createWeiboAPI(context, SocialCenter.getDefault().appIdWb);
        mWeibo.registerApp();
        SocialUtils.logoutSina(context);
        AuthInfo mWeiboAuth = new AuthInfo(context,
                SocialCenter.getDefault().appIdWb,
                SocialCenter.getDefault().wbRedirectUrl,
                SocialCenter.getDefault().wbScope);
        SsoHandler mSsoHandler = new SsoHandler(context, mWeiboAuth);
        mSsoHandler.authorize(new AuthListener());
        SocialCenter.getDefault().setWbCallbackInfo(mSsoHandler);
    }

    class AuthListener implements WeiboAuthListener {

        @Override
        public void onComplete(Bundle value) {

            SocialAccountInfo info = new SocialAccountInfo();
            info.platform = SocialCenter.PLATFORM_WB;
            if (value.getString("access_token") != null) {
                info.accessToken = (value.getString("access_token"));
            } else if (value.getString("access_key") != null) {
                info.accessToken = (value.getString("access_key"));
            }
            info.openid = "";
            info.expiresIn = "";

            if(SocialCenter.getDefault().getShareCallback() != null){
                SocialCenter.getDefault().getShareCallback().onLoginSuccess(info);
                SocialCenter.getDefault().setShareCallback(null);
                SocialCenter.getDefault().setWbCallbackInfo(null);
            }
        }

        @Override
        public void onCancel() {
            if(SocialCenter.getDefault().getShareCallback() != null){
                SocialCenter.getDefault().getShareCallback().onCancel();
                SocialCenter.getDefault().setShareCallback(null);
                SocialCenter.getDefault().setWbCallbackInfo(null);
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            if(SocialCenter.getDefault().getShareCallback() != null){
                SocialCenter.getDefault().getShareCallback().onFail(e, e.getMessage());
                SocialCenter.getDefault().setShareCallback(null);
                SocialCenter.getDefault().setWbCallbackInfo(null);
            }
        }
    }
}
