package org.ayo.social.callback;

import android.support.annotation.Nullable;

import org.ayo.social.model.SocialAccountInfo;


/**
 * Created by qiaoliang on 2017/6/22.
 */

public class BaseShareCallback implements ShareCallback{

    @Override
    public void onSuccess() {

    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onFail(@Nullable Exception e, String reason) {

    }

    @Override
    public void onLoginSuccess(SocialAccountInfo info) {

    }
}
