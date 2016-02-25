package com.ruiyu.taozhuma.activity;

import java.lang.reflect.Type;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.adapter.MineWalletAdapter;
import com.ruiyu.taozhuma.api.ApiClient;
import com.ruiyu.taozhuma.api.ApiListener;
import com.ruiyu.taozhuma.api.UserWelletDetailApi;
import com.ruiyu.taozhuma.model.BaseModel;
import com.ruiyu.taozhuma.model.UserModel;
import com.ruiyu.taozhuma.model.UserWelletDetailModel;
import com.ruiyu.taozhuma.model.UserWelletDetailModel.source;
import com.ruiyu.taozhuma.utils.LogUtil;
import com.ruiyu.taozhuma.utils.ProgressDialogUtil;
import com.ruiyu.taozhuma.utils.ToastUtils;
import com.ruiyu.taozhuma.utils.UserInfoUtils;
import com.ruiyu.taozhuma.widget.ListViewForScrollView;

public class MineWalletActivity extends Activity implements OnClickListener {
	@ViewInject(R.id.txt_head_title)
	private TextView txt_head_title;
	@ViewInject(R.id.btn_head_left)
	private Button btn_head_left;
	@ViewInject(R.id.rl_qbzq)
	private RelativeLayout rl_qbzq;
	@ViewInject(R.id.ptrf_sv)
	private PullToRefreshScrollView pullToRefreshScrollView;
	@ViewInject(R.id.tv_balance)
	private TextView tv_balance;
	@ViewInject(R.id.lv_banlce)
	private ListViewForScrollView listView;

	private ScrollView scrollView;

	private UserWelletDetailApi api;
	private MineWalletAdapter adapter;
	private UserWelletDetailModel model;
	private List<source> list;

	// 接口调用
	private ApiClient client;
	private int currentPage = 1;// 当前页数
	private boolean isLoadMore = false;// 是否是加载更多
	private Boolean isLogin;
	private UserModel userModel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mine_wallet_activity);
		ViewUtils.inject(this);
		initView();
		client = new ApiClient(this);
		api = new UserWelletDetailApi();
		model = new UserWelletDetailModel();

		pullToRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.ptrf_sv);

		pullToRefreshScrollView
				.setOnRefreshListener(new OnRefreshListener2<ScrollView>() {
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ScrollView> refreshView) {
						// String label = DateUtils.formatDateTime(
						// MineWalletActivity.this.getApplicationContext(),
						// System.currentTimeMillis(),
						// DateUtils.FORMAT_SHOW_TIME
						// | DateUtils.FORMAT_SHOW_DATE
						// | DateUtils.FORMAT_ABBREV_ALL);
						//
						// // Update the LastUpdatedLabel
						// refreshView.getLoadingLayoutProxy()
						// .setLastUpdatedLabel(label);
						isLoadMore = false;
						currentPage = 1;
						loadData();

					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ScrollView> refreshView) {
						isLoadMore = true;
						currentPage++;
						loadData();
					}
				});

		scrollView = pullToRefreshScrollView.getRefreshableView();
		listView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		listView.setFocusable(false);
		checkLogin();
		loadData();
	}

	private void initView() {
		txt_head_title.setText("我的钱包");
		btn_head_left.setOnClickListener(this);
		rl_qbzq.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_head_left:
			onBackPressed();
			break;
		case R.id.rl_qbzq:
			Intent intenwa = new Intent(MineWalletActivity.this,
					TzmWalletActivity.class);
			startActivity(intenwa);
			break;
		default:
			break;
		}
		
	}

	protected void loadData() {
		if (!isLogin) {
			ToastUtils.showShortToast(MineWalletActivity.this, "找不到用户资料");
			finish();
			return;
		}
		api.setUid(userModel.uid);
		api.setPageNo(currentPage);
		client.api(this.api, new ApiListener() {
			@Override
			public void onStart() {
				ProgressDialogUtil.openProgressDialog(MineWalletActivity.this,
						"", "");
			}

			@Override
			public void onComplete(String jsonStr) {
				ProgressDialogUtil.closeProgressDialog();
				Gson gson = new Gson();
				Type type = new TypeToken<BaseModel<UserWelletDetailModel>>() {
				}.getType();
				BaseModel<UserWelletDetailModel> base = gson.fromJson(jsonStr,
						type);

				if (isLoadMore) {
					// 加载更多
					if (base.result != null && base.result.detailList != null
							&& base.result.detailList.size() > 0) {
						list.addAll(base.result.detailList);
						// tzmShopDetailAdapter.setList(tzmShopDetailModel);
						adapter.notifyDataSetChanged();
					} else {
						ToastUtils.showShortToast(MineWalletActivity.this,
								R.string.msg_list_null);
					}

				} else {
					// 刷新
					if (base.result != null) {
						model = base.result;
						tv_balance.setText(model.balance+"");
						list = model.detailList;
						if (list != null && list.size() > 0) {
							adapter = new MineWalletAdapter(
									MineWalletActivity.this, list);
							listView.setAdapter(adapter);
						}

					} else {
						// 返回数据为空
						ToastUtils.showShortToast(MineWalletActivity.this,
								base.error_msg);
					}

				}
				pullToRefreshScrollView.onRefreshComplete();

			}

			@Override
			public void onError(String error) {
				pullToRefreshScrollView.onRefreshComplete();
				ToastUtils.showShortToast(MineWalletActivity.this,
						R.string.msg_list_null);
				ProgressDialogUtil.closeProgressDialog();
			}

			@Override
			public void onException(Exception e) {
				pullToRefreshScrollView.onRefreshComplete();
				ProgressDialogUtil.closeProgressDialog();
				LogUtil.ErrorLog(e);
			}
		}, true);
	}

	private void checkLogin() {
		isLogin = UserInfoUtils.isLogin();
		if (isLogin) {
			userModel = UserInfoUtils.getUserInfo();
		}

	}

}
