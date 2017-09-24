package org.ayo.robot.canvas.path;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ComposePathEffect;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.DiscretePathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PathEffect;
import android.graphics.SumPathEffect;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;

import org.ayo.robot.BaseView;
import org.ayo.sample.menu.notify.ToasterDebug;

import java.util.List;

public class PathEffectView extends BaseView {
    public PathEffectView(Context context) {
        super(context);
        init();
    }

    public PathEffectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PathEffectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PathEffectView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        path = new Path();
        // 定义路径的起点
        path.moveTo(0, 50);

        // 定义路径的各个点
        for (int i = 0; i <= 30; i++) {
            path.lineTo(i * 35, (float) (Math.random() * 300));
        }
    }

    Path path;
    private float mPhase;// 偏移值

    @Override
    protected void drawShape(Canvas canvas, int w, int h, Paint paint) {
        canvas.drawPath(path, paint);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mPhase += 1;
                postInvalidate();
            }
        }, 500);

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


    public void setPathEffect(List<String> pathEffects, boolean isCompose, boolean isSum){
        if(pathEffects == null || pathEffects.size() == 0){
            getPaint().setPathEffect(null);
            getObservable().notifyDataChanged(this, "PathEffect = null");
        }else{
            if(isCompose && isSum){
                ToasterDebug.toastShort("compose和sum这俩就能学一个");
            }else if(isCompose || isSum){
                if(pathEffects.size() != 2){
                    ToasterDebug.toastShort("compose或者sum要求必须有两个输入");
                }else{
                    PathEffect pe = getPathEffect(pathEffects.get(0));
                    PathEffect pe2 = getPathEffect(pathEffects.get(1));
                    if(isCompose){
                        ComposePathEffect composePathEffect = new ComposePathEffect(pe, pe2);
                        ToasterDebug.toastShort("按选择的先后顺序compose--先选是out，后选是in");
                        getPaint().setPathEffect(composePathEffect);
                        invalidate();
                        getObservable().notifyDataChanged(this, "ComposePathEffect({1}, {2}在)".replace("{1}", pathEffects.get(0)).replace("{2}", pathEffects.get(1)));
                    }else{
                        SumPathEffect composePathEffect = new SumPathEffect(pe, pe2);
                        ToasterDebug.toastShort("按选择的先后顺序sum--先选是first，后选是second");
                        getPaint().setPathEffect(composePathEffect);
                        invalidate();
                        getObservable().notifyDataChanged(this, "SumPathEffect({1}, {2})".replace("{1}", pathEffects.get(0)).replace("{2}", pathEffects.get(1)));
                    }
                    getPaint().setPathEffect(pe);
                    invalidate();
                }
            }else{
                if(pathEffects.size() == 1){
                    String name = pathEffects.get(pathEffects.size()-1);
                    ToasterDebug.toastShort("显示：" + name);
                    PathEffect pe = getPathEffect(name);
                    getPaint().setPathEffect(pe);
                    invalidate();
                    getObservable().notifyDataChanged(this, name);
                }else{
                    ToasterDebug.toastShort("非组合模式下，不要选多个");
                }
            }
        }
    }

    private PathEffect getPathEffect(String name){
        PathEffect[] mEffects = new PathEffect[7];
        mEffects[0] = null;
        mEffects[1] = new CornerPathEffect(10);
        mEffects[2] = new DiscretePathEffect(3.0F, 5.0F);
        mEffects[3] = new DashPathEffect(new float[] { 20, 10, 5, 10 }, mPhase);
        Path path = new Path();
        path.addRect(0, 0, 8, 8, Path.Direction.CCW);
        mEffects[4] = new PathDashPathEffect(path, 12, mPhase, PathDashPathEffect.Style.ROTATE);
        mEffects[5] = new ComposePathEffect(mEffects[2], mEffects[4]);
        mEffects[6] = new SumPathEffect(mEffects[4], mEffects[3]);

        if(name.equals("CornerPathEffect")){
            return mEffects[1];
        }else if(name.equals("DiscretePathEffect")){
            return mEffects[2];
        }else if(name.equals("DashPathEffect")){
            return mEffects[3];
        }else if(name.equals("PathDashPathEffect")){
            return mEffects[4];
        }else if(name.equals("SumPathEffect")){
            return mEffects[5];
        }else if(name.equals("ComposePathEffect")){
            return mEffects[6];
        }else{
            return mEffects[0];
        }
    }


}