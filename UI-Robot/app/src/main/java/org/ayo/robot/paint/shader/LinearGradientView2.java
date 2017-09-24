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

public class LinearGradientView2 extends BaseView {
    public LinearGradientView2(Context context) {
        super(context);
    }

    public LinearGradientView2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LinearGradientView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public LinearGradientView2(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    int startX = 5;
    int startY = 5;
    int endX = 300;
    int endY = 300;
    Shader.TileMode tileMode = Shader.TileMode.REPEAT;

    @Override
    protected void drawShape(Canvas canvas, int w, int h, Paint paint) {
        canvas.drawColor(Color.WHITE);
        paint.setShader(new LinearGradient(startX, startY, endX, endY,
                new int[] { Color.RED, Color.YELLOW, Color.GREEN, Color.CYAN, Color.BLUE },
                new float[] { 0, 0.1F, 0.5F, 0.7F, 0.8F },
                tileMode));
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
        return "new LinearGradient(startX, startY, endX, endY, int[] colors, float[] postions, Shader.TileMode.REPEAT)";
    }

    @Override
    public String getComment() {
        return "这个好理解，参数int[] colors定义了一组颜色，两两渐变\n" +
                "两两之间得有个距离，就是float[] positions\n" +
                "postion是[0, 1]，表示颜色两两之间距离占的比重\n" +
                "比重就是相对于起点和重点的距离来说\n" +
                "起点终点就是前四个参数决定的\n" +
                "所以其实就是多个LinearGradient的组合\n" +
                "postions可以是null，表示平分";
    }
}
