package org.ayo.robot.canvas.shape;

import org.ayo.robot.BaseView;
import org.ayo.robot.DemoBase;

/**
 * Created by Administrator on 2016/12/15.
 */

public class DemoDrawARGB  extends DemoBase {

    @Override
    protected BaseView createTestView() {
        setNotify("canvas.drawARGB(100, 77, 11, 111)");
        enableLeftTouchBoard(false);
        enableRightTouchBoard(false);
        setTestViewBackgroundStroke();
        return new ARGBView(getActivity());
    }
}
