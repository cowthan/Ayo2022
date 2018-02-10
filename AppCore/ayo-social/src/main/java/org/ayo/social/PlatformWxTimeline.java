package org.ayo.social;

import android.app.Activity;
import android.content.Context;

import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;

import org.ayo.core.Lang;
import org.ayo.log.Trace;
import org.ayo.social.callback.FetchUserInfoCallback;
import org.ayo.social.model.ShareArticle;
import org.ayo.social.utils.SocialUtils;

/**
 * Created by qiaoliang on 2017/6/20.
 */

public class PlatformWxTimeline extends PlatformWechatAll {

    private PlatformWxTimeline(){}
    private static final class H{
        private static final PlatformWxTimeline instance = new PlatformWxTimeline();
    }
    public static PlatformWxTimeline getDefault(){
        return H.instance;
    }


    @Override
    public void init(Context context) {

    }

    @Override
    public void shareArticle(Activity context, ShareArticle article) {
        shareArticle(context, article, SendMessageToWX.Req.WXSceneTimeline);
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

    }

    @Override
    public void getUserInfo(FetchUserInfoCallback callback) {
        throw new RuntimeException("没法通过朋友圈登录");
    }


}
