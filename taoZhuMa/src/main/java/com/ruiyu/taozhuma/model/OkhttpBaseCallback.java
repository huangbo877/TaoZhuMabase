package com.ruiyu.taozhuma.model;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.ruiyu.taozhuma.LogUtil;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.zhy.http.okhttp.callback.Callback;

/**
 * @author 林尧 2016-1-11
 * @param <T>
 */
public abstract class OkhttpBaseCallback<T> extends
		Callback<OkhttpBaseModel<T>> {
	
	@Override
	public OkhttpBaseModel<T> parseNetworkResponse(Response response)
			throws IOException {
	
		String string = response.body().string();
		LogUtil.Log("OkhttpBaseCallback", string);
		
		Gson gson = new Gson();
		Type type = new TypeToken<OkhttpBaseModel<T>>() {
		}.getType();
		
		OkhttpBaseModel<T> base = gson.fromJson(string, type);
		return base;
	}
	
	
	@Override
	public void onResponse(OkhttpBaseModel<T> arg0) {
		
		
	}
	
	@Override
	public void onError(Request arg0, Exception arg1) {
		
		
	}
	

}
