package com.app.core.view.form;


import com.app.core.R;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;


/**
 * Created by qiaoliang on 2017/8/4.
 */

public class FormRadioGroupRows extends FormRadioGroup {
    public FormRadioGroupRows(Context context) {
        super(context);
    }

    public FormRadioGroupRows(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FormRadioGroupRows(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FormRadioGroupRows(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public int getLayoutId() {
        return R.layout.ui_form_radio_group_rows;
    }

}
