package org.ayo.robot.canvas.clip;

import org.ayo.robot.Observable;
import org.ayo.robot.Observer;
import org.ayo.robot.BaseView;
import org.ayo.robot.DemoBase;

/**
 * Created by Administrator on 2016/12/15.
 */

public class DemoClipRegionOp extends DemoBase {

    @Override
    protected BaseView createTestView() {
        setNotify("");
        enableLeftTouchBoard(true);
        enableRightTouchBoard(false);
        setTestViewBackgroundStroke();
        ClipRegionOpView v = new ClipRegionOpView(getActivity());
        v.getObservable().addObserver(new Observer() {
            @Override
            public void update(Observable observable, Object src, Object data) {
                setNotify(data.toString());
            }
        });
        return v;
    }

    @Override
    protected void onViewTouchDown(int x, int y) {
        ClipRegionOpView v = getTestView();
        v.onStart(x, y);
    }

    @Override
    protected void onViewTouchUp(int x, int y) {
        ClipRegionOpView v = getTestView();
        v.onStop(x, y);
    }

    @Override
    protected void onViewTouchMove(int x, int y, int dx, int dy) {
        ClipRegionOpView v = getTestView();
        v.onMove(dx, dy);
    }

    @Override
    protected void onLeftTouchBoardClicked() {
        ClipRegionOpView v = getTestView();
        v.changeOp();
    }

}
