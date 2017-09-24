package org.ayo.robot;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import org.ayo.observe.Observable;


/**
 * 画一个shape，点击可以切换FILL，STROKE样式
 * 为了降低对各种图形坐标尺寸的混淆，这个自定义控件不考虑padding，并且最小尺寸（即wrap_content）是200px
 */
public abstract class BaseView extends View {
    public BaseView(Context context) {
        super(context);
        initttt();
    }

    public BaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initttt();
    }

    public BaseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initttt();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BaseView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initttt();
    }

    private int mColor = Color.parseColor("#006633");
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public Paint getPaint(){
        return mPaint;
    }

    private void initttt(){
        mPaint.setColor(mColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.BUTT);
        mPaint.setStrokeJoin(Paint.Join.BEVEL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawShape(canvas, getWidth(), getHeight(), mPaint);
    }

    protected abstract void drawShape(Canvas canvas, int w, int h, Paint paint);

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

    protected Observable observable = new Observable();
    public Observable getObservable(){
        return observable;
    }

    int mLastX = 0;
    int mLastY = 0;
    private boolean isClickGood = true;
    private long fingerDownTime = 0;

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        //super.onTouchEvent(e);  //注掉这句就禁用了默认的setOnClickListener
        boolean consume = false;
        int x = (int)e.getX();
        int y = (int)e.getY();
        if(e.getAction() == MotionEvent.ACTION_DOWN){
            consume = true;
            if(onFingerTouchCallback != null) onFingerTouchCallback.onFingerDown(x, y);
            fingerDownTime = System.currentTimeMillis();
        }else if(e.getAction() == MotionEvent.ACTION_MOVE){
            int dx = x - mLastX;
            int dy = y - mLastY;
            consume = true;
            if(onFingerTouchCallback != null) onFingerTouchCallback.onFingerMove(x, y, dx, dy);
        }else if(e.getAction() == MotionEvent.ACTION_UP){
            consume = true;
            if(onFingerTouchCallback != null) onFingerTouchCallback.onFingerUp(x, y);
            if(System.currentTimeMillis() - fingerDownTime > 500){
                isClickGood = false;
            }else{
                isClickGood = true;
            }

            if(isClickGood){
                if(onFingerTouchCallback != null) onFingerTouchCallback.onClick(x, y);
            }
        }

        mLastX = x;
        mLastY = y;
        return consume;
    }


    public interface OnFingerTouchCallback{
        void onFingerMove(int x, int y, int dx, int dy);
        void onFingerDown(int x, int y);
        void onFingerUp(int x, int y);
        void onClick(int x, int y);
    }

    private OnFingerTouchCallback onFingerTouchCallback;
    public void setOnFingerTouchCallback(OnFingerTouchCallback callback){
        this.onFingerTouchCallback = callback;
    }

    public abstract String getTitle();
    public abstract String getMethod();
    public abstract String getComment();



    ///-------------------------------
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