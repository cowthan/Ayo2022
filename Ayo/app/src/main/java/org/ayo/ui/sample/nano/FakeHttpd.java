package org.ayo.ui.sample.nano;


import android.util.SparseArray;

import org.ayo.core.Lang;
import org.ayo.core.InetAddressUtils;
import org.ayo.core.JsonTools;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fi.iki.elonen.NanoHTTPD;

/**
 * Created by qiaoliang on 2017/6/4.
 *
 * apache服务器，假的，嵌入到安卓应用里的
 */

public class FakeHttpd {

    ///------------------------------------------
    /// 单例
    ///------------------------------------------
    private FakeHttpd(){

    }

    private static final class H{
        private static final FakeHttpd instance = new FakeHttpd();
    }

    public static FakeHttpd getDefault(){
        return H.instance;
    }

    ///------------------------------------------
    /// 服务器
    ///------------------------------------------
    private class HttpServer extends NanoHTTPD {

        public HttpServer(int port) {
            super(port);
        }

        @Override
        public NanoHTTPD.Response serve(IHTTPSession session) {

            Response r;
            try {
                Map<String, Object> requestData = new HashMap<>();

                // 请求方式
                String method = session.getMethod().name().toLowerCase();
                requestData.put("method", method); //Method.POST, Method.GET

                //请求的uri（不包含host和port）
                //如果请求是http://192.168.0.104:8092/type/dd?ss=ww，这里的值就是/type/dd
                String uri = session.getUri().toString();
                List<String> actions = new ArrayList<>();
                if(Lang.isNotEmpty(uri)){
                    if(uri.startsWith("/")) uri = uri.substring(1);
                    String[] arr = uri.split("/");
                    for(String s: arr){
                        actions.add(s);
                    }
                }
                requestData.put("uri", actions);

                //get参数
                // 如果请求是http://192.168.0.104:8092/type/dd?ss=ww&aa=1，则这里的值就是ss=ww&aa=1
                String body = session.getQueryParameterString();
                Map<String, String> ps = new HashMap<>();
                if(Lang.isNotEmpty(body)){
                    String[] arr = body.split("&");
                    for(String s: arr){
                        String[] aa = s.split("=");
                        if(aa.length == 1){
                            ps.put(aa[0], "");
                        }else if(aa.length == 2){
                            ps.put(aa[0], aa[1]);
                        }else{
                            //ignore
                        }
                    }
                }
                requestData.put("query-params", ps);

                //header
                Map<String, String> header = session.getHeaders();
                requestData.put("header", header);

                //上传的文件
                // 存的是name和临时路径：{"sdfsdf":"/data/data/org.ayo.sample_divider/cache/NanoHTTPD--1854564805"}
                Map<String, String> files = new HashMap<String, String>();
                session.parseBody(files);
                requestData.put("form-files", files);

                //post参数
                Map<String, List<String>> paramsd = session.getParameters();
                Map<String, String> ppp = new HashMap<>();
                if(Lang.isNotEmpty(paramsd)){

                    for(Map.Entry<String, List<String>> e: paramsd.entrySet()){
                        String key = e.getKey();
                        List<String> ll = paramsd.get(key);
                        if(ll == null || ll.size() == 0) {
                            ppp.put(key, "");
                        }else if(ll.size() == 1){
                            ppp.put(key, ll.get(0));
                        }else{
                            ppp.put(key, Lang.fromList(ll, ",", true));
                        }
                    }
                }
                requestData.put("form-datas", ppp);

                requestData.put("remote-ip", session.getRemoteIpAddress());
                requestData.put("remote-host", session.getRemoteHostName());

                FakeResponse resp = dispatch(requestData, actions, method, ps, ppp, files);

                if(resp == null){
                    r = newFixedLengthResponse(Response.Status.NOT_FOUND, "text/html", "The controller just return null....");
                }else{
                    r = newFixedLengthResponse(codes.get(resp.statusCode), resp.mediaType, resp.data);
                }

            } catch (IOException e) {
                e.printStackTrace();
                r = newFixedLengthResponse(Response.Status.INTERNAL_ERROR, "text/html", e.getMessage());
            } catch (ResponseException e) {
                e.printStackTrace();
                r = newFixedLengthResponse(Response.Status.INTERNAL_ERROR, "text/html", e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
                r = newFixedLengthResponse(Response.Status.INTERNAL_ERROR, "text/html", e.getMessage());
            }
            r.addHeader("Access-Control-Allow-Origin", "*");
            return r;
        }
    }

    HttpServer mHttpServer;
    private boolean isRunning = false;

    public boolean start(int port){
        if(isRunning) return true;
        mHttpServer = new HttpServer(port);
        try {
            mHttpServer.start();
            isRunning = true;
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            isRunning = false;
            return false;
        }
    }

    public void stop(){
        if(isRunning){
            if(mHttpServer != null) {
                mHttpServer.closeAllConnections();
                mHttpServer.stop();
                mHttpServer = null;
            }
        }
    }

    public boolean isRunning(){
        return isRunning && mHttpServer != null && mHttpServer.isAlive();
    }

    ///------------------------------------------
    /// 工具
    ///------------------------------------------
    public String getLocalIpAddress() {
        try {
            // 遍历网络接口
            Enumeration<NetworkInterface> infos = NetworkInterface
                    .getNetworkInterfaces();
            while (infos.hasMoreElements()) {
                // 获取网络接口
                NetworkInterface niFace = infos.nextElement();
                Enumeration<InetAddress> enumIpAddr = niFace.getInetAddresses();
                while (enumIpAddr.hasMoreElements()) {
                    InetAddress mInetAddress = enumIpAddr.nextElement();
                    // 所获取的网络地址不是127.0.0.1时返回得得到的IP
                    if (!mInetAddress.isLoopbackAddress()
                            && InetAddressUtils.isIPv4Address(mInetAddress
                            .getHostAddress())) {
                        return mInetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException e) {

        }
        return null;
    }

    ///------------------------------------------
    /// 转发请求--mvc路由
    ///------------------------------------------
    public static class FakeResponse{
        public String mediaType = "text/html";
        public String data = "";
        public int statusCode = 200;
        public FakeResponse(int statusCode, String mediaType, String data){
            this.mediaType = mediaType;
            this.data = data;
            this.statusCode = statusCode;
        }
    }

    private static SparseArray<NanoHTTPD.Response.IStatus> codes = new SparseArray<>();

    static{
        codes.put(101, NanoHTTPD.Response.Status.SWITCH_PROTOCOL);  //Switching Protocols
        codes.put(200, NanoHTTPD.Response.Status.OK);//OK
        codes.put(201, NanoHTTPD.Response.Status.CREATED);//Created
        codes.put(202, NanoHTTPD.Response.Status.ACCEPTED);//Accepted
        codes.put(204, NanoHTTPD.Response.Status.NO_CONTENT);//No Content
        codes.put(206, NanoHTTPD.Response.Status.PARTIAL_CONTENT);//Partial Content
        codes.put(207, NanoHTTPD.Response.Status.MULTI_STATUS);//Multi-Status

        codes.put(301, NanoHTTPD.Response.Status.REDIRECT);//Moved Permanently
        codes.put(302, NanoHTTPD.Response.Status.FOUND);//Found
        codes.put(303, NanoHTTPD.Response.Status.REDIRECT_SEE_OTHER);//See Other
        codes.put(304, NanoHTTPD.Response.Status.NOT_MODIFIED);//Not Modified
        codes.put(307, NanoHTTPD.Response.Status.TEMPORARY_REDIRECT);//Temporary Redirect

        codes.put(400, NanoHTTPD.Response.Status.BAD_REQUEST);//Bad Request
        codes.put(401, NanoHTTPD.Response.Status.UNAUTHORIZED);//Unauthorized
        codes.put(403, NanoHTTPD.Response.Status.FORBIDDEN);//Forbidden
        codes.put(404, NanoHTTPD.Response.Status.NOT_FOUND);//Not Found
        codes.put(405, NanoHTTPD.Response.Status.METHOD_NOT_ALLOWED);//Method Not Allowed
        codes.put(406, NanoHTTPD.Response.Status.NOT_ACCEPTABLE);//Not Acceptable
        codes.put(408, NanoHTTPD.Response.Status.REQUEST_TIMEOUT);//Request Timeout
        codes.put(409, NanoHTTPD.Response.Status.CONFLICT);//Conflict
        codes.put(410, NanoHTTPD.Response.Status.GONE);//Gone
        codes.put(411, NanoHTTPD.Response.Status.LENGTH_REQUIRED);//Length Required
        codes.put(412, NanoHTTPD.Response.Status.PRECONDITION_FAILED);//Precondition Failed"
        codes.put(413, NanoHTTPD.Response.Status.PAYLOAD_TOO_LARGE);//Payload Too Large
        codes.put(415, NanoHTTPD.Response.Status.UNSUPPORTED_MEDIA_TYPE);//Unsupported Media Type"
        codes.put(416, NanoHTTPD.Response.Status.RANGE_NOT_SATISFIABLE);//Requested Range Not Satisfiable
        codes.put(417, NanoHTTPD.Response.Status.EXPECTATION_FAILED);//Expectation Failed
        codes.put(429, NanoHTTPD.Response.Status.TOO_MANY_REQUESTS);//Too Many Requests

        codes.put(500, NanoHTTPD.Response.Status.INTERNAL_ERROR);//Internal Server Error
        codes.put(501, NanoHTTPD.Response.Status.NOT_IMPLEMENTED);//Not Implemented
        codes.put(503, NanoHTTPD.Response.Status.SERVICE_UNAVAILABLE);//Service Unavailable
        codes.put(505, NanoHTTPD.Response.Status.UNSUPPORTED_HTTP_VERSION);//HTTP Version Not Supported
    }


    public static abstract class FakeController{
        public abstract FakeResponse handleRequest(String method, Map<String, String> queryParams, Map<String, String> formData, Map<String, String> formFile);
    }

    private final Map<String, FakeController> routers = new HashMap<>();

    /**
     * 要想处理：http://192.168.0.104:8092/type/dd?ss=ww&dd=55555&sdf=都是谁
     * router应该传入type/dd
     * @param router
     * @param controller
     */
    public void addRoute(String router, FakeController controller){
        this.routers.put(router, controller);
    }

    public Set<String> getRoutes(){
        return routers.keySet();
    }

    private FakeResponse dispatch(Map<String, Object> request, List<String> uri, String method, Map<String, String> queryParams, Map<String, String> formData, Map<String, String> formFile){
        String route = Lang.fromList(uri, "/", true);

        if(route == null || route.equals("")){
            return new FakeResponse(200, "text/json", JsonTools.toJsonPretty(getRoutes()));
        }else if(route.equals("echo")){
            return new FakeResponse(200, "text/json", JsonTools.toJsonPretty(request));
        }

        if(routers.containsKey(route)){
            FakeController c = routers.get(route);
            if(c != null){
                return c.handleRequest(method, queryParams, formData, formFile);
            }else{
                return new FakeResponse(404, "text/html", "没有找到该控制器：" + route);
            }
        }else{
            return new FakeResponse(404, "text/html", "没有找到该请求：" + route);
        }
    }
}
