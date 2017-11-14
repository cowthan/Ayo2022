package com.bolex.timetask.timetask;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class AlarmTestActivity2 extends AppCompatActivity {

    public static final String INTENT_ALARM_LOG = "intent_alarm_log";

    private TimePicker timePicker;
    private CheckBox checkBox;
    private Button btnConfirm;

    private int hour;
    private int minute;

    AlarmManager am;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_alarm_test);

        init();
    }

    private void init() {
        timePicker = (TimePicker) findViewById(R.id.time_picker);
        timePicker.setIs24HourView(true);
        checkBox = (CheckBox) findViewById(R.id.checkbox);
        btnConfirm = (Button) findViewById(R.id.confirm);

        am = (AlarmManager) getSystemService(ALARM_SERVICE);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) {
                    hour = timePicker.getHour();
                    minute = timePicker.getMinute();
                } else {
                    hour = timePicker.getCurrentHour();
                    minute = timePicker.getCurrentMinute();
                }
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);

                Intent intent = new Intent(INTENT_ALARM_LOG);
                PendingIntent pi = PendingIntent.getBroadcast(AlarmTestActivity2.this, 0, intent, 0);

                if (!checkBox.isChecked()) {
//                    am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);
                    am.set(AlarmManager.RTC_WAKEUP, getStartTime(), pi);
                } else {
                    long intervalMillis  = 1000; // 60秒
                    am.setRepeating(AlarmManager.RTC_WAKEUP, getStartTime(), intervalMillis, pi);
                }
            }
        });
    }

    public static class AlarmReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == AlarmTestActivity2.INTENT_ALARM_LOG) {
                Log.d("AlarmReceiver", "log log log");
            }
        }
    }

    private long countDownSec = 0;

    private long getStartTime(){
        final TextView tv_info = (TextView) findViewById(R.id.tv_info);
        int delaySec = 10;
        long startTime = System.currentTimeMillis() + delaySec * 1000;
        countDownSec = delaySec;
        tv_info.setText(countDownSec + "秒后触发Alarm");
        CountDownTimer timer = new CountDownTimer(countDownSec * 1000, 1000) {
            @Override
            public void onTick(long l) {
                countDownSec--;
                tv_info.setText(countDownSec + "秒后触发Alarm");
            }

            @Override
            public void onFinish() {

            }
        };
        timer.start();
        return startTime;
    }

}
