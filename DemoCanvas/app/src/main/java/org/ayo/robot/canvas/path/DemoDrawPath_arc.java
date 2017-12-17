package org.ayo.robot.canvas.path;

import org.ayo.robot.BaseView;
import org.ayo.robot.DemoBase;

/**
 * Created by Administrator on 2016/12/15.
 */

public class DemoDrawPath_arc extends DemoBase {


    @Override
    protected BaseView createTestView() {
        setNotify("");
        enableLeftTouchBoard(true);
        enableRightTouchBoard(true);
        setTestViewBackgroundStroke();
        PathArcView v = new PathArcView(getActivity());
        return v;
    }

    @Override
    protected void onLeftTouchBoardMove(int x, int y, int dx, int dy) {
        PathArcView v = getTestView();
        v.changeStartAngle(dy);
    }

    @Override
    protected void onRightTouchBoardMove(int x, int y, int dx, int dy) {
        PathArcView v = getTestView();
        v.changeSweepAngle(dy);
    }

    @Override
    protected void onViewClicked(int x, int y) {
        PathArcView v = getTestView();
        v.switchShape();
    }

    @Override
    protected void onViewTouchMove(int x, int y, int dx, int dy) {
        PathArcView v = getTestView();
        v.changeRectSize(dx, dy);
    }
}
