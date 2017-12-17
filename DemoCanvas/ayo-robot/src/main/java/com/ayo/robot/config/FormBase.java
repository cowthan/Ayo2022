package com.ayo.robot.config;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/12/16.
 */

/**
 * 一个form表单，包含一个label和一个输入项
 * 输入项由子类决定， 可能是：Label，EditText，EditText（多行），RadioButton，CheckBox，SeekBar
 */
public abstract class FormBase extends LinearLayout {


    public FormBase(Context context) {
        super(context);
        init();
    }

    public FormBase(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FormBase(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FormBase(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }
    View label;
    private void init(){
        removeAllViews();
        setGravity(Gravity.CENTER);

        label = createLabel();

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        if(getOrientation() == HORIZONTAL){
            lp.width = LayoutParams.WRAP_CONTENT;
        }else{
            lp.height = LayoutParams.WRAP_CONTENT;
        }
        addView(label, lp);


        View formView = createFormView();
        lp = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        if(getOrientation() == HORIZONTAL){
            lp.width = LayoutParams.WRAP_CONTENT;
        }else{
            lp.height = LayoutParams.WRAP_CONTENT;
        }
        addView(formView, lp);
    }

    protected View createLabel(){
        TextView tv = new TextView(getContext());
        if(getOrientation() == HORIZONTAL){
            tv.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        }else{
            tv.setGravity(Gravity.CENTER);
        }
        tv.setTextColor(Color.parseColor("#caff70"));

        return tv;
    }

    protected abstract View createFormView();

    public void setLabel(String label){
        TextView tv = (TextView) this.label;
        tv.setText(label);
    }

}
