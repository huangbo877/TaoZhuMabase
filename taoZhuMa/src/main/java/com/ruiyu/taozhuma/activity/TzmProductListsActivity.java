package com.ruiyu.taozhuma.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.adapter.TzmProductListsAdapter;
import com.ruiyu.taozhuma.api.ApiClient;
import com.ruiyu.taozhuma.api.ApiListener;
import com.ruiyu.taozhuma.api.TzmProductListsApi;
import com.ruiyu.taozhuma.model.BaseModel;
import com.ruiyu.taozhuma.model.TzmProductListsModel;
import com.ruiyu.taozhuma.model.UserModel;
import com.ruiyu.taozhuma.utils.LogUtil;
import com.ruiyu.taozhuma.utils.ProgressDialogUtil;
import com.ruiyu.taozhuma.utils.ToastUtils;
import com.ruiyu.taozhuma.utils.UserInfoUtils;

public class TzmProductListsActivity extends Activity{
	private TextView txt_head_title;
	private Button btn_head_left;
	private GridView gridView;
	
	private PullToRefreshGridView mPullRefreshGridView;
	private TzmProductListsAdapter productListsAdapter;
	
	private ApiClient client;
	private TzmProductListsApi api;
	private List<TzmProductListsModel> list;
	private int currentPage = 1;// 当前页数
	private boolean isLoadMore = false;// 是否是加载更多
	private int listPosition = 0;
	
	private Boolean isLogin;
	private UserModel userModel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tzm_productlists_activity);
		initView();
		checkLogin();
		client=new ApiClient(this);
		api=new TzmProductListsApi();
		list=new ArrayList<TzmProductListsModel>();
		mPullRefreshGridView= (PullToRefreshGridView) findViewById(R.id.prl_productlists_list);
		mPullRefreshGridView
		.setOnRefreshListener(new OnRefreshListener2<GridView>() {
			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<GridView> refreshView) {
				String label = DateUtils.formatDateTime(
						TzmProductListsActivity.this
								.getApplicationContext(), System
								.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME
								| DateUtils.FORMAT_SHOW_DATE
								| DateUtils.FORMAT_ABBREV_ALL);

				// Update the LastUpdatedLabel
				refreshView.getLoadingLayoutProxy()
						.setLastUpdatedLabel(label);
				isLoadMore = false;
				currentPage = 1;
				loadData();

			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<GridView> refreshView) {
				isLoadMore = true;
				currentPage++;
				loadData();
			}
		});
		gridView = mPullRefreshGridView.getRefreshableView();
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
//		gridView.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View v, int positon,
//					long id) {
//				TzmProductListsModel model = list.get(positon);
//				Intent intent = new Intent(TzmProductListsActivity.this,
//						TzmProductDetailActivity.class);
//				intent.putExtra("id", model.id);
//				startActivity(intent);
//			}
//		});
		loadData();
	}
	
	protected void loadData() {
		api.setPageNo(currentPage);
		api.setUid(userModel.uid);
		client.api(api, new ApiListener() {
			
			@Override
			public void onStart() {
				ProgressDialogUtil.openProgressDialog(
						TzmProductListsActivity.this, "", "");
			}
			@Override
			public void onError(String error) {
				mPullRefreshGridView.onRefreshComplete();
				ToastUtils.showShortToast(TzmProductListsActivity.this,
						R.string.msg_list_null);
				ProgressDialogUtil.closeProgressDialog();
			}
			public void onException(Exception e) {
				mPullRefreshGridView.onRefreshComplete();
				ProgressDialogUtil.closeProgressDialog();
				LogUtil.ErrorLog(e);
			};
			@Override
			public void onComplete(String jsonStr) {
				ProgressDialogUtil.closeProgressDialog();
				Gson gson = new Gson();
				Type type = new TypeToken<BaseModel<ArrayList<TzmProductListsModel>>>() {
				}.getType();
				BaseModel<ArrayList<TzmProductListsModel>> base = gson.fromJson(
						jsonStr, type);

				if (isLoadMore) {
					// 加载更多
					listPosition = gridView.getCount();
					if (base.result != null && base.result.size() > 0) {
						list.addAll(base.result);
					} else {
						ToastUtils.showShortToast(TzmProductListsActivity.this,
								R.string.msg_list_null);
					}

				} else {
					// 刷新
					listPosition = 0;
					list.clear();
					if (base.result != null && base.result.size() > 0) {
						list = base.result;
					} else {
						// 返回数据为空
						ToastUtils.showShortToast(TzmProductListsActivity.this,
								R.string.msg_list_null);
					}
				}

				initProductList();
			}
		}, true);
	}

	protected void initProductList() {
		productListsAdapter = new TzmProductListsAdapter(TzmProductListsActivity.this,
				list);
		gridView.setAdapter(productListsAdapter);
		gridView.setSelection(listPosition);
		mPullRefreshGridView.onRefreshComplete();
	}

	private void initView() {
		txt_head_title = (TextView) findViewById(R.id.txt_head_title);
		txt_head_title.setText("产品管理");
		btn_head_left = (Button) findViewById(R.id.btn_head_left);
		btn_head_left.setOnClickListener(clickListener);
	}

	View.OnClickListener clickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_head_left:
				onBackPressed();
				break;

			}
			
		}
	};
	private void checkLogin() {
		isLogin = UserInfoUtils.isLogin();
		if (isLogin) {
			userModel = UserInfoUtils.getUserInfo();
		}
	}
}
