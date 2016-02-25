package com.ruiyu.taozhuma.activity;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.api.ApiClient;
import com.ruiyu.taozhuma.api.ApiListener;
import com.ruiyu.taozhuma.api.ApplyAgencyApi;
import com.ruiyu.taozhuma.api.TzmAddressApi;
import com.ruiyu.taozhuma.api.TzmProvinceApi;
import com.ruiyu.taozhuma.base.BaseApplication;
import com.ruiyu.taozhuma.model.BaseModel;
import com.ruiyu.taozhuma.model.OldTzmProvinceModel;
import com.ruiyu.taozhuma.model.TzmProvinceModel;
import com.ruiyu.taozhuma.model.UserModel;
import com.ruiyu.taozhuma.utils.LogUtil;
import com.ruiyu.taozhuma.utils.PictureUtil;
import com.ruiyu.taozhuma.utils.ProgressDialogUtil;
import com.ruiyu.taozhuma.utils.StringUtils;
import com.ruiyu.taozhuma.utils.ToastUtils;
import com.ruiyu.taozhuma.utils.UserInfoUtils;

public class TzmAddDataActivity extends Activity {
	private ImageView btn_head_left;
	private EditText et_contact, et_tel, et_phone, et_fax, et_addressHome,
			et_addressCompany, et_corporation, et_company, et_email, et_QQ;

	private String contact, tel, phone, fax, addressHome, addressCompany,
			corporation, company, email, QQ;
	private int provinceId, cityId, areaId;
	private int shopId = -1;
	private long uid;
	private UserModel userInfo;
	private String shopName;

	private RelativeLayout rl_busLicence, rl_agentBrand;
	// private LinearLayout ll_agentArea;
	private ImageView image__busLicence;
	private CheckBox cb_check;
	private TextView tv_check, tv_shop_name;
	// private TextView tv_ProviceName,tv_CityName,tv_DistrictName;
	private TextView tv_submit;
	private Spinner as_province, as_city, as_counties;

	private String ImagePath;

	// private int CITIES = 11;
	private int SELECTSHOP = 22;

	private Handler handler = new Handler();

	private ScrollView scrollView;

	private LinearLayout inner;

	private ApplyAgencyApi agencyApi;

	private ApiClient apiClient2, apiClient3, apiClient4;
	private TzmProvinceApi tzmProvinceApi;
	private TzmProvinceApi tzmProvinceApi2;
	private TzmProvinceApi tzmProvinceApi3;

	private ArrayList<OldTzmProvinceModel> tzmProvinceModels;
	private String[] provinces;
	private ArrayList<OldTzmProvinceModel> tzmProvinceModels2;
	private String[] citys;
	private ArrayList<OldTzmProvinceModel> tzmProvinceModels3;
	private String[] counties;
	private ArrayAdapter<String> adapter, adapter2, adapter3;

	private int classid, classid2, classid3;
	private String province, city, area;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tzm_adddata_activity);
		userInfo = BaseApplication.getInstance().getLoginUser();
		initView();
		province();

	}

	private void initView() {
		btn_head_left=(ImageView)findViewById(R.id.btn_head_left);
		btn_head_left.setOnClickListener(clickListener);
		et_contact = (EditText) findViewById(R.id.et_contact);
		et_tel = (EditText) findViewById(R.id.et_tel);
		et_phone = (EditText) findViewById(R.id.et_phone);
		et_fax = (EditText) findViewById(R.id.et_fax);
		et_addressHome = (EditText) findViewById(R.id.et_addressHome);
		et_addressCompany = (EditText) findViewById(R.id.et_addressCompany);
		et_corporation = (EditText) findViewById(R.id.et_corporation);
		et_company = (EditText) findViewById(R.id.et_company);
		et_email = (EditText) findViewById(R.id.et_email);
		et_QQ = (EditText) findViewById(R.id.et_QQ);

		as_province = (Spinner) findViewById(R.id.as_province);
		as_city = (Spinner) findViewById(R.id.as_city);
		as_counties = (Spinner) findViewById(R.id.as_counties);

		rl_busLicence = (RelativeLayout) findViewById(R.id.rl_busLicence);
		// ll_agentArea = (LinearLayout) findViewById(R.id.ll_agentArea);
		rl_agentBrand = (RelativeLayout) findViewById(R.id.rl_agentBrand);

		image__busLicence = (ImageView) findViewById(R.id.image__busLicence);

		rl_busLicence.setOnClickListener(clickListener);
		// ll_agentArea.setOnClickListener(clickListener);
		rl_agentBrand.setOnClickListener(clickListener);

		scrollView = (ScrollView) findViewById(R.id.scrollView);

		inner = (LinearLayout) findViewById(R.id.inner);

		cb_check = (CheckBox) findViewById(R.id.cb_check);
		cb_check.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {

			}
		});

		tv_check = (TextView) findViewById(R.id.tv_check);
		tv_shop_name = (TextView) findViewById(R.id.tv_shop_name);
		tv_check.setOnClickListener(clickListener);
		// tv_ProviceName=(TextView) findViewById(R.id.tv_ProviceName);
		// tv_CityName=(TextView) findViewById(R.id.tv_CityName);
		// tv_DistrictName=(TextView) findViewById(R.id.tv_DistrictName);
		//
		// tv_ProviceName.setOnClickListener(clickListener);
		// tv_CityName.setOnClickListener(clickListener);
		// tv_DistrictName.setOnClickListener(clickListener);

		tv_submit = (TextView) findViewById(R.id.tv_submit);
		tv_submit.setOnClickListener(clickListener);
	}

	View.OnClickListener clickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_head_left:
				onBackPressed();
				break;
			case R.id.rl_busLicence:
				selectPic(R.id.image__busLicence);
				break;
			// case R.id.ll_agentArea:
			// v.setClickable(false);
			// scrollToBottom(scrollView, inner);
			//
			// Intent intent2 = new Intent(TzmAddDataActivity.this,
			// CitiesActivity.class);
			// startActivityForResult(intent2, CITIES);
			// v.setClickable(true);
			// break;
			// case R.id.tv_ProviceName:
			// as_province.performClick();
			// sp_mainCategory.setVisibility(View.VISIBLE);
			// tv_mainCategory.setVisibility(View.GONE);
			// v.setVisibility(View.GONE);
			// break;
			// case R.id.tv_CityName:
			//
			// break;
			// case R.id.tv_DistrictName:
			//
			// break;
			case R.id.rl_agentBrand:
				Intent intent_agentBrand = new Intent(TzmAddDataActivity.this,
						SelectShopActivity.class);
				startActivityForResult(intent_agentBrand, SELECTSHOP);
				break;
			case R.id.tv_check:
				if (cb_check.isChecked()) {
					cb_check.setChecked(false);
				} else {
					cb_check.setChecked(true);
				}
				break;
			case R.id.tv_submit:
				postSubmit();
				break;

			}
		}
	};

	class SpinnerSelectedListener implements OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			classid = tzmProvinceModels.get(arg2).id;
			city();
		}

		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}

	class SpinnerSelectedListener2 implements OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			classid2 = tzmProvinceModels2.get(arg2).id;

			counties();

		}

		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}

	class SpinnerSelectedListener3 implements OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			classid3 = tzmProvinceModels3.get(arg2).id;
		}

		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}

	private void selectPic(int imgId) {
		Intent intent1 = new Intent(this, SelectPicActivity.class);
		intent1.putExtra("imgId", imgId);
		startActivityForResult(intent1, imgId);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {

			// if (requestCode == CITIES) {//设置省市区
			// tv_CityName.setText(data.getStringExtra("mCurrentProviceName"));
			// tv_CityName.setTextColor(Color.parseColor("#727171"));
			// tv_ProviceName.setText(data.getStringExtra("mCurrentCityName"));
			// tv_ProviceName.setTextColor(Color.parseColor("#727171"));
			// tv_DistrictName.setText(data.getStringExtra("mCurrentDistrictName"));
			// tv_DistrictName.setTextColor(Color.parseColor("#727171"));
			// } else {
			if (requestCode == SELECTSHOP) {
				shopId = data.getIntExtra("id", -1);
				shopName = data.getStringExtra("shopName");
				if (StringUtils.isNotBlank(shopName)
						&& StringUtils.isNotEmpty(shopName)) {
					tv_shop_name.setText(shopName);
				}
			} else {
				// imageView不设null, 第一次上传成功后，第二次在选择上传的时候会报错。
				ImageView img = (ImageView) findViewById(requestCode);
				img.setImageBitmap(null);
				String picPath = data
						.getStringExtra(SelectPicActivity.KEY_PHOTO_PATH);
				LogUtil.Log(picPath);

				if (picPath != null && !StringUtils.isBlank(picPath)) {
					LogUtil.Log(requestCode + "最终选择的图片=" + picPath);
					Bitmap bm = PictureUtil.getSmallBitmap(picPath);
					img.setImageBitmap(bm);
					ImagePath = picPath;
				}

			}
			// }
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	// 滚到底部
	// public static void scrollToBottom(final View scroll, final View inner) {
	//
	// scroll.post(new Runnable() {
	// public void run() {
	// ((ScrollView) scroll).fullScroll(ScrollView.FOCUS_DOWN);
	// }
	// });
	// }

	private void init(String[] provinces) {
		adapter = new ArrayAdapter<String>(TzmAddDataActivity.this,
				android.R.layout.simple_spinner_item, provinces);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		as_province.setAdapter(adapter);
		as_province.setOnItemSelectedListener(new SpinnerSelectedListener());
		for (int i = 0; i < provinces.length; i++) {
			if (provinces[i].equals(province)) {
				as_province.setSelection(i, true);
				break;
			}
		}
	}

	private void province() {
		apiClient2 = new ApiClient(this);
		tzmProvinceApi = new TzmProvinceApi();
		tzmProvinceApi.setPid(0);
		apiClient2.api(tzmProvinceApi, new ApiListener() {

			@Override
			public void onStart() {
				ProgressDialogUtil.openProgressDialog(TzmAddDataActivity.this,
						"", "正在加载...");
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
			}

			@Override
			public void onComplete(String jsonStr) {
				ProgressDialogUtil.closeProgressDialog();
				try {
					Gson gson = new Gson();
					Type type = new TypeToken<BaseModel<ArrayList<OldTzmProvinceModel>>>() {
					}.getType();
					BaseModel<ArrayList<OldTzmProvinceModel>> base = gson
							.fromJson(jsonStr, type);

					if (base.success) {
						tzmProvinceModels = base.result;
						provinces = new String[tzmProvinceModels.size()];
						for (int i = 0; i < tzmProvinceModels.size(); i++) {
							provinces[i] = tzmProvinceModels.get(i).name;
						}
					}
					init(provinces);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		}, true);
	}

	private void city() {
		apiClient3 = new ApiClient(this);
		tzmProvinceApi2 = new TzmProvinceApi();

		tzmProvinceApi2.setPid(classid);
		apiClient3.api(tzmProvinceApi2, new ApiListener() {

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
				ToastUtils.showShortToast(TzmAddDataActivity.this, error);
			}

			@Override
			public void onComplete(String jsonStr) {
				try {
					Gson gson = new Gson();
					Type type = new TypeToken<BaseModel<ArrayList<OldTzmProvinceModel>>>() {
					}.getType();
					BaseModel<ArrayList<OldTzmProvinceModel>> base = gson
							.fromJson(jsonStr, type);

					if (base.success) {
						tzmProvinceModels2 = base.result;
						citys = new String[tzmProvinceModels2.size()];
						for (int i = 0; i < tzmProvinceModels2.size(); i++) {
							citys[i] = tzmProvinceModels2.get(i).name;
						}

					}
					adapter2 = new ArrayAdapter<String>(
							TzmAddDataActivity.this,
							android.R.layout.simple_spinner_item, citys);
					adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					as_city.setAdapter(adapter2);
					as_city.setOnItemSelectedListener(new SpinnerSelectedListener2());
					for (int i = 0; i < citys.length; i++) {
						if (citys[i].equals(city)) {
							as_city.setSelection(i, true);
							break;
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		}, true);
	}

	private void counties() {
		apiClient4 = new ApiClient(this);
		tzmProvinceApi3 = new TzmProvinceApi();
		tzmProvinceApi3.setPid(classid2);
		apiClient4.api(tzmProvinceApi3, new ApiListener() {

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
				ToastUtils.showShortToast(TzmAddDataActivity.this, error);
			}

			@Override
			public void onComplete(String jsonStr) {
				try {
					Gson gson = new Gson();
					Type type = new TypeToken<BaseModel<ArrayList<OldTzmProvinceModel>>>() {
					}.getType();
					BaseModel<ArrayList<OldTzmProvinceModel>> base = gson
							.fromJson(jsonStr, type);

					if (base.success) {
						tzmProvinceModels3 = base.result;
						counties = new String[tzmProvinceModels3.size()];
						for (int i = 0; i < tzmProvinceModels3.size(); i++) {
							counties[i] = tzmProvinceModels3.get(i).name;
						}

					}
					adapter3 = new ArrayAdapter<String>(
							TzmAddDataActivity.this,
							android.R.layout.simple_spinner_item, counties);
					adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					as_counties.setAdapter(adapter3);
					as_counties
							.setOnItemSelectedListener(new SpinnerSelectedListener3());
					for (int i = 0; i < counties.length; i++) {
						if (counties[i].equals(area)) {
							as_counties.setSelection(i, true);
							break;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		}, true);
	}

	private void postSubmit() {
		uid = userInfo.uid;
		contact = et_contact.getText().toString();
		tel = et_tel.getText().toString();
		phone = et_phone.getText().toString();
		fax = et_fax.getText().toString();
		addressHome = et_addressHome.getText().toString();
		addressCompany = et_addressCompany.getText().toString();
		corporation = et_corporation.getText().toString();
		company = et_company.getText().toString();
		email = et_email.getText().toString();
		QQ = et_QQ.getText().toString();

		if (StringUtils.isBlank(contact)) {
			ToastUtils.showShortToast(TzmAddDataActivity.this, "请输入联系人");
			return;
		}
		if (StringUtils.isBlank(phone)) {
			ToastUtils.showShortToast(TzmAddDataActivity.this, "请输入手机号码");
			return;
		}
		if (!StringUtils.isMobileNO(phone)) {
			ToastUtils.showShortToast(TzmAddDataActivity.this, "检查联系人手机号码");
			return;
		}
		if (StringUtils.isBlank(addressCompany)) {
			ToastUtils.showShortToast(TzmAddDataActivity.this, "请输入公司地址");
			return;
		}
		if (StringUtils.isBlank(corporation)) {
			ToastUtils.showShortToast(TzmAddDataActivity.this, "请输入法人代表");
			return;
		}
		if (StringUtils.isBlank(ImagePath)) {
			ToastUtils.showShortToast(TzmAddDataActivity.this, "请上传授权认证书");
			return;
		}
		if(shopId==-1){
			ToastUtils.showShortToast(TzmAddDataActivity.this, "请选择代理品牌");
			return;
		}
		if (StringUtils.isBlank(company)) {
			ToastUtils.showShortToast(TzmAddDataActivity.this, "请输入公司名称");
			return;
		}
		if (StringUtils.isBlank(email)) {
			ToastUtils.showShortToast(TzmAddDataActivity.this, "请输入联系人邮箱");
			return;
		}
		if (!StringUtils.isEmailStr(email)) {
			ToastUtils.showShortToast(TzmAddDataActivity.this, "请检查联系人邮箱");
			return;
		}
		if (StringUtils.isBlank(QQ)) {
			ToastUtils.showShortToast(TzmAddDataActivity.this, "请输入联系人QQ");
			return;
		}

		if(cb_check.isChecked()){
		}else{
			ToastUtils.showShortToast(TzmAddDataActivity.this, "请仔细阅读条款与协议...");
			return;
		}
		
		agencyApi = new ApplyAgencyApi();
		RequestParams params = new RequestParams();

		params.addBodyParameter("uid", uid + "");
		params.addBodyParameter("contact", contact + "");
		params.addBodyParameter("tel", tel + "");
		params.addBodyParameter("phone", phone + "");
		params.addBodyParameter("fax", fax + "");
		params.addBodyParameter("addressHome", addressHome + "");
		params.addBodyParameter("addressCompany", addressCompany + "");
		params.addBodyParameter("corporation", corporation + "");
		params.addBodyParameter("company", company + "");
		params.addBodyParameter("email", email + "");
		params.addBodyParameter("QQ", QQ + "");
		params.addBodyParameter("file1", new File(ImagePath));
		params.addBodyParameter("province", classid + "");
		params.addBodyParameter("city", classid2 + "");
		params.addBodyParameter("area", classid3 + "");
		params.addBodyParameter("manufacturerId", shopId + "");
		params.addBodyParameter("proxyProduct", 0 + "");
		params.addBodyParameter("mainCategory", 0 + "");	

		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.POST, agencyApi.getUrl(), params,
				new RequestCallBack<String>() {

					@Override
					public void onStart() {
						ProgressDialogUtil.openProgressDialog(
								TzmAddDataActivity.this, "", "正在提交...");
					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						if (isUploading) {
							// testTextView.setText("upload: " + current + "/" +
							// total);
						} else {
							// testTextView.setText("reply: " + current + "/" +
							// total);
						}
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						// testTextView.setText("reply: " +
						// responseInfo.result);

						ProgressDialogUtil.closeProgressDialog();
						if (StringUtils.isNotBlank(responseInfo.result)) {
							try {
								JSONObject json = new JSONObject(
										responseInfo.result);
								ToastUtils.showShortToast(
										TzmAddDataActivity.this,
										json.getString("error_msg"));
								if (json.getBoolean("success")) {
									userInfo.judgeType = 6;
									UserInfoUtils.updateUserInfo(userInfo);
									// finish();
									Intent intent = new Intent();
									setResult(Activity.RESULT_OK, intent);
									finish();

								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						// testTextView.setText(error.getExceptionCode() + ":" +
						// msg);
						ToastUtils.showShortToast(TzmAddDataActivity.this,
								error.getExceptionCode() + ":" + msg);
						ProgressDialogUtil.closeProgressDialog();
					}
				});
	}

}
