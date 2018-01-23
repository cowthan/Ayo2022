package org.ayo.robot.paint.shader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;

import org.ayo.core.Bitmaps;
import org.ayo.robot.BaseView;
import org.ayo.robot.R;

/**
 * Created by Administrator on 2016/12/21.
 */

public class LinearGradientView3 extends BaseView {
    public LinearGradientView3(Context context) {
        super(context);
        init();
    }

    public LinearGradientView3(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LinearGradientView3(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public LinearGradientView3(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    int startX = 5;
    int startY = 5;
    int endX = 300;
    int endY = 300;
    int startColor = 0xAA000000;
    int endColor = Color.TRANSPARENT;
    Shader.TileMode tileMode = Shader.TileMode.CLAMP;

    private Bitmap mBitmap;

    private void init(){
        mBitmap = Bitmaps.getBitmapByResource(getContext(), R.drawable.test5, 500, 500);
    }

    @Override
    protected void drawShape(Canvas canvas, int w, int h, Paint paint) {
        canvas.drawColor(Color.WHITE);

        canvas.drawBitmap(mBitmap, 0, 0, null);

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
        return "";
    }

    @Override
    public String getComment() {
        return "";
    }
}
