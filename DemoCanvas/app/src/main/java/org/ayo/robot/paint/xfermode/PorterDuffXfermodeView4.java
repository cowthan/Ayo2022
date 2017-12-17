package org.ayo.robot.paint.xfermode;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;

import org.ayo.lang.Bitmaps;
import org.ayo.robot.R;

/**
 * Created by Administrator on 2016/12/19.
 */

public class PorterDuffXfermodeView4 extends BaseProterDuffView {
    public PorterDuffXfermodeView4(Context context) {
        super(context);
        init();
    }

    public PorterDuffXfermodeView4(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PorterDuffXfermodeView4(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PorterDuffXfermodeView4(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private Bitmap mBitmap;
    private PorterDuff.Mode mode = PorterDuff.Mode.CLEAR;
    int centerX = 50;
    int centerY = 50;
    int radius = 5;
    Path mPath;

    public void setPorterDuffMode(PorterDuff.Mode mode){
        this.mode = mode;
        invalidate();
    }
    private void init(){
        mBitmap = Bitmaps.getBitmapByResource(getContext(), R.drawable.test5, 500, 500);
    }

    @Override
    protected void drawShape(Canvas canvas, int w, int h, Paint paint) {
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.argb(255, 249, 119, 99));
        //--------------------
        canvas.drawBitmap(mBitmap, 0, 0, paint);

        canvas.saveLayer(0,0, w, h, null, Canvas.ALL_SAVE_FLAG);

        paint.setColor(Color.argb(255, 0, 0, 0));
        canvas.drawRect(0, 0, w, h, paint);

        if(mode != null) paint.setXfermode(new PorterDuffXfermode(mode));

        paint.setColor(Color.argb(180, 249, 119, 99));
        paint.setStrokeWidth(radius * 2);
        paint.setStyle(Paint.Style.STROKE);
        if(mPath != null) canvas.drawPath(mPath, paint);

        paint.setXfermode(null);
        paint.setStrokeWidth(2);
        canvas.drawCircle(centerX, centerY, radius, paint);

        canvas.restore();
        //-------------------
    }

    public void move(int dx, int dy){
        if(mPath == null){
            mPath = new Path();
            mPath.moveTo(50, 50);
        }
        mPath.rLineTo(dx, dy);
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
        return null;
    }

    @Override
    public String getMethod() {
        return null;
    }

    @Override
    public String getComment() {
        return null;
    }
}
