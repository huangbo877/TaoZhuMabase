package com.ruiyu.taozhuma.utils;

import java.util.Set;

import com.ruiyu.taozhuma.config.AppConfig;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtils {

	private SharedPreferences settings;

	private static SharedPreferencesUtils self = null;
	
	public SharedPreferencesUtils(Context context) {
		settings = context.getSharedPreferences(AppConfig.SHARED_PREFERENCE_NAME, 0);
	}
	
	public static SharedPreferencesUtils getInstance(Context context) {
		if (self == null) {
			self = new SharedPreferencesUtils(context);
		}
		return self;
	}
	
	public void removeParam(String key){
		settings.edit().remove(key).commit();
	}
	
	/**
	 * 
	 * @param paramString  配置名称
	 * @param paramBoolean 配置值
	 */
	public void setBoolean(String paramString, boolean paramBoolean) {
		settings.edit().putBoolean(paramString, paramBoolean).commit();
	}
	
	public boolean getBoolean(String paramString, boolean defaultValue) {
		return settings.getBoolean(paramString, defaultValue);
	}
	
	/**
	 * 
	 * @param paramString
	 * @param paramValue
	 */
	public void setString(String paramString, String paramValue) {
		settings.edit().putString(paramString, paramValue).commit();
	}
	
	public String getString(String paramString, String defaultValue) {
		return settings.getString(paramString, defaultValue);
	}
	
	/**
	 * 
	 * @param paramString
	 * @param paramValue
	 */
	public void setInt(String paramString, int paramValue) {
		settings.edit().putInt(paramString, paramValue).commit();
	}
	
	public int getInt(String paramString, int defaultValue) {
		return settings.getInt(paramString,defaultValue);
	}
	
	/**
	 * 
	 * @param paramString
	 * @param paramValue
	 */
	public void setLong(String paramString, long paramValue) {
		settings.edit().putLong(paramString, paramValue).commit();
	}
	
	public long getLong(String paramString, long defaultValue) {
		return settings.getLong(paramString,defaultValue);
	}
	

}
