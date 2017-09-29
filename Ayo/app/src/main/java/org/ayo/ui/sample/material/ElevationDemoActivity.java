package org.ayo.ui.sample.material;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;

import org.ayo.sample.R;
import org.ayo.ui.sample.BaseActivity;

/**
 * Created by qiaoliang on 2017/9/25.
 */

public class ElevationDemoActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.ac_demo_elevation;
    }

    boolean is_Z_setted = false;

    @Override
    protected void onCreate2(View view, @Nullable Bundle bundle) {
        final ImageView iv = (ImageView) findViewById(R.id.iv);
        final ImageView iv2 = (ImageView) findViewById(R.id.iv2);
        final ImageView iv3 = (ImageView) findViewById(R.id.iv3);
        final ImageView iv4 = (ImageView) findViewById(R.id.iv4);
        final Button iv5 = (Button) findViewById(R.id.iv5);

        final Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.badge_orange);
        iv.setImageBitmap(bm);

        final Bitmap bm2 = BitmapFactory.decodeResource(getResources(), R.drawable.badge_blue);
        iv2.setImageBitmap(bm2);

        final Bitmap bm3 = BitmapFactory.decodeResource(getResources(), R.drawable.pic3);
        iv3.setImageBitmap(bm3);

        final Bitmap bm4 = BitmapFactory.decodeResource(getResources(), R.raw.test1);
        iv4.setImageBitmap(bm4);

        iv5.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (!is_Z_setted) {
                        iv4.animate().translationZ(100);
                        iv5.animate().translationZ(100);
                        is_Z_setted = true;
                    } else {
                        iv4.animate().translationZ(0);
                        iv5.animate().translationZ(0);
                        is_Z_setted = false;
                    }
                }
            }
        });

        SeekBar seekBar = (SeekBar) findViewById(R.id.seek_bar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    iv5.setText("Elevation: " + progress);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        iv.setElevation(progress);
                        iv2.setElevation(progress);
                        iv3.setElevation(progress);
                        iv4.setElevation(progress);
                        iv5.setElevation(progress);

                        iv4.setTranslationZ(progress);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

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
