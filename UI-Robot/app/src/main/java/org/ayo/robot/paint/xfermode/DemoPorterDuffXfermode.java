package org.ayo.robot.paint.xfermode;

import android.graphics.PorterDuff;
import android.util.Pair;

import org.ayo.robot.BaseView;
import org.ayo.robot.DemoBase;

/**
 * Created by Administrator on 2016/12/19.
 */

public class DemoPorterDuffXfermode extends DemoBase {




    @Override
    protected BaseView createTestView() {
        setNotify("不加Xfermode");
        return new PorterDuffXfermodeView(getActivity());
    }


    int currentPostion = -1;

    @Override
    protected void onViewClicked(int x, int y) {
        currentPostion++;
        if(currentPostion == porterDuffModes.length){
            currentPostion = -1;
        }

        PorterDuffXfermodeView v = (PorterDuffXfermodeView) getTestView();
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
        PorterDuffXfermodeView v = (PorterDuffXfermodeView) getTestView();
        v.moveCircleBy(dx, dy);
    }

    @Override
    protected void onRightTouchBoardMove(int x, int y, int dx, int dy) {
        PorterDuffXfermodeView v = (PorterDuffXfermodeView) getTestView();
        v.moveRectBy(dx, dy);
    }

    public static Pair<String, PorterDuff.Mode>[] porterDuffModes = new Pair[]{
            new Pair("CLEAR：src碰到的区域，颜色都被清楚，抠出来一块", PorterDuff.Mode.CLEAR),
            new Pair("DARKEN：？？？src直接覆盖在dest身上", PorterDuff.Mode.DARKEN),
            new Pair("LIGHTEN：？？？src直接覆盖在dest身上", PorterDuff.Mode.LIGHTEN),
            new Pair("ADD：", PorterDuff.Mode.ADD),
            new Pair("MULTIPLY", PorterDuff.Mode.MULTIPLY),
            new Pair("XOR", PorterDuff.Mode.XOR),
            new Pair("SCREEN", PorterDuff.Mode.SCREEN),
            new Pair("OVERLAY", PorterDuff.Mode.OVERLAY),
            new Pair("DST", PorterDuff.Mode.DST),
            new Pair("DST_ATOP", PorterDuff.Mode.DST_ATOP),
            new Pair("DST_IN", PorterDuff.Mode.DST_IN),
            new Pair("DST_OUT", PorterDuff.Mode.DST_OUT),
            new Pair("DST_OVER", PorterDuff.Mode.DST_OVER),
            new Pair("SRC", PorterDuff.Mode.SRC),
            new Pair("SRC_ATOP", PorterDuff.Mode.SRC_ATOP),
            new Pair("SRC_IN", PorterDuff.Mode.SRC_IN),
            new Pair("SRC_OUT", PorterDuff.Mode.SRC_OUT),
            new Pair("SRC_OVER", PorterDuff.Mode.SRC_OVER),
    };


}
