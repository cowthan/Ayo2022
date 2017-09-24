package org.ayo.robot.canvas.clip;

import org.ayo.robot.BaseView;
import org.ayo.robot.DemoBase;

/**
 * Created by Administrator on 2016/12/15.
 */

public class DemoClipRect extends DemoBase {


    @Override
    protected BaseView createTestView() {
        setNotify("");
        enableLeftTouchBoard(true);
        enableRightTouchBoard(true);
        setTestViewBackgroundStroke();
        ClipRectView v = new ClipRectView(getActivity());
        return v;
    }

    @Override
    protected void onViewClicked(int x, int y) {
        ClipRectView v = getTestView();
        v.changeRect();
    }
}
