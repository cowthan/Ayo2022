package org.ayo.robot.canvas.shape;

import org.ayo.robot.BaseView;
import org.ayo.robot.DemoBase;
import org.ayo.robot.Observable;
import org.ayo.robot.Observer;

/**
 * Created by Administrator on 2016/12/15.
 */

public class DemoDrawRoundRect extends DemoBase {


    @Override
    protected BaseView createTestView() {
        setNotify("xRadius = " + 30 + ", yRadius = " + 30 + "   左右滑动看效果");
        enableLeftTouchBoard(true);
        enableRightTouchBoard(true);
        setTestViewBackgroundStroke();
        RoundRectView v = new RoundRectView(getActivity());
        v.getObservable().addObserver(new Observer() {
            @Override
            public void update(Observable observable, Object src, Object data) {
                setNotify(data.toString() + "   左右滑动看效果");
            }
        });
        return v;
    }

    @Override
    protected void onLeftTouchBoardMove(int x, int y, int dx, int dy) {
        RoundRectView v = getTestView();
        v.moveCenter(dx, dy);
    }

    @Override
    protected void onRightTouchBoardMove(int x, int y, int dx, int dy) {
        RoundRectView v = getTestView();
        v.changeSize(dx, dy);
    }

    @Override
    protected void onViewTouchMove(int x, int y, int dx, int dy) {
        RoundRectView v = getTestView();
        v.changeRadius(dx, dy);
    }
}
