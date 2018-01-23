package org.ayo.robot.canvas.matrix;

import org.ayo.robot.Observable;
import org.ayo.robot.Observer;
import org.ayo.robot.BaseView;
import org.ayo.robot.DemoBase;

/**
 * Created by Administrator on 2016/12/15.
 */

public class DemoRotate extends DemoBase {

    @Override
    protected BaseView createTestView() {
        setNotify("");
        enableLeftTouchBoard(true);
        enableRightTouchBoard(false);
        setTestViewBackgroundStroke();
        RotateView v = new RotateView(getActivity());
        v.getObservable().addObserver(new Observer() {
            @Override
            public void update(Observable observable, Object src, Object data) {
                setNotify(data.toString());
            }
        });
        return v;
    }

    @Override
    protected void onViewTouchMove(int x, int y, int dx, int dy) {
        RotateView v = getTestView();
        v.changeRotateAngle(Math.abs(dx) >= Math.abs(dy) ? dx : dy);
    }

    @Override
    protected void onLeftTouchBoardMove(int x, int y, int dx, int dy) {
        RotateView v = getTestView();
        v.changeCenter(dx, dy);
    }
}
