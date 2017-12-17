package org.ayo.robot.canvas.path;

import org.ayo.robot.BaseView;
import org.ayo.robot.DemoBase;

/**
 * Created by Administrator on 2016/12/15.
 */

public class DemoDrawPath_bezier2 extends DemoBase {

    @Override
    protected BaseView createTestView() {
        setNotify("滑动左TouchBoard，移动控制点; 滑动控件，移动终点");
        enableLeftTouchBoard(true);
        enableRightTouchBoard(false);
        setTestViewBackgroundStroke();
        return new PathBezier2View(getActivity());
    }

    @Override
    protected void onViewTouchMove(int x, int y, int dx, int dy) {
        PathBezier2View v = getTestView();
        v.moveEndPoint(dx, dy);
    }

    @Override
    protected void onLeftTouchBoardMove(int x, int y, int dx, int dy) {
        PathBezier2View v = getTestView();
        v.moveControlPoint(dx, dy);
    }
}
