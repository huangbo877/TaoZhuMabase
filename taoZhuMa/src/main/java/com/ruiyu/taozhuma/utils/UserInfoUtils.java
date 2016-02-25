package com.ruiyu.taozhuma.utils;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ruiyu.taozhuma.base.BaseApplication;
import com.ruiyu.taozhuma.model.TzmCustomSearchModel;
import com.ruiyu.taozhuma.model.UserModel;

public class UserInfoUtils {
	public final static String USERINFO = "userinfo";
	public final static String CHANNELID = "channelId";
	public final static String FIRSTLOGIN = "firstlogin";
	public final static String SEARCHHISTORY = "searchhistory";

	/**
	 * 设置Device Token
	 * 
	 * @param info
	 */
	public static void setDeviceToken(String devicetoken) {
		BaseApplication.getInstance().sp.setString("devicetoken", devicetoken);
	}

	/**
	 * 获取Device Token
	 * 
	 * @param info
	 */
	public static String getDeviceToken() {
		return BaseApplication.getInstance().sp.getString("devicetoken", null);
	}

	/**
	 * 记录是否第一次打开
	 * 
	 * @param isFirst
	 */
	public static void setFirst(Boolean isFirst) {
		BaseApplication.getInstance().sp.setBoolean(FIRSTLOGIN, isFirst);
	}

	/**
	 * 是否第一次打开
	 * 
	 * @return
	 */
	public static Boolean getIsFirst() {
		Boolean isFirst = BaseApplication.getInstance().sp.getBoolean(
				FIRSTLOGIN, true);
		return isFirst;

	}


	/**
	 * 获取用户信息
	 * 
	 * @return
	 */
	public static UserModel getUserInfo() {
		String json = BaseApplication.getInstance().sp
				.getString(USERINFO, null);
		UserModel user = null;
		if (!StringUtils.isBlank(json)) {
			// json不为空
			Gson gson = new Gson();
			Type type = new TypeToken<UserModel>() {
			}.getType();
			try {
				user = gson.fromJson(json, type);
				BaseApplication.getInstance().setLoginUser(user);
			} catch (Exception e) {
				LogUtil.ErrorLog(e);
				return null;
			}
		}
		return user;

	}

	/**
	 * 设置用户信息
	 * 
	 * @param info
	 */
	public static void setUserInfo(UserModel info) {
		Gson gson = new Gson();
		String json = gson.toJson(info);
		BaseApplication.getInstance().sp.setString(USERINFO, json);
		BaseApplication.getInstance().setLoginUser(info);
	}

	/**
	 * 获取搜索历史缓存
	 * 
	 * @param info
	 */
	public static ArrayList<ArrayList<TzmCustomSearchModel>> getSearchHistory() {
		String json = BaseApplication.getInstance().sp.getString(SEARCHHISTORY,
				null);
		ArrayList<ArrayList<TzmCustomSearchModel>> list = null;
		if (!StringUtils.isBlank(json)) {
			// json不为空
			Gson gson = new Gson();
			Type type = new TypeToken<ArrayList<ArrayList<TzmCustomSearchModel>>>() {
			}.getType();
			try {
				list = gson.fromJson(json, type);
			} catch (Exception e) {
				LogUtil.ErrorLog(e);
				return new ArrayList<ArrayList<TzmCustomSearchModel>>();
			}
		}
		return list;

	}

	/**
	 * 设置搜索历史缓存
	 * 
	 * @param info
	 */
	public static void setSearchHistory(
			ArrayList<ArrayList<TzmCustomSearchModel>> list) {
		Gson gson = new Gson();
		String json = gson.toJson(list);
		BaseApplication.getInstance().sp.setString(SEARCHHISTORY, json);
	}

	/**
	 * 清空搜索历史缓存
	 * 
	 * 
	 * @param info
	 */
	public static void clearSearchHistory() {
		BaseApplication.getInstance().sp.removeParam(SEARCHHISTORY);
	}

	/**
	 * 用户是否登录
	 * 
	 * @return
	 */
	public static boolean isLogin() {
		if (BaseApplication.getInstance().getLoginUser() != null) {
			return true;
		} else {
			UserModel u = getUserInfo();
			return u == null ? false : true;
		}
	}

	/**
	 * 退出
	 */
	public static void signOut() {
		BaseApplication.getInstance().setLoginUser(null);
		BaseApplication.getInstance().sp.removeParam(USERINFO);
	}

	/**
	 * 更新用户信息
	 * 
	 * @param info
	 */
	public static void updateUserInfo(UserModel info) {
		signOut();
		setUserInfo(info);
	}
}
