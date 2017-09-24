package org.ayo.ui.sample;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.GridLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/23 0023.
 */

public class SmartRadioGroup extends GridLayout {

    public SmartRadioGroup(Context context) {
        super(context);
    }

    public SmartRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SmartRadioGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SmartRadioGroup(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(){

    }

    List<RadioButton> radios = new ArrayList<>();
    RadioButton currentChecked;

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        for(int i = 0; i < getChildCount(); i++){
            View v = getChildAt(i);
            if(v instanceof RadioButton){
                final RadioButton r = (RadioButton) v;
                radios.add(r);

                if(r.isChecked()){
                    currentChecked = r;
                }

                r.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                            if(currentChecked != null){
                                currentChecked.setChecked(false);
                                currentChecked = r;
                            }
                            onCheckedChangeListener.onCheckedChanged(null, r.getId());
                        }
                    }
                });

            }else{

            }
        }


    }

    private RadioButton getById(int checkId){
        return (RadioButton) findViewById(checkId);
    }

    RadioGroup.OnCheckedChangeListener onCheckedChangeListener;

    public void setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener onCheckedChangeListener){
        this.onCheckedChangeListener = onCheckedChangeListener;
    }

    public void setCheck(int checkId){
    }
}
