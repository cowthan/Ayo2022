
package org.ayo.video.view;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;

import com.google.android.exoplayer.ExoPlayer;
import com.google.android.exoplayer.audio.AudioCapabilities;
import com.google.android.exoplayer.audio.AudioCapabilitiesReceiver;
import com.google.android.exoplayer.metadata.GeobMetadata;
import com.google.android.exoplayer.metadata.PrivMetadata;
import com.google.android.exoplayer.metadata.TxxxMetadata;
import com.google.android.exoplayer.text.Cue;
import com.google.android.exoplayer.util.Util;

import org.ayo.video.EventLogger;
import org.ayo.video.ExoPlayerHelper;
import org.ayo.video.ExtractorRendererBuilder;
import org.ayo.video.HlsRendererBuilder;
import org.ayo.video.R;

import java.util.List;
import java.util.Map;

/**
 * Created by hujinghui on 15/4/20.
 */
public class ExoVideoView extends LiveVideoView {

    private final static String tag = "ExoVideoView";

    private TextureView mTextureView;

    private SurfaceTexture mSurfaceTexture;

    private ExoPlayerHelper player;

    private AudioCapabilitiesReceiver audioCapabilitiesReceiver;

    private boolean isUserOption;

    private long playerPosition;

    private EventLogger eventLogger;

    private String path;

    private boolean prepared = false;

    private boolean mIsFirstPlay = true;

    private TextureView.SurfaceTextureListener mSurfaceTextureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            Log.d(tag,"onSurfaceTextureAvailable");
            mSurfaceTexture = surface;
            mSurface = new Surface(mSurfaceTexture);
            if (player != null) {
                //解决按home后,切回应用黑屏的bug
                player.setSurface(mSurface);
                if (!TextUtils.isEmpty(path) && !mIsFirstPlay) {
                    setIsRestart(true);
                    play(path);
                    start();
                }
            } else {
                initPlayer();
            }
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            Log.d(tag,"onSurfaceTextureSizeChanged");
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            Log.d(tag,"onSurfaceTextureDestroyed");
            if (player != null) {
                player.stop();
                player.blockingClearSurface();
                mIsFirstPlay = false;
            }
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        }
    };

    private ExoPlayerHelper.Listener playerListener = new ExoPlayerHelper.Listener() {
        @Override
        public void onStateChanged(boolean playWhenReady, int playbackState) {
            String text = "playWhenReady=" + playWhenReady + ", playbackState=";
            switch (playbackState) {
                case ExoPlayer.STATE_BUFFERING:
                    if (callback != null && !isUserOption)
                        callback.onInfo(INFO_CODE_BUFFERING_START);
                    text += "buffering";
                    break;
                case ExoPlayer.STATE_ENDED:
                    if (callback != null ){
                        if( !isUserOption){
                            callback.onInfo(INFO_CODE_BUFFERING_END);
                        }
                        callback.onCompletion();
                    }

                    text += "ended";
                    break;
                case ExoPlayer.STATE_IDLE:
                    text += "idle";
                    break;
                case ExoPlayer.STATE_PREPARING:
                    text += "preparing";
                    break;
                case ExoPlayer.STATE_READY:
                    if (callback != null && !isUserOption) {
                        callback.onInfo(INFO_CODE_BUFFERING_END);
                        if (!prepared) {
                            prepared = true;
                            callback.onPrepared();
                        }
                    }
                    text += "ready";
                    break;
                default:
                    text += "unknown";
                    break;
            }
            Log.e(tag, "onStateChanged:" + text);
        }

        @Override
        public void onError(Exception e) {
            e.printStackTrace();
            if (callback != null)
                callback.onError(ERROR_CODE_UNKNOWN);
            release();
        }

        @Override
        public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees,
                float pixelWidthAspectRatio) {
            setAspectRatio(height == 0 ? 1 : (width * pixelWidthAspectRatio) / height);
        }
    };

    private ExoPlayerHelper.CaptionListener captionListener = new ExoPlayerHelper.CaptionListener() {
        @Override
        public void onCues(List<Cue> cues) {

        }
    };

    private ExoPlayerHelper.Id3MetadataListener id3MetadataListener = new ExoPlayerHelper.Id3MetadataListener() {
        @Override
        public void onId3Metadata(Map<String, Object> metadata) {
            for (Map.Entry<String, Object> entry : metadata.entrySet()) {
                if (TxxxMetadata.TYPE.equals(entry.getKey())) {
                    TxxxMetadata txxxMetadata = (TxxxMetadata)entry.getValue();
                    Log.i(tag, String.format("ID3 TimedMetadata %s: description=%s, value=%s",
                            TxxxMetadata.TYPE, txxxMetadata.description, txxxMetadata.value));
                } else if (PrivMetadata.TYPE.equals(entry.getKey())) {
                    PrivMetadata privMetadata = (PrivMetadata)entry.getValue();
                    Log.i(tag, String.format("ID3 TimedMetadata %s: owner=%s", PrivMetadata.TYPE,
                            privMetadata.owner));
                } else if (GeobMetadata.TYPE.equals(entry.getKey())) {
                    GeobMetadata geobMetadata = (GeobMetadata)entry.getValue();
                    Log.i(tag, String.format(
                            "ID3 TimedMetadata %s: mimeType=%s, filename=%s, description=%s",
                            GeobMetadata.TYPE, geobMetadata.mimeType, geobMetadata.filename,
                            geobMetadata.description));
                } else {
                    Log.i(tag, String.format("ID3 TimedMetadata %s", entry.getKey()));
                }
            }
        }
    };

    private AudioCapabilitiesReceiver.Listener receiverListener = new AudioCapabilitiesReceiver.Listener() {
        @Override
        public void onAudioCapabilitiesChanged(AudioCapabilities audioCapabilities) {

        }
    };

    public ExoVideoView(Context context) {
        super(context);
    }

    public ExoVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    private Surface mSurface;
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
//        initPlayer();
        mTextureView = (TextureView) findViewById(R.id.surface_view);
        mTextureView.setSurfaceTextureListener(mSurfaceTextureListener);
        audioCapabilitiesReceiver = new AudioCapabilitiesReceiver(getContext(), receiverListener);
        audioCapabilitiesReceiver.register();
    }

    @Override
    protected void onDetachedFromWindow() {
        // 页面退出时注销掉监听
        if (audioCapabilitiesReceiver != null)
            audioCapabilitiesReceiver.unregister();
        super.onDetachedFromWindow();
    }

    private void initPlayer() {
        if (player == null) {
            player = new ExoPlayerHelper();
        }
        player.addListener(playerListener);
        player.setCaptionListener(captionListener);
        player.setMetadataListener(id3MetadataListener);
        player.seekTo(playerPosition);
        eventLogger = new EventLogger();
        eventLogger.startSession();
        player.addListener(eventLogger);
        player.setInfoListener(eventLogger);
        player.setInternalErrorListener(eventLogger);
        if (mSurface != null){
            player.setSurface(mSurface);
        }
    }

    private void prepare(boolean playWhenReady) {
        player.prepare(getRendererBuilder(path));
        player.setPlayWhenReady(playWhenReady);
    }

    public void release() {
        if (player != null) {
            prepared = false;
            playerPosition = player.getCurrentPosition();
            player.release();
            player = null;
            eventLogger.endSession();
            eventLogger = null;
        }
    }

    private ExoPlayerHelper.RendererBuilder getRendererBuilder(String path) {
        String userAgent = Util.getUserAgent(getContext(), "ExoPlayerDemo");
        if (path.contains("m3u8"))
            return new HlsRendererBuilder(getContext(), userAgent, path);
        else if (path.contains("mp4"))
            return new ExtractorRendererBuilder(getContext(), userAgent, parse(path),
                    getHeaders());
        else if (path.contains("rtmp")) {
            // live=1 标记为实时流,如果不加,可能播不了
            return new ExtractorRendererBuilder(getContext(), userAgent,
                    parse(path + " live=1"));
        } else {
            return new ExtractorRendererBuilder(getContext(), userAgent, parse(path),
                    getHeaders());
        }
    }
    public static Uri parse(String url) {
        if (TextUtils.isEmpty(url))
            return Uri.parse("");
        return Uri.parse(url);
    }
    @Override
    public void start() {
        isUserOption = false;
        if (player == null) {
            initPlayer();
        }
        player.setPlayWhenReady(true);
    }

    @Override
    public void start(boolean userOption) {
        isUserOption = userOption;
        if (player != null)
            player.setPlayWhenReady(true);
    }

    @Override
    public void pause() {
        isUserOption = false;
        if (player != null)
            player.setPlayWhenReady(false);
    }

    @Override
    public void pause(boolean userOption) {
        isUserOption = userOption;
        if (player != null)
            player.setPlayWhenReady(false);
    }

    @Override
    public boolean isPlaying() {
        return player != null && player.getPlaybackState() == ExoPlayerHelper.STATE_READY && player.getPlayWhenReady();
    }

    @Override
    public void play(String path) {
        Log.e(tag, "play:" + path);
        this.path = path;
        if (player == null) {
            initPlayer();
        }
        prepare(false);
    }

    @Override
    public void setKeepScreenOn(boolean keepScreenOn) {
        // setFlags(keepScreenOn ? KEEP_SCREEN_ON : 0, KEEP_SCREEN_ON);
    }

    @Override
    public void suspend() {
        release();
    }

    @Override
    public void seekTo(long positionMs) {
        if (player == null)
            return;
        player.seekTo(positionMs);
    }

    @Override
    public long getDuration() {
        if (player == null)
            return 0;
        return player.getDuration();
    }

    @Override
    public int getCurrentPosition() {
        if (player == null)
            return 0;
        return (int)player.getCurrentPosition();
    }

    @Override
    public boolean isBuffering() {
        return player != null && player.getPlaybackState() == ExoPlayerHelper.STATE_BUFFERING;
    }

}
