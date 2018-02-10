package org.ayo;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import org.ayo.core.Lang;
import org.ayo.http.HttpUserProxy;
import org.ayo.log.Trace;

import java.io.File;

import okhttp3.OkHttpClient;
import okhttp3.Request;


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

	private boolean isInBackground = true;

	public boolean isAppInBackground(){
		return isInBackground;
	}

	public void init(Application a, boolean isDebug){
		app = a;
		DEBUG = isDebug;
		handler = new Handler(Looper.getMainLooper());

		mActivityLifecycleCallbacksImpl = new ActivityLifecycleCallbacksImpl();
		a.registerActivityLifecycleCallbacks(mActivityLifecycleCallbacksImpl);
	}

	private ActivityLifecycleCallbacksImpl mActivityLifecycleCallbacksImpl;

	private class ActivityLifecycleCallbacksImpl implements Application.ActivityLifecycleCallbacks {
		// 前台 Activity 的数目
		private int mFrontActivityCount = 0;
		// 缓冲计数器，记录 configChanges 的状态
		// 没有配置 configChanges 和 screenOrientation，屏幕旋转造成 Activity 重启；
		// 手动调用 Activity.recreate() 重启 Activity；
		private int tmpCount = 0;

		@Override
		public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

		}

		@Override public void onActivityStarted(Activity activity) {
			if (mFrontActivityCount <= 0) {
				// TODO 处理从后台恢复到前台的逻辑
				isInBackground = false;
				Toast.makeText(app, "----进入前台----", Toast.LENGTH_LONG).show();
			}

			if (tmpCount < 0) {
				tmpCount++;
			} else {
				mFrontActivityCount++;
			}
		}

		@Override
		public void onActivityResumed(Activity activity) { }

		@Override public void onActivityPaused(Activity activity) { }

		@Override public void onActivityStopped(Activity activity) {
			// 遇到 configChanges 情况，操作缓冲计数器
			if (activity.isChangingConfigurations()) {
				tmpCount--;
			} else {
				mFrontActivityCount--;
				if (mFrontActivityCount <= 0) {
					// TODO 处理从前台进入到后台的逻辑
					isInBackground = true;
					Toast.makeText(app, "----进入后台----", Toast.LENGTH_LONG).show();
				}
			}
		}

		@Override
		public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

		}
		@Override public void onActivityDestroyed(Activity activity) {

		}
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

	private HttpUserProxy httpUserProxy;

	public void setHttpUserProxy(HttpUserProxy proxy){
		httpUserProxy = proxy;
	}

	public HttpUserProxy getHttpUserProxy(){
		if(httpUserProxy == null){
			return new HttpUserProxy() {
				@Override
				public void handleOkHttpBuilder(OkHttpClient.Builder builder) {

				}

				@Override
				public void handleCommonHeaderAndParameters(Request.Builder builder) {

				}
			};
		}
		return httpUserProxy;
	}

}
