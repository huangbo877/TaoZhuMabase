package com.ruiyu.taozhuma.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.api.ApiClient;
import com.ruiyu.taozhuma.api.ApiListener;
import com.ruiyu.taozhuma.api.TzmReSetPassWordApi;
import com.ruiyu.taozhuma.utils.LogUtil;
import com.ruiyu.taozhuma.utils.MD5;
import com.ruiyu.taozhuma.utils.ProgressDialogUtil;
import com.ruiyu.taozhuma.utils.StringUtils;
import com.ruiyu.taozhuma.utils.ToastUtils;
import com.ruiyu.taozhuma.utils.UserInfoUtils;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class TzmReSetPassWork extends Activity {
	private TextView txt_head_title;
	private Button btn_head_left, btn_reSet;
	private EditText et_pass, et_passWord, et_passWord1;
	private String phone, pass, passWord, passWord1;
	private TzmReSetPassWordApi reSetPassWordApi;
	private ApiClient apiClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tzm_resetpassword_activity);
		initView();
	}

	private void initView() {
		txt_head_title = (TextView) findViewById(R.id.txt_head_title);
		txt_head_title.setText("修改密码");
		btn_head_left = (Button) findViewById(R.id.btn_head_left);
		btn_head_left.setBackgroundResource(R.drawable.icon_close);
		btn_head_left.setOnClickListener(clickListener);
		apiClient = new ApiClient(this);
		et_pass = (EditText) findViewById(R.id.et_pass);
		et_passWord = (EditText) findViewById(R.id.et_passWord);
		et_passWord1 = (EditText) findViewById(R.id.et_passWord1);
		btn_reSet = (Button) findViewById(R.id.btn_reSet);
		btn_reSet.setOnClickListener(clickListener);

	}

	private void reSetPassWord() {
		phone = UserInfoUtils.getUserInfo().phone;

		pass = et_pass.getText().toString();
		passWord = et_passWord.getText().toString();
		passWord1 = et_passWord1.getText().toString();
		if (StringUtils.isBlank(pass)) {
			ToastUtils.showShortToast(this, "请输入旧密码");
			return;
		}
		if (StringUtils.isBlank(passWord)) {
			ToastUtils.showShortToast(this, "请输入新密码");
			return;
		}
		if (!StringUtils.isLegalPassword(passWord)) {
			ToastUtils.showShortToast(this, "请检查密码是否合理");
			return;
		}
		if (StringUtils.isBlank(passWord1)) {
			ToastUtils.showShortToast(this, "请确认新密码");
			return;
		}
		if (!passWord.equals(passWord1)) {
			ToastUtils.showShortToast(this, "两次密码不一致");
			return;
		}
		reSetPassWordApi = new TzmReSetPassWordApi();
		reSetPassWordApi.setPhone(phone);
		reSetPassWordApi.setPass(MD5.MD5Encode(pass));
		reSetPassWordApi.setPassWord(MD5.MD5Encode(passWord));

		// 加密签名
		Map<String, String> map = new HashMap<String, String>();
		map.put("phone", phone);
		map.put("pass", MD5.MD5Encode(pass));
		map.put("passWork", MD5.MD5Encode(passWord));
		String sign = MD5.createLinkString(map);
		reSetPassWordApi.setSign(sign);

		apiClient.api(reSetPassWordApi, new ApiListener() {
			@Override
			public void onStart() {
				ProgressDialogUtil.openProgressDialog(TzmReSetPassWork.this,
						"", "正在提交...");
			}

			@Override
			public void onError(String error) {
				ProgressDialogUtil.closeProgressDialog();
				ToastUtils.showShortToast(TzmReSetPassWork.this, error);
			}

			@Override
			public void onException(Exception e) {
				ProgressDialogUtil.closeProgressDialog();
				LogUtil.ErrorLog(e);
			}

			@Override
			public void onComplete(String jsonStr) {
				ProgressDialogUtil.closeProgressDialog();
				if (StringUtils.isNotBlank(jsonStr)) {
					try {
						JSONObject json = new JSONObject(jsonStr);
						ToastUtils.showShortToast(TzmReSetPassWork.this,
								json.getString("error_msg"));
						if (json.getBoolean("success")) {
							onBackPressed();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}, true);

	}

	View.OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_head_left:
				onBackPressed();
				break;
			case R.id.btn_reSet:
				reSetPassWord();
				break;
			}
		}
	};
}
