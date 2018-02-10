package org.ayo.social;

import android.app.Activity;
import android.content.Context;

import org.ayo.social.callback.FetchUserInfoCallback;
import org.ayo.social.model.ShareArticle;


/**
 * Created by qiaoliang on 2017/6/20.
 */

public abstract class SocialPlatform {

    public abstract void init(Context context);
    public abstract void shareArticle(Activity context, ShareArticle article);
    public abstract void shareImage(Activity context, String path);
//    public abstract void shareBitmap(Activity context, Bitmap bitmap);
    public abstract void login(Activity context);
    public abstract void getUserInfo(FetchUserInfoCallback callback);
}
