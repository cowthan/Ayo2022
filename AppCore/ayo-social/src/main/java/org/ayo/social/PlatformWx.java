package org.ayo.social;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.ayo.AppCore;
import org.ayo.core.HttpTools;
import org.ayo.core.Lang;
import org.ayo.log.Trace;
import org.ayo.social.callback.FetchUserInfoCallback;
import org.ayo.social.model.ShareArticle;
import org.ayo.social.model.SocialAccountInfo;
import org.ayo.social.model.SocialUserInfo;
import org.ayo.social.utils.SocialUtils;
import org.ayo.thread.Task;
import org.ayo.thread.ThreadManager;
import org.json.JSONObject;

/**
 * Created by qiaoliang on 2017/6/20.
 */

public class PlatformWx extends PlatformWechatAll {

    private PlatformWx(){}
    private static final class H{
        private static final PlatformWx instance = new PlatformWx();
    }
    public static PlatformWx getDefault(){
        return H.instance;
    }


    @Override
    public void init(Context context) {

    }

    @Override
    public void shareArticle(Activity context, ShareArticle article) {
        shareArticle(context, article, SendMessageToWX.Req.WXSceneSession);
    }

    @Override
    public void shareImage(final Activity context, final String path) {
        if(path.startsWith("http")){
            SocialUtils.getImageFromUrl(context, path, new SocialUtils.OnDownloadCallback() {
                @Override
                public void onFinish(String path2) {
                    if(Lang.isNotEmpty(path2)){
                        shareImage(context, path2, SendMessageToWX.Req.WXSceneSession);
                    }else{
                        Trace.e("share", "无效图片地址：" + path);
                        //Toaster.toastShort("获取图片失败");
                    }
                }
            });
        }else{
            shareImage(context, path, SendMessageToWX.Req.WXSceneSession);
        }
    }

    @Override
    public void login(Activity context) {
        IWXAPI iwxapi = WXAPIFactory.createWXAPI(context, SocialCenter.getDefault().appIdWx);
        if(!iwxapi.isWXAppInstalled()){
            if(SocialCenter.getDefault().getShareCallback() != null){
                SocialCenter.getDefault().getShareCallback().onFail(null, "微信未安装");
                SocialCenter.getDefault().setShareCallback(null);
                SocialCenter.getDefault().setQQCallbackInfo(null, null);
            }
            return;
        }
        SendAuth.Req req = new SendAuth.Req();
        req.scope = SocialCenter.getDefault().wxScope;
        req.state = SocialCenter.getDefault().wxState + System.currentTimeMillis();
        iwxapi.sendReq(req);
    }

    @Override
    public void getUserInfo(final FetchUserInfoCallback callback) {
        final SocialAccountInfo info = SocialCenter.getDefault().getSocialAccountInfo();
        if(info == null){
            callback.onFinish(null);
            return;
        }

        ThreadManager.getDefault().runOnAsyncThread(AppCore.app(), new Task(AppCore.app()) {
            @Override
            public Object run() {
                String url = "https://api.weixin.qq.com/sns/userinfo?";
                url += "access_token=" + info.accessToken + "&";
                url += "openid=" + info.openid;
                final Handler mainHandler = new Handler(Looper.getMainLooper());
                try {
                    String json = HttpTools.get(url, null, null);

                    JSONObject object = new JSONObject(json.trim().replace("/n", ""));
                    try {
                        final SocialUserInfo u = new SocialUserInfo();
                        u.nickname = object.getString("nickname");
                        u.portrait = object.getString("headimgurl");
                        u.openid = info.openid;

                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onFinish(u);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onFinish(null);
                            }
                        });
                    }

                }catch (final Exception e){
                    e.printStackTrace();
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onFinish(null);
                        }
                    });

                }
                return null;
            }
        });
    }


}
