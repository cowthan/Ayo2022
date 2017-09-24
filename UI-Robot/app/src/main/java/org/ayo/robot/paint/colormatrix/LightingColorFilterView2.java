package org.ayo.robot.paint.colormatrix;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;

import org.ayo.lang.Bitmaps;
import org.ayo.robot.BaseView;
import org.ayo.robot.R;

/**
 * Created by Administrator on 2016/12/19.
 */

public class LightingColorFilterView2 extends BaseView {
    public LightingColorFilterView2(Context context) {
        super(context);
        init();
    }

    public LightingColorFilterView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LightingColorFilterView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public LightingColorFilterView2(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }
    Bitmap mBitmap;

    private void init(){
        mBitmap = Bitmaps.getBitmapByResource(getContext(), R.drawable.test5, 500, 500);
    }
    private LightingColorFilter colorFilter;

    public void setLightingColorFilter(LightingColorFilter filter){
        this.colorFilter = filter;
        invalidate();
    }
    @Override
    protected void drawShape(Canvas canvas, int w, int h, Paint paint) {
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.argb(255, 249, 119, 99));
        //--------------------
        paint.setColorFilter(colorFilter);
        //-------------------
        canvas.drawBitmap(mBitmap, 0, 0, paint);
    }

    @Override
    public String getTitle() {
        return "LightingColorFilter";
    }

    @Override
    public String getMethod() {
        return "mPaint.setColorFilter(new LightingColorFilter(int mul, int add))";
    }

    @Override
    public String getComment() {
        return "mul和add都是0xAARRGGBB的颜色值\n" +
                "mul全称是colorMultiply意为色彩倍增\n" +
                "add全称是colorAdd意为色彩添加\n" +
                "具体原理还得参考色彩处理的相关文章\n" +
                "如果要修改bitmap的颜色，这个就有用了\n" +
                "公式好像是先和mul或运算，再和add加";
    }
}
