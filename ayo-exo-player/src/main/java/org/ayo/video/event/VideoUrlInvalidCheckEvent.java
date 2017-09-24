package org.ayo.video.event;

/**
 * Created by Administrator on 2017/8/22.
 */

public class VideoUrlInvalidCheckEvent {
    public boolean success;

    public String type;

    public String stream;

    public String src;

    public String redirectUrl;

    public VideoUrlInvalidCheckEvent(boolean success, String type, String stream, String src) {
        this.success = success;
        this.type = type;
        this.stream = stream;
        this.src = src;
    }

    public VideoUrlInvalidCheckEvent(boolean success, String type, String stream, String src,
                                     String redirectUrl) {
        this.success = success;
        this.type = type;
        this.stream = stream;
        this.src = src;
        this.redirectUrl = redirectUrl;
    }
}
