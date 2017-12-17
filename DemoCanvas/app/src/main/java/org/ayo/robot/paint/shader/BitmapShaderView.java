package org.ayo.robot.paint.shader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;

import org.ayo.lang.Bitmaps;
import org.ayo.robot.BaseView;
import org.ayo.robot.R;

/**
 * Created by Administrator on 2016/12/19.
 */

public class BitmapShaderView extends BaseView {
    public BitmapShaderView(Context context) {
        super(context);
        init();
    }

    public BitmapShaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BitmapShaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BitmapShaderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private Bitmap bitmap;
    private void init(){
        bitmap = Bitmaps.getBitmapByResource(getContext(), R.drawable.test5, 500, 500);
    }

    private boolean isGirl = true;
    public void onClicked(){
        if(isGirl) {
            bitmap = Bitmaps.getBitmapByResource(getContext(), R.mipmap.ic_launcher, 500, 500);
            isGirl = false;
        }else{
            bitmap = Bitmaps.getBitmapByResource(getContext(), R.drawable.test5, 500, 500);
            isGirl = true;
        }
        invalidate();
    }

    private Shader.TileMode xTileMode; // = Shader.TileMode.CLAMP;
    private Shader.TileMode yTileMode; // = Shader.TileMode.CLAMP;

    public void setXTileMode(Shader.TileMode mode){
        xTileMode = mode;
        invalidate();
    }
    public void setYTileMode(Shader.TileMode mode){
        yTileMode = mode;
        invalidate();
    }
    @Override
    protected void drawShape(Canvas canvas, int w, int h, Paint paint) {
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.GRAY);
        //--------------------
        if(xTileMode != null && yTileMode != null) paint.setShader(new BitmapShader(bitmap, xTileMode, yTileMode));
        canvas.drawRect(0, 0, w, h, paint);
        paint.setShader(null);
        //-------------------
    }

    @Override
    public String getTitle() {
        return "paint.setShader(BitmapShader)";
    }

    @Override
    public String getMethod() {
        return "new BitmapShader(bitmap, Shader.TileMode xmode, Shader.TileMode ymode)";
    }

    @Override
    public String getComment() {
        return "用Bitmap给所绘图形着色\n" +
                "如果Bitmap小于绘图区域，填充方案由TileMode决定\n" +
                "如果Bitmap大于绘图区域，Bitmap就被裁剪了\n" +
                "点击左右TouchBoard看效果\n" +
                "着色模式Shader：必须配合paint的FILL模式，因为这就是填充，至于填充什么颜色，由颜色， Shader来确定";
    }
}
