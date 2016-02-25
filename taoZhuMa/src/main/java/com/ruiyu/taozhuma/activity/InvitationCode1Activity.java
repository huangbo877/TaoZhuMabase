package com.ruiyu.taozhuma.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.api.ApiClient;
import com.ruiyu.taozhuma.api.ApiListener;
import com.ruiyu.taozhuma.api.InvitationCode1Api;
import com.ruiyu.taozhuma.model.BaseModel;

import com.ruiyu.taozhuma.test.ProductImageModel;
import com.ruiyu.taozhuma.utils.MD5;
import com.ruiyu.taozhuma.utils.ProgressDialogUtil;
import com.ruiyu.taozhuma.utils.ToastUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class InvitationCode1Activity extends Activity {

	// 接口调用
	private ApiClient client;
	private InvitationCode1Api InvitationCode1Api;
	int id;
	@ViewInject(R.id.et_input)
	private EditText et_input;
	@ViewInject(R.id.bt_code)
	private Button bt_code;
	@ViewInject(R.id.txt_head_title)
	private TextView txt_head_title;
	@ViewInject(R.id.btn_head_left)
	private Button btn_head_left;
	@ViewInject(R.id.ll_suo)
	private LinearLayout ll_suo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.invitation_code1_activity);
		ViewUtils.inject(this);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		id = getIntent().getExtras().getInt("id");
		client = new ApiClient(this);
		InvitationCode1Api = new InvitationCode1Api();
		txt_head_title.setText("输入邀请码");

		bt_code.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				load();
			}
		});
		ll_suo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
			}
		});
		btn_head_left.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

	}

	private void load() {
		// TODO Auto-generated method stub
		try {

			InvitationCode1Api.setUid(id);
			InvitationCode1Api.setinviteCode(et_input.getText().toString());
			Map<String, String> map = new HashMap<String, String>();
			map.put("uid", id+"");
			map.put("inviteCode",et_input.getText().toString());
			String sign = MD5.createLinkString(map);
			InvitationCode1Api.setSign(sign);
		} catch (Exception e) {
			// TODO: handle exception
		}
		client.api(this.InvitationCode1Api, new ApiListener() {
			@Override
			public void onStart() {

			}

			@Override
			public void onComplete(String jsonStr) {
				ProgressDialogUtil.closeProgressDialog();
				Gson gson = new Gson();
				Type type = new TypeToken<BaseModel<ArrayList<ProductImageModel>>>() {
				}.getType();
				BaseModel<String> base = gson.fromJson(jsonStr, type);

				if (base.success) {
					Intent intent = new Intent(new Intent(
							InvitationCode1Activity.this,
							InvitationCode2Activity.class));
					intent.putExtra("id", id);
					startActivity(intent);
					ToastUtils.showShortToast(getApplicationContext(),
							base.error_msg);
					finish();
				} else {
					ToastUtils.showShortToast(getApplicationContext(),
							base.error_msg);
				}

			}

			@Override
			public void onError(String error) {
				ToastUtils.showShortToast(getApplicationContext(), error);
			}

			@Override
			public void onException(Exception e) {
				ToastUtils.showShortToast(getApplicationContext(), "网络异常！");
			}
		}, true);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
