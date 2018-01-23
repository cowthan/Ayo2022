package com.app.core.view.form;


import com.app.core.R;

import org.ayo.core.Lang;
import org.ayo.view.TextWatcherForLimitLength;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by qiaoliang on 2017/8/4.
 */

public class FormInput extends LinearLayout {
    public FormInput(Context context) {
        super(context);
        init(context, null);
    }

    public FormInput(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public FormInput(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FormInput(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private TextView mLTvTitle;
    private EditText mTvValue;
    private View mIvClear;

    private void init(Context context, AttributeSet attrs){
        View v = View.inflate(context, R.layout.ui_form_input, null);
        MarginLayoutParams lp = new MarginLayoutParams(MarginLayoutParams.MATCH_PARENT, MarginLayoutParams.MATCH_PARENT);
        addView(v, lp);
        mLTvTitle = (TextView) findViewById(R.id.tv_title);
        mTvValue = (EditText) findViewById(R.id.tv_value);
        mIvClear = findViewById(R.id.iv_clear);

        mIvClear.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view) {
                mTvValue.setText("");
            }
        });

        mTvValue.addTextChangedListener(new TextWatcherForLimitLength(mTvValue) {
            @Override
            public int getLimitedLength() {
                return 100;
            }

            @Override
            public void onLengthChanged(int length) {
                if(length == 0){
                    mIvClear.setVisibility(View.GONE);
                }else{
                    mIvClear.setVisibility(View.VISIBLE);
                }
            }
        });
        mIvClear.setVisibility(View.GONE);
    }

    public void setTitle(String label){
        mLTvTitle.setText(Lang.snull(label));
    }

    public void setHint(String hint){
        mTvValue.setHint(hint);
    }

    public void setValue(CharSequence value){
        if(value == null) value = "";
        mTvValue.setText(value);
        mTvValue.setSelection(value.length());
    }

    public EditText getEditText(){
        return mTvValue;
    }

    public String getValue(){
        return mTvValue.getText().toString().trim();
    }

}
