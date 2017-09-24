package org.ayo.push;

/**
 * Created by qiaoliang on 2017/4/14.
 */

public interface PushListener {

    void onPassthroughReceived(String platform, String msgId, String payload);
    void onNotificationReceived(String platform, String msgId, String payload);
    void onNotificationClicked(String platform, String msgId, String payload);
    void onRegistIdReceived(String platform, String resId);

}
