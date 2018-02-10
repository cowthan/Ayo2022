//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cn.jzvd.decode;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.Surface;
import android.view.SurfaceHolder;

import com.google.android.exoplayer.util.Util;

import java.io.FileDescriptor;
import java.util.Map;

import cn.jzvd.decode.rtmp.MyExtractorRendererBuilder;
import tv.danmaku.ijk.media.exo.demo.EventLogger;
import tv.danmaku.ijk.media.exo.demo.SmoothStreamingTestMediaDrmCallback;
import tv.danmaku.ijk.media.exo.demo.player.DemoPlayer;
import tv.danmaku.ijk.media.exo.demo.player.DemoPlayer.InfoListener;
import tv.danmaku.ijk.media.exo.demo.player.DemoPlayer.InternalErrorListener;
import tv.danmaku.ijk.media.exo.demo.player.DemoPlayer.Listener;
import tv.danmaku.ijk.media.exo.demo.player.DemoPlayer.RendererBuilder;
import tv.danmaku.ijk.media.exo.demo.player.ExtractorRendererBuilder;
import tv.danmaku.ijk.media.exo.demo.player.HlsRendererBuilder;
import tv.danmaku.ijk.media.exo.demo.player.SmoothStreamingRendererBuilder;
import tv.danmaku.ijk.media.player.AbstractMediaPlayer;
import tv.danmaku.ijk.media.player.MediaInfo;
import tv.danmaku.ijk.media.player.misc.IjkTrackInfo;

public class MyIjkExoMediaPlayer extends AbstractMediaPlayer {
    private Context mAppContext;
    private DemoPlayer mInternalPlayer;
    private EventLogger mEventLogger;
    private String mDataSource;
    private int mVideoWidth;
    private int mVideoHeight;
    private Surface mSurface;
    private RendererBuilder mRendererBuilder;
    private DemoPlayerListener mDemoListener;

    public MyIjkExoMediaPlayer(Context context) {
        this.mAppContext = context.getApplicationContext();
        this.mDemoListener = new DemoPlayerListener();
        this.mEventLogger = new EventLogger();
        this.mEventLogger.startSession();
    }

    public void setDisplay(SurfaceHolder sh) {
        if(sh == null) {
            this.setSurface((Surface)null);
        } else {
            this.setSurface(sh.getSurface());
        }

    }

    public void setSurface(Surface surface) {
        this.mSurface = surface;
        if(this.mInternalPlayer != null) {
            this.mInternalPlayer.setSurface(surface);
        }

    }

    public DemoPlayer getDemoPlayer() {
        return mInternalPlayer;
    }

    public void setDataSource(Context context, Uri uri) {
        this.mDataSource = uri.toString();
        this.mRendererBuilder = this.getRendererBuilder2(this.mDataSource);
    }

    public void setDataSource(Context context, Uri uri, Map<String, String> headers) {
        this.setDataSource(context, uri);
    }

    public void setDataSource(String path) {
        this.setDataSource(this.mAppContext, Uri.parse(path));
    }

    public void setDataSource(FileDescriptor fd) {
        throw new UnsupportedOperationException("no support");
    }

    public String getDataSource() {
        return this.mDataSource;
    }

    public void prepareAsync() throws IllegalStateException {
        if(this.mInternalPlayer != null) {
            throw new IllegalStateException("can't prepare a prepared player");
        } else {
            this.mInternalPlayer = new DemoPlayer(this.mRendererBuilder);
            this.mInternalPlayer.addListener(this.mDemoListener);
            this.mInternalPlayer.addListener(this.mEventLogger);
            this.mInternalPlayer.setInfoListener(this.mEventLogger);
            this.mInternalPlayer.setInternalErrorListener(this.mEventLogger);
            if(this.mSurface != null) {
                this.mInternalPlayer.setSurface(this.mSurface);
            }

            this.mInternalPlayer.prepare();
            this.mInternalPlayer.setPlayWhenReady(false);
        }
    }

    public void start() throws IllegalStateException {
        if(this.mInternalPlayer != null) {
            this.mInternalPlayer.setPlayWhenReady(true);
        }
    }

    public void stop() throws IllegalStateException {
        if(this.mInternalPlayer != null) {
            this.mInternalPlayer.release();
        }
    }

    public void pause() throws IllegalStateException {
        if(this.mInternalPlayer != null) {
            this.mInternalPlayer.setPlayWhenReady(false);
        }
    }

    public void setWakeMode(Context context, int mode) {
    }

    public void setScreenOnWhilePlaying(boolean screenOn) {
    }

    public IjkTrackInfo[] getTrackInfo() {
        return null;
    }

    public int getVideoWidth() {
        return this.mVideoWidth;
    }

    public int getVideoHeight() {
        return this.mVideoHeight;
    }

    public boolean isPlaying() {
        if(this.mInternalPlayer == null) {
            return false;
        } else {
            int state = this.mInternalPlayer.getPlaybackState();
            switch(state) {
                case 1:
                case 2:
                case 5:
                default:
                    return false;
                case 3:
                case 4:
                    return this.mInternalPlayer.getPlayWhenReady();
            }
        }
    }

    public void seekTo(long msec) throws IllegalStateException {
        if(this.mInternalPlayer != null) {
            this.mInternalPlayer.seekTo(msec);
        }
    }

    public long getCurrentPosition() {
        return this.mInternalPlayer == null?0L:this.mInternalPlayer.getCurrentPosition();
    }

    public long getDuration() {
        return this.mInternalPlayer == null?0L:this.mInternalPlayer.getDuration();
    }

    public int getVideoSarNum() {
        return 1;
    }

    public int getVideoSarDen() {
        return 1;
    }

    public void reset() {
        if(this.mInternalPlayer != null) {
            this.mInternalPlayer.release();
            this.mInternalPlayer.removeListener(this.mDemoListener);
            this.mInternalPlayer.removeListener(this.mEventLogger);
            this.mInternalPlayer.setInfoListener((InfoListener)null);
            this.mInternalPlayer.setInternalErrorListener((InternalErrorListener)null);
            this.mInternalPlayer = null;
        }

        this.mSurface = null;
        this.mDataSource = null;
        this.mVideoWidth = 0;
        this.mVideoHeight = 0;
    }

    public void setLooping(boolean looping) {
        throw new UnsupportedOperationException("no support");
    }

    public boolean isLooping() {
        return false;
    }

    public void setVolume(float leftVolume, float rightVolume) {
    }

    public int getAudioSessionId() {
        return 0;
    }

    public MediaInfo getMediaInfo() {
        return null;
    }

    public void setLogEnabled(boolean enable) {
    }

    public boolean isPlayable() {
        return true;
    }

    public void setAudioStreamType(int streamtype) {
    }

    public void setKeepInBackground(boolean keepInBackground) {
    }

    public void release() {
        if(this.mInternalPlayer != null) {
            this.reset();
            this.mDemoListener = null;
            this.mEventLogger.endSession();
            this.mEventLogger = null;
        }

    }

    public int getBufferedPercentage() {
        return this.mInternalPlayer == null?0:this.mInternalPlayer.getBufferedPercentage();
    }

    private RendererBuilder getRendererBuilder() {
        Uri contentUri = Uri.parse(this.mDataSource);
        String userAgent = Util.getUserAgent(this.mAppContext, "MyIjkExoMediaPlayer");
        if (contentUri.toString().contains("m3u8")) {
            return new HlsRendererBuilder(this.mAppContext, userAgent, contentUri.toString());
        }
        int contentType = inferContentType(contentUri);
        switch(contentType) {
            case 1:
                return new SmoothStreamingRendererBuilder(this.mAppContext, userAgent, contentUri.toString(), new SmoothStreamingTestMediaDrmCallback());
            case 2:
                return new HlsRendererBuilder(this.mAppContext, userAgent, contentUri.toString());
            case 3:
            default:
                return new ExtractorRendererBuilder(this.mAppContext, userAgent, contentUri);
        }
    }


    private RendererBuilder getRendererBuilder2(String path) {
        // 获取系统UA，部分视频流需要UA判断，无法识别的UA会造成无权范围的问题
        String userAgent = "";
//        Map<String, String> header = new HashMap<>();

        if (TextUtils.isEmpty(userAgent))
            userAgent = Util.getUserAgent(mAppContext, "ExoPlayerDemo");
        if (path.contains("m3u8"))
            return new HlsRendererBuilder(mAppContext, userAgent, path);
        else if (path.contains("mp4"))
            return new MyExtractorRendererBuilder(mAppContext, userAgent, Uri.parse(path));
        else if (path.contains("rtmp")) {
            // live=1 标记为实时流,如果不加,可能播不了
            return new MyExtractorRendererBuilder(mAppContext, userAgent,
                    Uri.parse(path + " live=1"));
        } else {
            return new MyExtractorRendererBuilder(mAppContext, userAgent, Uri.parse(path));
        }
    }

    private static int inferContentType(Uri uri) {
        String lastPathSegment = uri.getLastPathSegment();
        return Util.inferContentType(lastPathSegment);
    }

    private class DemoPlayerListener implements Listener {
        private boolean mIsPrepareing;
        private boolean mDidPrepare;
        private boolean mIsBuffering;

        private DemoPlayerListener() {
            this.mIsPrepareing = false;
            this.mDidPrepare = false;
            this.mIsBuffering = false;
        }

        public void onStateChanged(boolean playWhenReady, int playbackState) {
            if(this.mIsBuffering) {
                switch(playbackState) {
                    case 4:
                    case 5:
                        MyIjkExoMediaPlayer.this.notifyOnInfo(702, MyIjkExoMediaPlayer.this.mInternalPlayer.getBufferedPercentage());
                        this.mIsBuffering = false;
                }
            }

            if(this.mIsPrepareing) {
                switch(playbackState) {
                    case 4:
                        MyIjkExoMediaPlayer.this.notifyOnPrepared();
                        this.mIsPrepareing = false;
                        this.mDidPrepare = false;
                }
            }

            switch(playbackState) {
                case 1:
                    MyIjkExoMediaPlayer.this.notifyOnCompletion();
                    break;
                case 2:
                    this.mIsPrepareing = true;
                    break;
                case 3:
                    MyIjkExoMediaPlayer.this.notifyOnInfo(701, MyIjkExoMediaPlayer.this.mInternalPlayer.getBufferedPercentage());
                    this.mIsBuffering = true;
                case 4:
                default:
                    break;
                case 5:
                    MyIjkExoMediaPlayer.this.notifyOnCompletion();
            }

        }

        public void onError(Exception e) {
            MyIjkExoMediaPlayer.this.notifyOnError(1, 1);
        }

        public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
            MyIjkExoMediaPlayer.this.mVideoWidth = width;
            MyIjkExoMediaPlayer.this.mVideoHeight = height;
            MyIjkExoMediaPlayer.this.notifyOnVideoSizeChanged(width, height, 1, 1);
            if(unappliedRotationDegrees > 0) {
                MyIjkExoMediaPlayer.this.notifyOnInfo(10001, unappliedRotationDegrees);
            }

        }
    }

}
