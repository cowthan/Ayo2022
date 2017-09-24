package org.ayo.robot.paint.shader;

import org.ayo.robot.BaseView;
import org.ayo.robot.DemoBase;

/**
 * Created by Administrator on 2016/12/21.
 */

public class DemoRadialGradient3 extends DemoBase {


    @Override
    protected BaseView createTestView() {
        return new RadialGradientView3(getActivity());
    }

    @Override
    protected void onViewClicked(int x, int y) {

    }

    @Override
    protected void onLeftTouchBoardMove(int x, int y, int dx, int dy) {
    }

    @Override
    protected void onRightTouchBoardMove(int x, int y, int dx, int dy) {
    }
}
