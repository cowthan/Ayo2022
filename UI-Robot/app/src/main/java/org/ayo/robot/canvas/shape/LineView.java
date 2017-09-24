package org.ayo.robot.canvas.shape;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import org.ayo.robot.BaseView;

public class LineView extends BaseView {
    public LineView(Context context) {
        super(context);
        init();
    }

    private void init(){

    }

    int startX = 100;
    int startY = 190;
    int endX = 200;
    int endY = 200;

    @Override
    protected void drawShape(Canvas canvas, int w, int h, Paint paint) {
        canvas.drawLine(startX, startY, endX, endY, paint);
    }

    public void moveStartPoint(int dx, int dy){
        startX += dx;
        startY += dy;
        invalidate();
    }

    public void moveEndPoint(int dx, int dy){
        endX += dx;
        endY += dy;
        invalidate();
    }

    @Override
    public String getTitle() {
        return "canvas.drawLine(startX, startY, endX, endY, paint)";
    }

    @Override
    public String getMethod() {
        return "画条直线";
    }

    @Override
    public String getComment() {
        return "drawLine(startX, startY, endX, endY, paint)：使用四个点画个直线\n" +
                "drawLines(@Size(multiple=4) float[] pts, int offset, int count, Paint paint)\n" +
                "pts：每两个值是一个点，两个点是一条线\n" +
                "offest: Number of values in the array to skip before drawing\n" +
                "count：画几条直线，skip过offset个值之后，处理count条直线，注意，是count*4个值\n" +
                "是离散的直线";
    }

}