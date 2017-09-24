package org.ayo.robot.canvas.path;

import org.ayo.robot.BaseView;
import org.ayo.robot.DemoBase;

/**
 * Created by Administrator on 2016/12/15.
 */

public class DemoDrawPath_bezier3 extends DemoBase {

    @Override
    protected BaseView createTestView() {
        setNotify("滑动左右TouchBoard，移动控制点; 滑动控件，移动终点");
        enableLeftTouchBoard(true);
        enableRightTouchBoard(true);
        setTestViewBackgroundStroke();
        return new PathBezier3View(getActivity());
    }

    @Override
    protected void onViewTouchMove(int x, int y, int dx, int dy) {
        PathBezier3View v = getTestView();
        v.moveEndPoint(dx, dy);
    }

    @Override
    protected void onLeftTouchBoardMove(int x, int y, int dx, int dy) {
        PathBezier3View v = getTestView();
        v.changeControlPoint_2(dx, dy);
    }

    @Override
    protected void onRightTouchBoardMove(int x, int y, int dx, int dy) {
        PathBezier3View v = getTestView();
        v.changeControlPoint_1(dx, dy);

    }
}
