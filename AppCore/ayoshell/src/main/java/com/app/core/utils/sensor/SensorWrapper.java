package com.app.core.utils.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import org.ayo.AppCore;

/**
 * Created by qiaoliang on 2017/10/27.
 *
 * 摇一摇
 */

public class SensorWrapper {

    private SensorWrapper(){}
//
//    private static final class Holder{
//        private static final SensorWrapper instance = new SensorWrapper();
//    }
//
//    public static SensorWrapper newInstance(){
//        return Holder.instance;
//    }

    SensorManager sm;
    SensorEventListener mListener;
    Sensor mSensor;
    OnSensorCallback onSensorCallback;

    public static SensorWrapper start(int sensorType, int rate, OnSensorCallback callback){
        SensorWrapper s = new SensorWrapper();
        s.onSensorCallback = callback;
        s.init(sensorType, rate);
        return s;
    }

    private void init(int sensorType, int rate){
        sm = (SensorManager) AppCore.app().getSystemService(Context.SENSOR_SERVICE);
        mSensor = sm.getDefaultSensor(sensorType);
        mListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if(onSensorCallback != null){
                    onSensorCallback.onSensorChanged(event);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                if(onSensorCallback != null){
                    onSensorCallback.onAccuracyChanged(sensor, accuracy);
                }
            }
        };
        sm.registerListener(mListener, mSensor, rate);
    }

    public void stop(){
        if(sm != null){
            sm.unregisterListener(mListener);
            onSensorCallback.release();
            onSensorCallback = null;
        }
    }



}

/**
 运动传感器：
 - 加速度： 摇一摇
 - 陀螺仪

 环境传感器：
 - 温度
 - 光

 位置传感器：
 - 方向
 - 磁力计

 SensorManager：传感器服务
 Sensor：某一个传感器
 SensorEvent：传感器事件
 SensorEventListener：监听

 */
