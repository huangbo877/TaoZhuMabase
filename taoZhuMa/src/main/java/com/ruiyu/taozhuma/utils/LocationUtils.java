package com.ruiyu.taozhuma.utils;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ruiyu.taozhuma.base.BaseApplication;
import com.ruiyu.taozhuma.model.CitiesModel;
import com.ruiyu.taozhuma.model.UserModel;

public class LocationUtils {
	public final static String Lat = "lat";// 纬度
	public final static String Lng = "lng";// 经度
	public final static String COUNTRY = "country";

	/**
	 * 获取省市信息
	 * 
	 * @return
	 */
	public static List<CitiesModel> getCountry() {
		String json = BaseApplication.getInstance().sp.getString(COUNTRY, null);
		List<CitiesModel> list = null;
		if (!StringUtils.isBlank(json)) {
			// json不为空
			Gson gson = new Gson();
			Type type = new TypeToken<List<CitiesModel>>() {
			}.getType();
			try {
				list = gson.fromJson(json, type);
			} catch (Exception e) {
				LogUtil.ErrorLog(e);
				return null;
			}
		}
		return list;

	}

	/**
	 * 保存省市信息
	 * 
	 * @param info
	 */
	public static void setCountry(List<CitiesModel> info) {
		Gson gson = new Gson();
		String json = gson.toJson(info);
		BaseApplication.getInstance().sp.setString(COUNTRY, json);
	}

	/**
	 * 获取经度
	 * 
	 * @return
	 */
	public static String getLng() {
		String lng = BaseApplication.getInstance().sp.getString(Lng, null);
		return lng;

	}

	/**
	 * 获取纬度
	 * 
	 * @return
	 */
	public static String getLat() {
		String lat = BaseApplication.getInstance().sp.getString(Lat, null);
		return lat;

	}

	/**
	 * 设置经度
	 * 
	 * @param info
	 */
	public static void setLng(String lng) {
		BaseApplication.getInstance().sp.setString(Lng, lng);
	}

	/**
	 * 设置经度
	 * 
	 * @param info
	 */
	public static void setLat(String lat) {
		BaseApplication.getInstance().sp.setString(Lat, lat);
	}

}
