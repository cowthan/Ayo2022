package org.ayo.http.dns;

import com.qiniu.android.dns.DnsManager;
import com.qiniu.android.dns.IResolver;
import com.qiniu.android.dns.NetworkInfo;
import com.qiniu.android.dns.http.DnspodFree;
import com.qiniu.android.dns.local.AndroidDnsServer;
import com.qiniu.android.dns.local.Resolver;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by qiaoliang on 2017/6/23.
 */

public class QiniuDns {

    private static DnsManager dns;

    public static DnsManager getDnsManager() {
        if (dns == null)
            dns = initDns();
        return dns;
    }

    public static DnsManager initDns() {
        if (DnsManager.needHttpDns()) {
            IResolver[] resolvers = new IResolver[2];
            resolvers[0] = new DnspodFree();
            resolvers[1] = AndroidDnsServer.defaultResolver();
            return new DnsManager(NetworkInfo.normal, resolvers);
        } else {
            try {
                IResolver[] resolvers = new IResolver[2];
                resolvers[0] = AndroidDnsServer.defaultResolver();
                resolvers[1] = new Resolver(InetAddress.getByName("8.8.8.8"));
                return new DnsManager(NetworkInfo.normal, resolvers);
            } catch (UnknownHostException e) {
                e.printStackTrace();
                IResolver[] resolvers = new IResolver[1];
                resolvers[0] = AndroidDnsServer.defaultResolver();
                return new DnsManager(NetworkInfo.normal, resolvers);
            }
        }
    }

}
