package org.ayo.robot.paint.colormatrix;

import org.ayo.robot.BaseView;
import org.ayo.robot.DemoBase;

import static org.ayo.robot.paint.colormatrix.DemoLightingColorFilter.filters;

/**
 * Created by Administrator on 2016/12/19.
 */

public class DemoLightingColorFilter2 extends DemoBase {

    @Override
    protected BaseView createTestView() {
        setNotify("原图");
        return new LightingColorFilterView2(getActivity());
    }

    @Override
    protected void onViewClicked(int x, int y) {
        currentPostion++;
        if(currentPostion == filters.length) currentPostion = 0;
        LightingColorFilterView2 v = (LightingColorFilterView2) getTestView();
        v.setLightingColorFilter(filters[currentPostion].second);
        setNotify(filters[currentPostion].first);
    }

    int currentPostion = 0;

    @Override
    protected void onLeftTouchBoardMove(int x, int y, int dx, int dy) {

    }

    @Override
    protected void onRightTouchBoardMove(int x, int y, int dx, int dy) {

    }


}
