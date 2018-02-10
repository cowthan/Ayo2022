package org.ayo.result;

import android.content.Intent;

/**
 * Created by qiaoliang on 2018/1/2.
 */

public interface OnActivityResultCallback {

    void onActivityResult(int requestCode, int resultCode, Intent data);

}
