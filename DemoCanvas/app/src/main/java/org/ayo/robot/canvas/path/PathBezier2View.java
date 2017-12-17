package org.ayo.robot.canvas.path;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

import org.ayo.robot.BaseView;

public class PathBezier2View extends BaseView {
    public PathBezier2View(Context context) {
        super(context);
        init();
    }


    private void init(){
    }

    Path path;


    @Override
    protected void drawShape(Canvas canvas, int w, int h, Paint paint) {

        if(!isInited){

            startX = 30;
            startY = h / 2;
            endX = w - 30;
            endY = h/2;
            control_1_x = startX + 100;
            control_1_y = startY - 100;

            isInited = true;
        }

        paint.setStyle(Paint.Style.STROKE);

        path = new Path();
        path.moveTo(startX, startY);
        path.quadTo(control_1_x, control_1_y, endX, endY);
        canvas.drawPath(path, paint);

        float oldWidth = paint.getStrokeWidth();
        paint.setStrokeWidth(7);
        canvas.drawPoint(startX, startY, paint);
        canvas.drawPoint(endX, endY, paint);
        canvas.drawPoint(control_1_x, control_1_y, paint);

        paint.setStrokeWidth(oldWidth);
    }

    @Override
    public String getTitle() {
        return "quadTo(float x1, float y1, float x2, float y2)";
    }

    @Override
    public String getMethod() {
        return "二阶贝塞尔曲线";
    }

    @Override
    public String getComment() {
        return "x1, y1是控制点\n" +
                "x2, y2是终点\n";
    }


    public void moveControlPoint(int dx, int dy){
        path.reset();
        control_1_x += dx;
        control_1_y += dy;
        invalidate();
    }

    public void moveEndPoint(int dx, int dy){
        path.reset();
        endX += dx;
        endY += dy;
        invalidate();
    }

    int startX = 10;
    int startY = 150;
    int endX = startX + 150;
    int endY = startY;
    int control_1_x = 150;
    int control_1_y = 30;
    private boolean isInited = false;

}