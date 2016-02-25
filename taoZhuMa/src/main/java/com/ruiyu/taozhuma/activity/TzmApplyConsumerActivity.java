package com.ruiyu.taozhuma.activity;

import org.json.JSONObject;

import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.api.ApiClient;
import com.ruiyu.taozhuma.api.ApiListener;
import com.ruiyu.taozhuma.api.TzmApplyConsumerApi;
import com.ruiyu.taozhuma.base.BaseApplication;
import com.ruiyu.taozhuma.model.UserModel;
import com.ruiyu.taozhuma.utils.LogUtil;
import com.ruiyu.taozhuma.utils.ProgressDialogUtil;
import com.ruiyu.taozhuma.utils.StringUtils;
import com.ruiyu.taozhuma.utils.ToastUtils;
import com.ruiyu.taozhuma.utils.UserInfoUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

//分销
public class TzmApplyConsumerActivity extends Activity {
	private TextView txt_head_title;
	private Button btn_head_left;
	private ArrayAdapter<String> adapter, adapter2;
	private String[] mainCategorys = { "遥控/电动玩具", "早教/音乐玩具", "过家家玩具", "童车玩具",
			"益智玩具", "其他玩具" };
	private String[] groups = { "1~5人", "6~20人", "21~50人", "51~100人", "100人以上" };
	private Spinner sp_mainCategory, sp_groups;
	private TextView tv_mainCategory, tv_groups;
	private EditText et_companyName, et_platform, et_salesArea, et_contact,
			et_duty, et_email, et_QQ, et_tel, et_phone, et_fax, et_content;
	private ImageView img_type1, img_type2;
	private Integer mainCategory, group;
	private long uid;
	private String companyName, platform, salesArea, contact, duty, email, QQ,
			tel, phone, fax, content;
	private String type=2+"";
	private Button applyConsumer_submit;
	private CheckBox cb_consent;
	private UserModel userInfo;
	private TzmApplyConsumerApi tzmApplyConsumerApi;
	private ApiClient apiClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tzm_apply_consumer_activity);
		txt_head_title = (TextView) findViewById(R.id.txt_head_title);
		txt_head_title.setText("申请分销商");
		btn_head_left = (Button) findViewById(R.id.btn_head_left);
		btn_head_left.setOnClickListener(clickListener);
		userInfo = BaseApplication.getInstance().getLoginUser();
		initView();
	}

	private void initView() {
		sp_mainCategory = (Spinner) findViewById(R.id.sp_mainCategory);
		tv_mainCategory = (TextView) findViewById(R.id.tv_mainCategory);
		tv_mainCategory.setOnClickListener(clickListener);

		sp_groups = (Spinner) findViewById(R.id.sp_groups);
		tv_groups = (TextView) findViewById(R.id.tv_groups);
		tv_groups.setOnClickListener(clickListener);
		adapter = new ArrayAdapter<String>(TzmApplyConsumerActivity.this,
				android.R.layout.simple_spinner_item, mainCategorys);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_mainCategory.setAdapter(adapter);
		sp_mainCategory
				.setOnItemSelectedListener(new SpinnerSelectedListener());
		sp_mainCategory.setSelection(0, true);
		adapter2 = new ArrayAdapter<String>(TzmApplyConsumerActivity.this,
				android.R.layout.simple_spinner_item, groups);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_groups.setAdapter(adapter2);
		sp_groups.setOnItemSelectedListener(new SpinnerSelectedListener2());
		sp_groups.setSelection(0, true);

		img_type1 = (ImageView) findViewById(R.id.img_type1);
		img_type2 = (ImageView) findViewById(R.id.img_type2);
		findViewById(R.id.tv_type1).setOnClickListener(clickListener);
		findViewById(R.id.tv_type2).setOnClickListener(clickListener);
		img_type1.setOnClickListener(clickListener);
		img_type2.setOnClickListener(clickListener);

		et_companyName = (EditText) findViewById(R.id.et_companyName);
		et_platform = (EditText) findViewById(R.id.et_platform);
		et_salesArea = (EditText) findViewById(R.id.et_salesArea);
		et_contact = (EditText) findViewById(R.id.et_contact);
		et_duty = (EditText) findViewById(R.id.et_duty);
		et_email = (EditText) findViewById(R.id.et_email);
		et_QQ = (EditText) findViewById(R.id.et_QQ);
		et_tel = (EditText) findViewById(R.id.et_tel);
		et_phone = (EditText) findViewById(R.id.et_phone);
		et_fax = (EditText) findViewById(R.id.et_fax);
		et_content = (EditText) findViewById(R.id.et_content);
		cb_consent = (CheckBox) findViewById(R.id.cb_consent);
		applyConsumer_submit = (Button) findViewById(R.id.applyConsumer_submit);
		applyConsumer_submit.setOnClickListener(clickListener);
	}

	View.OnClickListener clickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_head_left:
				onBackPressed();
				break;
			case R.id.tv_mainCategory:
				sp_mainCategory.performClick();
				sp_mainCategory.setVisibility(View.VISIBLE);
				tv_mainCategory.setVisibility(View.GONE);
				break;
			case R.id.tv_groups:
				sp_groups.performClick();
				sp_groups.setVisibility(View.VISIBLE);
				tv_groups.setVisibility(View.GONE);
				break;
			case R.id.img_type1:
				setTabSelection(1);
				break;
			case R.id.img_type2:
				setTabSelection(0);
				break;
			case R.id.tv_type1:
				setTabSelection(1);
				break;
			case R.id.tv_type2:
				setTabSelection(0);
				break;
			case R.id.applyConsumer_submit:
				applyConsumer_submit();
				break;
			default:
				break;
			}
		}
	};

	class SpinnerSelectedListener implements OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			mainCategory = arg2;
		}

		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}

	class SpinnerSelectedListener2 implements OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			group = arg2;
		}

		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}

	private void setTabSelection(int index) {
		switch (index) {
		case 0:
			type = 1 + "";// 网店
			img_type1.setImageResource(R.drawable.tzm_127);
			img_type2.setImageResource(R.drawable.tzm_128);
			et_content.setHint("请输入网店链接");
			et_content.setInputType(InputType.TYPE_TEXT_VARIATION_URI);
			break;
		case 1:
			type = 2 + "";// 实体店
			img_type1.setImageResource(R.drawable.tzm_128);
			img_type2.setImageResource(R.drawable.tzm_127);
			et_content.setHint("请输入公司地址");
			et_content.setInputType(InputType.TYPE_CLASS_TEXT);
			break;
		}
	}

	private void applyConsumer_submit() {
		uid = userInfo.uid;
		companyName = et_companyName.getText().toString();
		platform = et_platform.getText().toString();
		salesArea = et_salesArea.getText().toString();
		contact = et_contact.getText().toString();
		duty = et_duty.getText().toString();
		email = et_email.getText().toString();
		QQ = et_QQ.getText().toString();
		tel = et_tel.getText().toString();
		phone = et_phone.getText().toString();
		fax = et_fax.getText().toString();
		content = et_content.getText().toString();
		if (StringUtils.isBlank(companyName)) {
			ToastUtils.showShortToast(TzmApplyConsumerActivity.this, "请输入店铺名称");
			return;
		}
		if (tv_mainCategory.getVisibility() == View.VISIBLE) {
			ToastUtils.showShortToast(TzmApplyConsumerActivity.this, "请选择主营类目");
			return;
		}
		if (tv_groups.getVisibility() == View.VISIBLE) {
			ToastUtils.showShortToast(TzmApplyConsumerActivity.this, "请选择运营总人数");
			return;
		}
		if (StringUtils.isBlank(platform)) {
			ToastUtils.showShortToast(TzmApplyConsumerActivity.this, "请输入销售平台");
			return;
		}
		if (StringUtils.isBlank(salesArea)) {
			ToastUtils.showShortToast(TzmApplyConsumerActivity.this, "请输入销售地区");
			return;
		}
		if (StringUtils.isBlank(contact)) {
			ToastUtils.showShortToast(TzmApplyConsumerActivity.this, "请输入联系人");
			return;
		}
		if (StringUtils.isBlank(email)) {
			ToastUtils.showShortToast(TzmApplyConsumerActivity.this, "请输入联系人邮箱");
			return;
		}
		if (!StringUtils.isEmailStr(email)) {
			ToastUtils.showShortToast(TzmApplyConsumerActivity.this, "请检查一下邮箱");
			return;
		}
		if (StringUtils.isBlank(QQ)) {
			ToastUtils.showShortToast(TzmApplyConsumerActivity.this, "请输入联系人QQ");
			return;
		}
		if (StringUtils.isBlank(phone)) {
			ToastUtils.showShortToast(TzmApplyConsumerActivity.this, "请输入手机号码");
			return;
		}
		if (!StringUtils.isMobileNO(phone)) {
			ToastUtils.showShortToast(TzmApplyConsumerActivity.this, "请检查一下手机号码");
			return;
		}
		if (StringUtils.isBlank(content)) {
			ToastUtils.showShortToast(TzmApplyConsumerActivity.this, "请输入网店链接或实体店地址");
			return;
		}
		if (!cb_consent.isChecked()) {
			ToastUtils.showShortToast(TzmApplyConsumerActivity.this, "请同意分销服务条款以及分销商协议");
			return;
		}

		apiClient = new ApiClient(this);
		tzmApplyConsumerApi = new TzmApplyConsumerApi();
		tzmApplyConsumerApi.setUid(uid);
		tzmApplyConsumerApi.setCompanyName(companyName);
		tzmApplyConsumerApi.setMainCategory(mainCategory);
		tzmApplyConsumerApi.setGroups(group);
		tzmApplyConsumerApi.setPlatform(platform);
		tzmApplyConsumerApi.setSalesArea(salesArea);
		tzmApplyConsumerApi.setContact(contact);
		tzmApplyConsumerApi.setDuty(duty);
		tzmApplyConsumerApi.setEmail(email);
		tzmApplyConsumerApi.setQQ(QQ);
		tzmApplyConsumerApi.setTel(tel);
		tzmApplyConsumerApi.setPhone(phone);
		tzmApplyConsumerApi.setFax(fax);
		tzmApplyConsumerApi.setType(type);
		tzmApplyConsumerApi.setContent(content);
		apiClient.api(tzmApplyConsumerApi, new ApiListener() {
			@Override
			public void onStart() {
				ProgressDialogUtil.openProgressDialog(
						TzmApplyConsumerActivity.this, "", "正在提交...");
			}

			@Override
			public void onError(String error) {
				ProgressDialogUtil.closeProgressDialog();
				ToastUtils.showShortToast(TzmApplyConsumerActivity.this, error);
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
						ToastUtils.showShortToast(TzmApplyConsumerActivity.this,
								json.getString("error_msg"));
						if (json.getBoolean("success")) {
							userInfo.judgeType = 2;
							UserInfoUtils.updateUserInfo(userInfo);
							Intent intent= new Intent();
							setResult(Activity.RESULT_OK, intent);
							finish();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}, true);

	}
}
