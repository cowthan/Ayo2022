package com.ayo.sdk.demo;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.evernote.android.job.Job;

import org.ayo.core.Lang;
import org.ayo.sample.R;

import java.util.Random;

/**
 * @author rwondratschek
 */
public class DemoSyncJob extends Job {

    public static final String TAG = "job_demo_tag";

    private static int times = 0;

    @Override
    @NonNull
    protected Result onRunJob(final Params params) {
        boolean success = new DemoSyncEngine(getContext()).sync();

        if (params.isPeriodic()) {

            times++;
            Lang.alert(new Random().nextInt(), "定时任务", "定时任务：第" + times + "次激活", "定时任务", R.mipmap.ic_launcher, new Intent(getContext(), DemoJobActivity.class));

//            PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, new Intent(getContext(), DemoJobActivity.class), 0);
//
//            Notification notification = new NotificationCompat.Builder(getContext())
//                    .setContentTitle("Job Demo")
//                    .setContentText("Periodic job ran")
//                    .setAutoCancel(true)
//                    .setContentIntent(pendingIntent)
//                    .setSmallIcon(R.mipmap.ic_launcher)
//                    .setShowWhen(true)
//                    .setColor(Color.GREEN)
//                    .setLocalOnly(true)
//                    .build();
//
//            NotificationManagerCompat.from(getContext()).notify(new Random().nextInt(), notification);
        }

        return success ? Result.SUCCESS : Result.FAILURE;
    }
}
