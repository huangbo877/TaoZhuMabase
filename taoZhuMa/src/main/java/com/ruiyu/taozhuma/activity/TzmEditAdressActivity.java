package com.ruiyu.taozhuma.activity;

import java.lang.reflect.Type;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.api.ApiClient;
import com.ruiyu.taozhuma.api.ApiListener;
import com.ruiyu.taozhuma.api.TzmAddressApi;
import com.ruiyu.taozhuma.api.TzmAddressDetailApi;
import com.ruiyu.taozhuma.api.TzmDelAddressApi;
import com.ruiyu.taozhuma.api.TzmEidtAddressApi;
import com.ruiyu.taozhuma.config.AppConfig;
import com.ruiyu.taozhuma.model.BaseModel;
import com.ruiyu.taozhuma.model.TzmAddressDetailModel;
import com.ruiyu.taozhuma.model.UserModel;
import com.ruiyu.taozhuma.utils.LogUtil;
import com.ruiyu.taozhuma.utils.ProgressDialogUtil;
import com.ruiyu.taozhuma.utils.StringUtils;
import com.ruiyu.taozhuma.utils.ToastUtils;
import com.ruiyu.taozhuma.utils.UserInfoUtils;

public class TzmEditAdressActivity extends Activity {
	@ViewInject(R.id.txt_head_title)
	private TextView txt_head_title;
	@ViewInject(R.id.btn_head_left)
	private Button btn_head_left;
	@ViewInject(R.id.btn_head_right)
	private Button btn_head_right;
	@ViewInject(R.id.as_name)
	private EditText as_name;
	@ViewInject(R.id.as_phone)
	private EditText as_phone;
	@ViewInject(R.id.as_number)
	private EditText as_number;
	@ViewInject(R.id.tv_choseaddress)
	private TextView tv_choseaddress;
	@ViewInject(R.id.cb_isdefault)
	private CheckBox cb_isdefault;
	@ViewInject(R.id.as_address)
	private EditText as_address;
	@ViewInject(R.id.iv_del)
	private ImageView iv_del;

	protected Integer provinceId;
	protected Integer cityId;
	protected Integer areaId;
	protected String mCurrentProviceName;
	protected String mCurrentCityName;
	protected String mCurrentAreaName;

	private Integer uid;
	private String name, phone, number, address;

	private Boolean isLogin;
	private UserModel userModel;
	private ApiClient apiClient;
	private TzmAddressDetailApi tzmAddressDetailApi;
	private Long addId;
	private TzmAddressDetailModel tzmAddressDetailModel;

	private TzmEidtAddressApi tzmEidtAddressApi;
	private TzmDelAddressApi addressApi;
	private int isDefault;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tzm_edit_address_activity);
		ViewUtils.inject(this);
		checkLogin();
		addId = getIntent().getLongExtra("addId", 0);
		btn_head_left.setOnClickListener(clickListener);
		btn_head_right.setOnClickListener(clickListener);
		tv_choseaddress.setOnClickListener(clickListener);
		iv_del.setOnClickListener(clickListener);
		as_name.addTextChangedListener(textWatcher);
		// if (addId == -1) {// 新增地址
		// txt_head_title.setText("新增地址");
		// iv_delete.setVisibility(View.GONE);
		// province();
		// } else {// 修改地址
		// txt_head_title.setText("编辑地址");
		// iv_delete.setVisibility(View.VISIBLE);
		// iv_delete.setOnClickListener(clickListener);
		// tv_province.setVisibility(View.GONE);
		// tv_city.setVisibility(View.GONE);
		// tv_counties.setVisibility(View.GONE);
		// as_province.setVisibility(View.VISIBLE);
		// as_city.setVisibility(View.VISIBLE);
		// as_counties.setVisibility(View.VISIBLE);
		initdata();
		// }
	}

	private void initdata() {
		tzmAddressDetailApi = new TzmAddressDetailApi();
		apiClient = new ApiClient(this);
		tzmAddressDetailApi.setUid(UserInfoUtils.getUserInfo().uid);
		tzmAddressDetailApi.setAddId(addId);
		apiClient.api(tzmAddressDetailApi, new ApiListener() {

			@Override
			public void onStart() {
				ProgressDialogUtil.openProgressDialog(
						TzmEditAdressActivity.this, "", "");
			}

			@Override
			public void onError(String error) {
				ToastUtils.showShortToast(TzmEditAdressActivity.this, error);
				ProgressDialogUtil.closeProgressDialog();
				super.onError(error);
			}

			@Override
			public void onException(Exception e) {
				super.onException(e);
			}

			@Override
			public void onComplete(String jsonStr) {
				ProgressDialogUtil.closeProgressDialog();
				Gson gson = new Gson();
				Type type = new TypeToken<BaseModel<TzmAddressDetailModel>>() {
				}.getType();
				BaseModel<TzmAddressDetailModel> base = gson.fromJson(jsonStr,
						type);
				if (base.success) {
					tzmAddressDetailModel = base.result;
					as_name.setText(tzmAddressDetailModel.name);
					as_phone.setText(tzmAddressDetailModel.tel);
					as_number.setText(tzmAddressDetailModel.postcodes);
					as_address.setText(tzmAddressDetailModel.address);
					tv_choseaddress.setText(tzmAddressDetailModel.provinceName
							+ tzmAddressDetailModel.cityName
							+ tzmAddressDetailModel.areaName);
					if (tzmAddressDetailModel.defaultStatus == 0) {
						cb_isdefault.setChecked(false);
					}
					if (tzmAddressDetailModel.defaultStatus == 1) {
						cb_isdefault.setChecked(true);
					}
					provinceId = tzmAddressDetailModel.province;
					cityId = tzmAddressDetailModel.city;
					areaId = tzmAddressDetailModel.area;
					mCurrentProviceName = tzmAddressDetailModel.provinceName;
					mCurrentCityName = tzmAddressDetailModel.cityName;
					mCurrentAreaName = tzmAddressDetailModel.areaName;
				}
			}
		}, true);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			mCurrentProviceName = data.getStringExtra("mCurrentProviceName");
			mCurrentCityName = data.getStringExtra("mCurrentCityName");
			mCurrentAreaName = data.getStringExtra("mCurrentAreaName");
			tv_choseaddress.setText(mCurrentProviceName + mCurrentCityName
					+ mCurrentAreaName);
			provinceId = data.getIntExtra("provinceId", 0);
			cityId = data.getIntExtra("cityId", 0);
			areaId = data.getIntExtra("areaId", 0);
			LogUtil.Log("provinceId:" + provinceId + ";cityId:" + cityId
					+ ";areaId:" + areaId);
		}
	}

	private void keep() {

		name = as_name.getText().toString();
		phone = as_phone.getText().toString();
		number = as_number.getText().toString();
		address = as_address.getText().toString();

		if (StringUtils.isBlank(name)
				|| calculateLength(as_name.getText().toString()) < 2) {
			ToastUtils.showShortToast(TzmEditAdressActivity.this, "名称长度不对哦");
			return;
		}
		if (StringUtils.isBlank(phone) || !StringUtils.isMobileNO(phone)) {
			ToastUtils.showShortToast(TzmEditAdressActivity.this, "请检查手机号码");
			return;
		}
		if (StringUtils.isBlank(number)) {
			ToastUtils.showShortToast(TzmEditAdressActivity.this, "请填好邮政编码");
			return;
		}
		if (StringUtils.isBlank(mCurrentProviceName)
				&& StringUtils.isBlank(mCurrentCityName)
				&& StringUtils.isBlank(mCurrentAreaName)) {
			ToastUtils.showShortToast(TzmEditAdressActivity.this, "请选择所在地区");
			return;
		}
		if (StringUtils.isBlank(address)) {
			ToastUtils.showShortToast(TzmEditAdressActivity.this, "请填上详细的联系地址");
			return;
		}
		if (cb_isdefault.isChecked()) {
			isDefault = 1;
		} else {
			isDefault = 0;
		}
		ProgressDialogUtil.openProgressDialog(TzmEditAdressActivity.this, null,
				null);
		uid = UserInfoUtils.getUserInfo().uid;
		apiClient = new ApiClient(this);
		tzmEidtAddressApi = new TzmEidtAddressApi();
		tzmEidtAddressApi.setUid(uid);
		tzmEidtAddressApi.setAddId(addId);
		tzmEidtAddressApi.setProvince(provinceId);
		tzmEidtAddressApi.setCity(cityId);
		tzmEidtAddressApi.setArea(areaId);
		tzmEidtAddressApi.setTel(phone);
		tzmEidtAddressApi.setName(name);
		tzmEidtAddressApi.setPostcode(number);
		tzmEidtAddressApi.setAddress(address);
		tzmEidtAddressApi.setIsDefault(isDefault);
		apiClient.api(tzmEidtAddressApi, new ApiListener() {

			@Override
			public void onStart() {

			}

			@Override
			public void onError(String error) {
				LogUtil.Log(error);
				ProgressDialogUtil.closeProgressDialog();
				ToastUtils.showShortToast(TzmEditAdressActivity.this, error);
			}

			@Override
			public void onException(Exception e) {
				super.onException(e);
			}

			@Override
			public void onComplete(String jsonStr) {
				ProgressDialogUtil.closeProgressDialog();
				if (StringUtils.isNotBlank(jsonStr)) {
					try {
						JSONObject json = new JSONObject(jsonStr);
						ToastUtils.showShortToast(TzmEditAdressActivity.this,
								json.optString("error_msg"));
						if (json.optBoolean("success")) {
							setResult(Activity.RESULT_OK);
							finish();
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}, true);
	}

	OnClickListener clickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_head_left:
				onBackPressed();
				// ProgressDialogUtil.openProgressDialog(TzmAddAdressActivity.this,
				// null, null);
				// startActivityForResult(new Intent(TzmAddAdressActivity.this,
				// CitiesActivity.class), 0);
				break;
			case R.id.tv_choseaddress:
				ProgressDialogUtil.openProgressDialog(
						TzmEditAdressActivity.this, null, null);
				startActivityForResult(new Intent(TzmEditAdressActivity.this,
						CitiesActivity.class), 0);
				break;

			case R.id.btn_head_right:
				keep();
				break;
			case R.id.iv_del:
				AlertDialog.Builder builder = new AlertDialog.Builder(
						TzmEditAdressActivity.this);
				builder.setTitle("确认删除该地址？")
						.setPositiveButton("删除",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										Delete();
									}
								})
						.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										dialog.cancel();
									}
								});
				AlertDialog alert = builder.create();
				alert.show();
				break;

			}
		}
	};

	protected void Delete() {
		addressApi = new TzmDelAddressApi();
		apiClient = new ApiClient(this);
		addressApi.setUid(userModel.uid);
		addressApi.setAddId(addId);
		apiClient.api(addressApi, new ApiListener() {

			@Override
			public void onStart() {
				ProgressDialogUtil.openProgressDialog(
						TzmEditAdressActivity.this, "", "");

			}

			@Override
			public void onError(String error) {
				LogUtil.Log(error);
				ProgressDialogUtil.closeProgressDialog();
				ToastUtils.showShortToast(TzmEditAdressActivity.this, error);
			}

			@Override
			public void onComplete(String jsonStr) {
				ProgressDialogUtil.closeProgressDialog();
				if (StringUtils.isNotBlank(jsonStr)) {
					try {
						JSONObject jsonObject = new JSONObject(jsonStr);
						boolean success = jsonObject.optBoolean("success");
						int result = jsonObject.optInt("result");
						String error_msg = jsonObject.optString("error_msg");
						ToastUtils.showShortToast(TzmEditAdressActivity.this,
								error_msg);
						if (success) {
							setResult(Activity.RESULT_OK);
							finish();
						}

					} catch (JSONException e) {
						e.printStackTrace();
					}

				}

			}

		}, true);
	}

	private void checkLogin() {
		isLogin = UserInfoUtils.isLogin();
		if (isLogin) {
			userModel = UserInfoUtils.getUserInfo();
		}

	}

	// 限2-16个字符，支持中英文、数字、下划线
	private TextWatcher textWatcher = new TextWatcher() {
		// 监听改变的文本框
		private int editStart;
		private int editEnd;
		private int maxLen = 16; // the max byte

		@Override
		public void onTextChanged(CharSequence ss, int start, int before,
				int count) {
			String editable = as_name.getText().toString();
			String str = StringFilter(editable.toString());
			if (!editable.equals(str)) {
				as_name.setText(str);
				// 设置新的光标所在位置
				as_name.setSelection(str.length());
			}
		}

		@Override
		public void afterTextChanged(Editable s) {
			editStart = as_name.getSelectionStart();
			editEnd = as_name.getSelectionEnd();
			// 先去掉监听器，否则会出现栈溢出
			as_name.removeTextChangedListener(textWatcher);
			if (!TextUtils.isEmpty(as_name.getText())) {
				String etstring = as_name.getText().toString().trim();
				while (calculateLength(s.toString()) > maxLen) {
					s.delete(editStart - 1, editEnd);
					editStart--;
					editEnd--;
				}
			}

			as_name.setText(s);
			as_name.setSelection(editStart);

			// 恢复监听器
			as_name.addTextChangedListener(textWatcher);
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}

	};

	private int calculateLength(String etstring) {
		char[] ch = etstring.toCharArray();

		int varlength = 0;
		for (int i = 0; i < ch.length; i++) {
			// changed by zyf 0825 , bug 6918，加入中文标点范围 ， TODO 标点范围有待具体化
			if ((ch[i] >= 0x2E80 && ch[i] <= 0xFE4F)
					|| (ch[i] >= 0xA13F && ch[i] <= 0xAA40) || ch[i] >= 0x80) { // 中文字符范围0x4e00
																				// 0x9fbb
				varlength = varlength + 2;
			} else {
				varlength++;
			}
		}
		return varlength;
	}

	// 只允许中英文、数字、下划线
	public static String StringFilter(String str) throws PatternSyntaxException {
		String regEx = "[^a-zA-Z0-9\u4E00-\u9FA5_]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}
}
