package com.ruiyu.taozhuma.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.adapter.TzmMyRefundAdapter;
import com.ruiyu.taozhuma.api.ApiClient;
import com.ruiyu.taozhuma.api.ApiListener;
import com.ruiyu.taozhuma.api.TzmMyRefundApi;
import com.ruiyu.taozhuma.base.xUtilsImageLoader;
import com.ruiyu.taozhuma.model.BaseModel;
import com.ruiyu.taozhuma.model.TzmMyRefundModel;
import com.ruiyu.taozhuma.model.UserModel;
import com.ruiyu.taozhuma.utils.LogUtil;
import com.ruiyu.taozhuma.utils.ProgressDialogUtil;
import com.ruiyu.taozhuma.utils.ToastUtils;
import com.ruiyu.taozhuma.utils.UserInfoUtils;

public class TzmMyRefundActivity extends Activity {

	private TextView txt_head_title;
	private Button btn_head_left;
	private ListView lv_order_list;

	private ApiClient apiClient;
	private TzmMyRefundApi tzmMyRefundApi;
	private ArrayList<TzmMyRefundModel> tzmMyRefundModels;
	private TzmMyRefundAdapter tzmMyRefundAdapter;

	private PullToRefreshListView pullToRefreshListView;
	private int currentPage = 1;
	private boolean isLoadMore = false;

	private Boolean isLogin;
	private UserModel userModel;
	private Button btn_gotoAllOrders;
	private xUtilsImageLoader imageLoader;

	private RelativeLayout rl_empty;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tzm_my_refund_activity);
		imageLoader = new xUtilsImageLoader(this);
		initView();
	}

	private void initView() {
		txt_head_title = (TextView) findViewById(R.id.txt_head_title);
		btn_head_left = (Button) findViewById(R.id.btn_head_left);
		btn_gotoAllOrders = (Button) findViewById(R.id.btn_gotoAllOrders);
		txt_head_title.setText("退款维权");
		btn_head_left.setOnClickListener(clickListener);
		btn_gotoAllOrders.setOnClickListener(clickListener);
		rl_empty = (RelativeLayout) findViewById(R.id.rl_empty);
		tzmMyRefundApi = new TzmMyRefundApi();
		apiClient = new ApiClient(this);
		tzmMyRefundModels = new ArrayList<TzmMyRefundModel>();
		pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.lv_refund_list);
		pullToRefreshListView
				.setOnRefreshListener(new OnRefreshListener2<ListView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						String label = DateUtils.formatDateTime(
								TzmMyRefundActivity.this
										.getApplicationContext(), System
										.currentTimeMillis(),
								DateUtils.FORMAT_SHOW_TIME
										| DateUtils.FORMAT_SHOW_DATE
										| DateUtils.FORMAT_ABBREV_ALL);

						refreshView.getLoadingLayoutProxy()
								.setLastUpdatedLabel(label);

						isLoadMore = false;
						currentPage = 1;
						loadData();
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						isLoadMore = true;
						currentPage++;
						loadData();
					}

				});
		lv_order_list = pullToRefreshListView.getRefreshableView();
		loadData();

	}

	protected void loadData() {
		tzmMyRefundApi.setPageNo(currentPage);
		isLogin = UserInfoUtils.isLogin();
		if (isLogin) {
			userModel = UserInfoUtils.getUserInfo();
		}
		tzmMyRefundApi.setUid(userModel.uid);
		apiClient.api(tzmMyRefundApi, new ApiListener() {

			@Override
			public void onStart() {
				ProgressDialogUtil.openProgressDialog(TzmMyRefundActivity.this,
						"", "正在加载...");
			}

			@Override
			public void onError(String error) {
				ProgressDialogUtil.closeProgressDialog();
				pullToRefreshListView.onRefreshComplete();
				ToastUtils.showShortToast(TzmMyRefundActivity.this,
						R.string.msg_list_null);
			}

			@Override
			public void onException(Exception e) {
				ProgressDialogUtil.closeProgressDialog();
				pullToRefreshListView.onRefreshComplete();
				LogUtil.ErrorLog(e);
			}

			@Override
			public void onComplete(String jsonStr) {
				ProgressDialogUtil.closeProgressDialog();
				Gson gson = new Gson();
				Type type = new TypeToken<BaseModel<ArrayList<TzmMyRefundModel>>>() {
				}.getType();
				BaseModel<ArrayList<TzmMyRefundModel>> base = gson.fromJson(
						jsonStr, type);
				if (isLoadMore) {
					// 加载更多
					// listPosition = tzmMyRefundModels.size();
					if (base.result != null && base.result.size() > 0) {
						tzmMyRefundModels.addAll(base.result);
						tzmMyRefundAdapter.notifyDataSetChanged();
					} else {

						ToastUtils.showShortToast(TzmMyRefundActivity.this,
								R.string.msg_list_null);
					}
					pullToRefreshListView.onRefreshComplete();

				} else {
					// 刷新
					// listPosition = 0;
					tzmMyRefundModels.clear();
					if (base.result != null && base.result.size() > 0) {
						tzmMyRefundModels = base.result;
						rl_empty.setVisibility(View.GONE);
						pullToRefreshListView.setVisibility(View.VISIBLE);
						initList();
					} else {
						rl_empty.setVisibility(View.VISIBLE);
						pullToRefreshListView.setVisibility(View.GONE);
						// ToastUtils.showShortToast(TzmMyRefundActivity.this,
						// R.string.msg_list_null);
					}

				}

			}
		}, true);
	}

	protected void initList() {
		if (this.tzmMyRefundModels != null) {
			tzmMyRefundAdapter = new TzmMyRefundAdapter(
					TzmMyRefundActivity.this, this.tzmMyRefundModels,
					imageLoader);
			lv_order_list.setAdapter(tzmMyRefundAdapter);// 调用Adapter的getView()绘制item
			// lv_order_list.setSelection(listPosition);
			pullToRefreshListView.onRefreshComplete();
		}
	}

	View.OnClickListener clickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_head_left:
				onBackPressed();
				break;
			case R.id.btn_gotoAllOrders:
				Intent intent_product = new Intent(TzmMyRefundActivity.this,
						TzmOrderListActivity.class);
				startActivity(intent_product);
				break;

			}

		}
	};

	protected void onRestart() {
		super.onRestart();
		// isLoadMore = false;
		// currentPage = 1;
		// loadData();
	};
}
