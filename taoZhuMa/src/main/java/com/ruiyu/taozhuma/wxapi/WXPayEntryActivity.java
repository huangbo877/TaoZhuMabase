package com.ruiyu.taozhuma.wxapi;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.simcpux.Constants;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.activity.MainTabActivity;
import com.ruiyu.taozhuma.activity.TzmActivity;
import com.ruiyu.taozhuma.activity.TzmOrderListActivity;
import com.ruiyu.taozhuma.activity.ProductDetailActivity;
import com.ruiyu.taozhuma.adapter.TzmActivityAdapter;
import com.ruiyu.taozhuma.api.ApiClient;
import com.ruiyu.taozhuma.api.ApiListener;
import com.ruiyu.taozhuma.api.TzmActivityApi;
import com.ruiyu.taozhuma.base.xUtilsImageLoader;
import com.ruiyu.taozhuma.even.WeChatEven;
import com.ruiyu.taozhuma.model.BaseModel;
import com.ruiyu.taozhuma.model.TzmActivityModel;
import com.ruiyu.taozhuma.utils.LogUtil;
import com.ruiyu.taozhuma.utils.ToastUtils;
import com.ruiyu.taozhuma.widget.GridViewForScrollView;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import de.greenrobot.event.EventBus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

	private IWXAPI api;
	@ViewInject(R.id.btn_head_left)
	private Button btn_head_left;
	@ViewInject(R.id.txt_head_title)
	private TextView txt_head_title;
	@ViewInject(R.id.btn_head_right)
	private Button btn_head_right;
	@ViewInject(R.id.gv_recommend)
	private GridViewForScrollView gv_recommend;
	@ViewInject(R.id.btn_order)
	private Button btn_order;
	// 接口调用
	private ApiClient client;
	private TzmActivityApi activityApi;
	private List<TzmActivityModel> tzmActivityModels;
	private TzmActivityAdapter tzmActivityAdapter;
	private xUtilsImageLoader imageLoader;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pay_result);
		api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
		api.handleIntent(getIntent(), this);
		ViewUtils.inject(this);
		EventBus.getDefault().post(new WeChatEven());
		initView();
		imageLoader = new xUtilsImageLoader(this);
		loadData();
	}

	private void initView() {
		txt_head_title.setText("支付结果");
		btn_head_right.setVisibility(View.VISIBLE);
		btn_head_right.setBackgroundDrawable(null);
		btn_head_right.setTextColor(getResources().getColor(R.color.base));
		btn_head_right.setText("返回首页");
		btn_head_left.setOnClickListener(clickListener);
		btn_head_right.setOnClickListener(clickListener);
		btn_order.setOnClickListener(clickListener);
		gv_recommend.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int positon,
					long id) {
				TzmActivityModel model = tzmActivityModels.get(positon);
				Intent intent = new Intent(WXPayEntryActivity.this,
						ProductDetailActivity.class);
				intent.putExtra("id", model.id);
				startActivity(intent);
			}
		});
	}

	OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_head_left:
				onBackPressed();
				finish();
				break;
			case R.id.btn_head_right:
				startActivity(new Intent(WXPayEntryActivity.this,
						MainTabActivity.class));
				finish();
				break;
			case R.id.btn_order:
				startActivity(new Intent(WXPayEntryActivity.this,
						TzmOrderListActivity.class));
				finish();
				break;
			default:
				break;
			}

		}
	};

	protected void loadData() {
		client = new ApiClient(this);
		tzmActivityModels = new ArrayList<TzmActivityModel>();
		activityApi = new TzmActivityApi();
		activityApi.setType(7);
		activityApi.setPageNo(1);
		client.api(this.activityApi, new ApiListener() {
			@Override
			public void onStart() {
				// ProgressDialogUtil.openProgressDialog(
				// NewFragment.this.getActivity(), "", "正在加载...");
			}

			@Override
			public void onComplete(String jsonStr) {
				// ProgressDialogUtil.closeProgressDialog();
				Gson gson = new Gson();
				Type type = new TypeToken<BaseModel<ArrayList<TzmActivityModel>>>() {
				}.getType();
				BaseModel<ArrayList<TzmActivityModel>> base = gson.fromJson(
						jsonStr, type);
				if (base.result != null && base.result.size() > 0) {
					if (base.result.size() <= 4) {
						tzmActivityModels = base.result;
					} else {
						tzmActivityModels.add(base.result.get(0));
						tzmActivityModels.add(base.result.get(1));
						tzmActivityModels.add(base.result.get(2));
						tzmActivityModels.add(base.result.get(3));
					}
					tzmActivityAdapter = new TzmActivityAdapter(
							WXPayEntryActivity.this, tzmActivityModels,
							imageLoader);
					gv_recommend.setAdapter(tzmActivityAdapter);
				}

			}

			@Override
			public void onError(String error) {
				LogUtil.e(error);
				// ToastUtils.showShortToast(TzmActivity.this,
				// R.string.msg_list_null);
				// ProgressDialogUtil.closeProgressDialog();
			}

			@Override
			public void onException(Exception e) {
				// ProgressDialogUtil.closeProgressDialog();
				LogUtil.ErrorLog(e);
			}
		}, true);

	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			ImageView iv_paystatus = (ImageView) findViewById(R.id.iv_paystatus);
			TextView tv_message = (TextView) findViewById(R.id.tv_message);
			//ToastUtils.showShortToast(getApplicationContext(), resp.errCode+"'");
			switch (resp.errCode) {
			case 0:
				iv_paystatus.setImageResource(R.drawable.pay_success);
				tv_message.setTextColor(getResources().getColor(
						R.color.text_pay_success));
				tv_message.setText("支付成功");
				break;
			case -1:
				iv_paystatus.setImageResource(R.drawable.pay_fail);
				tv_message.setTextColor(getResources().getColor(
						R.color.text_pay_fail));
				tv_message.setText("发起支付失败,请确认你手机中安装了微信或稍后再试");
				break;
			case -2:
				iv_paystatus.setImageResource(R.drawable.pay_fail);
				tv_message.setTextColor(getResources().getColor(
						R.color.text_pay_fail));
				tv_message.setText("支付取消");
				break;

			default:
				break;
			}
			// AlertDialog.Builder builder = new AlertDialog.Builder(this);
			// builder.setTitle("提示");
			// builder.setMessage("微信支付结果：%s," + resp.errStr + ";code="
			// + String.valueOf(resp.errCode));
			// builder.show();
		}
	}
}