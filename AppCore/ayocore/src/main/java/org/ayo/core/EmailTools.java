package org.ayo.core;

import android.util.Log;

import org.ayo.http.impl2.SimpleHttpClient;

/**
 * Created by qiaoliang on 2017/4/12.
 */

public class EmailTools {


    public static void sendEmail(final String subjuct, final String content, final String to, final String from){
        String url = "http://114.215.81.196/service/mail/index.php";
        SimpleHttpClient.HTTP_METHOD method = SimpleHttpClient.HTTP_METHOD.POST;
        boolean isMultipart = false;
        SimpleHttpClient.UrlBuilder urlBuilder = null; //new SimpleHttpClient.UrlBuilder().addQueryField("phone", "15011571302");
        SimpleHttpClient.SimpleRequestBuilder requestBuilder = new SimpleHttpClient.FormDataBuilder()
                .addFormField("to", to)
                .addFormField("subject", subjuct)
                .addFormField("content", content)
                .addFormField("from", from)
                .addHeaders(SimpleHttpClient.getDefaultHeaders());

        SimpleHttpClient.request(url, method, isMultipart, urlBuilder, requestBuilder, new SimpleHttpClient.StringHttpCallback() {
            @Override
            public void onSuccess(String response) {
                Log.e("jssssssss", "发邮件，返回：" + response);
            }

            @Override
            public void onFail(Exception e) {
                if(e instanceof SimpleHttpClient.HttpErrorException){
                    SimpleHttpClient.HttpErrorException ee = (SimpleHttpClient.HttpErrorException) e;
                    System.out.println(ee.getResponseCode() + ", " + ee.getResponseBody());
                }else{
                    e.printStackTrace();
                }
            }
        });
    }
}
