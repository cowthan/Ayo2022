package com.app.core.view.form;


import com.app.core.R;

import org.ayo.core.Lang;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by qiaoliang on 2017/8/4.
 */

public class FormText extends LinearLayout {
    public FormText(Context context) {
        super(context);
        init(context, null);
    }

    public FormText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public FormText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FormText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private TextView mLTvTitle, mTvValue;

    private void init(Context context, AttributeSet attrs){
        View v = View.inflate(context, R.layout.ui_form_text, null);
        MarginLayoutParams lp = new MarginLayoutParams(MarginLayoutParams.MATCH_PARENT, MarginLayoutParams.MATCH_PARENT);
        addView(v, lp);
        mLTvTitle = (TextView) findViewById(R.id.tv_title);
        mTvValue = (TextView) findViewById(R.id.tv_value);
    }

    public void setTitle(String label){
        mLTvTitle.setText(Lang.snull(label));
    }

    public void setValue(CharSequence value){
        mTvValue.setText(value);
    }
}
