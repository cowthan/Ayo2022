package org.ayo.robot.canvas.shape;

import org.ayo.robot.BaseView;
import org.ayo.robot.DemoBase;

/**
 * Created by Administrator on 2016/12/15.
 */

public class DemoDrawLine extends DemoBase {

    @Override
    protected BaseView createTestView() {
        setNotify("");
        enableLeftTouchBoard(true);
        enableRightTouchBoard(true);
        setTestViewBackgroundStroke();
        return new LineView(getActivity());
    }

    @Override
    protected void onViewTouchMove(int x, int y, int dx, int dy) {
        LineView v = (LineView) getTestView();
        v.moveEndPoint(dx, dy);
    }

    @Override
    protected void onLeftTouchBoardMove(int x, int y, int dx, int dy) {
        LineView v = (LineView) getTestView();
        v.moveStartPoint(dx, dy);
    }

    @Override
    protected void onRightTouchBoardMove(int x, int y, int dx, int dy) {
        LineView v = (LineView) getTestView();
        v.moveEndPoint(dx, dy);
    }
}
