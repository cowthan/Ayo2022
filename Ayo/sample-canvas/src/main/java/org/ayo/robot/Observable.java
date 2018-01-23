package org.ayo.robot;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by qiaoliang on 2017/12/19.
 */

public class Observable {
    private ArrayList<Observer> observers = new ArrayList();

    public Observable() {
    }

    public void addObserver(Observer observer) {
        this.observers.add(observer);
    }

    public void notifyDataChanged(Object source, Object data) {
        Iterator var3 = this.observers.iterator();

        while(var3.hasNext()) {
            Observer observer = (Observer)var3.next();
            observer.update(this, source, data);
        }

    }
}
