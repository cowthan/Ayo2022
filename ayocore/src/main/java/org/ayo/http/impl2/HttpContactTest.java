package org.ayo.http.impl2;


import static org.ayo.http.impl2.SimpleHttpClient.getDefaultHeaders;

/**
 * Created by qiaoliang on 2018/2/9.
 */

public class HttpContactTest {

    public static void main(String[] args){
//        testContactAdd();
//        testUpdate();
        testDelete();
//        testQueryDetail();
//        testQueryByKeyword();
    }

    private static final String host = "http://localhost:7888/tt201801/public/index.php/";

    public static void testContactAdd(){
        String url = host + "user/addContact";
        SimpleHttpClient.HTTP_METHOD method = SimpleHttpClient.HTTP_METHOD.POST;
        boolean isMultipart = false;
        SimpleHttpClient.UrlBuilder urlBuilder = null;
        SimpleHttpClient.SimpleRequestBuilder requestBuilder = new SimpleHttpClient.FormDataBuilder()
                .addFormField("user_id", "3")
                .addFormField("contact_id", "0") //传入就是修改，不传或者传0就是新建
                .addFormField("real_name", "王总")
                .addFormField("phone", "15011223344")
                .addFormField("birthday", "1998-03-04")
                .addFormField("gender", "1")  //1男  2女
                .addFormField("shengxiao", "狗")
                .addFormField("marriage", "1") // 1已婚 2未婚 3保密
                .addFormField("company", "北京理想三旬科技有限公司")
                .addFormField("department", "技术研发部")
                .addFormField("title", "安卓组开发经理")
                .addFormField("lnt", "16") //经度
                .addFormField("lat", "16") //纬度
                .addFormField("addr", "客户的公司地址")
                .addFormField("extra",
                        "{\"name\":\"value\", \"name2\":\"value2\"}")  //自定义字段：所有自定义字段放在json串里一块传过来

                .addHeaders(getDefaultHeaders())
                .addHeaderField("sid", "112233445544332211");
        //{"code":"0","msg":"ok","result":{"contact_id":6}}
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


    public static void testUpdate(){
        String url = host + "user/addContact";
        SimpleHttpClient.HTTP_METHOD method = SimpleHttpClient.HTTP_METHOD.POST;
        boolean isMultipart = false;
        SimpleHttpClient.UrlBuilder urlBuilder = null;
        SimpleHttpClient.SimpleRequestBuilder requestBuilder = new SimpleHttpClient.FormDataBuilder()
                .addFormField("user_id", "3")
                .addFormField("contact_id", "9") //传入就是修改，不传或者传0就是新建
                .addFormField("real_name", "王总2")
                .addFormField("phone", "15011223344")
                .addFormField("birthday", "1998-03-04")
                .addFormField("gender", "1")  //1男  2女
                .addFormField("shengxiao", "狗")
                .addFormField("marriage", "1") // 1已婚 2未婚 3保密
                .addFormField("company", "北京理想三旬科技有限公司")
                .addFormField("department", "技术研发部")
                .addFormField("title", "安卓组开发经理")
                .addFormField("lnt", "16") //经度
                .addFormField("lat", "16") //纬度
                .addFormField("addr", "客户的公司地址")
                .addFormField("extra",
                        "{\"name\":\"value\", \"name2\":\"value2\"}")  //自定义字段：所有自定义字段放在json串里一块传过来

                .addHeaders(getDefaultHeaders())
                .addHeaderField("sid", "112233445544332211");
        //{"code":"0","msg":"ok","result":{"contact_id":6}}
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

    public static void testDelete(){
        String url = host + "user/deleteContact";
        SimpleHttpClient.HTTP_METHOD method = SimpleHttpClient.HTTP_METHOD.POST;
        boolean isMultipart = false;
        SimpleHttpClient.UrlBuilder urlBuilder = null;
        SimpleHttpClient.SimpleRequestBuilder requestBuilder = new SimpleHttpClient.FormDataBuilder()
                .addFormField("contact_id", "9")
                .addHeaders(getDefaultHeaders())
                .addHeaderField("sid", "112233445544332211");
        //{"code":"0","msg":"ok","result":{"contact_id":6}}
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

    public static void testQueryDetail(){
        String url = host + "user/contactDetail";
        SimpleHttpClient.HTTP_METHOD method = SimpleHttpClient.HTTP_METHOD.GET;
        boolean isMultipart = false;
        SimpleHttpClient.UrlBuilder urlBuilder = new SimpleHttpClient.UrlBuilder()
                .addQueryField("contact_id", "1");
        SimpleHttpClient.SimpleRequestBuilder requestBuilder = new SimpleHttpClient.GetDataBuilder()
                .addHeaders(getDefaultHeaders())
                .addHeaderField("sid", "112233445544332211");
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

    public static void testQueryByKeyword(){
        String url = host + "user/listContact";
        SimpleHttpClient.HTTP_METHOD method = SimpleHttpClient.HTTP_METHOD.GET;
        boolean isMultipart = false;
        SimpleHttpClient.UrlBuilder urlBuilder = new SimpleHttpClient.UrlBuilder()
                .addQueryField("type", "full")
                .addQueryField("keyword", "王");
        SimpleHttpClient.SimpleRequestBuilder requestBuilder = new SimpleHttpClient.GetDataBuilder()
                .addHeaders(getDefaultHeaders())
                .addHeaderField("sid", "112233445544332211");
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
