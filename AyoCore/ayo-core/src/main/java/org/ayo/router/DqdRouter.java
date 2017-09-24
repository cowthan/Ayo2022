package org.ayo.router;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import org.ayo.core.Lang;
import org.ayo.log.Trace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by qiaoliang on 2017/6/17.
 *
 * 写个router，处理之前的router逻辑吧，需要考虑：
 * 1 router注册
 * 2 router拦截（是否需要登录，是否需要参数验证等）
 * 3 统计日志
 * 4 referer和当前scheme传入
 * 5 所有打开activity的操作，都走router
 *     5.1  应用内跳转，直接打开
 *     5.2  应用内h5打开，直接打开（shouldOverride。。。方法里处理）
 *     5.3  外部浏览器打开，通过中转activity打开
 *     5.4  打开activity for result的情况处理
 *
 *  怎么使用：
 *  DqdRouter.setDomain("dongqiudi://");
 *  DqdRouter.regist("/type/id", XXActivity.class, XXIntercepter);
 *  DqdRouter.regist("type/id", xx, xx);
 *  DqdRouter.regist("type/{id}", xx, xx);
 *
 *  打开：（从外部--接口传来的scheme，或者h5里）
 *  DqdRouter.want(scheme).open();
 *
 *  打开：（从内部，自己拼scheme呗）
 *  DqdRouter.want(XXActivity.class).withPath("path-parameter", "").withQuery("query-parameter", "").open();
 *
 *  问题：
 *  1 带path参数的uri怎么匹配
 */

public class DqdRouter {

    public static final String REFERER = "key-dqd-referer";
    public static final String CURRENT_SCHEME = "key-dqd-current-scheme";
    public static final String REDIRECT_SCHEME = "key-dqd-redirect";


    private DqdRouter(){}

    private static final class H{
        private static final DqdRouter instance = new DqdRouter();
    }

    public static DqdRouter getDefault(){
        return H.instance;
    }

    public interface Intercepter{
        /** 返回true，表示拦截了，不能继续前进了，界面也就打不开了 */
        boolean intercept(Intent intentTo, String fromScheme, Context fromContext, String schemeTo, RouterFoundInfo routerInfo);
    }

    public static class RouterPageInfo{
        private Class<?> activityClass;
        private String path;
        private List<Intercepter> intercepters = new ArrayList<>();

        public RouterPageInfo addIntercepter(Intercepter intercepter){
            intercepters.add(intercepter);
            return this;
        }
    }

    private String domain;
    private Map<String, RouterPageInfo> routers = new HashMap<>();
    private Map<Class<?>, RouterPageInfo> reversRouters = new HashMap<>();

    private Map<String, RouterFoundInfo> schemeRouterCahce = new HashMap<>();

    public RouterPageInfo regist(String path, Class<?> activityClass){
        RouterPageInfo p = new RouterPageInfo();
        p.path = path;
        p.activityClass = activityClass;

        routers.put(path, p);
        reversRouters.put(activityClass, p);

        return p;
    }

    public void setDomain(String domain){
        this.domain = domain;
    }

    public String route(String pathScheme){
        if(pathScheme.startsWith(domain)) return pathScheme;
        else return domain + pathScheme;
    }

    public RouterBuilder from(Context context, String currentScheme){
        RouterBuilder b = RouterBuilder.from(context, currentScheme);
        return b;
    }

    public RouterBuilder from(Context context){
        RouterBuilder b = RouterBuilder.from(context);
        return b;
    }

    public static class RouterFoundInfo{
        public String router;
        public RouterPageInfo pageInfo;
        public Map<String, String> pathParams;

        public RouterFoundInfo(String router, RouterPageInfo pageInfo, Map<String, String> pathParams) {
            this.router = router;
            this.pageInfo = pageInfo;
            this.pathParams = pathParams;
        }
    }

    public RouterFoundInfo findRouter(Class<?> activityClass){
        if(reversRouters.containsKey(activityClass)){
            RouterPageInfo r = reversRouters.get(activityClass);
            RouterFoundInfo rr = new RouterFoundInfo(r.path, r, new HashMap<String, String>());
            return rr;
        }
        return null;
    }

    public RouterFoundInfo findRouter(String scheme){
        if(schemeRouterCahce.containsKey(scheme)){
            //return schemeRouterCahce.get(scheme);
        }

        /// 找到activity class，这一步就是匹配path
        Uri uri = Uri.parse(scheme);
        String path = uri.getHost() + uri.getPath();
        Trace.e("router", "scheme是：" + scheme);
        Trace.e("router", "path是：" + path);
        Trace.e("router", "host是：" + uri.getHost());
        Trace.e("router", "Authority是：" + uri.getAuthority());
        Trace.e("router", "query是：" + uri.getQuery());
        if(path != null && path.endsWith("/")){
            path = Lang.trimEnd(path, "/");
        }

        /// path =        "type/subType/22323";
        /// 注册时的 path = type/subType/{id}

        /// 优先无path参数的
        if(routers.containsKey(path)){
            RouterFoundInfo rfi = new RouterFoundInfo(path, routers.get(path), null);
            schemeRouterCahce.put(scheme, rfi);
            return rfi;
        }

        /// 然后匹配带path参数的
        for(String key: routers.keySet()){
            Map<String, String> matchMap = match(key, path);
            if(matchMap == null) continue;
            else{
                RouterFoundInfo rfi = new RouterFoundInfo(key, routers.get(key), matchMap);
                schemeRouterCahce.put(scheme, rfi);
                return rfi;
            }
        }

        return null;
    }

    // router: type/{id}/page
    // path: type/234/page
    private Map<String, String> match(String router, String path){

        Uri uriRouter = Uri.parse(domain + router);
        Uri uriPath= Uri.parse(domain + path);


        if(!router.contains("{")){
            //无{}，表示没有path参数，只能直接判相等
            if(router.equals(path)){
                return new HashMap<>();
            }else{
                return null;
            }
        }else{
            List<String> routers = uriRouter.getPathSegments();
            List<String> paths = uriPath.getPathSegments();
            if(routers == null || paths == null){
                return null;
            }
            if(routers.size() != paths.size()){
                return null;
            }

            Map<String, String> map = new HashMap<>();
            for(int i = 0; i < routers.size(); i++){
                String r = routers.get(i);
                String p = paths.get(i);
                Trace.e("router", "尝试匹配：" + r + " <=> " + p);
                if(r.equals(p)){
                    continue;
                }else{
                    if(r.startsWith("{") && r.endsWith("}")){
                        // 无论p是什么，都可以匹配上，反正是path参数
                        String k = r.replace("{", "").replace("}", "");
                        map.put(k, p);

                        continue;
                    }else{
                        break;
                    }
                }

            }
            return map.size() == 0 ? null : map;
        }


    }


    public static class RouterBuilder{
        public Context fromContext;
        public String fromScheme;
        public String scheme = "";
        public Class<?> activityClass;
        public Intent intent;
        public RouterPageInfo pageInfo;
        public RouterFoundInfo foundInfo;
        private RouterBuilder(){

        }

        public static RouterBuilder from(Context context, String currentScheme){
            RouterBuilder r = new RouterBuilder();
            r.fromContext = context;
            r.fromScheme = currentScheme;
            return r;
        }

        public static RouterBuilder from(Context context){
            RouterBuilder r = new RouterBuilder();
            r.fromContext = context;
            r.fromScheme = context.getClass().getSimpleName();
            return r;
        }

        public RouterBuilder want(String scheme){
            this.scheme = scheme;

            /// 找到activity class，这一步就是匹配path
            RouterFoundInfo rfi = DqdRouter.getDefault().findRouter(scheme);
            if(rfi == null){
                ///？？？ 没找到路由！！！
                Trace.e("router", "router not found: " + scheme);
            }else{
                foundInfo = rfi;
                activityClass = rfi.pageInfo.activityClass;
                intent = new Intent(fromContext, activityClass);
                pageInfo = rfi.pageInfo;

                /// 加入path参数
                if(Lang.isNotEmpty(rfi.pathParams)){
                    for(String key: rfi.pathParams.keySet()){
                        intent.putExtra(key, rfi.pathParams.get(key));
                    }
                }

                /// 加入query参数
                Uri uri = Uri.parse(scheme);
                Set<String> names = uri.getQueryParameterNames();
                if(Lang.isNotEmpty(names)){
                    for(String name: names){
                        intent.putExtra(name, uri.getQueryParameter(name));
                    }
                }


            }
            return this;
        }

        public RouterBuilder want(Class<?> activityClass){

            ///不用试图通过activityClass找route，否则就没法一个页面配置多个route了
            this.activityClass = activityClass;
            this.intent = new Intent(fromContext, activityClass);
            RouterFoundInfo r = DqdRouter.getDefault().findRouter(activityClass);
            if(r == null){
                // 找不到，就这样了
                foundInfo = null;
                pageInfo = null;
                scheme = activityClass.getSimpleName();
            }else{
                foundInfo = r;
                pageInfo = r.pageInfo;
                scheme = DqdRouter.getDefault().domain + pageInfo.path;
            }

            return this;
        }

        public RouterBuilder addPathParam(String key, String value){
            if(intent == null){
                Trace.e("route", "~~没构造出有效的intent");
                return this;
            }

            if(foundInfo != null){
                foundInfo.pathParams.put(key, value);
                scheme = scheme.replace("{" + key + "}", value);
            }

            intent.putExtra(key, Lang.snull(value, ""));

            return this;
        }

        public RouterBuilder addParam(String key, String value){
            if(intent == null){
                Trace.e("route", "~~没构造出有效的intent");
                return this;
            }

            if(foundInfo != null){
                if(scheme.contains("?")){
                    scheme += "&" + key + "=" + value;
                }else{
                    scheme += "?" + key + "=" + value;
                }
            }

            intent.putExtra(key, Lang.snull(value, ""));
            return this;
        }


        public boolean open(){
            try {
                return _open();
            }catch (Exception e){
                e.printStackTrace();
                return false;
            }
        }

        private boolean _open(){

            if(intent == null){
                Trace.e("route", "没构造出有效的intent");
                return false;
            }else{
                /// 加入referer参数
                if(Lang.isNotEmpty(fromScheme)){
                    intent.putExtra(REFERER, fromScheme);
                }else{
                    intent.putExtra(REFERER, fromContext.getClass().getSimpleName());
                }

                /// 加入当前scheme
                if(Lang.isNotEmpty(fromScheme)){
                    intent.putExtra(CURRENT_SCHEME, scheme);
                }else{
                    intent.putExtra(CURRENT_SCHEME, scheme);
                }

            }

            if(pageInfo != null){
                if(Lang.isNotEmpty(pageInfo.intercepters)){
                    boolean intercept = false;
                    for(Intercepter intercepter: pageInfo.intercepters){
                        intercept = intercepter.intercept(intent, fromScheme, fromContext, scheme, foundInfo);
                    }
                    if(intercept){
                        return true;
                    }
                }
            }


            if(intent == null){
                Trace.e("route", "没构造出有效的intent");
                return false;
            }else{
                fromContext.startActivity(intent);
                return true;
            }
        }
    }


    public boolean isHttp(String uri) {
        String low = uri.toLowerCase();
        return low.startsWith("http://") || low.startsWith("https://");
    }
}
