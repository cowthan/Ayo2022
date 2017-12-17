package org.ayo.robot.canvas.path;

import org.ayo.robot.BaseView;
import org.ayo.robot.DemoBase;

/**
 * Created by Administrator on 2016/12/15.
 */

public class DemoDrawPath_line extends DemoBase {

    @Override
    protected BaseView createTestView() {
        setNotify("点击控件，控制是否force close，滑动则画线");
        enableLeftTouchBoard(false);
        enableRightTouchBoard(false);
        setTestViewBackgroundStroke();
        return new PathLineView(getActivity());
    }

    @Override
    protected void onViewClicked(int x, int y) {
        PathLineView v = getTestView();
        v.changeCloseStatus();
    }

    @Override
    protected void onViewTouchDown(int x, int y) {
        PathLineView v = getTestView();
        v.onStart(x, y);
    }

    @Override
    protected void onViewTouchUp(int x, int y) {
        PathLineView v = getTestView();
        v.onStop(x, y);
    }

    @Override
    protected void onViewTouchMove(int x, int y, int dx, int dy) {
        PathLineView v = getTestView();
        v.onMove(dx, dy);
    }
}
