package org.ayo.robot.canvas.shape;

import org.ayo.observe.Observable;
import org.ayo.observe.Observer;
import org.ayo.robot.BaseView;
import org.ayo.robot.DemoBase;

/**
 * Created by Administrator on 2016/12/15.
 */

public class DemoDrawPoint extends DemoBase {

    @Override
    protected BaseView createTestView() {
        setNotify("");
        enableLeftTouchBoard(false);
        enableRightTouchBoard(false);
        setTestViewBackgroundStroke();
        PointView v = new PointView(getActivity());
        v.getObservable().addObserver(new Observer() {
            @Override
            public void update(Observable observable, Object src, Object data) {
                setNotify(data.toString());
            }
        });
        return v;
    }

    @Override
    protected void onViewClicked(int x, int y) {
        PointView v = (PointView) getTestView();
        v.addPoint();
    }
}
