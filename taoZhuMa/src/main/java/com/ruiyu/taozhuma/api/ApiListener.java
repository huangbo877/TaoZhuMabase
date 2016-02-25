package com.ruiyu.taozhuma.api;

import java.lang.reflect.Type;
import java.util.ArrayList;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ruiyu.taozhuma.model.BaseModel;
import com.ruiyu.taozhuma.utils.LogUtil;
import com.ruiyu.taozhuma.utils.ProgressDialogUtil;

/**
 * API调用的事件监听器
 * @author Toby
 * @param <classType>
 * 
 */
public abstract class ApiListener {
	public abstract void onStart();
	/**
	 * API调用成功后返回值以json对象方式通知监听器
	 * @param json
	 */
	public abstract void onComplete(String jsonStr);
	/**
	 * 出现业务错误时通知监听器错误码及字错误码等信息
	 * @param error
	 */
	public void onError(String error) {
		//ProgressDialogUtil.closeProgressDialog();
		LogUtil.e(error);
	}
	/**
	 * 出现网络问题等未知异常时会回调此方法
	 * @param e
	 */
	public void onException(Exception e) {
		//ProgressDialogUtil.closeProgressDialog();
		LogUtil.ErrorLog(e);
	}
	
	
}
