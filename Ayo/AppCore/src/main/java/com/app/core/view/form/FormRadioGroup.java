package com.app.core.view.form;


import com.app.core.R;

import org.ayo.core.Lang;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by qiaoliang on 2017/8/4.
 */

public class FormRadioGroup extends LinearLayout {
    public FormRadioGroup(Context context) {
        super(context);
        init(context, null);
    }

    public FormRadioGroup(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, null);
    }

    public FormRadioGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, null);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FormRadioGroup(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, null);
    }

    public int getLayoutId() {
        return R.layout.ui_form_radio_group;
    }

    private RadioGroup radio_group;
    private TextView tv_title;

    private void init(Context context, AttributeSet attrs){
        View v = View.inflate(context, getLayoutId(), null);
        MarginLayoutParams lp = new MarginLayoutParams(MarginLayoutParams.MATCH_PARENT, MarginLayoutParams.MATCH_PARENT);
        addView(v, lp);
        tv_title = (TextView) findViewById(R.id.tv_title);
        radio_group = (RadioGroup) findViewById(R.id.radio_group);


    }

    public void setTitle(String label){
        tv_title.setText(Lang.snull(label));
    }


    public void setItems(List<String> items, int selectedIndex){
        radio_group.removeAllViews();
        if(items == null || items.size() == 0) return;
        for(int i = 0; i < items.size(); i++){
            RadioButton rb = new RadioButton(getContext());
            rb.setText(items.get(i));
            rb.setId(i);
            if(i == selectedIndex){
                rb.setChecked(true);
            }else{
                rb.setChecked(false);
            }
            radio_group.addView(rb);
        }
    }

    public void setOnCheckedChangedCallback(final OnCheckChangedCallback callback){
        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                callback.onCheckChanged(i, "", true);
            }
        });
    }
}
