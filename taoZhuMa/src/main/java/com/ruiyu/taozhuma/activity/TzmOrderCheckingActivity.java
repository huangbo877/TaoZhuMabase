package com.ruiyu.taozhuma.activity;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;

import net.sourceforge.simcpux.Constants;

import android.R.string;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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
import com.ruiyu.taozhuma.api.TzmMyRefundApi;
import com.ruiyu.taozhuma.api.TzmMyordertApi;
import com.ruiyu.taozhuma.api.TzmOrderCheskApi;
import com.ruiyu.taozhuma.api.TzmOrderCheskFavoriteApi;
import com.ruiyu.taozhuma.base.xUtilsImageLoader;
import com.ruiyu.taozhuma.dialog.PayMethodDialog;
import com.ruiyu.taozhuma.even.RePayEven;
import com.ruiyu.taozhuma.model.BaseModel;
import com.ruiyu.taozhuma.model.OrderAddModel;
import com.ruiyu.taozhuma.model.TzmGuessFavoriteListModel;
import com.ruiyu.taozhuma.model.TzmMyorderModel;
import com.ruiyu.taozhuma.model.TzmOrderCheskModel;
import com.ruiyu.taozhuma.model.UserModel;
import com.ruiyu.taozhuma.model.OrderAddModel.AliPay;
import com.ruiyu.taozhuma.model.OrderAddModel.WeChat;
import com.ruiyu.taozhuma.utils.LogUtil;
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

/**
 * 
 * 
 * 
 * @author huangbo 2015-11-10 上午9:57:57
 */
public class TzmOrderCheckingActivity extends Activity {
	@ViewInject(R.id.txt_head_title)
	private TextView txt_head_title;
	@ViewInject(R.id.btn_head_left)
	private Button btn_head_left;

	private ApiClient apiClient;
	private ApiClient apiClient1;
	private TzmOrderCheskApi tzmOrderCheskApi;
	private TzmOrderCheskModel tzmOrderCheskModel;
	TzmOrderCheskFavoriteApi tzmOrderCheskFavoriteApi;
	ArrayList<TzmGuessFavoriteListModel> tzmGuessFavoriteListModel;
	private xUtilsImageLoader imageLoader;

	@ViewInject(R.id.tv_refund_type)
	private TextView tv_refund_type;
	@ViewInject(R.id.tv_refund_m)
	private TextView tv_refund_m;
	@ViewInject(R.id.tv_refund_y)
	private TextView tv_refund_y;
	@ViewInject(R.id.tv_refund_s)
	private TextView tv_refund_s;
	@ViewInject(R.id.tv_status1)
	private TextView tv_status1;
	@ViewInject(R.id.tv_status)
	private TextView tv_status;

	@ViewInject(R.id.im_like01)
	private ImageView im_like01;
	@ViewInject(R.id.im_like02)
	private ImageView im_like02;
	@ViewInject(R.id.im_like03)
	private ImageView im_like03;
	@ViewInject(R.id.im_like04)
	private ImageView im_like04;

	@ViewInject(R.id.tv_like_name01)
	private TextView tv_like_name01;
	@ViewInject(R.id.tv_like_name02)
	private TextView tv_like_name02;
	@ViewInject(R.id.tv_like_name03)
	private TextView tv_like_name03;
	@ViewInject(R.id.tv_like_name04)
	private TextView tv_like_name04;
	@ViewInject(R.id.tv_like_price01)
	private TextView tv_like_price01;
	@ViewInject(R.id.tv_like_price02)
	private TextView tv_like_price02;
	@ViewInject(R.id.tv_like_price03)
	private TextView tv_like_price03;
	@ViewInject(R.id.tv_like_price04)
	private TextView tv_like_price04;

	@ViewInject(R.id.rl_like01)
	private RelativeLayout rl_like01;
	@ViewInject(R.id.rl_like02)
	private RelativeLayout rl_like02;
	@ViewInject(R.id.rl_like03)
	private RelativeLayout rl_like03;
	@ViewInject(R.id.rl_like04)
	private RelativeLayout rl_like04;
	// 用户信息
	private Boolean isLogin;
	private UserModel userModel;
	int oid;
	int reStatus;
	int productId;
	String reStatus1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tzm_order_checking_activity);
		ViewUtils.inject(this);
		oid = getIntent().getIntExtra("oid", 0);
		reStatus = getIntent().getIntExtra("reStatus", 0);
		productId = getIntent().getIntExtra("productId", 0);
		if (reStatus == 1) {
			txt_head_title.setText("申请审核");
		} else if (reStatus == 2) {
			txt_head_title.setText("请退货");
		} else if (reStatus == 3) {
			txt_head_title.setText("退货中");
		} else if (reStatus == 4) {
			txt_head_title.setText("退货成功");
		} else if (reStatus == 5) {
			txt_head_title.setText("退货失败");
		} else if (reStatus == 6) {
			txt_head_title.setText("申请失败");
		} else if (reStatus == 7) {
			txt_head_title.setText("退款成功");
		}

		// txt_head_title.setText("审核状态");
		apiClient = new ApiClient(this);
		apiClient1 = new ApiClient(this);
		tzmOrderCheskApi = new TzmOrderCheskApi();
		tzmOrderCheskFavoriteApi = new TzmOrderCheskFavoriteApi();
		imageLoader = new xUtilsImageLoader(this);
		loadData(oid);
		loadData1();
		btn_head_left.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	// 退货信息
	private void loadData(int i) {

		isLogin = UserInfoUtils.isLogin();
		if (isLogin) {
			userModel = UserInfoUtils.getUserInfo();
		}
		tzmOrderCheskApi.setUid(userModel.uid);
		tzmOrderCheskApi.setOid(i);
		apiClient.api(this.tzmOrderCheskApi, new ApiListener() {
			@Override
			public void onStart() {
				// ProgressDialogUtil.openProgressDialog(
				// TzmOrderCheckingActivity.this, "", "");
			}

			@Override
			public void onComplete(String jsonStr) {
				// ProgressDialogUtil.closeProgressDialog();
				Gson gson = new Gson();
				Type type = new TypeToken<BaseModel<TzmOrderCheskModel>>() {
				}.getType();
				BaseModel<TzmOrderCheskModel> base = gson.fromJson(jsonStr,
						type);

				if (base.result != null) {
					if (base.result.type != null) {
						switch (Integer.parseInt(base.result.type)) {
						case 1:
							tv_refund_type.setText("退款");

							break;
						case 2:
							tv_refund_type.setText("仅退货");

							break;
						case 3:
							tv_refund_type.setText("售后退货");

							break;

						}
					}
					// tv_refund_type.setText(base.result.type);
					tv_refund_m.setText("¥ " + base.result.refundPrice);
					if (base.result.refundType != null) {
						switch (Integer.parseInt(base.result.refundType)) {
						case 1:
							tv_refund_y.setText("不喜欢");

							break;
						case 2:
							tv_refund_y.setText("质量不好");

							break;
						case 3:
							tv_refund_y.setText("尺码不对");

							break;
						case 4:
							tv_refund_y.setText("颜色不对");

							break;
						case 5:
							tv_refund_y.setText("其他");

							break;

						}
					}
					tv_refund_s.setText(base.result.refundReason);

				} else {
					ToastUtils.showShortToast(TzmOrderCheckingActivity.this,
							R.string.msg_list_null);
				}

			}

			@Override
			public void onError(String error) {
				// ProgressDialogUtil.closeProgressDialog();
			}

			@Override
			public void onException(Exception e) {
				// //
			}
		}, true);

	}

	// 猜你喜欢
	private void loadData1() {

		isLogin = UserInfoUtils.isLogin();
		if (isLogin) {
			userModel = UserInfoUtils.getUserInfo();
		}
		tzmOrderCheskFavoriteApi.setUid(userModel.uid);
		// tzmOrderCheskFavoriteApi.setId(k);
		apiClient1.api(this.tzmOrderCheskFavoriteApi, new ApiListener() {
			@Override
			public void onStart() {
				ProgressDialogUtil.openProgressDialog(
						TzmOrderCheckingActivity.this, "", "");
			}

			@Override
			public void onComplete(String jsonStr) {
				ProgressDialogUtil.closeProgressDialog();
				Gson gson = new Gson();
				Type type = new TypeToken<BaseModel<ArrayList<TzmGuessFavoriteListModel>>>() {
				}.getType();
				BaseModel<ArrayList<TzmGuessFavoriteListModel>> base = gson
						.fromJson(jsonStr, type);
				tzmGuessFavoriteListModel = base.result;
				if (base.result != null) {
					int size = base.result.size();
					if (size > 0) {
						rl_like01.setVisibility(View.VISIBLE);
						imageLoader.display(im_like01, base.result.get(0).image);
						tv_like_name01.setText(base.result.get(0).productName);
						tv_like_price01.setText("¥ "
								+ base.result.get(0).sellingPrice);
						// rl_like01.setOnClickListener(clickListener);
						rl_like01.setOnClickListener(clickListener);
						rl_like01.setTag(0);
					}
					if (size > 1) {
						rl_like02.setVisibility(View.VISIBLE);
						imageLoader.display(im_like02, base.result.get(1).image);
						tv_like_name02.setText(base.result.get(1).productName);
						tv_like_price02.setText("¥ "
								+ base.result.get(1).sellingPrice);
						// rl_like02.setOnClickListener(clickListener);
						rl_like02.setOnClickListener(clickListener);
						rl_like02.setTag(1);
					}
					if (size > 2) {
						rl_like03.setVisibility(View.VISIBLE);
						imageLoader.display(im_like03, base.result.get(2).image);
						tv_like_name03.setText(base.result.get(2).productName);
						tv_like_price03.setText("¥ "
								+ base.result.get(2).sellingPrice);
						// rl_like03.setOnClickListener(clickListener);
						rl_like03.setOnClickListener(clickListener);
						rl_like03.setTag(2);
					}
					if (size > 3) {
						rl_like04.setVisibility(View.VISIBLE);
						imageLoader.display(im_like04, base.result.get(3).image);
						tv_like_name04.setText(base.result.get(3).productName);
						tv_like_price04.setText("¥ "
								+ base.result.get(3).sellingPrice);
						// rl_like04.setOnClickListener(clickListener);
						rl_like04.setOnClickListener(clickListener);
						rl_like04.setTag(3);
					}

				} else {
					ToastUtils.showShortToast(TzmOrderCheckingActivity.this,
							R.string.msg_list_null);
				}

			}

			@Override
			public void onError(String error) {
				ProgressDialogUtil.closeProgressDialog();
			}

			@Override
			public void onException(Exception e) {
				ProgressDialogUtil.closeProgressDialog();
				LogUtil.ErrorLog(e);
			}
		}, true);

	}

	OnClickListener clickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {

			case R.id.rl_like01:
				Intent intent = new Intent(TzmOrderCheckingActivity.this,
						ProductDetailActivity.class);
				intent.putExtra("id", Integer
						.parseInt(tzmGuessFavoriteListModel.get(0).productId));
				intent.putExtra("activityId", Integer
						.parseInt(tzmGuessFavoriteListModel.get(0).activityId));

				// System.out.println(tzmGuessFavoriteListModel.get(0).activityId);
				startActivity(intent);
				break;
			case R.id.rl_like02:
				Intent intent2 = new Intent(TzmOrderCheckingActivity.this,
						ProductDetailActivity.class);
				intent2.putExtra("id", Integer
						.parseInt(tzmGuessFavoriteListModel.get(1).productId));
				intent2.putExtra("activityId", Integer
						.parseInt(tzmGuessFavoriteListModel.get(1).activityId));

				startActivity(intent2);
				break;
			case R.id.rl_like03:
				Intent intent3 = new Intent(TzmOrderCheckingActivity.this,
						ProductDetailActivity.class);
				intent3.putExtra("id", Integer
						.parseInt(tzmGuessFavoriteListModel.get(2).productId));
				intent3.putExtra("activityId", Integer
						.parseInt(tzmGuessFavoriteListModel.get(2).activityId));
				startActivity(intent3);
				break;
			case R.id.rl_like04:
				Intent intent4 = new Intent(TzmOrderCheckingActivity.this,
						ProductDetailActivity.class);
				intent4.putExtra("id", Integer
						.parseInt(tzmGuessFavoriteListModel.get(3).productId));
				intent4.putExtra("activityId", Integer
						.parseInt(tzmGuessFavoriteListModel.get(3).activityId));
				startActivity(intent4);
				break;

			}
		}
	};
}
