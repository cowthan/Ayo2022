package org.ayo.robot.canvas.shape;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import org.ayo.robot.BaseView;

public class ColorView extends BaseView {
    public ColorView(Context context) {
        super(context);
        init();
    }

    private void init(){
        getPaint().setColor(Color.YELLOW);
    }

    @Override
    protected void drawShape(Canvas canvas, int w, int h, Paint paint) {
        canvas.drawColor(paint.getColor());
    }

    @Override
    public String getTitle() {
        return "canvas.drawColor(Color.YELLOW)";
    }

    @Override
    public String getMethod() {
        return "Color构造";
    }

    @Override
    public String getComment() {
        return "给View画个背景，颜色就是Color\n" +
                "构造Color的int：注意，所有需要Color的地方，要的都是int\n" +
                "内置变量：Color.BLACK\n" +
                "Color.argb(int a, int r, int g, int b)：[0, 255]\n" +
                "Color.rgb(int red, int green, int blue)\n" +
                "Color.parseColor(\"#ffffff\")\n" +
                "也可以直接在需要int color的地方传入0xFFFFFF\n" +
                "Color.HSVToColor(@Size(3) float[] hsv)：HSV颜色是另一种颜色表示法";
    }

}