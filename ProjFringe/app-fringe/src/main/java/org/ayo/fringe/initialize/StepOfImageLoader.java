package org.ayo.fringe.initialize;

import org.ayo.fringe.utils.Initializer;
import org.ayo.fresco.Flesco;
import org.ayo.fringe.App;

/**
 */
public class StepOfImageLoader implements Initializer.Step{
    @Override
    public boolean doSeriousWork() {
        Flesco.initFresco(App.app);
        return true;
    }

    @Override
    public String getName() {
        return "图片加载库";
    }

    @Override
    public String getNotify() {
        return "图片加载库没有正确初始化";
    }

    @Override
    public boolean acceptFail() {
        return false;
    }
}
