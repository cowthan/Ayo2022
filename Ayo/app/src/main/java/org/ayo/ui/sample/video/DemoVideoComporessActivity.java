package org.ayo.ui.sample.video;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import org.ayo.component.MasterActivity;
import org.ayo.core.Lang;
import org.ayo.imagepicker.ThumbModel;
import org.ayo.imagepicker.VideoListActivity;
import org.ayo.log.Trace;
import org.ayo.sample.R;
import org.ayo.ui.sample.video.compress.BaseCompressListener;
import org.ayo.ui.sample.video.compress.VideoCompress;
import org.ayo.ui.sample.video.player.VideoPlayerCommonActivity;

import java.util.List;

/**
 * Created by qiaoliang on 2017/9/29.
 */

public class DemoVideoComporessActivity extends MasterActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.ac_video_compress;
    }
    TextView tv_info;
    VideoCompress mCompress;

    @Override
    protected void onCreate2(View view, @Nullable Bundle bundle) {

        tv_info = (TextView) findViewById(R.id.tv_info);
        findViewById(R.id.btn_reset).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                VideoListActivity.startForResult(getActivity());
            }
        });

        mCompress = new VideoCompress();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        List<ThumbModel> list2 = VideoListActivity.tryToHandleResult(this, requestCode, resultCode, data);
        if(Lang.isNotEmpty(list2)){
            tv_info.setText("选了：" + Lang.count(list2) + "个视频\n");
//            tv_info.append(JsonUtils.toJsonPretty(list2));
            for(int i = 0; i < list2.size(); i++){
                compress(list2.get(i).getPath(), list2.get(i).duration);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void compress(String path, long duration){
        //参数1：本地路径
        //参数2：

        //this表示该对话框是针对当前Activity的
        final ProgressDialog progressDialog = new ProgressDialog(this);
        //设置最大值为100
        progressDialog.setMax(100);
        progressDialog.setProgress(0);
        //设置进度条风格STYLE_HORIZONTAL
        progressDialog.setProgressStyle(
                ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("压缩中...");
        // 设置ProgressDialog 的进度条是否不明确
        progressDialog.setIndeterminate(false);
        // 设置ProgressDialog 是否可以按退回按键取消
        progressDialog.setCancelable(false);

        mCompress.startCompress(path, new BaseCompressListener() {
            @Override
            public void onStart() {
                tv_info.append("开始压缩...\n");
                progressDialog.show();
            }

            @Override
            public void onProgress(long total, long current) {
                double progress = (current * 100.0 / total);
                Trace.e("compress", "进度：" + progress + "-----" + current + "/" + total);
                //tv_info.append("进度：" + progress + "-----" + current + "/" + total + "\n");
                progressDialog.setProgress((int) (progress));
            }

            @Override
            public void onSuccess(String outputPath) {
                tv_info.append("结束：" + outputPath + "\n");
                progressDialog.dismiss();
                Intent intent = new Intent(getActivity(), VideoPlayerCommonActivity.class);
                intent.putExtra("url", "file://" + outputPath);
                getActivity().startActivity(intent);
            }

            @Override
            public void onError(String error) {
                tv_info.append("出错：" + error + "\n");
                progressDialog.dismiss();
            }
        }, duration).compress();
    }

    @Override
    protected void onDestroy2() {

    }

    @Override
    protected void onPageVisibleChanged(boolean b, boolean b1, @Nullable Bundle bundle) {

    }
}
