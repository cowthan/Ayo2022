package org.ayo.ui.sample.video.compress;

/**
 * Created by wanglong on 17/6/28.
 * 压缩监听
 */

public interface CompressListener {
    void compressStart();
    void compressProgress(long total, long current);
    void compressSuccess(String resultPath);
    void compressError(String error);
}
