package org.ayo.social;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;

import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.ayo.core.Lang;
import org.ayo.log.Trace;
import org.ayo.social.model.ShareArticle;
import org.ayo.social.utils.SocialUtils;

/**
 * Created by qiaoliang on 2017/6/20.
 */

public abstract class PlatformWechatAll extends SocialPlatform {


    public void shareArticle(final Context context, final ShareArticle article, final int wxService) {
        /// redirectUrl存在
        /// title存在且小于30个字符
        /// desc存在且小于50个字符，然后加上：（分享自@懂球帝）
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                final IWXAPI wxApi = WXAPIFactory.createWXAPI(context, SocialCenter.getDefault().appIdWx);
                wxApi.registerApp(SocialCenter.getDefault().appIdWx);
                WXWebpageObject webpage = new WXWebpageObject();
                webpage.webpageUrl = article.redirectUrl;
                final WXMediaMessage wxMsg = new WXMediaMessage(webpage);
                wxMsg.title = article.title;
                wxMsg.description = article.desc;

                SocialUtils.getImageFromUrl(context, article.imageUrl, new SocialUtils.OnDownloadCallback() {
                    @Override
                    public void onFinish(String path) {
                        if(Lang.isEmpty(path)){
                            //Toaster.toastShort("分享失败，图片url无法获取");
                        }else{
                            Bitmap thumbBmp = SocialUtils.getBitmapFromPath(path);
                            int height = SocialUtils.THUMB_SIZE * thumbBmp.getHeight() / thumbBmp.getWidth();
                            Bitmap thumb = Bitmap.createScaledBitmap(thumbBmp, SocialUtils.THUMB_SIZE, height, true);
                            wxMsg.setThumbImage(thumb);

                            Trace.e("share", "封面图大小：" + SocialUtils.getBitmapSize(thumbBmp));
                            Trace.e("share", "封面图压缩后大小：" + SocialUtils.getBitmapSize(thumb));
                            thumbBmp.recycle();

                            SendMessageToWX.Req req = new SendMessageToWX.Req();
                            req.transaction = String.valueOf(System.currentTimeMillis());
                            req.message = wxMsg;
                            req.scene = wxService;
                            wxApi.sendReq(req);
                        }
                    }
                });
            }
        });
    }

    public void shareImage(final Context context, final String imagePath, final int wxService) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                final IWXAPI wxApi = WXAPIFactory.createWXAPI(context, SocialCenter.getDefault().appIdWx);
                wxApi.registerApp(SocialCenter.getDefault().appIdWx);

                // 分享图片
                WXImageObject imageObject = new WXImageObject();
                imageObject.setImagePath(imagePath);

                WXMediaMessage msg = new WXMediaMessage();
                msg.mediaObject = imageObject;
                Bitmap bmp = BitmapFactory.decodeFile(imagePath);
                int height = SocialUtils.THUMB_SIZE * bmp.getHeight() / bmp.getWidth();
                byte[] bytes;
                if (wxService == SendMessageToWX.Req.WXSceneSession) {
                    Bitmap thumb = Bitmap.createScaledBitmap(bmp, SocialUtils.THUMB_SIZE, height, true);
                    bmp.recycle();
                    bytes = SocialUtils.bmpToByteArray(thumb, true);
                    bytes = SocialUtils.compressBitmap(bytes, 32768);
                } else {
                    Bitmap thumb = Bitmap.createScaledBitmap(bmp, SocialUtils.THUMB_SIZE, SocialUtils.THUMB_SIZE, true);
                    bmp.recycle();
                    bytes = SocialUtils.bmpToByteArray(thumb, true);
                }
                msg.thumbData = bytes;

                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = String.valueOf(System.currentTimeMillis());
                req.message = msg;
                req.scene = wxService;
                wxApi.sendReq(req);
            }
        });
    }

}
