package org.ayo.component.mvp;

/**
 * Created on : 05-03-2016
 * Author     : derohimat
 * Name       : Deni Rohimat
 * Email      : rohimatdeni@gmail.com
 * GitHub     : https://github.com/derohimat
 * LinkedIn   : https://www.linkedin.com/in/derohimat
 */

public abstract class AyoPresenter<T extends AyoView>{
    T view;

    public T getView(){
        return view;
    }

    public void attachView(T view){
        this.view = view;
    }
    public void detachView(){
        this.view = null;
    }
}