package org.ayo.thread;

/**
 * Created by qiaoliang on 2017/5/2.
 */

public class Test {

    private static class TaskA extends Task<Test>{

        public TaskA(Test callback) {
            super(callback);
        }

        @Override
        public Object run() {
            return null;
        }

        @Override
        public void onFinished(Test callback, Object result) {

        }
    }

    public void test(){
        ThreadManager.getDefault().runOnUiThread(this, new TaskA(this));
        ThreadManager.getDefault().cancelAllTask(this);

        ThreadManager.getDefault().runOnAsyncThread(this, new TaskA(this));
        ThreadManager.getDefault().cancelAllTask(this);
    }

}
