package org.ayo.logpage;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/9.
 *
 * 对应的broadcast type是log
 */

public class LogInfo implements Serializable{
    public String title;
    public String subTitle;
    public String content;
    public int type;  // 1 崩溃   2 请求  3 普通log   后台可根据这个type来筛选
    public boolean needNotification = false;  // 是否需要打开
    public long reportTime;

    public static String parseCrash(String content){
        return content;
    }

    public static String parseRequest(String content){
        return content;
    }
}
