package com.ruiyu.taozhuma.activity;

import java.lang.reflect.Type;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.api.ApiClient;
import com.ruiyu.taozhuma.api.ApiListener;
import com.ruiyu.taozhuma.api.TzmOrderDetailApi;
import com.ruiyu.taozhuma.base.BaseApplication;
import com.ruiyu.taozhuma.base.xUtilsImageLoader;
import com.ruiyu.taozhuma.model.BaseModel;
import com.ruiyu.taozhuma.model.TzmOrderDetailModel;
import com.ruiyu.taozhuma.model.UserModel;
import com.ruiyu.taozhuma.utils.LogUtil;
import com.ruiyu.taozhuma.utils.StringUtils;
import com.ruiyu.taozhuma.utils.ToastUtils;

/**
 * 
 * 订单详情
 * 
 * @author yangfen
 */
public class OrdersDetailActivity extends Activity {
	private String TAG = "OrdersDetailActivity";
	@ViewInject(R.id.btn_head_left)
	private Button btn_head_left;
	@ViewInject(R.id.txt_head_title)
	private TextView txt_head_title;

	private TextView tv_address_name, tv_address_tel, tv_address_address;
	private TextView tv_order_number, tv_order_deliverType, tv_order_addtime,
			tv_order_orderPrice;
	private ImageView iv_productImage;
	private TextView tv_shopName, tv_productName, tv_price, tv_num;
	private Button btn_applyRefund;
	private LinearLayout ll_order_detail, ll_address_info;
	private View convertView[] = null;
	private LayoutInflater layoutInflater;

	private TzmOrderDetailApi tzmOrderDetailApi;
	private ApiClient client;
	private TzmOrderDetailModel tzmOrderDetailModel;

	private UserModel userInfo;
	private int oid, status;// status==2待发货 status==3 待收货 status==4待评价

	private int j = 0;
	private int requestCode;
	@ViewInject(R.id.tv_order_couponNo)
	private TextView tv_order_couponNo;// 代金券号
	int reStatus, orderdetailID;
	private TextView tv_skuvalue;// sku信息
	private RelativeLayout rl_all_item;
	xUtilsImageLoader imageLoader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.orders_detail_activity);
		Log.i(TAG, TAG + ">>>>>>>>onCreate");
		ViewUtils.inject(this);
		imageLoader = new xUtilsImageLoader(this);
		btn_head_left.setOnClickListener(onClick);
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
		layoutInflater = LayoutInflater.from(OrdersDetailActivity.this);

		userInfo = BaseApplication.getInstance().getLoginUser();
		oid = getIntent().getIntExtra("oid", 0);
		System.out.println(oid);
		// status=1 待付款 status==2待发货 status==3 待收货 status==4待评价
		status = getIntent().getIntExtra("oStatus", 0);
		System.out.println(status + "??????????????????");
		tzmOrderDetailModel = new TzmOrderDetailModel();
		client = new ApiClient(this);
		loadData();

	}

	@SuppressLint("InflateParams")
	private void initView() {
		if (tzmOrderDetailModel != null) {
			tv_order_couponNo.setText(tzmOrderDetailModel.couponNo);
			tv_address_name.setText(tzmOrderDetailModel.name);
			tv_address_tel.setText(tzmOrderDetailModel.tel + "");
			tv_address_address.setText(tzmOrderDetailModel.address);
			tv_order_number.setText(tzmOrderDetailModel.orderNumber);
			tv_order_deliverType.setText(tzmOrderDetailModel.deliverType);
			tv_order_orderPrice.setText(tzmOrderDetailModel.orderPrice + "（含运费"
					+ tzmOrderDetailModel.espressPrice + "）");
			tv_order_addtime.setText(tzmOrderDetailModel.addtime);
			tv_shopName.setText(tzmOrderDetailModel.shopName);
			if (tzmOrderDetailModel.deliverType.equals("自提")) {
				ll_address_info.setVisibility(View.GONE);
			}
			if (tzmOrderDetailModel.product.size() != 0) {
				for (int index = 0; index < tzmOrderDetailModel.product.size(); index++) {
					convertView = new View[tzmOrderDetailModel.product.size()];
					convertView[index] = (View) layoutInflater.inflate(
							R.layout.tzm_order_detail, null);
					j = index;
					convertView[index].setId(j);

					tv_skuvalue = (TextView) convertView[index]
							.findViewById(R.id.tv_skuvalue);
					tv_productName = (TextView) convertView[index]
							.findViewById(R.id.tv_productName);
					iv_productImage = (ImageView) convertView[index]
							.findViewById(R.id.iv_productImage);
					tv_price = (TextView) convertView[index]
							.findViewById(R.id.tv_price);
					tv_num = (TextView) convertView[index]
							.findViewById(R.id.tv_num);
					rl_all_item = (RelativeLayout) convertView[index]
							.findViewById(R.id.rl_all_item);

					rl_all_item.setOnClickListener(onClick);
					// status==2待发货
					// reStatus退货类型(0==>未申请,1=>审核，2=>请退货，3=》退货中，4=》退货成功，5=》退货失败，6=》审核不成功
					// 7 退款成功)
					btn_applyRefund = (Button) convertView[index]
							.findViewById(R.id.btn_applyRefund);
					reStatus = Integer.parseInt(tzmOrderDetailModel.product
							.get(index).reStatus);
					orderdetailID = tzmOrderDetailModel.product.get(index).orderDetailId;
					rl_all_item.setTag(tzmOrderDetailModel.activityId+":"+tzmOrderDetailModel.product.get(index).productId);
					if (status == 2) {

						if (userInfo.type == 6) {
							btn_applyRefund.setVisibility(View.GONE);
						} else {
							btn_applyRefund.setVisibility(View.VISIBLE);
						}

						Log.i(TAG, "状态 :" + status + "orderdetailID"
								+ orderdetailID + "商品退货类型  :" + reStatus);
						if (reStatus == 0) {
							btn_applyRefund.setText("退款");

						}
						if (reStatus == 1) {
							btn_applyRefund.setText("审核中");

						}
						if (reStatus == 2) {
							btn_applyRefund.setText("请退货");

						}
						if (reStatus == 3) {
							btn_applyRefund.setText("退货中");

						}
						if (reStatus == 4) {
							btn_applyRefund.setText("退货成功");

						}
						if (reStatus == 5) {
							btn_applyRefund.setText("退货失败");

						}
						if (reStatus == 6) {
							btn_applyRefund.setText("审核不成功");

						}
						if (reStatus == 7) {
							btn_applyRefund.setText("退款成功");

						}
						btn_applyRefund.setOnClickListener(onClick);
						btn_applyRefund.setTag(index + ":" + orderdetailID
								+ ":" + reStatus);
					}
					// status==3 待收货
					// reStatus退货类型(0==>未申请,1=>审核，2=>请退货，3=》退货中，4=》退货成功，5=》退货失败，6=》审核不成功
					// 7 退款成功)
					if (status == 3) {

						if (userInfo.type == 6) {
							btn_applyRefund.setVisibility(View.GONE);
						} else {
							btn_applyRefund.setVisibility(View.VISIBLE);
						}

						Log.i(TAG, "状态 :" + status);
						Log.i(TAG, "状态 :" + "商品退货类型  :" + reStatus);
						if (reStatus == 0) {
							btn_applyRefund.setText("退货/退款");

						}

						if (reStatus == 1) {
							btn_applyRefund.setText("审核中");

						}

						if (reStatus == 2) {
							btn_applyRefund.setText("请退货");

						}
						if (reStatus == 3) {
							btn_applyRefund.setText("退货中");

						}
						if (reStatus == 4) {
							btn_applyRefund.setText("退货成功");

						}
						if (reStatus == 5) {
							btn_applyRefund.setText("退货失败");

						}
						if (reStatus == 6) {
							btn_applyRefund.setText("审核不成功");

						}
						if (reStatus == 7) {
							btn_applyRefund.setText("退款成功");

						}
						btn_applyRefund.setOnClickListener(onClick);
						btn_applyRefund.setTag(index + ":" + orderdetailID
								+ ":" + reStatus);
					}
					// status==4待评价
					// reStatus退货类型(0==>未申请,1=>审核，2=>请退货，3=》退货中，4=》退货成功，5=》退货失败，6=》审核不成功)
					if (status == 4) {

						btn_applyRefund.setVisibility(View.VISIBLE);

						Log.i(TAG, "状态 :" + status);
						Log.i(TAG, "状态 :" + "商品退货类型  :" + reStatus);
						if (reStatus == 0) {
							btn_applyRefund.setText("申请售后");

						}
						if (reStatus == 1) {
							btn_applyRefund.setText("审核中");

						}
						if (reStatus == 2) {
							btn_applyRefund.setText("填写物流");

						}
						if (reStatus == 3) {
							btn_applyRefund.setText("退货中");

						}
						if (reStatus == 4) {
							btn_applyRefund.setText("退货成功");

						}
						if (reStatus == 5) {
							btn_applyRefund.setText("退货失败");

						}
						if (reStatus == 6) {
							btn_applyRefund.setText("审核不成功");

						}
						if (reStatus == 7) {
							btn_applyRefund.setText("退款成功");

						}
						btn_applyRefund.setOnClickListener(onClick);
						btn_applyRefund.setTag(index + ":" + orderdetailID
								+ ":" + reStatus);
					}

					if (!StringUtils.isEmpty(tzmOrderDetailModel.product
							.get(index).productImage)) {
						imageLoader.display(iv_productImage, tzmOrderDetailModel.product.get(index).productImage);
					}
					tv_productName.setText(tzmOrderDetailModel.product
							.get(index).productName);
					tv_price.setText("¥ "
							+ tzmOrderDetailModel.product.get(index).price);
					tv_num.setText("x"
							+ tzmOrderDetailModel.product.get(index).productNumber);
					tv_skuvalue
							.setText(tzmOrderDetailModel.product.get(index).skuValue);

					ll_order_detail.addView(convertView[index]);
				}
			}

		}

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
				ToastUtils.showShortToast(OrdersDetailActivity.this, error);
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
			case R.id.rl_all_item:
				// 跳到商品详情页面
				String str1 = v.getTag().toString();
				String aa[] =str1.split(":");
				Intent intentaa = new Intent(OrdersDetailActivity.this,
						ProductDetailActivity.class);
				intentaa.putExtra("id", Integer.parseInt(aa[1]));
				intentaa.putExtra("activityId",
						Integer.parseInt(aa[0]));
				startActivity(intentaa);
				break;
			case R.id.btn_applyRefund:

				Log.i(TAG, "按钮的标志" + v.getTag());
				String flag = (String) v.getTag();
				String a[] = flag.split(":");
				Log.i(TAG, "a[0]:" + a[0] + "a[1]:" + a[1] + "a[2]:" + a[2]);

				Integer a0 = Integer.parseInt(a[0].toString());
				Integer a1 = Integer.parseInt(a[1].toString());
				Integer a2 = Integer.parseInt(a[2].toString());

				String str = ((Button) v).getText().toString();

				switch (str) {
				case "退货/退款":
					Intent applyRefund3 = new Intent(OrdersDetailActivity.this,
							TzmApplyRefundActivity.class);
					Bundle mBundle1 = new Bundle();
					mBundle1.putSerializable("model", tzmOrderDetailModel);
					applyRefund3.putExtra("index", a0.toString());
					applyRefund3.putExtra("status", status);
					Log.i(TAG, "status" + status);
					applyRefund3.putExtras(mBundle1);
					requestCode = 0;
					startActivityForResult(applyRefund3, requestCode);

					break;
				case "退款":
					Intent applyRefund4 = new Intent(OrdersDetailActivity.this,
							TzmApplyRefundActivity.class);
					Bundle mBundle2 = new Bundle();
					mBundle2.putSerializable("model", tzmOrderDetailModel);
					applyRefund4.putExtra("index", a0.toString());
					applyRefund4.putExtra("status", status);
					Log.i(TAG, "status" + status);
					applyRefund4.putExtras(mBundle2);
					requestCode = 0;
					startActivityForResult(applyRefund4, requestCode);

					break;
				case "审核中":

					Intent intent = new Intent(OrdersDetailActivity.this,
							TzmOrderCheckingActivity.class);// 审核中
					intent.putExtra("oid", a1);
					intent.putExtra("reStatus", a2);
					startActivity(intent);
					break;
				case "请退货":

					Intent applyRefund = new Intent(OrdersDetailActivity.this,
							TzmLogisticsActivity.class);

					applyRefund.putExtra("model",
							tzmOrderDetailModel.product.get(a0));
					requestCode = 0;
					startActivityForResult(applyRefund, requestCode);
					break;
				case "退货中":

					Intent intent33 = new Intent(OrdersDetailActivity.this,
							TzmReturnExpressLogisticsActivity.class);
					intent33.putExtra("reStatus", a2);
					intent33.putExtra("orderId", a1);
					startActivity(intent33);
					break;
				case "退货成功":

					Intent intent2 = new Intent(OrdersDetailActivity.this,
							TzmOrderCheckActivity.class);
					intent2.putExtra("reStatus", a2);
					intent2.putExtra("oid", a1);
					startActivity(intent2);
					break;
				case "退货失败":

					/*
					 * Intent intent3 = new Intent(OrdersDetailActivity.this,
					 * TzmOrderCheckActivity.class);// 审核中
					 * intent3.putExtra("reStatus", reStatus);
					 * intent3.putExtra("oid", orderdetailID);
					 * startActivity(intent3);
					 */
					break;
				case "审核不成功":

					Intent intent4 = new Intent(OrdersDetailActivity.this,
							TzmOrderCheckActivity.class);
					intent4.putExtra("reStatus", a2);
					intent4.putExtra("oid", a1);
					startActivity(intent4);
					break;
				case "退款成功":

					Intent intent5 = new Intent(OrdersDetailActivity.this,
							TzmOrderCheckActivity.class);
					intent5.putExtra("reStatus", a2);
					intent5.putExtra("oid", a1);
					startActivity(intent5);

					break;
				case "申请售后":

					Intent applyRefund2 = new Intent(OrdersDetailActivity.this,
							TzmApplyRefundActivity.class);
					Bundle mBundle = new Bundle();
					mBundle.putSerializable("model", tzmOrderDetailModel);
					applyRefund2.putExtra("index", a0.toString());
					applyRefund2.putExtra("status", status);
					Log.i(TAG, "status" + status);
					applyRefund2.putExtras(mBundle);
					requestCode = 0;
					startActivityForResult(applyRefund2, requestCode);

					break;
				case "填写物流":

					Intent intent6 = new Intent(OrdersDetailActivity.this,
							TzmReturnLogisticsActivity.class);
					// intent6.putExtra("reStatus", a2);
					intent6.putExtra("oid", a1);
					startActivity(intent6);
					break;
				case "售后审核中":

					Intent intent7 = new Intent(OrdersDetailActivity.this,
							TzmOrderCheckingActivity.class);// 审核中
					intent7.putExtra("oid", a1);
					intent7.putExtra("reStatus", a2);
					startActivity(intent7);
					break;

				default:
					break;
				}
				break;
			}
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 0:
			ll_order_detail.removeAllViews();
			loadData();
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
