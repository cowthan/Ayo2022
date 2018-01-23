package org.ayo.robot.canvas.shape;

import org.ayo.robot.BaseView;
import org.ayo.robot.DemoBase;

/**
 * Created by Administrator on 2016/12/15.
 */

public class DemoDrawCircile extends DemoBase {

    @Override
    protected BaseView createTestView() {
        setNotify("");
        enableLeftTouchBoard(true);
        enableRightTouchBoard(false);
        setTestViewBackgroundStroke();
        return new CircleView(getActivity());
    }

    @Override
    protected void onLeftTouchBoardMove(int x, int y, int dx, int dy) {
        CircleView v = getTestView();
        v.moveCenter(dx, dy);
    }

    @Override
    protected void onViewTouchMove(int x, int y, int dx, int dy) {
        CircleView v = getTestView();
        v.changeRadius(dx, dy);
    }
}
