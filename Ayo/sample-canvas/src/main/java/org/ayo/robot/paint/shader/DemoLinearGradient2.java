package org.ayo.robot.paint.shader;

import org.ayo.robot.BaseView;
import org.ayo.robot.DemoBase;

import static org.ayo.robot.paint.shader.DemoLinearGradient.tileModess;

/**
 * Created by Administrator on 2016/12/21.
 */

public class DemoLinearGradient2 extends DemoBase {
    private String colorInfo = "";
    private String tileModeInfo = "Shader.TileMode.REPEAT";
    @Override
    protected BaseView createTestView() {
        setNotify(colorInfo + "     " + tileModeInfo);
        return new LinearGradientView2(getActivity());
    }


    @Override
    protected void onViewClicked(int x, int y) {
    }

    @Override
    protected void onLeftTouchBoardMove(int x, int y, int dx, int dy) {
        LinearGradientView2 v = (LinearGradientView2) getTestView();
        v.moveStartPoint(dx, dy);
    }

    @Override
    protected void onRightTouchBoardMove(int x, int y, int dx, int dy) {
        LinearGradientView2 v = (LinearGradientView2) getTestView();
        v.moveEndPoint(dx, dy);
    }

    int index = 2;
    @Override
    protected void onLeftTouchBoardClicked() {
        index++;
        LinearGradientView2 v = (LinearGradientView2) getTestView();
        v.setTileMode(tileModess[index%3].second);
        tileModeInfo = tileModess[index%3].first;
        setNotify(colorInfo + "     " + tileModeInfo);
    }


    @Override
    protected void onRightTouchBoardClicked() {

    }
}
