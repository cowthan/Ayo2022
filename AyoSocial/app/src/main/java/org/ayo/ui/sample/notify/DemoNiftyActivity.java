package org.ayo.ui.sample.notify;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import org.ayo.notify.nifity.Effects;
import org.ayo.notify.nifity.NiftyNotificationView;
import org.ayo.sample.R;
import org.ayo.ui.sample.BasePage;

public class DemoNiftyActivity extends BasePage implements View.OnClickListener{
    private Effects effect;


    public void onClick(View v){

        String msg="Today we’d like to share a couple of simple styles and effects for android notifications.";

        switch (v.getId()){
            case R.id.scale:effect=Effects.scale;break;
            case R.id.thumbSlider:effect=Effects.thumbSlider;break;
            case R.id.jelly:effect=Effects.jelly;break;
            case R.id.slidein:effect=Effects.slideIn;break;
            case R.id.flip:effect=Effects.flip;break;
            case R.id.slideOnTop:effect=Effects.slideOnTop;break;
            case R.id.standard:effect=Effects.standard;break;
        }




        NiftyNotificationView.build(getActivity(), msg, effect,R.id.mLyout)
                .setIcon(R.drawable.lion)         //You must call this method if you use ThumbSlider effect
                .show();



//        You can configure like this
//        The default

//        Configuration cfg=new Configuration.Builder()
//                .setAnimDuration(700)
//                .setDispalyDuration(1500)
//                .setBackgroundColor("#FFBDC3C7")
//                .setTextColor("#FF444444")
//                .setIconBackgroundColor("#FFFFFFFF")
//                .setTextPadding(5)                      //dp
//                .setViewHeight(48)                      //dp
//                .setTextLines(2)                        //You had better use setViewHeight and setTextLines together
//                .setTextGravity(Gravity.CENTER)         //only text def  Gravity.CENTER,contain icon Gravity.CENTER_VERTICAL
//                .build();
//
//        NiftyNotificationView.build(this,msg, effect,R.id.mLyout,cfg)
//                .setIcon(R.drawable.lion)               //remove this line ,only text
//                .setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        //add your code
//                    }
//                })
//                .show();                               //  show(boolean) allow duplicates   or showSticky() sticky notification,you can call removeSticky() method close it
    }

    @Override
    protected int getLayoutId() {
        return R.layout.ac_demo_nifty;
    }

    @Override
    protected void onCreate2(View view, @Nullable Bundle bundle) {
        View scale = findViewById(R.id.scale);
        View thumbSlider = findViewById(R.id.thumbSlider);
        View jelly = findViewById(R.id.jelly);
        View slidein = findViewById(R.id.slidein);
        View flip = findViewById(R.id.flip);
        View slideOnTop = findViewById(R.id.slideOnTop);
        View standard = findViewById(R.id.standard);

        scale.setOnClickListener(this);
        thumbSlider.setOnClickListener(this);
        jelly.setOnClickListener(this);
        slidein.setOnClickListener(this);
        flip.setOnClickListener(this);
        slideOnTop.setOnClickListener(this);
        standard.setOnClickListener(this);
    }

    @Override
    protected void onDestroy2() {

    }

    @Override
    protected void onPageVisibleChanged(boolean b, boolean b1, @Nullable Bundle bundle) {

    }
}
