package org.ayo.http.retrofit;

/**
 * Created by _SOLID
 * Date:2016/5/13
 * Time:11:39
 */
public abstract class AbsConvert<T> {

    abstract T parseData(String result);
}
