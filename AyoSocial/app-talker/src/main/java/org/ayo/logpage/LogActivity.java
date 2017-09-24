package org.ayo.logpage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.zebdar.tom.R;

import org.ayo.core.Lang;
import org.ayo.core.EmailTools;
import org.ayo.notify.spinner.IOptionModel;
import org.ayo.notify.spinner.NiceSpinner;
import org.ayo.notify.spinner.OnOptionSelectedListener;
import org.ayo.notify.spinner.SpinnerWrapper;
import org.ayo.notify.toaster.Toaster;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by Administrator on 2017/7/9.
 */

public class LogActivity extends AppCompatActivity{

    private LogInfo mLogInfo;

    TextView mtitleTv;
    TextView msubtitleTv;
    TextView mContentTv;
    NiceSpinner mDeveloperSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_log);
        setTitle("日志页");
        mLogInfo = Lang.rserializable(getIntent(), "log");
        if(mLogInfo == null){
            Toaster.toastLong("必须传入一个LogInfo对象，字段为log");
            return;
        }

        mtitleTv = (TextView) findViewById(R.id.tv_log_title);
        msubtitleTv = (TextView) findViewById(R.id.tv_subtitle);
        mContentTv = (TextView) findViewById(R.id.tv_content);
        mDeveloperSpinner = (NiceSpinner) findViewById(R.id.spinner_developer_emails);

        List<DeveloperModel> developers = new ArrayList<>();
        DeveloperModel d = new DeveloperModel();
//        d.name = "乔良";
//        d.email = "cowthan@163.com";
//        developers.add(d);
//
//        d = new DeveloperModel();
//        d.name = "郑雪";
//        d.email = "snowy1013@163.com";
//        developers.add(d);
//
//        d = new DeveloperModel();
//        d.name = "李帅";
//        d.email = "357200900@qq.com";
//        developers.add(d);

        d.name = "乔良";
        d.email = "qiaoliang@dongqiudi.com";
        developers.add(d);

        d = new DeveloperModel();
        d.name = "社会我龙哥";
        d.email = "wanglong@dongqiudi.com";
        developers.add(d);

        d = new DeveloperModel();
        d.name = "二龙湖昊哥";
        d.email = "hehao@dongqiudi.com";
        developers.add(d);

        SpinnerWrapper.bind(this, mDeveloperSpinner, developers, new OnOptionSelectedListener() {
            @Override
            public void onSelected(View v, IOptionModel m, int position) {
                DeveloperModel dev = (DeveloperModel) m;
                EmailTools.sendEmail("应用报错", mLogInfo.title + "<br/>" + mLogInfo.subTitle + "<br/>" + mLogInfo.content, dev.email, "279800561@qq.com");
                Toaster.toastLong("邮件已发出，收不收的到不确定。。。");
            }

            @Override
            public void onNothingSelected() {

            }
        });

        mtitleTv.append(mLogInfo.title);
        msubtitleTv.append(mLogInfo.subTitle);
        Lang.setHtml(mContentTv, (mLogInfo.content));

    }

}
