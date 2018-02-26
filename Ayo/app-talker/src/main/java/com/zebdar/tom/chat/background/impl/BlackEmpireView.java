package com.zebdar.tom.chat.background.impl;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import org.ayo.core.Lang;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Administrator on 2017/1/19 0019.
 */

public class BlackEmpireView extends View {
    public BlackEmpireView(Context context) {
        super(context);
        init();
    }

    public BlackEmpireView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BlackEmpireView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BlackEmpireView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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

    static String  str1 = " 123456545634534532qwertyuiopasdfghjklmnbvcxz";
    static Random r = new Random(System.currentTimeMillis());
    public static int randomInt(int min, int max){

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

    private int frameCount = 0;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //canvas.drawRect(10, 10, 200, 200, paint);

        for(Info info: infos){
            //log.i("black", "draw--" + info.str + "--" + info.left + ", " + info.top + "----------" + infos.size());
            //canvas.drawCircle(info.left, info.top, 4, paint);
            // 绘制文本
            if(info.top <= Lang.screenHeight())  {
                for(int i = 0; i < info.str.length(); i++){
                    canvas.drawText( info.str.charAt(i) + "", info.left, info.top + (i * 50), textPaint);  //一个字母高50
                }
            }
        }
        run();
    }

    class Info{
        public String str;
        public int left;
        public int top;

        public int drawCount = 0;
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
            info.top += 14;
            info.drawCount++;
            if(info.drawCount > 10) {
                info.str = randomString(info.str.length());
                info.drawCount = 0;
            }
        }

        ///生成个新的
        frameCount++;
        if(frameCount == 3){
            Info info = new Info();
            info.left = randomInt(0, Lang.screenWidth());
            info.top = -300;
            info.str = randomString(randomInt(5, 12));
            infos.add(info);
            frameCount = 0;
        }

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
