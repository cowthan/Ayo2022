package com.app.core.utils.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;

/**
 * Created by qiaoliang on 2017/10/28.
 */

public interface OnSensorCallback {

    public void onSensorChanged(SensorEvent event) ;

    public void onAccuracyChanged(Sensor sensor, int accuracy);

    public void release();

}
