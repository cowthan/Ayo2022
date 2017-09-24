package org.ayo.sp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import org.ayo.AppCore;
import org.ayo.http.utils.JsonUtils;

import java.util.List;


@SuppressLint("NewApi")
public class UserDefault {
	private SharedPreferences sp;
	private static UserDefault instance;
	private UserDefault(Context context){
		sp = DbSharedPreferences.getInstance(context);
	}
	
	public static UserDefault getInstance(){
		if(instance == null) instance = new UserDefault(AppCore.app());
		return instance;
	}
	
	public void put(String name, boolean value){
		sp.edit().putBoolean(name, value).apply();
	}
	public boolean get(String name, boolean defaultValue){
		return sp.getBoolean(name, defaultValue);
	}
	
	public void put(String name, String value){
		sp.edit().putString(name, value).apply();
	}
	public String get(String name, String defaultValue){
		return sp.getString(name, defaultValue);
	}
	public void put(String name, int value){
		sp.edit().putInt(name, value).apply();
	}
	public int get(String name, int defaultValue){
		return sp.getInt(name, defaultValue);
	}
	public void put(String name, double value){
		sp.edit().putFloat(name, (float)value).apply();
	}
	public double get(String name, double defaultValue){
		return sp.getFloat(name, (float)defaultValue);
	}

	public static <T> void putObject(String key, T t){
		if(t == null) return;
		String json = JsonUtils.toJson(t);
		getInstance().put(key, json);
	}
	
	public static <T> T getObject(String key, Class<T> clazz){
		String json = getInstance().get(key, "{}");
		if(json.equals("{}")){
			return null;
		}
		T t = JsonUtils.getBean(json, clazz);
		return t;
	}
	
	public static <T> List<T> getList(String key, Class<T> clazz){
		String json = getInstance().get(key, "[]");
		List<T> t = JsonUtils.getBeanList(json, clazz);
		return t;
	}
	
}
