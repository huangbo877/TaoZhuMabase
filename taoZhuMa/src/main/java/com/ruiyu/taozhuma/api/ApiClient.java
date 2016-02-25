package com.ruiyu.taozhuma.api;

import org.json.JSONException;
import org.json.JSONObject;

import com.ruiyu.taozhuma.config.AppConfig;
import com.ruiyu.taozhuma.http.WebUtils;
import com.ruiyu.taozhuma.utils.LogUtil;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

public class ApiClient {
	private Context context;
	private int connectTimeout = 10000;// 10秒
	private int readTimeout = 30000;// 30秒

	// private BaseApi api;
	private ApiListener listener;

	public ApiClient(Context context) {
		this.context = context;
	}

	/**
	 * 调用 API
	 * 
	 * @see TopApiListener
	 * @param params
	 *            系统及业务参数
	 * @param userId
	 *            需要使用哪个用户授权的access token来调用api，当API不需要session key时此参数可以为null
	 * @param listener
	 *            api调用回调处理监听器，不能为null
	 * @param async
	 *            true:异步调用；false:同步调用。Android 3.0以后会限制在UI主线程中同步访问网络，使用同步方式需谨慎
	 * @throws IllegalArgumentException
	 *             当参数<code>params</code>或<code>listener</code>为null时
	 */
	public void api(final BaseApi api, final ApiListener listener,
			final boolean async) {
		if (api == null) {
			throw new IllegalArgumentException("params must not null.");
		}
		if (listener == null) {
			throw new IllegalArgumentException("listener must not null.");
		}
		// this.api=api;
		this.listener = listener;
		if (this.listener == null) {
			LogUtil.Log("this.listener is null");
		} else {
			LogUtil.Log("this.listener not null");
		}
		if (async) {// 异步调用
			LogUtil.Log("异步调用");
			listener.onStart();

			new Thread() {
				@Override
				public void run() {
					// Looper.prepare();
					invokeApi(api, listener);
					// Looper.loop();
				}
			}.start();

		} else {// 同步
			invokeApi(api, listener);
		}

	}

	private void invokeApi(BaseApi api, ApiListener listener) {
		try {
			String jsonStr = WebUtils.doGet(context, api.getUrl(),
					api.getParamMap());
			LogUtil.Log(jsonStr);
			handleApiResponse(listener, jsonStr);
		} catch (Exception e) {
			Message msg = mHandler.obtainMessage();
			msg.obj = e;
			msg.what = AppConfig.API_HTTP_ONEXCEPTION;
			mHandler.sendMessage(msg);
		}
	}

	private void handleApiResponse(ApiListener listener, String jsonStr)
			throws JSONException {
		String error = parseError(jsonStr);
		if (error != null) {// failed
			Message msg = mHandler.obtainMessage();
			msg.obj = error;
			msg.what = AppConfig.API_HTTP_ONERROR;
			mHandler.sendMessage(msg);
		} else {
			Message msg = mHandler.obtainMessage();
			msg.obj = jsonStr;
			msg.what = AppConfig.API_HTTP_ONCOMPLETE;
			mHandler.sendMessage(msg);

		}
	}

	private String parseError(String jsonStr) throws JSONException {
		JSONObject json = new JSONObject(jsonStr);
		// LogUtil.Log(json.toString());
		boolean success = json.optBoolean("success");
		String msg = json.getString("error_msg");
		return success ? null : msg;
	}

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case AppConfig.API_HTTP_ONEXCEPTION:
				Exception e = (Exception) msg.obj;
				listener.onException(e);
				break;
			case AppConfig.API_HTTP_ONERROR:
				String error = (String) msg.obj;
				listener.onError(error);
				break;
			case AppConfig.API_HTTP_ONCOMPLETE:
				String jsonStr = (String) msg.obj;
				listener.onComplete(jsonStr);
				break;
			}
		}
	};
}
