package org.ayo.robot.paint.xfermode;

import org.ayo.robot.BaseView;
import org.ayo.robot.DemoBase;

import static org.ayo.robot.paint.xfermode.DemoPorterDuffXfermode.porterDuffModes;

/**
 * Created by Administrator on 2016/12/19.
 */

public class DemoPorterDuffXfermode4 extends DemoBase {

    @Override
    protected BaseView createTestView() {
        setNotify("CLEAR");
        return new PorterDuffXfermodeView4(getActivity());
    }

    int currentPostion = -1;

    @Override
    protected void onViewClicked(int x, int y) {
        currentPostion++;
        if(currentPostion == porterDuffModes.length){
            currentPostion = -1;
        }

        PorterDuffXfermodeView4 v = (PorterDuffXfermodeView4) getTestView();
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
        PorterDuffXfermodeView4 v = (PorterDuffXfermodeView4) getTestView();
        v.move(dx, dy);
    }
    @Override
    protected void onRightTouchBoardMove(int x, int y, int dx, int dy) {
        PorterDuffXfermodeView4 v = (PorterDuffXfermodeView4) getTestView();
        v.changeRadiusBy(dx);
    }


}
