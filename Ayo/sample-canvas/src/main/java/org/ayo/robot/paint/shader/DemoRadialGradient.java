package org.ayo.robot.paint.shader;

import android.graphics.Color;

import org.ayo.robot.BaseView;
import org.ayo.robot.DemoBase;

import static org.ayo.robot.paint.shader.DemoLinearGradient.tileModess;

/**
 * Created by Administrator on 2016/12/21.
 */

public class DemoRadialGradient extends DemoBase {

    String colorInfo = "RED  YELLOW";
    String tileModeInfo = "Shader.TileMode.REPEAT";
    @Override
    protected BaseView createTestView() {
        setNotify(colorInfo + "  " + tileModeInfo);
        return new RadialGradientView(getActivity());
    }

    int count = 0;
    @Override
    protected void onViewClicked(int x, int y) {
        count++;
        RadialGradientView v = (RadialGradientView) getTestView();
        if(count % 2 == 1){
            v.changeColor(Color.WHITE, Color.BLACK);
            colorInfo = "WHITE  BLACK";
            setNotify(colorInfo + "  " + tileModeInfo);
        }
    }

    @Override
    protected void onLeftTouchBoardMove(int x, int y, int dx, int dy) {
        RadialGradientView v = (RadialGradientView) getTestView();
        v.moveStartPoint(dx, dy);
    }

    @Override
    protected void onRightTouchBoardMove(int x, int y, int dx, int dy) {
        RadialGradientView v = (RadialGradientView) getTestView();
        v.changeRadiusBy(dx);
    }
    int index = 2;
    @Override
    protected void onLeftTouchBoardClicked() {
        index++;
        RadialGradientView v = (RadialGradientView) getTestView();
        v.setTileMode(tileModess[index%3].second);
        tileModeInfo = tileModess[index%3].first;
        setNotify(colorInfo + "  " + tileModeInfo);
    }
}
