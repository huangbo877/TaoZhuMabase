package com.ruiyu.taozhuma.activity;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.geetest.gt_sdk.GeetestLib;
import com.geetest.gt_sdk.GtDialog;
import com.geetest.gt_sdk.GtDialog.GtListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.api.ApiClient;
import com.ruiyu.taozhuma.api.ApiListener;
import com.ruiyu.taozhuma.api.TzmCaptchaApi;
import com.ruiyu.taozhuma.api.TzmPasswordApi;
import com.ruiyu.taozhuma.config.AppConfig;
import com.ruiyu.taozhuma.model.BaseModel;
import com.ruiyu.taozhuma.model.TzmCaptchaModel;
import com.ruiyu.taozhuma.utils.LogUtil;
import com.ruiyu.taozhuma.utils.MD5;
import com.ruiyu.taozhuma.utils.ProgressDialogUtil;
import com.ruiyu.taozhuma.utils.StringUtils;
import com.ruiyu.taozhuma.utils.ToastUtils;
import com.umeng.onlineconfig.OnlineConfigAgent;

public class TzmForgetPasswordActivity extends Activity {

	private TextView txt_head_title;
	private Button btn_head_left, bt_getPass, btn_head_right;
	private EditText et_phone;
	private EditText et_captcha, et_password, et_password1;
	private String phone, captcha, password, password1;
	private Button btn_sendCaptcha;
	private TzmCaptchaApi tzmCaptchaApi;
	private TzmPasswordApi tzmPasswordApi;
	private ApiClient apiClient, apiClient1;
	private int source = 2;// 1=注册2=修改密码
	private TimeCount time;

	// 图形验证
	private GeetestLib gt = new GeetestLib();
	private Context context = TzmForgetPasswordActivity.this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tzm_forget_password_activity);
		// 友盟在线参数
		OnlineConfigAgent.getInstance().updateOnlineConfig(this);
		txt_head_title = (TextView) findViewById(R.id.txt_head_title);
		txt_head_title.setText("找回密码");
		btn_head_left = (Button) findViewById(R.id.btn_head_left);
		btn_head_left.setOnClickListener(clickListener);
		et_phone = (EditText) findViewById(R.id.et_phone);
		et_phone.setOnClickListener(clickListener);
		bt_getPass = (Button) findViewById(R.id.bt_getPass);
		bt_getPass.setOnClickListener(clickListener);
		btn_head_right = (Button) findViewById(R.id.btn_head_right);
		btn_head_right.setText("注册");
		btn_sendCaptcha = (Button) findViewById(R.id.btn_sendCaptcha);
		time = new TimeCount(60000, 1000);
		btn_sendCaptcha.setOnClickListener(clickListener);
		et_captcha = (EditText) findViewById(R.id.et_captcha);
		et_password = (EditText) findViewById(R.id.et_password);
		et_password1 = (EditText) findViewById(R.id.et_password1);
		gt.setCaptchaURL(AppConfig.CAPTCHAURL);
		apiClient = new ApiClient(this);
		apiClient1 = new ApiClient(this);
	}

	OnClickListener clickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_head_left:
				onBackPressed();
				break;
			case R.id.btn_sendCaptcha:
				phone = et_phone.getText().toString();
				if (phone.equals("")) {
					ToastUtils.showShortToast(context, "请输入手机号码");
					return;
				}
				if (!StringUtils.isMobileNO(phone)) {
					ToastUtils.showShortToast(context, "请输入正确格式的手机号码");
					return;
				}
				String openGeetest = OnlineConfigAgent.getInstance()
						.getConfigParams(TzmForgetPasswordActivity.this,
								"openGeetest");
				if (StringUtils.isNotEmpty(openGeetest)
						&& openGeetest.equals("1")) {
					// 图形验证
					new GtAppDlgTask().execute();
				} else {
					time.start();
					sendVerifyCode(null, null, null, false);// 获取验证码
				}

				break;
			case R.id.bt_getPass:
				// Intent intent = new Intent();
				// intent.putExtra("phone", et_phone.getText().toString());
				resetPassword();
				break;
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
				// 极验服务器暂时性宕机：
                ToastUtils.showShortToast(context, "现处于用户访问高峰期，请稍候再试。");
				// 1. 可以选择继续使用极验，去掉下行注释
				// openGtTest(context, gt.getCaptcha(), gt.getChallenge(),
				// false);

				// 2. 使用自己的验证
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
						time.start();
						sendVerifyCode(res_json.getString("geetest_challenge"),
								res_json.getString("geetest_validate"),
								res_json.getString("geetest_seccode"),true);
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

	// private void Countdown() {
	// phone = et_phone.getText().toString();
	// if (phone.equals("")) {
	// Toast.makeText(this, "请输入手机号码", Toast.LENGTH_SHORT).show();
	// } else if (!StringUtils.isMobileNO(phone)) {
	// Toast.makeText(this, "请检查一下号码", Toast.LENGTH_SHORT).show();
	// } else {
	// time.start();
	// }
	//
	// }
	/**
	 * 
	 * @param geetest_challenge
	 * @param geetest_validate
	 * @param geetest_seccode
	 * @param openGeetest
	 *            是否开启极简验证
	 */
	private void sendVerifyCode(String geetest_challenge,
			String geetest_validate, String geetest_seccode, boolean openGeetest) {
		phone = et_phone.getText().toString();
		if (phone.equals("")) {
			ToastUtils.showShortToast(this, "请输入手机号码");
			return;
		}
		if (!StringUtils.isMobileNO(phone)) {
			ToastUtils.showShortToast(this, "请检查一下号码");
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
		map.put("phone", phone);
		map.put("source", source + "");
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
				ProgressDialogUtil.openProgressDialog(
						TzmForgetPasswordActivity.this, "", "正在提交...");
			}

			@Override
			public void onException(Exception e) {
				ProgressDialogUtil.closeProgressDialog();
				LogUtil.ErrorLog(e);
			}

			@Override
			public void onError(String error) {
				ProgressDialogUtil.closeProgressDialog();
				LogUtil.Log(error);
				ToastUtils.showShortToast(TzmForgetPasswordActivity.this, error);
			}

			@Override
			public void onComplete(String jsonStr) {
				ProgressDialogUtil.closeProgressDialog();
				try {
					Gson gson = new Gson();
					Type type = new TypeToken<BaseModel<TzmCaptchaModel>>() {
					}.getType();
					BaseModel<TzmCaptchaModel> base = gson.fromJson(jsonStr,
							type);
					if (base.success) {
						// tzmCaptchaModel = base.result;
						ToastUtils.showShortToast(
								TzmForgetPasswordActivity.this, "已发送");
					} else {
						LogUtil.Log(base.error_msg);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, true);
	}

	private void resetPassword() {
		phone = et_phone.getText().toString();
		captcha = et_captcha.getText().toString();
		password = et_password.getText().toString();
		password1 = et_password1.getText().toString();
		if (StringUtils.isEmpty(phone)) {
			ToastUtils.showShortToast(this, "请输入手机号码");
			return;
		}
		if (!StringUtils.isMobileNO(phone)) {
			ToastUtils.showShortToast(this, "请检查一下号码");
			return;
		}
		if (StringUtils.isEmpty(captcha)) {
			ToastUtils.showShortToast(this, "请获取验证码");
			return;
		}
		if (!StringUtils.isLegalPassword(password)) {
			ToastUtils.showShortToast(this, "请检查密码的合理性");
			return;
		}
		if (!password.equals(password1)) {
			ToastUtils.showShortToast(this, "两次密码不一致");
			return;
		}
		tzmPasswordApi = new TzmPasswordApi();
		tzmPasswordApi.setPhone(phone);
		tzmPasswordApi.setCaptcha(captcha);
		tzmPasswordApi.setPass(MD5.MD5Encode(password));

		// 加密签名
		Map<String, String> map = new HashMap<String, String>();
		map.put("phone", phone);
		map.put("pass", MD5.MD5Encode(password));
		map.put("captcha", captcha);
		String sign = MD5.createLinkString(map);
		tzmPasswordApi.setSign(sign);

		apiClient1.api(tzmPasswordApi, new ApiListener() {

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
				ToastUtils.showShortToast(TzmForgetPasswordActivity.this, error);
			}

			@Override
			public void onComplete(String jsonStr) {
				if (StringUtils.isNotBlank(jsonStr)) {
					try {
						JSONObject jsonObject = new JSONObject(jsonStr);
						boolean success = jsonObject.optBoolean("success");
						String error_msg = jsonObject.optString("error_msg");
						ToastUtils.showShortToast(
								TzmForgetPasswordActivity.this, error_msg);
						if (success) {
							onBackPressed();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}
			}
		}, true);

	}

	// 倒计时内部类
	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			btn_sendCaptcha.setText("获取验证码");
			btn_sendCaptcha.setClickable(true);

		}

		@Override
		public void onTick(long millisUntilFinished) {
			btn_sendCaptcha.setClickable(false);
			btn_sendCaptcha.setText("稍等" + millisUntilFinished / 1000 + "秒");

		}
	}
}
