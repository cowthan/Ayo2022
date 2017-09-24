package org.ayo.robot.paint.maskfilter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;

import org.ayo.robot.BaseView;

/**
 * Created by Administrator on 2016/12/19.
 */

public class BlurMaskFilterView3 extends BaseView {
    public BlurMaskFilterView3(Context context) {
        super(context);
        init();
    }

    public BlurMaskFilterView3(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BlurMaskFilterView3(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BlurMaskFilterView3(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private static final int H_COUNT = 2, V_COUNT = 4;// 水平和垂直切割数
    private Paint mPaint;// 画笔
    private PointF[] mPointFs;// 存储各个巧克力坐上坐标的点

    private int width, height;// 单个巧克力宽高
    private float coorY;// 单个巧克力坐上Y轴坐标值

    private void init(){
        // 不使用硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null);

        // 初始化画笔
        mPaint = new Paint();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(0xFF603811);

        // 设置画笔遮罩滤镜
        mPaint.setMaskFilter(new EmbossMaskFilter(new float[] { 1, 1, 1F }, 0.1F, 10F, 20F));

        // 计算参数
        int[] screenSize = new int[]{400, 400};

        width = screenSize[0] / H_COUNT;
        height = screenSize[1] / V_COUNT;

        int count = V_COUNT * H_COUNT;

        mPointFs = new PointF[count];
        for (int i = 0; i < count; i++) {
            if (i % 2 == 0) {
                coorY = i * height / 2F;
                mPointFs[i] = new PointF(0, coorY);
            } else {
                mPointFs[i] = new PointF(width, coorY);
            }
        }
    }



    @Override
    protected void drawShape(Canvas canvas, int w, int h, Paint paint) {
        canvas.drawColor(Color.GRAY);

        // 画矩形
        for (int i = 0; i < V_COUNT * H_COUNT; i++) {
            canvas.drawRect(mPointFs[i].x, mPointFs[i].y, mPointFs[i].x + width, mPointFs[i].y + height, mPaint);
        }
    }

    @Override
    public String getTitle() {
        return "mPaint.setMaskFilter(EmbossMaskFilter)";
    }

    @Override
    public String getMethod() {
        return "构造函数有点复杂";
    }

    @Override
    public String getComment() {
        return "也需要关闭硬件加速\n" +
                "这个类用的比较少，碰见需求，直接跟美工要图";
    }
}
