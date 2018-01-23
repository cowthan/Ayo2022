package org.ayo.robot.canvas.path;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

import org.ayo.robot.BaseView;

import static android.R.attr.endX;
import static android.R.attr.endY;

public class PathArcView extends BaseView {
    public PathArcView(Context context) {
        super(context);
        init();
    }

    private void init(){
    }

    Path mPath;

    int startX = 30;
    int startY = 30;

    @Override
    protected void drawShape(Canvas canvas, int w, int h, Paint paint) {

        ///画路径，从上一个点到指定弧线（可以直线相连，也可以断开，forceMoveTo控制）
        paint.setColor(Color.BLUE);
        mPath = new Path();
        mPath.moveTo(startX, startY);

        int left = centerX - rw/2;
        int top = centerY - rh/2;
        int right = left + rw;
        int bottom = top + rh;
        RectF oval = new RectF(left, top, right, bottom);
        //RectF oval = new RectF(endX, endY, endX + 100, endY + 100);
        mPath.arcTo(oval, startAngle, sweepAngle, forceMoveTo);
        canvas.drawPath(mPath, paint);

        //画外接矩形
        float oldWidth = paint.getStrokeWidth();
        Paint.Style oldStyle = paint.getStyle();
        paint.setStrokeWidth(0);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
        canvas.drawRect(oval, paint);
        paint.setStrokeWidth(oldWidth);
        paint.setStyle(oldStyle);

        oldWidth = paint.getStrokeWidth();
        paint.setStrokeWidth(7);
        canvas.drawPoint(startX, startY, paint);
        canvas.drawPoint(endX, endY, paint);
        paint.setStrokeWidth(oldWidth);
    }

    @Override
    public String getTitle() {
        return "arcTo(RectF oval, float startAngle, float sweepAngle, boolean forceMoveTo)";
    }

    @Override
    public String getMethod() {
        return "";
    }

    @Override
    public String getComment() {
        return "画路径，从上一个点到指定弧线（可以直线相连，也可以断开，forceMoveTo控制）\n" +
                "mPath.arcTo((RectF oval, float startAngle, float sweepAngle, boolean forceMoveTo)确定一条弧线\n" +
                "从当前点到这条弧线的起点就是咱的path，啥意思？？\n" +
                "如果forceMoveTo为true，就只剩一条弧线了";
    }

    int centerX, centerY;
    int rw, rh;
    boolean isInited = false;

    private int startAngle = 0;
    private int sweepAngle = 90;
    private boolean forceMoveTo = false;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if(!isInited){
            centerX = w/2;
            centerY = h/2;
            rw = 150;
            rh = 150;
            isInited = true;
        }
    }


    public void changeStartAngle(int dy){
        startAngle += dy;
        invalidate();
    }

    public void changeSweepAngle(int dy){
        sweepAngle += dy;
        invalidate();
    }

    public void changeRectSize(int dx, int dy){
        rw += dx;
        rh += dy;
        invalidate();
    }

    public void switchShape(){
        forceMoveTo = !forceMoveTo;
        invalidate();
    }
}