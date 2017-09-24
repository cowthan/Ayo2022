package org.ayo.ui.sample.update;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.ayo.http.utils.JsonUtils;
import org.ayo.sample.R;
import org.ayo.ui.sample.BasePage;
import org.ayo.update.IUpdateParser;
import org.ayo.update.UpdateInfo;
import org.ayo.update.UpdateManager;
import org.ayo.update.UpdateUtil;


public class UpdateTestActivity extends BasePage implements View.OnClickListener {

    String mCheckUrl = "http://114.215.81.196/service/apps/export/version?appid=2&lang=0"; //http://client.waimai.baidu.com/message/updatetag";
//    String mUpdateUrl = "http://114.215.81.196/service/apps/export/version?appid=2&lang=0"; //http://mobile.ac.qq.com/qqcomic_android.apk";
    @Override
    protected int getLayoutId() {
        return R.layout.ac_update_test;
    }

    @Override
    protected void onCreate2(View view, @Nullable Bundle bundle) {

        UpdateManager.setDebuggable(true);
        UpdateManager.setWifiOnly(false);
        UpdateManager.setUrl(mCheckUrl, "yyb");
        UpdateManager.check(getActivity());
        check(false, true, false, false, true, 998);

        findViewById(R.id.check_update).setOnClickListener(this);
        findViewById(R.id.check_update_cant_ignore).setOnClickListener(this);
        findViewById(R.id.check_update_force).setOnClickListener(this);
        findViewById(R.id.check_update_no_newer).setOnClickListener(this);
        findViewById(R.id.check_update_silent).setOnClickListener(this);
        findViewById(R.id.clean).setOnClickListener(this);
    }

    @Override
    protected void onDestroy2() {

    }

    @Override
    protected void onPageVisibleChanged(boolean b, boolean b1, @Nullable Bundle bundle) {

    }
    void check(boolean isManual, final boolean hasUpdate, final boolean isForce, final boolean isSilent, final boolean isIgnorable, final int
            notifyId) {
        UpdateManager.create(getActivity())
//                .setChecker(new IUpdateChecker() {
//                    @Override
//                    public void check(ICheckAgent agent, String url) {
//                        Log.e("ezy.update", "checking");
//                        agent.setInfo("");
//                    }
//                })
                .setUrl(mCheckUrl).setManual(isManual).setNotifyId(notifyId).setParser(new IUpdateParser() {
            @Override
            public UpdateInfo parse(String source) throws Exception {
                Log.e("update", "返回：" + source);
                MyUpdateInfo m = JsonUtils.getBean(source, MyUpdateInfo.class);
                UpdateInfo info = new UpdateInfo();

                info.hasUpdate = m.result == 0;
                info.updateContent = m.context; //"• 支持文字、贴纸、背景音乐，尽情展现欢乐气氛；\n• 两人视频通话支持实时滤镜，丰富滤镜，多彩心情；\n• 图片编辑新增艺术滤镜，一键打造文艺画风；\n• 资料卡新增点赞排行榜，看好友里谁是魅力之王。";
                info.versionCode =m.code; // 587;
                info.versionName = m.version;// "v5.8.7";
                info.url = m.url; //mUpdateUrl;
                info.md5 = "update-app"; //"56cf48f10e4cf6043fbf53bbbc4009e3";
                info.size = m.file_size; // 10149314;
                info.isForce = m.compel != 0; //isForce;
                info.isIgnorable = false; //isIgnorable;
                info.isSilent = false; //isSilent;
                return info;
            }
        }).check();
    }

    private static class MyUpdateInfo{
        public int result;
        public int code;
        public String version;
        public String url;
        public long file_size;
        public String context;
        public int compel;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.check_update:
                check(true, true, false, false, true, 998);
                break;
            case R.id.check_update_cant_ignore:
                check(true, true, false, false, false, 998);
                break;
            case R.id.check_update_force:
                check(true, true, true, false, true, 998);
                break;
            case R.id.check_update_no_newer:
                check(true, false, false, false, true, 998);
                break;
            case R.id.check_update_silent:
                check(true, true, false, true, true, 998);
                break;
            case R.id.clean:
                UpdateUtil.clean(getActivity());
                Toast.makeText(getActivity(), "cleared", Toast.LENGTH_LONG).show();
                break;
        }
    }

}
