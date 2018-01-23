package org.ayo.robot.paint.shader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
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

public class RadialGradientView3 extends BaseView {
    public RadialGradientView3(Context context) {
        super(context);
        init();
    }

    public RadialGradientView3(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RadialGradientView3(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RadialGradientView3(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    Bitmap bitmap;
    float bw, bh;

    private void init(){
        bitmap = Bitmaps.getBitmapByResource(getContext(), R.drawable.test5, 500, 500);
        bw = bitmap.getWidth();
        bh = bitmap.getHeight();
    }


    @Override
    protected void drawShape(Canvas canvas, int w, int h, Paint paint) {
        canvas.drawColor(Color.WHITE);

        canvas.drawBitmap(bitmap, 0, 0, null);

        paint.setShader(new RadialGradient(bw/2, bh/2, bh*7/8, Color.TRANSPARENT, Color.BLACK, Shader.TileMode.CLAMP));
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0, 0, w, h, paint);

        paint.setStyle(Paint.Style.STROKE);
        //canvas.drawColor(Color.YELLOW);
        canvas.drawCircle(bw/2, bh/2, bh*7/8, paint);
        canvas.drawCircle(bw/2, bh/2, 5, paint);

    }


    @Override
    public String getTitle() {
        return "mPaint.setShader(RadialGradient); ";
    }

    @Override
    public String getMethod() {
        return "RadialGradient (float centerX, float centerY, float radius, int centerColor, int edgeColor, Shader.TileMode tileMode) ";
    }

    @Override
    public String getComment() {
        return "辐射式渐变，或者叫径向渐变\n" +
                "从圆心向边缘辐射式渐变\n" +
                "页支持多色渐变，positons也是渐变距离比例";
    }
}
