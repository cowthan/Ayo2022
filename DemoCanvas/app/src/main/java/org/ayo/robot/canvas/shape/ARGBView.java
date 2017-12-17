package org.ayo.robot.canvas.shape;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import org.ayo.robot.BaseView;

public class ARGBView extends BaseView {
    public ARGBView(Context context) {
        super(context);
        init();
    }

    private void init(){

    }

    @Override
    protected void drawShape(Canvas canvas, int w, int h, Paint paint) {
        canvas.drawARGB(100, 77, 11, 111);
    }

    @Override
    public String getTitle() {
        return "canvas.drawARGB(100, 77, 11, 111)";
    }

    @Override
    public String getMethod() {
        return "argb";
    }

    @Override
    public String getComment() {
        return "没啥说的，也是给Canvas绘制背景";
    }

}