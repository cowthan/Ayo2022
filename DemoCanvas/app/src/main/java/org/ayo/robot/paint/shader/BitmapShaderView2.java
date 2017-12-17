package org.ayo.robot.paint.shader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;

import org.ayo.lang.Bitmaps;
import org.ayo.robot.BaseView;
import org.ayo.robot.R;

/**
 * Created by Administrator on 2016/12/19.
 */

public class BitmapShaderView2 extends BaseView {
    public BitmapShaderView2(Context context) {
        super(context);
        init();
    }

    public BitmapShaderView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BitmapShaderView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BitmapShaderView2(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private Bitmap bitmap;

    Paint mStrokePaint;

    private void init(){
        bitmap = Bitmaps.getBitmapByResource(getContext(), R.drawable.brick, 100, 100);

        mStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mStrokePaint.setColor(0xFF000000);
        mStrokePaint.setStyle(Paint.Style.STROKE);
        mStrokePaint.setStrokeWidth(5);

        BitmapShader mBitmapShader = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        getPaint().setShader(mBitmapShader);
    }

    int centerX = 100;
    int centerY = 100;
    int radius = 100;

    @Override
    protected void drawShape(Canvas canvas, int w, int h, Paint paint) {
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.GRAY);
        //--------------------
        canvas.drawCircle(centerX, centerY, radius, mStrokePaint);
        canvas.drawCircle(centerX, centerY, radius, paint);
        //-------------------
    }

    public void moveCircleBy(int dx, int dy){
        centerX += dx;
        centerY += dy;
        invalidate();
    }

    public void changeRadiusBy(int dx){
        radius += dx;
        invalidate();
    }

    @Override
    public String getTitle() {
        return "paint.setShader(BitmapShader)";
    }

    @Override
    public String getMethod() {
        return "new BitmapShader(bitmap, Shader.TileMode xmode, Shader.TileMode ymode)";
    }

    @Override
    public String getComment() {
        return "用Bitmap给所绘图形着色\n" +
                "如果Bitmap小于绘图区域，填充方案由TileMode决定\n" +
                "如果Bitmap大于绘图区域，Bitmap就被裁剪了\n" +
                "点击左右TouchBoard看效果";
    }
}
