package org.ayo.fringe.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import org.ayo.view.Display;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 单个字母落下，速度不变，每一帧增加一个字母
 */

public class BlackEmpireView_v1 extends View {
    public BlackEmpireView_v1(Context context) {
        super(context);
        init();
    }

    public BlackEmpireView_v1(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BlackEmpireView_v1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BlackEmpireView_v1(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    Paint textPaint;
    Paint.FontMetrics fontMetrics;

    private void init(){
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.YELLOW);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        textPaint = new Paint( Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize( 35);
        textPaint.setColor(Color.GREEN);

        // FontMetrics对象
        fontMetrics = textPaint.getFontMetrics();
    }


    Paint paint;

    static String  str1 = "123456545634534532qwertyuiopasdfghjklmnbvcxz,./;'[]";

    public static int randomInt(int min, int max){
        Random r = new Random(System.currentTimeMillis());
        int i = r.nextInt(max - min);
        return min + i;
    }

    public static char randomChar1(){
        int r = randomInt(0, str1.length());
        return str1.charAt(r);
    }

    public static String randomString(int len){
        String s = "";
        for(int i = 0; i < len; i++){
            s += randomChar1();
        }
        return s;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //canvas.drawRect(10, 10, 200, 200, paint);

        for(Info info: infos){
            //log.i("black", "draw--" + info.str + "--" + info.left + ", " + info.top + "----------" + infos.size());
            //canvas.drawCircle(info.left, info.top, 4, paint);
            // 绘制文本
            if(info.top <= Display.screenHeight)  canvas.drawText( info.str, info.left, info.top, textPaint);
        }
        run();
    }

    class Info{
        public String str;
        public int left;
        public int top;
    }

    public List<Info> infos = new CopyOnWriteArrayList<>();

    /**
     * 每隔16ms，
     * 1 产生一个
     * 2 当前所有的
     * - 变字
     * - 继续下落
     * - 如果top在屏幕以下，remove掉
     */
    public void run(){

        ///所有之前的，下落
        for(Info info: infos){
            info.top += 8;
        }

        ///生成个新的
        Info info = new Info();
        info.left = randomInt(0, Display.screenWidth);
        info.top = 0;
        info.str = randomChar1() + "";
        infos.add(info);

        postInvalidate();
    }


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

    ///你自己测量宽度：宽度wrap_content时，会走这
    private int calculateContentWidth(){
        return 200;
    }

    ///你自己测量高度：宽度wrap_content时，会走这
    private int calculateContentHeight(){
        return 200;
    }
}
