package com.ruiyu.taozhuma.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.api.ApiClient;
import com.ruiyu.taozhuma.api.ApiListener;
import com.ruiyu.taozhuma.api.LogisticsApi;
import com.ruiyu.taozhuma.api.SubmitLogisticsApi;
import com.ruiyu.taozhuma.base.BaseApplication;
import com.ruiyu.taozhuma.base.xUtilsImageLoader;
import com.ruiyu.taozhuma.model.BaseModel;
import com.ruiyu.taozhuma.model.OrderDetailModel;
import com.ruiyu.taozhuma.model.TzmMyRefundModel;
import com.ruiyu.taozhuma.model.TzmMyRefundModel.Carts;
import com.ruiyu.taozhuma.model.TzmOrderDetailModel;
import com.ruiyu.taozhuma.model.UserModel;
import com.ruiyu.taozhuma.model.logisticsModel;
import com.ruiyu.taozhuma.utils.LogUtil;
import com.ruiyu.taozhuma.utils.ProgressDialogUtil;
import com.ruiyu.taozhuma.utils.StringUtils;
import com.ruiyu.taozhuma.utils.ToastUtils;

/**
 * 
 * 退货填写物流
 * 
 * @author fu
 * 
 */
public class TzmLogisticsActivity extends Activity {
	private Button btn_head_left;
	private TextView txt_head_title;
	private LinearLayout ll_logistics_detail;
	private TextView tv_logisticsName;
	private Spinner sp_logisticsName;
	private EditText et_num;
	private Button logistics_submit;

	private String[] logisticsNames;
	private ArrayAdapter<String> adapter;

	private TzmOrderDetailModel.Products model;
	// private Carts carts;
	// private List<product> products;
	private UserModel userInfo;
	private LayoutInflater layoutInflater;

	private LogisticsApi logisticsApi;
	private SubmitLogisticsApi submitLogisticsApi;
	private ApiClient apiClient1, apiClient2;
	private List<logisticsModel> logisticsModels;


	private int position;

	xUtilsImageLoader imageLoader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tzm_logistics_activity);
		imageLoader = new xUtilsImageLoader(this);
		model = (TzmOrderDetailModel.Products) getIntent()
				.getSerializableExtra("model");
		userInfo = BaseApplication.getInstance().getLoginUser();
		// products= model.product;
		initView();
		loadData();
	}

	private void loadData() {
		apiClient1 = new ApiClient(this);
		logisticsApi = new LogisticsApi();
		apiClient1.api(logisticsApi, new ApiListener() {

			@Override
			public void onStart() {
				ProgressDialogUtil.openProgressDialog(
						TzmLogisticsActivity.this, "", "正在提交...");
			}

			@Override
			public void onError(String error) {
				ProgressDialogUtil.closeProgressDialog();
				LogUtil.Log(error);
				ToastUtils.showShortToast(TzmLogisticsActivity.this, error);
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
						logisticsNames = new String[logisticsModels.size()];
						for (int i = 0; i < logisticsModels.size(); i++) {
							logisticsNames[i] = logisticsModels.get(i).name;
						}
						tv_logisticsName = (TextView) findViewById(R.id.tv_logisticsName);
						sp_logisticsName = (Spinner) findViewById(R.id.sp_logisticsName);
						tv_logisticsName.setOnClickListener(clickListener);
						adapter = new ArrayAdapter<String>(
								TzmLogisticsActivity.this,
								android.R.layout.simple_spinner_item,
								logisticsNames);
						adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						sp_logisticsName.setAdapter(adapter);
						sp_logisticsName
								.setOnItemSelectedListener(new SpinnerSelectedListener());
					} else {
						ToastUtils.showShortToast(TzmLogisticsActivity.this,
								base.error_msg);
					}
				}
			}
		}, true);

	}

	private void initView() {
		btn_head_left = (Button) findViewById(R.id.btn_head_left);
		btn_head_left.setOnClickListener(clickListener);
		txt_head_title = (TextView) findViewById(R.id.txt_head_title);
		txt_head_title.setText("提交物流");
		ll_logistics_detail = (LinearLayout) findViewById(R.id.ll_logistics_detail);
		layoutInflater = LayoutInflater.from(TzmLogisticsActivity.this);
		et_num = (EditText) findViewById(R.id.et_num);
		logistics_submit = (Button) findViewById(R.id.logistics_submit);
		logistics_submit.setOnClickListener(clickListener);
		tv_logisticsName = (TextView) findViewById(R.id.tv_logisticsName);
		sp_logisticsName = (Spinner) findViewById(R.id.sp_logisticsName);
		logisticsModels = new ArrayList<logisticsModel>();
		ordersDetail();
	}

	private void ordersDetail() {
		View convertView = (View) layoutInflater.inflate(
				R.layout.tzm_refund_detail_item, null);
		TextView tv_productName = (TextView) convertView
				.findViewById(R.id.tv_productName);
		ImageView iv_productImage = (ImageView) convertView
				.findViewById(R.id.iv_productImage);
		TextView tv_num = (TextView) convertView.findViewById(R.id.tv_num);
		// TextView tv_refund = (TextView) convertView
		// .findViewById(R.id.tv_refund);
		// tv_refund.setVisibility(View.GONE);
//		if (!StringUtils.isEmpty(model.productImage)) {
			imageLoader.display(iv_productImage, model.productImage);
			// BaseApplication
			// .getInstance()
			// .getImageWorker()
			// .loadBitmap(
			// model.productImage,
			// iv_productImage);
//		}
		tv_productName.setText(model.productName);
		tv_num.setText(model.productNumber + "件");
		ll_logistics_detail.addView(convertView);

	}

	View.OnClickListener clickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_head_left:
				onBackPressed();
				break;
			case R.id.tv_logisticsName:
				sp_logisticsName.performClick();
				sp_logisticsName.setVisibility(View.VISIBLE);
				tv_logisticsName.setVisibility(View.GONE);
				break;
			case R.id.logistics_submit:
				submitData();
				break;
			}
		}
	};

	protected void submitData() {
		apiClient2 = new ApiClient(this);
		submitLogisticsApi = new SubmitLogisticsApi();
		if (logisticsModels.size() == 0 || logisticsModels == null) {
			loadData();
			ToastUtils.showShortToast(TzmLogisticsActivity.this, "请选择物流公司");
			return;
		}
		if (tv_logisticsName.getVisibility() == View.VISIBLE) {
			ToastUtils.showShortToast(TzmLogisticsActivity.this, "请选择物流公司");
			return;
		}
		if (StringUtils.isEmpty(et_num.getText().toString())) {
			ToastUtils.showShortToast(TzmLogisticsActivity.this, "请输入物流的单号");
			return;
		}
		submitLogisticsApi.setUid(userInfo.uid);
		submitLogisticsApi.setOid(model.orderDetailId);
		submitLogisticsApi.setLogisticsNum(et_num.getText().toString());
		submitLogisticsApi
				.setLogisticsName(logisticsModels.get(position).nameEn);
		apiClient2.api(submitLogisticsApi, new ApiListener() {
			@Override
			public void onStart() {
				ProgressDialogUtil.openProgressDialog(
						TzmLogisticsActivity.this, "", "正在提交...");
			}

			@Override
			public void onError(String error) {
				ProgressDialogUtil.closeProgressDialog();
				ToastUtils.showShortToast(TzmLogisticsActivity.this, error);
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
						ToastUtils.showShortToast(TzmLogisticsActivity.this,
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

	class SpinnerSelectedListener implements OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			position = arg2;
		}

		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}
}
