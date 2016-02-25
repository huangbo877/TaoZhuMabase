package com.ruiyu.taozhuma.fragment;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alipay.android.phone.mrpc.core.t;
import com.geetest.gt_sdk.GeetestLib;
import com.geetest.gt_sdk.GtDialog;
import com.geetest.gt_sdk.GtDialog.GtListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.api.ApiClient;
import com.ruiyu.taozhuma.api.ApiListener;
import com.ruiyu.taozhuma.api.TzmCaptchaApi;
import com.ruiyu.taozhuma.api.TzmRegisterApi;
import com.ruiyu.taozhuma.config.AppConfig;
import com.ruiyu.taozhuma.model.BaseModel;
import com.ruiyu.taozhuma.model.TzmCaptchaModel;
import com.ruiyu.taozhuma.model.TzmRegisterModel;
import com.ruiyu.taozhuma.model.UserModel;
import com.ruiyu.taozhuma.utils.LogUtil;
import com.ruiyu.taozhuma.utils.MD5;
import com.ruiyu.taozhuma.utils.StringUtils;
import com.ruiyu.taozhuma.utils.ToastUtils;
import com.ruiyu.taozhuma.utils.UserInfoUtils;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengRegistrar;
import com.umeng.onlineconfig.OnlineConfigAgent;

public class TzmRegisterFragment extends Fragment {
	private View countLayout;
	private EditText et_telephone, et_password, et_captcha;
	// private String[] str = {"普通用户","批发商"};
	private Button btn_sendCaptcha, bt_register;
	// private Spinner sp_perIndents;
	// private TextView tv_perIndents;

	private ApiClient apiClient, apiClient2;
	private TzmCaptchaApi tzmCaptchaApi;
	private TzmCaptchaModel tzmCaptchaModel;
	private TzmRegisterModel tzmRegisterModel;
	private UserModel userModel;
	String regChannel;
	private TzmRegisterApi tzmRegisterApi;

	private String phone, captcha, pass, captchaReturn;
	private int source = 1;// 1=注册2=修改密码

	private TimeCount time;

	// 图形验证
	private GeetestLib gt = new GeetestLib();
	// 设置二次验证的URL，需替换成自己的服务器URL,发送验证码接口
	// private String validateURL = AppConfig.TZM_CAPTCHA_URL;
	private Context context;

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// 友盟在线参数
		OnlineConfigAgent.getInstance().updateOnlineConfig(getActivity());
		time = new TimeCount(60000, 1000);
		initView();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		countLayout = inflater.inflate(R.layout.tzm_register_fragment,
				container, false);
		context = getActivity();
		gt.setCaptchaURL(AppConfig.CAPTCHAURL);
		return countLayout;
	}

	private void initView() {

		et_telephone = (EditText) countLayout.findViewById(R.id.et_telephone);
		et_telephone.setOnClickListener(clickListener);
		btn_sendCaptcha = (Button) countLayout
				.findViewById(R.id.btn_sendCaptcha);
		btn_sendCaptcha.setOnClickListener(clickListener);
		et_password = (EditText) countLayout.findViewById(R.id.et_password);
		bt_register = (Button) countLayout.findViewById(R.id.bt_register);
		bt_register.setOnClickListener(clickListener);
		et_captcha = (EditText) countLayout.findViewById(R.id.et_captcha);
		// tv_perIndents=(TextView)
		// countLayout.findViewById(R.id.tv_perIndents);
		// tv_perIndents.setOnClickListener(clickListener);
		// sp_perIndents=(Spinner) countLayout.findViewById(R.id.sp_perIndents);
		// adapter = new ArrayAdapter<String>(getActivity(),
		// android.R.layout.simple_spinner_item, str);
		// adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// sp_perIndents.setAdapter(adapter);
		// sp_perIndents
		// .setOnItemSelectedListener(new SpinnerSelectedListener());
		// sp_perIndents.setSelection(0, true);
		ApplicationInfo appInfo;
		try {
			appInfo = getActivity().getPackageManager().getApplicationInfo(
					getActivity().getPackageName(),
					PackageManager.GET_META_DATA);
			regChannel = appInfo.metaData.getString("UMENG_CHANNEL");

		} catch (NameNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// System.out.println(regChannel);
		tzmRegisterModel = new TzmRegisterModel();
		apiClient = new ApiClient(getActivity());
		apiClient2 = new ApiClient(getActivity());

	}

	OnClickListener clickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_sendCaptcha:
				phone = et_telephone.getText().toString();
				if (phone.equals("")) {
					ToastUtils.showShortToast(getActivity(), "请输入手机号码");
					return;
				}
				if (!StringUtils.isMobileNO(phone)) {
					ToastUtils.showShortToast(getActivity(), "请输入正确格式的手机号码");
					return;
				}
				
				String openGeetest = OnlineConfigAgent.getInstance()
						.getConfigParams(getActivity(), "openGeetest");
				if (StringUtils.isNotEmpty(openGeetest)
						&& openGeetest.equals("1")) {
					// 图形验证
					new GtAppDlgTask().execute();
				} else {
					time.start();
					sendVerifyCode(null, null, null, false);// 获取验证码
				}

				break;
			case R.id.bt_register:
				register();// 注册
				break;
			// case R.id.tv_perIndents:
			// sp_perIndents.performClick();
			// sp_perIndents.setVisibility(View.VISIBLE);
			// tv_perIndents.setVisibility(View.GONE);
			// break;
			}
		}
	};

	class GtAppDlgTask extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... params) {

			return gt.startCaptcha();
		}

		@Override
		protected void onPostExecute(Boolean result) {

			if (result) {

				openGtTest(context, gt.getCaptcha(), gt.getChallenge(), true);

			} else {
				ToastUtils.showShortToast(context, "现处于用户访问高峰期，请稍候再试。");
			}
		}
	}

	public void openGtTest(Context ctx, String captcha, String challenge,
			boolean success) {

		GtDialog dialog = new GtDialog(ctx, captcha, challenge, success);

		// 启用debug可以在webview上看到验证过程的一些数据
		// dialog.setDebug(true);

		dialog.setGtListener(new GtListener() {

			@Override
			public void gtResult(boolean success, String result) {

				if (success) {

					// toastMsg("client captcha succeed:" + result);

					try {
						JSONObject res_json = new JSONObject(result);

						// Map<String, String> params = new HashMap<String,
						// String>();
						//
						// params.put("geetest_challenge",
						// res_json.getString("geetest_challenge"));
						//
						// params.put("geetest_validate",
						// res_json.getString("geetest_validate"));
						//
						// params.put("geetest_seccode",
						// res_json.getString("geetest_seccode"));
						time.start();// 开始计时
						sendVerifyCode(res_json.getString("geetest_challenge"),
								res_json.getString("geetest_validate"),
								res_json.getString("geetest_seccode"), true);// 获取验证码

						// String string =
						// getUTF8XMLString(res_json.getString("geetest_seccode"));
						// LogUtil.Log(string);
						// String response = gt.submitPostData(params, "utf-8");

						// toastMsg("server captcha :" + response);

					} catch (Exception e) {

						e.printStackTrace();
					}

				} else {
					// 验证失败
					// toastMsg("client captcha failed:" + result);
				}
			}

			@Override
			public void closeGt() {

				// toastMsg("Close geetest windows");
			}
		});

		dialog.show();
	}

	// private void toastMsg(String msg) {
	//
	// Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	//
	// }

	// 验证码倒计时
	class TimeCount extends CountDownTimer {

		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
		}

		@Override
		public void onFinish() {// 计时完毕时触发
			btn_sendCaptcha.setText("获取验证码");
			btn_sendCaptcha.setClickable(true);
			btn_sendCaptcha.setTextColor(Color.RED);
		}

		@Override
		public void onTick(long millisUntilFinished) {// 计时过程显示
			btn_sendCaptcha.setClickable(false);
			btn_sendCaptcha.setTextColor(Color.GRAY);
			// btn_sendCaptcha.setText(millisUntilFinished /1000+"秒");
			btn_sendCaptcha.setText("请稍等...(" + millisUntilFinished / 1000
					+ ")");
		}

	}

	// 发送验证码
	/**
	 * 
	 * @param geetest_challenge
	 * @param geetest_validate
	 * @param geetest_seccode
	 * @param openGeetest
	 *            是否开启极简验证
	 */
	public void sendVerifyCode(String geetest_challenge,
			String geetest_validate, String geetest_seccode, boolean openGeetest) {
		phone = et_telephone.getText().toString();
		if (phone.equals("")) {
			ToastUtils.showShortToast(getActivity(), "请输入手机号码");
			return;
		}
		tzmCaptchaApi = new TzmCaptchaApi();
		tzmCaptchaApi.setPhone(phone);
		tzmCaptchaApi.setSource(source);
		if (openGeetest) {
			tzmCaptchaApi.setGeetest_challenge(geetest_challenge);
			tzmCaptchaApi.setGeetest_validate(geetest_validate);
			tzmCaptchaApi.setGeetest_seccode(geetest_seccode);
		}

		Map<String, String> map = new HashMap<String, String>();
		map.put("phone", this.phone);
		map.put("source", this.source + "");
		if (openGeetest) {
			map.put("geetest_challenge", geetest_challenge);
			map.put("geetest_validate", geetest_validate);
			map.put("geetest_seccode", geetest_seccode);
		}

		String sign = MD5.createLinkString(map);
		tzmCaptchaApi.setSign(sign);
		apiClient.api(tzmCaptchaApi, new ApiListener() {
			@Override
			public void onStart() {
			}

			@Override
			public void onException(Exception e) {
				LogUtil.ErrorLog(e);
			}

			@Override
			public void onError(String error) {
				ToastUtils.showShortToast(getActivity(), error);
			}

			@Override
			public void onComplete(String jsonStr) {
				try {
					Gson gson = new Gson();
					Type type = new TypeToken<BaseModel<TzmCaptchaModel>>() {
					}.getType();
					BaseModel<TzmCaptchaModel> base = gson.fromJson(jsonStr,
							type);
					if (base.success) {
						tzmCaptchaModel = base.result;
						captchaReturn = tzmCaptchaModel.captcha;// 保存接口返回的验证码
						ToastUtils.showShortToast(getActivity(), "已发送");
					} else {
						LogUtil.Log(base.error_msg);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, true);
	}

	/**
	 * Get XML String of utf-8
	 * 
	 * @return XML-Formed string
	 */
	public static String getUTF8XMLString(String xml) {
		// A StringBuffer Object
		StringBuffer sb = new StringBuffer();
		sb.append(xml);
		String xmString = "";
		String xmlUTF8 = "";
		try {
			xmString = new String(sb.toString().getBytes("UTF-8"));
			xmlUTF8 = URLEncoder.encode(xmString, "UTF-8");
			System.out.println("utf-8 编码：" + xmlUTF8);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// return to String Formed
		return xmlUTF8;
	}

	// 注册
	public void register() {
		phone = et_telephone.getText().toString();
		if (StringUtils.isEmpty(phone)) {
			ToastUtils.showShortToast(getActivity(), "请输入手机号码");
			return;
		}
		if (!StringUtils.isMobileNO(phone)) {
			ToastUtils.showShortToast(getActivity(), "请检查一下号码");
			return;
		}
		captcha = et_captcha.getText().toString();
		if (StringUtils.isEmpty(captcha)) {
			ToastUtils.showShortToast(getActivity(), "请输入验证码");
			return;
		}
		if (!(captcha.equals(captchaReturn))) {
			ToastUtils.showShortToast(getActivity(), "验证码错误");
			return;
		}
		pass = et_password.getText().toString();
		if (StringUtils.isEmpty(pass)) {
			ToastUtils.showShortToast(getActivity(), "请输入密码");
			return;
		}
		if (!StringUtils.isLegalPassword(pass)) {
			ToastUtils.showShortToast(getActivity(), "请检查密码的合理性");
			return;
		}
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
		// name = et_name.getText().toString();
		// if (name.equals("")) {
		// Toast.makeText(getActivity(), "请输入昵称", Toast.LENGTH_SHORT).show();
		// return;
		// }
		// if(tv_perIndents.getVisibility()!=View.GONE){
		// Toast.makeText(getActivity(), "请选择类型", Toast.LENGTH_SHORT).show();
		// return;
		// }

		// 手机串行号
		String Imei = ((TelephonyManager) getActivity().getSystemService(
				getActivity().TELEPHONY_SERVICE)).getDeviceId();
		// LogUtil.Log("手机串行号", Imei);
		tzmRegisterApi = new TzmRegisterApi();
		tzmRegisterApi.setPhone(phone);
		tzmRegisterApi.setCaptcha(captcha);
		tzmRegisterApi.setImei(Imei);
		tzmRegisterApi.setPass(MD5.MD5Encode(pass));
		tzmRegisterApi.setregChannel(regChannel);
		tzmRegisterApi.setType(1);
		tzmRegisterApi.setBaidu_uid(device_token);

		Map<String, String> map = new HashMap<String, String>();
		map.put("phone", this.phone);
		map.put("captcha", this.captcha);
		map.put("pass", MD5.MD5Encode(pass));
		map.put("type", 1 + "");
		map.put("regChannel", this.regChannel);
		map.put("baidu_uid", device_token);
		map.put("imei", Imei);
		String sign = MD5.createLinkString(map);
		tzmRegisterApi.setSign(sign);

		// 加入类型
		apiClient2.api(tzmRegisterApi, new ApiListener() {
			@Override
			public void onStart() {
			}

			@Override
			public void onException(Exception e) {
				LogUtil.ErrorLog(e);
			}

			@Override
			public void onError(String error) {
				LogUtil.Log(error);
				ToastUtils.showShortToast(getActivity(), error);
			}

			@Override
			public void onComplete(String jsonStr) {
				if (StringUtils.isNotBlank(jsonStr)) {
					Gson gson = new Gson();
					Type type = new TypeToken<BaseModel<TzmRegisterModel>>() {
					}.getType();
					BaseModel<TzmRegisterModel> base = gson.fromJson(jsonStr,
							type);
					if (base.success) {
						tzmRegisterModel = base.result;
						userModel = new UserModel();
						userModel.uid = tzmRegisterModel.uid;
						userModel.type = tzmRegisterModel.type;
						userModel.name = tzmRegisterModel.name;
						userModel.phone = tzmRegisterModel.phone;
						userModel.judgeType = tzmRegisterModel.judgeType;
						userModel.status = tzmRegisterModel.status;

						UserInfoUtils.setUserInfo(userModel);// 保存用户登陆信息register_tip
						ToastUtils.showShortToast(getActivity(), R.string.register_tip);
						Intent intentUserinfo = new Intent();
						intentUserinfo.putExtra("uid", tzmRegisterModel.uid);
						intentUserinfo.putExtra("name", tzmRegisterModel.name);
						intentUserinfo.putExtra("type", tzmRegisterModel.type);
						intentUserinfo
								.putExtra("type", tzmRegisterModel.status);
						getActivity().setResult(Activity.RESULT_OK,
								intentUserinfo);
						getActivity().finish();
					} else {
						LogUtil.Log(base.error_msg);
					}
				}
			}
		}, true);
	}

	// class SpinnerSelectedListener implements OnItemSelectedListener {
	//
	// public void onItemSelected(AdapterView<?> arg0, View view, int arg2,
	// long arg3) {
	// if(arg2==1){
	// perIndents=6;
	// }else if(arg2==0){
	// perIndents=1;
	// }
	//
	// TextView tv=(TextView) view;
	// tv.setTextColor(Color.parseColor("#3e3a39"));
	// }
	//
	// public void onNothingSelected(AdapterView<?> arg0) {
	// }
	// }
}