package org.ayo.robot.canvas.matrix;

import org.ayo.observe.Observable;
import org.ayo.observe.Observer;
import org.ayo.robot.BaseView;
import org.ayo.robot.DemoBase;

/**
 * Created by Administrator on 2016/12/15.
 */

public class DemoScale extends DemoBase {

    @Override
    protected BaseView createTestView() {
        setNotify("");
        enableLeftTouchBoard(true);
        enableRightTouchBoard(false);
        setTestViewBackgroundStroke();
        ScaleView v = new ScaleView(getActivity());
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
        ScaleView v = getTestView();
        v.changeScale(dx, dy);
    }

    @Override
    protected void onLeftTouchBoardMove(int x, int y, int dx, int dy) {
        ScaleView v = getTestView();
        v.changeCenter(dx, dy);
    }
}
