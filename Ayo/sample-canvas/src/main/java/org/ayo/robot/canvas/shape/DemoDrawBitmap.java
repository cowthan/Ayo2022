package org.ayo.robot.canvas.shape;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.ayo.robot.AyoActivityAttacher;
import org.ayo.robot.BaseView;
import org.ayo.robot.Observable;
import org.ayo.robot.Observer;
import org.ayo.robot.R;


/**
 * Created by Administrator on 2016/12/15.
 */

public class DemoDrawBitmap extends AyoActivityAttacher {

    @Override
    protected int getLayoutId() {
        return R.layout.demo_draw_bitmap;
    }

    @Override
    protected void onCreate2(View view, @Nullable Bundle bundle) {

        final  BitmapView shapeView = id(R.id.shapeView);
//        final View root = id(R.id.body);
//        DemoShapeMgmr.attach(getActivity(), root, shapeView);

        shapeView.setOnFingerTouchCallback(new BaseView.OnFingerTouchCallback() {
            @Override
            public void onFingerMove(int x, int y, int dx, int dy) {

            }

            @Override
            public void onFingerDown(int x, int y) {

            }

            @Override
            public void onFingerUp(int x, int y) {

            }

            @Override
            public void onClick(int x, int y) {
                shapeView.changeBitmap();
            }
        });

        TextView tv_method = id(R.id.tv_method);
        tv_method.setText("canvas.drawBitmap(mBitmap, src, dest, paint);");

        TextView tv_comment = id(R.id.tv_comment);
        tv_comment.setText("绘制位图\n" +
                "总是会尝试把ong的src区域全部绘制到View的dest区域，会拉伸变形\n" +
                "对于jpg图片的处理，没弄明白，demo里只显示了部分\n" +
                "高级处理：drawBitmap(Bitmap bitmap, Matrix matrix, Paint paint)\n" +
                "依靠matrix进行变换，以让bitmap以各种形式适应控件尺寸" +
                "ImageView是依靠canvas变换和Drawable");

        final ImageView iv = id(R.id.iv);


        final TextView tv_radius = id(R.id.tv_radius);
        shapeView.getObservable().addObserver(new Observer() {
            @Override
            public void update(Observable observable, Object src, Object data) {
                tv_radius.setText(data.toString() + "   点击中图");
                if(src instanceof BitmapView){
                    BitmapView bitmapView = (BitmapView) src;
                    iv.setImageResource(bitmapView.getCurrentShowingResourceId());
                }
            }
        });
    }

    @Override
    protected void onDestroy2() {

    }

    @Override
    protected void onPageVisibleChanged(boolean b, boolean b1, @Nullable Bundle bundle) {

    }
}

