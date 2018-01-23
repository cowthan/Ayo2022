package com.app.core.view.form;


import com.app.core.R;

import org.ayo.core.Lang;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by qiaoliang on 2017/8/4.
 */

public class FormMenu extends LinearLayout {
    public FormMenu(Context context) {
        super(context);
        init(context, null);
    }

    public FormMenu(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public FormMenu(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FormMenu(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private TextView tv_menu_title, tv_extra_info;
    private ImageView iv_menu_notify, iv_arrow;

    private void init(Context context, AttributeSet attrs){
//        LayoutInflater.from(getContext()).inflate(R.layout.layout_form_big_checkbox, this, true);
        View v = View.inflate(context, R.layout.ui_menu_item_horizontal, null);
        MarginLayoutParams lp = new MarginLayoutParams(MarginLayoutParams.MATCH_PARENT, MarginLayoutParams.MATCH_PARENT);
        addView(v, lp);
        tv_menu_title = (TextView) findViewById(R.id.tv_menu_title);
        tv_extra_info = (TextView) findViewById(R.id.tv_extra_info);
        iv_menu_notify = (ImageView) findViewById(R.id.iv_menu_notify);
        iv_arrow = (ImageView) findViewById(R.id.iv_arrow);
    }

    public void setTitle(String title){
        tv_menu_title.setText(Lang.snull(title));
    }


    public void setSubTitle(String subTitle){
        if(TextUtils.isEmpty(subTitle)){
            tv_extra_info.setVisibility(View.INVISIBLE);
        }else{
            tv_extra_info.setText(subTitle);
        }
    }

    public void setArrowEnable(boolean enable){
        iv_arrow.setVisibility(enable ? View.VISIBLE : View.INVISIBLE);
    }

    public void setNotify(int notifyCount){
        iv_menu_notify.setVisibility(notifyCount > 0 ? View.VISIBLE : View.INVISIBLE);
    }

}
