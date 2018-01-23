package org.ayo.robot.paint.maskfilter;


import org.ayo.robot.BaseView;
import org.ayo.robot.DemoBase;

/**
 * Created by Administrator on 2016/12/19.
 */

public class DemoBlurMaskFilter3 extends DemoBase {


    @Override
    protected BaseView createTestView() {
        BlurMaskFilterView3 v =  new BlurMaskFilterView3(getActivity());
        return v;
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
