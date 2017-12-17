package org.ayo.robot.paint.xfermode;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;

/**
 * Created by Administrator on 2016/12/19.
 */

public class PorterDuffXfermodeView extends BaseProterDuffView {
    public PorterDuffXfermodeView(Context context) {
        super(context);
        init();
    }

    public PorterDuffXfermodeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PorterDuffXfermodeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PorterDuffXfermodeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    Bitmap bmSrc;
    Bitmap bmDst;
    private PorterDuff.Mode mode = null;
    BitmapShader mBG;


    private void init(){
        setBackgroundColor(Color.TRANSPARENT);
        Bitmap bm = Bitmap.createBitmap(new int[] { 0xFFFFFFFF, 0xFFCCCCCC,
                        0xFFCCCCCC, 0xFFFFFFFF }, 2, 2,
                Bitmap.Config.RGB_565);
        mBG = new BitmapShader(bm,
                Shader.TileMode.REPEAT,
                Shader.TileMode.REPEAT);
        Matrix m = new Matrix();
        m.setScale(6, 6);
        mBG.setLocalMatrix(m);
    }

    public void setPorterDuffMode(PorterDuff.Mode mode){
        this.mode = mode;
        invalidate();
    }

    int centerX = 0;
    int centerY = 0;
    int rectLeft = 0;
    int rectTop = 0;
    boolean isInited = false;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        bmDst = makeDst(w, h);
        bmSrc = makeSrc(w, h);
    }

    @Override
    protected void drawShape(Canvas canvas, int w, int h, Paint paint) {
        paint.setStyle(Paint.Style.FILL);
        canvas.drawColor(Color.parseColor("#f4f4f4"));

        ///绘制网格背景
        // draw the checker-board pattern
        paint.setStyle(Paint.Style.FILL);
        paint.setShader(mBG);
        canvas.drawRect(0, 0, w, h, paint);

        if(!isInited){
            centerX = w/2;
            centerY = h/2;
            rectLeft = w/2;
            rectTop = h/2;
            isInited = true;
        }
        //--------------------
        int sc = canvas.saveLayer(0, 0, w, h, null, Canvas.MATRIX_SAVE_FLAG |
                Canvas.CLIP_SAVE_FLAG |
                Canvas.HAS_ALPHA_LAYER_SAVE_FLAG |
                Canvas.FULL_COLOR_LAYER_SAVE_FLAG |
                Canvas.CLIP_TO_LAYER_SAVE_FLAG);

        //绘制dst：一个圆，黄色
        paint.setColor(Color.YELLOW);
        canvas.drawBitmap(bmDst, 0, 0, paint);

        //设置xfermode，绘制src，一个矩形，蓝色
        if(mode != null) paint.setXfermode(new PorterDuffXfermode(mode));
        paint.setColor(Color.parseColor("#0000ff"));
        canvas.drawBitmap(bmSrc, 0, 0, paint);

        //恢复paint
        paint.setXfermode(null);

        canvas.restoreToCount(sc);
        //-------------------
       // paint.setStyle(Paint.Style.STROKE);
        //paint.setColor(Color.parseColor("#ff0000"));
        //canvas.drawRect(0, 0, w, h, paint);
//        canvas.drawCircle(w/2, h/2, 100, paint);
//        canvas.drawRect(w/2, h/2, w/2+200, h/2+200, paint);
    }

    public void moveCircleBy(int dx, int dy){
        centerX += dx;
        centerY += dy;
        invalidate();
    }

    public void moveRectBy(int dx, int dy){
        rectLeft += dx;
        rectTop += dy;
        invalidate();
    }

    @Override
    public String getTitle() {
        return "PorterDuffXfermode";
    }

    @Override
    public String getMethod() {
        return "mPaint.setXfermode(porterDuffXfermode)";
    }

    @Override
    public String getComment() {
        return "过渡模式，或者叫图形混合模式，色彩混合模式\n" +
                "相对于paint和canvas的两次绘图来说\n" +
                "先绘制dest，再设置Xfermode，再绘制src";
    }

    static Bitmap makeDst(int w, int h) {
        Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);

        p.setColor(0xFFFFCC44);
        c.drawOval(new RectF(0, 0, w*3/4, h*3/4), p);
        return bm;
    }

    // create a bitmap with a rect, used for the "src" image
    static Bitmap makeSrc(int w, int h) {
        Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);

        p.setColor(0xFF66AAFF);
        c.drawRect(w/3, h/3, w*19/20, h*19/20, p);
        return bm;
    }
}
