package org.ayo.robot.canvas.shape;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import org.ayo.robot.AyoActivityAttacher;
import org.ayo.robot.R;

/**
 * Created by Administrator on 2016/12/15.
 */

public class DemoDrawBitmapMesh extends AyoActivityAttacher {


    final BitmapMeshView.VertsProvider[] providers = {
            new BitmapMeshVertsProviders.SkepProvider(),
            new BitmapMeshVertsProviders.ReadingGlassesProvider(),

    };

    int currentPostion = -1;

    @Override
    protected int getLayoutId() {
        return R.layout.demo_draw_bitmap_mesh;
    }

    @Override
    protected void onCreate2(View view, @Nullable Bundle bundle) {
        final BitmapMeshView shapeView = id(R.id.shapeView);
        final View root = id(R.id.body);



        final Button btn_change = id(R.id.btn_change);
        btn_change.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                currentPostion++;
                if(currentPostion >= providers.length){
                    currentPostion = 0;
                }
                BitmapMeshView.VertsProvider provider = providers[currentPostion];
                btn_change.setText("切换（当前：" + provider.getName() + ")");

                shapeView.setVertsPrivider(provider);
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

