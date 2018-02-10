package com.app.core.update;

import android.app.Activity;
import android.util.Log;

import org.ayo.AppCore;
import org.ayo.JsonUtils;
import org.ayo.core.TheApp;
import org.ayo.update.IUpdateParser;
import org.ayo.update.UpdateInfo;
import org.ayo.update.UpdateManager;

/**
 * Created by qiaoliang on 2017/9/16.
 */

public class UpdateCenter {

    public interface UpdateCallback{
        void onFinish(boolean hasUpdate, UpdateInfo info);
    }

    public static void checkUpdate(Activity activity, final UpdateCallback callback){
        UpdateManager.create(activity)
//                .setChecker(new IUpdateChecker() {
//                    @Override
//                    public void check(ICheckAgent agent, String url) {
//                        Log.e("ezy.update", "checking");
//                        agent.setInfo("");
//                    }
//                })
                .setUrl("http://www.vchuangkou.com/update/update.json")
                .setManual(false)
                .setWifiOnly(false)
                .setNotifyId(1)
                .setParser(new IUpdateParser() {
                    @Override
                    public UpdateInfo parse(String source) throws Exception {
                        Log.e("update", "返回：" + source);
                        MyUpdateInfo m = JsonUtils.getBean(source, MyUpdateInfo.class);
                        final UpdateInfo info = new UpdateInfo();

                        info.hasUpdate = (TheApp.getAppVersionCode() < m.code);
                        info.updateContent = m.context; //"• 支持文字、贴纸、背景音乐，尽情展现欢乐气氛；\n• 两人视频通话支持实时滤镜，丰富滤镜，多彩心情；\n• 图片编辑新增艺术滤镜，一键打造文艺画风；\n• 资料卡新增点赞排行榜，看好友里谁是魅力之王。";
                        info.versionCode = m.code; // 587;
                        info.versionName = m.version;// "v5.8.7";
                        info.url = m.url; //mUpdateUrl;
                        info.md5 = "update-app"; //"56cf48f10e4cf6043fbf53bbbc4009e3";
                        info.size = m.file_size; // 10149314;
                        info.isForce = m.compel != 0; //isForce;
                        info.isIgnorable = false; //isIgnorable;
                        info.isSilent = false; //isSilent;

                        AppCore.getDefault().getGlobalHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                AppCore.getDefault().getGlobalHandler().post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(info.hasUpdate){
                                            callback.onFinish(true, info);
                                        }else{
                                            callback.onFinish(false, null);
                                        }
                                    }
                                });
                            }
                        });


                        return info;
                    }
                }).check();
    }

}
