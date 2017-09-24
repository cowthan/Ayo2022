package org.ayo.core;

import android.util.Log;

import org.ayo.AppCore;


/**
 * Created by qiaoliang on 2017/5/4.
 */

public class JsonReporter {

    private static final String EMAIL = "qiaoliang@dongqiudi.com";

    private static void sendEmail(String name, String content){
        EmailTools.sendEmail("json解析错误--" + name, content, EMAIL, "279800561@qq.com");
    }

    // -------------------------------------------------
    // 基于安卓自带org.json的json解析--带报错功能
    // -------------------------------------------------
    public static <T> void reportJsonIssue(String msgPrefix, String json, Class<T> clazz,
                                           boolean shouldBeList) {
        if (AppCore.isDebug()) {
            String msg = "";
            if (clazz == null) {
                msg = "传入class是null，没法聊天了！可能是待解析的model是泛型，且不是List";
            }
            if (json == null) {
                msg = "传入的json是null";
            } else if (json.equals("")) {
                msg = "传入的json是空串";
            } else if (!json.startsWith("{") && !json.startsWith("[")) {
                msg = "json既不是List，也不是Obj，根本就TM他妈不是json" + ", 原始json是：" + json;
            } else if (shouldBeList && !json.startsWith("[")) {
                msg = "要的是List，但json不是以[开头" + ", 原始json是：" + json;
            } else if (!shouldBeList && !json.startsWith("{")) {
                msg = "要的是Obj，但json不是以{开头" + ", 原始json是：" + json;
            } else if (shouldBeList) {
                final StringBuilder sb = new StringBuilder("");
                try {
                    JsonTools.getBeanList(json, clazz, new JsonTools.OnDecodeCallback() {
                        @Override
                        public void onDecodeFinish(boolean success, String name, String rawJson,
                                                   String errorReason) {
                            sb.append("解析错误，字段是(" + name + "), 原因: " + errorReason
                                    + ", 原始json是：" + JsonTools.formatJson(rawJson) + "\n" + "<br/>");
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    sb.append("遇见了Jsons本身的错误：" + e.getMessage() + ", 原始json：" + json);
                }
                msg = sb.toString();
            } else {
                final StringBuilder sb = new StringBuilder("");
                try {
                    JsonTools.getBean(json, clazz, new JsonTools.OnDecodeCallback() {
                        @Override
                        public void onDecodeFinish(boolean success, String name, String rawJson,
                                                   String errorReason) {
                            sb.append("解析错误，字段是(" + name + "), 原因: " + errorReason
                                    + ", 原始json是：<br/>" + JsonTools.formatJson(rawJson) + "\n"
                                    + "<br/><br/>");
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    sb.append("意料之外的Jsons本身的错误（本错误对你调试json没有任何帮助，不好意思）：" + e.getMessage());
                }
                msg = sb.toString();
            }

            String prettyJson = JsonTools.formatJson(json); // JSON.toJSONString(json,
            // true);
            reportSth("懂球帝：json解析错误",
                    msgPrefix + "<br/><br/>" + msg + "<br/><br/>json串：<br/><br/>" + prettyJson);
        }
    }

    public static void reportSth(String subject, String msg) {
        if (AppCore.isDebug()) {
            Log.e("error", msg);
            EmailTools.sendEmail(subject, msg, EMAIL, "279800561@qq.com");
        }
    }

    public static void reportSth2(String subject, String msg) {
        EmailTools.sendEmail(subject, msg, EMAIL, "279800561@qq.com");
    }

    public static void reportSth(String msg) {
        reportSth("懂球帝预警", msg);
    }

}
