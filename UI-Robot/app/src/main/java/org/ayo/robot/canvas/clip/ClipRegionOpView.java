package org.ayo.robot.canvas.clip;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Region;

import org.ayo.robot.BaseView;

import java.util.EnumMap;

public class ClipRegionOpView extends BaseView {
    public ClipRegionOpView(Context context) {
        super(context);
        init();
    }

    private Rect mRegionA, mRegionB;// 区域A和区域B对象
    Path path; //区域B也可以由用户自己绘制path决定

    private void init(){
        // 实例化区域A和区域B
        mRegionA = new Rect(100, 100, 200, 200);
        mRegionB = new Rect(150, 150, 300, 300);
        path = new Path();
    }

    Region.Op[] ops = {
            Region.Op.DIFFERENCE,
            Region.Op.REVERSE_DIFFERENCE,
            Region.Op.INTERSECT,
            Region.Op.REPLACE,
            Region.Op.UNION,
            Region.Op.XOR,
    };
    static EnumMap<Region.Op, String> map = new EnumMap<Region.Op, String>(Region.Op.class);
    static{
        map.put(Region.Op.DIFFERENCE, "保留前区域中的和后区域不重合的地方");
        map.put(Region.Op.REVERSE_DIFFERENCE, "保留后区域中的和前区域不重合的地方");
        map.put(Region.Op.INTERSECT, "保留前后区域重合的地方");
        map.put(Region.Op.REPLACE, "只保留后区域");
        map.put(Region.Op.UNION, "保留前后区域");
        map.put(Region.Op.XOR, "对于UNION，抠出INTERSECT，即去掉前后重合的地方");
    }
    int currentPositon = 0;

    public void changeOp(){
        currentPositon++;
        if(currentPositon == ops.length){
            currentPositon = 0;
        }
        invalidate();
    }

    @Override
    protected void drawShape(Canvas canvas, int w, int h, Paint paint) {
        // 填充颜色
        canvas.save();

        // 裁剪区域A
        canvas.clipRect(mRegionA);

        // 再通过组合方式裁剪区域B
        if(clipUsePath){
            canvas.clipPath(path, ops[currentPositon]);
        }else{
            canvas.clipRect(mRegionB, ops[currentPositon]);
        }
        getObservable().notifyDataChanged(this, ops[currentPositon].name() + ": " + map.get(ops[currentPositon]));

        // 填充颜色
        canvas.drawColor(Color.RED);

        canvas.restore();

        // 绘制框框帮助我们观察
        canvas.drawRect(mRegionA, paint);
        if(clipUsePath) canvas.drawPath(path, paint);
        else canvas.drawRect(mRegionB, paint);
    }

    private boolean clipUsePath = false;

    @Override
    public String getTitle() {
        return "canvas.clipRect(Rect或Path, Region.Op op)";
    }

    @Override
    public String getMethod() {
        return "主要看参数2：如何叠加两个剪切区域";
    }

    @Override
    public String getComment() {
        return "与现有裁剪区域进行组合，Region.Op就是组合方式\n" +
                "DIFFENCE, INTERSECT, REPLACE, REVERSE_DEFFERENCE, UNION, XOR";
    }


    public void onMove(int dx, int dy){
        path.rLineTo(dx, dy);
        invalidate();
    }

    public void onStart(int x, int y){
        path.reset();
        path.moveTo(x, y);
        invalidate();
    }


    public void onStop(int x, int y){
        clipUsePath = true;
        path.close();
        invalidate();
    }
}