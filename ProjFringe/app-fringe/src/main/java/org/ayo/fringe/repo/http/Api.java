package org.ayo.fringe.repo.http;

import org.ayo.fringe.model.MmListModel;
import org.ayo.http.AyoRequest;
import org.ayo.http.callback.BaseHttpCallback;
import org.ayo.http.converter.TypeToken;
import org.ayo.http.impl.FastJsonConverter;
import org.ayo.http.impl.OkHttp3Worker;
import org.ayo.http.stream.StreamConverter;

/**
 * Created by qiaoliang on 2017/9/7.
 */

public class Api {
    private static AyoRequest getRequest(){
        return AyoRequest.request()
                .connectionTimeout(10000)
                .writeTimeout(10000)
                .readTimeout(10000)
                .worker(new OkHttp3Worker())
                .streamConverter(new StreamConverter.StringConverter())   //ByteArrayConverter   FileConverter
                .topLevelConverter(new MmTopLevelConverter())
                .resonseConverter(new FastJsonConverter());
    }


    public static void getMmList(String tag, String url, BaseHttpCallback<MmListModel> callback){
        getRequest().tag(tag)
                .actionGet()
                .url(url)
                .callback(callback, new TypeToken<MmListModel>(){})
                .fire();
    }
}
