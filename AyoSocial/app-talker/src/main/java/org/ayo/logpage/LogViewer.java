package org.ayo.logpage;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.zebdar.tom.R;
import com.zebdar.tom.chat.MessageCenter;

import org.ayo.core.Lang;
import org.ayo.log.Trace;
import org.ayo.receiver.MyCustomBroarcast;
import org.ayo.receiver.ReceiverAction;
import org.ayo.receiver.ReceiverDelegate;

/**
 * Created by Administrator on 2017/7/9.
 *
 * 不管从哪儿来的日志，不管什么类型的日志，都有以下几个主要字段：
 * ——主标题：如崩溃，请求，错误请求
 * ——副标题：如e.getMessage(), 哪个接口返回什么code
 * ——type：隐藏的字段
 * ——内容：对原始的通过Intent或者其他途径传过来的json做一个解析，生成html
 */

public class LogViewer {

    private LogViewer(){}

    private static final class Holder{
        private static final LogViewer instance = new LogViewer();
    }

    public static LogViewer getDefault(){
        return Holder.instance;
    }

    public void init(Application app){
        ReceiverDelegate.getDefault().register(new ReceiverAction() {
            @Override
            public void onReceive(Context context, String action, Intent intent) {
                if(MyCustomBroarcast.isMyCustomBroardcast(intent)){
                    MyCustomBroarcast m = MyCustomBroarcast.parse(intent);
                    if("log".equals(m.type)){
                        LogInfo logInfo = LogViewer.getDefault().parse(m.payload);

                        if(logInfo.needNotification){
                            Intent intent1 = new Intent(context, LogActivity.class);
                            intent1.putExtra("log", logInfo);
                            Lang.alert(34, "日志系统：" + logInfo.title, logInfo.subTitle, "收到一个日志", R.drawable.ic_customer_loading2, intent1);
                        }else{
                            Trace.e("则不达日志系统", "收到日志：" + logInfo.title + ", " + logInfo.subTitle);
                        }

                        MessageCenter.getDefault().sendText("收到日志：" + logInfo.title + ", " + logInfo.subTitle);
                    }
                }
            }
        });

    }

    public LogInfo parse(Bundle bundle){
        LogInfo logInfo = new LogInfo();
        logInfo.title = Lang.rstring(bundle, "title", "未定义title");
        logInfo.subTitle = Lang.rstring(bundle, "subTitle", "未定义subTitle");
        logInfo.type = Lang.rint(bundle, "type", 0);
        logInfo.content = Lang.rstring(bundle, "content", "未定义content");
        logInfo.type = Lang.rint(bundle, "type", 0);
        logInfo.needNotification = Lang.rbool(bundle, "needNotification", false);
        logInfo.reportTime = Lang.toLong(Lang.rstring(bundle, "reportTime", "0"));
        return logInfo;
    }

}
