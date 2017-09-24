package org.ayo.update.demo;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

public final class JsonUtils {

	private JsonUtils() {
	}

	public static String toJson(Object bean){

		if(bean == null){
			return "{}";
		}
		return JSON.toJSONString(bean);
	}
	public static String toJsonPretty(Object bean){

		if(bean == null){
			return "{}";
		}
		return JSON.toJSONString(bean, true);
	}

	public static <T> List<T> getBeanList(String jsonArrayString, Class<T> cls) {

		List<T> beanList = new ArrayList<T>();
		beanList = JSON.parseArray(jsonArrayString, cls);
		return beanList;
	}

	public static <T> T getBean(String jsonString, Class<T> cls) {
		T t = null;
		t = JSON.parseObject(jsonString, cls);
		return t;
	}

}
