
package org.ayo.robot.canvas.shape;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import org.ayo.robot.BaseView;

import java.util.Random;

public class PointView extends BaseView {
    public PointView(Context context) {
        super(context);
        init();
    }

    Random r = new Random();
    private void init(){
    }

    private int countOfPoint = 1000;
    public void addPoint(){
        countOfPoint *= 10;
        if(countOfPoint >= 1000000){
            countOfPoint = 1000;
        }
        invalidate();
    }

    int drawCount = 0;
    int countInCircle = 0;

    @Override
    protected void drawShape(Canvas canvas, int w, int h, Paint paint) {

        double radius = Math.min(w, h) / 2;
        int centerX = w/2;
        int centerY = h/2;
        //随机放出1000个点，离圆心距离小于radius的就在圆内，否则就在圆外，如果有900个在圆内，概率上来说，圆的面积就是w和h这个矩形的9/10


        ///一次画100个点
        for(int i = 0; i < 100; i++){
            int x = r.nextInt(w);
            int y = r.nextInt(h);
            drawCount++;
            canvas.drawPoint(x, y, paint);

            ///判断是否落在圆内（到圆心的距离小于半径，则在圆内）
            if(Math.sqrt(Math.pow(x-centerX, 2) + Math.pow(y-centerY, 2)) < radius){
                countInCircle++;
            }
        }

        if(drawCount < countOfPoint){
            invalidate();
        }else{
            int mianjiForRect = w * h;
            double mainjiForCircle = mianjiForRect * ((double)countInCircle/countOfPoint);
            double pai = mainjiForCircle/(radius*radius);

            getObservable().notifyDataChanged(this, "根据概率算出来的π=" + pai + "(采样点：" + countOfPoint +"个)");

            drawCount = 0;
            countInCircle = 0;
        }


        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(w/2, h/2, (int)radius, paint);

    }

    @Override
    public String getTitle() {
        return "canvas.drawPoint(x, y, paint)";
    }

    @Override
    public String getMethod() {
        return "注意参数";
    }

    @Override
    public String getComment() {
        return "drawPoint(x, y, paint)：画个点，点大小基于stroke width\n" +
                "drawPoints(float[] pts, int offest, int count, paint)：画很多点\n" +
                "pts:[x0 y0 x1 y1 x2 y2 ...]\n" +
                "offest: Number of values in the array to skip before drawing\n" +
                "count: 画几个点，skip过offset个值之后，处理count个点，注意，是count*2个值\n" +
                "\n" +
                "例子中，指定N个点，根据概率计算圆周率（为了界面流畅，不会在一帧内画超过100个点）";
    }

}