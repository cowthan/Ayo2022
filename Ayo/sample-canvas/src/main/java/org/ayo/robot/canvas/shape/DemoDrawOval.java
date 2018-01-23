package org.ayo.robot.canvas.shape;

import org.ayo.robot.Observable;
import org.ayo.robot.Observer;
import org.ayo.robot.BaseView;
import org.ayo.robot.DemoBase;

/**
 * Created by Administrator on 2016/12/15.
 */

public class DemoDrawOval extends DemoBase {


    @Override
    protected BaseView createTestView() {
        setNotify("");
        enableLeftTouchBoard(true);
        enableRightTouchBoard(true);
        setTestViewBackgroundStroke();
        OvalView v = new OvalView(getActivity());
        v.getObservable().addObserver(new Observer() {
            @Override
            public void update(Observable observable, Object src, Object data) {
                setNotify(data.toString());
            }
        });
        return v;
    }

    @Override
    protected void onLeftTouchBoardMove(int x, int y, int dx, int dy) {
        OvalView v = getTestView();
        v.moveCenter(dx, dy);
    }

    @Override
    protected void onRightTouchBoardMove(int x, int y, int dx, int dy) {
        OvalView v = getTestView();
        v.changeSize(dx, dy);
    }


}
