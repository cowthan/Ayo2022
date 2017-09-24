package org.ayo.robot.canvas.clip;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import org.ayo.robot.BaseView;

public class ClipRectView extends BaseView {
    public ClipRectView(Context context) {
        super(context);
        init();
    }


    private void init(){
    }

    private boolean clipTop = true;

    public void changeRect(){
        clipTop = !clipTop;
        invalidate();
    }

    @Override
    protected void drawShape(Canvas canvas, int w, int h, Paint paint) {
        RectF top = new RectF(5, 5, 2*w/3.0f, 2*h/3.0f);
        RectF bottom = new RectF(w/3.0f, h/3.0f, w-5, h-5);
        paint.setStyle(Paint.Style.STROKE);

        canvas.drawRect(top, paint);
        canvas.drawRect(bottom, paint);

        if(clipTop){
            top = new RectF(5, 5, 2*w/3.0f, 2*h/3.0f);
        }else{
            top.intersect(bottom);
        }
        canvas.clipRect(top);

        ///剪切后，绘制一个背景
        canvas.drawColor(Color.parseColor("#55000000"));
    }

    @Override
    public String getTitle() {
        return "canvas.clipRect(Rect)";
    }

    @Override
    public String getMethod() {
        return "剪切一个矩形区域";
    }

    @Override
    public String getComment() {
        return "剪切一个矩形区域\n" +
                "可以利用Rect和RectF提供的有关矩形的算法，如相交，union等\n" +
                "但结果都是一个矩形\n" +
                "图中实在整个canvas上drawColor，不过受到了clip的限制\n" +
                "所以所有draw动作都是基于clip区域的，默认剪切区域就是全canvas\n" +
                "出了剪切区域的draw是看不到东西的";
    }

}