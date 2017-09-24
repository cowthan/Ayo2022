package org.ayo.video.event;

/**
 * Created by Administrator on 2017/8/22.
 */

public class ScreenChangeEvent {
    public int orientation;

    public String tag;

    public ScreenChangeEvent(int orientation, String tag) {
        this.orientation = orientation;
        this.tag = tag;
    }
}
