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
import com.ruiyu.taozhuma.api.TzmMyRefundApi;
import com.ruiyu.taozhuma.api.TzmMyordertApi;
import com.ruiyu.taozhuma.api.TzmOrderCheskApi;
import com.ruiyu.taozhuma.base.xUtilsImageLoader;
import com.ruiyu.taozhuma.dialog.PayMethodDialog;
import com.ruiyu.taozhuma.even.RePayEven;
import com.ruiyu.taozhuma.model.BaseModel;
import com.ruiyu.taozhuma.model.OrderAddModel;
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
 * @author huangbo 2015-11-10 上午9:57:50
 */
public class TzmOrderCheckActivity extends Activity {
	@ViewInject(R.id.txt_head_title)
	private TextView txt_head_title;
	@ViewInject(R.id.btn_head_left)
	private Button btn_head_left;

	private ApiClient apiClient;
	private TzmOrderCheskApi tzmOrderCheskApi;
	private TzmOrderCheskModel tzmOrderCheskModel;

	@ViewInject(R.id.tv_refundPrice)
	private TextView tv_refundPrice;
	@ViewInject(R.id.tv_createDate)
	private TextView tv_createDate;
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
		setContentView(R.layout.tzm_order_check_activity);
		ViewUtils.inject(this);
		oid = getIntent().getIntExtra("oid", 0);
		reStatus = getIntent().getIntExtra("reStatus", 0);
		productId = getIntent().getIntExtra("productId", 0);
		if (reStatus == 1) {
			txt_head_title.setText("审核中");
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
		tzmOrderCheskApi = new TzmOrderCheskApi();
		loadData(oid);
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
				ProgressDialogUtil.openProgressDialog(
						TzmOrderCheckActivity.this, "", "");
			}

			@Override
			public void onComplete(String jsonStr) {
				ProgressDialogUtil.closeProgressDialog();
				Gson gson = new Gson();
				Type type = new TypeToken<BaseModel<TzmOrderCheskModel>>() {
				}.getType();
				BaseModel<TzmOrderCheskModel> base = gson.fromJson(jsonStr,
						type);
				try {

					if (base.result != null) {
						tv_refundPrice.setText(base.result.refundPrice + "元");
						tv_createDate.setText(base.result.createDate);
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
						if (base.result.status != null) {
							switch (Integer.parseInt(base.result.status)) {
							case 4:
								tv_status.setText("退款成功");
								tv_status1.setBackgroundDrawable(getResources()
										.getDrawable(R.drawable.tzmcheak01));
								break;
							case 5:
								tv_status.setText("退款失败");
								tv_status1.setBackgroundDrawable(getResources()
										.getDrawable(R.drawable.tzmcheak02));
								break;
							case 6:
								tv_status.setText("退款失败");
								tv_status1.setBackgroundDrawable(getResources()
										.getDrawable(R.drawable.tzmcheak02));
								break;
							default:
								break;
							}
						}

					} else {
						ToastUtils.showShortToast(TzmOrderCheckActivity.this,
								R.string.msg_list_null);
					}
				} catch (Exception e) {
					// TODO: handle exception
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
}
