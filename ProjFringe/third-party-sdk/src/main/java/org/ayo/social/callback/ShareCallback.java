package org.ayo.social.callback;

import android.support.annotation.Nullable;

import org.ayo.social.model.SocialAccountInfo;


/**
 * Created by qiaoliang on 2017/6/21.
 */

public interface ShareCallback {
    void onSuccess();
    void onCancel();
    void onFail(@Nullable Exception e, String reason);
    void onLoginSuccess(SocialAccountInfo info);
}
