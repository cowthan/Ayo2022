package org.ayo.robot.paint.maskfilter;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;

import org.ayo.robot.BaseView;

/**
 * Created by Administrator on 2016/12/19.
 */

public class BlurMaskFilterView extends BaseView {
    public BlurMaskFilterView(Context context) {
        super(context);
        init();
    }

    public BlurMaskFilterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BlurMaskFilterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BlurMaskFilterView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        setLayerType(LAYER_TYPE_SOFTWARE, null);
    }

    private BlurMaskFilter.Blur blur;
    private int radius = 20;

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
        paint.setColor(Color.DKGRAY);
        canvas.drawCircle(w/2, h/2, 150, paint);
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
        return "BlurMaskFilter(float radius, Blur style)\n" +
                "BlurMaskFilter是根据Alpha通道的边界来计算模糊\n" +
                "radius：值越大我们的阴影越扩散\n" +
                "SOLID:在图像的Alpha边界外产生一层与Paint颜色一致的阴影效果而不影响图像本身\n" +
                "OUTER会在Alpha边界外产生一层阴影且会将原本的图像变透明\n" +
                "INNER则会在图像内部产生模糊\n" +
                "NORMAL：什么效果" +
                "混合模式和渐变可以获得更完美的内阴影效果\n";
    }
}
