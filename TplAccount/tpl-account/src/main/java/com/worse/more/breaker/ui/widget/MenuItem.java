package com.worse.more.breaker.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.worse.more.breaker.R;

import org.ayo.view.textview.BadgeView;


/**
 * 菜单列表的item
 */
public class MenuItem extends FrameLayout{
    public MenuItem(Context context) {
        super(context);
        init(null);
    }

    public MenuItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public MenuItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MenuItem(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    TextView tv;
    ImageView iv;
    BadgeView tv_unread;
    ImageView iv_indicator;

    private void init(AttributeSet attrs){
        View v = View.inflate(getContext(), R.layout.ayo_layout_menu_item, null);
        this.addView(v);
        tv = (TextView) findViewById(R.id.tv);
        iv = (ImageView) findViewById(R.id.iv);
        tv_unread = (BadgeView) findViewById(R.id.tv_unread);
        iv_indicator = (ImageView) findViewById(R.id.iv_indicator);

        if(attrs == null) return;

//        //解析android:text
//        TypedArray a = getContext().obtainStyledAttributes(attrs, com.android.internal.R.styleable.TextView);
//        int n = a.getIndexCount();
//        for (int i = 0; i < n; i++) {
//            int attr = a.getIndex(i);
//            if(attr == com.android.internal.R.styleable.TextView_text){
//                String text = a.getText(attr).toString();
//                tv.setText(text);
//            }
//        }
//        a.recycle();
//        a = null;
//
//        //解析android:src
//        a = getContext().obtainStyledAttributes(attrs, com.android.internal.R.styleable.ImageView);
//        Drawable d = a.getDrawable(com.android.internal.R.styleable.ImageView_src);
//        if (d != null) {
//            iv.setImageDrawable(d);
//        }
//        a.recycle();
//        a = null;

    }

    public void setMenuInfo(String menu, int icon, OnClickListener onClickListener){
        this.tv.setText(menu);
        this.iv.setImageResource(icon);
        this.setOnClickListener(onClickListener);
    }

    public void setUnreadMessageCount(int count){
        if(count <= 0){
            tv_unread.setVisibility(View.INVISIBLE);
            tv_unread.setText(0 + "");
        }else{
            tv_unread.setVisibility(View.VISIBLE);
            tv_unread.setText(count >= 100 ? "99+" : count + "");
        }
    }

    public void setRightIndicator(int icon){
        if(icon > 0) {
            iv_indicator.setVisibility(View.VISIBLE);
            iv_indicator.setImageResource(icon);
        }else{
            iv_indicator.setVisibility(View.INVISIBLE);
        }
    }


}
