package org.ayo.http.callback;

public abstract class BaseHttpCallback<T> {

	public BaseHttpCallback(){
	}
	
	public abstract void onFinish(boolean isSuccess, FailInfo resp, T t);

}
