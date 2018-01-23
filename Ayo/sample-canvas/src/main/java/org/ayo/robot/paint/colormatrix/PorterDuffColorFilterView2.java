package org.ayo.robot.paint.colormatrix;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;

import org.ayo.core.Bitmaps;
import org.ayo.robot.R;
import org.ayo.robot.paint.xfermode.BaseProterDuffView;

/**
 * Created by Administrator on 2016/12/19.
 */

public class PorterDuffColorFilterView2 extends BaseProterDuffView {
    public PorterDuffColorFilterView2(Context context) {
        super(context);
        init();
    }

    public PorterDuffColorFilterView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PorterDuffColorFilterView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PorterDuffColorFilterView2(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
        if(mode != null) paint.setColorFilter(new PorterDuffColorFilter(Color.BLUE, mode));
        canvas.drawBitmap(mBitmap, 0, 0, paint);
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
        return "PorterDuffColorFilter";
    }

    @Override
    public String getMethod() {
        return "paint.setColorFilter(new PorterDuffColorFilter(Color.BLUE, mode)";
    }

    @Override
    public String getComment() {
        return null;
    }
}
