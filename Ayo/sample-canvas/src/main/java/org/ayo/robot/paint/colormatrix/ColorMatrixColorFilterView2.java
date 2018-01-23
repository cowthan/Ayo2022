package org.ayo.robot.paint.colormatrix;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
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

public class ColorMatrixColorFilterView2 extends BaseView {
    public ColorMatrixColorFilterView2(Context context) {
        super(context);
        init();
    }

    public ColorMatrixColorFilterView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ColorMatrixColorFilterView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ColorMatrixColorFilterView2(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    Bitmap mBitmap;

    private void init(){
        mBitmap = Bitmaps.getBitmapByResource(getContext(), R.drawable.test5, 500, 500);
    }
    @Override
    protected void drawShape(Canvas canvas, int w, int h, Paint paint) {
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.argb(255, 249, 119, 99));
        //--------------------
        if(matrix == null){
            paint.setColorFilter(null);
        }else{
            paint.setColorFilter(new ColorMatrixColorFilter(matrix));
        }
        //-------------------
        canvas.drawBitmap(mBitmap, 0, 0, paint);
    }

    @Override
    public String getTitle() {
        return "ColorMatrxColorFilter";
    }

    @Override
    public String getMethod() {
        return "paint.setColorFilter(new ColorMatrixColorFilter(matrix))";
    }

    @Override
    public String getComment() {
        return "构造不同的matrix，对经过paint的所有绘制结果进行颜色过滤\n" +
                "所谓ColorMatrixColorFilter，就是使用颜色矩阵进行过滤";
    }

    private ColorMatrix matrix;

    public void setColorMatrix(ColorMatrix matrix){
        this.matrix = matrix;
        invalidate();
    }

}
