package com.worse.more.breaker.base;

/**
 * Created on : 05-03-2016
 * Author     : derohimat
 * Name       : Deni Rohimat
 * Email      : rohimatdeni@gmail.com
 * GitHub     : https://github.com/derohimat
 * LinkedIn   : https://www.linkedin.com/in/derohimat
 */

public abstract class AyoPresenter22<T extends AyoView22>{

    T v;

    protected T getView(){
        return v;
    }

    void attachView(T view){
        v = view;
    }
    void detachView(){
        v = null;
    }
}