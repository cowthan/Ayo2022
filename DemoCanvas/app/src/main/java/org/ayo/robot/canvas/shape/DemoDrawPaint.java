package org.ayo.robot.canvas.shape;

import org.ayo.robot.BaseView;
import org.ayo.robot.DemoBase;

/**
 * Created by Administrator on 2016/12/15.
 */

public class DemoDrawPaint extends DemoBase {

    @Override
    protected BaseView createTestView() {
        setNotify("给canvas绘制背景");
        enableLeftTouchBoard(false);
        enableRightTouchBoard(false);
        setTestViewBackgroundStroke();
        return new PaintView(getActivity());
    }
}
