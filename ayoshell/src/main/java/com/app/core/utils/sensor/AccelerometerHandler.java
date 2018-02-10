package com.app.core.utils.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

/**
 * Created by qiaoliang on 2017/10/28.
 *
 * 加速度传感器
 */

public abstract class AccelerometerHandler implements OnSensorCallback {


    private long last_time; //上一次记录的时间
    private static long timeThreashold = 10;
    private static long speedThreadhold_for_shake = 4800;
    private static long speed_scale = 10000;
    private static long shake_delay_time = 1000; //激活一次shake之后，1秒之内不能再激活

    private float lastX, lastY, lastZ;

    private Handler handler;

    public AccelerometerHandler(){
        handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        long currentTime = System.currentTimeMillis();
        long timeDistance = currentTime - last_time;
        if(timeDistance > timeThreashold){
            last_time = currentTime;

            //当前加速度传感器3个轴的距离
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
//            Log.e("Sensor", x + ", " + y + ", " + z);

            // 速度的阈值

            float current = x + y + z;
            float last = lastX + lastY + lastZ;
            double absValue = Math.abs(current - last);
            double speed = absValue / timeDistance;

            speed *= speed_scale; //数据太小，做适当缩放

            if(speed > speedThreadhold_for_shake && isShakeEnabled){
                //速度大于此数，就认为是晃动
                setEnableShake(false);
                onShake(this);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setEnableShake(true);
                    }
                }, shake_delay_time);
            }

            lastX = x;
            lastY = y;
            lastZ = z;
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.e("Sensor", "onAccuracyChanged--"+ accuracy );
    }


    private boolean isShakeEnabled = true;
    public void setEnableShake(boolean enable){
        isShakeEnabled = enable;
    }

    @Override
    public void release() {
        handler.removeCallbacksAndMessages(null);
    }

    protected abstract void onShake(AccelerometerHandler handler);
}
