package org.ayo.robot.canvas.path;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

import org.ayo.robot.BaseView;

public class PathBezier3View extends BaseView {
    public PathBezier3View(Context context) {
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
            control_2_x = endX - 100;
            control_2_y = startY + 100;

            isInited = true;
        }

        paint.setStyle(Paint.Style.STROKE);

        path = new Path();
        path.moveTo(startX, startY);
        path.cubicTo(control_1_x, control_1_y, control_2_x, control_2_y, endX, endY);
        canvas.drawPath(path, paint);

        float oldWidth = paint.getStrokeWidth();
        paint.setStrokeWidth(10);
        canvas.drawPoint(startX, startY, paint);
        canvas.drawPoint(endX, endY, paint);
        canvas.drawPoint(control_1_x, control_1_y, paint);
        canvas.drawPoint(control_2_x, control_2_y, paint);
        paint.setStrokeWidth(oldWidth);
    }

    @Override
    public String getTitle() {
        return "cubicTo(float x1, float y1, float x2, float y2, float endX, float endY)";
    }

    @Override
    public String getMethod() {
        return "三阶贝塞尔曲线";
    }

    @Override
    public String getComment() {
        return "三阶贝塞尔曲线\n" +
                "x1,y1和x2,y2是两个控制点\n";
    }


    int startX = 30;
    int startY = 300;
    int endX = startX + 150;
    int endY = startY;
    int control_1_x = 150;
    int control_1_y = 30;
    int control_2_x = 150;
    int control_2_y = 30;
    private boolean isInited = false;


    public void changeControlPoint_1(int dx, int dy){
        control_1_x += dx;
        control_1_y += dy;
        invalidate();
    }

    public void changeControlPoint_2(int dx, int dy){
        control_2_x += dx;
        control_2_y += dy;
        invalidate();
    }

    public void moveEndPoint(int dx, int dy){
        path.reset();
        path.moveTo(startX, startY);
        endX += dx;
        endY += dy;
        invalidate();
    }
}