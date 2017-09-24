package org.ayo.imagepicker;

/**
 * Created by qiaoliang on 2017/8/19.
 */

public class MyEventBus {

    private MyEventBus() {
    }

    private static final class Holder {
        private static final MyEventBus instance = new MyEventBus();
    }

    public static MyEventBus getDefault() {
        return Holder.instance;
    }

    public void register(Object o){

    }

    public void unregister(Object o){

    }

    public void post(Object e){

    }

}
