package org.ayo.robot.canvas.shape;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import org.ayo.robot.BaseView;

public class PaintView extends BaseView {
    public PaintView(Context context) {
        super(context);
        init();
    }

    private void init(){

    }

    @Override
    protected void drawShape(Canvas canvas, int w, int h, Paint paint) {
        canvas.drawPaint(paint);
    }

    @Override
    public String getTitle() {
        return "canvas.drawPaint(paint)";
    }

    @Override
    public String getMethod() {
        return "Paint的stroke，fill，shader，colorfilter等都会影响";
    }

    @Override
    public String getComment() {
        return "在View上绘制，Paint的stroke，fill，shader，colorfilter都会影响最终效果";
    }

}