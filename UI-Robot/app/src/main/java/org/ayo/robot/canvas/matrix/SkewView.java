package org.ayo.robot.canvas.matrix;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import org.ayo.robot.BaseView;

import static android.R.attr.centerX;
import static android.R.attr.centerY;

/**
 * Created by Administrator on 2016/12/19.
 */

public class SkewView extends BaseView {
    public SkewView(Context context) {
        super(context);
    }


    @Override
    protected void drawShape(Canvas canvas, int w, int h, Paint mPaint) {

        getObservable().notifyDataChanged(this, "skew(" + sx + ", " + sy + ")");
        int rectW = 150;
        int rectH = 150;
        int left = w/2;
        int top = h/2;
        /*
         * 绘制一个红色矩形
         */
        mPaint.setColor(Color.RED);
        canvas.drawRect(left, top, top + rectW, top + rectH, mPaint);
        canvas.drawCircle(centerX, centerY, 10, mPaint);

        /*
         * 保存画布并绘制一个黑色的矩形
         */
        canvas.save();
        mPaint.setColor(Color.BLACK);
        canvas.skew(sx, sy);
        canvas.drawRect(left, top, top + rectW, top + rectH, mPaint);
        canvas.drawRect(0, 0, rectW, rectH, mPaint);
        canvas.restore();
    }

    @Override
    public String getTitle() {
        return "canvas.skew(sx, sy)";
    }

    @Override
    public String getMethod() {
        return "错切";
    }

    @Override
    public String getComment() {
        return "没细研究";
    }

    float sx = 0.1f;
    float sy = 0.1f;

    protected void change(int dx, int dy) {
        sx += dx/50f;
        sy += dy/50f;
        invalidate();
    }



}
