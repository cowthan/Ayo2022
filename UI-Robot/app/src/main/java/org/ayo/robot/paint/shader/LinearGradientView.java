package org.ayo.robot.paint.shader;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;

import org.ayo.robot.BaseView;

/**
 * Created by Administrator on 2016/12/21.
 */

public class LinearGradientView extends BaseView {
    public LinearGradientView(Context context) {
        super(context);
    }

    public LinearGradientView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LinearGradientView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public LinearGradientView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    int startX = 5;
    int startY = 5;
    int endX = 300;
    int endY = 300;
    int startColor = Color.WHITE;
    int endColor = Color.BLACK;
    Shader.TileMode tileMode = Shader.TileMode.REPEAT;

    @Override
    protected void drawShape(Canvas canvas, int w, int h, Paint paint) {
        canvas.drawColor(Color.WHITE);
        paint.setShader(new LinearGradient(startX, startY, endX, endY, startColor, endColor, tileMode));
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0, 0, w, h, paint);

        //绘制起点终点，便于观察
        paint.setShader(null);
        paint.setStrokeWidth(3);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(startX, startY, 5, paint);
        canvas.drawCircle(endX, endY, 5, paint);
        canvas.drawLine(startX, startY, endX, endY, paint);
        canvas.drawRect(0, 0, w, h, paint);
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

    public void changeColor(int startColor, int endColor){
        this.startColor = startColor;
        this.endColor = endColor;
        invalidate();
    }

    public void setTileMode(Shader.TileMode mode){
        this.tileMode = mode;
        invalidate();
    }

    @Override
    public String getTitle() {
        return "paint.setShader(LinearGradient)";
    }

    @Override
    public String getMethod() {
        return "new LinearGradient(startX, startY, endX, endY, startColor, endColor, Shader.TileMode.REPEAT)";
    }

    @Override
    public String getComment() {
        return "从起点到终点的颜色线性渐变\n" +
                "如果渐变区域不充满绘制区域，则TileMode决定填充方式\n" +
                "滑动TouchBoard，点击View观察效果\n" +
                "点击左TouchBoard，切换TileMode";
    }
}
