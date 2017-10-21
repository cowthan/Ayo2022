package org.ayo.thread;

import android.os.Handler;

/**
 * Created by qiaoliang on 2017/5/2.
 */

public class DisposableHandler extends Disposable<Handler>{

    private DisposableHandler(Handler h) {
        super(h);
    }

    public static DisposableHandler from(Handler task){
        DisposableHandler d = new DisposableHandler(task);
        return d;
    }

    @Override
    public void dispose(Handler t){
        t.removeCallbacksAndMessages(null);
    }

}
