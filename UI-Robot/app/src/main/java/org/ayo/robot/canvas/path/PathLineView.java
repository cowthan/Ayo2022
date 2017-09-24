package org.ayo.robot.canvas.path;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;

import org.ayo.robot.BaseView;
import org.ayo.sample.menu.notify.ToasterDebug;

public class PathLineView extends BaseView {
    public PathLineView(Context context) {
        super(context);
        init();
    }

    public PathLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PathLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PathLineView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        path = new Path();
        path.moveTo(90, 330);
        path.lineTo(150, 330);
        path.lineTo(120, 270);
        path.close();
    }

    Path path;


    @Override
    protected void drawShape(Canvas canvas, int w, int h, Paint paint) {
        //paint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(path, paint);
    }

    @Override
    public String getTitle() {
        return "canvas.drawPath(path, paint)";
    }

    @Override
    public String getMethod() {
        return "路径可以是任意图形，任意线条，连续或者不连续都行";
    }

    @Override
    public String getComment() {
        return "drawPath(Path, Paint)\n" +
                "Path path = new Path();\n" +
                "path.moveTo(90, 330);\n" +
                "path.lineTo(150, 330);\n" +
                "path.lineTo(120, 270);\n" +
                "path.close();\n" +
                "close的Path是个闭合的图形\n" +
                "不受style的填充和线框影响\n" +
                "rLineTo(dx, dy)：用相对距离画线\n" +
                "path连接两点的方法还挺多，各种曲线，可以好好研究一下\n" +
                "但是注意：本质上Path就是连续的直线";
    }

    public void onMove(int dx, int dy){
        path.rLineTo(dx, dy);
        invalidate();
    }

    public void onStart(int x, int y){
        startX = x;
        startY = y;
        path.reset();
        path.moveTo(x, y);
        invalidate();
    }

    int startX = 0;
    int startY = 0;
    private boolean needClose = true;

    public void onStop(int x, int y){
        if(needClose) path.close();
        invalidate();
    }

    public void changeCloseStatus(){
        needClose = !needClose;
        ToasterDebug.toastShort(needClose ? "开启force close" : "关闭force close");

    }

}