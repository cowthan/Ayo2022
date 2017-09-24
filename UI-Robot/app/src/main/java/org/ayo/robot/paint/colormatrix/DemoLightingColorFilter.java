package org.ayo.robot.paint.colormatrix;

import android.graphics.LightingColorFilter;
import android.util.Pair;

import org.ayo.robot.BaseView;
import org.ayo.robot.DemoBase;

/**
 * Created by Administrator on 2016/12/19.
 */

public class DemoLightingColorFilter extends DemoBase {

    @Override
    protected BaseView createTestView() {
        setNotify("原图");
        return new LightingColorFilterView(getActivity());
    }

    @Override
    protected void onViewClicked(int x, int y) {
        currentPostion++;
        if(currentPostion == filters.length) currentPostion = 0;
        LightingColorFilterView v = (LightingColorFilterView) getTestView();
        v.setLightingColorFilter(filters[currentPostion].second);
        setNotify(filters[currentPostion].first);
    }

    int currentPostion = 0;
    public static Pair<String, LightingColorFilter>[] filters = new Pair[]{
            new Pair("原图", new LightingColorFilter(0xFFFFFFFF, 0x00000000)),
            new Pair("增加红色", new LightingColorFilter(0xFFFFFFFF, 0x00FF0000)),
            new Pair("屏蔽绿色", new LightingColorFilter(0xFFFF00FF, 0x00000000)),
            new Pair("变成黄色", new LightingColorFilter(0xFFFFFFFF, 0X00FFFF00)),
    };

    @Override
    protected void onLeftTouchBoardMove(int x, int y, int dx, int dy) {

    }

    @Override
    protected void onRightTouchBoardMove(int x, int y, int dx, int dy) {

    }


}
