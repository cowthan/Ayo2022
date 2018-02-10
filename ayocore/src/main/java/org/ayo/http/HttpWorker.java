package org.ayo.http;


import org.ayo.http.callback.BaseHttpCallback;
import org.ayo.http.callback.ProgressRequestListener;
import org.ayo.http.callback.ProgressResponseListener;

/**
 * Created by Administrator on 2016/8/16.
 */
public abstract class HttpWorker {

    public abstract void fire(AyoRequest req, BaseHttpCallback<String> callback, ProgressRequestListener progressRequestListener, ProgressResponseListener progressResponseListener);
    public abstract AyoResponse fireSync(AyoRequest req, ProgressRequestListener progressRequestListener, ProgressResponseListener progressResponseListener);
    public abstract void cancelAll(Object tag);

}
