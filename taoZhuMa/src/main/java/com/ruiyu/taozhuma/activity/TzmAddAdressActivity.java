package com.ruiyu.taozhuma.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.api.ApiClient;
import com.ruiyu.taozhuma.api.ApiListener;
import com.ruiyu.taozhuma.api.TzmAddressApi;
import com.ruiyu.taozhuma.api.TzmAddressDetailApi;
import com.ruiyu.taozhuma.api.TzmDelAddressApi;
import com.ruiyu.taozhuma.api.TzmEidtAddressApi;
import com.ruiyu.taozhuma.api.TzmProvinceApi;
import com.ruiyu.taozhuma.model.BaseModel;
import com.ruiyu.taozhuma.model.OldTzmProvinceModel;
import com.ruiyu.taozhuma.model.TzmAddressDetailModel;
import com.ruiyu.taozhuma.model.UserModel;
import com.ruiyu.taozhuma.utils.LogUtil;
import com.ruiyu.taozhuma.utils.ProgressDialogUtil;
import com.ruiyu.taozhuma.utils.StringUtils;
import com.ruiyu.taozhuma.utils.ToastUtils;
import com.ruiyu.taozhuma.utils.UserInfoUtils;

public class TzmAddAdressActivity extends Activity {

	private TextView txt_head_title;
	private Button btn_head_left;
	private Button btn_head_right;
	private EditText as_name,as_phone,as_number,as_address;
	private Integer uid;
	private String name,phone,number,address;
	private Spinner as_province,as_city,as_counties;
	private ImageView iv_delete;
	private TextView tv_province,tv_city,tv_counties;
	
	
	private ApiClient apiClient,apiClient2,apiClient3,apiClient4,apiClient5,client;
	private TzmAddressApi tzmAddressApi;
	private TzmProvinceApi tzmProvinceApi;
	private TzmProvinceApi tzmProvinceApi2;
	private TzmProvinceApi tzmProvinceApi3;
	private TzmAddressDetailApi tzmAddressDetailApi;
	private TzmEidtAddressApi tzmEidtAddressApi;
	
	private ArrayList<OldTzmProvinceModel> tzmProvinceModels;
	private String[] provinces;
	private ArrayList<OldTzmProvinceModel> tzmProvinceModels2;
	private String[] citys;
	private ArrayList<OldTzmProvinceModel> tzmProvinceModels3;
	private String[] counties;
	private ArrayAdapter<String> adapter, adapter2, adapter3;
	
	private int classid,classid2,classid3;
	private String province,city,area;
	private Long addId;
	private TzmAddressDetailModel tzmAddressDetailModel;
	
	private Boolean isLogin;
	private UserModel userModel;
	private TzmDelAddressApi addressApi;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tzm_add_adress_activity);
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();  
		addId = bundle.getLong("addId", 0);
		
		checkLogin();
		
		txt_head_title = (TextView) findViewById(R.id.txt_head_title);
		btn_head_left = (Button) findViewById(R.id.btn_head_left);
		btn_head_left.setOnClickListener(clickListener);
		btn_head_right=(Button) findViewById(R.id.btn_head_right);
		btn_head_right.setVisibility(View.VISIBLE);
		btn_head_right.setText("完成");
		btn_head_right.setOnClickListener(clickListener);
		as_name=(EditText) findViewById(R.id.as_name);
		as_phone=(EditText) findViewById(R.id.as_phone);
		as_number=(EditText) findViewById(R.id.as_number);
		as_address=(EditText) findViewById(R.id.as_address);
		as_province=(Spinner) findViewById(R.id.as_province);
		tv_province=(TextView) findViewById(R.id.tv_province);
		tv_province.setOnClickListener(clickListener);
		as_city=(Spinner) findViewById(R.id.as_city);
		tv_city=(TextView) findViewById(R.id.tv_city);
		tv_city.setOnClickListener(clickListener);
		as_counties=(Spinner) findViewById(R.id.as_counties);
		tv_counties=(TextView) findViewById(R.id.tv_counties);
		tv_counties.setOnClickListener(clickListener);
		iv_delete= (ImageView) findViewById(R.id.iv_delete);
		if (addId == -1) {// 新增地址
			txt_head_title.setText("新增地址");
			iv_delete.setVisibility(View.GONE);
			province();
		} else {// 修改地址
			txt_head_title.setText("编辑地址");
			iv_delete.setVisibility(View.VISIBLE);
			iv_delete.setOnClickListener(clickListener);
			tv_province.setVisibility(View.GONE);
			tv_city.setVisibility(View.GONE);
			tv_counties.setVisibility(View.GONE);
			as_province.setVisibility(View.VISIBLE);
			as_city.setVisibility(View.VISIBLE);
			as_counties.setVisibility(View.VISIBLE);
			initdata();
		}
	}
	
	private void initdata() {
		tzmAddressDetailApi=new TzmAddressDetailApi();
		apiClient5=new ApiClient(this);
		tzmAddressDetailApi.setUid(UserInfoUtils.getUserInfo().uid);
		tzmAddressDetailApi.setAddId(addId);
		apiClient5.api(tzmAddressDetailApi, new ApiListener() {
			
			@Override
			public void onStart() {
				ProgressDialogUtil.openProgressDialog(
						TzmAddAdressActivity.this, "", "");
			}
			@Override
			public void onError(String error) {
				ProgressDialogUtil.closeProgressDialog();
				super.onError(error);
			}
			@Override
			public void onException(Exception e) {
				ProgressDialogUtil.closeProgressDialog();
				super.onException(e);
			}
			@Override
			public void onComplete(String jsonStr) {
				ProgressDialogUtil.closeProgressDialog();
				Gson gson = new Gson();
				Type type = new TypeToken<BaseModel<TzmAddressDetailModel>>() {
				}.getType();
				BaseModel<TzmAddressDetailModel> base = gson.fromJson(
						jsonStr, type);
				if (base.success) {
					tzmAddressDetailModel = base.result;
					as_name.setText(tzmAddressDetailModel.name);
					as_phone.setText(tzmAddressDetailModel.tel);
					as_number.setText(tzmAddressDetailModel.postcodes);
					as_address.setText(tzmAddressDetailModel.address);
					province=tzmAddressDetailModel.provinceName;
					city=tzmAddressDetailModel.cityName;
					area=tzmAddressDetailModel.areaName;
					province();
				}
			}
		}, true);
	}

	private void init(String[] provinces) {
		adapter = new ArrayAdapter<String>(TzmAddAdressActivity.this,
				android.R.layout.simple_spinner_item, provinces);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		as_province.setAdapter(adapter);
		as_province.setOnItemSelectedListener(new SpinnerSelectedListener());
		for(int i=0;i<provinces.length;i++){
			if(provinces[i].equals(province)){
				as_province.setSelection(i, true);
				break; 
			}
		}
	}

	private void keep() {
		btn_head_right.setClickable(false);
		uid=UserInfoUtils.getUserInfo().uid;
		name=as_name.getText().toString();
		phone=as_phone.getText().toString();
		number=as_number.getText().toString();
		address=as_address.getText().toString();
		if(StringUtils.isBlank(name)){
			btn_head_right.setClickable(true);
			ToastUtils.showShortToast(TzmAddAdressActivity.this, "名字不能为空哦");
			return;
		}
		if(StringUtils.isBlank(phone)){
			btn_head_right.setClickable(true);
			ToastUtils.showShortToast(TzmAddAdressActivity.this, "电话号码不能为空哦");
			return;
		}
		if(StringUtils.isBlank(number)){
			btn_head_right.setClickable(true);
			ToastUtils.showShortToast(TzmAddAdressActivity.this, "请填好邮政编码");
			return;
		}
		if(tv_province.getVisibility()!=View.GONE){
			btn_head_right.setClickable(true);
			ToastUtils.showShortToast(TzmAddAdressActivity.this, "请选择省份");
			return;
		}
		if(tv_city.getVisibility()!=View.GONE){
			btn_head_right.setClickable(true);
			ToastUtils.showShortToast(TzmAddAdressActivity.this, "请选择城市");
			return;
		}
		if(tv_counties.getVisibility()!=View.GONE){
			btn_head_right.setClickable(true);
			ToastUtils.showShortToast(TzmAddAdressActivity.this, "请选择区县");
			return;
		}
		if(StringUtils.isBlank(address)){
			btn_head_right.setClickable(true);
			ToastUtils.showShortToast(TzmAddAdressActivity.this, "请填上详细的联系地址");
			return;
		}
		
		if(addId==-1){//保存新增的地址
			apiClient = new ApiClient(this);
			tzmAddressApi = new TzmAddressApi();
			tzmAddressApi.setUid(uid);
			tzmAddressApi.setProvince(classid);
			tzmAddressApi.setCity(classid2);
			tzmAddressApi.setArea(classid3);
			tzmAddressApi.setAddress(address);
			tzmAddressApi.setName(name);
			tzmAddressApi.setTel(phone);
			tzmAddressApi.setPostCode(number);
			apiClient.api(tzmAddressApi, new ApiListener() {
				
				@Override
				public void onStart() {
					
				}
				@Override
				public void onError(String error) {
					btn_head_right.setClickable(false);
					LogUtil.Log(error);
					ToastUtils.showShortToast(TzmAddAdressActivity.this, error);
				}
				@Override
				public void onException(Exception e) {
					btn_head_right.setClickable(false);
					super.onException(e);
				}
				@Override
				public void onComplete(String jsonStr) {
					btn_head_right.setClickable(false);
					if (StringUtils.isNotBlank(jsonStr)) {
						try {
							JSONObject json = new JSONObject(jsonStr);
							if(json.optBoolean("success")){
								ToastUtils.showShortToast(TzmAddAdressActivity.this,
										json.optString("error_msg"));
							}else{
								LogUtil.Log(json.optString("error_msg"));
								ToastUtils.showShortToast(TzmAddAdressActivity.this,
										json.optString("error_msg"));
							}
							btn_head_right.setClickable(false);
							finish();
						} catch (Exception e) {
							e.printStackTrace();
						}
						
					}
				}
			}, true);
			
		}else{//保存修改的地址
			apiClient = new ApiClient(this);
			tzmEidtAddressApi=new TzmEidtAddressApi();
			tzmEidtAddressApi.setUid(uid);
			tzmEidtAddressApi.setAddId(addId);
			tzmEidtAddressApi.setProvince(classid);
			tzmEidtAddressApi.setCity(classid2);
			tzmEidtAddressApi.setArea(classid3);
			tzmEidtAddressApi.setTel(phone);
			tzmEidtAddressApi.setName(name);
			tzmEidtAddressApi.setPostcode(number);
			tzmEidtAddressApi.setAddress(address);
			apiClient.api(tzmEidtAddressApi, new ApiListener() {
				
				@Override
				public void onStart() {
					
				}
				@Override
				public void onError(String error) {
					btn_head_right.setClickable(false);
					LogUtil.Log(error);
					ToastUtils.showShortToast(TzmAddAdressActivity.this, error);
				}
				@Override
				public void onException(Exception e) {
					btn_head_right.setClickable(false);
					super.onException(e);
				}
				@Override
				public void onComplete(String jsonStr) {
					if (StringUtils.isNotBlank(jsonStr)) {
						try {
							JSONObject json = new JSONObject(jsonStr);
							if(json.optBoolean("success")){
								ToastUtils.showShortToast(TzmAddAdressActivity.this,
										json.optString("error_msg"));
							}else{
								LogUtil.Log(json.optString("error_msg"));
								ToastUtils.showShortToast(TzmAddAdressActivity.this,
										json.optString("error_msg"));
							}
							btn_head_right.setClickable(false);
							finish();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}, true);
		}
	}
		
	private void province(){
		apiClient2 = new ApiClient(this);
		tzmProvinceApi = new TzmProvinceApi();
		tzmProvinceApi.setPid(0);
		apiClient2.api(tzmProvinceApi, new ApiListener() {

			@Override
			public void onStart() {
				 ProgressDialogUtil.openProgressDialog(TzmAddAdressActivity.this,
				 "",
				 "正在加载...");
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
				ToastUtils.showShortToast(TzmAddAdressActivity.this, error);
			}

			@Override
			public void onComplete(String jsonStr) {
				ProgressDialogUtil.closeProgressDialog();
				try {
					Gson gson = new Gson();
					Type type = new TypeToken<BaseModel<ArrayList<OldTzmProvinceModel>>>() {
					}.getType();
					BaseModel<ArrayList<OldTzmProvinceModel>> base = gson.fromJson(
							jsonStr, type);
					
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
	
	private void city (){
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
				ToastUtils.showShortToast(TzmAddAdressActivity.this, error);
			}

			@Override
			public void onComplete(String jsonStr) {
				try {
					Gson gson = new Gson();
					Type type = new TypeToken<BaseModel<ArrayList<OldTzmProvinceModel>>>() {
					}.getType();
					BaseModel<ArrayList<OldTzmProvinceModel>> base = gson.fromJson(
							jsonStr, type);
					
					if (base.success) {
						tzmProvinceModels2 = base.result;
						citys = new String[tzmProvinceModels2.size()];
						for (int i = 0; i < tzmProvinceModels2.size(); i++) {
							citys[i] = tzmProvinceModels2.get(i).name;
						}
						
					}
					adapter2 = new ArrayAdapter<String>(TzmAddAdressActivity.this,
							android.R.layout.simple_spinner_item, citys);
					adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					as_city.setAdapter(adapter2);
					as_city.setOnItemSelectedListener(new SpinnerSelectedListener2());
					for(int i=0;i<citys.length;i++){
						if(citys[i].equals(city)){
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
				ToastUtils.showShortToast(TzmAddAdressActivity.this, error);
			}

			@Override
			public void onComplete(String jsonStr) {
				try {
					Gson gson = new Gson();
					Type type = new TypeToken<BaseModel<ArrayList<OldTzmProvinceModel>>>() {
					}.getType();
					BaseModel<ArrayList<OldTzmProvinceModel>> base = gson.fromJson(
							jsonStr, type);
					
					if (base.success) {
						tzmProvinceModels3 = base.result;
						counties = new String[tzmProvinceModels3.size()];
						for (int i = 0; i < tzmProvinceModels3.size(); i++) {
							counties[i] = tzmProvinceModels3.get(i).name;
						}
						
					}
					adapter3 = new ArrayAdapter<String>(TzmAddAdressActivity.this,
							android.R.layout.simple_spinner_item, counties);
					adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					as_counties.setAdapter(adapter3);
					as_counties.setOnItemSelectedListener(new SpinnerSelectedListener3());
					for(int i=0;i<counties.length;i++){
						if(counties[i].equals(area)){
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
	
	OnClickListener clickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_head_left:
				onBackPressed();
				//ProgressDialogUtil.openProgressDialog(TzmAddAdressActivity.this, null, null);
				//startActivityForResult(new Intent(TzmAddAdressActivity.this, CitiesActivity.class), 0);
				break;
			case R.id.btn_head_right:
				keep();
				break;
			case R.id.tv_province:
				as_province.performClick();
				as_province.setVisibility(View.VISIBLE);
				tv_province.setVisibility(View.GONE);
				break;
			case R.id.tv_city:
				as_city.performClick();
				as_city.setVisibility(View.VISIBLE);
				tv_city.setVisibility(View.GONE);
				break;
			case R.id.tv_counties:
				as_counties.performClick();
				as_counties.setVisibility(View.VISIBLE);
				tv_counties.setVisibility(View.GONE);
				break;
			case R.id.iv_delete:
				AlertDialog.Builder builder = new AlertDialog.Builder(TzmAddAdressActivity.this);
				builder.setTitle("操作")
						.setPositiveButton("删除", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Delete();
							}
						})
						.setNegativeButton("取消", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
				AlertDialog alert = builder.create();
				alert.show();
				
				break;
			}
		}
	};
	
	private void checkLogin() {
		isLogin = UserInfoUtils.isLogin();
		if (isLogin) {
			userModel = UserInfoUtils.getUserInfo();
		}

	}
	
	protected void Delete() {
		addressApi = new TzmDelAddressApi();
		client = new ApiClient(this);
		addressApi.setUid(userModel.uid);
		addressApi.setAddId(Long.parseLong(addId+""));
		client.api(addressApi, new ApiListener() {

			@Override
			public void onStart() {
				ProgressDialogUtil.openProgressDialog(
						TzmAddAdressActivity.this, "", "");

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
						ToastUtils.showShortToast(TzmAddAdressActivity.this,
								error_msg);
						if (success) {
							finish();
						}

					} catch (JSONException e) {
						e.printStackTrace();
					}

				}

			}

		}, true);
	}
}
