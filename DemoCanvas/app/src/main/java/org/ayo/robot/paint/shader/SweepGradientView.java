package org.ayo.robot.paint.shader;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.SweepGradient;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;

import org.ayo.robot.BaseView;


/**
 * Created by Administrator on 2016/12/21.
 */

public class SweepGradientView extends BaseView {
    public SweepGradientView(Context context) {
        super(context);
    }

    public SweepGradientView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SweepGradientView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SweepGradientView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    float cx = 100;
    float cy = 100;
    int startColor = Color.RED;
    int endColor = Color.YELLOW;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        cx = w/2;
        cy = h/2;
    }

    @Override
    protected void drawShape(Canvas canvas, int w, int h, Paint paint) {
        canvas.drawColor(Color.WHITE);
        paint.setShader(new SweepGradient(cx, cy, startColor, endColor));
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0, 0, w, h, paint);

        //绘制起点终点，便于观察
        paint.setShader(null);
        paint.setStrokeWidth(3);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(cx, cy, 5, paint);
        canvas.drawRect(0, 0, w, h, paint);
    }

    public void moveStartPoint(int dx, int dy){
        cx += dx;
        cy += dy;
        invalidate();
    }

    @Override
    public String getTitle() {
        return "mPaint.setShader(SweepGradient); ";
    }

    @Override
    public String getMethod() {
        return "SweepGradient(float cx, float cy, int color0, int color1) ";
    }

    @Override
    public String getComment() {
        return "扫描式渐变，或者叫梯度渐变";
    }
}
