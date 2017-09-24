package org.ayo.fringe.api2;


import org.ayo.http.AyoRequest;
import org.ayo.http.callback.BaseHttpCallback;
import org.ayo.http.converter.TypeToken;
import org.ayo.http.impl.FastJsonConverter;
import org.ayo.http.impl.OkHttp3Worker;
import org.ayo.http.stream.StreamConverter;
import org.ayo.fringe.model.top.Top;

import java.util.List;

/**
 * 资讯接口
 *
 */
public class TopApi {

    private static AyoRequest getAyoRequest(){
        return AyoRequest.request()
                .connectionTimeout(10000)
                .writeTimeout(10000)
                .readTimeout(10000)
                .worker(new OkHttp3Worker())
                .streamConverter(new StreamConverter.StringConverter())   //ByteArrayConverter   FileConverter
                .topLevelConverter(new TopLevelConverterTop())
                .resonseConverter(new FastJsonConverter())
                .header("os", "android")
                .header("version", "1")
                ;
    }

    /**
     * access_token	true	string	采用OAuth授权方式为必填参数，OAuth授权后获得。
     count	false	int	单页返回的记录条数，默认为50。
     page	false	int	返回结果的页码，默认为1。
     base_app	false	int	是否只获取当前应用的数据。0为否（所有数据），1为是（仅当前应用），默认为0。
     * @param flag
     * @param callback
     */
    public static void getTopList(String flag, int page,  BaseHttpCallback<List<Top>> callback){
        TopApi.getAyoRequest().tag(flag)
                //.url("http://192.168.1.194/appsdaddy/public/index.php/top/listTask")
                .url("http://114.215.81.196/appsdaddy/public/index.php/top/listTask")
                .actionGet()
                .queryString("count", "50")
                .queryString("page", page + "")
                .callback(callback, new TypeToken<List<Top>>(){})
                .fire();
    }

}
