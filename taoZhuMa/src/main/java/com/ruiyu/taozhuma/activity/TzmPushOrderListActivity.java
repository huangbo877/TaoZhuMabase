package com.ruiyu.taozhuma.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.adapter.TzmOrderListAdapter;
import com.ruiyu.taozhuma.adapter.TzmPushOrderListAdapter;
import com.ruiyu.taozhuma.api.ApiClient;
import com.ruiyu.taozhuma.api.ApiListener;
import com.ruiyu.taozhuma.api.TzmCancelOrderApi;
import com.ruiyu.taozhuma.api.TzmMyordertApi;
import com.ruiyu.taozhuma.api.TzmPushOrdertListApi;
import com.ruiyu.taozhuma.model.BaseModel;
import com.ruiyu.taozhuma.model.TzmMyorderModel;
import com.ruiyu.taozhuma.model.TzmPushOrderModel;
import com.ruiyu.taozhuma.model.UserModel;
import com.ruiyu.taozhuma.utils.LogUtil;
import com.ruiyu.taozhuma.utils.StringUtils;
import com.ruiyu.taozhuma.utils.ToastUtils;
import com.ruiyu.taozhuma.utils.UserInfoUtils;

public class TzmPushOrderListActivity extends Activity {

	private TextView txt_head_title;
	private Button btn_head_left;
	private ListView lv_order_list;
	private ArrayList<String> list;
	private TextView tv_content;

	private ApiClient apiClient;
	private TzmPushOrdertListApi api;
	private ArrayList<TzmPushOrderModel> tzmPushOrderModels;
	private TzmPushOrderListAdapter adapter;

	// 使用PullToRefreshListView
	private PullToRefreshListView mPullRefreshListView;
	private int currentPage = 1;// 当前页数
	private boolean isLoadMore = false;// 是否是加载更多
	private int listPosition = 0;
	private int mark = 0;// 默认类型是0:全部订单,可以修改

	// 用户信息
	private Boolean isLogin;
	private UserModel userModel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tzm_order_list_activity);
		txt_head_title = (TextView) findViewById(R.id.txt_head_title);
		txt_head_title.setText("我的推送订单");
		btn_head_left = (Button) findViewById(R.id.btn_head_left);
		btn_head_left.setOnClickListener(clickListener);
		tv_content = (TextView) findViewById(R.id.tv_content);
		apiClient = new ApiClient(this);
		tzmPushOrderModels = new ArrayList<TzmPushOrderModel>();

		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.lv_order_list);
		mPullRefreshListView
				.setOnRefreshListener(new OnRefreshListener2<ListView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// TODO 下拉刷新
						String label = DateUtils.formatDateTime(
								TzmPushOrderListActivity.this
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
						loadData();
						System.out.println("mark=" + mark);
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// TODO Auto-generated method stub
						isLoadMore = true;
						currentPage++;
						loadData();
						System.out.println("mark=" + mark);
					}
				});
		lv_order_list = mPullRefreshListView.getRefreshableView();

	}

	private void loadData() {
		api = new TzmPushOrdertListApi();
		api.setPageNo(currentPage);
		isLogin = UserInfoUtils.isLogin();
		if (isLogin) {
			userModel = UserInfoUtils.getUserInfo();
		}
		// tzmMyordertApi.setUid(0);//测试用户
		api.setUid(userModel.uid);
		apiClient.api(this.api, new ApiListener() {
			@Override
			public void onStart() {
			}

			@Override
			public void onComplete(String jsonStr) {
				Gson gson = new Gson();
				Type type = new TypeToken<BaseModel<ArrayList<TzmPushOrderModel>>>() {
				}.getType();
				BaseModel<ArrayList<TzmPushOrderModel>> base = gson.fromJson(
						jsonStr, type);
				if (isLoadMore) {
					// 加载更多
					listPosition = tzmPushOrderModels.size();
					if (base.result != null && base.result.size() > 0) {
						tzmPushOrderModels.addAll(base.result);
					} else {
						ToastUtils.showShortToast(
								TzmPushOrderListActivity.this,
								R.string.msg_list_null);
					}

				} else {
					// 刷新
					listPosition = 0;
					tzmPushOrderModels.clear();
					if (base.result != null && base.result.size() > 0) {
						tzmPushOrderModels = base.result;
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
				ToastUtils.showShortToast(TzmPushOrderListActivity.this, error);
			}

			@Override
			public void onException(Exception e) {
				mPullRefreshListView.onRefreshComplete();
				LogUtil.ErrorLog(e);
			}
		}, true);

	}

	protected void initOrderList() {
		if (this.tzmPushOrderModels != null) {
			adapter = new TzmPushOrderListAdapter(
					TzmPushOrderListActivity.this, this.tzmPushOrderModels);
			lv_order_list.setAdapter(adapter);// 调用Adapter的getView()绘制item
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

	@Override
	protected void onResume() {
		super.onResume();
		loadData();
	};

	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		// LogUtil.e("MainTabActivity", " onNewIntent...");
		// handleIntent(intent);
	}

	protected void onRestart() {
		super.onRestart();
		// if(mark==4){
		// loadData(mark);
		// }if(mark==0){
		// loadData(mark);
		// }
		// tzmOrderListAdapter.notifyDataSetChanged();
		// loadData(mark);
	};

}
