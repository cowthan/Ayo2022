package org.ayo.robot.paint.xfermode;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;

import org.ayo.core.Bitmaps;
import org.ayo.robot.R;

/**
 * Created by Administrator on 2016/12/19.
 */

public class PorterDuffXfermodeView2 extends BaseProterDuffView {
    public PorterDuffXfermodeView2(Context context) {
        super(context);
        init();
    }

    public PorterDuffXfermodeView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PorterDuffXfermodeView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PorterDuffXfermodeView2(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private Bitmap mBitmap;
    private PorterDuff.Mode mode = PorterDuff.Mode.CLEAR;
    int centerX = 50;
    int centerY = 50;
    int radius = 50;

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

        paint.setColor(Color.argb(255, 249, 119, 99));
        canvas.drawCircle(centerX, centerY, radius, paint);

        paint.setXfermode(null);

        canvas.restore();
        //-------------------
    }

    public void moveCircleBy(int dx, int dy){
        centerX += dx;
        centerY += dy;
        invalidate();
    }
    public void changeCircleRadiusBy(int dx){
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
