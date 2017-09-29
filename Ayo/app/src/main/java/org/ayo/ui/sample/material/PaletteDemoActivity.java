package org.ayo.ui.sample.material;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import org.ayo.log.Trace;
import org.ayo.sample.R;
import org.ayo.ui.sample.BaseActivity;

/**
 * Created by qiaoliang on 2017/9/25.
 *
 * 调色板，5.0引入，可以从Bitmap获取色调，控制界面其他部分，实现整体风格统一
 */

public class PaletteDemoActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.ac_demo_pallete;
    }

    @Override
    protected void onCreate2(View view, @Nullable Bundle bundle) {
        final ImageView iv = (ImageView) findViewById(R.id.iv);
        final ImageView iv2 = (ImageView) findViewById(R.id.iv2);
        final ImageView iv3 = (ImageView) findViewById(R.id.iv3);
        final ImageView iv4 = (ImageView) findViewById(R.id.iv4);
        final ImageView iv5 = (ImageView) findViewById(R.id.iv5);
        final Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.badge_orange);
        iv.setImageBitmap(bm);

        final Bitmap bm2 = BitmapFactory.decodeResource(getResources(), R.drawable.badge_blue);
        iv2.setImageBitmap(bm2);

        final Bitmap bm3 = BitmapFactory.decodeResource(getResources(), R.drawable.pic3);
        iv3.setImageBitmap(bm3);

        final Bitmap bm4 = BitmapFactory.decodeResource(getResources(), R.raw.test1);
        iv4.setImageBitmap(bm4);

        final Bitmap bm5 = BitmapFactory.decodeResource(getResources(), R.drawable.a3);
        iv5.setImageBitmap(bm5);

        iv.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                extractPalette(bm);
            }
        });
        iv2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                extractPalette(bm2);
            }
        });
        iv3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                extractPalette(bm3);
            }
        });
        iv4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                extractPalette(bm4);
            }
        });
        iv5.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                extractPalette(bm5);
            }
        });

    }

    private void extractPalette(Bitmap bm){
        Palette.from(bm).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                //获取色调
                Palette.Swatch vibrant = palette.getDarkVibrantSwatch();

                //将色调设置给其他组件
                if(vibrant != null){
                    Window window = getWindow();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        window.setStatusBarColor(vibrant.getRgb());
                    }
                }else{
                    ///bitmap的颜色整体偏亮时，getDarkVibrantSwatch这个就提取不到
                    Trace.e("palette", "提取出null了");
                }

                //打印一下信息
                Palette.Swatch vibrant1 = palette.getVibrantSwatch();
                Palette.Swatch vibrant2 = palette.getDarkVibrantSwatch();
                Palette.Swatch vibrant3 = palette.getLightVibrantSwatch();
                Palette.Swatch vibrant4 = palette.getMutedSwatch();
                Palette.Swatch vibrant5 = palette.getDarkMutedSwatch();
                Palette.Swatch vibrant6 = palette.getLightMutedSwatch();

                TextView tv1 = (TextView) findViewById(R.id.tv1);
                TextView tv2 = (TextView) findViewById(R.id.tv2);
                TextView tv3 = (TextView) findViewById(R.id.tv3);
                TextView tv4 = (TextView) findViewById(R.id.tv4);
                TextView tv5 = (TextView) findViewById(R.id.tv5);
                TextView tv6 = (TextView) findViewById(R.id.tv6);

                tv1.setText(vibrant1 == null ? "getVibrantSwatch--null" : "getVibrantSwatch");
                tv2.setText(vibrant2 == null ? "getDarkVibrantSwatch--null" : "getDarkVibrantSwatch--设成状态栏颜色");
                tv3.setText(vibrant3 == null ? "getLightVibrantSwatch--null" : "getLightVibrantSwatch");
                tv4.setText(vibrant4 == null ? "getMutedSwatch--null" : "getMutedSwatch");
                tv5.setText(vibrant5 == null ? "getDarkMutedSwatch--null" : "getDarkMutedSwatch");
                tv6.setText(vibrant6 == null ? "getLightMutedSwatch--null" : "getLightMutedSwatch");

                tv1.setTextColor(vibrant1 != null ? vibrant1.getRgb() : Color.BLACK);
                tv2.setTextColor(vibrant2 != null ? vibrant2.getRgb() : Color.BLACK);
                tv3.setTextColor(vibrant3 != null ? vibrant3.getRgb() : Color.BLACK);
                tv4.setTextColor(vibrant4 != null ? vibrant4.getRgb() : Color.BLACK);
                tv5.setTextColor(vibrant5 != null ? vibrant5.getRgb() : Color.BLACK);
                tv6.setTextColor(vibrant6 != null ? vibrant6.getRgb() : Color.BLACK);
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
