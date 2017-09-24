package com.ayo.robot;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.ayo.robot.action.ActionView;
import com.ayo.robot.cmd.CommandView;
import com.ayo.robot.config.ConfigView;
import com.ayo.robot.log.LogView;
import com.ayo.robot.phone.PhoneInfoView;

/**
 * Created by Administrator on 2016/12/13.
 */

public class RobotView extends FrameLayout {
    public RobotView(Context context) {
        super(context);
        init();
    }

    public RobotView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RobotView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RobotView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    CenterButton btn;
    FrameLayout leftContainer;
    FrameLayout topContainer;
    FrameLayout bottomContainer;
    FrameLayout rightContainer;

    ConfigView configView;
    LogView logView;
    ActionView actionView;
    PhoneInfoView phoneInfoView;
    CommandView commandView;

    private void init(){

        this.setBackgroundColor(Color.TRANSPARENT);

        //create center button
        btn = new CenterButton(getContext());
        btn.setText("UI Center");
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER;
        this.addView(btn, lp);

        //create left, top, right, bottom
        leftContainer = new FrameLayout(getContext());
        lp = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        this.addView(leftContainer, lp);

        rightContainer = new FrameLayout(getContext());
        lp = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        this.addView(rightContainer, lp);

        topContainer = new FrameLayout(getContext());
        lp = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        this.addView(topContainer, lp);

        bottomContainer = new FrameLayout(getContext());
        lp = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        this.addView(bottomContainer, lp);

        leftContainer.setBackgroundColor(Color.TRANSPARENT);
        rightContainer.setBackgroundColor(Color.TRANSPARENT);
        topContainer.setBackgroundColor(Color.TRANSPARENT);
        bottomContainer.setBackgroundColor(Color.TRANSPARENT);

        //init left
        configView = new ConfigView(getContext());
        lp = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        leftContainer.addView(configView, lp);

        //init top
        logView = new LogView(getContext());
        lp = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        topContainer.addView(logView, lp);

        //init right
        actionView = new ActionView(getContext());
        lp = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        rightContainer.addView(actionView, lp);

        //init bottom
        phoneInfoView = new PhoneInfoView(getContext());
        lp = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        bottomContainer.addView(phoneInfoView, lp);

        //init voice record and recognize
        commandView = new CommandView(getContext());
        lp = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        this.addView(commandView, lp);

        ///hide all
        commandView.setVisibility(View.GONE);

        ///event
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                touchHeadSize = 300;
                commandView.setVisibility(View.VISIBLE);
                requestLayout();
            }
        });

        btn.setOnLongClickListener(new OnLongClickListener(){
            @Override
            public boolean onLongClick(View view) {
                touchHeadSize = 300;
                commandView.setVisibility(View.VISIBLE);
                requestLayout();
                return true;
            }
        });
    }

    public ConfigView getConfigView(){
        return configView;
    }

    public LogView getLogView(){
        return logView;
    }

    public ActionView getActionView(){
        return actionView;
    }

    public PhoneInfoView getPhoneInfoView(){
        return phoneInfoView;
    }

    public CommandView getCommandView(){
        return commandView;
    }

    int touchHeadSize = 0;

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int sw = Display.screenWidth;
        int sh = Display.screenHeight;
        configView.layout(-sw + touchHeadSize, 0, 0 + touchHeadSize, sh);
        logView.layout(0, -sh + touchHeadSize, sw, 0 + touchHeadSize);
        actionView.layout(sw - touchHeadSize, 0, sw + sw - touchHeadSize, sh);
        phoneInfoView.layout(0, sh - touchHeadSize, sw, sh + sh - touchHeadSize);
        if(commandView.getVisibility() != View.GONE) commandView.layout(0, 0, sw, sh);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
