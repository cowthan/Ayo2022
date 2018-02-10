package com.app.core.view.form;


import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.app.core.R;


/**
 * Created by qiaoliang on 2017/8/4.
 */

public class FormCheckBoxItem extends FormCheckBoxButton {
    public FormCheckBoxItem(Context context) {
        super(context);
    }

    public FormCheckBoxItem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FormCheckBoxItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FormCheckBoxItem(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public int getLayoutId() {
        return R.layout.ui_form_checkbox_item;
    }
}
