package org.ayo.video.view;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.ayo.video.R;
import org.ayo.video.VideoCenter;
import org.ayo.video.event.CloseVideoEvent;
import org.ayo.video.event.VideoUrlInvalidCheckEvent;

import java.util.HashMap;



/**
 */
public class VideoFragment extends Fragment implements View.OnClickListener{

    public static final String FROM_VIDEO_ACTIVITY = "FROM_VIDEO_ACTIVITY";

    public static final String FROM_THREAD = "FROM_THREAD";

    private final String tag = "VIDEO_FRAGMENT";

    public boolean isFromPlayerActivity = false;

    public boolean isFromThread;

    public String mType;

    private String mRefer;

    private boolean mShowClose;

    //private ViewStub nativeVideoViewStub;

    private ViewStub exoVideoViewStub;

    private LiveVideoView mVideoView;

    private MediaController mMediaController;

    private View mVideoCenterPage;

    private volatile int mCurrentPosition = -1;

    private int mBufferProgress = 100;

    private float width;

    private PowerManager.WakeLock mWakeLock;

    private Handler mHandler;

    public boolean pausedByUser = false;

    private long mTotalDuration;

//    private NewConfirmDialog mDialog;

    private String mUrl;

    private String mTitle;

    private GestureDetector mGestureDetector;

    private volatile int mSmoothTotalDistance;

    private boolean isForegrond = true;

    private HashMap<String, Integer> mCurrentHashMap = new HashMap<>();

    private float x;

    private final int COUNT = 15;

    private final int DELAY = 1000;

    private int mCurrent;
    
    private View view;

    private int mTitlePadding;

    private ImageButton mCloseBtn;

    //避免显示两次dialog,TournamentVideoFragment  initVideo()也有一次弹窗
    private boolean enableShowDialog;

    private ConnectionChangeReceiver myReceiver;

    private boolean isComplete;

    public boolean mPauseStatusPlaying = false;
    
    private String mFromTag;

    private VideoControllerActivity mOuterController;

    /**
     * 判断网络类型
     *
     * @return 0:wifi 1:流量 2:无网络
     */
    public static int checkNetworkIsWifi(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobNetInfo = connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifiNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if ((wifiNetInfo == null || !wifiNetInfo.isConnected())) {
            if (mobNetInfo != null && mobNetInfo.isConnected()) {
                return 1;
            } else {
                return 2;
            }

        } else {
            return 0;
        }
    }

    public static void showToast(Activity activity, String s){
        Toast.makeText(activity, s, Toast.LENGTH_LONG).show();
    }

    public LiveVideoView.LiveVideoViewCallback liveVideoViewCallback = new LiveVideoView.LiveVideoViewCallback() {
        @Override
        public void onError(int errorCode) {
            Log.e(tag, "OnErrorListener  " + errorCode);
            Toast.makeText(getActivity(), "onError" + errorCode, Toast.LENGTH_LONG).show();
            mHandler.removeCallbacks(mTimeRunnable);
            //无网络
            if(checkNetworkIsWifi(getActivity())!=2){
                if (!mVideoView.isPlaying()) {
                    //startPlayerOnLineVideoActivity();
                    VideoCenter.getDefault().post(new CloseVideoEvent());
                }
            }

        }

        @Override
        public void onInfo(int infoCode) {
            Log.e(tag, "MediaPlayer.OnInfoListener:" + infoCode);
            switch (infoCode) {
                case LiveVideoView.INFO_CODE_BUFFERING_END: {// MediaPlayer is
                    // 缓冲到可以播放的程度了，可能会被多次调用
                    if (!pausedByUser && !mVideoView.isPlaying()) {
                        startPlay();
                        mOuterController.onVideoStart();
                    }
                    break;
                }
                case LiveVideoView.INFO_CODE_BUFFERING_START: {// 为了等缓冲区的数据，临时暂停playback
                    stopPlayWithNoBuffer();

                    break;
                }
                case LiveVideoView.INFO_CODE_NOT_SEEKABLE: {// 不能定位的媒体（如：实时流）
                    showToast(getActivity(), getString(R.string.unsupport_drag));
                    break;
                }
                case LiveVideoView.INFO_CODE_VIDEO_RENDERING_START: {
                    break;
                }
            }
        }

        @Override
        public void onCompletion() {
            mHandler.removeCallbacks(mUpdateRunnable);
            mMediaController.show();
            mMediaController.setPlayState(MediaController.MEDIA_PLAY_STATE_STOP);
            mMediaController.setSwitchBg(true);
            mVideoView.setKeepScreenOn(true);
            isComplete=true;
            mOuterController.onVideoComplete();

//            VideoCenter.getDefault().post(new VideoCompletedEvent());
            // TODO: 16/1/19 小米4.1.1在视频源不可用时会调用onCompletion 先注释掉finish
            // finish();
        }

        @Override
        public void onPrepared() {
            Log.e(tag, "mOnPreparedListener");
            //无网络
            if(checkNetworkIsWifi(getActivity())==2){
//                AppUtils.showToast(getActivity(), getString(R.string.network_disable_exit));
                return;
            }
            enableShowDialog = true;
//            mHandler.post(mUpdateRunnable);
            onPrepareToPlay();
        }

    };

    /**
     * 更新播放的进度条
     */
    private Runnable mUpdateRunnable = new Runnable() {

        @Override
        public void run() {
            if (mHandler == null)
                return;
            if (mMediaController == null) {
                mHandler.removeCallbacks(mUpdateRunnable);
                return;
            }
            mHandler.postDelayed(mUpdateRunnable, 50);
            updateProgress();
        }
    };

    private MediaPlayer.OnBufferingUpdateListener onBufferingUpdateListener = new MediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
            if (mBufferProgress == i)
                return;
            mBufferProgress = i;
            mMediaController.setSecondProgress(mBufferProgress);
        }
    };

    private MediaController.MediaControllerListener mediaControllerListener = new MediaController.MediaControllerListener() {
        @Override
        public void onStart() {
            pausedByUser = false;
            startPlay();
            Log.e(tag, "onStart");
        }

        @Override
        public void onStop() {
            pausedByUser = true;
            stopPlay();
            Log.e(tag, "onStop");
        }

        @Override
        public void onProgress(int progress) {
            if (mHandler != null)
                mHandler.post(mUpdateRunnable);
            if (mVideoView != null) {
                mVideoView.seekTo((mVideoView.getDuration()) * progress / 100);
            }

        }

        @Override
        public void onScreenRequired(int requestedOrientation) {
            getActivity().setRequestedOrientation(requestedOrientation);
            mOuterController.onScreenRequired(requestedOrientation);

//            VideoCenter.getDefault().post(new ScreenChangeEvent(requestedOrientation, mFromTag));
        }

        @Override
        public void onExit() {
            if(isFromThread){
                getActivity().finish();
            }else {
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                mOuterController.onScreenRequired(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//                VideoCenter.getDefault().post(
//                        new ScreenChangeEvent(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT, mFromTag));
            }

        }

        @Override
        public void onSeekBarProgressChanged(int changeProgress) {
        }

        @Override
        public void onStartTrackingTouch(Boolean isStart) {
            mHandler.removeCallbacks(mUpdateRunnable);
        }

        @Override
        public void onClose() {
            VideoCenter.getDefault().post(new CloseVideoEvent());
        }
    };
    private ProgressBar mProgressBar;

    /**
     *
     * @param url 播放地址
     * @param title 播放器标题
     * @param refer
     * @param type 类型:qq,pptv...
     * @return
     */
    public static VideoFragment newInstance(String url, String title, String refer, String type,
                                            String tag) {
        Bundle bundle=new Bundle();
        bundle.putString("url",url);
        bundle.putString("title",title);
        bundle.putString("refer",refer);
        bundle.putString("type", type);
        bundle.putString("tag",tag);
        VideoFragment fragment=new VideoFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static VideoFragment newInstance(String url, String title, String refer, String type,
                                            boolean isFromThread, String tag) {
        Bundle bundle=new Bundle();
        bundle.putString("url",url);
        bundle.putString("title",title);
        bundle.putString("refer",refer);
        bundle.putString("type", type);
        bundle.putString("tag",tag);
        bundle.putBoolean(FROM_THREAD, isFromThread);
        VideoFragment fragment=new VideoFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static String IS_FROM_PLAYER_ON_LINE_VIDEO_ACTIVITY = "is_from_player_on_line_video_activity";
    /**
     *
     * @param url 播放地址
     * @param title 播放器标题
     * @param refer
     * @param type 类型:qq,pptv...
     * @param showCloseBtn 是否显示关闭按钮
     * @return
     */
    public static VideoFragment newInstance(String url, String title, String refer, String type,
                                            boolean isFromPlayerActivity, boolean showCloseBtn, String tag) {
        Bundle bundle = new Bundle();
        bundle.putString("url",url);
        bundle.putString("title",title);
        bundle.putString("refer",refer);
        bundle.putString("type", type);
        bundle.putString("tag",tag);
        bundle.putBoolean(IS_FROM_PLAYER_ON_LINE_VIDEO_ACTIVITY, isFromPlayerActivity);
        bundle.putBoolean("close", showCloseBtn);
        VideoFragment fragment=new VideoFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static VideoFragment newInstance(String url, String title, String refer, String type,
                                            boolean isFromPlayerActivity, boolean showCloseBtn, boolean showReturn, String tag) {
        Bundle bundle = new Bundle();
        bundle.putString("url",url);
        bundle.putString("title",title);
        bundle.putString("refer",refer);
        bundle.putString("type", type);
        bundle.putString("tag",tag);
        bundle.putBoolean(IS_FROM_PLAYER_ON_LINE_VIDEO_ACTIVITY, isFromPlayerActivity);
        bundle.putBoolean("close", showCloseBtn);
        bundle.putBoolean("return",showReturn);
        VideoFragment fragment=new VideoFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static VideoFragment newInstance(String url, String title, String refer, String type,
                                            int titlePadding, boolean isFromPlayerActivity, boolean showCloseBtn,
                                            boolean showReturn, String tag) {
        Bundle bundle = new Bundle();
        bundle.putString("url",url);
        bundle.putString("title",title);
        bundle.putString("refer",refer);
        bundle.putString("type", type);
        bundle.putString("tag",tag);
        bundle.putBoolean(IS_FROM_PLAYER_ON_LINE_VIDEO_ACTIVITY, isFromPlayerActivity);
        bundle.putBoolean("close", showCloseBtn);
        bundle.putBoolean("return", showReturn);
        bundle.putInt("title_padding", titlePadding);
        VideoFragment fragment=new VideoFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private Runnable mTimeRunnable = new Runnable() {
        @Override
        public void run() {
            if (mCurrent >= COUNT && (mVideoView == null || !mVideoView.isPlaying())) {
                mHandler.removeCallbacks(mTimeRunnable);
                VideoCenter.getDefault().post(new CloseVideoEvent());
                startPlayerOnLineVideoActivity();
            } else {
                if (mVideoView != null && mVideoView.isPlaying()) {
                    mHandler.removeCallbacks(mTimeRunnable);
                } else {
                    mCurrent++;
                    mHandler.postDelayed(mTimeRunnable, DELAY);
                }
            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mOuterController = (VideoControllerActivity) getActivity();
        mGestureDetector = new GestureDetector(getActivity(), new MyGestureDetector());
        mCurrentHashMap.clear();
        view=inflater.inflate(R.layout.fragment_video, null);
        Bundle bundle=getArguments();
        if(bundle==null){
            return view;
        }
        mUrl = bundle.getString("url");
        if (TextUtils.isEmpty(mUrl)) {
//            finish();
            return view;
        }
        mTitle = bundle.getString("title");
        mRefer = bundle.getString("refer");
        mType = bundle.getString("type");
        mTitlePadding = bundle.getInt("title_padding");
        isFromPlayerActivity = bundle.getBoolean(IS_FROM_PLAYER_ON_LINE_VIDEO_ACTIVITY, false);
        isFromThread = bundle.getBoolean(FROM_THREAD, false);
        mShowClose = bundle.getBoolean("close");
        mFromTag = bundle.getString("tag");
        mHandler = new Handler();
        initView();
        // v4.1版本播放视频只有声音
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2)
            mVideoView = (LiveVideoView) exoVideoViewStub.inflate();
        else
            mVideoView = (LiveVideoView) exoVideoViewStub.inflate();
//            mVideoView = (LiveVideoView) nativeVideoViewStub.inflate();
        if (!TextUtils.isEmpty(mRefer)) {
            mVideoView.addHeader("referer",mRefer);
        }
        mVideoView.setLiveVideoViewCallback(liveVideoViewCallback);
        mMediaController.setListener(mediaControllerListener);
        mMediaController.setSwitchBg(false);
        if (!TextUtils.isEmpty(mTitle))
            mMediaController.setTitle(mTitle);
        if (mShowClose) {
            mCloseBtn.setVisibility(View.VISIBLE);
//            mMediaController.showCloseButton(true);
            mMediaController.showReturnButton(false);
            mMediaController.showTitle(true);
        }else {
            mCloseBtn.setVisibility(View.GONE);
//            mMediaController.showCloseButton(false);
            mMediaController.showReturnButton(true);
            mMediaController.showTitle(true);
        }
        mMediaController.paddingTitleLayout(mTitlePadding);
        showReturnButton(bundle.getBoolean("return", false));
        width = getResources().getDisplayMetrics().heightPixels;

        //// TODO: 16/3/26
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
//            getWindow().getDecorView().setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

//        if(checkNetworkIsWifi(getActivity())){
//            init();
//        }

        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        requestAudioFocus();
        init();
        registerReceiver();
        return view;
    }

    private void initView(){
//        nativeVideoViewStub= (ViewStub) view.findViewById(R.id.video_native_view);

        exoVideoViewStub= (ViewStub) view.findViewById(R.id.video_exo_view);

        mMediaController= (MediaController) view.findViewById(R.id.media_controller);

        mVideoCenterPage=view.findViewById(R.id.video_centre_page);

        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        mCloseBtn = (ImageButton) view.findViewById(R.id.close_out);
        mCloseBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (mMediaController != null)
                    mMediaController.closeVideo();
            }
        });
    }


    public void paddingTitleLayout(int padding) {
        if (mMediaController != null)
            mMediaController.paddingTitleLayout(padding);
    }

    private void init() {
//        if(isFromThread){
////            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//            mVideoView.play(mUrl);
//            mHandler.post(mTimeRunnable);
//            mMediaController.showFullLayout(false);
//            return;
//        }
        if (isFromPlayerActivity) {
            mVideoView.play(mUrl);
            VideoCenter.getDefault().upLoadVideoUr(getActivity(), mTitle, mUrl);
        } else {
            VideoCenter.getDefault().startCheckUrlInvalidThread(mType, mTitle, mUrl);
        }
        mHandler.post(mTimeRunnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        pausedByUser = false;
        isForegrond = true;
        getActivity().getWindow().getDecorView().setKeepScreenOn(true);
//        if (checkNetworkIsWifi(getActivity())) {

//        } else {
//            stopPlay();
//        }
        if (mCurrentHashMap.containsKey(mUrl)) {
            mCurrentPosition = mCurrentHashMap.get(mUrl);
        }
        if (mWakeLock == null)
            mWakeLock = ((PowerManager)getActivity().getSystemService(Context.POWER_SERVICE)).newWakeLock(
                    PowerManager.SCREEN_DIM_WAKE_LOCK, getClass().getSimpleName());
        try {
            if (!mWakeLock.isHeld())
                mWakeLock.acquire();
        } catch (Exception e) {
            Log.e(tag, e.getMessage());
        }
//        if(mMediaController!=null){
//            mMediaController.show();
//        }
    }

    private void startPlayerOnLineVideoActivity() {
        if (!isForegrond)
            return;
        showToast(getActivity(), "打开WebView播放：" + mTitle + "----" + mType);
//        Intent intent = new Intent(getActivity(), PlayerOnLineVideoActivity.class);
//        intent.putExtra("url", mTitle);
//        intent.putExtra("type", mType);
//        intent.putExtra(FROM_VIDEO_ACTIVITY, true);
//        startActivity(intent);
    }

//    @Override
//    public void finish() {
//        isForegrond = false;
//        setResult(PlayerOnLineVideoActivity.REQUEST_PLAYER_ONLINE_VIDEO_ACTIVITY);
//        overridePendingTransition(0, R.anim.slide_right_out);
//        super.finish();
//    }

    @Override
    public void onPause() {
        if (mVideoView == null) return;
        mPauseStatusPlaying = mVideoView.isPlaying();
        super.onPause();
        pausedByUser = true;
        isForegrond = false;
        stopPlay();
        mCurrentHashMap.put(mUrl, mCurrentPosition);
        if (mWakeLock != null) {
            try {
                if (mWakeLock.isHeld())
                    mWakeLock.release();
            } catch (Exception e) {
                Log.e(tag, e.getMessage());
            }
        }
    }

    private void onPrepareToPlay() {
        mVideoView.requestLayout();
        mMediaController.setDuration((int) mVideoView.getDuration());
        mMediaController.setVisibility(View.VISIBLE);
        mHandler.post(mUpdateRunnable);
        mVideoView.start();
        mVideoView.setKeepScreenOn(true);
        mTotalDuration = mVideoView.getDuration();
        mProgressBar.setMax((int)mTotalDuration);
        mVideoCenterPage.setVisibility(View.GONE);
    }

    public void onEventMainThread(VideoUrlInvalidCheckEvent event) {
        if (mVideoView != null && mVideoView.isPlaying())
            return;
        if (event.success && !TextUtils.isEmpty(event.stream) && event.stream.equals(mUrl)
                && !TextUtils.isEmpty(event.redirectUrl) && mVideoView != null) {
            // 检查直实地址
            mUrl = event.redirectUrl;
            mVideoView.play(event.redirectUrl);
        } else {
            showToast(getActivity(), "真实地址验证失败，应该用WebView打开视频");
//            startActivity(PlayerOnLineVideoActivity.getIntent(getActivity(), event.src, event.type));
            VideoCenter.getDefault().post(new CloseVideoEvent());
        }
    }


    private AudioManager mAudioManager;

    public AudioManager.OnAudioFocusChangeListener mAudioFocusListener = new AudioManager.OnAudioFocusChangeListener() {
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_LOSS:
                    stopPlay();
                    break;
            }
        }
    };

    private void requestAudioFocus() {
        if (mAudioManager != null) {
            mAudioManager.requestAudioFocus(mAudioFocusListener, AudioManager.STREAM_MUSIC,
                    AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
        }
    }

    private void abandonAudioFocus() {
        if (mAudioManager != null) {
            mAudioManager.abandonAudioFocus(mAudioFocusListener);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacks(mUpdateRunnable);
            mHandler.removeCallbacks(mTimeRunnable);
            mHandler = null;
        }
        if (null != mVideoView) {
            mVideoView.suspend();
            mVideoView = null;
        }
        unregisterReceiver();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            width = getResources().getDisplayMetrics().heightPixels;
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            width = getResources().getDisplayMetrics().widthPixels;
        }
        super.onConfigurationChanged(newConfig);
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (getActivity() == null || isDetached() || mMediaController == null || event == null)
            return true;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
//                mMediaController.show();
                x = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                mSmoothTotalDistance = 0;
                int lastX = (int) event.getX();
                if (mVideoView != null && (!mMediaController.ismScreenLocked())) {
                    if (Math.abs(lastX - x) > 20) {
                        mVideoView.seekTo((mVideoView.getDuration())
                                * mMediaController.getProgress() / 100);
                    }
                }

                if (mMediaController.isShowing()) {
                    if (Math.abs(lastX - x) > 20) {
                        mMediaController.hide();
                    } else {
                        if (event.getY() < (width - mMediaController.getSeekBarHight())) {
                            mMediaController.hide();
                        } else {
                            mMediaController.show();
                        }
                    }
                    mMediaController.mIsShowing.set(false);
                } else {
                    mMediaController.show();
                    mMediaController.mIsShowing.set(true);
                }
                mMediaController.showProgressTextViews(false);
                break;
            case MotionEvent.ACTION_CANCEL:
                mMediaController.showProgressTextViews(false);
                break;
        }
        if (mGestureDetector != null)
            mGestureDetector.onTouchEvent(event);
        return true;
    }

//    @Override
//    public void onBackPressed() {
//        if (mMediaController.ismScreenLocked()) {
//            mMediaController.show();
//            AppUtils.showToast(getActivity(), getString(R.string.video_lock_success));
//            return;
//        }
//        geta.onBackPressed();
//    }

    public void startPlay() {
        if(isComplete){
            mVideoView.seekTo(0);
            isComplete=false;
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2)
            mVideoView.seekTo(mCurrentPosition);

        mVideoView.start();
        mVideoView.setKeepScreenOn(true);
        mMediaController.setPlayState(MediaController.MEDIA_PLAY_STATE_START);
        mMediaController.setSwitchBg(false);
        mMediaController.hideController();
        mHandler.post(mUpdateRunnable);
        mVideoCenterPage.setVisibility(View.GONE);
//        VideoCenter.getDefault().post(new RequestAudioFocusEvent());
        requestAudioFocus();

    }

    private void stopPlay() {
        mVideoView.pause();
        mMediaController.setPlayState(MediaController.MEDIA_PLAY_STATE_STOP);
        mMediaController.setSwitchBg(true);
        mVideoView.setKeepScreenOn(true);
        mHandler.removeCallbacks(mUpdateRunnable);
        //todo 写两次才生效，未找到原因先这样写
        mVideoView.pause();
    }

    private void stopPlayWithNoBuffer() {
//        stopPlay();
        mVideoView.pause();
        mMediaController.setPlayState(MediaController.MEDIA_PLAY_STATE_STOP);
//        mMediaController.setSwitchBg(true);
        mVideoView.setKeepScreenOn(true);
        mHandler.removeCallbacks(mUpdateRunnable);
        mVideoCenterPage.setVisibility(View.VISIBLE);
        mMediaController.setSwitchBg(false);
    }

    private void updateProgress() {
        mCurrentPosition = mVideoView.getCurrentPosition();

        mCurrentPosition = mCurrentPosition + mSmoothTotalDistance;
        mCurrentPosition = mCurrentPosition < 0 ? 0 : mCurrentPosition;
        mCurrentPosition = mCurrentPosition > mTotalDuration ? (int)mTotalDuration : mCurrentPosition;
        mMediaController.setPosition(mCurrentPosition);
        mProgressBar.setProgress(mCurrentPosition);
        if (mCurrentPosition >= 100) {
            if ((mVideoCenterPage != null) && (mVideoCenterPage.getVisibility() == View.VISIBLE)) {
                mVideoCenterPage.setVisibility(View.GONE);
                mMediaController.show();
            }
        }
    }

    private  void registerReceiver(){
        IntentFilter filter=new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        myReceiver=new ConnectionChangeReceiver();
        getActivity().registerReceiver(myReceiver, filter);
    }

    private  void unregisterReceiver(){
        getActivity().unregisterReceiver(myReceiver);
    }

    @Override
    public void onClick(View v) {
    }

    //// TODO: 16/4/20
    public class ConnectionChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!enableShowDialog) {
                return;
            }
            int status = checkNetworkIsWifi(getActivity());
            switch (status) {
                case 0:
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // 如果播放器未准备好或者已经被回收，则不操作（umeng上bug）
                            if (mVideoView == null)
                                return;
                            startPlay();
                        }
                    }, 500);

                    break;
                case 1:
                    stopPlay();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startPlay();
                        }
                    },500);
                    showToast(getActivity(), getString(R.string.network_notify_live_video));
                    break;
                case 2:
                    showToast(getActivity(), getString(R.string.network_disable_exit));
                    stopPlay();
                    break;
            }
        }
    }

    public boolean isPlaying(){
        return mVideoView.isPlaying();
    }

    public void showTitle(boolean show){
        mMediaController.showTitle(show);
    }

    public void showReturnButton(boolean show){
        mMediaController.showReturnButton(show);
    }

    public void showCloseButton(boolean show){
        mCloseBtn.setVisibility(show ? View.VISIBLE : View.GONE);
//        mMediaController.showCloseButton(show);
    }

    public void setControllerTitlebarBackground(int color){
        mMediaController.setTitlebarBackground(color);
    }

    public void showBottomLine(boolean show){
        mMediaController.showBottomLine(show);
    }
    
    public void showMediaController(boolean show){
        if(show){
            mMediaController.show();
        }else{
            mMediaController.hideController();
        }
    }

    private class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {

        public MyGestureDetector() {
            super();
        }

        // Touch了不移动一直Touch down时触发
        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);
        }

        // Touch了滑动时触发
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            mMediaController.getSeekBarHight();
            if (!mMediaController.ismScreenLocked()) {
                if (Math.abs(distanceX) / 3 > Math.abs(distanceY)) {
                    mMediaController.showProgressTextViews(true);
                    mMediaController.show();
                    int mSmoothDistance = (int)(-distanceX * (width / 50));
                    mSmoothTotalDistance += mSmoothDistance;
                }

            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return super.onFling(e1, e2, velocityX, velocityY);
        }

        // Touch了还没有滑动时触发
        @Override
        public void onShowPress(MotionEvent e) {
            super.onShowPress(e);
        }

        // Touch down时触发
        @Override
        public boolean onDown(MotionEvent e) {
            return super.onDown(e);
        }

        // 双击的第二下Touch down时触发
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            return super.onDoubleTap(e);
        }

        // 双击的第二下Touch down和up都会触发，可用e.getAction()区分
        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            return super.onDoubleTapEvent(e);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return super.onSingleTapConfirmed(e);
        }
    }


}


