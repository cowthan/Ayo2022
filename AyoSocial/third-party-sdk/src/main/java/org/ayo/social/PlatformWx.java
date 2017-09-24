package org.ayo.social;

import android.app.Activity;
import android.content.Context;

import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.ayo.core.Lang;
import org.ayo.log.Trace;
import org.ayo.social.model.ShareArticle;
import org.ayo.social.utils.SocialUtils;

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



}
