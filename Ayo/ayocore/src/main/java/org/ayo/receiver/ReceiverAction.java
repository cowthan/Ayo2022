package org.ayo.receiver;

import android.content.Context;
import android.content.Intent;

/**
 * Created by Administrator on 2017/7/9.
 */

public interface ReceiverAction {
    void onReceive(Context context, String action, Intent intent);
}
