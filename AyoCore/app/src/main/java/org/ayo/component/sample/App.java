package org.ayo.component.sample;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import org.ayo.AppCore;
import org.ayo.component.core.Core;
import org.ayo.fragmentation.SupportActivity;
import org.ayo.fragmentation.SupportFragment;
import org.ayo.fragmentation.helper.FragmentLifecycleCallbacks;
import org.ayo.fresco.Flesco;
import org.ayo.log.CrashHandler;
import org.ayo.log.LogReporter;
import org.ayo.log.Trace;
import org.ayo.sample.menu.notify.ToasterDebug;

/**
 * Created by cowthan on 2016/1/24.
 */
public class App extends Application{

    public static Application app;
    public static boolean DEBUG = BuildConfig.DEBUG;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        ToasterDebug.init(this);
        LogReporter.init(this, "AyoCore示例");
        Core.init(this, DEBUG);
        Trace.setLevel(DEBUG ? Trace.LEVEL_VERBOSE : Trace.LEVEL_SILENCE);
        Trace.setLogToFile(false);
        AppCore.getDefault().init(this, DEBUG);
        AppCore.getDefault().setSDRoot("ayo-core-sample");
        CrashHandler crashHandler = CrashHandler.getInstance();
        Thread.setDefaultUncaughtExceptionHandler(crashHandler);
        Flesco.initFresco(this);

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                Log.e("activity---栈", activity.getClass().getSimpleName() + " onCreate(savedInstanceState = " + (savedInstanceState == null ? "null" : "有值") + ")");
                if(activity instanceof SupportActivity){
                    ((SupportActivity)activity).registerFragmentLifecycleCallbacks(fragmentLifecycleCallbacks);
                }
            }

            @Override
            public void onActivityStarted(Activity activity) {
                Log.e("activity---栈", activity.getClass().getSimpleName() + " onStart");
            }

            @Override
            public void onActivityResumed(Activity activity) {
                Log.e("activity---栈", activity.getClass().getSimpleName() + " onResume");
            }

            @Override
            public void onActivityPaused(Activity activity) {
                Log.e("activity---栈", activity.getClass().getSimpleName() + " onPause");
            }

            @Override
            public void onActivityStopped(Activity activity) {
                Log.e("activity---栈", activity.getClass().getSimpleName() + " onStop");
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                Log.e("activity---栈", activity.getClass().getSimpleName() + " onSaveInstanceState");
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                Log.e("activity---栈", activity.getClass().getSimpleName() + " onDestroy");
                if(activity instanceof SupportActivity){
                    ((SupportActivity)activity).unregisterFragmentLifecycleCallbacks(fragmentLifecycleCallbacks);
                }
            }

        });

    }

    private FragmentLifecycleCallbacks fragmentLifecycleCallbacks = new FragmentLifecycleCallbacks(){
        @Override
        public void onFragmentSaveInstanceState(SupportFragment fragment, Bundle outState) {
            Log.e("fragment---栈", fragment.getClass().getSimpleName() + " onSaveInstanceState");
        }

        @Override
        public void onFragmentEnterAnimationEnd(SupportFragment fragment, Bundle savedInstanceState) {
            Log.e("fragment---栈", fragment.getClass().getSimpleName() + " onFragmentEnterAnimationEnd");
        }

        @Override
        public void onFragmentLazyInitView(SupportFragment fragment, Bundle savedInstanceState) {
            Log.e("fragment---栈", fragment.getClass().getSimpleName() + " onFragmentLazyInitView");
        }

        @Override
        public void onFragmentSupportVisible(SupportFragment fragment) {
            Log.e("fragment---栈", fragment.getClass().getSimpleName() + " onFragmentSupportVisible");
        }

        @Override
        public void onFragmentSupportInvisible(SupportFragment fragment) {
            Log.e("fragment---栈", fragment.getClass().getSimpleName() + " onFragmentSupportInvisible");
        }

        @Override
        public void onFragmentAttached(SupportFragment fragment) {
            Log.e("fragment---栈", fragment.getClass().getSimpleName() + " onFragmentAttached");
        }

        @Override
        public void onFragmentCreated(SupportFragment fragment, Bundle savedInstanceState) {
            Log.e("fragment---栈", fragment.getClass().getSimpleName() + " onFragmentCreated");
        }

        @Override
        public void onFragmentViewCreated(SupportFragment fragment, Bundle savedInstanceState) {
            Log.e("fragment---栈", fragment.getClass().getSimpleName() + " onFragmentViewCreated");
        }

        @Override
        public void onFragmentActivityCreated(SupportFragment fragment, Bundle savedInstanceState) {
            Log.e("fragment---栈", fragment.getClass().getSimpleName() + " onFragmentActivityCreated");
        }

        @Override
        public void onFragmentStarted(SupportFragment fragment) {
            Log.e("fragment---栈", fragment.getClass().getSimpleName() + " onFragmentStarted");
        }

        @Override
        public void onFragmentResumed(SupportFragment fragment) {
            Log.e("fragment---栈", fragment.getClass().getSimpleName() + " onFragmentResumed");
        }

        @Override
        public void onFragmentPaused(SupportFragment fragment) {
            Log.e("fragment---栈", fragment.getClass().getSimpleName() + " onFragmentPaused");
        }

        @Override
        public void onFragmentStopped(SupportFragment fragment) {
            Log.e("fragment---栈", fragment.getClass().getSimpleName() + " onFragmentStopped");
        }

        @Override
        public void onFragmentDestroyView(SupportFragment fragment) {
            Log.e("fragment---栈", fragment.getClass().getSimpleName() + " onFragmentDestroyView");
        }

        @Override
        public void onFragmentDestroyed(SupportFragment fragment) {
            Log.e("fragment---栈", fragment.getClass().getSimpleName() + " onFragmentDestroyed");
        }

        @Override
        public void onFragmentDetached(SupportFragment fragment) {
            Log.e("fragment---栈", fragment.getClass().getSimpleName() + " onFragmentDetached");
        }
    };

}
