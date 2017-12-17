package org.ayo.robot.paint.shader;

import org.ayo.robot.BaseView;
import org.ayo.robot.DemoBase;

/**
 * Created by Administrator on 2016/12/21.
 */

public class DemoSweepGradient2 extends DemoBase {


    @Override
    protected BaseView createTestView() {
        return new SweepGradientView2(getActivity());
    }

    @Override
    protected void onViewClicked(int x, int y) {

    }

    @Override
    protected void onLeftTouchBoardMove(int x, int y, int dx, int dy) {
        SweepGradientView2 v = (SweepGradientView2) getTestView();
        v.moveStartPoint(dx, dy);
    }
    @Override
    protected void onRightTouchBoardMove(int x, int y, int dx, int dy) {

    }

}
