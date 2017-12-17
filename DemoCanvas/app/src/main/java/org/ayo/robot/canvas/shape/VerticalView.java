package org.ayo.robot.canvas.shape;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import org.ayo.robot.R;

public class VerticalView extends View {

    private final Paint mPaint = new Paint();
    private final float[] mVerts = new float[10];
    private final float[] mTexs = new float[10];
    private final short[] mIndices = { 0, 1, 2, 3, 4, 1 };

    private final Matrix mMatrix = new Matrix();
    private final Matrix mInverse = new Matrix();


    public VerticalView(Context context) {
        super(context);
        init();
    }

    public VerticalView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VerticalView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public VerticalView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private static void setXY(float[] array, int index, float x, float y) {
        array[index*2 + 0] = x;
        array[index*2 + 1] = y;
    }


    private void init(){
        setFocusable(true);

        Bitmap bm = BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_launcher);
        Shader s = new BitmapShader(bm, Shader.TileMode.CLAMP,
                Shader.TileMode.CLAMP);
        mPaint.setShader(s);//通过BitmapShader设置Paint的Shader

        float w = bm.getWidth();
        float h = bm.getHeight();
        // construct our mesh
        setXY(mTexs, 0, w/2, h/2);
        setXY(mTexs, 1, 0, 0);
        setXY(mTexs, 2, w, 0);
        setXY(mTexs, 3, w, h);
        setXY(mTexs, 4, 0, h); //初始化图片纹理映射坐标

        setXY(mVerts, 0, w/2, h/2);
        setXY(mVerts, 1, 0, 0);
        setXY(mVerts, 2, w, 0);
        setXY(mVerts, 3, w, h);
        setXY(mVerts, 4, 0, h);//初始化顶点数据数组

        mMatrix.setScale(0.8f, 0.8f);
        mMatrix.preTranslate(20, 20);
        mMatrix.invert(mInverse); //初始化变形矩阵
    }

    @Override protected void onDraw(Canvas canvas) {
        canvas.drawColor(0xFFCCCCCC); //绘制背景色
        canvas.save();  //变形canvas前先保存canvas现场
        canvas.concat(mMatrix); //设置变换矩阵

        canvas.drawVertices(Canvas.VertexMode.TRIANGLE_FAN, 10, mVerts, 0,
                mTexs, 0, null, 0, null, 0, 0, mPaint);
        //绘制当前顶点坐标和纹理坐标决定的图片Paint，得到图片变形效果。

        canvas.translate(0, 240);//向下平移canvas
        canvas.drawVertices(Canvas.VertexMode.TRIANGLE_FAN, 10, mVerts, 0,
                mTexs, 0, null, 0, mIndices, 0, 6, mPaint);
        //绘制当前顶点坐标和纹理坐标和索引数组决定的图片Paint，得到另外的图片变形效果。
        canvas.restore();//变换完成后恢复canvas现场
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float[] pt = { event.getX(), event.getY() };
        mInverse.mapPoints(pt); // 根据当前的触摸位置变换Marix
        setXY(mVerts, 0, pt[0], pt[1]); //据触摸的位置变换顶点坐标
        invalidate();//刷新界面，触发onDraw方法被再次调用
        return true;
    }
}