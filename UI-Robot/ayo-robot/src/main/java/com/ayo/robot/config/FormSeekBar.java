package com.ayo.robot.config;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.SeekBar;

/**
 */

public class FormSeekBar extends FormBase{

    public FormSeekBar(Context context) {
        super(context);
    }

    public FormSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FormSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FormSeekBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    AppCompatSeekBar seekBar;

    @Override
    protected View createFormView() {
        seekBar = new AppCompatSeekBar(getContext()); // (RadioGroup) View.inflate(getContext(), R.layout.layout_form_radio, null);
        return seekBar;
    }

    public void setSeekRange(int max, int progress){
        seekBar.setMax(max);
        seekBar.setProgress(progress);
    }


    public int getInput(){
        return seekBar.getProgress();
    }

    public SeekBar getInputView(){
        return seekBar;
    }
}
