package org.ayo.thread;

import java.lang.ref.WeakReference;

/**
 * Created by qiaoliang on 2017/5/2.
 */

public abstract class Disposable<T> {

    private WeakReference<T> ref;

    public Disposable(T t){
        ref = new WeakReference<T>(t);
    }

    public final void dispose(){
        if(ref != null && ref.get() != null){
            dispose(ref.get());
        }
    }

    protected abstract void dispose(T t);
}
