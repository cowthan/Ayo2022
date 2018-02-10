package org.ayo.http.impl;


import android.os.Handler;
import android.os.Looper;

import org.ayo.http.AyoRequest;
import org.ayo.http.AyoResponse;
import org.ayo.http.HttpWorker;
import org.ayo.http.OkHttp3BuilderManager;
import org.ayo.http.callback.BaseHttpCallback;
import org.ayo.http.callback.FailInfo;
import org.ayo.http.callback.ProgressRequestListener;
import org.ayo.http.callback.ProgressResponseListener;
import org.ayo.http.impl.progress.ProgressHelper;
import org.ayo.http.impl.progress.ProgressRequestBody;
import org.ayo.http.utils.HttpHelper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/8/16.
 */
public class OkHttp3Worker extends HttpWorker {

//    TypeToken typeToken;
    private OkHttpClient okHttpClient;
    private Handler handler;

//    public static void setDnsEnabled(boolean enable){
//        isDnsEnabled = enable;
//    }

    public OkHttp3Worker(){
        OkHttpClient.Builder okHttpClientBuilder = OkHttp3BuilderManager.getOkHttpClientBuilder();
        okHttpClient = okHttpClientBuilder.build();
        handler = new Handler(Looper.getMainLooper());
    }

//    private void init(AyoRequest request){
////        typeToken = request.token;
//    }

    private RequestBody getOkhttpRequestBody(final AyoRequest request, ProgressRequestListener progressRequestListener){
        if(request.method.equalsIgnoreCase("get")){
            return null;
        }else if(request.method.equalsIgnoreCase("head")){
            return null;
        }else{
            //post put delete patch都能传入RequestBody
            boolean hasStringEntity = (request.stringEntity != null && !request.stringEntity.equals(""));
            boolean postFileLikeForm = (request.files != null && request.files.size() > 0);
            boolean postFileLikeStream = (request.file != null);
            if(!hasStringEntity && !postFileLikeForm && !postFileLikeStream){
                //情况1：postForm
                request.mediaType("application/x-www-form-urlencoded");
                FormBody.Builder builder = new FormBody.Builder();
                if(request.params != null && request.params.size() > 0){
                    for(String key: request.params.keySet()){
                        builder.add(key, request.params.get(key));
                    }
                }
                return  builder.build();
            }else if(hasStringEntity){
                //情况2：postString
                if(request.mediaType == null || "".equals(request.mediaType)){
                    request.mediaType("application/json; charset=utf-8");
                }
                return RequestBody.create(MediaType.parse(request.mediaType), request.stringEntity);
            }else if(postFileLikeStream){
                //情况3：postFile--流形式，不带name，带mime
                if(request.mediaType == null || "".equals(request.mediaType)){
                    request.mediaType("application/json; charset=utf-8");
                }
                return RequestBody.create(MediaType.parse(request.mediaType), request.file);
            }else if(postFileLikeForm){
                //情况4：postFile--multipart形式，带name，带filename
                request.mediaType("multipart/mixed");
                MultipartBody.Builder builder = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM);
                if(request.params != null && request.params.size() > 0){
                    for(String key: request.params.keySet()){
                        builder.addFormDataPart(key, request.params.get(key));
                    }
                }

                if(request.files != null && request.files.size() > 0){
                    for(String key: request.files.keySet()){
                        File f = request.files.get(key);
                        builder.addFormDataPart(key, f.getName(), RequestBody.create(MediaType.parse(HttpHelper.getMediaType(f)), f));
                    }
                }
                RequestBody r = builder.build();
                if(progressRequestListener != null){
                    ProgressRequestBody body = new ProgressRequestBody(r, progressRequestListener);
                    return body;
                }else{
                    return r;
                }
            }else{
                return null;
            }
        }
    }

    private Request parseToOkHttpRequest(AyoRequest request, ProgressRequestListener progressRequestListener){
        String url = request.url;
        //基于OkHttpUtils辅助类
        Request.Builder builder = new Request.Builder()
                .url(url)
                .tag(request.tag);
        processHeader(builder, request.headers);

        //1 method决定了OkHttpRequestBuilder的哪个子类
        if(request.method.equalsIgnoreCase("get")){
            return builder.get().build();
        }else if(request.method.equalsIgnoreCase("head")){
            return builder.head().build();
        }else if(request.method.equalsIgnoreCase("post")){
            RequestBody body = getOkhttpRequestBody(request, progressRequestListener);
            return builder.post(body).build();
        }else if(request.method.equalsIgnoreCase("put")){
            RequestBody body = getOkhttpRequestBody(request, progressRequestListener);
            return builder.put(body).build();
        }else if(request.method.equalsIgnoreCase("delete")){
            RequestBody body = getOkhttpRequestBody(request, progressRequestListener);
            return builder.delete(body).build();
        }else if(request.method.equalsIgnoreCase("patch")){
            RequestBody body = getOkhttpRequestBody(request, progressRequestListener);
            return builder.patch(body).build();
        }else{
            throw new RuntimeException("使用了不支持的http谓词：" + request.method);
        }
    }

    private void processHeader(Request.Builder builder, Map<String, String> headers){
        if(headers != null && headers.size() > 0){
            for(String key: headers.keySet()){
                builder.addHeader(key, headers.get(key));
            }
        }
    }

    @Override
    public void fire(final AyoRequest request, final BaseHttpCallback<String> callback, ProgressRequestListener progressRequestListener, ProgressResponseListener progressResponseListener) {
//        init(request);
        Request req = parseToOkHttpRequest(request, progressRequestListener);

        OkHttpClient thisOkHttpClient = this.okHttpClient;
        if(progressResponseListener != null){
            OkHttpClient.Builder thisBuilder = this.okHttpClient.newBuilder();
            ProgressHelper.addProgressResponseListener(thisBuilder,progressResponseListener);
            thisOkHttpClient = thisBuilder.build();
        }

        Call call = thisOkHttpClient.newCall(req);
        tryToAddNewCall(call, req.tag());
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                e.printStackTrace();
                tryToRemoveCall(call);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        FailInfo failInfo = new FailInfo(-705, e.getMessage());
                        if(request.callback != null){
                            request.callback.onFinish(false, failInfo, null);
                        }
                    }
                });

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                tryToRemoveCall(call);
                final String respStr = response.body().string();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(!response.isSuccessful() ){
                            FailInfo failInfo = new FailInfo(response.code(),  respStr);
                            callback.onFinish(false,  failInfo, null);
                        }else{
                            AyoResponse r = new AyoResponse();
                            r.data = respStr;
                            if(callback != null){
                                callback.onFinish(true, null, r.data);
                            }
                        }
                    }
                });
            }
        });
    }

    private Map<String, String> getHeaderMap(Response r){
        Headers headers = r.headers();
        if(headers == null) return new HashMap<>();
        Map<String, String> map = new HashMap<>();
        for(String name: headers.names()){
            map.put(name, r.header(name));
        }
        return map;
    }

    @Override
    public AyoResponse fireSync(final AyoRequest request, ProgressRequestListener progressRequestListener, ProgressResponseListener progressResponseListener) {
//        init(request);
        Request req = parseToOkHttpRequest(request, progressRequestListener);

        OkHttpClient thisOkHttpClient = this.okHttpClient;
        if(progressResponseListener != null){
            OkHttpClient.Builder thisBuilder = this.okHttpClient.newBuilder();
            ProgressHelper.addProgressResponseListener(thisBuilder,progressResponseListener);
            thisOkHttpClient = thisBuilder.build();
        }

        Call call = thisOkHttpClient.newCall(req);
        try {
            tryToAddNewCall(call, req.tag());
            Response response = call.execute();
            tryToRemoveCall(call);
            if(!response.isSuccessful()){
                FailInfo failInfo = new FailInfo(response.code(),  response.networkResponse().body().string());
                AyoResponse r = new AyoResponse();
                r.failInfo = failInfo;
                return r;
            }else{
                AyoResponse r = new AyoResponse();
                r.data = response.body().string();
                return r;
            }

        } catch (IOException e) {
            e.printStackTrace();
            tryToRemoveCall(call);
            AyoResponse r = new AyoResponse();
            r.failInfo = new FailInfo(-705, e.getMessage());
            r.data = null;
            return r;
        }
    }

    private void handleOkHttpResponse(AyoRequest req, Response resp){

    }

    private void handleOkHttpException(AyoRequest req, IOException e){

    }



    private class CallToCancel{
        public Object tag;
        public Call call;
        public CallToCancel(Object tag, Call call){
            this.tag = tag;
            this.call = call;
        }
    }
    List<CallToCancel> firedCalls = new LinkedList<>();
    private Object lock = new Object();
    private void tryToRemoveCall(Call call){
        synchronized (lock){
            int indexWillBeRemove = -1;
            for(int i = 0; i < firedCalls.size(); i++){
                if(call == firedCalls.get(i).call){
                    indexWillBeRemove = i;
                    break;
                }
            }
            if(indexWillBeRemove >= 0 && indexWillBeRemove < firedCalls.size()){
                firedCalls.remove(indexWillBeRemove);
            }
        }
    }

    private void tryToAddNewCall(Call call, Object tag){
        synchronized (lock){
            firedCalls.add(new CallToCancel(tag, call));
        }
    }
    @Override
    public void cancelAll(Object tag) {
        synchronized (lock){
            for(int i = 0; i < firedCalls.size(); i++){
                if(tag != null && tag.equals(firedCalls.get(i).tag)){
                    Call call = firedCalls.get(i).call;
                    if(call != null) call.cancel();
                }
            }
        }
    }
}
