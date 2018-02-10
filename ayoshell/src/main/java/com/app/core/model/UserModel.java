package com.app.core.model;

/**
 * Created by Administrator on 2017/10/13.
 */

public class UserModel {
    public String avatar = "";
    public String name = "";
    public String desc = "";

    public String token;
    public String user_id;
    public int role;
    public boolean hasInvitedEnough = false;

    public boolean isStudent(){
        return role == 1;
    }

    public boolean isTeacher(){
        return role == 2;
    }

    public boolean isGuest(){
        return role == 0;
    }
}
