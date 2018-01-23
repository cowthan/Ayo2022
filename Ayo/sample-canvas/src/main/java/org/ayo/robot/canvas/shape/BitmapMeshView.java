package org.ayo.robot.canvas.shape;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;

import org.ayo.core.Bitmaps;
import org.ayo.robot.BaseView;
import org.ayo.robot.R;

public class BitmapMeshView extends BaseView {
    public BitmapMeshView(Context context) {
        super(context);
        init();
    }
    public BitmapMeshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BitmapMeshView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BitmapMeshView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }
    private void init(){
        setFocusable(true);

        // 实例画笔并设置颜色
        origPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        origPaint.setColor(0x660000FF);
        movePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        movePaint.setColor(0x99FF0000);
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(0xFFFFFB00);

        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test5);
        bitmapSize = Bitmaps.getImageSize(getContext(), R.drawable.test5);

        // 初始化坐标数组
        int index = 0;
        for (int y = 0; y <= HEIGHT; y++) {
            float fy = mBitmap.getHeight() * y / HEIGHT;

            for (int x = 0; x <= WIDTH; x++) {
                float fx = mBitmap.getWidth() * x / WIDTH;
                setXY(matrixMoved, index, fx, fy);
                setXY(matrixOriganal, index, fx, fy);
                index += 1;
            }
        }


    }
    int[] bitmapSize;

    @Override
    protected void drawShape(Canvas canvas, int w, int h, Paint paint) {

       if(matrixMoved != null && matrixMoved.length > 0){
           // 绘制网格位图
           canvas.drawBitmapMesh(mBitmap, WIDTH, HEIGHT, matrixMoved, 0, null, 0, null);

           // 绘制参考元素
           drawGuide(canvas);
       }

    }

    /**
     * 绘制参考元素
     *
     * @param canvas
     *            画布
     */
    private void drawGuide(Canvas canvas) {
        for (int i = 0; i < COUNT * 2; i += 2) {
            float x = matrixOriganal[i + 0];
            float y = matrixOriganal[i + 1];
            canvas.drawCircle(x, y, 4, origPaint);

            float x1 = matrixOriganal[i + 0];
            float y1 = matrixOriganal[i + 1];
            float x2 = matrixMoved[i + 0];
            float y2 = matrixMoved[i + 1];
            canvas.drawLine(x1, y1, x2, y2, origPaint);
        }

        for (int i = 0; i < COUNT * 2; i += 2) {
            float x = matrixMoved[i + 0];
            float y = matrixMoved[i + 1];
            canvas.drawCircle(x, y, 4, movePaint);
        }

        canvas.drawCircle(clickX, clickY, 6, linePaint);
    }

    /**
     * 计算变换数组坐标
     */
    private void smudge() {
        for (int i = 0; i < COUNT * 2; i += 2) {

            float xOriginal = matrixOriganal[i + 0];
            float yOriginal = matrixOriganal[i + 1];

            float dist_click_to_origin_x = clickX - xOriginal;
            float dist_click_to_origin_y = clickY - yOriginal;

            float kv_kat = dist_click_to_origin_x * dist_click_to_origin_x + dist_click_to_origin_y * dist_click_to_origin_y;

            float pull = (float) (1000000 / kv_kat / Math.sqrt(kv_kat));

            if (pull >= 1) {
                matrixMoved[i + 0] = clickX;
                matrixMoved[i + 1] = clickY;
            } else {
                matrixMoved[i + 0] = xOriginal + dist_click_to_origin_x * pull;
                matrixMoved[i + 1] = yOriginal + dist_click_to_origin_y * pull;
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        clickX = event.getX();
        clickY = event.getY();
        smudge();
        invalidate();
        return true;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public String getMethod() {
        return null;
    }

    @Override
    public String getComment() {
        return null;
    }

    //private float[] verts;// 交点的坐标数组
    private Bitmap mBitmap;// 位图资源

    private static final int WIDTH = 9;// 横向分割成的网格数量
    private static final int HEIGHT = 9;// 纵向分割成的网格数量
    private static final int COUNT = (WIDTH + 1) * (HEIGHT + 1);// 横纵向网格交织产生的点数量

    private static void setXY(float[] array, int index, float x, float y) {
        array[index * 2 + 0] = x;
        array[index * 2 + 1] = y;
    }


    public interface VertsProvider{
        void calculatVerts(int bitmapWidth, int bitmapHeight, int horizontalGridCount, int verticaGridCount, float[] verts);
        String getName();
    }

    private VertsProvider vertProvider;
    public void setVertsPrivider(VertsProvider privider){
        this.vertProvider = privider;
        vertProvider.calculatVerts(bitmapSize[0], bitmapSize[1], WIDTH, HEIGHT, matrixMoved);
        invalidate();
    }


    //=======================
    private float[] matrixOriganal = new float[COUNT * 2];// 基准点坐标数组
    private float[] matrixMoved = new float[COUNT * 2];// 变换后点坐标数组

    private float clickX, clickY;// 触摸屏幕时手指的xy坐标

    private Paint origPaint, movePaint, linePaint;// 基准点、变换点和线段的绘制Paint
}