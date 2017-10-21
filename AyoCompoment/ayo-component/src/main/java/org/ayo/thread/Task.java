package org.ayo.thread;

import java.lang.ref.WeakReference;

/**
 * Created by qiaoliang on 2017/5/2.
 */

public abstract class Task<T> {

    private WeakReference<T> ref;

    public Task(T callback){
        ref = new WeakReference<T>(callback);
    }

    public T getCallback(){
        return ref == null ? null : ref.get();
    }

    public abstract Object run();

    public void onFinished(T callback, Object result){}

    public void onCanceled(T callback){}

}
