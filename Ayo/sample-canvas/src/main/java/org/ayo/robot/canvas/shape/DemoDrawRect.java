package org.ayo.robot.canvas.shape;

import org.ayo.robot.BaseView;
import org.ayo.robot.DemoBase;

/**
 * Created by Administrator on 2016/12/15.
 */

public class DemoDrawRect extends DemoBase {

    @Override
    protected BaseView createTestView() {
        setNotify("");
        enableLeftTouchBoard(true);
        enableRightTouchBoard(true);
        setTestViewBackgroundStroke();
        return new RectView(getActivity());
    }

    @Override
    protected void onLeftTouchBoardMove(int x, int y, int dx, int dy) {
        RectView v = getTestView();
        v.moveCenter(dx, dy);
    }

    @Override
    protected void onRightTouchBoardMove(int x, int y, int dx, int dy) {
        RectView v = getTestView();
        v.changeSize(dx, dy);
    }

}
