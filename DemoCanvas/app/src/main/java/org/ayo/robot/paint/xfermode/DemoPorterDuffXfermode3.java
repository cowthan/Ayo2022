package org.ayo.robot.paint.xfermode;

import org.ayo.robot.BaseView;
import org.ayo.robot.DemoBase;

import static org.ayo.robot.paint.xfermode.DemoPorterDuffXfermode.porterDuffModes;

/**
 * Created by Administrator on 2016/12/19.
 */

public class DemoPorterDuffXfermode3 extends DemoBase {

    @Override
    protected BaseView createTestView() {
        setNotify("不加Xfermode");
        return new PorterDuffXfermodeView3(getActivity());
    }

    int currentPostion = -1;

    @Override
    protected void onViewClicked(int x, int y) {
        currentPostion++;
        if(currentPostion == porterDuffModes.length){
            currentPostion = -1;
        }

        PorterDuffXfermodeView3 v = (PorterDuffXfermodeView3) getTestView();
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
        PorterDuffXfermodeView3 v = (PorterDuffXfermodeView3) getTestView();
        v.moveCircleBy(dx, dy);
    }
    @Override
    protected void onRightTouchBoardMove(int x, int y, int dx, int dy) {
        PorterDuffXfermodeView3 v = (PorterDuffXfermodeView3) getTestView();
        v.changeCircleRadiusBy(dx);
    }


}
