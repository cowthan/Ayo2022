package org.ayo.fringe.ui.fragment;

import android.content.res.AssetFileDescriptor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.volokh.danylo.video_player_manager.manager.PlayerItemChangeListener;
import com.volokh.danylo.video_player_manager.manager.SingleVideoPlayerManager;
import com.volokh.danylo.video_player_manager.manager.VideoPlayerManager;
import com.volokh.danylo.video_player_manager.meta.MetaData;
import com.volokh.danylo.video_player_manager.ui.MediaPlayerWrapper;
import com.volokh.danylo.video_player_manager.ui.VideoPlayerView;

import org.ayo.fringe.R;
import org.ayo.fringe.ui.base.BaseFrgFragment;

import java.io.IOException;

/**
 */
public class GuideFrgFragmentVedioFragment extends BaseFrgFragment {

    VideoPlayerView mPlayer;
    VideoPlayerManager<MetaData> mVideoPlayerManager;
    private String assetVedioPath = "";

    public void setAssetVedioPath(String path){
        this.assetVedioPath = path;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.wb_frag_guide_vedio_page;
    }

    @Override
    protected void onCreate2(View view, @Nullable Bundle bundle) {
        mPlayer = (VideoPlayerView) findViewById(R.id.player);
        mPlayer.addMediaPlayerListener(new MediaPlayerWrapper.MainThreadMediaPlayerListener() {
            @Override
            public void onVideoSizeChangedMainThread(int width, int height) {
            }

            @Override
            public void onVideoPreparedMainThread() {
                // When video is prepared it's about to start playback. So we hide the cover
                //videoViewHolder.mCover.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onVideoCompletionMainThread() {
                play();
            }

            @Override
            public void onErrorMainThread(int what, int extra) {
            }

            @Override
            public void onBufferingUpdateMainThread(int percent) {
            }

            @Override
            public void onVideoStoppedMainThread() {

                // Show the cover when video stopped
                //videoViewHolder.mCover.setVisibility(View.VISIBLE);
            }
        });

        mVideoPlayerManager = new SingleVideoPlayerManager(new PlayerItemChangeListener() {
            @Override
            public void onPlayerItemChanged(MetaData metaData) {

            }
        });

        //如果只首页直接播放的话，其他页面划过来时，会他妈有段时间空白，所以不如大家都播放，显示时就重新播放一下，反正视频不带声音
//        if(isTheFirstPage){
//            play();
//        }

        play();
    }

    @Override
    protected void onDestroy2() {
        try{
            stop();
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }catch (Exception e){

        }
    }

    @Override
    protected void onPageVisibleChanged(boolean b, boolean b1, @Nullable Bundle bundle) {

    }


    private void play(){
        AssetFileDescriptor mVideoFileDecriptor_sample_1 = null;
        try {
            mVideoFileDecriptor_sample_1 = getActivity().getAssets().openFd(assetVedioPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mVideoPlayerManager.playNewVideo(null, mPlayer, mVideoFileDecriptor_sample_1);
    }

    private void stop(){
        if(mVideoPlayerManager != null){
            mVideoPlayerManager.stopAnyPlayback();
        }
    }
}
