package org.ayo.robot.canvas.clip;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import org.ayo.robot.BaseView;

public class ClipPathView extends BaseView {
    public ClipPathView(Context context) {
        super(context);
        init();
    }


    Path path;

    private void init(){
        path = new Path();
        path.moveTo(50, 50);
        path.lineTo(200, 200);
        path.lineTo(100, 300);
        path.close();
    }

    private boolean nowYouCanClip = true;
    private boolean isFirst = true;

    @Override
    protected void drawShape(Canvas canvas, int w, int h, Paint paint) {
        ///绘制path路径，便于观察
        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLUE);
        canvas.drawPath(path, paint);

        ///剪切一块path指定的区域
        if(nowYouCanClip || isFirst){
            canvas.save();
            canvas.clipPath(path);
            canvas.drawColor(Color.parseColor("#55000000"));
            canvas.restore();
            nowYouCanClip = false;
            isFirst = false;
        }
    }

    @Override
    public String getTitle() {
        return "canvas.clipPath(Path)";
    }

    @Override
    public String getMethod() {
        return "剪切path指定的闭合图形区域";
    }

    @Override
    public String getComment() {
        return "以一个闭合图形为裁剪区域\n" +
                "如果Path没有close，则会自动close\n" +
                "鉴于Path的功能比较强大，这个方法功能也就跟着强大了";
    }


    public void onMove(int dx, int dy){
        path.rLineTo(dx, dy);
        invalidate();
    }

    public void onStart(int x, int y){
        path.reset();
        path.moveTo(x, y);
        invalidate();
    }


    public void onStop(int x, int y){
        nowYouCanClip = true;
        invalidate();
    }


}