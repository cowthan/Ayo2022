package org.ayo.robot.canvas.matrix;

import org.ayo.observe.Observable;
import org.ayo.observe.Observer;
import org.ayo.robot.BaseView;
import org.ayo.robot.DemoBase;

/**
 * Created by Administrator on 2016/12/15.
 */

public class DemoTranslate extends DemoBase {

    @Override
    protected BaseView createTestView() {
        setNotify("");
        enableLeftTouchBoard(false);
        enableRightTouchBoard(false);
        setTestViewBackgroundStroke();
        TranslateView v = new TranslateView(getActivity());
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
        TranslateView v = getTestView();
        v.move(dx, dy);
    }

}
