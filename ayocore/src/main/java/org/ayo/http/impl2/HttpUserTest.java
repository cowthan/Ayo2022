package org.ayo.http.impl2;


import static org.ayo.http.impl2.SimpleHttpClient.getDefaultHeaders;

/**
 * Created by qiaoliang on 2018/2/9.
 */

public class HttpUserTest {

    public static void main(String[] args){
        testVerifyCode();
//        testRegister();
//        testLogin();
    }

    private static final String host = "http://localhost:7888/tt201801/public/index.php/";

    public static void testVerifyCode(){
        String url = host + "user/request_code";
        SimpleHttpClient.HTTP_METHOD method = SimpleHttpClient.HTTP_METHOD.GET;
        boolean isMultipart = false;
        SimpleHttpClient.UrlBuilder urlBuilder = new SimpleHttpClient.UrlBuilder()
                .addQueryField("phone", "15011571302");
        SimpleHttpClient.SimpleRequestBuilder requestBuilder = new SimpleHttpClient.GetDataBuilder().addHeaders(getDefaultHeaders());
        //{"code":0,"msg":{"result":{"err_code":"0","model":"584818718189292646^0","msg":"OK","success":"true"},"request_id":"3bt5c4zdm09j"},"result":"{}"}
        //{"code":0,"msg":{"code":"15","msg":"Remote service error","sub_code":"isv.MOBILE_NUMBER_ILLEGAL","sub_msg":"22invalid mobile number","request_id":"iv103b7pacp4"},"result":"{}"}
        //请求频繁的返回：{"code":0,"msg":{"code":"15","msg":"Remote service error","sub_code":"isv.BUSINESS_LIMIT_CONTROL","sub_msg":"\u89e6\u53d1\u5206\u949f\u7ea7\u6d41\u63a7Permits:1","request_id":"iv1lx1bdt7y7"},"result":"{}"}
        SimpleHttpClient.request(url, method, isMultipart, urlBuilder, requestBuilder, new SimpleHttpClient.StringHttpCallback() {
            @Override
            public void onSuccess(String response) {
                System.out.println("接口返回：" + response);
            }

            @Override
            public void onFail(Exception e) {
                e.printStackTrace();
            }
        });
    }



    public static void testRegister(){
        String url = host + "user/v1/regist";
        SimpleHttpClient.HTTP_METHOD method = SimpleHttpClient.HTTP_METHOD.POST;
        boolean isMultipart = false;
        SimpleHttpClient.UrlBuilder urlBuilder = null; //new SimpleHttpClient.UrlBuilder().addQueryField("phone", "15011571302");
        SimpleHttpClient.SimpleRequestBuilder requestBuilder = new SimpleHttpClient.FormDataBuilder()
                            .addFormField("username", "15011571302")
                            .addFormField("password", "111111")
                            .addFormField("code", "270810")
                            .addFormField("nickname", "王二")
                            .addHeaders(getDefaultHeaders());
        //{"code":0,"msg":"ok","result":{"sid":"{80A80573-376B-C56D-186C-B9713C42FD07}"}}
        SimpleHttpClient.request(url, method, isMultipart, urlBuilder, requestBuilder, new SimpleHttpClient.StringHttpCallback() {
            @Override
            public void onSuccess(String response) {
                System.out.println("接口返回：" + response);
            }

            @Override
            public void onFail(Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static void testLogin(){
        String url = host + "user/login";
        SimpleHttpClient.HTTP_METHOD method = SimpleHttpClient.HTTP_METHOD.POST;
        boolean isMultipart = false;
        SimpleHttpClient.UrlBuilder urlBuilder = null; //new SimpleHttpClient.UrlBuilder().addQueryField("phone", "15011571302");
        SimpleHttpClient.SimpleRequestBuilder requestBuilder = new SimpleHttpClient.FormDataBuilder()
                .addFormField("username", "15011571302")
                .addFormField("password", "111111")
                .addHeaders(getDefaultHeaders());
        //{"code":0,"msg":"ok","result":{"info":"","id":17,"sid":"{80A80573-376B-C56D-186C-B9713C42FD07}"}}
        // {"code":1,"msg":"\u7528\u6237\u4e0d\u5b58\u5728"}

        SimpleHttpClient.request(url, method, isMultipart, urlBuilder, requestBuilder, new SimpleHttpClient.StringHttpCallback() {
            @Override
            public void onSuccess(String response) {
                System.out.println("接口返回：" + response);
            }

            @Override
            public void onFail(Exception e) {
                e.printStackTrace();
            }
        });
    }
}
