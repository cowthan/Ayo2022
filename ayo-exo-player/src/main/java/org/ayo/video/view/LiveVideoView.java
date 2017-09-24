
package org.ayo.video.view;

import android.content.Context;
import android.util.AttributeSet;

import com.google.android.exoplayer.AspectRatioFrameLayout;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hujinghui on 15/11/9.
 */
public abstract class LiveVideoView extends AspectRatioFrameLayout {

    public static final int ERROR_CODE_SERVER_DIED = 0x0001;

    public static final int ERROR_CODE_UNKNOWN = 0x0002;

    public static final int ERROR_CODE_OTHER = 0x0003;

    public static final int INFO_CODE_NOT_SEEKABLE = 0x0010;

    public static final int INFO_CODE_BUFFERING_END = 0x0011;

    public static final int INFO_CODE_BUFFERING_START = 0x0012;

    public static final int INFO_CODE_VIDEO_RENDERING_START = 0x0013;

    protected Map<String, String> mHeaders;

    protected LiveVideoViewCallback callback;

    public LiveVideoView(Context context) {
        super(context);
    }

    public LiveVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setLiveVideoViewCallback(LiveVideoViewCallback callback) {
        this.callback = callback;
    }
    public boolean isRestart() {
        return mIsRestart;
    }

    public void setIsRestart(boolean isRestart) {
        mIsRestart = isRestart;
    }

    /**
     * 标记是否是切应用后重新播放
     */
    private boolean mIsRestart = false;

    public abstract boolean isPlaying();

    public abstract void start();

    public abstract void start(boolean userOption);

    public abstract void pause();

    public abstract void pause(boolean userOption);

    public abstract void setKeepScreenOn(boolean keepScreenOn);

    public abstract void play(String path);

    public abstract void suspend();

    public abstract void seekTo(long positionMs);

    public abstract long getDuration();

    public abstract int getCurrentPosition();

    public abstract boolean isBuffering();

    public Map<String, String> getHeaders() {
        return mHeaders;
    }

    public void addHeader(String key, String value) {
        if (mHeaders == null)
            mHeaders = new HashMap<>();
        mHeaders.put(key, value);
    }

    public void setHeaders(Map<String, String> headers) {
        this.mHeaders = headers;
    }

    public interface LiveVideoViewCallback {
        void onError(int errorCode);

        void onInfo(int infoCode);

        void onCompletion();

        void onPrepared();
    }

}
