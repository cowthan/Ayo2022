package org.ayo.robot.canvas.shape;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.ayo.robot.AyoActivityAttacher;
import org.ayo.robot.R;

/**
 * Created by Administrator on 2016/12/15.
 */

public class DemoDrawPicture extends AyoActivityAttacher {

    @Override
    protected int getLayoutId() {
        return R.layout.demo_draw_picture;
    }

    @Override
    protected void onCreate2(View view, @Nullable Bundle bundle) {
        final PictureView shapeView = id(R.id.shapeView);
//        final View root = id(R.id.body);
//        DemoShapeMgmr.attach(getActivity(), root, shapeView);

        TextView tv_method = id(R.id.tv_method);
        tv_method.setText("canvas.drawPicture(Picture picture)");

        TextView tv_comment = id(R.id.tv_comment);
        tv_comment.setText("绘制Picture\n" +
                "Picture对象提供一个Canvas，可以在上面绘图，但不会直接显示\n" +
                "Canvas beginRecording(int width, int height)之后可以绘制，\n" +
                "绘制过程可以在子线程中\n" +
                "endRecording(); //end之后Cavans对象不可以再修改\n" +
                "显示Picture的View必须：\n" +
                "setLayerType(LAYER_TYPE_SOFTWARE,null);\n" +
                "API23之后好像移除了这个限制");

        final ImageView iv = id(R.id.iv);

    }

    @Override
    protected void onDestroy2() {

    }

    @Override
    protected void onPageVisibleChanged(boolean b, boolean b1, @Nullable Bundle bundle) {

    }
}

