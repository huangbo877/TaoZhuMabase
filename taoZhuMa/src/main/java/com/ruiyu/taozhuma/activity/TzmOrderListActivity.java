package com.ruiyu.taozhuma.activity;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;

import net.sourceforge.simcpux.Constants;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.adapter.TzmOrderListAdapter;
import com.ruiyu.taozhuma.api.ApiClient;
import com.ruiyu.taozhuma.api.ApiListener;
import com.ruiyu.taozhuma.api.PayOrderApi;
import com.ruiyu.taozhuma.api.TzmMyordertApi;
import com.ruiyu.taozhuma.api.UserWelletDetailApi;
import com.ruiyu.taozhuma.base.xUtilsImageLoader;
import com.ruiyu.taozhuma.dialog.PayMethodDialog;
import com.ruiyu.taozhuma.even.RePayEven;
import com.ruiyu.taozhuma.model.BaseModel;
import com.ruiyu.taozhuma.model.OrderAddModel;
import com.ruiyu.taozhuma.model.TzmMyorderModel;
import com.ruiyu.taozhuma.model.UserModel;
import com.ruiyu.taozhuma.model.UserWelletDetailModel;
import com.ruiyu.taozhuma.model.OrderAddModel.AliPay;
import com.ruiyu.taozhuma.model.OrderAddModel.WeChat;
import com.ruiyu.taozhuma.utils.LogUtil;
import com.ruiyu.taozhuma.utils.PackageUtils;
import com.ruiyu.taozhuma.utils.PayResult;
import com.ruiyu.taozhuma.utils.ProgressDialogUtil;
import com.ruiyu.taozhuma.utils.SignUtils;
import com.ruiyu.taozhuma.utils.StringUtils;
import com.ruiyu.taozhuma.utils.ToastUtils;
import com.ruiyu.taozhuma.utils.UserInfoUtils;
import com.ruiyu.taozhuma.wxapi.ALiPayResultActivity;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import de.greenrobot.event.EventBus;

public class TzmOrderListActivity extends Activity {
	@ViewInject(R.id.txt_head_title)
	private TextView txt_head_title;
	@ViewInject(R.id.btn_head_left)
	private Button btn_head_left;
	private ListView lv_order_list;

	private ApiClient apiClient;
	private TzmMyordertApi tzmMyordertApi;
	private ArrayList<TzmMyorderModel> tzmMyorderModels;
	private TzmOrderListAdapter tzmOrderListAdapter;

	// 使用PullToRefreshListView
	@ViewInject(R.id.lv_order_list)
	private PullToRefreshListView mPullRefreshListView;
	private int currentPage = 1;// 当前页数
	private boolean isLoadMore = false;// 是否是加载更多
	private int listPosition = 0;
	private int mark = 0;// 默认类型是0:全部订单,可以修改

	// 用户信息
	private Boolean isLogin;
	private UserModel userModel;

	private xUtilsImageLoader imageLoader;
	@ViewInject(R.id.rl_bg)
	private RelativeLayout rl_bg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tzm_order_list_activity);
		imageLoader = new xUtilsImageLoader(TzmOrderListActivity.this);
		ViewUtils.inject(this);
		EventBus.getDefault().register(this);
		txt_head_title.setText("全部订单");
		btn_head_left.setOnClickListener(clickListener);
		mark = getIntent().getIntExtra("mark", 0);
		if (mark == 1) {
			txt_head_title.setText("待付款");
		} else if (mark == 2) {
			txt_head_title.setText("待发货");
		} else if (mark == 3) {
			txt_head_title.setText("待收货");
		} else if (mark == 4) {
			txt_head_title.setText("待评价");
		}

		apiClient = new ApiClient(this);
		tzmMyordertApi = new TzmMyordertApi();
		tzmMyorderModels = new ArrayList<TzmMyorderModel>();
		mPullRefreshListView
				.setOnRefreshListener(new OnRefreshListener2<ListView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// TODO 下拉刷新
						String label = DateUtils.formatDateTime(
								TzmOrderListActivity.this
										.getApplicationContext(), System
										.currentTimeMillis(),
								DateUtils.FORMAT_SHOW_TIME
										| DateUtils.FORMAT_SHOW_DATE
										| DateUtils.FORMAT_ABBREV_ALL);

						refreshView.getLoadingLayoutProxy()
								.setLastUpdatedLabel(label);

						isLoadMore = false;
						currentPage = 1;
						loadData(mark);
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						isLoadMore = true;
						currentPage++;
						loadData(mark);
					}
				});
		lv_order_list = mPullRefreshListView.getRefreshableView();
		loadData(mark);// 默认加载类型0,即全部

	}

	private UserWelletDetailApi userWelletDetailApi;

	/**
	 * 获取钱包钱数
	 * @param orderPrice
	 * @param isWallet 是否为钱包商品（0-为普通商品 或者 普通商品与钱包商品2者混合；1-为钱包商品）
	 */
	private void loadWallet(final double orderPrice, final String isWallet) {
		apiClient = new ApiClient(this);
		userWelletDetailApi = new UserWelletDetailApi();
		userWelletDetailApi.setUid(userModel.uid);
		userWelletDetailApi.setPageNo(1000);
		apiClient.api(this.userWelletDetailApi, new ApiListener() {
			@Override
			public void onStart() {
				ProgressDialogUtil.openProgressDialog(
						TzmOrderListActivity.this, "", "");
			}

			@Override
			public void onComplete(String jsonStr) {
				ProgressDialogUtil.closeProgressDialog();
				try {
					Gson gson = new Gson();
					Type type = new TypeToken<BaseModel<UserWelletDetailModel>>() {
					}.getType();
					BaseModel<UserWelletDetailModel> base = gson.fromJson(
							jsonStr, type);
					if (base.result != null) {
						if (base.result.balance > orderPrice
								&& StringUtils.isNotEmpty(isWallet)
								&& "1".equals(isWallet)) {
							// 用钱包
							showPayDialog(true);
						} else {
							showPayDialog(false);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			@Override
			public void onError(String error) {
				ProgressDialogUtil.closeProgressDialog();
				ToastUtils.showShortToast(TzmOrderListActivity.this, error);
				// LogUtil.Log(error);
			}

			@Override
			public void onException(Exception e) {
				ProgressDialogUtil.closeProgressDialog();
				ToastUtils.showShortToast(TzmOrderListActivity.this, "网络异常");
				LogUtil.ErrorLog(e);
			}
		}, true);

	}

	private double orderPrice;
	private String isWallet;

	// 订单付款
	public void onEventMainThread(RePayEven event) {
		orderNumber = event.getOrderNumber();
		orderPrice = event.getOrderPrice();
		isWallet = event.getIsWallet();
		loadWallet(orderPrice, isWallet);

		// ToastUtils.showShortToast(this, event.getOrderNumber());
	}

	private void showPayDialog(boolean wallet) {
		PayMethodDialog.Builder builder = new PayMethodDialog.Builder(this);
		builder.setALiPayClickListener(new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				loadRePay(1);

			}
		});
		builder.setWeChatClickListener(new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				loadRePay(2);

			}
		});
		builder.setcancleClickListener(new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();

			}
		});
		if (wallet) {
			builder.setWalletPay(true);
			builder.setwalletClickListener(new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
					loadRePay(4);

				}
			});
		}

		builder.create().show();
	}

	/**
	 * /1:支付宝 ；2：微信支付
	 */
	// 支付
	private PayOrderApi payOrderApi;
	private String orderNumber;

	private void loadRePay(final int method) {
		payOrderApi = new PayOrderApi();
		apiClient = new ApiClient(this);
		payOrderApi.setUid(userModel.uid);
		payOrderApi.setPayMethod(method);
		payOrderApi.setOrderNo(orderNumber);
		apiClient.api(payOrderApi, new ApiListener() {

			@Override
			public void onStart() {
				ProgressDialogUtil.openProgressDialog(
						TzmOrderListActivity.this, "", "");

			}

			@Override
			public void onError(String error) {
				super.onError(error);
				ToastUtils.showShortToast(TzmOrderListActivity.this, error);
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
							if (method == 1) {
								goAliPay(addModel.aplipay);
							} else if (method == 2) {
								goWeChat(addModel.wxpay);
							} else if (method == 4) {
								Intent intent = new Intent(
										TzmOrderListActivity.this,
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

	// 微信开始

	PayReq req;
	final IWXAPI msgApi = WXAPIFactory.createWXAPI(TzmOrderListActivity.this,
			null);

	// StringBuffer sb;
	protected void goWeChat(WeChat wxpay) {
		if (!PackageUtils.isApkInstalled(TzmOrderListActivity.this,
				"com.tencent.mm")) {
			ToastUtils
					.showShortToast(TzmOrderListActivity.this, "您的手机中尚未安装微信哦");
			return;
		}
		req = new PayReq();
		// sb=new StringBuffer();
		msgApi.registerApp(Constants.APP_ID);
		// 生成prepay_id
		// GetPrepayIdTask getPrepayId = new GetPrepayIdTask();
		// getPrepayId.execute();
		// 生成签名参数
		req.appId = Constants.APP_ID;
		req.partnerId = Constants.MCH_ID;
		req.prepayId = wxpay.prepayid;
		req.packageValue = "Sign=WXPay";
		req.nonceStr = wxpay.noncestr;
		req.timeStamp = wxpay.timestamp;
		req.sign = wxpay.sign;

		// List<NameValuePair> signParams = new LinkedList<NameValuePair>();
		// signParams.add(new BasicNameValuePair("appid", req.appId));
		// signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
		// signParams.add(new BasicNameValuePair("package", req.packageValue));
		// signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
		// signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
		// signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));

		// req.sign = genAppSign(signParams);

		// sb.append("sign\n" + req.sign + "\n\n");

		// show.setText(sb.toString());

		// Log.e("orion", signParams.toString());
		// sendPayReq
		msgApi.registerApp(Constants.APP_ID);
		msgApi.sendReq(req);
	}

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
				PayTask alipay = new PayTask(TzmOrderListActivity.this);
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
					ToastUtils.showShortToast(TzmOrderListActivity.this, "支付结果确认中");

				} else {
					Intent intent = new Intent(TzmOrderListActivity.this,
							ALiPayResultActivity.class);
					intent.putExtra("resultStatus", resultStatus);
					startActivity(intent);
					// finish();
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

	// 支付宝结束
	private void loadData(int sign) {
		tzmMyordertApi.setOrderStatus(sign);
		tzmMyordertApi.setPageNo(currentPage);
		isLogin = UserInfoUtils.isLogin();
		if (isLogin) {
			userModel = UserInfoUtils.getUserInfo();
		}
		tzmMyordertApi.setUid(userModel.uid);
		apiClient.api(this.tzmMyordertApi, new ApiListener() {
			@Override
			public void onStart() {
				ProgressDialogUtil.openProgressDialog(
						TzmOrderListActivity.this, "", "");
			}

			@Override
			public void onComplete(String jsonStr) {
				ProgressDialogUtil.closeProgressDialog();
				Gson gson = new Gson();
				Type type = new TypeToken<BaseModel<ArrayList<TzmMyorderModel>>>() {
				}.getType();
				BaseModel<ArrayList<TzmMyorderModel>> base = gson.fromJson(
						jsonStr, type);
				if (isLoadMore) {
					// 加载更多
					listPosition = tzmMyorderModels.size();
					if (base.result != null && base.result.size() > 0) {
						tzmMyorderModels.addAll(base.result);
					} else {
						ToastUtils.showShortToast(TzmOrderListActivity.this,
								R.string.msg_list_null);
					}

				} else {
					// 刷新
					listPosition = 0;
					tzmMyorderModels.clear();
					if (base.result != null && base.result.size() > 0) {
						tzmMyorderModels = base.result;
						rl_bg.setVisibility(View.GONE);
						mPullRefreshListView.setVisibility(View.VISIBLE);
						lv_order_list.setVisibility(View.VISIBLE);
					} else {
						rl_bg.setVisibility(View.VISIBLE);
						mPullRefreshListView.setVisibility(View.GONE);
						lv_order_list.setVisibility(View.GONE);
					}
				}
				initOrderList();
			}

			@Override
			public void onError(String error) {
				mPullRefreshListView.onRefreshComplete();
			}

			@Override
			public void onException(Exception e) {
				mPullRefreshListView.onRefreshComplete();
				LogUtil.ErrorLog(e);
			}
		}, true);

	}

	protected void initOrderList() {
		if (this.tzmMyorderModels != null) {
			tzmOrderListAdapter = new TzmOrderListAdapter(
					TzmOrderListActivity.this, this.tzmMyorderModels,
					imageLoader);
			lv_order_list.setAdapter(tzmOrderListAdapter);// 调用Adapter的getView()绘制item
			lv_order_list.setSelection(listPosition);
			mPullRefreshListView.onRefreshComplete();
		}
	}

	OnClickListener clickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_head_left:
				onBackPressed();
				break;
			}
		}
	};

	protected void onRestart() {
		super.onRestart();
		currentPage = 1;
		isLoadMore = false;
		loadData(mark);
	};

	@OnClick(R.id.ib_guang)
	private void buttonClick(View view) {
		Intent intent = new Intent(TzmOrderListActivity.this,
				TenNewActivity.class);
		intent.putExtra("type", 1);
		startActivity(intent);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
}
