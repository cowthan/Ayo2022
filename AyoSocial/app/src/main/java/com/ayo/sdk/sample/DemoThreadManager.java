package com.ayo.sdk.sample;

import android.view.View;

import org.ayo.notify.toaster.Toaster;
import org.ayo.thread.Task;
import org.ayo.thread.ThreadManager;

/**
 * Created by qiaoliang on 2017/5/4.
 */

public class DemoThreadManager extends BaseRxDemo {


    @Override
    protected String getTitle() {
        return "thread-manager";
    }

    @Override
    protected ButtonInfo[] createButtons() {
        return new ButtonInfo[]{
                ButtonInfo.button("主线程运行", new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        ThreadManager.getDefault().runOnUiThread(getActivity(), new Task(getActivity()) {
                            @Override
                            public Object run() {
                                notifyy("runOnMainThread");
                                Toaster.toastShort("返回了");
                                return null;
                            }
                        }, 1000);
                    }
                }),
                ButtonInfo.button("子线程运行", new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        ThreadManager.getDefault().runOnAsyncThread(getActivity(), new Task(getActivity()) {
                            @Override
                            public Object run() {
                                try {
                                    Thread.sleep(2000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                return null;
                            }

                            @Override
                            public void onFinished(Object callback, Object result) {
                                notifyy("runOnAsyncThread");
                                Toaster.toastShort("返回了");
                            }
                        });
                    }
                }),
        };
    }


    @Override
    protected void onDestroy2() {
        super.onDestroy2();
        ThreadManager.getDefault().cancelAllTask(getActivity());
    }
}
