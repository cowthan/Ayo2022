
package org.ayo.video.view;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import org.ayo.video.R;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by hujinghui on 14/11/6.
 */
public class MediaController extends RelativeLayout implements View.OnClickListener {

    public final static int MEDIA_PLAY_STATE_START = 0x0001;

    public final static int MEDIA_PLAY_STATE_STOP = 0x0002;
    private final static String tag = "video";
    public AtomicBoolean mIsShowing = new AtomicBoolean(false);

    TextView mTitle;


    ImageButton mReturnBtn;


    ImageButton mClose;


    LinearLayout mTitleLayout;


    TextView mProcessTime;


    TextView mProcessMidTime;


    SeekBar mSeekbar;


    TextView mTotleTime;


    ImageView mPlay;


    View mFullscreen_lay;


    View mPlay_lay;


    ImageView mFullscreen;


    View mDialog;


    LinearLayout mControlLayout;


    ImageButton mMediacontrollerLock;
    private int playState = MEDIA_PLAY_STATE_STOP;
    private volatile boolean mScreenLocked = false;
    private Handler mHandler = new Handler();
    private MediaControllerListener mediaControllerListener;

    private int duration, position;

    private String mVideoTotleTime, mVideoCurrentTime;

    private Boolean mSeekBarIsPressed = false;
    private Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            if (!mSeekBarIsPressed) {
                showViews(false);
                mIsShowing.set(false);
            }
        }
    };
    private SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            Log.e(tag, "onProgressChanged");

            mSeekBarIsPressed = seekBar.isPressed();
            if (mediaControllerListener != null) {
                mediaControllerListener.onSeekBarProgressChanged(i);
            }
            setPositionFormProcessMidTime(duration * i / 100);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            Log.e(tag, "onStartTrackingTouch");
            showProgressTextViews(true);
            mSeekBarIsPressed = seekBar.isPressed();
            if (mediaControllerListener != null) {
                mediaControllerListener.onStartTrackingTouch(seekBar.isPressed());
            }

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            Log.e(tag, "onStopTrackingTouch");
            showProgressTextViews(false);
            mSeekBarIsPressed = seekBar.isPressed();
            if (mediaControllerListener != null) {
                mediaControllerListener.onProgress(seekBar.getProgress());
            }
            hide();
        }

    };

    public MediaController(Context context) {
        super(context);
    }

    public MediaController(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MediaController(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        mControlLayout = (LinearLayout) findViewById(R.id.control_layout);
        mFullscreen = (ImageView) findViewById(R.id.fullscreen);
        mProcessTime = (TextView) findViewById(R.id.process_time);
        mTitleLayout = (LinearLayout) findViewById(R.id.title_layout);
        mReturnBtn = (ImageButton) findViewById(R.id.returnBtn);
        mTotleTime = (TextView) findViewById(R.id.totle_time);
        mMediacontrollerLock = (ImageButton) findViewById(R.id.mediacontroller_lock);
        mClose = (ImageButton) findViewById(R.id.close);
        mClose.setVisibility(GONE);
        mSeekbar = (SeekBar) findViewById(R.id.seekbar);
        mFullscreen_lay =  findViewById(R.id.fullscreen_lay);
        mPlay_lay =  findViewById(R.id.play_lay);
        mProcessMidTime = (TextView) findViewById(R.id.process_mid_time);
        mPlay = (ImageView) findViewById(R.id.play);
        mTitle = (TextView) findViewById(R.id.title);
        mDialog =  findViewById(R.id.dialog);
        mReturnBtn.setOnClickListener(this);
        mPlay_lay.setOnClickListener(this);
        mFullscreen_lay.setOnClickListener(this);
        mMediacontrollerLock.setOnClickListener(this);
        mClose.setOnClickListener(this);
        initViews();
        super.onFinishInflate();
    }

    private void initViews() {
        mSeekbar.setOnSeekBarChangeListener(onSeekBarChangeListener);
    }

    public boolean isShowing() {
        return mIsShowing.get();
    }

    public void show() {
        showViews(true);
        mHandler.removeCallbacks(mHideRunnable);
        mHandler.postDelayed(mHideRunnable, 4000);
        mIsShowing.set(true);
    }

    public void showViews(boolean show) {
        if (mTitleLayout.getVisibility() != (show && !mScreenLocked ? VISIBLE : GONE)) {
            mTitleLayout.setVisibility(show && !mScreenLocked ? VISIBLE : GONE);
        }

        if (mControlLayout.getVisibility() != (show && !mScreenLocked ? VISIBLE : GONE)) {
            mControlLayout.setVisibility(show && !mScreenLocked ? VISIBLE : GONE);
        }
    }

    public void showProgressTextViews(boolean show) {
        if (mProcessMidTime.getVisibility() != (show ? VISIBLE : GONE)) {
            mProcessMidTime.setVisibility(show ? VISIBLE : GONE);
        }
    }

    public void hide() {
        mHandler.removeCallbacks(mHideRunnable);
        mHideRunnable.run();
    }

    public void hideController(){
        mHandler.removeCallbacks(mHideRunnable);
        showViews(false);
        mIsShowing.set(false);
    }


    @Override
    public void onClick(View view) {
        Log.d(tag, "onClick");
        int id = view.getId();
        if(id == R.id.returnBtn){
            if (mediaControllerListener != null)
                mediaControllerListener.onExit();
        }else if(id == R.id.close){
            closeVideo();
        }else if(id == R.id.play_lay){
            if (mediaControllerListener != null) {
                if (playState == MEDIA_PLAY_STATE_START) {
                    mPlay.setBackgroundResource(R.drawable.playing_start);
                    mediaControllerListener.onStop();
                    playState = MEDIA_PLAY_STATE_STOP;
                } else {
                    mPlay.setBackgroundResource(R.drawable.playing_pause);
                    mediaControllerListener.onStart();
                    playState = MEDIA_PLAY_STATE_START;
                }
            }
        }else if(id == R.id.fullscreen_lay){
            if (mediaControllerListener != null) {
                int requestedOrientation = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                        : ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                mFullscreen
                        .setBackgroundResource((requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) ? R.drawable.btn_landscape
                                : R.drawable.btn_landscape);
                mediaControllerListener.onScreenRequired(requestedOrientation);
            }
        }else if(id == R.id.mediacontroller_lock){
            lock(!mScreenLocked);
        }else if(id == R.id.media_controller){

        }

    }

    public void closeVideo() {
        if (mediaControllerListener != null)
            mediaControllerListener.onClose();
    }

    private void lock(boolean toLock) {
        if (toLock) {
            mTitleLayout.setVisibility(View.INVISIBLE);
            mDialog.setVisibility(View.INVISIBLE);
            mControlLayout.setVisibility(View.INVISIBLE);
            mProcessMidTime.setVisibility(View.INVISIBLE);
        } else {
            mTitleLayout.setVisibility(View.VISIBLE);
            mDialog.setVisibility(View.INVISIBLE);
            mControlLayout.setVisibility(View.VISIBLE);
            mProcessMidTime.setVisibility(View.INVISIBLE);
        }
        if (mediaControllerListener != null) {
            int requestedOrientation = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    : ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
            mFullscreen
                    .setBackgroundResource((requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) ? R.drawable.btn_landscape
                            : R.drawable.btn_landscape);
            mediaControllerListener.onScreenRequired(requestedOrientation);
        }
        mScreenLocked = toLock;
    }

    public void paddingTitleLayout(int padding) {
        if (mTitleLayout != null) {
            mTitleLayout.setPadding(0, padding, 0, 0);
        }
    }

    public void setDuration(int duration) {
        this.duration = duration;
        int m = duration / 1000 / 60;
        int s = duration / 1000 % 60;
        Log.d(tag, "" + duration + "   m:" + m + " s:" + s);
        mTotleTime.setText(String.valueOf(m < 10 ? ("0" + m) : m) + ":"
                + String.valueOf(s < 10 ? ("0" + s) : s));
        mVideoTotleTime = String.valueOf(m < 10 ? ("0" + m) : m) + ":"
                + String.valueOf(s < 10 ? ("0" + s) : s);
    }

    public int getTotleTime() {
        return Integer.parseInt(mVideoTotleTime);
    }

    public int getSeekBarHight() {
        int SeekBarHight = 0;
        if (mSeekbar != null && mSeekbar.getVisibility() != View.GONE) {
            SeekBarHight = mSeekbar.getHeight();
        }

        // value 120
        return SeekBarHight;
    }

    public void setPosition(int position) {
        this.position = position;
        int m = position / 1000 / 60;
        int s = position / 1000 % 60;
        mProcessTime.setText(String.valueOf(m < 10 ? ("0" + m) : m) + ":"
                + String.valueOf(s < 10 ? ("0" + s) : s));
        if (duration != 0)
            mSeekbar.setProgress(position * 100 / duration);
        mVideoCurrentTime = String.valueOf(m < 10 ? ("0" + m) : m) + ":"
                + String.valueOf(s < 10 ? ("0" + s) : s);
    }

    public void setPositionFormProcessMidTime(int position) {
        this.position = position;
        int m = position / 1000 / 60;
        int s = position / 1000 % 60;
        mProcessTime.setText(String.valueOf(m < 10 ? ("0" + m) : m) + ":"
                + String.valueOf(s < 10 ? ("0" + s) : s));
        mVideoCurrentTime = String.valueOf(m < 10 ? ("0" + m) : m) + ":"
                + String.valueOf(s < 10 ? ("0" + s) : s);
        if (null != mProcessMidTime && !TextUtils.isEmpty(mVideoTotleTime)) {
            mProcessMidTime.setText(mVideoCurrentTime + "/" + mVideoTotleTime);
        }
    }

    public void setPlayState(int state) {
        this.playState = state;
    }

    public void setTitle(String title) {
        mTitle.setText(title);
    }

    public int getProgress() {
        return mSeekbar.getProgress();
    }

    public void setProgress(int progress) {
        mSeekbar.setProgress(progress);
        mProcessTime.setText(String.valueOf(progress));
    }

    public void setSecondProgress(int sp) {
        mSeekbar.setSecondaryProgress(sp);
    }

    public void setListener(MediaControllerListener listener) {
        this.mediaControllerListener = listener;
    }

    public boolean ismScreenLocked() {
        return mScreenLocked;
    }

    public void setSwitchBg(boolean isPlay) {
        if (isPlay) {
            mPlay.setBackgroundResource(R.drawable.playing_start);
        } else {
            mPlay.setBackgroundResource(R.drawable.playing_pause);
        }

    }

    public void showTitle(boolean show){
        mTitle.setVisibility(show? View.VISIBLE: View.INVISIBLE);
    }

    public void showReturnButton(boolean show){
        mReturnBtn.setVisibility(show? View.VISIBLE: View.INVISIBLE);
    }

//    public void showCloseButton(boolean show){
//        mClose.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
//    }

    public void setTitlebarBackground(int color){
        findViewById(R.id.titlebar).setBackgroundColor(color);
    }

    public void showBottomLine(boolean show){
        findViewById(R.id.bottom_line).setVisibility(show? View.VISIBLE: View.GONE);
    }

    public void showTitleLayout(boolean show){
        mTitleLayout.setVisibility(show? View.VISIBLE: View.INVISIBLE);
    }

    public void showFullLayout(boolean show){
        mFullscreen_lay.setVisibility(show? View.VISIBLE: View.GONE);
    }

    public interface MediaControllerListener {
        void onStart();

        void onStop();

        void onProgress(int progress);

        void onScreenRequired(int requestedOrientation);

        void onExit();

        void onSeekBarProgressChanged(int changeProgress);

        void onStartTrackingTouch(Boolean isStart);
        //关闭按钮回调
        void onClose();
    }

}
