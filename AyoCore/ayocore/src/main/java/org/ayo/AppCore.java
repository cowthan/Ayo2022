package org.ayo;

import android.app.Application;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;

import org.ayo.core.Lang;
import org.ayo.log.Trace;

import java.io.File;


public class AppCore {

	private AppCore(){}

	private static final class Holder{
		private static final AppCore instance = new AppCore();
	}

	public static AppCore getDefault(){
		return Holder.instance;
	}

	public static final String TAG = "AppCore";
	private static Application app;
	private static boolean DEBUG;
	private Handler handler;

	public void init(Application a, boolean isDebug){
		app = a;
		DEBUG = isDebug;
		handler = new Handler(Looper.getMainLooper());
	}

	public Handler getGlobalHandler(){
		return handler;
	}

	public static Application app(){
		return app;
	}

	public static boolean isDebug(){
		return DEBUG;
	}



	public static String ROOT;// default work directory
	public static String ROOT_DIR_NAME;



	/**
	 * setSDRoot("work-dir"), used be ROOT
	 * @param dirName  "dirName"
	 * @return  AppCore.ROOT变成了/sd/dirName/
     */
	public boolean setSDRoot(String dirName) {
		if (Lang.isEmpty(dirName)){
			Trace.i(TAG, "sd root: " + "设置失败，路径为空");
			return false;
		}

		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			AppCore.ROOT = Environment.getExternalStorageDirectory().getAbsolutePath() + (dirName.startsWith("/") ? "" : "/") + dirName;
		} else {
			Trace.i(TAG, "sd root: " + "找不到sd卡");
			return false;
		}

		if (!AppCore.ROOT.endsWith("/")) {
			AppCore.ROOT += "/";
		}

		AppCore.ROOT_DIR_NAME = dirName;
		
		File dir = new File(AppCore.ROOT);
		if(dir.exists() && dir.isDirectory()){
			Trace.i(TAG, "sd root: " + "已经有啦");
			return true;
		}else{
			if(dir.mkdirs()){
				Trace.i(TAG, "sd root: " + "设置成功-" + AppCore.ROOT);
				return true;
			}else{
				Trace.i(TAG, "sd root: " + "目录创建失败-" + AppCore.ROOT);
				return false;
			}
		}

	}



}
