package org.ayo.core;

import android.util.Log;

import java.util.HashMap;

/**
 * Created by qiaoliang on 2017/4/12.
 */

public class EmailTools {




    public static void sendEmail(final String subjuct, final String content, final String to, final String from){
		/*
		http://114.215.81.196/service/mail/index.php
			?to=279800561@qq.com
			&subject=ssss
			&content=%E5%93%88%E5%93%88%E5%93%88%E5%93%88%E5%93%88%E5%93%88

		 */
        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> param = new HashMap<String, String>();
                param.put("to", to);
                param.put("subject", subjuct);
                param.put("content", content);
                param.put("from", from);
                String resp = HttpTools.postForm("http://114.215.81.196/service/mail/index.php", param, null);
                Log.e("jssssssss", "发邮件，返回：" + resp);

            }
        }).start();
    }


    public String sendEmailSync(String from, String to, String subject, String content){
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("to", to);
        param.put("subject", subject);
        param.put("content", content);
        param.put("from", from);
        String resp = HttpTools.postForm("http://114.215.81.196/service/mail/index.php", param, null);
        Log.e("jssssssss", "发邮件，返回：" + resp);
        return resp;
    }

}
