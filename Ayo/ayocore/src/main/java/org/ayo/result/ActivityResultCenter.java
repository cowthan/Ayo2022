package org.ayo.result;

import android.content.Intent;
import android.util.SparseArray;

/**
 * Created by qiaoliang on 2018/1/2.
 */

public class ActivityResultCenter {
    private ActivityResultCenter(){}

    private static final class Holder{
        private static final ActivityResultCenter instance = new ActivityResultCenter();
    }

    public static ActivityResultCenter getDefault(){
        return Holder.instance;
    }

    private SparseArray<OnActivityResultCallback> callbacks = new SparseArray<>();

    public void register(Object component, OnActivityResultCallback callback) {
        int key = component.hashCode();
        if (callbacks.get(key) == null) {
            callbacks.put(key, callback);
        }
    }

    public void unregister(Object component) {
        int key = component.hashCode();
        if (callbacks.get(key) != null) {
            callbacks.remove(key);
        }
    }

    public void onActivityResult(Object component, int requestCode, int resultCode, Intent data){
        int key = component.hashCode();
        OnActivityResultCallback callback = callbacks.get(callbacks.keyAt(key));
        unregister(component);
        callback.onActivityResult(requestCode, resultCode, data);
    }
}
