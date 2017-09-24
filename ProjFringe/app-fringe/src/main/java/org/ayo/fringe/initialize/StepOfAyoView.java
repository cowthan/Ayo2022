package org.ayo.fringe.initialize;

import org.ayo.fringe.utils.Initializer;
import org.ayo.view.AyoViewLib;
import org.ayo.fringe.App;

/**
 * Created by Administrator on 2016/4/30.
 */
public class StepOfAyoView implements Initializer.Step{
    @Override
    public boolean doSeriousWork() {
        AyoViewLib.init(App.app);
        return true;
    }

    @Override
    public String getName() {
        return "UI Framework";
    }

    @Override
    public String getNotify() {
        return "UI Framework初始化失败";
    }

    @Override
    public boolean acceptFail() {
        return false;
    }
}
