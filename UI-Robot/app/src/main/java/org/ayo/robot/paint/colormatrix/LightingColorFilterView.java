package org.ayo.robot.paint.colormatrix;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;

import org.ayo.robot.BaseView;

/**
 * Created by Administrator on 2016/12/19.
 */

public class LightingColorFilterView extends BaseView {
    public LightingColorFilterView(Context context) {
        super(context);
        init();
    }

    public LightingColorFilterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LightingColorFilterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public LightingColorFilterView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){

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
        paint.setColor(Color.RED);
        canvas.drawCircle(100, 100, 50, paint);
        paint.setColor(Color.GREEN);
        canvas.drawCircle(200, 100, 50, paint);
        paint.setColor(Color.BLUE);
        canvas.drawCircle(100, 200, 50, paint);
        paint.setColor(Color.YELLOW);
        canvas.drawCircle(200, 200, 50, paint);
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
