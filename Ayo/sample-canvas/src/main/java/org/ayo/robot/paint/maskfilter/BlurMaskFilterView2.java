package org.ayo.robot.paint.maskfilter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;

import org.ayo.core.Bitmaps;
import org.ayo.robot.BaseView;
import org.ayo.robot.R;

/**
 * Created by Administrator on 2016/12/19.
 */

public class BlurMaskFilterView2 extends BaseView {
    public BlurMaskFilterView2(Context context) {
        super(context);
        init();
    }

    public BlurMaskFilterView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BlurMaskFilterView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BlurMaskFilterView2(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        bitmap = Bitmaps.getBitmapByResource(getContext(), R.drawable.test5, 500, 500);
        shadowBitmap = bitmap.extractAlpha();
    }

    private BlurMaskFilter.Blur blur;
    private int radius = 20;
    private Bitmap bitmap;
    private Bitmap shadowBitmap;

    public void setBlur(BlurMaskFilter.Blur blur){
        this.blur = blur;
        invalidate();
    }

    public void changeRadiusBy(int dx){
        radius += dx;
        if(radius < 1) radius = 1;
        getObservable().notifyDataChanged(this, "radius = " + radius);
        invalidate();
    }

    @Override
    protected void drawShape(Canvas canvas, int w, int h, Paint paint) {
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.argb(255, 249, 119, 99));
        //--------------------

        if(blur != null) paint.setMaskFilter(new BlurMaskFilter(radius, blur));
        canvas.drawBitmap(bitmap, 40, 40, null);  ///这里不能用设置了MaskFilter的paint，否则效果很奇怪
        paint.setColor(Color.DKGRAY);  //这个就是阴影的颜色
        if(blur != null) canvas.drawBitmap(shadowBitmap, 40, 40, paint);
        paint.setMaskFilter(null);
        //-------------------
    }

    @Override
    public String getTitle() {
        return "mPaint.setMaskFilter(BlurMaskFilter)";
    }

    @Override
    public String getMethod() {
        return "new BlurMaskFilter(20, BlurMaskFilter.Blur.SOLID)";
    }

    @Override
    public String getComment() {
        return "给Bitmap加阴影有点麻烦，因为\n" +
                "BlurMaskFilter是根据Alpha通道的边界来计算模糊\n" +
                "而res里的png都被转成了RGB565\n" +
                "所以得借助extractAlpha抽取出alpha通道";
    }
}
