package org.ayo.robot.canvas.matrix;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

import org.ayo.robot.BaseView;

/**
 * Created by Administrator on 2016/12/19.
 */

public class MatrixView extends BaseView {
    public MatrixView(Context context) {
        super(context);
    }


    @Override
    protected void drawShape(Canvas canvas, int w, int h, Paint mPaint) {

        int rectW = 150;
        int rectH = 150;
        int left = w/2;
        int top = h/2;
        /*
         * 绘制一个红色矩形
         */
        mPaint.setColor(Color.RED);
        canvas.drawRect(left, top, top + rectW, top + rectH, mPaint);
        canvas.drawRect(0, 0, rectW, rectH, mPaint);

        /*
         * 保存画布并绘制一个黑色的矩形
         */
        canvas.save();
        mPaint.setColor(Color.BLACK);

        Matrix matrix = new Matrix();
        //matrix.setRotate(10, w/2, h/2);
        matrix.setTranslate(10, 10);
        canvas.setMatrix(matrix);

        canvas.drawRect(left, top, top + rectW, top + rectH, mPaint);
        canvas.drawRect(0, 0, rectW, rectH, mPaint);
        canvas.restore();
    }

    @Override
    public String getTitle() {
        return "canvas.setMatrix(Matrix)";
    }

    @Override
    public String getMethod() {
        return "自己配置Matrix";
    }

    @Override
    public String getComment() {
        return "效果可以叠加\n" +
                "set系列是替换\n" +
                "post系列是叠加（矩阵相乘，post的在后）\n" +
                "pre系列是叠加（矩阵相乘，pre的在前）\n" +
                "注意，同样是一个平移加一个缩放，谁先谁后不一样，因为矩阵相乘不能交换";
    }


}
