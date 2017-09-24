package org.ayo.robot.canvas.shape;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import org.ayo.robot.observe.Observable;

/**
 * 画一个shape，点击可以切换FILL，STROKE样式
 * 为了降低对各种图形坐标尺寸的混淆，这个自定义控件不考虑padding，并且最小尺寸（即wrap_content）是200px
 */
public abstract class ShapeView extends View {
    public ShapeView(Context context) {
        super(context);
        initttt();
    }

    public ShapeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initttt();
    }

    public ShapeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initttt();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ShapeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initttt();
    }

    private int mColor = Color.parseColor("#006633");
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private TripleState tripleState = new TripleState();

    public Paint getPaint(){
        return mPaint;
    }

    private void initttt(){
        mPaint.setColor(mColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.BUTT);
        mPaint.setStrokeJoin(Paint.Join.BEVEL);
        tripleState.toPositive();

//        this.setOnClickListener(new OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                tripleState.toNextState();
//                if(tripleState.isPositive()) {
//                    mPaint.setStyle(Paint.Style.STROKE);
//                    Toaster.toastShort("STROKE");
//                }else if(tripleState.isNutral()) {
//                    mPaint.setStyle(Paint.Style.FILL);
//                    Toaster.toastShort("FILL");
//                }else if(tripleState.isNegative()) {
//                    mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
//                    Toaster.toastShort("FILL_AND_STROKE");
//                }
//                invalidate();
//            }
//        });

        ///----------------------------------------
//        mPaint.setStyle(Paint.Style.STROKE);
//        mPaint.setStyle(Paint.Style.FILL);
//        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
//
//        mPaint.setStrokeWidth(10); //px
//        mPaint.setStrokeMiter(20);  //是设置笔画的倾斜度，如：小时候用的铅笔，削的时候斜与垂直削出来的笔尖效果是不一样的
//
//        //设置笔触的样式：影响画笔的始末端
//        mPaint.setStrokeCap(Paint.Cap.BUTT);
//        mPaint.setStrokeCap(Paint.Cap.ROUND);
//        mPaint.setStrokeCap(Paint.Cap.SQUARE);
//
//        //设置结合处的样子：影响画笔的结合处
//        mPaint.setStrokeJoin(Paint.Join.BEVEL); //BEVEL：结合处为直线
//        mPaint.setStrokeJoin(Paint.Join.MITER); //Miter:结合处为锐角
//        mPaint.setStrokeJoin(Paint.Join.ROUND); //Round:结合处为圆弧



    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawShape(canvas, getWidth(), getHeight(), mPaint);
    }

    protected abstract void drawShape(Canvas canvas, int w, int h, Paint paint);
    protected void onFingerMove(int x, int y, int dx, int dy){}
    protected void onFingerDown(int x, int y){}
    protected void onFingerUp(int x, int y){}

    //===========================================
    //为了让控件支持wrap_content时，内容尺寸取200px，需要我们重写measure过程
    //===========================================
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(
                mearureWidth(widthMeasureSpec),
                mearureHeight(heightMeasureSpec));
    }

    private int mearureWidth(int measureSpec){
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if(specMode == MeasureSpec.EXACTLY){
            result = specSize;
        }else{
            result = calculateContentWidth();
            if(specMode == MeasureSpec.AT_MOST){
                result = Math.min(result, specSize);
            }
        }

        return result;
    }

    private int mearureHeight(int measureSpec){
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if(specMode == MeasureSpec.EXACTLY){
            result = specSize;
        }else{
            result = calculateContentHeight();
            if(specMode == MeasureSpec.AT_MOST){
                result = Math.min(result, specSize);
            }
        }

        return result;
    }

    private int calculateContentWidth(){
        return 200;
    }

    private int calculateContentHeight(){
        return 200;
    }

    int mLastX = 0;
    int mLastY = 0;

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        super.onTouchEvent(e);
        boolean consume = false;
        int x = (int)e.getX();
        int y = (int)e.getY();
        if(e.getAction() == MotionEvent.ACTION_DOWN){
            consume = true;
            onFingerDown(x, y);
        }else if(e.getAction() == MotionEvent.ACTION_MOVE){
            int dx = x - mLastX;
            int dy = y - mLastY;
            consume = true;
            onFingerMove(x, y, dx, dy);
        }else if(e.getAction() == MotionEvent.ACTION_UP){
            consume = true;
            onFingerUp(x, y);
        }

        mLastX = x;
        mLastY = y;
        return consume;
    }

    protected Observable observable = new Observable();
    public Observable getObservable(){
        return observable;
    }

    public void setPaintColor(int color){
        mPaint.setColor(color);
        invalidate();
    }

    /**
     *
     * @param alpha  0-255
     */
    public void setPaintAlpha(int alpha){
        mPaint.setAlpha(alpha);
        invalidate();
    }

    public void setPaintStrokeWidth(float width){
        mPaint.setStrokeWidth(width);
        invalidate();
    }

    public void setPaintStrokeMiter(float miter){
        mPaint.setStrokeMiter(miter);
        invalidate();
    }

    public void setPaintStyle(Paint.Style style){
        mPaint.setStyle(style);
        invalidate();
    }


    public void setPaintStrokeCap(Paint.Cap cap){
        mPaint.setStrokeCap(cap);
        invalidate();
    }

    public void setPaintStrokeJoin(Paint.Join join){
        mPaint.setStrokeJoin(join);
        invalidate();
    }


}