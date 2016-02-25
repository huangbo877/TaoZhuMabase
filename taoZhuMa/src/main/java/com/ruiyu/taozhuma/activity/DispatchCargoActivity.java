package com.ruiyu.taozhuma.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.api.ApiClient;
import com.ruiyu.taozhuma.api.ApiListener;
import com.ruiyu.taozhuma.api.LogisticsApi;
import com.ruiyu.taozhuma.api.TzmDeliveryApi;
import com.ruiyu.taozhuma.api.TzmOrderDetailApi;
import com.ruiyu.taozhuma.base.BaseApplication;
import com.ruiyu.taozhuma.model.BaseModel;
import com.ruiyu.taozhuma.model.TzmOrderDetailModel;
import com.ruiyu.taozhuma.model.UserModel;
import com.ruiyu.taozhuma.model.logisticsModel;
import com.ruiyu.taozhuma.utils.LogUtil;
import com.ruiyu.taozhuma.utils.ProgressDialogUtil;
import com.ruiyu.taozhuma.utils.StringUtils;
import com.ruiyu.taozhuma.utils.ToastUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class DispatchCargoActivity extends Activity{
	private TextView txt_head_title;
	private Button btn_head_left;
	private TextView tv_address_name,tv_address_tel,tv_address_address;
	private Button btn_submit;
	
	private TextView tv_logisticsName;
	private Spinner sp_logisticsName;
	private EditText et_logisticsNum;
	
	private String [] logisticsNames; 
	private ArrayAdapter<String> adapter;
	
	private TzmOrderDetailApi tzmOrderDetailApi;
	private ApiClient client,client2,client3;
	private TzmOrderDetailModel tzmOrderDetailModel;
	private int oid;
	
	private TzmDeliveryApi deliveryApi;
	
	private LogisticsApi logisticsApi;
	private List<logisticsModel> logisticsModels;
	private UserModel userInfo;
	
	private int position;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dispatch_cargo_activity);
		userInfo = BaseApplication.getInstance().getLoginUser();
		oid = getIntent().getIntExtra("oid", 0);
		initView();
		tzmOrderDetailModel = new TzmOrderDetailModel();
		logisticsModels=new ArrayList<logisticsModel>();
		client = new ApiClient(this);
		client2 = new ApiClient(this);
		client3 = new ApiClient(this);
		loadData();
		
	}
	private void loadData() {
		tzmOrderDetailApi = new TzmOrderDetailApi();
		tzmOrderDetailApi.setOid(oid);
		client.api(tzmOrderDetailApi, new ApiListener() {

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
				ToastUtils.showShortToast(DispatchCargoActivity.this, error);
			}

			@Override
			public void onComplete(String jsonStr) {
				try {
					Gson gson = new Gson();
					Type type = new TypeToken<BaseModel<TzmOrderDetailModel>>() {
					}.getType();
					BaseModel<TzmOrderDetailModel> base = gson.fromJson(
							jsonStr, type);
					if (base.success) {
						tzmOrderDetailModel = base.result;
						tv_address_name.setText(tzmOrderDetailModel.name);
						tv_address_tel.setText(tzmOrderDetailModel.tel);
						tv_address_address.setText(tzmOrderDetailModel.address);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}, true);
		
		client2=new ApiClient(this);
		logisticsApi=new LogisticsApi();
		client2.api(logisticsApi, new ApiListener() {
			
			@Override
			public void onStart() {
				ProgressDialogUtil.openProgressDialog(
						DispatchCargoActivity.this, "", "正在提交...");
			}
			@Override
			public void onError(String error) {
				ProgressDialogUtil.closeProgressDialog();
				LogUtil.Log(error);
				ToastUtils.showShortToast(DispatchCargoActivity.this, error);
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
					Gson gson = new Gson();
					Type type = new TypeToken<BaseModel<ArrayList<logisticsModel>>>() {
					}.getType();
					BaseModel<ArrayList<logisticsModel>> base = gson.fromJson(
							jsonStr, type);
					if (base.result != null && base.result.size() > 0) {
						logisticsModels.addAll(base.result);
						logisticsNames=new String [logisticsModels.size()];
						for(int i=0;i<logisticsModels.size();i++){
							logisticsNames[i]=logisticsModels.get(i).name;
						}
						adapter = new ArrayAdapter<String>(DispatchCargoActivity.this,
								android.R.layout.simple_spinner_item, logisticsNames);
						adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						sp_logisticsName.setAdapter(adapter);
						sp_logisticsName
								.setOnItemSelectedListener(new SpinnerSelectedListener());
					} else {
						ToastUtils.showShortToast(DispatchCargoActivity.this,
								base.error_msg);
					}
				}
			}
		}, true);
		
	}
	private void initView() {
		txt_head_title = (TextView) findViewById(R.id.txt_head_title);
		txt_head_title.setText("发货");
		btn_head_left = (Button) findViewById(R.id.btn_head_left);
		btn_head_left.setOnClickListener(clickListener);
		tv_address_name=(TextView) findViewById(R.id.tv_address_name);
		tv_address_tel=(TextView) findViewById(R.id.tv_address_tel);
		tv_address_address=(TextView) findViewById(R.id.tv_address_address);
		tv_logisticsName=(TextView) findViewById(R.id.tv_logisticsName);
		sp_logisticsName=(Spinner) findViewById(R.id.sp_logisticsName);
		tv_logisticsName.setOnClickListener(clickListener);
		et_logisticsNum=(EditText) findViewById(R.id.et_logisticsNum);
		btn_submit=(Button) findViewById(R.id.btn_submit);
		btn_submit.setOnClickListener(clickListener);
	}
	
	OnClickListener clickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_head_left:
				onBackPressed();
				break;
			case R.id.tv_logisticsName:
				sp_logisticsName.performClick();//触发Spinner
				sp_logisticsName.setVisibility(View.VISIBLE);
				tv_logisticsName.setVisibility(View.GONE);
				break;
			case R.id.btn_submit:
				submitData();
				break;
			}
		}
	};
	
	class SpinnerSelectedListener implements OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			position=arg2;
		}

		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}

	protected void submitData() {
		deliveryApi=new TzmDeliveryApi();
		if(tv_logisticsName.getVisibility()!=View.GONE){
			ToastUtils.showShortToast(DispatchCargoActivity.this, "请选择物流公司");
			return;
		}
		if(StringUtils.isEmpty(et_logisticsNum.getText().toString())){
			ToastUtils.showShortToast(DispatchCargoActivity.this, "请输入物流单号");
			return;
		}
		deliveryApi.setUid(userInfo.uid);
		deliveryApi.setOid(oid);
		deliveryApi.setLogisticsName(logisticsNames[position]);
		deliveryApi.setLogisticsNum(et_logisticsNum.getText().toString());
		client3.api(deliveryApi, new ApiListener() {
			
			@Override
			public void onStart() {
				ProgressDialogUtil.openProgressDialog(DispatchCargoActivity.this, "", "");
			}

			@Override
			public void onException(Exception e) {
				LogUtil.ErrorLog(e);
				ProgressDialogUtil.closeProgressDialog();
			}

			@Override
			public void onError(String error) {
				ProgressDialogUtil.closeProgressDialog();
				LogUtil.Log(error);
				ToastUtils.showShortToast(DispatchCargoActivity.this,error);
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
						if (success) {
							ToastUtils.showShortToast(DispatchCargoActivity.this, "提交成功");
							onBackPressed();
						}else{
							ToastUtils.showShortToast(DispatchCargoActivity.this, error_msg);
						}

					} catch (JSONException e) {
						e.printStackTrace();
					}

				}
			}
		}, true);
	}
	
}
