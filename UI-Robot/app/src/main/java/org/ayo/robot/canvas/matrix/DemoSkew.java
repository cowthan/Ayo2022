package org.ayo.robot.canvas.matrix;

import org.ayo.observe.Observable;
import org.ayo.observe.Observer;
import org.ayo.robot.BaseView;
import org.ayo.robot.canvas.layer.DemoSave;

/**
 * Created by Administrator on 2016/12/15.
 */

public class DemoSkew extends DemoSave {
    @Override
    protected BaseView createTestView() {
        setNotify("");
        enableLeftTouchBoard(true);
        enableRightTouchBoard(false);
        setTestViewBackgroundStroke();
        SkewView v = new SkewView(getActivity());
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
        SkewView v = getTestView();
        v.change(dx, dy);
    }

}
