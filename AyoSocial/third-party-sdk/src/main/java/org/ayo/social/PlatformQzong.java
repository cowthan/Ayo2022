package org.ayo.social;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.ayo.core.Lang;
import org.ayo.log.Trace;
import org.ayo.social.model.ShareArticle;
import org.ayo.social.utils.SocialUtils;

import java.util.ArrayList;

/**
 * Created by qiaoliang on 2017/6/20.
 */

public class PlatformQzong extends SocialPlatform {

    private PlatformQzong(){}
    private static final class H{
        private static final PlatformQzong instance = new PlatformQzong();
    }
    public static PlatformQzong getDefault(){
        return H.instance;
    }


    @Override
    public void init(Context context) {

    }

    @Override
    public void shareArticle(Activity context, ShareArticle article) {
        Tencent tencent = Tencent.createInstance(SocialCenter.getDefault().appIdQQ, context);
        final Bundle params = new Bundle();
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE,
                QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, article.title);
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, article.desc);
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, article.redirectUrl);
        params.putString(QzoneShare.SHARE_TO_QQ_APP_NAME, context.getPackageName());
        ArrayList<String> imageUrls = new ArrayList<>();
        imageUrls.add(Lang.isNotEmpty(article.imageUrl) ? article.imageUrl : SocialUtils.backupImageUrl);
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);

        SocialCenter.getDefault().setQQCallbackInfo(tencent, iUiListener);
        tencent.shareToQQ(context, params, iUiListener);
    }

    @Override
    public void shareImage(final Activity context, final String path) {
        if(path.startsWith("http")){
            ShareArticle a = new ShareArticle();
            a.title = "来自懂球帝的图片~~";
            a.desc = "";
            a.imageUrl = path;
            a.redirectUrl = path;
            PlatformQzong.getDefault().shareArticle(context, a);
        }else{
            shareImageLocal(context, path);
        }
    }

    public void shareImageLocal(Activity context, String path) {
        //Toaster.toastShort("qzong不能分享图片吗？");
        Trace.e("share", "qzong分享本地图片，这个帖子方法不错: http://blog.csdn.net/buptlzx/article/details/9767203" + "");
    }



    @Override
    public void login(Activity context) {

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
                SocialCenter.getDefault().getShareCallback().onFail(null, uiError.errorDetail);
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
}
