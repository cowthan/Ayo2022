package com.zebdar.tom;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.baidu.mapapi.SDKInitializer;
import com.iflytek.cloud.SpeechUtility;
import com.zebdar.tom.ai.impl.AiManager;
import com.zebdar.tom.chat.ChatManager;
import com.zebdar.tom.chat.MessageCenter;
import com.zebdar.tom.chat.adapter.ImageTemplate;
import com.zebdar.tom.chat.adapter.ItemTemplateManager;
import com.zebdar.tom.chat.adapter.LocationTemplate;
import com.zebdar.tom.chat.adapter.TextCommonTemplate;
import com.zebdar.tom.chat.adapter.VoiceTemplate;
import com.zebdar.tom.chat.menu.ChatMenuManager;
import com.zebdar.tom.chat.menu.impl.LocationMenu;
import com.zebdar.tom.chat.model.IMUser;

import org.ayo.logpage.LogViewer;
import org.ayo.notify.toaster.Toaster;
import org.ayo.receiver.MyCustomBroarcast;
import org.ayo.receiver.ReceiverAction;
import org.ayo.receiver.ReceiverDelegate;
import org.ayo.service.GuardService;
import org.ayo.service.ServiceAction;
import org.ayo.service.ServiceDelegate;

/*
                   _ooOoo_
                  o8888888o
                  88" . "88
                  (| -_- |)
                  O\  =  /O
               ____/`---'\____
             .'  \\|     |//  `.
            /  \\|||  :  |||//  \
           /  _||||| -:- |||||-  \
           |   | \\\  -  /// |   |
           | \_|  ''\---/''  |   |
           \  .-\__  `-`  ___/-. /
         ___`. .'  /--.--\  `. . __
      ."" '<  `.___\_<|>_/___.'  >'"".
     | | :  `- \`.;`\ _ /`;.`/ - ` : | |
     \  \ `-.   \_ __\ /__ _/   .-` /  /
======`-.____`-.___\_____/___.-`____.-'======
                   `=---='
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
         佛祖保佑       永无BUG
*/
//          佛曰:
//                  写字楼里写字间，写字间里程序员；
//                  程序人员写程序，又拿程序换酒钱。
//                  酒醒只在网上坐，酒醉还来网下眠；
//                  酒醉酒醒日复日，网上网下年复年。
//                  但愿老死电脑间，不愿鞠躬老板前；
//                  奔驰宝马贵者趣，公交自行程序员。
//                  别人笑我忒疯癫，我笑自己命太贱；
//                  不见满街漂亮妹，哪个归得程序员？

public class IMCore {


	public static void onCreate(final Application app) {
		SDKInitializer.initialize(app);	    //初始化地图相关
		SpeechUtility.createUtility(app, "appid="+ Const.XF_VOICE_APPID);
		// 以下语句用于设置日志开关（默认开启），设置成false时关闭语音云SDK日志打印
		// Setting.setShowLog(false);
		LogUtil.isShowLog=true;//是否打印log


		if (SysUtils.extraUse()) {
			SysUtils.initFiles();
		} else {
			Toaster.toastShort("请安装存储卡");
		}

		AiManager.init(app);

		ReceiverDelegate.getDefault().register(new ReceiverAction() {
			@Override
			public void onReceive(Context context, String action, Intent intent) {
				if(MyCustomBroarcast.isMyCustomBroardcast(intent)){
					MyCustomBroarcast m = MyCustomBroarcast.parse(intent);
					MessageCenter.getDefault().sendText("自定义广播已收到，type = " + m.type);
				}else{
					MessageCenter.getDefault().sendText("收到系统广播已收到，action = " + action);
				}
			}
		});

		ServiceDelegate.getDefault().register("send-log", new ServiceAction() {
			@Override
			public void run(Context context, String action, Intent intent) {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		ServiceDelegate.getDefault().runAction(app, "send-log", new Intent());

		Intent intent = new Intent(app, GuardService.class);
		app.startService(intent);

		LogViewer.getDefault().init(app);

		ItemTemplateManager.getDefault().registerTemplate(new TextCommonTemplate());
		ItemTemplateManager.getDefault().registerTemplate(new LocationTemplate());
		ItemTemplateManager.getDefault().registerTemplate(new ImageTemplate());
		ItemTemplateManager.getDefault().registerTemplate(new VoiceTemplate());

		ChatMenuManager.getDefault().getMenus().clear();
		ChatMenuManager.getDefault().register(new LocationMenu());
		ChatMenuManager.getDefault().register(new LocationMenu());
		ChatMenuManager.getDefault().register(new LocationMenu());
		ChatMenuManager.getDefault().register(new LocationMenu());
		ChatMenuManager.getDefault().register(new LocationMenu());

		IMUser me = new IMUser();
		me.id = "9527";
		me.name = "DavyJohns";
		me.portrait = "http://img5.imgtn.bdimg.com/it/u=460436168,2403758961&fm=26&gp=0.jpg";
		ChatManager.getDefault().setMe(me);
	}
	

}
