package com.ruiyu.taozhuma.fragment;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.activity.TzmForgetPasswordActivity;
import com.ruiyu.taozhuma.api.ApiClient;
import com.ruiyu.taozhuma.api.ApiListener;
import com.ruiyu.taozhuma.api.TzmLoginApi;
import com.ruiyu.taozhuma.model.BaseModel;
import com.ruiyu.taozhuma.model.UserModel;
import com.ruiyu.taozhuma.utils.LogUtil;
import com.ruiyu.taozhuma.utils.MD5;
import com.ruiyu.taozhuma.utils.ProgressDialogUtil;
import com.ruiyu.taozhuma.utils.StringUtils;
import com.ruiyu.taozhuma.utils.ToastUtils;
import com.ruiyu.taozhuma.utils.UserInfoUtils;


import com.umeng.message.PushAgent;

import com.umeng.message.UmengRegistrar;
//import com.zhy.http.okhttp.OkHttpUtils;
//import com.zhy.http.okhttp.callback.Callback;

public class TzmLoginFragment extends Fragment {
	private View countLayout;
	private EditText et_phone, et_pass;
	private Button btn_login;
	private TextView tv_forget_password;

	private TzmLoginApi tzmLoginApi;
	private ApiClient apiClient;
	private UserModel userModel;
	private String phone, pass;

	private Context context;

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initView();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		countLayout = inflater.inflate(R.layout.tzm_login_fragment, container,
				false);
		return countLayout;
	}

	private void initView() {
		et_phone = (EditText) countLayout.findViewById(R.id.et_phone);
		et_pass = (EditText) countLayout.findViewById(R.id.et_pass);
		btn_login = (Button) countLayout.findViewById(R.id.btn_login);
		tv_forget_password = (TextView) countLayout
				.findViewById(R.id.tv_forget_password);
		tv_forget_password.setOnClickListener(clickListener);
		btn_login.setOnClickListener(clickListener);
	}

	public void login() {
		// 获取设备id
		String device_token = UserInfoUtils.getDeviceToken();
		if (StringUtils.isEmpty(device_token)) {
			// 推送服务再开一次
			PushAgent mPushAgent = PushAgent.getInstance(getActivity());
			mPushAgent.enable();
			device_token = UmengRegistrar.getRegistrationId(getActivity());
			if (StringUtils.isEmpty(device_token)) {
				ToastUtils.showShortToast(getActivity(),
						"设备注册中,请稍等,如果长时间注册未成功,请检查网络");
				return;
			} else {
				UserInfoUtils.setDeviceToken(device_token);
			}
		}

		apiClient = new ApiClient(getActivity());
		phone = et_phone.getText().toString();
		if (phone.equals("")) {
			ToastUtils.showShortToast(getActivity(), "请输入手机号码");
			return;
		}
		pass = et_pass.getText().toString();
		if (pass.equals("")) {
			ToastUtils.showShortToast(getActivity(), "请输入密码");
			return;
		}

		tzmLoginApi = new TzmLoginApi();
		tzmLoginApi.setPhone(phone);
		tzmLoginApi.setPass(MD5.MD5Encode(pass));
		tzmLoginApi.setBaidu_uid(device_token);
		//加密签名
		Map<String, String> map = new HashMap<String, String>();
		map.put("phone", phone);
		map.put("pass", MD5.MD5Encode(pass));
		map.put("baidu_uid", device_token);
		String sign = MD5.createLinkString(map);
		tzmLoginApi.setSign(sign);
		/*
		 * OkHttpUtils.post().url(tzmLoginApi.getUrl())
		 * .params(tzmLoginApi.getParamMap()).build() .execute(new
		 * Callback<BaseModel<UserModel>>() {
		 * 
		 * @Override public void onAfter() {
		 * ProgressDialogUtil.closeProgressDialog(); }
		 * 
		 * @Override public void onBefore(Request request) {
		 * ProgressDialogUtil.openProgressDialog(getActivity(), "", ""); }
		 * 
		 * @Override public void onError(Request arg0, Exception arg1) {
		 * ProgressDialogUtil.closeProgressDialog(); }
		 * 
		 * @Override public void onResponse(BaseModel<UserModel> arg0) { if
		 * (arg0.success) { userModel = arg0.result;
		 * UserInfoUtils.setUserInfo(userModel);// 保存用户登陆信息
		 * Toast.makeText(getActivity(), arg0.error_msg,
		 * Toast.LENGTH_SHORT).show(); Intent intentUserinfo = new Intent();
		 * intentUserinfo.putExtra("uid", userModel.uid);
		 * intentUserinfo.putExtra("name", userModel.name);
		 * intentUserinfo.putExtra("type", userModel.type);
		 * intentUserinfo.putExtra("status", userModel.status);
		 * intentUserinfo.putExtra("agencyId", userModel.agencyId);
		 * getActivity().setResult(Activity.RESULT_OK, intentUserinfo);
		 * InputMethodManager imm = (InputMethodManager) context
		 * .getSystemService(context.INPUT_METHOD_SERVICE);
		 * imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
		 * getActivity().finish(); } else { LogUtil.Log(arg0.error_msg); }
		 * 
		 * }
		 * 
		 * @Override public BaseModel<UserModel> parseNetworkResponse( Response
		 * arg0) throws IOException {
		 * 
		 * LogUtil.Log("响应头", arg0.headers().toString());
		 * 
		 * String jsonStr = arg0.body().string(); Gson gson = new Gson(); Type
		 * type = new TypeToken<BaseModel<UserModel>>() { }.getType();
		 * BaseModel<UserModel> base = gson .fromJson(jsonStr, type); return
		 * base; } });
		 */

		apiClient.api(tzmLoginApi, new ApiListener() {

			@Override
			public void onStart() {
				ProgressDialogUtil.openProgressDialog(getActivity(), "", "");
			}

			@Override
			public void onException(Exception e) {
				LogUtil.ErrorLog(e);
				ProgressDialogUtil.closeProgressDialog();
			}

			@Override
			public void onError(String error) {
				ProgressDialogUtil.closeProgressDialog();
				ToastUtils.showShortToast(getActivity(), error);
			}

			@SuppressWarnings("static-access")
			@Override
			public void onComplete(String jsonStr) {
				ProgressDialogUtil.closeProgressDialog();
				try {
					Gson gson = new Gson();
					Type type = new TypeToken<BaseModel<UserModel>>() {
					}.getType();
					BaseModel<UserModel> base = gson.fromJson(jsonStr, type);
					if (base.success) {
						userModel = base.result;
						UserInfoUtils.setUserInfo(userModel);// 保存用户登陆信息
						ToastUtils.showShortToast(getActivity(), base.error_msg);
						Intent intentUserinfo = new Intent();
						intentUserinfo.putExtra("uid", userModel.uid);
						intentUserinfo.putExtra("name", userModel.name);
						intentUserinfo.putExtra("type", userModel.type);
						intentUserinfo.putExtra("status", userModel.status);
						intentUserinfo.putExtra("agencyId", userModel.agencyId);
						getActivity().setResult(Activity.RESULT_OK,
								intentUserinfo);
						InputMethodManager imm = (InputMethodManager) context
								.getSystemService(context.INPUT_METHOD_SERVICE);
						imm.toggleSoftInput(0,
								InputMethodManager.HIDE_NOT_ALWAYS);
						getActivity().finish();
					} else {
						LogUtil.Log(base.error_msg);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}, true);

	}

	View.OnClickListener clickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tv_forget_password:
				Intent intent1 = new Intent(getActivity(),
						TzmForgetPasswordActivity.class);
				startActivity(intent1);

				break;
			case R.id.btn_login:
				login();
				break;

			}
		}
	};

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		context = getActivity();
	}

}
