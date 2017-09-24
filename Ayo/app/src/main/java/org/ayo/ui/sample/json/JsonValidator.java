package org.ayo.ui.sample.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.ayo.core.Lang;
import org.ayo.core.Strings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by qiaoliang on 2017/3/28.
 *
 * Json格式检查工具，用于检查给定的json串是否符合特定的格式，
 * 格式由json-dl来描述，本身也是个json串
 */

public class JsonValidator {

    public interface ActionCallback{
        void onShitDetected(String fieldName, String json, String errorMsg, int arrayIndex);
    }

    public static class DLModel{
        public String type;
        public String contraint;
        public DLModel ref;  //如果type是object或array，这个就是对应的对象或item的dl
        public List<String> enums; ///如果type是int或string，这个就可能是值的范围


    }

    /**
     * 检查json里的每一个字段是否符合jsonDL里指定的格式
     *
     * 返回一个json串，是原始json删除了非法字段之后的结果
     *
     * 每发现一个非法字段，就会走一次回调
     *
     * @param json
     * @param jsonDL
     */
    public static String checkObject(String json, String jsonDL, ActionCallback c, int arrayIndex){
        //Map<String, Object> map = JSON.parseObject(json, Map.class);
        JSONObject dls = JSON.parseObject(jsonDL);
        Set<String> dlKeys = dls.keySet();

        JSONObject jsonOri = JSON.parseObject(json);
        Set<String> oriKeys = dls.keySet();

        Map<String, String> res = new HashMap<>();

        ///对于map里的每一个字段，都
        for(String key: oriKeys){
            String value = jsonOri.getString(key);

            ///先看看dl中有没有这个key的描述
            if(!dls.containsKey(key)){
                c.onShitDetected(key, value, "没有关于" + key + "的格式描述", arrayIndex);
                continue;
            }

            ///取出DLModel
            DLModel dl = JSON.parseObject(dls.get(key).toString(), DLModel.class);

            ///单字段check
            /*
            name:{
                type: string,
                contrain: nullable,
            },
            gener:{
                type: string,
                contrain: nullable,
                enum:[
                    男，女
                ]
            },
            sign: {
                type: object,
                ref: {
                    ///这里应该是一个完整的dl
                },
            }
            photo: {
                type: array,
                ref:{
                    type: object,
                    ref: SignInfo
                }
            }
             */
            if(dl.type.equals("object")){
                /*
                sign: {
                    type: object,
                    ref: {
                        ///这里应该是一个完整的dl
                    },
                }
                 */

                if(!value.startsWith("{")){
                    c.onShitDetected(key, value, "这里要的是个object", arrayIndex);
                    continue;
                }

                if(dl.ref == null){
                    c.onShitDetected(key, value, "dl文件描述不完整，缺少ref", arrayIndex);
                    continue;
                }

                {
                    String s = check(value, JSON.toJSONString(dl.ref), c, arrayIndex);
                    if(Lang.isNotEmpty(s)) res.put(key, s);
                }
            }else if(dl.type.equals("array")){
                /*
                photo: {
                    type: array,
                    ref:{
                        name:{
                        },
                        id:{
                        }
                    }
                }
                 */
                if(!value.startsWith("[")){
                    c.onShitDetected(key, value, "这里要的是个array", arrayIndex);
                    continue;
                }

                if(dl.ref == null){
                    c.onShitDetected(key, value, "dl文件描述不完整，缺少ref，那就不知道array的元素的格式了", arrayIndex);
                    continue;
                }

                {
                    String s = check(value, "[" + JSON.toJSONString(dl.ref) + "]", c, arrayIndex);
                    if(Lang.isNotEmpty(s)) res.put(key, s);
                }
            }else{
                ///其他基本类型
                if(dl.type.equals("int")){
                    if(!Strings.isDigital(value)){
                        c.onShitDetected(key, value, "不是合法的int", arrayIndex);
                        continue;
                    }else{
                        res.put(key, value);
                    }
                }else if(dl.type.equals("string")){
                    res.put(key, value);
                }else{
                    res.put(key, value);
                }
            }

        }

        return JSON.toJSONString(res);
    }



    public static String check(String json, String jsonDL, ActionCallback c, int arrayIndex){

        if(json.charAt(0) != jsonDL.charAt(0)){
            c.onShitDetected("json-root", json, "需要" + jsonDL.charAt(0) + ", 但json串给的是" + json.charAt(0), -1);
            if(jsonDL.charAt(0) == '[') return "[]";
            else return "{}";
        }

        if(json.startsWith("[")){
            if(json.equals("[]")) return json;
            JSONArray arr = JSON.parseArray(json);
            List<String> list = new ArrayList<>(arr.size());
            for (int i = 0; i < arr.size(); i++) {
                list.add(arr.get(i).toString());
            }

            String itemJsonDL = JSON.parseArray(jsonDL).get(0).toString();

            List<String> res = new ArrayList<>();
            int count = 0;
            for(String jsn: list){
                String s = check(jsn, itemJsonDL, c, count);
                if(Lang.isNotEmpty(s)) res.add(s);
                count++;
            }
            return JSON.toJSONString(res);
        }else{
            if(json.equals("{}")) return json;
            return checkObject(json, json, c, -1);
        }
    }

}
