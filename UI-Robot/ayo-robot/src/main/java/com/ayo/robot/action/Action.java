package com.ayo.robot.action;

/**
 * Created by Administrator on 2016/12/13.
 */

public class Action {
    public String actionName;
    public OnActionCallback callback;

    public Action(String actionName, OnActionCallback callback){
        this.actionName = actionName;
        this.callback = callback;
    }
}
