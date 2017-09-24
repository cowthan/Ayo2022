package org.ayo.service;

import android.content.Context;
import android.content.Intent;

/**
 * Created by qiaoliang on 2017/6/16.
 */

public interface ServiceAction {

    void run(Context context, String action, Intent intent);

}
