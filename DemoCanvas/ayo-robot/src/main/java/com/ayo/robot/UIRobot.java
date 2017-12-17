package com.ayo.robot;

import android.app.Activity;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.ayo.robot.action.Action;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/13.
 */

public class UIRobot {

    private RobotView robotView;

    public static UIRobot attachTo(Activity mActivity, ViewGroup root){

        Display.init(mActivity);

        UIRobot robot = new UIRobot();
        robot.robotView = new RobotView(mActivity);

        if(!(root instanceof FrameLayout)){
            throw new RuntimeException("根布局不是FrameLayout，没法往里添加全屏的view了啊！");
        }else{
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            root.addView(robot.robotView, lp);
        }

        robot.robotView.getConfigView().setBackgroundColor(Color.parseColor("#7777D9B2"));
        robot.robotView.getLogView().setBackgroundColor(Color.parseColor("#77F7B748"));
        robot.robotView.getActionView().setBackgroundColor(Color.parseColor("#77E86D64"));
        robot.robotView.getPhoneInfoView().setBackgroundColor(Color.parseColor("#77F7B748"));
        robot.robotView.getCommandView().setBackgroundColor(Color.parseColor("#77AEA6E3"));

        return robot;
    }

    //-------------------------------------------------------------------
    //action
    //-------------------------------------------------------------------
    private List<Action> actions = new ArrayList<>();
    public void setActions(List<Action> actions){
        this.actions = actions;
        robotView.getActionView().notifyActionSetChanged(actions);
    }

    //-------------------------------------------------------------------
    //log
    //-------------------------------------------------------------------


    //-------------------------------------------------------------------
    //config
    //-------------------------------------------------------------------


    //-------------------------------------------------------------------
    //phone info
    //-------------------------------------------------------------------


    //-------------------------------------------------------------------
    //command
    //-------------------------------------------------------------------


}
