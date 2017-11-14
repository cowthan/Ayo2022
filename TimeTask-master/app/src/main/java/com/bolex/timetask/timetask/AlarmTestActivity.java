package com.bolex.timetask.timetask;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class AlarmTestActivity extends AppCompatActivity {

    public static final String INTENT_ALARM_LOG = "intent_alarm_log";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_alarm_test);

        init();
    }

    private int count2 = 0;

    private void init() {
        final TextView tv_info = (TextView) findViewById(R.id.tv_info);
        final TextView tv_info2 = (TextView) findViewById(R.id.tv_info2);

        AlarmCenter.getDefault().addCallback(this, new AlarmCenter.OnAlarmCallback() {
            @Override
            public void onAlarm(String name) {
                if("name1".equals(name)){
                    tv_info.setText("触发");
                }else if("name2".equals(name)){
                    count2++;
                    tv_info2.setText("触发" + count2 + "次");
                }
            }
        });

        AlarmCenter.getDefault().alarm("name1", SystemClock.elapsedRealtime()  + 5000);
        AlarmCenter.getDefault().alarmRepeat("name2", SystemClock.elapsedRealtime() + 3000, 2000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AlarmCenter.getDefault().removeCallbackByTag(this);
    }
}
