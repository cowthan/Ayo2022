package org.ayo.imagepicker;

import java.util.List;

/**
 * Created by qiaoliang on 2017/6/15.
 */

public class ImagePickedEvent {
    public List<ThumbModel> data;

    public ImagePickedEvent(List<ThumbModel> data) {
        this.data = data;
    }
}
