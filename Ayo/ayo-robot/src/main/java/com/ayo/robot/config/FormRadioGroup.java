package com.ayo.robot.config;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.List;

/**
 */

public class FormRadioGroup extends FormBase{

    public FormRadioGroup(Context context) {
        super(context);
    }

    public FormRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FormRadioGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FormRadioGroup(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    RadioGroup radioGroup;

    @Override
    protected View createFormView() {
        radioGroup = new RadioGroup(getContext()); // (RadioGroup) View.inflate(getContext(), R.layout.layout_form_radio, null);
        radioGroup.setOrientation(RadioGroup.HORIZONTAL);
        radioGroup.setGravity(Gravity.CENTER);
        return radioGroup;
    }

    public void setRadioGroupOrientation(int orientation){
        radioGroup.setOrientation(orientation);
    }

    List<?> list = null;
    public void notifyDataSetChanged(List<?> list){
        this.list = list;
        radioGroup.removeAllViews();
        if(list == null || list.size() == 0) return;
        for(int i = 0; i < list.size(); i++){
            RadioButton btn = new RadioButton(getContext());
            btn.setTextColor(Color.parseColor("#00E5EE"));
            btn.setText(list.get(i).toString());
            btn.setId(i+1);
            radioGroup.addView(btn);
            if(i == 0){
                btn.setChecked(true);
            }else{
                btn.setChecked(false);
            }
            /*
           <RadioButton
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="FILL"
            android:id="@+id/rb1"
            android:layout_marginRight="10dp"
            android:textColor="#ffffff"
            android:checked="true"/>
             */
        }
    }

    public int getInput(){
        return radioGroup.getCheckedRadioButtonId() - 1;
    }

    public RadioGroup getInputView(){
        return radioGroup;
    }
}
