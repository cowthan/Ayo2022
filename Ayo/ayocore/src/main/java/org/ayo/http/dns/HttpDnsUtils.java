package org.ayo.http.dns;

import android.text.TextUtils;

import com.qiniu.android.dns.Domain;

import org.ayo.log.Trace;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Dns;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by hujinghui on 16/11/16.
 */

public class HttpDnsUtils {
    private final static String tag = "HttpDnsUtils";

    private static HttpUrl mHttpUrl;

    public static Interceptor getDnsInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                okhttp3.Request originRequest = chain.request();
                HttpUrl httpUrl = originRequest.url();
                if ("https".equalsIgnoreCase(httpUrl.scheme()))
                    return chain.proceed(originRequest);
                okhttp3.Request newRequest = getRequest(httpUrl, originRequest);
                Response newResponse = chain.proceed(newRequest);
                if (newResponse != null && newResponse.priorResponse() != null
                        && (newResponse.priorResponse().code() == 300 || newResponse.priorResponse().code() == 301 ||
                newResponse.priorResponse().code() == 302 || newResponse.priorResponse().code() == 303)) {
                    if (sameConnection(newResponse, mHttpUrl))
                        return newResponse;
                    mHttpUrl = newResponse.request().url();
                    okhttp3.Request redirectRequest = getRequest(newResponse.request().url(), newRequest);
                    return chain.proceed(redirectRequest);
                }
                return newResponse;
            }
        };
    }

    private static okhttp3.Request getRequest(HttpUrl httpUrl, okhttp3.Request request) throws IOException {
        String host = httpUrl.host();
        String[] array = QiniuDns.getDnsManager().query(host);
        String hostIP = null;
        if (array != null && array.length > 0) {
            hostIP = array[0];
        }
        okhttp3.Request.Builder builder = request.newBuilder();
        if (!TextUtils.isEmpty(hostIP)) {
            httpUrl = httpUrl.newBuilder().host(hostIP).build();
            Trace.d(tag, "new url:" + httpUrl.toString());
            builder.url(httpUrl);
            builder.header("Host", host);
            builder.header("originalUrl", httpUrl.toString());
        } else {
            Trace.e("HttpDNS", "can't get the ip , can't replace the host");
        }
        okhttp3.Request newRequest = builder.build();
        return newRequest;
    }

    private static boolean sameConnection(Response response, HttpUrl followUp) {
        HttpUrl url = response.request().url();
        return url != null && followUp != null
                && url.host().equals(followUp.host())
                && url.port() == followUp.port()
                && url.scheme().equals(followUp.scheme());
    }

    private static Dns getDns() {
        return new Dns() {
            @Override
            public List<InetAddress> lookup(String hostname) throws UnknownHostException {
                InetAddress[] ips;
                try {
                    ips = QiniuDns.getDnsManager().queryInetAdress(new Domain(hostname));
                    String ip = "";
                    for (InetAddress s : ips) {
                        ip += s.toString() + "     ";
                    }
                    Trace.d("HttpDNS", "hostname: " + hostname + "   InetAddress:" + ip);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new UnknownHostException(e.getMessage());
                }
                if (ips == null) {
                    throw new UnknownHostException(hostname + " resolve failed");
                }
                List<InetAddress> l = new ArrayList<>();
                Collections.addAll(l, ips);
                return l;
            }
        };
    }
}
