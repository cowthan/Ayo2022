package org.ayo.robot.paint.colormatrix;

import org.ayo.robot.BaseView;
import org.ayo.robot.DemoBase;

import static org.ayo.robot.paint.xfermode.DemoPorterDuffXfermode.porterDuffModes;

/**
 * Created by Administrator on 2016/12/19.
 */

public class DemoPorterDuffColorFilter2 extends DemoBase {

    @Override
    protected BaseView createTestView() {
        setNotify("CLEAR");
        return new PorterDuffColorFilterView2(getActivity());
    }

    int currentPostion = -1;

    @Override
    protected void onViewClicked(int x, int y) {
        currentPostion++;
        if(currentPostion == porterDuffModes.length){
            currentPostion = -1;
        }

        PorterDuffColorFilterView2 v = (PorterDuffColorFilterView2) getTestView();
        if(currentPostion == -1){
            v.setPorterDuffMode(null);
            setNotify("不加Xfermode");
        }else{
            v.setPorterDuffMode(porterDuffModes[currentPostion].second);
            setNotify(porterDuffModes[currentPostion].first);
        }

    }

    @Override
    protected void onLeftTouchBoardMove(int x, int y, int dx, int dy) {
        PorterDuffColorFilterView2 v = (PorterDuffColorFilterView2) getTestView();
        v.moveCircleBy(dx, dy);
    }
    @Override
    protected void onRightTouchBoardMove(int x, int y, int dx, int dy) {
        PorterDuffColorFilterView2 v = (PorterDuffColorFilterView2) getTestView();
        v.changeCircleRadiusBy(dx);
    }


}
