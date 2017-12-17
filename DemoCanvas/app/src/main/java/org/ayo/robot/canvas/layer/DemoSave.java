package org.ayo.robot.canvas.layer;

import org.ayo.robot.BaseView;
import org.ayo.robot.DemoBase;

/**
 * Created by Administrator on 2016/12/15.
 */

public class DemoSave extends DemoBase {

    @Override
    protected BaseView createTestView() {
        setNotify("");
        enableLeftTouchBoard(false);
        enableRightTouchBoard(false);
        setTestViewBackgroundStroke();
        SaveView v = new SaveView(getActivity());
        return v;
    }

}
