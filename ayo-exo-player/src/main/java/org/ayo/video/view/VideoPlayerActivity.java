package org.ayo.video.view;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

import org.ayo.video.R;
import org.ayo.video.model.NewsVideoEntity;

/**
 * Created by Administrator on 2017/8/22.
 */

public abstract class VideoPlayerActivity extends AppCompatActivity implements VideoControllerActivity {


    View mVideoLayout;

    FrameLayout mContainer;

    SimpleDraweeView mVideoThumb;

    TextView mVideoDuration;

    TextView mRetryBtn;

    ImageView mPlayButton;

    TextView mVideoSize;

    TextView mContinueBtn;

    ImageView mBack;

    LinearLayout mVideoFailedLayout;

    VideoFragment mVideoFragment;

    private boolean mVideoIsFullScreen;

    private boolean mHasVideoCompleted = true;

    private NewsVideoEntity mVideoEntity;

    protected int getLayoutId(){
        return R.layout.activity_video_player;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        mVideoLayout = findViewById(R.id.video_layout);
        mContainer = (FrameLayout) findViewById(R.id.video_container);
        mVideoThumb = (SimpleDraweeView) findViewById(R.id.thumb);
        mVideoDuration = (TextView) findViewById(R.id.duration);
        mRetryBtn = (TextView) findViewById(R.id.retry_btn);
        mPlayButton = (ImageView) findViewById(R.id.play_btn);
        mVideoSize = (TextView) findViewById(R.id.video_size);
        mContinueBtn = (TextView) findViewById(R.id.continue_play);
        mBack = (ImageView) findViewById(R.id.back);
        mVideoFailedLayout = (LinearLayout) findViewById(R.id.video_failed_layout);


        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mVideoIsFullScreen) {
                    onScreenRequired(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                } else {
                    finish();
                }
            }
        });


        mRetryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVideoFailedLayout.setVisibility(View.GONE);
                requestVideoInfo();
            }
        });

        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (checkNetworkIsWifi(getApplicationContext())) {
                    case 0:
                        localPlay();
                        break;
                    case 1:
                        findViewById(R.id.tip_layout).setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        showToast(VideoPlayerActivity.this, getString(R.string.network_disable_exit));
                        break;
                }
            }
        });

        mContinueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                localPlay();
            }
        });

        mContainer.setOnTouchListener(mVideoTouchListener);

        requestVideoInfo();

    }

    protected abstract void requestVideoInfo();

    protected void onLoadOk(NewsVideoEntity videoEntity){

        mVideoEntity = videoEntity;
        if(mVideoEntity != null){
            mVideoThumb.setImageURI(parse(mVideoEntity.video_thumb));
            mVideoDuration.setText(TextUtils.isEmpty(mVideoEntity.video_time)
                    ? "" : mVideoEntity.video_time);
            mVideoSize.setText(getString(R.string.video_size_new,
                    TextUtils.isEmpty(mVideoEntity.video_time)
                            ? getString(R.string.not_available_tip)
                            : mVideoEntity.video_time,
                    TextUtils.isEmpty(mVideoEntity.size)
                            ? getString(R.string.not_available_tip)
                            : mVideoEntity.size));
            if (checkNetworkIsWifi(this) == 0) {
                if (!TextUtils.isEmpty(mVideoEntity.video_hash)
                        && "player".equals(mVideoEntity.video_mode)) {
                    mPlayButton.setVisibility(View.GONE);
                    mVideoDuration.setVisibility(View.GONE);
                    mVideoThumb.setVisibility(View.GONE);
                    FragmentTransaction mFragmentTransaction = getSupportFragmentManager()
                            .beginTransaction();
                    int padding = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT ? getStatusBarHeight(this) : 0;
//                    mVideoFragment = VideoFragment.newInstance(
//                            getVideoUrl(mVideoEntity.video_hash),
//                            mVideoEntity.video_src, null, "local",
//                            padding, false, false, false, "");

                    //http://7xo0ny.com1.z0.glb.clouddn.com/111.mp4
                    //http://7xo0ny.com1.z0.glb.clouddn.com/12.mp4
                    if(mVideoFragment != null && !mVideoFragment.isDetached()){
                        mFragmentTransaction.remove(mVideoFragment);
                        mVideoFragment = null;
                    }

                    if(mVideoFragment == null){
                        mVideoFragment = VideoFragment.newInstance(mVideoEntity.video_hash,
                                mVideoEntity.video_src, null, "local",
                                padding, true, false, false, "");
                        mFragmentTransaction.replace(R.id.video_container,
                                mVideoFragment);
                        mFragmentTransaction.show(mVideoFragment);
                        mFragmentTransaction.commitAllowingStateLoss();
                    }else{
                        mVideoFragment.startPlay();
                    }

                    mHasVideoCompleted = false;
                } else {
                    if (mVideoFragment != null) {
                        FragmentTransaction mFragmentTransaction = getSupportFragmentManager()
                                .beginTransaction();
                        mFragmentTransaction.remove(mVideoFragment);
                        mFragmentTransaction.commitAllowingStateLoss();
                        mVideoFragment = null;
                    }
                    mPlayButton.setVisibility(View.VISIBLE);
                    mVideoDuration.setVisibility(View.VISIBLE);
                    mVideoThumb.setVisibility(View.VISIBLE);
                }

            } else {
                findViewById(R.id.tip_layout).setVisibility(View.VISIBLE);
            }
        }else {
            mVideoFailedLayout.setVisibility(View.VISIBLE);
        }

    }

    private View.OnTouchListener mVideoTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (mVideoFragment != null) {
                mVideoFragment.onTouchEvent(event);
            }
            return true;
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mVideoIsFullScreen) {
                // 设置为竖屏
                onScreenRequired(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void setVideoFullScreen(int orientation) {
        if (mVideoFragment == null) {
            return;
        }
        if (orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            // 竖屏
            mVideoIsFullScreen = false;
            mVideoLayout.getLayoutParams().height
                    = (int) getResources().getDimension(R.dimen.video_fixed_height);

//            paddingTitle(AppUtils.getStatusBarHeight(this));
//            if (Const.AppConstant.VIDEOMODE == Const.AppConstant.NIGHTMODE) {
//                AppUtils.setKitkatStatusBarBackground(this, R.color.night_status_bar_background);
//            }
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
//                    && mPortraitSystemUiVisibility != -1)
//                getWindow().getDecorView().setSystemUiVisibility(mPortraitSystemUiVisibility);
        } else {
            mVideoIsFullScreen = true;
            mVideoLayout.getLayoutParams().height = LinearLayout.LayoutParams.MATCH_PARENT;

//            paddingTitle(0);
//            if (Const.AppConstant.VIDEOMODE == Const.AppConstant.NIGHTMODE) {
//                AppUtils.setKitkatStatusBarBackground(this, R.color.transparent);
//            }
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                mPortraitSystemUiVisibility = getWindow().getDecorView()
//                        .getWindowSystemUiVisibility();
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                    getWindow().getDecorView()
//                            .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                                    | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE);
//                }
//            }
        }
    }


    public void localPlay() {
        if (mVideoEntity != null) {
            if ("player".equals(mVideoEntity.video_mode)) {
                mHasVideoCompleted = false;
                mPlayButton.setVisibility(View.GONE);
                mVideoDuration.setVisibility(View.GONE);
                mVideoThumb.setVisibility(View.GONE);
                findViewById(R.id.tip_layout).setVisibility(View.GONE);
                FragmentTransaction mFragmentTransaction = getSupportFragmentManager()
                        .beginTransaction();
                int padding = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT ? getStatusBarHeight(this) : 0;//AppUtils.getStatusBarHeight(BaseApplication.app) : 0;

                //本地上传视频，如果播放器播放不了，不跳转到H5页面

                if(mVideoFragment != null && !mVideoFragment.isDetached()){
                    mFragmentTransaction.remove(mVideoFragment);
                    mVideoFragment = null;
                }
                if(mVideoFragment == null){
                    mVideoFragment = VideoFragment.newInstance(mVideoEntity.video_hash,
                            mVideoEntity.video_src,
                            null, "local", padding, true, false, false, "TAG");
                    mFragmentTransaction.replace(R.id.video_container, mVideoFragment);
                    mFragmentTransaction.show(mVideoFragment);
                    mFragmentTransaction.commitAllowingStateLoss();
                }else{
                    mVideoFragment.startPlay();
                }

                mHasVideoCompleted = false;
                mContainer.setOnTouchListener(mVideoTouchListener);
            } else {

//                if ("h5".equals(mVideoEntity.getVideo_mode())) {
//                    WebActivity.start(context, mVideoEntity.getVideo_src(),
//                            mVideoEntity.getVideo_src());
//                } else if (!TextUtils.isEmpty(mVideoEntity.getVideo_src())) {
//                    Intent intent = new Intent();
//                    intent.setAction("android.intent.action.VIEW");
//                    Uri content_url = AppUtils.parse(mVideoEntity.getVideo_src());
//                    intent.setData(content_url);
//                    startActivity(intent);
//                }
            }
        }
    }
    public String getVideoUrl(String hash) {
        String url = "https://api.dongqiudi.com" + "/video/play/" + hash;
        if (url.startsWith("https")) {
            url = url.replaceAll("https", "http");
        }
        return url;
    }

    @Override
    public void onScreenRequired(int requestedOrientation) {
        setVideoFullScreen(requestedOrientation);
        setRequestedOrientation(requestedOrientation);
    }

    @Override
    public void onVideoComplete() {
        mHasVideoCompleted = true;
        mVideoFragment.showMediaController(false);
        mPlayButton.setBackgroundResource(R.drawable.icon_video_replay_btn);
        mPlayButton.setVisibility(View.VISIBLE);
        mContainer.setOnTouchListener(null);
        mVideoFragment.onTouchEvent(null);
    }

    @Override
    public void onVideoStart() {
        mPlayButton.setVisibility(View.GONE);
    }

    public static Uri parse(String url) {
        if (TextUtils.isEmpty(url))
            return Uri.parse("");
        return Uri.parse(url);
    }

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


    public static int getStatusBarHeight(Context context) {
        final Resources resources = context.getResources();
        final int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        int height;
        if (resourceId > 0)
            height = resources.getDimensionPixelSize(resourceId);
        else
            height = (int) Math.ceil(25 * resources.getDisplayMetrics().density);
        return height;
    }

    public static void showToast(Activity activity, String s){
        Toast.makeText(activity, s, Toast.LENGTH_LONG).show();
    }
}
