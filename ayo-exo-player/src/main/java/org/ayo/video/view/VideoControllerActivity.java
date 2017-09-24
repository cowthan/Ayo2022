package org.ayo.video.view;

/**
 * Created by Administrator on 2017/8/22.
 */

public interface VideoControllerActivity {

    void onScreenRequired(int requestedOrientation);

    void onVideoComplete();

    void onVideoStart();

}
