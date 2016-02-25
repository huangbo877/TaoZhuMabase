package com.ruiyu.taozhuma.utils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.ruiyu.taozhuma.model.BaseModel;
import com.ruiyu.taozhuma.model.TzmOrderDetailModel;

public class JsonUtils {

	/**
	 * 得到success的结果
	 * 
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static boolean getSuccessResult(String json) {

		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(json);
			if (jsonObject.optString("success").equals("true")) {
				return true;
			} else {
				return false;
			}
		} catch (JSONException e) {
			return false;
		}

	}

	/**
	 * 得到服务器端返回来的错误信息
	 * 
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static String getErrorMsg(String json) {
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(json);
			return jsonObject.getString("error_msg");
		} catch (JSONException e) {
			return "App Error";
		}
	}

	/**
	 * 获取result信息
	 * @param json
	 * @return
	 */
	public static String getResultInfo(String json) {
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(json);
			if (jsonObject.optString("result").equals("null")) {
				return "";
			}
			return jsonObject.optString("result").toString();
		} catch (JSONException e) {
			return "";
		}
		
	}

	public static String getParamByKey(String json, String key) {
		LogUtil.Log("Json:" + json);
		LogUtil.Log("Key:" + key);
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(json);
			LogUtil.Log(jsonObject.getString(key));
			if (jsonObject.optString(key).equals("null")) {
				return "";
			}
			return jsonObject.optString(key).toString();
		} catch (JSONException e) {
			LogUtil.ErrorLog(e);
			return "";
		}
	}

	/**
	 * 将字符串转成 普通json 对象 ,webview 专用
	 * 截取 web超链接返回的json 参数
	 * @throws JSONException
	 * @throws UnsupportedEncodingException
	 * i表示开始截取的位置
	 * @throws Exception 
	 */
//	public static String StrToJson(int i,String jsonStr) throws JSONException,
//			UnsupportedEncodingException, Exception {
//		jsonStr=jsonStr.substring(i);  
//		String output = URLDecoder.decode(jsonStr, "UTF-8");
//		Log.i("JsonUtils", output);
//		Gson gson = new Gson();
////		Type type = new TypeToken<Sence2>() {
////		}.getType();
////		Sence2 base = gson.fromJson(output, type);
////		if(base.detail.equals("0")){
////			return base.sceneId;
////		}
////		return base.productId;
//        
//	}
}
