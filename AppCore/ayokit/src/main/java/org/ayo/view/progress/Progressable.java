package org.ayo.view.progress;

/**
 * Created by Administrator on 2016/7/6.
 */
public interface Progressable {

    void setProgress(int val);
    void setMax(int val);
    int getProgress();
    int getMax();
}
