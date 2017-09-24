
package org.ayo.editor.editor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

/**
 * 这只是一个简单的ImageView，可以存放Bitmap和Path等信息 Created by lishuai on 16/5/4.
 */
public class RichImageView extends android.support.v7.widget.AppCompatImageView {
    private String absolutePath;

    private Bitmap bitmap;

    private boolean isVideo;

    private String filename;

    /**
     * 视频时长
     */
    public long duration;

    /**
     * 视频类型
     */
    public String videoType;

    public int videoWidth;

    public int videoHeight;

    public long videoSize;

    /**
     * 七牛视频相对路径
     */
    public String videoUrl;

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public boolean getIsVideo() {
        return isVideo;
    }

    public void setIsVideo(boolean isVideo) {
        this.isVideo = isVideo;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public RichImageView(Context context) {
        this(context, null);
    }

    public RichImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RichImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Path clipPath = new Path();
        int w = this.getWidth();
        int h = this.getHeight();
        clipPath.addRoundRect(new RectF(0, 0, w, h), 10.0f, 10.0f, Path.Direction.CW);
        canvas.clipPath(clipPath);
        super.onDraw(canvas);
    }

}
