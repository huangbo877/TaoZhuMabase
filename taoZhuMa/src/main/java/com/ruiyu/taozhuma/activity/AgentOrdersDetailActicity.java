package com.ruiyu.taozhuma.activity;

import java.lang.reflect.Type;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.api.AgentOrderDetailApi;
import com.ruiyu.taozhuma.api.ApiClient;
import com.ruiyu.taozhuma.api.ApiListener;
import com.ruiyu.taozhuma.api.TzmsetAgencyPushApi;
import com.ruiyu.taozhuma.base.BaseApplication;
import com.ruiyu.taozhuma.base.xUtilsImageLoader;
import com.ruiyu.taozhuma.model.AgentOrderDetailModel;
import com.ruiyu.taozhuma.model.BaseModel;
import com.ruiyu.taozhuma.model.UserModel;
import com.ruiyu.taozhuma.utils.LogUtil;
import com.ruiyu.taozhuma.utils.StringUtils;
import com.ruiyu.taozhuma.utils.ToastUtils;

public class AgentOrdersDetailActicity extends Activity {
	private Button btn_head_left;
	private TextView txt_head_title;
	private TextView tv_address_name, tv_address_tel, tv_address_address;
	private TextView tv_order_number, tv_order_deliverType, tv_order_addtime,
			tv_order_orderPrice;
	private ImageView iv_productImage;
	private TextView tv_shopName, tv_productName, tv_price, tv_num;
	private Button btn_applyRefund;
	private Button btn_status1, btn_status2, btn_status3;
	private LinearLayout ll_order_detail, ll_address_info;
	private View convertView[] = null;
	private LayoutInflater layoutInflater;

	private AgentOrderDetailApi agentOrderDetailApi;
	private ApiClient client, apiClient;
	private AgentOrderDetailModel agentOrderDetailModel;

	private UserModel userInfo;
	private int oid;// status==2待发货 status==3 待收货 status==4待评价

	private int j = 0;
	// private int requestCode;

	private xUtilsImageLoader imageLoader;

	private TzmsetAgencyPushApi api;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.orders_detail_activity);

		btn_head_left = (Button) findViewById(R.id.btn_head_left);
		btn_head_left.setOnClickListener(onClick);
		txt_head_title = (TextView) findViewById(R.id.txt_head_title);
		txt_head_title.setText("订单详情");
		tv_address_name = (TextView) findViewById(R.id.tv_address_name);
		tv_address_tel = (TextView) findViewById(R.id.tv_address_tel);
		tv_address_address = (TextView) findViewById(R.id.tv_address_address);
		tv_order_number = (TextView) findViewById(R.id.tv_order_number);
		tv_order_deliverType = (TextView) findViewById(R.id.tv_order_deliverType);
		tv_order_orderPrice = (TextView) findViewById(R.id.tv_order_orderPrice);
		tv_order_addtime = (TextView) findViewById(R.id.tv_order_addtime);
		ll_order_detail = (LinearLayout) findViewById(R.id.ll_order_detail);
		ll_address_info = (LinearLayout) findViewById(R.id.ll_address_info);
		tv_shopName = (TextView) findViewById(R.id.tv_shopName);
		// tv_order_espressPrice = (TextView)
		// findViewById(R.id.tv_order_espressPrice);
		// iv_logo = (ImageView) findViewById(R.id.iv_logo);
		layoutInflater = LayoutInflater.from(AgentOrdersDetailActicity.this);
		btn_status1 = (Button) findViewById(R.id.btn_status1);
		btn_status2 = (Button) findViewById(R.id.btn_status2);
		btn_status3 = (Button) findViewById(R.id.btn_status3);
		userInfo = BaseApplication.getInstance().getLoginUser();
		oid = getIntent().getIntExtra("oid", 0);
		// status = getIntent().getIntExtra("oStatus", 0);

		agentOrderDetailModel = new AgentOrderDetailModel();
		client = new ApiClient(this);
		imageLoader = new xUtilsImageLoader(this);

		loadData();

	}

	private void initView() {
		if (agentOrderDetailModel != null) {
			tv_address_name.setText(agentOrderDetailModel.name);
			tv_address_tel.setText(agentOrderDetailModel.tel + "");
			tv_address_address.setText(agentOrderDetailModel.address);
			tv_order_number.setText(agentOrderDetailModel.orderNumber);
			tv_order_deliverType.setText(agentOrderDetailModel.deliverType);
			tv_order_orderPrice.setText(agentOrderDetailModel.orderPrice + "");
			tv_order_addtime.setText(agentOrderDetailModel.addtime);
			tv_shopName.setText(agentOrderDetailModel.shopName);
			// tv_order_espressPrice.setText(agentOrderDetailModel.espressPrice
			// + "");

			if (agentOrderDetailModel.pushStatus.equals("0")) {
				btn_status1.setVisibility(View.VISIBLE);
				btn_status2.setVisibility(View.GONE);
				btn_status3.setVisibility(View.GONE);
			} else if (agentOrderDetailModel.pushStatus.equals("1")) {
				btn_status1.setVisibility(View.GONE);
				btn_status2.setVisibility(View.VISIBLE);
				btn_status3.setVisibility(View.GONE);
			} else if (agentOrderDetailModel.pushStatus.equals("2")) {
				btn_status1.setVisibility(View.GONE);
				btn_status2.setVisibility(View.GONE);
				btn_status3.setVisibility(View.VISIBLE);
			}
			// if (StringUtils.isNotEmpty(agentOrderDetailModel.shopLogo)) {
			// imageLoader.display(iv_logo, agentOrderDetailModel.shopLogo);
			// }
			if (agentOrderDetailModel.deliverType.equals("自提")) {
				ll_address_info.setVisibility(View.GONE);
			}
			if (agentOrderDetailModel.product.size() != 0) {
				for (int index = 0; index < agentOrderDetailModel.product
						.size(); index++) {
					convertView = new View[agentOrderDetailModel.product.size()];
					convertView[index] = (View) layoutInflater.inflate(
							R.layout.tzm_order_detail, null);
					j = index;
					convertView[index].setId(j);
					tv_productName = (TextView) convertView[index]
							.findViewById(R.id.tv_productName);
					iv_productImage = (ImageView) convertView[index]
							.findViewById(R.id.iv_productImage);
					tv_price = (TextView) convertView[index]
							.findViewById(R.id.tv_price);
					tv_num = (TextView) convertView[index]
							.findViewById(R.id.tv_num);

					btn_applyRefund = (Button) convertView[index]
							.findViewById(R.id.btn_applyRefund);
					btn_applyRefund.setVisibility(View.GONE);
					// if (!StringUtils.isEmpty(agentOrderDetailModel.product
					// .get(index).productImage)) {
					// BaseApplication
					// .getInstance()
					// .getImageWorker()
					// .loadBitmap(
					// agentOrderDetailModel.product
					// .get(index).productImage,
					// iv_productImage);
					// }
					if (StringUtils.isNotEmpty(agentOrderDetailModel.product
							.get(index).productImage)) {
						imageLoader
								.display(iv_productImage,
										agentOrderDetailModel.product
												.get(index).productImage);
					}
					tv_productName.setText(agentOrderDetailModel.product
							.get(index).productName);
					tv_price.setText("¥ "
							+ agentOrderDetailModel.product.get(index).price);
					tv_num.setText(agentOrderDetailModel.product.get(index).productNumber
							+ "件");
					ll_order_detail.addView(convertView[index]);
					btn_status1.setOnClickListener(onClick);
				}
			}

		}

	}

	private void loadData() {

		agentOrderDetailApi = new AgentOrderDetailApi();
		agentOrderDetailApi.setOid(oid);
		agentOrderDetailApi.setAgencyId(userInfo.agencyId);
		client.api(agentOrderDetailApi, new ApiListener() {

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
				ToastUtils.showShortToast(AgentOrdersDetailActicity.this, error);
			}

			@Override
			public void onComplete(String jsonStr) {
				try {
					Gson gson = new Gson();
					Type type = new TypeToken<BaseModel<AgentOrderDetailModel>>() {
					}.getType();
					BaseModel<AgentOrderDetailModel> base = gson.fromJson(
							jsonStr, type);
					if (base.success) {
						agentOrderDetailModel = base.result;
						initView();// 初始化

					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}, true);
	}

	OnClickListener onClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_head_left:
				onBackPressed();
				break;
			case R.id.btn_status1:
				// final int index = (Integer) v.getTag();
				AlertDialog.Builder builder = new Builder(
						AgentOrdersDetailActicity.this);
				builder.setMessage("确认抢下该订单？");
				builder.setTitle("提示");
				builder.setPositiveButton("确认",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								getorder(agentOrderDetailModel.orderId,
										userInfo.agencyId);
							}

						});
				builder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
							}
						});
				builder.create().show();
				break;
			}
		}
	};

	private void getorder(Integer orderId, final Integer agencyId) {
		api = new TzmsetAgencyPushApi();
		apiClient = new ApiClient(AgentOrdersDetailActicity.this);
		api.setAgencyId(agencyId);
		api.setOrderId(orderId);
		apiClient.api(this.api, new ApiListener() {
			@Override
			public void onStart() {
			}

			@Override
			public void onComplete(String jsonStr) {
				if (StringUtils.isNotBlank(jsonStr)) {
					try {
						JSONObject jsonObject = new JSONObject(jsonStr);
						boolean success = jsonObject.optBoolean("success");
						Integer result = jsonObject.optInt("result");
						String error_msg = jsonObject.optString("error_msg");
						if (success) {
							// String success_msg;
							if (result == 0) {
								// success_msg = "抢单成功!";
								btn_status1.setVisibility(View.GONE);
								btn_status2.setVisibility(View.VISIBLE);
								btn_status3.setVisibility(View.GONE);
							} else if (result == 1) {
								// success_msg = "订单已被抢先确认！";
								btn_status1.setVisibility(View.GONE);
								btn_status2.setVisibility(View.GONE);
								btn_status3.setVisibility(View.VISIBLE);
							} else if (result == 2) {
								// success_msg = "你已经成功抢单过了，无需再抢";
								btn_status1.setVisibility(View.GONE);
								btn_status2.setVisibility(View.VISIBLE);
								btn_status3.setVisibility(View.GONE);
							}
							ToastUtils.showShortToast(
									AgentOrdersDetailActicity.this, error_msg);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}

			@Override
			public void onError(String error) {
				ToastUtils
						.showShortToast(AgentOrdersDetailActicity.this, error);
			}

			@Override
			public void onException(Exception e) {
				LogUtil.ErrorLog(e);
			}
		}, true);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 0:
			ll_order_detail.removeAllViews();
			loadData();

			// OrdersDetailActivity.this.invalidate();
			break;
		case 1:

			break;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
