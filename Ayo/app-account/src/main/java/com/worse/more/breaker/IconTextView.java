package com.worse.more.breaker;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 一个Icon，一个TextView， 可以上下，可以左右
 * Created by Administrator on 2016/3/21.
 */
public class IconTextView extends LinearLayout{

    public static final int MODE_ICON_LEFT = 1;
    public static final int MODE_ICON_RIGHT = 2;
    public static final int MODE_ICON_TOP = 3;
    public static final int MODE_ICON_BOTTOM = 4;

    private int mode = MODE_ICON_LEFT;

    /**
     * icon和text的间距
     */
    private int space = 10;

    public IconTextView(Context context) {
        super(context);
        init(context);
    }

    public IconTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public IconTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public IconTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
    }

    public void setMode(int mode, int space){

        LayoutParams lpIv = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        );

        LayoutParams lpTv = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        );

        if(mode == MODE_ICON_LEFT || mode == MODE_ICON_RIGHT){
            this.setOrientation(HORIZONTAL);
            //lp.height = LinearLayout.LayoutParams.MATCH_PARENT;
            this.setGravity(Gravity.CENTER);
        }else{
            this.setOrientation(VERTICAL);
            //lp.width = LinearLayout.LayoutParams.MATCH_PARENT;
            this.setGravity(Gravity.CENTER);
        }


        tv = getTextView();
        iv = getImageView();

        if(mode == MODE_ICON_LEFT || mode == MODE_ICON_TOP){
            this.addView(iv, lpIv);
            this.addView(tv, lpTv);
        }else{
            this.addView(tv, lpTv);
            this.addView(iv, lpIv);
        }


        //间距
        if(mode == MODE_ICON_LEFT){
            lpIv.rightMargin = space;
        }else if(mode == MODE_ICON_RIGHT){
            lpIv.leftMargin = space;
        }else if(mode == MODE_ICON_TOP){
            lpIv.bottomMargin = space;
        }else if(mode == MODE_ICON_BOTTOM){
            lpIv.topMargin = space;
        }
        iv.setLayoutParams(lpIv);
    }

    private TextView getTextView(){
        TextView tv = new TextView(getContext());
        tv.setSingleLine();
        return tv;
    }

    private ImageView getImageView(){
        ImageView iv = new ImageView(getContext());

        return iv;
    }

    private ImageView iv;
    private TextView tv;

    public void setIcon(int resId){
        iv.setImageResource(resId);
    }

    public void setImageSize(int w, int h){
        MarginLayoutParams lp = (MarginLayoutParams) iv.getLayoutParams();
        lp.width = w;
        lp.height = h;
        iv.setLayoutParams(lp);
    }

    public void setText(String text){
        tv.setText(text);
    }

    public void setTextColor(int color){
        tv.setTextColor(color);
    }

    public void setTextSize(float size){
        tv.setTextSize(size);
    }

}
