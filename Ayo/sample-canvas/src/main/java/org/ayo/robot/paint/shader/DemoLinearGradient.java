package org.ayo.robot.paint.shader;

import android.graphics.Color;
import android.graphics.Shader;
import android.util.Pair;

import org.ayo.robot.BaseView;
import org.ayo.robot.DemoBase;

/**
 * Created by Administrator on 2016/12/21.
 */

public class DemoLinearGradient extends DemoBase {


    @Override
    protected BaseView createTestView() {
        setNotify(colorInfo + "     " + tileModeInfo);
        return new LinearGradientView(getActivity());
    }

    int count = -1;

    @Override
    protected void onViewClicked(int x, int y) {
        LinearGradientView v = (LinearGradientView) getTestView();
        count++;
        if(count % 3 == 0){
            v.changeColor(Color.RED, Color.YELLOW);
            colorInfo = "Color.RED   Color.YELLOW";
            setNotify(colorInfo + "     " + tileModeInfo);
        }else if(count % 3 == 1){
            v.changeColor(Color.BLACK, Color.WHITE);
            colorInfo = "Color.BLACK   Color.WHITE";
            setNotify(colorInfo + "     " + tileModeInfo);
        }else if(count % 3 == 2){
            v.changeColor(Color.WHITE, Color.BLACK);
            colorInfo = "Color.WHITE   Color.BLACK";
            setNotify(colorInfo + "     " + tileModeInfo);
        }
    }

    @Override
    protected void onLeftTouchBoardMove(int x, int y, int dx, int dy) {
        LinearGradientView v = (LinearGradientView) getTestView();
        v.moveStartPoint(dx, dy);
    }

    @Override
    protected void onRightTouchBoardMove(int x, int y, int dx, int dy) {
        LinearGradientView v = (LinearGradientView) getTestView();
        v.moveEndPoint(dx, dy);
    }

    int index = 2;
    @Override
    protected void onLeftTouchBoardClicked() {
        index++;
        LinearGradientView v = (LinearGradientView) getTestView();
        v.setTileMode(tileModess[index%3].second);
        tileModeInfo = tileModess[index%3].first;
        setNotify(colorInfo + "     " + tileModeInfo);
    }

    public static Pair<String, Shader.TileMode>[] tileModess = new Pair[]{
            new Pair("Shader.TileMode.CLAMP", Shader.TileMode.CLAMP),
            new Pair("Shader.TileMode.MIRROR", Shader.TileMode.MIRROR),
            new Pair("Shader.TileMode.REPEAT", Shader.TileMode.REPEAT),
    };

    @Override
    protected void onRightTouchBoardClicked() {

    }

    private String colorInfo = "Color.WHITE   Color.BLACK";
    private String tileModeInfo = "Shader.TileMode.REPEAT";
}
