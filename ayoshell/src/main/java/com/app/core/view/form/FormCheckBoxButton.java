package com.app.core.view.form;


import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.core.R;

import org.ayo.core.Lang;

/**
 * Created by qiaoliang on 2017/8/4.
 */

public class FormCheckBoxButton extends LinearLayout {
    public FormCheckBoxButton(Context context) {
        super(context);
        init(context, null);
    }

    public FormCheckBoxButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public FormCheckBoxButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FormCheckBoxButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private CheckBox cb;
    private TextView tv_title;

    public int getLayoutId(){
        return R.layout.ui_form_checkbox_button;
    }

    private void init(Context context, AttributeSet attrs){
        View v = View.inflate(context, getLayoutId(), null);
        MarginLayoutParams lp = new MarginLayoutParams(MarginLayoutParams.MATCH_PARENT, MarginLayoutParams.MATCH_PARENT);
        addView(v, lp);
        tv_title = (TextView) findViewById(R.id.tv_title);
        cb = (CheckBox) findViewById(R.id.cb);

        this.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View view) {
                cb.setChecked(!cb.isChecked());
            }
        });
    }

    public void setTitle(String label){
        tv_title.setText(Lang.snull(label));
    }


    public void setChecked(boolean checked){
        cb.setChecked(checked);
    }

    public void setOnCheckedChangedCallback(final OnCheckChangedCallback callback){
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                callback.onCheckChanged(0, "", b);
            }
        });
    }

    public CheckBox getCheckbox(){
        return cb;
    }

    public boolean isChecked(){
        return cb.isChecked();
    }

}
