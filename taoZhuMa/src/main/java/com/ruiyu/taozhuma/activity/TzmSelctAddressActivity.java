package com.ruiyu.taozhuma.activity;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.simcpux.Constants;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.adapter.TzmSelectAddressAdapter;
import com.ruiyu.taozhuma.api.AddPkgOrderByPurchaseApi;
import com.ruiyu.taozhuma.api.AddPkgPurchaseApi;
import com.ruiyu.taozhuma.api.ApiClient;
import com.ruiyu.taozhuma.api.ApiListener;
import com.ruiyu.taozhuma.api.DefaultAddressApi;
import com.ruiyu.taozhuma.api.TzmAddOrderByPurchaseApi;
import com.ruiyu.taozhuma.api.TzmAddPurchaseApi;
import com.ruiyu.taozhuma.api.TzmAddorderApi;
import com.ruiyu.taozhuma.api.TzmOrderProductApi;
import com.ruiyu.taozhuma.api.UserWelletDetailApi;
import com.ruiyu.taozhuma.config.AppConfig;
import com.ruiyu.taozhuma.even.WeChatEven;
import com.ruiyu.taozhuma.model.BaseModel;
import com.ruiyu.taozhuma.model.OrderAddModel;
import com.ruiyu.taozhuma.model.OrderAddModel.AliPay;
import com.ruiyu.taozhuma.model.OrderAddModel.WeChat;
import com.ruiyu.taozhuma.model.TzmOrderProduct1Model;
import com.ruiyu.taozhuma.model.UserWelletDetailModel.source;
import com.ruiyu.taozhuma.model.TzmAddressModel;
import com.ruiyu.taozhuma.model.TzmOrderProductModel;
import com.ruiyu.taozhuma.model.UserCouponListModel;
import com.ruiyu.taozhuma.model.UserModel;
import com.ruiyu.taozhuma.model.UserWelletDetailModel;
import com.ruiyu.taozhuma.utils.LogUtil;
import com.ruiyu.taozhuma.utils.PackageUtils;
import com.ruiyu.taozhuma.utils.PayResult;
import com.ruiyu.taozhuma.utils.ProgressDialogUtil;
import com.ruiyu.taozhuma.utils.SignUtils;
import com.ruiyu.taozhuma.utils.StringUtils;
import com.ruiyu.taozhuma.utils.ToastUtils;
import com.ruiyu.taozhuma.utils.UserInfoUtils;
import com.ruiyu.taozhuma.widget.ListViewForScrollView;
import com.ruiyu.taozhuma.wxapi.ALiPayResultActivity;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import de.greenrobot.event.EventBus;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 版本备份，功能：偏远地区运费
 * 
 * @author Fu
 * 
 */
public class TzmSelctAddressActivity extends Activity implements
		OnClickListener {
	@ViewInject(R.id.txt_head_title)
	private TextView txt_head_title;
	@ViewInject(R.id.btn_head_left)
	private Button btn_head_left;
	private String cids;

	// 接口调用
	private ApiClient apiClient;
	private TzmOrderProductModel list;
	private TzmOrderProductApi api;

	@ViewInject(R.id.lv_product_list)
	private ListViewForScrollView listViewForScrollView;
	private TzmSelectAddressAdapter adapter;
	private TzmAddorderApi addorderApi;

	// 用户信息
	private Boolean isLogin;
	private UserModel userModel;

	// 地址
	@ViewInject(R.id.rl_address)
	private RelativeLayout rl_address;
	@ViewInject(R.id.tv_address)
	private TextView tv_address;
	private Long addressId;

	// 支付方式
	private int payMethod = 1;// 默认支付宝 1支付宝2微信

	@ViewInject(R.id.btn_affirm)
	private Button btn_affirm;

	// 默认地址返回
	private DefaultAddressApi defaultAddressApi;
	private ApiClient apiClient2;
	@ViewInject(R.id.tv_allprice)
	private TextView tv_allprice;

	private DecimalFormat df;

	// 支付方式选择
	@ViewInject(R.id.rl_alipay)
	private RelativeLayout rl_alipay;
	@ViewInject(R.id.iv_alipay)
	private ImageView iv_alipay;
	@ViewInject(R.id.rl_wechat)
	private RelativeLayout rl_wechat;
	@ViewInject(R.id.iv_wechat)
	private ImageView iv_wechat;

	// 限时秒杀购买
	private String TAG = null;
	private Integer pid;
	private TzmAddPurchaseApi addPurchaseApi;
	private TzmAddOrderByPurchaseApi addOrderByPurchaseApi;
	// 代金卷
	@ViewInject(R.id.rl_coupon)
	private RelativeLayout rl_coupon;
	@ViewInject(R.id.tv_coupon_price)
	private TextView tv_coupon_price;

	private float allPrice;// 订单总价
	private static final int TAG_SELECT_ADDRESS = 10;// 选择地址
	private static final int TAG_SELECT_COUPON = 100;// 选址代金卷
	private String couponNo;// 优惠卷码

	private boolean isRemote = false;// 是否偏远
	@ViewInject(R.id.tv_shipment)
	private TextView tv_shipment;// 运费

	private float couponPrice = 0;// 代金卷金额

	private float expressPrice;// 偏远地区运费

	// 套餐直接购买
	private AddPkgPurchaseApi addPkgPurchaseApi;
	private Integer pkgId;// 套餐ID
	private AddPkgOrderByPurchaseApi addPkgOrderByPurchaseApi;

	// 钱包
	@ViewInject(R.id.tv_wallet_price)
	private TextView tv_wallet_price;
	@ViewInject(R.id.cb_wallet)
	private CheckBox cb_wallet;
	@ViewInject(R.id.rl_wallet)
	private RelativeLayout rl_wallet;
	private int wallet = 1;// 0-不使用钱包1-使用钱包
	private UserWelletDetailApi userWelletDetailApi;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tzm_select_address_activity);
		ViewUtils.inject(this);
		EventBus.getDefault().register(this);
		df = new DecimalFormat("#.#");
		cids = getIntent().getExtras().getString("cids");
		TAG = getIntent().getExtras().getString("TAG");
		pid = getIntent().getIntExtra("pid", 0);
		initView();
		try {
			checkLogin();
			getDefaultAddress();
			loadWallet();
			if (StringUtils.isNotEmpty(TAG) && TAG.equals("addPurchase")) {
				// 限时秒杀直接购买
				loadPurchaseData();
			} else if (StringUtils.isNotEmpty(TAG)
					&& TAG.equals("addPkgPurchase")) {
				// 套餐直接购买
				pkgId = getIntent().getIntExtra("pkgId", 0);
				loadAddPkgPurchaseData();
			}

			else {
				loadData();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void loadWallet() {
		apiClient = new ApiClient(TzmSelctAddressActivity.this);
		userWelletDetailApi = new UserWelletDetailApi();
		userWelletDetailApi.setUid(userModel.uid);
		userWelletDetailApi.setPageNo(1000);
		apiClient.api(this.userWelletDetailApi, new ApiListener() {
			@Override
			public void onStart() {
			}

			@Override
			public void onComplete(String jsonStr) {
				ProgressDialogUtil.closeProgressDialog();
				Gson gson = new Gson();
				Type type = new TypeToken<BaseModel<UserWelletDetailModel>>() {
				}.getType();
				BaseModel<UserWelletDetailModel> base = gson.fromJson(jsonStr,
						type);
				if (base.result != null) {
					tv_wallet_price.setText("¥ " + base.result.balance);
				}

			}

			@Override
			public void onError(String error) {
				LogUtil.Log(error);
			}

			@Override
			public void onException(Exception e) {
				LogUtil.ErrorLog(e);
			}
		}, true);

	}

	/**
	 * 套餐直接购买加载数据
	 */
	private void loadAddPkgPurchaseData() {
		// Log.i("loadAddPkgPurchaseData=========", "套餐直接购买");
		listViewForScrollView.setFocusable(false);
		apiClient = new ApiClient(this);
		list = new TzmOrderProductModel();
		addPkgPurchaseApi = new AddPkgPurchaseApi();
		if (!isLogin) {
			return;
		}
		addPkgPurchaseApi.setUid(userModel.uid);
		addPkgPurchaseApi.setPkgId(pkgId);
		apiClient.api(addPkgPurchaseApi, new ApiListener() {

			@Override
			public void onStart() {
				ProgressDialogUtil.openProgressDialog(
						TzmSelctAddressActivity.this, "", "");
			}

			@Override
			public void onComplete(String jsonStr) {
				ProgressDialogUtil.closeProgressDialog();
				Gson gson = new Gson();
				Type type = new TypeToken<BaseModel<TzmOrderProductModel>>() {
				}.getType();
				BaseModel<TzmOrderProductModel> base = gson.fromJson(jsonStr,
						type);
				if (base.result != null) {
					list = base.result;
					if (list != null) {
						// && base.result.products.size() > 0) {
						// tv_result.setText("共" + model.products.size() +
						// "件,¥ "
						// + model.sumPrice + "元");
						adapter = new TzmSelectAddressAdapter(
								TzmSelctAddressActivity.this, list.result);
						listViewForScrollView.setAdapter(adapter);
						// et_message.clearFocus();
						// rl_address.requestFocus();
						getAllPrice(list.result);
						if (!list.isWallte.equals("1")) {
							wallet = 0;
							rl_wallet.setVisibility(View.GONE);
						} else {
							wallet = 1;
							rl_wallet.setVisibility(View.VISIBLE);
						}
					}
				} else {
					ToastUtils.showShortToast(TzmSelctAddressActivity.this,
							base.error_msg);
				}

			}
		}, true);

	}

	/**
	 * 限时秒杀加载数据
	 */
	private void loadPurchaseData() {
		// Log.i("loadPurchaseData=========", "限时秒杀直接购买");
		listViewForScrollView.setFocusable(false);
		apiClient = new ApiClient(this);
		list = new TzmOrderProductModel();
		addPurchaseApi = new TzmAddPurchaseApi();
		if (!isLogin) {
			return;
		}
		addPurchaseApi.setNum(1);
		addPurchaseApi.setUid(userModel.uid);
		addPurchaseApi.setPid(pid);
		apiClient.api(addPurchaseApi, new ApiListener() {

			@Override
			public void onStart() {
				ProgressDialogUtil.openProgressDialog(
						TzmSelctAddressActivity.this, "", "");
			}

			@Override
			public void onComplete(String jsonStr) {
				ProgressDialogUtil.closeProgressDialog();
				Gson gson = new Gson();
				Type type = new TypeToken<BaseModel<TzmOrderProductModel>>() {
				}.getType();
				BaseModel<TzmOrderProductModel> base = gson.fromJson(jsonStr,
						type);
				if (base.result != null) {
					list = base.result;
					if (list != null) {
						// && base.result.products.size() > 0) {
						// tv_result.setText("共" + model.products.size() +
						// "件,¥ "
						// + model.sumPrice + "元");

						adapter = new TzmSelectAddressAdapter(
								TzmSelctAddressActivity.this, list.result);
						listViewForScrollView.setAdapter(adapter);
						// et_message.clearFocus();
						// rl_address.requestFocus();
						getAllPrice(list.result);
						if (!list.isWallte.equals("1")) {
							wallet = 0;
							rl_wallet.setVisibility(View.GONE);
						} else {
							wallet = 1;
							rl_wallet.setVisibility(View.VISIBLE);
						}
					}
				} else {
					ToastUtils.showShortToast(TzmSelctAddressActivity.this,
							base.error_msg);
				}

			}
		}, true);

	}

	/**
	 * 购物车提交加载数据
	 */
	private void loadData() {
		listViewForScrollView.setFocusable(false);
		apiClient = new ApiClient(this);
		list = new TzmOrderProductModel();
		api = new TzmOrderProductApi();
		if (!isLogin) {
			return;
		}
		api.setCids(cids);
		api.setUid(userModel.uid);
		apiClient.api(api, new ApiListener() {

			@Override
			public void onStart() {
				ProgressDialogUtil.openProgressDialog(
						TzmSelctAddressActivity.this, "", "");
			}

			@Override
			public void onComplete(String jsonStr) {
				ProgressDialogUtil.closeProgressDialog();
				Gson gson = new Gson();
				Type type = new TypeToken<BaseModel<TzmOrderProductModel>>() {
				}.getType();
				BaseModel<TzmOrderProductModel> base = gson.fromJson(jsonStr,
						type);
				if (base.result != null) {
					list = base.result;
					if (list != null) {
						// && base.result.products.size() > 0) {
						// tv_result.setText("共" + model.products.size() +
						// "件,¥ "
						// + model.sumPrice + "元");
						adapter = new TzmSelectAddressAdapter(
								TzmSelctAddressActivity.this, list.result);
						listViewForScrollView.setAdapter(adapter);
						// et_message.clearFocus();
						// rl_address.requestFocus();
						getAllPrice(list.result);
						// System.out.println(list.isWallte+"--------------");
						if (!list.isWallte.equals("1")) {
							wallet = 0;
							rl_wallet.setVisibility(View.GONE);
						} else {
							wallet = 1;
							rl_wallet.setVisibility(View.VISIBLE);
						}
					}
				} else {
					ToastUtils.showShortToast(TzmSelctAddressActivity.this,
							base.error_msg);
				}

			}
		}, true);

	}

	public void onEventMainThread(WeChatEven event) {
		finish();
	}

	@Override
	protected void onDestroy() {
		EventBus.getDefault().unregister(this);
		handler.removeCallbacksAndMessages(null);
		super.onDestroy();
	}

	private void initView() {

		txt_head_title.setText("订单确认");
		btn_head_left.setOnClickListener(this);
		rl_address.setOnClickListener(this);
		rl_coupon.setOnClickListener(this);
		btn_affirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (StringUtils.isNotEmpty(TAG) && TAG.equals("addPurchase")) {
					addOrderByPurchase();
				} else if (StringUtils.isNotEmpty(TAG)
						&& TAG.equals("addPkgPurchase")) {
					// 套餐直接购买提交订单
					addPkgOrderByPurchase();
				} else {
					// 购物车提交订单
					orderSubmit();
				}

				// if (buymethod == 2)
				// addOrderByPurchase();

			}
		});
		rl_alipay.setOnClickListener(this);
		rl_wechat.setOnClickListener(this);
		cb_wallet.setChecked(true);
		cb_wallet.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					wallet = 1;

				} else {
					wallet = 0;
				}
				LogUtil.Log("wallet" + wallet);

			}
		});
	}

	/**
	 * 套餐直接购买提交订单
	 */
	protected void addPkgOrderByPurchase() {
		if (addressId == null) {
			ToastUtils.showToast(this, "请选择您的收货地址！");
			return;
		}
		// buyer_message = et_message.getText().toString();
		// ToastUtils.showShortToast(this, buyer_message);
		addPkgOrderByPurchaseApi = new AddPkgOrderByPurchaseApi();
		apiClient = new ApiClient(this);
		addPkgOrderByPurchaseApi.setUid(userModel.uid);
		addPkgOrderByPurchaseApi.setPkgId(pkgId);
		addPkgOrderByPurchaseApi.setAddressId(addressId);
		addPkgOrderByPurchaseApi.setBuyer_message(getMessage());
		addPkgOrderByPurchaseApi.setConsigneeType(1);
		addPkgOrderByPurchaseApi.setPayMethod(payMethod);
		if (StringUtils.isNotEmpty(couponNo)) {
			addPkgOrderByPurchaseApi.setCouponNo(couponNo);
		}
		apiClient.api(addPkgOrderByPurchaseApi, new ApiListener() {

			@Override
			public void onStart() {
				ProgressDialogUtil.openProgressDialog(
						TzmSelctAddressActivity.this, "", "");

			}

			@Override
			public void onError(String error) {
				// TODO Auto-generated method stub
				super.onError(error);
				ToastUtils.showShortToast(TzmSelctAddressActivity.this, error);
				ProgressDialogUtil.closeProgressDialog();
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * com.ruiyu.taozhuma.api.ApiListener#onException(java.lang.Exception
			 * )
			 */
			@Override
			public void onException(Exception e) {
				// TODO Auto-generated method stub
				super.onException(e);
				ToastUtils
						.showShortToast(TzmSelctAddressActivity.this, "网络错误！");
				ProgressDialogUtil.closeProgressDialog();
			}

			@Override
			public void onComplete(String jsonStr) {
				ProgressDialogUtil.closeProgressDialog();
				if (StringUtils.isNotBlank(jsonStr)) {
					try {
						Gson gson = new Gson();
						Type type = new TypeToken<BaseModel<OrderAddModel>>() {
						}.getType();
						BaseModel<OrderAddModel> base = gson.fromJson(jsonStr,
								type);
						// ToastUtils.showShortToast(TzmSelctAddressActivity.this,
						// base.error_msg);
						if (base.success) {
							OrderAddModel addModel = base.result;
							if (payMethod == 1) {
								goAliPay(addModel.aplipay);
							} else if (payMethod == 2) {
								goWeChat(addModel.wxpay);
							}
							// ToastUtils.showShortToast(
							// TzmSelctAddressActivity.this, "订单提交成功！");
							// startActivity(new Intent(
							// TzmSelctAddressActivity.this,
							// TzmOrderListActivity.class));
							// finish();
						}

					} catch (Exception e) {
						e.printStackTrace();
					}

				}

			}
		}, true);
	}

	/**
	 * 购物车提交订单
	 */
	protected void orderSubmit() {
		if (addressId == null) {
			ToastUtils.showToast(this, "请选择您的收货地址！");
			return;
		}
		// buyer_message = et_message.getText().toString();
		// ToastUtils.showShortToast(this, buyer_message);
		addorderApi = new TzmAddorderApi();
		apiClient = new ApiClient(this);
		addorderApi.setUid(userModel.uid);
		addorderApi.setAddressId(addressId);
		addorderApi.setCids(cids);
		addorderApi.setMessages(getMessage());
		addorderApi.setConsigneeType(1);
		addorderApi.setPayMethod(payMethod);
		addorderApi.setWallet(wallet);
		if (StringUtils.isNotEmpty(couponNo)) {
			addorderApi.setCouponNo(couponNo);
		}
		apiClient.api(addorderApi, new ApiListener() {

			@Override
			public void onStart() {
				ProgressDialogUtil.openProgressDialog(
						TzmSelctAddressActivity.this, "", "");

			}

			@Override
			public void onError(String error) {
				// TODO Auto-generated method stub
				super.onError(error);
				ToastUtils.showShortToast(TzmSelctAddressActivity.this, error);
				ProgressDialogUtil.closeProgressDialog();
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * com.ruiyu.taozhuma.api.ApiListener#onException(java.lang.Exception
			 * )
			 */
			@Override
			public void onException(Exception e) {
				// TODO Auto-generated method stub
				super.onException(e);
				ToastUtils
						.showShortToast(TzmSelctAddressActivity.this, "网络错误！");
				ProgressDialogUtil.closeProgressDialog();
			}

			@Override
			public void onComplete(String jsonStr) {
				ProgressDialogUtil.closeProgressDialog();
				if (StringUtils.isNotBlank(jsonStr)) {
					try {
						Gson gson = new Gson();
						Type type = new TypeToken<BaseModel<OrderAddModel>>() {
						}.getType();
						BaseModel<OrderAddModel> base = gson.fromJson(jsonStr,
								type);
						// ToastUtils.showShortToast(TzmSelctAddressActivity.this,
						// base.error_msg);
						if (base.success) {
							OrderAddModel addModel = base.result;
							if (addModel.fullpay == 0) {
								if (payMethod == 1) {
									goAliPay(addModel.aplipay);
								} else if (payMethod == 2) {
									goWeChat(addModel.wxpay);
								}
							} else {
								// 钱包全额
								Intent intent = new Intent(
										TzmSelctAddressActivity.this,
										ALiPayResultActivity.class);
								intent.putExtra("resultStatus", "9000");
								startActivity(intent);
								finish();
							}

							// ToastUtils.showShortToast(
							// TzmSelctAddressActivity.this, "订单提交成功！");
							// startActivity(new Intent(
							// TzmSelctAddressActivity.this,
							// TzmOrderListActivity.class));
							// finish();
						}

					} catch (Exception e) {
						e.printStackTrace();
					}

				}

			}
		}, true);
	}

	/**
	 * 限时秒杀购买提交订单
	 */
	protected void addOrderByPurchase() {

		if (addressId == null) {
			ToastUtils.showToast(this, "请选择您的收货地址！");
			return;
		}
		// buyer_message = et_message.getText().toString();
		// ToastUtils.showShortToast(this, buyer_message);
		addOrderByPurchaseApi = new TzmAddOrderByPurchaseApi();
		apiClient = new ApiClient(this);
		addOrderByPurchaseApi.setNum(1);
		addOrderByPurchaseApi.setUid(userModel.uid);
		addOrderByPurchaseApi.setAddressId(addressId);
		addOrderByPurchaseApi.setpid(pid);
		addOrderByPurchaseApi.setBuyer_message(getMessage());
		addOrderByPurchaseApi.setConsigneeType(1);
		addOrderByPurchaseApi.setPayMethod(payMethod);
		if (StringUtils.isNotEmpty(couponNo)) {
			addOrderByPurchaseApi.setCouponNo(couponNo);
		}
		apiClient.api(addOrderByPurchaseApi, new ApiListener() {

			@Override
			public void onStart() {
				ProgressDialogUtil.openProgressDialog(
						TzmSelctAddressActivity.this, "", "");

			}

			@Override
			public void onError(String error) {
				// TODO Auto-generated method stub
				super.onError(error);
				ToastUtils.showShortToast(TzmSelctAddressActivity.this, error);
				ProgressDialogUtil.closeProgressDialog();
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * com.ruiyu.taozhuma.api.ApiListener#onException(java.lang.Exception
			 * )
			 */
			@Override
			public void onException(Exception e) {
				// TODO Auto-generated method stub
				super.onException(e);
				ToastUtils
						.showShortToast(TzmSelctAddressActivity.this, "网络错误！");
				ProgressDialogUtil.closeProgressDialog();
			}

			@Override
			public void onComplete(String jsonStr) {
				ProgressDialogUtil.closeProgressDialog();
				if (StringUtils.isNotBlank(jsonStr)) {
					try {
						Gson gson = new Gson();
						Type type = new TypeToken<BaseModel<OrderAddModel>>() {
						}.getType();
						BaseModel<OrderAddModel> base = gson.fromJson(jsonStr,
								type);
						// ToastUtils.showShortToast(TzmSelctAddressActivity.this,
						// base.error_msg);
						if (base.success) {
							OrderAddModel addModel = base.result;
							if (payMethod == 1) {
								goAliPay(addModel.aplipay);
							} else if (payMethod == 2) {
								goWeChat(addModel.wxpay);
							}
							// ToastUtils.showShortToast(
							// TzmSelctAddressActivity.this, "订单提交成功！");
							// startActivity(new Intent(
							// TzmSelctAddressActivity.this,
							// TzmOrderListActivity.class));
							// finish();
						}

					} catch (Exception e) {
						e.printStackTrace();
					}

				}

			}
		}, true);

	}

	/**
	 * 计算总价 (受代金卷，偏远地区参数影响)
	 * 
	 * @param orderProduct1
	 */
	protected void getAllPrice(List<TzmOrderProduct1Model> orderProduct1) {
		allPrice = 0;
		expressPrice = 0;
		for (TzmOrderProduct1Model tzmOrderProductModel : orderProduct1) {
			allPrice = allPrice + tzmOrderProductModel.sumPrice;
			expressPrice = expressPrice + tzmOrderProductModel.espressPrice;
		}
		if (couponPrice > 0) {
			allPrice = allPrice - couponPrice;
		}
		if (isRemote) {
			allPrice = allPrice + expressPrice;
			tv_allprice.setText("总计：¥ " + df.format(allPrice));
			tv_shipment.setText("(含运费" + expressPrice + ")");
		} else {
			tv_allprice.setText("总计：¥ " + df.format(allPrice));
			tv_shipment.setText("(含运费0.0)");
		}

	}

	/**
	 * 获取用户留言
	 * 
	 * @return
	 */
	protected String getMessage() {
		String message = null;
		for (int i = 0; i < adapter.getList().size(); i++) {
			if (message == null) {
				message = adapter.getList().get(i).shopId + "-"
						+ adapter.getList().get(i).shopName + ":"
						+ adapter.getList().get(i).content;
			} else {
				message = message + ";" + adapter.getList().get(i).shopId + "-"
						+ adapter.getList().get(i).shopName + ":"
						+ adapter.getList().get(i).content;
			}
		}
		return message;
	}

	// 微信开始

	PayReq req;
	final IWXAPI msgApi = WXAPIFactory.createWXAPI(
			TzmSelctAddressActivity.this, null);

	protected void goWeChat(WeChat wxpay) {
		if (!PackageUtils.isApkInstalled(TzmSelctAddressActivity.this,
				"com.tencent.mm")) {
			ToastUtils.showShortToast(TzmSelctAddressActivity.this,
					"您的手机中尚未安装微信哦");
			return;
		}
		req = new PayReq();
		msgApi.registerApp(Constants.APP_ID);
		// 生成签名参数
		req.appId = Constants.APP_ID;
		req.partnerId = Constants.MCH_ID;
		req.prepayId = wxpay.prepayid;
		req.packageValue = "Sign=WXPay";
		req.nonceStr = wxpay.noncestr;
		req.timeStamp = wxpay.timestamp;
		req.sign = wxpay.sign;
		msgApi.registerApp(Constants.APP_ID);
		msgApi.sendReq(req);
	}

	// 微信结束
	// 支付宝开始
	// 商户PID
	private static final String PARTNER = "2088021116930692";
	// 商户收款账号
	private static final String SELLER = "taozhumab2c@5315.cn";
	// 商户私钥，pkcs8格式
	private static final String RSA_PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMDkVP71LwPi2oN2atT+dxJIxLYa5gjLLP6PjXM+xUHSixaXWrfzK17wuP8yA4JXCJKwElJDXO+pkrJmzr0GoUK5Dd117t6cS4pE6JQAh+ySdcPIxXa7V5kBI2C845zME1GrMQtOJgJ7BjirMBreTZrqOjd2Y+vimIfMNXElHqOrAgMBAAECgYAKxnaNC4tFz01fnKTZIyHprpD2j0gcY6UuzzwanP4N8EH+0BOsRPQ+SHo9H0Ec7dm3wAg1+jdd1/4u4xlXa+BkLfhINf3NxNo1quVwZevWa+I990yJv2SxsLQctd/FBXwaHdBXU0L81Q74Jr80XUvfKKNO3adfHSPXPfRi7gzPWQJBAOAFsdIqhjnP3gI3iJhukrbyGhs6munnOmQFSgtpOsAoS4Y1WpqR5dOsrrThhHp+Hbp6Agw5HTWbyzJxrA+ORm8CQQDcbQ+4UwL1eDXMTIo6R+Xei29kOXYUiYmFSVgTRXTE3UbDH9UXRYIdDKzgC6kGcUQ7HbwYaCYeIY+nAbuLMrSFAkEAsr9Yd10+5HobpqEw+I+huR/L/NXyLZg2WwBtaFV6b0C3okqIFLU9MHykmecnkaV3iwc3AxG/YLsCkyloJLFdOQJBAMzRCipb+APfDx4mgMpmxFFlfrvCKNYsdnxDCnPdtgK3GWl1yqs1CxGzFW+ZZhV2adAEcSK8o66bwL+N/JX7J4ECQEcEFMyF3jyT/3q/FSEiPTuT0EG9qi3GF9NG0nz6ikE8lTv1moIQqJpaLp8kWD8p9fDC4iHp2kI+IcEIj5HzeZY=";
	// 支付宝公钥
	// private static final String RSA_PUBLIC = "";
	private static final int SDK_PAY_FLAG = 1;

	// private static final int SDK_CHECK_FLAG = 2;

	/**
	 * 支付宝支付发起
	 */
	protected void goAliPay(AliPay aplipay) {

		// 订单
		String orderInfo = getOrderInfo(aplipay);

		// 对订单做RSA 签名
		String sign = sign(orderInfo);
		try {
			// 仅需对sign 做URL编码
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// 完整的符合支付宝参数规范的订单信息
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
				+ getSignType();

		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask(TzmSelctAddressActivity.this);
				// 调用支付接口，获取支付结果
				String result = alipay.pay(payInfo);

				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();

	}

	/**
	 * create the order info. 创建订单信息
	 * 
	 */
	public String getOrderInfo(AliPay aplipay) {

		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" + PARTNER + "\"";

		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + aplipay.out_trade_no + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + aplipay.subject + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + aplipay.subject + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + aplipay.total_fee + "\"";

		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\"" + aplipay.notify_url + "\"";

		// 服务接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";

		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		orderInfo += "&return_url=\"m.alipay.com\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
		// orderInfo += "&paymethod=\"expressGateway\"";

		return orderInfo;
	}

	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param content
	 *            待签名订单信息
	 */
	public String sign(String content) {
		return SignUtils.sign(content, RSA_PRIVATE);
	}

	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 */
	public String getSignType() {
		return "sign_type=\"RSA\"";
	}

	// 支付宝结束

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_head_left:
			onBackPressed();
			break;
		case R.id.rl_address:
			Intent intent = new Intent(this, TzmAddressListActivity.class);
			intent.putExtra("tag", AppConfig.TAG_ADDRESS_SELECT);
			startActivityForResult(intent, TAG_SELECT_ADDRESS);
			break;
		case R.id.rl_alipay:
			handler.sendEmptyMessage(1);
			break;
		case R.id.rl_wechat:
			handler.sendEmptyMessage(2);
			break;
		case R.id.rl_coupon:
			Intent intent2 = new Intent(TzmSelctAddressActivity.this,
					UserCouponListActivity.class);
			intent2.putExtra("tag", 2);
			intent2.putExtra("price", allPrice);
			startActivityForResult(intent2, TAG_SELECT_COUPON);
			break;

		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 地址选择返回
		if (resultCode == RESULT_OK && requestCode == TAG_SELECT_ADDRESS) {
			TzmAddressModel model = (TzmAddressModel) data.getExtras()
					.getSerializable("data");
			addressId = model.addId;
			tv_address.setTextColor(getResources().getColor(R.color.black));

			if (model.isRemote == 1) {
				isRemote = true;
				tv_address.setText("收货人:" + model.name + "    电话：" + model.tel
						+ "\n地址：（偏远地区）" + model.address);
			} else {
				isRemote = false;
				tv_address.setText("收货人:" + model.name + "    电话：" + model.tel
						+ "\n地址：" + model.address);
			}

			// ToastUtils.showToast(TzmSelctAddressActivity.this,
			// model.address);
		}
		// 代金卷选择返回
		if (resultCode == RESULT_OK && requestCode == TAG_SELECT_COUPON) {
			UserCouponListModel model = (UserCouponListModel) data.getExtras()
					.getSerializable("model");
			tv_coupon_price.setText("-¥ " + model.m);
			couponNo = model.couponNo;
			couponPrice = Float.valueOf(model.m);
		}
		getAllPrice(list.result);

	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				payMethod = 1;
				iv_alipay.setImageResource(R.drawable.tzm128);
				iv_wechat.setImageResource(R.drawable.tzm127);
				break;
			case 2:
				payMethod = 2;
				iv_alipay.setImageResource(R.drawable.tzm127);
				iv_wechat.setImageResource(R.drawable.tzm128);
				break;
			}
		};
	};

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				PayResult payResult = new PayResult((String) msg.obj);

				// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
				// String resultInfo = payResult.getResult();

				String resultStatus = payResult.getResultStatus();

				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档

				if (TextUtils.equals(resultStatus, "8000")) {
					ToastUtils.showShortToast(TzmSelctAddressActivity.this,
							"支付结果确认中");

				} else {
					Intent intent = new Intent(TzmSelctAddressActivity.this,
							ALiPayResultActivity.class);
					intent.putExtra("resultStatus", resultStatus);
					startActivity(intent);
					finish();
				}
				// if (TextUtils.equals(resultStatus, "9000")) {
				// Toast.makeText(TzmSelctAddressActivity.this, "支付成功",
				// Toast.LENGTH_SHORT).show();
				// startActivity(new Intent(TzmSelctAddressActivity.this,
				// TzmOrderListActivity.class));
				// finish();
				// } else {
				// // 判断resultStatus 为非“9000”则代表可能支付失败
				// //
				// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
				// if (TextUtils.equals(resultStatus, "8000")) {
				// Toast.makeText(TzmSelctAddressActivity.this, "支付结果确认中",
				// Toast.LENGTH_SHORT).show();
				//
				// } else {
				// // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
				// Toast.makeText(TzmSelctAddressActivity.this, "支付失败",
				// Toast.LENGTH_SHORT).show();
				// }
				// }
				break;
			}
			// case SDK_CHECK_FLAG: {
			// Toast.makeText(TzmSelctAddressActivity.this,
			// "检查结果为：" + msg.obj, Toast.LENGTH_SHORT).show();
			// break;
			// }
			// default:
			// break;
			}
		};
	};

	private void checkLogin() {
		isLogin = UserInfoUtils.isLogin();
		if (isLogin) {
			userModel = UserInfoUtils.getUserInfo();
		}

	}

	/**
	 * 获取默认地址
	 */
	private void getDefaultAddress() {
		apiClient2 = new ApiClient(this);
		defaultAddressApi = new DefaultAddressApi();
		defaultAddressApi.setUid(userModel.uid);
		apiClient2.api(defaultAddressApi, new ApiListener() {

			@Override
			public void onStart() {
			}

			@Override
			public void onError(String error) {
				ToastUtils.showShortToast(TzmSelctAddressActivity.this,
						R.string.msg_list_null);
			}

			@Override
			public void onException(Exception e) {
				LogUtil.ErrorLog(e);
			}

			@Override
			public void onComplete(String jsonStr) {
				Gson gson = new Gson();
				Type type = new TypeToken<BaseModel<TzmAddressModel>>() {
				}.getType();
				BaseModel<TzmAddressModel> base = gson.fromJson(jsonStr, type);
				if (base.result != null) {
					TzmAddressModel model = base.result;
					addressId = model.addId;
					tv_address.setTextColor(getResources().getColor(
							R.color.black));
					if (model.isRemote == 1) {
						isRemote = true;
						tv_address.setText("收货人:" + model.name + "    电话："
								+ model.tel + "\n地址：（偏远地区）" + model.address);
					} else {
						isRemote = false;
						tv_address.setText("收货人:" + model.name + "    电话："
								+ model.tel + "\n地址：" + model.address);
					}

				}
			}
		}, true);
	}

}
