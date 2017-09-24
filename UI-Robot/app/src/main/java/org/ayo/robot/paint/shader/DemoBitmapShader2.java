package org.ayo.robot.paint.shader;

import org.ayo.robot.BaseView;
import org.ayo.robot.DemoBase;

/**
 * Created by Administrator on 2016/12/19.
 */

public class DemoBitmapShader2 extends DemoBase {

    @Override
    protected BaseView createTestView() {
        setNotify("");
        return new BitmapShaderView2(getActivity());
    }

    @Override
    protected void onViewClicked(int x, int y) {

    }

    @Override
    protected void onLeftTouchBoardMove(int x, int y, int dx, int dy) {
        BitmapShaderView2 v = (BitmapShaderView2) getTestView();
        v.moveCircleBy(dx, dy);
    }

    @Override
    protected void onRightTouchBoardMove(int x, int y, int dx, int dy) {
        BitmapShaderView2 v = (BitmapShaderView2) getTestView();
        v.changeRadiusBy(dx);
    }


}
