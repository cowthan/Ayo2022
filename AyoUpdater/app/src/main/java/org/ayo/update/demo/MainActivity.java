package org.ayo.update.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.ayo.update.IUpdateParser;
import org.ayo.update.UpdateInfo;
import org.ayo.update.UpdateManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        check(true,  1);
    }

    void check(boolean isManual, final int notifyId) {
        UpdateManager.create(this)
//                .setChecker(new IUpdateChecker() {
//                    @Override
//                    public void check(ICheckAgent agent, String url) {
//                        Log.e("ezy.update", "checking");
//                        agent.setInfo("");
//                    }
//                })
                .setUrl("http://114.215.81.196/service/apps/export/version?appid=1&lang=0").setManual(isManual).setNotifyId(notifyId).setParser(new IUpdateParser() {
            @Override
            public UpdateInfo parse(String source) throws Exception {
                Log.e("update", "返回：" + source);
                MyUpdateInfo m = JsonUtils.getBean(source, MyUpdateInfo.class);
                UpdateInfo info = new UpdateInfo();

                info.hasUpdate = m.result == 0;
                info.updateContent = m.context; //"• 支持文字、贴纸、背景音乐，尽情展现欢乐气氛；\n• 两人视频通话支持实时滤镜，丰富滤镜，多彩心情；\n• 图片编辑新增艺术滤镜，一键打造文艺画风；\n• 资料卡新增点赞排行榜，看好友里谁是魅力之王。";
                info.versionCode = m.code; // 587;
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
}
