package org.ayo.http;


import org.ayo.AppCore;
import org.ayo.http.dns.HttpDnsUtils;
import org.ayo.http.impl.log.HttpLoggingInterceptor;
import org.ayo.http.utils.HttpsSignUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by qiaoliang on 2017/11/18.
 */

public class OkHttp3BuilderManager {

    static OkHttpClient.Builder okHttpClientBuilder;

    public static boolean isDnsEnabled = false;

    public static OkHttpClient.Builder getOkHttpClientBuilder(){
        if(okHttpClientBuilder == null){
            HttpsSignUtils.SSLParams sslParams = HttpsSignUtils.getSslSocketFactory(null, null, null);
            okHttpClientBuilder = new OkHttpClient.Builder()
                    .connectTimeout(30000, TimeUnit.MILLISECONDS)
                    .readTimeout(30000, TimeUnit.MILLISECONDS)
                    .writeTimeout(30000, TimeUnit.MILLISECONDS)
                    .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);  //设置可访问所有的https网站
            if(isDnsEnabled) okHttpClientBuilder.addInterceptor(HttpDnsUtils.getDnsInterceptor());

            AppCore.getDefault().getHttpUserProxy().handleOkHttpBuilder(okHttpClientBuilder);

            //公共header设置
            Interceptor mTokenInterceptor = new Interceptor() {
                @Override public Response intercept(Chain chain) throws IOException {
                    Request originalRequest = chain.request();
                    Request.Builder builder = originalRequest.newBuilder();
                    AppCore.getDefault().getHttpUserProxy().handleCommonHeaderAndParameters(builder);
                    Request authorised = builder.build();
                    return chain.proceed(authorised);
                }
            };
            okHttpClientBuilder.addNetworkInterceptor(mTokenInterceptor);

        }
        return okHttpClientBuilder;
    }

}
