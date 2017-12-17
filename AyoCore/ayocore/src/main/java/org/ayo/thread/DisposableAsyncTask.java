package org.ayo.thread;

/**
 * Created by qiaoliang on 2017/5/2.
 */

public class DisposableAsyncTask extends Disposable<InternalAsyncTask>{

    private DisposableAsyncTask(InternalAsyncTask internalAsyncTask) {
        super(internalAsyncTask);
    }

    public static DisposableAsyncTask from(InternalAsyncTask task){
        DisposableAsyncTask d = new DisposableAsyncTask(task);
        return d;
    }

    @Override
    protected void dispose(InternalAsyncTask t){
        t.cancel(true);
    }

}
