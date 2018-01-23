package org.ayo.robot.canvas.shape;

import org.ayo.robot.Observable;
import org.ayo.robot.Observer;
import org.ayo.robot.BaseView;
import org.ayo.robot.DemoBase;

/**
 * Created by Administrator on 2016/12/15.
 */

public class DemoDrawArc extends DemoBase {


    @Override
    protected BaseView createTestView() {
        setNotify("起始角 = " + 0 + ", 跨度 = " + 90 + ", " + (true ? "扇形" : "弧线") + "   TouchBoard上下滑动和看效果");
        enableLeftTouchBoard(true);
        enableRightTouchBoard(true);
        setTestViewBackgroundStroke();
        ArcView v = new ArcView(getActivity());
        v.getObservable().addObserver(new Observer() {
            @Override
            public void update(Observable observable, Object src, Object data) {
                setNotify(data.toString() + "   TouchBoard上下滑动看效果");
            }
        });
        return v;
    }

    @Override
    protected void onLeftTouchBoardMove(int x, int y, int dx, int dy) {
        ArcView v = getTestView();
        v.changeStartAngle(dy);
    }

    @Override
    protected void onRightTouchBoardMove(int x, int y, int dx, int dy) {
        ArcView v = getTestView();
        v.changeSweepAngle(dy);
    }

    @Override
    protected void onViewClicked(int x, int y) {
        ArcView v = getTestView();
        v.switchShape();
    }

    @Override
    protected void onViewTouchMove(int x, int y, int dx, int dy) {
        ArcView v = getTestView();
        v.changeRectSize(dx, dy);
    }
}
