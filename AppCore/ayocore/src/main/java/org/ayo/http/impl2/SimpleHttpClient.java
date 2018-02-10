
package org.ayo.http.impl2;

import android.os.Handler;
import android.os.Looper;

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by qiaoliang on 2018/2/7.
 */

public class SimpleHttpClient {

    public static final String boundary = "***AndroidMultipartBoundary" + System.currentTimeMillis() + "***";
    public static final String LINE_FEED = "\r\n";
    private static final int CONNECTION_TIMEOUT = 5000; // 建立连接超时时间 5s
    private static final int READ_TIMEOUT = 5000; // 数据传输超时时间 5s

    public enum HTTP_METHOD {
        GET, POST
    }

    public enum PROTOCOL_TYPE {
        HTTP, HTTPS
    }

    public interface SimpleRequestBuilder{
        void buildBeforeConnect(HttpURLConnection connection);
        void buildAfterConnect(HttpURLConnection connection) throws IOException;
    }

    public static class HttpErrorException extends RuntimeException{
        private int code;

        private String body;

        public HttpErrorException(int code, String body){
            this.code = code;
            this.body = body;
        }

        public int getResponseCode(){
            return code;
        }
        public String getResponseBody(){
            return body;
        }
    }


    public static class MytmArray implements X509TrustManager {
        public X509Certificate[] getAcceptedIssuers() {
            // return null;
            return new X509Certificate[] {};
        }

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
            // TODO Auto-generated method stub

        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
            // TODO Auto-generated method stub
            // System.out.println("cert: " + chain[0].toString() + ", authType: "
            // + authType);
        }
    }

    private static void trustAllHosts() {
        // Install the all-trusting trust manager
        TrustManager[] xtmArray = new MytmArray[] {
                new MytmArray()
        };

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, xtmArray, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            // TODO Auto-generated method stub
            return true;
        }
    };

    private static String httpAccess(String url, HTTP_METHOD method, boolean isMultipart, UrlBuilder urlBuilder, SimpleRequestBuilder requestBuilder) throws IOException {


         if(urlBuilder != null){
             url = urlBuilder.build(url);
         }

        String result = "";
        HttpURLConnection httpUrlCon = getUrlConnection(url, method, isMultipart);
        try {
            if(requestBuilder != null){
                requestBuilder.buildBeforeConnect(httpUrlCon);
            }

            httpUrlCon.connect();

            ///写数据
            ///写入参数
            if(requestBuilder != null){
                requestBuilder.buildAfterConnect(httpUrlCon);
            }

            // check the result of connection
            if (httpUrlCon.getResponseCode() == HttpURLConnection.HTTP_OK) {
                result = getResponse(httpUrlCon);
            }else{
                result = getErrorResponse(httpUrlCon);
                throw new HttpErrorException(httpUrlCon.getResponseCode(), result);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
            // 超时，如果需要处理超时，可以在这里写
            // 未知主机，UnknownHostException
        } finally {
            if (httpUrlCon != null) {
                httpUrlCon.disconnect(); // 断开连接
            }
        }
        return result;
    }

    private static <T> T httpAccess2(String url, HTTP_METHOD method, boolean isMultipart, UrlBuilder urlBuilder, SimpleRequestBuilder requestBuilder, SimpleHttpCallback<T> callback) throws IOException {


        if(urlBuilder != null){
            url = urlBuilder.build(url);
        }

        T result = null;
        HttpURLConnection httpUrlCon = getUrlConnection(url, method, isMultipart);
        try {
            if(requestBuilder != null){
                requestBuilder.buildBeforeConnect(httpUrlCon);
            }

            httpUrlCon.connect();

            ///写数据
            ///写入参数
            if(requestBuilder != null){
                requestBuilder.buildAfterConnect(httpUrlCon);
            }

            // check the result of connection
            if (httpUrlCon.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = httpUrlCon.getInputStream();
                try {
                    result = callback.handleResponse(inputStream);
                    return result;
                }catch (Exception e){
                    throw e;
                }finally {
                    inputStream.close();
                }
            }else{
                String res = getErrorResponse(httpUrlCon);
                throw new HttpErrorException(httpUrlCon.getResponseCode(), res);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
            // 超时，如果需要处理超时，可以在这里写
            // 未知主机，UnknownHostException
        } finally {
            if (httpUrlCon != null) {
                httpUrlCon.disconnect(); // 断开连接
            }
        }
    }

    public static String getResponse(HttpURLConnection connection) throws IOException {
        String content = "";
        InputStream in = connection.getInputStream();
        //Simplest way to get String from InputStream
        Scanner scanner = new Scanner(in, "UTF-8").useDelimiter("\\A"); //Scan to the end of file
        if (scanner.hasNext()) {
            content = scanner.next();
        }
        scanner.close();
        return content;
    }

    public static String getErrorResponse(HttpURLConnection connection) throws IOException {
        String content = "";
        InputStream in = connection.getErrorStream();
        //Simplest way to get String from InputStream
        Scanner scanner = new Scanner(in, "UTF-8").useDelimiter("\\A"); //Scan to the end of file
        if (scanner.hasNext()) {
            content = scanner.next();
        }
        scanner.close();
        return content;
    }

    public static HttpURLConnection getUrlConnection(String url, HTTP_METHOD method, boolean isMultipart) throws IOException {
        URL httpUrl = new URL(url);

        ///处理https
        PROTOCOL_TYPE mProtocolType = PROTOCOL_TYPE.HTTP;
        if (httpUrl.getProtocol().toLowerCase().equals("https")) {
            mProtocolType = PROTOCOL_TYPE.HTTPS;
        } else if (httpUrl.getProtocol().toLowerCase().equals("http")) {
            mProtocolType = PROTOCOL_TYPE.HTTP;
        }
        HttpURLConnection connection = (HttpURLConnection) httpUrl.openConnection();

        switch (mProtocolType) {
            case HTTP:
                break;
            case HTTPS:
                trustAllHosts();
                ((HttpsURLConnection)connection).setHostnameVerifier(DO_NOT_VERIFY);// 不进行主机名确认
                break;
            default:
                break;
        }


        connection.setConnectTimeout(CONNECTION_TIMEOUT);// 建立连接超时时间
        connection.setReadTimeout(READ_TIMEOUT);// 数据传输超时时间，很重要，必须设置。
        connection.setDoInput(true); // 向连接中写入数据
        connection.setDoOutput(true); // 从连接中读取数据
//        connection.setUseCaches(false); // 禁止缓存
//        connection.setInstanceFollowRedirects(true);
        connection.setRequestProperty("Charset", "UTF-8");
//        connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
//        connection.setRequestProperty("Accept-Encoding", "gzip, deflatee");
//        connection.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");
//        connection.setRequestProperty("Cache-Control", "zh-CN,zh;q=0.9,en;q=0.8max-age=0");
//        connection.setRequestProperty("Connection", "keep-alive");
//        connection.setRequestProperty("Host", "114.215.81.196");
//        connection.setRequestProperty("Upgrade-Insecure-Requests", "1");
//        connection.setRequestProperty("Cookie", "sfasdf");
//        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.140 Mobile Safari/537.36");

        if(method == HTTP_METHOD.GET){
            connection.setRequestMethod("GET");
        }else if(method == HTTP_METHOD.POST){
            connection.setRequestMethod("POST");
            if(isMultipart){
                connection.setRequestProperty("Content-Type",
                        "multipart/form-data; boundary=" + SimpleHttpClient.boundary);
            }else{
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            }
        }
        return connection;
    }



    /**
     * Allow makes custom streams. You need just make openStream method
     */
    public abstract static class BinaryField implements Closeable {
        String fileName;
        InputStream stream;

        public BinaryField(String fileName) {
            super();
            setFileName(fileName);
        }

        public InputStream getStream() throws IOException {
            if (stream != null) return stream;
            else
                return stream = openStream();
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String name) {
            fileName = name;
        }

        /**
         * This method allow open stream immediatelly when file sending.
         * If you have already opended stream, you can return him.
         * Then file was send, stream will be closed.
         * To avoid this behavior, override close() method
         *
         * @return your stream
         * @throws IOException
         */
        protected abstract InputStream openStream() throws IOException;

        @Override
        public void close() throws IOException {
            if (stream != null) stream.close();
        }
    }

    /**
     * FileInputStream implementation, for internal consumption
     */
    public static class FileField extends BinaryField {
        File file;

        public FileField(File file) {
            this(file, file.getName());
        }

        public FileField(File file, String name) {
            super(name);
            this.file = file;
        }

        @Override
        protected InputStream openStream() throws IOException {
            return new FileInputStream(file);
        }
    }
    /**
     * Implementation for byte arrays
     */
    public static class ByteArrayField extends BinaryField {
        byte[] mData;

        public ByteArrayField(String fileName, byte[] data) {
            super(fileName);
            mData = data;
        }

        @Override
        protected InputStream openStream() throws IOException {
            return new ByteArrayInputStream(mData);
        }
    }


    public static Map<String, String> getDefaultHeaders(){
        Map<String, String> headersFields = new HashMap<>();
        headersFields.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        headersFields.put("Accept-Encoding", "gzip, deflatee");
        headersFields.put("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");
        headersFields.put("Cache-Control", "zh-CN,zh;q=0.9,en;q=0.8max-age=0");
        headersFields.put("Connection", "keep-alive");
        headersFields.put("Host", "114.215.81.196");
        headersFields.put("Upgrade-Insecure-Requests", "1");
        headersFields.put("Cookie", "sfasdf");
        headersFields.put("User-Agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.140 Mobile Safari/537.36");
        return headersFields;
    }

    public static class HeaderBuilder implements SimpleHttpClient.SimpleRequestBuilder{

        protected HashMap<String, String> headersFields;

        /**
         * Simple class for post multipart data with HttpUrlConnection
         *
         * @throws IOException
         */
        public HeaderBuilder() {
            super();
            headersFields = new HashMap<>();


//            headersFields.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
//            headersFields.put("Accept-Encoding", "gzip, deflatee");
//            headersFields.put("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");
//            headersFields.put("Cache-Control", "zh-CN,zh;q=0.9,en;q=0.8max-age=0");
//            headersFields.put("Connection", "keep-alive");
//            headersFields.put("Host", "114.215.81.196");
//            headersFields.put("Upgrade-Insecure-Requests", "1");
//            headersFields.put("Cookie", "sfasdf");
//            headersFields.put("User-Agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.140 Mobile Safari/537.36");

        }

        /**
         * Adds header field to multipart data
         *
         * @param name  Field name
         * @param value Field value
         * @return self
         */
        public HeaderBuilder addHeaderField(String name, String value) {
            headersFields.put(name, value);
            return this;
        }

        public HeaderBuilder addHeaders(Map<String, String> header){
            headersFields.putAll(header);
            return this;
        }


        @Override
        public void buildBeforeConnect(HttpURLConnection connection) {
            for (Map.Entry<String, String> entry : headersFields.entrySet()) {
                connection.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }

        @Override
        public void buildAfterConnect(HttpURLConnection connection) throws IOException {

        }
    }

    /**
     * Builder for multipart data
     */
    public static class MultipartDataBuilder extends HeaderBuilder{

        HashMap<String, String> formFields;
        HashMap<String, BinaryField> formFiles;
        String charset;

        /**
         * Simple class for post multipart data with HttpUrlConnection
         *
         * @param charset    Charset. I most cases it's "UTF-8"
         * @throws IOException
         */
        public MultipartDataBuilder(String charset) {
            super();
            formFields = new HashMap<>();
            formFiles = new HashMap<>();
            this.charset = charset;
        }

        public MultipartDataBuilder() {
            this("UTF-8");
        }

        /**
         * Adds data field to multipart data
         *
         * @param name  Field name
         * @param value Field value
         * @return self
         */
        public MultipartDataBuilder addFormField(String name, String value) {
            formFields.put(name, value);
            return this;
        }

        /**
         * Add file field to multipart data. You need to override BinaryField for get stream
         *
         * @param name Field name
         * @param file Your implementation of BinaryField
         * @return self
         */
        public MultipartDataBuilder addFormFile(String name, SimpleHttpClient.BinaryField file) {
            formFiles.put(name, file);
            return this;
        }

        /**
         * Add file from filesystem to multipart data. It change file name to given by you
         *
         * @param name     Field name
         * @param file     Your file
         * @param fileName New file name
         * @return self
         */
        public MultipartDataBuilder addFormFile(String name, File file, String fileName) {
            formFiles.put(name, new SimpleHttpClient.FileField(file, fileName));
            return this;
        }

        /**
         * Add file from filesystem to multipart data. It leave file name from file system.
         *
         * @param name Field name
         * @param file Your file
         * @return self
         */
        public MultipartDataBuilder addFormFile(String name, File file) {
            formFiles.put(name, new SimpleHttpClient.FileField(file));
            return this;
        }

        /**
         * Add file from byte array
         * @param name Field name
         * @param data Your array
         * @param fileName New file name
         * @return
         */
        public MultipartDataBuilder addFormFile(String name, byte[] data, String fileName){
            formFiles.put(name, new SimpleHttpClient.ByteArrayField(fileName, data));
            return this;
        }

//        @Override
//        public void buildBeforeConnect(HttpURLConnection connection) {
//            for (Map.Entry<String, String> entry : headersFields.entrySet()) {
//                connection.setRequestProperty(entry.getKey(), entry.getValue());
//            }
//        }

        @Override
        public void buildAfterConnect(HttpURLConnection connection) throws IOException {
            OutputStream outputStream = null;
            PrintWriter writer = null;
            try {
                outputStream = connection.getOutputStream();
                writer = new PrintWriter(outputStream);

                //headers
//                for (Map.Entry<String, String> entry : headersFields.entrySet()) {
//                    writer.append(entry.getKey()).append(": ").append(entry.getValue()).append(HttpUtils.LINE_FEED);
//                }
                //fields
                StringBuilder body = new StringBuilder();
                for (Map.Entry<String, String> entry : formFields.entrySet()) {
                    writer.append("--").append(SimpleHttpClient.boundary).append(SimpleHttpClient.LINE_FEED);
                    writer.append("Content-Disposition: form-data; name=\"").append(entry.getKey()).append("\"").append(SimpleHttpClient.LINE_FEED);
                    writer.append("Content-Type: text/plain; charset=").append(charset).append(SimpleHttpClient.LINE_FEED);
                    writer.append(SimpleHttpClient.LINE_FEED);
                    writer.append(entry.getValue()).append(SimpleHttpClient.LINE_FEED);
                }
                //files
                for (Map.Entry<String, BinaryField> entry : formFiles.entrySet()) {
                    SimpleHttpClient.BinaryField binaryField = entry.getValue();
                    writer.append("--").append(SimpleHttpClient.boundary).append(SimpleHttpClient.LINE_FEED);
                    writer.append(
                            "Content-Disposition: form-data; name=\"").append(entry.getKey()).append("\"; filename=\"").append(binaryField.getFileName()).append("\"").append(SimpleHttpClient.LINE_FEED);
                    writer.append("Content-Type: ").append(URLConnection.guessContentTypeFromName(binaryField.getFileName())).append(SimpleHttpClient.LINE_FEED);
                    writer.append("Content-Transfer-Encoding: binary").append(SimpleHttpClient.LINE_FEED);
                    writer.append(SimpleHttpClient.LINE_FEED);
                    writer.flush();

                    InputStream inputStream = binaryField.getStream();
                    try {
                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                        }
                    } finally {
                        inputStream.close();
                    }
                    outputStream.flush();

                    writer.append(SimpleHttpClient.LINE_FEED);
                }


                writer.flush();

                writer.append("--").append(SimpleHttpClient.boundary).append("--").append(SimpleHttpClient.LINE_FEED);
                writer.close();

            } finally {
                if (outputStream != null) outputStream.close();
            }
        }
    }


    /**
     * Builder for multipart data
     */
    public static class FormDataBuilder extends HeaderBuilder{

        HashMap<String, String> formFields;

        /**
         * Simple class for post multipart data with HttpUrlConnection
         *
         */
        public FormDataBuilder() {
            super();
            formFields = new HashMap<>();
        }




        /**
         * Adds data field to multipart data
         *
         * @param name  Field name
         * @param value Field value
         * @return self
         */
        public FormDataBuilder addFormField(String name, String value) {
            formFields.put(name, value);
            return this;
        }


        @Override
        public void buildAfterConnect(HttpURLConnection connection) throws IOException {
            OutputStream outputStream = null;
            PrintWriter writer = null;
            try {
                outputStream = connection.getOutputStream();
                writer = new PrintWriter(outputStream);

//                //headers
//                for (Map.Entry<String, String> entry : headersFields.entrySet()) {
//                    writer.append(entry.getKey()).append(": ").append(entry.getValue()).append(HttpUtils.LINE_FEED);
//                }
                //fields
                String params = "";
                for (Map.Entry<String, String> entry : formFields.entrySet()) {
                    //name=Bob&age=18&weight=60
                    params += "&" + entry.getKey() + "=" + entry.getValue();
                }
                if(params.length() > 0){
                    params = params.substring(1);
                }

//                DataOutputStream out = new DataOutputStream(connection.getOutputStream()); // 获取输出流
                writer.write(params);// 将要传递的数据写入数据输出流,不要使用out.writeBytes(param);
                // 否则中文时会出错
                writer.flush();
                writer.close();

            } finally {
                if (outputStream != null) outputStream.close();
            }
        }
    }


    /**
     * Builder for multipart data
     */
    public static class GetDataBuilder extends HeaderBuilder{

        /**
         * Simple class for post multipart data with HttpUrlConnection
         *
         * @throws IOException
         */
        public GetDataBuilder() {
            super();
        }

    }


    /**
     * Builder for multipart data
     */
    public static class UrlBuilder{

        HashMap<String, String> queryFields;
        HashMap<String, String> pathyFields;

        /**
         * Simple class for post multipart data with HttpUrlConnection
         *
         * @throws IOException
         */
        public UrlBuilder() {
            super();
            queryFields = new HashMap<>();
            pathyFields = new HashMap<>();
        }

        /**
         * Adds data field to multipart data
         *
         * @param name  Field name
         * @param value Field value
         * @return self
         */
        public UrlBuilder addQueryField(String name, String value) {
            queryFields.put(name, value);
            return this;
        }

        public UrlBuilder addPathField(String name, String value) {
            pathyFields.put(name, value);
            return this;
        }

        public String build(String url){
            if(url == null) return "";

            //处理path参数：{id}
            for (Map.Entry<String, String> entry : pathyFields.entrySet()) {
                //name=Bob&age=18&weight=60
                url = url.replace("{" + entry.getKey() + "}", entry.getValue());
            }

            //处理query参数
            String params = "";
            String firstSeperator = (url.contains("?") ? "&" : "?");
            for (Map.Entry<String, String> entry : queryFields.entrySet()) {
                //name=Bob&age=18&weight=60
                params += "&" + entry.getKey() + "=" + entry.getValue();
            }
            if(params.length() > 0){
                params = params.substring(1);
                url = url + firstSeperator + params;
            }
            return url;
        }
    }


    private static ExecutorService ThreadPool = Executors.newFixedThreadPool(4);

    public static abstract class SimpleHttpCallback<T>{
        public void onSuccess(T response){}
        public void onFail(Exception e){}
        public void onOffline(){}

        private static final Handler Response_Handler = new Handler(Looper.getMainLooper());

        public T handleResponse(InputStream inputStream){
            return null;
        }

        public void dispatchResponse(Runnable runnable){
            /** 安卓的话，需要用handler在这里转发 */
            Response_Handler.post(runnable);
        }
    }

    public static abstract class StringHttpCallback extends SimpleHttpCallback<String>{
        @Override
        public String handleResponse(InputStream inputStream) {
            String content = "";
            //Simplest way to get String from InputStream
            Scanner scanner = new Scanner(inputStream, "UTF-8").useDelimiter("\\A"); //Scan to the end of file
            if (scanner.hasNext()) {
                content = scanner.next();
            }
            scanner.close();
            return content;
        }
    }

    public static abstract class DownloadHttpCallback extends SimpleHttpCallback<File>{
        @Override
        public File handleResponse(InputStream inputStream) {
            return null;
        }
    }

    public static <T> void request(final String url, final HTTP_METHOD method, final boolean isMultipart, final UrlBuilder urlBuilder, final SimpleRequestBuilder requestBuilder, final SimpleHttpCallback<T> callback){

        ThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    final T response = SimpleHttpClient.httpAccess2(url,
                            method,
                            isMultipart,
                            urlBuilder,
                            requestBuilder, callback);
                    //System.out.println("响应：" + response);

                    callback.dispatchResponse(new Runnable() {
                        @Override
                        public void run() {
                            callback.onSuccess(response);
                        }
                    });
                } catch (final Exception e) {
                    callback.dispatchResponse(new Runnable() {
                        @Override
                        public void run() {
                            callback.onFail(e);
                        }
                    });
                }
            }
        });


    }

    public static void main(String[] args){

//        String host = "http://114.215.81.196/tt2018/public/index.php/";
        String host = "http://localhost:7888/tt201801/public/index.php/";

        ///multipart
        ///上传头像
        String url = host + "user/avatar";
        HTTP_METHOD method = HTTP_METHOD.POST;
        boolean isMultipart = true;
        UrlBuilder urlBuilder = null;
        SimpleRequestBuilder requestBuilder = new MultipartDataBuilder()
                .addFormField("user_id", "3")
                .addFormFile("avatar", new File("/Users/seven/Documents/desktt.jpg"))
                .addHeaders(getDefaultHeaders())
                .addHeaderField("sid", "112233445544332211");
        request(url, method, isMultipart, urlBuilder, requestBuilder, new StringHttpCallback() {
            @Override
            public void onSuccess(String response) {
                System.out.println("接口返回：" + response);
            }

            @Override
            public void onFail(Exception e) {
                e.printStackTrace();
            }
        });

        ///上传头像


    }
}
