package org.ayo.http.callback;

/**
 * Created by Administrator on 2017/7/9.
 */
public interface ProgressRequestListener {
    void onRequestProgress(long bytesWritten, long contentLength, boolean done);
}