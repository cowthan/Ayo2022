package org.ayo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.List;

/**
 * Created by Administrator on 2017/7/8.
 */

public class GlobalReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        List<ReceiverAction> receivers = ReceiverDelegate.getDefault().getReceivers();
        for(ReceiverAction receiver: receivers){
            receiver.onReceive(context, intent.getAction(), intent);
        }
    }

}
