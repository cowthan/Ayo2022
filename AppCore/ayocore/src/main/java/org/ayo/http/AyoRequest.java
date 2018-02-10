package org.ayo.http;


import org.ayo.http.callback.BaseHttpCallback;
import org.ayo.http.callback.ProgressRequestListener;
import org.ayo.http.callback.ProgressResponseListener;
import org.ayo.http.utils.HttpHelper;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 */
public class AyoRequest {

    private AyoRequest(){
    }

    public static AyoRequest request(){
        AyoRequest r = new AyoRequest();
        return r;
    }

//    public TypeToken token;

    public long connectionTimeout = 30000;
    public long writeTimeout = 30000;
    public long readTimeout = 30000;

    public Map<String, String> params = new HashMap<String, String>();
    public Map<String, String> pathParams = new HashMap<String, String>();
    public Map<String, String> queryStrings = new HashMap<>();


    public Map<String, String> headers = new HashMap<String, String>();
    public Map<String, File> files = new HashMap<String, File>();

    public String stringEntity;
    public String mediaType;

    public File file;

    public String url = "";
    public String method = "get";
    public Object tag = "";
    public static HttpWorker worker;

//    public StreamConverter<?> streamConverter;
//    public TopLevelConverter topLevelConverter;
//    public ResponseConverter resonseConverter;
    public BaseHttpCallback<String> callback;

    public boolean isDnsOn = false;

    public AyoRequest dnsEnable(boolean enable){
        isDnsOn = enable;
        return this;
    }


    //---------------------------------------------------------------//
    public AyoRequest tag(Object tag){
        this.tag = tag;
        return this;
    }

    public AyoRequest connectionTimeout(long time){
        connectionTimeout = time;
        return this;
    }

    public AyoRequest writeTimeout(long time){
        writeTimeout = time;
        return this;
    }

    public AyoRequest readTimeout(long time){
        readTimeout = time;
        return this;
    }

    ///---------------
    public AyoRequest param(String name, String value){
        if(value == null) value = "";
        params.put(name, value);
        return this;
    }

    public AyoRequest queryString(String name, String value){
        if(value == null) value = "";
        queryStrings.put(name, value);
        return this;
    }

    public AyoRequest path(String name, String value){
        if(value == null) value = "";
        pathParams.put(name, value);
        return this;
    }

    //--------------

    private boolean uploadFile = false;
    private boolean needCompress = false;

    public AyoRequest param(String name, File value){
        if(this.files == null) this.files = new HashMap<String, File>();
        files.put(name, value);
        uploadFile = true;
        return this;
    }

    public AyoRequest file(File f){
        file = f;
        return this;
    }

    public AyoRequest mediaType(String mediaType){
        this.mediaType = mediaType;
        return this;
    }

    public AyoRequest mediaType(String mime, String charset){
        this.mediaType = mime + "; charset=" + charset;
        return this;
    }


    public AyoRequest header(String name, String value){
        if(this.headers == null) this.headers = new HashMap<String, String>();
        headers.put(name, value);
        return this;
    }

    ///------------
    public AyoRequest actionGet(){
        this.method = "get";
        return this;
    }

    public AyoRequest actionPost(){
        this.method = "post";
        return this;
    }

    public AyoRequest actionPut(){
        this.method = "put";
        return this;
    }

    public AyoRequest actionDelete(){
        this.method = "delete";
        return this;
    }

    public AyoRequest actionHead(){
        this.method = "head";
        return this;
    }

    public AyoRequest actionPatch(){
        this.method = "patch";
        return this;
    }

    ///--------------

    public AyoRequest stringEntity(String entity){
        this.stringEntity = entity;
        return this;
    }

    public AyoRequest url(String url){
        this.url = url;
        return this;
    }

    public AyoRequest worker(HttpWorker worker){
        AyoRequest.worker = worker;
        return this;
    }

//    public <T> AyoRequest streamConverter(StreamConverter<T> converter){
//        this.streamConverter = converter;
//        return this;
//    }
//
//    public AyoRequest topLevelConverter(TopLevelConverter converter){
//        this.topLevelConverter = converter;
//        return this;
//    }
//
//    public AyoRequest resonseConverter(ResponseConverter converter){
//        this.resonseConverter = converter;
//        return this;
//    }

    public AyoRequest callback(BaseHttpCallback<String> h){
        this.callback = h;
        return this;
    }

    public void fire(){
        fire(null, null);
    }

    public void fire(ProgressRequestListener progressRequestListener, ProgressResponseListener progressResponseListener){

        if(this.pathParams.size() > 0){
            for(String key: this.pathParams.keySet()){
                this.url = this.url.replace("{" + key + "}", this.pathParams.get(key) + "");
            }
        }

        this.url = HttpHelper.makeURL(this.url, this.queryStrings);
        worker.fire(this, this.callback, progressRequestListener, progressResponseListener);
    }

    public void fireSync(ProgressRequestListener progressRequestListener, ProgressResponseListener progressResponseListener){
        if(this.pathParams.size() > 0){
            for(String key: this.pathParams.keySet()){
                this.url = this.url.replace("{" + key + "}", this.pathParams.get(key) + "");
            }
        }

        this.url = HttpHelper.makeURL(this.url, this.queryStrings);

        worker.fireSync(this, progressRequestListener, progressResponseListener);
    }

    public static void cancelAll(Object tag){
        worker.cancelAll(tag);
    }

//    public <T> Observable<T> start(TypeToken<T> typeToken){
//        ObservableOnSubscribe<T> os = new ObservableOnSubscribe<T>() {
//            @Override
//            public void subscribe(final ObservableEmitter<T> e) throws Exception {
//                callback = new BaseHttpCallback<T>() {
//                    @Override
//                    public void onFinish(boolean isSuccess, HttpProblem problem, FailInfo resp, T t) {
//                        if(isSuccess){
//                            e.onNext(t);
//                        }else{
//                            AyoHttpException ae = new AyoHttpException();
//                            ae.failInfo = resp;
//                            ae.problem = problem;
//                            e.onError(ae);
//                        }
//                        e.onComplete();
//                    }
//                };
//                fire();
//            }
//        };
//        Observable<T> observable = Observable.create(os);
//        return observable;
//    }


}
