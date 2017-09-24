package org.ayo.fringe.ui.base;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import org.ayo.component.MasterActivity;

/**
 * Created by Administrator on 2017/1/14 0014.
 */

public abstract class BaseFrgActivity extends MasterActivity {
    public boolean checkPermission(@NonNull String permission) {
        return ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }
}
