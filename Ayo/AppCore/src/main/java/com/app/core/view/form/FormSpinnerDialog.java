package com.app.core.view.form;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.app.core.prompt.ListDialog;

import java.util.List;

/**
 * Created by qiaoliang on 2017/8/4.
 */

public class FormSpinnerDialog extends FormMenu {
    public FormSpinnerDialog(Context context) {
        super(context);
    }

    public FormSpinnerDialog(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FormSpinnerDialog(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FormSpinnerDialog(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setItems(final Activity activity, final List<String> items, final int selectedIndex, final OnCheckChangedCallback onCheckChangedCallback){

        if(items == null || items.size() == 0) return;
        if(selectedIndex >= 0 && selectedIndex < items.size()){
            setSubTitle(items.get(selectedIndex));
        }

        this.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view) {
                ListDialog dialog = new ListDialog(activity, items, selectedIndex);
                dialog.setTitle("");
                dialog.setCancelButtonEnable(false);
                dialog.setOnItemSelectedCallback(new ListDialog.OnItemSelectedCallback() {
                    @Override
                    public void onSelected(String item, int position) {
                        setSubTitle(item);
                        onCheckChangedCallback.onCheckChanged(position, item, true);
                    }
                });
                dialog.show();
            }
        });
    }

}
