package cn.jzvd.controller;

import android.graphics.SurfaceTexture;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;

import cn.jzvd.JZVideoPlayerManager;
import cn.jzvd.canvas.JZResizeTextureView;
import cn.jzvd.decode.JZMediaInterface;
import cn.jzvd.decode.JZMediaSystem;

/**
 * 这个类用来和jzvd互相调用，当jzvd需要调用Media的时候调用这个类，当MediaPlayer有回调的时候，通过这个类回调JZVD
 * Created by Nathen on 2017/11/18.
 *
 * 管理JZMediaInterface（播放器编解码内核，类似MediaPlayer）和JZResizeTextureView（渲染内核，画布）
 * 一个播放器内核，可以切换不同的画布，全屏小屏就是通过这个原理实现
 */
public class JZMediaManager implements TextureView.SurfaceTextureListener {

    public static final String TAG = "JiaoZiVideoPlayer";
    public static final int HANDLER_PREPARE = 0;
    public static final int HANDLER_RELEASE = 2;

    public static JZResizeTextureView textureView;
    public static SurfaceTexture savedSurfaceTexture;
    public static Surface surface;
    public static JZMediaManager jzMediaManager;
    public int positionInList = -1;
    public JZMediaInterface jzMediaInterface;
    public int currentVideoWidth = 0;
    public int currentVideoHeight = 0;

    public HandlerThread mMediaHandlerThread;
    public MediaHandler mMediaHandler;
    public Handler mainThreadHandler;

    public JZMediaManager() {
        mMediaHandlerThread = new HandlerThread(TAG);
        mMediaHandlerThread.start();
        mMediaHandler = new MediaHandler(mMediaHandlerThread.getLooper());
        mainThreadHandler = new Handler();
        if (jzMediaInterface == null)
            jzMediaInterface = new JZMediaSystem();
    }

    public static JZMediaManager instance() {
        if (jzMediaManager == null) {
            jzMediaManager = new JZMediaManager();
        }
        return jzMediaManager;
    }

    public static Object[] getDataSource() {
        return instance().jzMediaInterface.dataSourceObjects;
    }

    //这几个方法是不是多余了，为了不让其他地方动MediaInterface的方法
    public static void setDataSource(Object[] dataSourceObjects) {
        instance().jzMediaInterface.dataSourceObjects = dataSourceObjects;
    }

    //正在播放的url或者uri
    public static Object getCurrentDataSource() {
        return instance().jzMediaInterface.currentDataSource;
    }

    public static void setCurrentDataSource(Object currentDataSource) {
        instance().jzMediaInterface.currentDataSource = currentDataSource;
    }

    public static long getCurrentPosition() {
        return instance().jzMediaInterface.getCurrentPosition();
    }

    public static void setMediaInterface(JZMediaInterface mediaInterface) {
        JZMediaManager.instance().jzMediaInterface = mediaInterface;
    }

    public static long getDuration() {
        return instance().jzMediaInterface.getDuration();
    }

    public static void seekTo(long time) {
        instance().jzMediaInterface.seekTo(time);
    }

    public static void pause() {
        instance().jzMediaInterface.pause();
    }

    public static void start() {
        instance().jzMediaInterface.start();
    }

    public static boolean isPlaying() {
        return instance().jzMediaInterface.isPlaying();
    }

    public void releaseMediaPlayer() {
        Message msg = new Message();
        msg.what = HANDLER_RELEASE;
        mMediaHandler.sendMessage(msg);
    }

    public void prepare() {
        releaseMediaPlayer();
        Message msg = new Message();
        msg.what = HANDLER_PREPARE;
        mMediaHandler.sendMessage(msg);
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
        Log.i(TAG, "onSurfaceTextureAvailable [" + JZVideoPlayerManager.getCurrentJzvd().hashCode() + "] ");
        if (savedSurfaceTexture == null) {
            savedSurfaceTexture = surfaceTexture;
            prepare();
        } else {
            textureView.setSurfaceTexture(savedSurfaceTexture);
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        return savedSurfaceTexture == null;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

    }


    public class MediaHandler extends Handler {
        public MediaHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLER_PREPARE:
                    currentVideoWidth = 0;
                    currentVideoHeight = 0;
                    jzMediaInterface.prepare();
                    if (surface != null) {
                        surface.release();
                    }
                    surface = new Surface(savedSurfaceTexture);
                    jzMediaInterface.setSurface(surface);
                    break;
                case HANDLER_RELEASE:
                    jzMediaInterface.release();
                    break;
            }
        }
    }
}
