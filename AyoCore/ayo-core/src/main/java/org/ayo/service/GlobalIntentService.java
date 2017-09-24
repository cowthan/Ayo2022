
package org.ayo.service;

import android.app.IntentService;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

public class GlobalIntentService extends IntentService {

    public GlobalIntentService() {
        super("GlobalIntentService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e("global-service", "后台Service运行");
        if (intent != null) {
            final String action = intent.getAction();
            if (TextUtils.isEmpty(action)) {
                return;
            }

            ///尝试去delegate里找action，找到则运行
            if (ServiceDelegate.getDefault().containsAction(action)) {
                ServiceAction task = ServiceDelegate.getDefault().getAction(action);
                task.run(this, action, intent);
                return;
            }
            ///~~~
        }
    }

}
