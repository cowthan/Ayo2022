package org.ayo.social.callback;

import org.ayo.social.model.SocialUserInfo;

/**
 * Created by Administrator on 2017/10/14.
 */

public interface FetchUserInfoCallback {

    void onFinish(SocialUserInfo userInfo);

}
