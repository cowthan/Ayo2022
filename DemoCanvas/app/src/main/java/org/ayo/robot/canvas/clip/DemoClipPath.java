package org.ayo.robot.canvas.clip;

import org.ayo.robot.BaseView;
import org.ayo.robot.DemoBase;

/**
 * Created by Administrator on 2016/12/15.
 */

public class DemoClipPath extends DemoBase {

    @Override
    protected BaseView createTestView() {
        setNotify("");
        enableLeftTouchBoard(false);
        enableRightTouchBoard(false);
        setTestViewBackgroundStroke();
        return new ClipPathView(getActivity());
    }

    @Override
    protected void onViewTouchDown(int x, int y) {
        ClipPathView v = getTestView();
        v.onStart(x, y);
    }

    @Override
    protected void onViewTouchUp(int x, int y) {
        ClipPathView v = getTestView();
        v.onStop(x, y);
    }

    @Override
    protected void onViewTouchMove(int x, int y, int dx, int dy) {
        ClipPathView v = getTestView();
        v.onMove(dx, dy);
    }
}
