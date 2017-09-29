package org.ayo.ui.sample.video.compress;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by qiaoliang on 2017/9/29.
 */

public abstract class BaseCompressListener implements CompressListener {

    private Handler handler;


    public abstract void onStart();
    public abstract void onProgress(long total, long current);
    public abstract void onSuccess(String outputPath);
    public abstract void onError(String error);

    public BaseCompressListener(){
        handler = new Handler(Looper.getMainLooper());
    }

    @Override
    final public void compressStart() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                onStart();
            }
        });
    }

    @Override
    final public void compressProgress(final long total, final long current) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                onProgress(total, current);
            }
        });
    }

    @Override
    final public void compressSuccess(final String resultPath) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                onSuccess(resultPath);
            }
        });
    }

    @Override
    final public void compressError(final String error) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                onError(error);
            }
        });
    }
}
