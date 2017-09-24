package org.ayo.robot.paint.shader;

import android.graphics.Shader;
import android.util.Pair;

import org.ayo.robot.BaseView;
import org.ayo.robot.DemoBase;

/**
 * Created by Administrator on 2016/12/19.
 */

public class DemoBitmapShader extends DemoBase {

    private String xTileMode = "null";
    private String yTileMode = "null";

    @Override
    protected BaseView createTestView() {
        setNotify(xTileMode + ", " + yTileMode);
        return new BitmapShaderView(getActivity());
    }

    @Override
    protected void onViewClicked(int x, int y) {
        BitmapShaderView v = (BitmapShaderView) getTestView();
        v.onClicked();
    }

    private int currentXPostion = 0;
    private int currentYPostion = 0;

    @Override
    protected void onLeftTouchBoardClicked() {
        currentXPostion++;
        if(currentXPostion == tileModes.length) currentXPostion = 0;
        BitmapShaderView v = (BitmapShaderView) getTestView();
        v.setXTileMode(tileModes[currentXPostion].second);
        xTileMode = tileModes[currentXPostion].first;
        setNotify(xTileMode + ", " + yTileMode);
    }

    @Override
    protected void onRightTouchBoardClicked() {
        currentYPostion++;
        if(currentYPostion == tileModes.length) currentYPostion = 0;
        BitmapShaderView v = (BitmapShaderView) getTestView();
        v.setYTileMode(tileModes[currentYPostion].second);
        yTileMode = tileModes[currentYPostion].first;
        setNotify(xTileMode + ", " + yTileMode);
    }

    public static Pair<String, Shader.TileMode>[] tileModes = new Pair[]{
            new Pair("null", null),
            new Pair("CLAMP", Shader.TileMode.CLAMP),
            new Pair("MIRROR", Shader.TileMode.MIRROR),
            new Pair("REPEAT", Shader.TileMode.REPEAT),
    };

    @Override
    protected void onLeftTouchBoardMove(int x, int y, int dx, int dy) {

    }

    @Override
    protected void onRightTouchBoardMove(int x, int y, int dx, int dy) {

    }



}
