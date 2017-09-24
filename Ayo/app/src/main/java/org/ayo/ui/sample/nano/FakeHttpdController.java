package org.ayo.ui.sample.nano;


import org.ayo.core.Lang;
import org.ayo.core.JsonTools;
import org.ayo.ui.sample.nano.tools.ScreenLockManager;

import java.util.Map;

/**
 * Created by qiaoliang on 2017/6/8.
 */

public class FakeHttpdController {


    private FakeHttpdController(){}

    private static final class H{
        private static final FakeHttpdController instance = new FakeHttpdController();
    }

    public static FakeHttpdController getDefault(){
        return H.instance;
    }

    public void initRoutes(){
        FakeHttpd.getDefault().addRoute("home", new FakeHttpd.FakeController() {
            @Override
            public FakeHttpd.FakeResponse handleRequest(String method, Map<String, String> queryParams, Map<String, String> formData, Map<String, String> formFile) {
                return null;
            }
        });

        FakeHttpd.getDefault().addRoute("error", new FakeHttpd.FakeController() {
            @Override
            public FakeHttpd.FakeResponse handleRequest(String method, Map<String, String> queryParams, Map<String, String> formData, Map<String, String> formFile) {
                throw new RuntimeException("不知道出了什么错。。。。");
            }
        });

        FakeHttpd.getDefault().addRoute("notify", new FakeHttpd.FakeController() {
            @Override
            public FakeHttpd.FakeResponse handleRequest(String method, Map<String, String> queryParams, Map<String, String> formData, Map<String, String> formFile) {
                return null;
            }
        });

        /**
         *  sceen-control?status=on
         *  sceen-control?status=off
         */
        FakeHttpd.getDefault().addRoute("screen", new FakeHttpd.FakeController() {
            @Override
            public FakeHttpd.FakeResponse handleRequest(String method, Map<String, String> queryParams, Map<String, String> formData, Map<String, String> formFile) {
                String status = queryParams.get("status");
                if(Lang.isEmpty(status)){
                    return new FakeHttpd.FakeResponse(200, "text/json", "少参数status");
                }else{
                    if(status.equals("on")){
                        ScreenLockManager.getDefault().turnScreenOn();
                    }else if(status.equals("off")){
                        ScreenLockManager.getDefault().unlockScreen();
                    }else{
                        return new FakeHttpd.FakeResponse(200, "text/json", "status参数的值无效：" + status);
                    }
                }

                return new FakeHttpd.FakeResponse(200, "text/json", JsonTools.toJson("ok"));
            }
        });
    }



}
