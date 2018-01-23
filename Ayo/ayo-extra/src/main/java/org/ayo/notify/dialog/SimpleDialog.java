package org.ayo.notify.dialog;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;

import org.ayo.notify.R;

/**
 * 最简单版的Dialog封装，也挺通用，简单的进度框提示框，尽量用这个
 */
public class SimpleDialog extends Dialog {
    public SimpleDialog(Context context) {
        super(context, R.style.Ayo_DialogStyle);
    }

    public SimpleDialog(Context context, int theme) {
        super(context, theme);
    }

    public SimpleDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void show() {
        super.show();
    }

    public void resetWidth() {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = getContext().getResources().getDisplayMetrics().widthPixels * 7 / 9;
        getWindow().setAttributes(params);
    }
}
