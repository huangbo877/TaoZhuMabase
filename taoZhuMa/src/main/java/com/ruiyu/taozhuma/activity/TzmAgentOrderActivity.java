package com.ruiyu.taozhuma.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.adapter.TzmAgentOrderAdapter;
import com.ruiyu.taozhuma.api.ApiClient;
import com.ruiyu.taozhuma.api.ApiListener;
import com.ruiyu.taozhuma.api.TzmAgentOrderApi;
import com.ruiyu.taozhuma.base.xUtilsImageLoader;
import com.ruiyu.taozhuma.model.BaseModel;
import com.ruiyu.taozhuma.model.TzmAgentOrderModel;
import com.ruiyu.taozhuma.model.UserModel;
import com.ruiyu.taozhuma.utils.LogUtil;
import com.ruiyu.taozhuma.utils.ToastUtils;
import com.ruiyu.taozhuma.utils.UserInfoUtils;

public class TzmAgentOrderActivity extends Activity {
	private TextView txt_head_title;
	private Button btn_head_left;
	private ListView lv_order_list;
	private ArrayList<String> list;
	private TextView tv_content;

	private ApiClient apiClient;
	private TzmAgentOrderApi agentOrderApi;
	private ArrayList<TzmAgentOrderModel> agentOrderModels;
	private TzmAgentOrderAdapter tzmOrderListAdapter;

	private PullToRefreshListView mPullRefreshListView;
	private int currentPage = 1;// 当前页数
	private boolean isLoadMore = false;// 是否是加载更多
	private int listPosition = 0;
	private int mark = 0;

	// 用户信息
	private Boolean isLogin;
	private UserModel userModel;
	private xUtilsImageLoader imageLoader;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tzm_agent_order_activity);
		initView();

		imageLoader =new xUtilsImageLoader(this);
		apiClient = new ApiClient(this);
		agentOrderApi = new TzmAgentOrderApi();
		agentOrderModels = new ArrayList<TzmAgentOrderModel>();

		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.lv_order_list);
		mPullRefreshListView
				.setOnRefreshListener(new OnRefreshListener2<ListView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// TODO 下拉刷新
						String label = DateUtils.formatDateTime(
								TzmAgentOrderActivity.this
										.getApplicationContext(), System
										.currentTimeMillis(),
								DateUtils.FORMAT_SHOW_TIME
										| DateUtils.FORMAT_SHOW_DATE
										| DateUtils.FORMAT_ABBREV_ALL);

						// Update the LastUpdatedLabel
						refreshView.getLoadingLayoutProxy()
								.setLastUpdatedLabel(label);

						// TODO Auto-generated method stub
						isLoadMore = false;
						currentPage = 1;
						loadData(mark);
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// TODO Auto-generated method stub
						isLoadMore = true;
						currentPage++;
						loadData(mark);
					}
				});
		lv_order_list = mPullRefreshListView.getRefreshableView();

		loadData(mark);// 默认加载类型0,即全部
	}

	protected void loadData(int mark) {
		if (mark != 0) {
			agentOrderApi.setOrderStatus(mark);
		}
		agentOrderApi.setPageNo(currentPage);
		isLogin = UserInfoUtils.isLogin();
		if (isLogin) {
			userModel = UserInfoUtils.getUserInfo();
		}
		agentOrderApi.setUid(userModel.uid);
		apiClient.api(this.agentOrderApi, new ApiListener() {
			@Override
			public void onStart() {
			}

			@Override
			public void onComplete(String jsonStr) {
				Gson gson = new Gson();
				Type type = new TypeToken<BaseModel<ArrayList<TzmAgentOrderModel>>>() {
				}.getType();
				BaseModel<ArrayList<TzmAgentOrderModel>> base = gson.fromJson(
						jsonStr, type);
				if (isLoadMore) {
					// 加载更多
					listPosition = agentOrderModels.size();
					if (base.result != null && base.result.size() > 0) {
						agentOrderModels.addAll(base.result);
					} else {
						ToastUtils.showShortToast(TzmAgentOrderActivity.this,
								R.string.msg_list_null);
					}

				} else {
					// 刷新
					listPosition = 0;
					agentOrderModels.clear();
					if (base.result != null && base.result.size() > 0) {
						agentOrderModels = base.result;
						tv_content.setVisibility(View.GONE);
					} else {
						tv_content.setVisibility(View.VISIBLE);
					}
				}
				initOrderList();
			}

			@Override
			public void onError(String error) {
				mPullRefreshListView.onRefreshComplete();
//				ToastUtils.showShortToast(TzmAgentOrderActivity.this, error);
			}

			@Override
			public void onException(Exception e) {
				mPullRefreshListView.onRefreshComplete();
				LogUtil.ErrorLog(e);
			}
		}, true);

	}

	protected void initOrderList() {
		if (this.agentOrderModels != null) {
			tzmOrderListAdapter = new TzmAgentOrderAdapter(
					TzmAgentOrderActivity.this, this.agentOrderModels,imageLoader);
			lv_order_list.setAdapter(tzmOrderListAdapter);// 调用Adapter的getView()绘制item
			lv_order_list.setSelection(listPosition);
			mPullRefreshListView.onRefreshComplete();
		}
	}

	private void initView() {
		txt_head_title = (TextView) findViewById(R.id.txt_head_title);
		btn_head_left = (Button) findViewById(R.id.btn_head_left);
		btn_head_left.setOnClickListener(clickListener);
		tv_content = (TextView) findViewById(R.id.tv_content);
		txt_head_title.setText("订单列表");
		if (getIntent().getIntExtra("mark", 0) > 0) {
			mark = getIntent().getIntExtra("mark", 0);
			if (mark == 2) {
				txt_head_title.setText("待发货");
			} else if (mark == 3) {
				txt_head_title.setText("待收货");
			} else if (mark == 4) {
				txt_head_title.setText("待评价");
			}
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
		loadData(mark);
	};
	
}
