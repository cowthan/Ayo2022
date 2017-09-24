package org.ayo.fringe.initialize;

import org.ayo.fringe.utils.Initializer;

/**
 */
public class StepOfAyoSdk implements Initializer.Step {
    @Override
    public boolean doSeriousWork() {
        //AppCore.init(App.app, Config.Build.OPEN_DEBUG_LOG);
        return true;
    }

    @Override
    public String getName() {
        return "激活底层类库";
    }

    @Override
    public String getNotify() {
        return "底层类库激活失败";
    }

    @Override
    public boolean acceptFail() {
        return true;
    }
}
