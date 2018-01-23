package org.ayo.core;

import java.util.LinkedList;

/**
 *   protected Observable observable = new Observable();
     public Observable getObservable(){
            return observable;
     }
 */

public class Observable {
    private LinkedList<Observer> observers = new LinkedList<>();

    public synchronized void addObserver(Observer observer){
        observers.add(observer);
    }

    public synchronized void removeObserver(Observer observer){
        observers.remove(observer);
    }

    public synchronized void notifyDataChanged(Object source, Object data){
        for(Observer observer: observers){
            observer.update(this, source, data);
        }
    }
}
