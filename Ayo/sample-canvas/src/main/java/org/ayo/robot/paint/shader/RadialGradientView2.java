package org.ayo.robot.paint.shader;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;

import org.ayo.robot.BaseView;

/**
 * Created by Administrator on 2016/12/21.
 */

public class RadialGradientView2 extends BaseView {
    public RadialGradientView2(Context context) {
        super(context);
    }

    public RadialGradientView2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RadialGradientView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RadialGradientView2(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    float cx = 100;
    float cy = 100;
    float radius = 200;
    int startColor = Color.RED;
    int endColor = Color.YELLOW;
    Shader.TileMode tileMode = Shader.TileMode.REPEAT;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        cx = w/2;
        cy = h/2;
    }

    @Override
    protected void drawShape(Canvas canvas, int w, int h, Paint paint) {
        canvas.drawColor(Color.WHITE);
        paint.setShader(new RadialGradient(cx, cy, radius, new int[]{Color.WHITE, Color.BLACK, Color.GREEN}, null, tileMode));
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
    public void changeRadiusBy(int dx){
        radius += dx;
        invalidate();
    }
    public void setTileMode(Shader.TileMode mode){
        this.tileMode = mode;
        invalidate();
    }

    public void changeColor(int startColor, int endColor){
        this.startColor = startColor;
        this.endColor = endColor;
        invalidate();
    }

    @Override
    public String getTitle() {
        return "mPaint.setShader(RadialGradient); ";
    }

    @Override
    public String getMethod() {
        return "RadialGradient (float centerX, float centerY, float radius, int centerColor, int edgeColor, Shader.TileMode tileMode) ";
    }

    @Override
    public String getComment() {
        return "辐射式渐变，或者叫径向渐变\n" +
                "从圆心向边缘辐射式渐变\n" +
                "页支持多色渐变，positons也是渐变距离比例";
    }
}
