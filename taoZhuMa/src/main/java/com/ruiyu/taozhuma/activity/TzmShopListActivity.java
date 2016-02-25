package com.ruiyu.taozhuma.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.adapter.TzmShopListAdapter;
import com.ruiyu.taozhuma.api.ApiClient;
import com.ruiyu.taozhuma.api.ApiListener;
import com.ruiyu.taozhuma.api.TzmShopListApi;
import com.ruiyu.taozhuma.base.xUtilsImageLoader;
import com.ruiyu.taozhuma.model.BaseModel;
import com.ruiyu.taozhuma.model.TzmShopListModel;
import com.ruiyu.taozhuma.utils.LogUtil;
import com.ruiyu.taozhuma.utils.ProgressDialogUtil;
import com.ruiyu.taozhuma.utils.ToastUtils;

public class TzmShopListActivity extends Activity {

	private TextView txt_head_title;
	private Button btn_head_left;
	private GridView GridView;

	// 列表
	private PullToRefreshGridView mPullRefreshGridView;
	private TzmShopListAdapter tzmShopListAdapter;
	// 接口调用
	private ApiClient client;
	private TzmShopListApi tzmShopListApi;
	private List<TzmShopListModel> tzmShopListModels;
	private int currentPage = 1;// 当前页数
	private boolean isLoadMore = false;// 是否是加载更多
	private int listPosition = 0;
	//
	private int type;
	private TextView tv_type0, tv_type1, tv_type2, tv_type3, tv_type4,
			tv_type5, tv_type6;
	
	private xUtilsImageLoader imageLoader;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tzm_shop_list_activity);
		initView();
		client = new ApiClient(this);
		tzmShopListApi = new TzmShopListApi();
		tzmShopListModels = new ArrayList<TzmShopListModel>();

		imageLoader = new xUtilsImageLoader(this);
		
		mPullRefreshGridView = (PullToRefreshGridView) findViewById(R.id.prl_shop_list);
		mPullRefreshGridView
				.setOnRefreshListener(new OnRefreshListener2<GridView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<GridView> refreshView) {
						// TODO 下拉刷新
						String label = DateUtils.formatDateTime(
								TzmShopListActivity.this
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
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<GridView> refreshView) {
						// TODO Auto-generated method stub
						isLoadMore = true;
						currentPage++;
						loadData();

					}
				});
		GridView = mPullRefreshGridView.getRefreshableView();
		GridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		GridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int positon, long id) {
				TzmShopListModel model = tzmShopListModels.get(positon);
				Intent intent = new Intent(TzmShopListActivity.this,
						TzmShopDetailActivity.class);
				intent.putExtra("id", model.id);
				intent.putExtra("shopName", model.name);
				startActivity(intent);
			}
		});
		loadData();
	}

	private void initView() {
		txt_head_title = (TextView) findViewById(R.id.txt_head_title);
		txt_head_title.setText("品牌街");
		btn_head_left = (Button) findViewById(R.id.btn_head_left);
		btn_head_left.setOnClickListener(clickListener);
		
		tv_type0 = (TextView) findViewById(R.id.tv_type0);
		tv_type0.setOnClickListener(clickListener);
		tv_type1 = (TextView) findViewById(R.id.tv_type1);
		tv_type1.setOnClickListener(clickListener);
		tv_type2 = (TextView) findViewById(R.id.tv_type2);
		tv_type2.setOnClickListener(clickListener);
		tv_type3 = (TextView) findViewById(R.id.tv_type3);
		tv_type3.setOnClickListener(clickListener);
		tv_type4 = (TextView) findViewById(R.id.tv_type4);
		tv_type4.setOnClickListener(clickListener);
		tv_type5 = (TextView) findViewById(R.id.tv_type5);
		tv_type5.setOnClickListener(clickListener);
		tv_type6 = (TextView) findViewById(R.id.tv_type6);
		tv_type6.setOnClickListener(clickListener);

		
	}
	
	protected void loadData() {
		tzmShopListApi.setType(type);
		tzmShopListApi.setPageNo(currentPage);
		client.api(this.tzmShopListApi, new ApiListener() {
			@Override
			public void onStart() {
				ProgressDialogUtil.openProgressDialog(TzmShopListActivity.this,
						"", "");
			}

			@Override
			public void onComplete(String jsonStr) {
				ProgressDialogUtil.closeProgressDialog();
				Gson gson = new Gson();
				Type type = new TypeToken<BaseModel<ArrayList<TzmShopListModel>>>() {
				}.getType();
				BaseModel<ArrayList<TzmShopListModel>> base = gson.fromJson(
						jsonStr, type);

				if (isLoadMore) {
					// 加载更多
					listPosition = GridView.getCount();
					if (base.result != null && base.result.size() > 0) {
						tzmShopListModels.addAll(base.result);
					} else {
						ToastUtils.showShortToast(TzmShopListActivity.this,
								R.string.msg_list_null);
					}

				} else {
					// 刷新
					listPosition = 0;
					tzmShopListModels.clear();
					if (base.result != null && base.result.size() > 0) {
						tzmShopListModels = base.result;
					} else {
						// 返回数据为空
						ToastUtils.showShortToast(TzmShopListActivity.this,
								R.string.msg_list_null);
					}
				}

				initProductList();
			}

			@Override
			public void onError(String error) {
				mPullRefreshGridView.onRefreshComplete();
				ToastUtils.showShortToast(TzmShopListActivity.this,
						R.string.msg_list_null);
				ProgressDialogUtil.closeProgressDialog();
			}

			@Override
			public void onException(Exception e) {
				mPullRefreshGridView.onRefreshComplete();
				ProgressDialogUtil.closeProgressDialog();
				LogUtil.ErrorLog(e);
			}
		}, true);

	}

	protected void initProductList() {
		if (this.tzmShopListModels != null) {
			tzmShopListAdapter = new TzmShopListAdapter(
					TzmShopListActivity.this, this.tzmShopListModels,imageLoader);
			GridView.setAdapter(tzmShopListAdapter);
			GridView.setSelection(listPosition);
			mPullRefreshGridView.onRefreshComplete();
		}
	}

	OnClickListener clickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_head_left:
				onBackPressed();
				break;
			case R.id.tv_type0:
				handler.sendEmptyMessage(0);
				break;
			case R.id.tv_type1:
				handler.sendEmptyMessage(1);
				break;
			case R.id.tv_type2:
				handler.sendEmptyMessage(2);
				break;
			case R.id.tv_type3:
				handler.sendEmptyMessage(3);
				break;
			case R.id.tv_type4:
				handler.sendEmptyMessage(4);
				break;
			case R.id.tv_type5:
				handler.sendEmptyMessage(5);
				break;
			case R.id.tv_type6:
				handler.sendEmptyMessage(6);
				break;
			}
		}
	};

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				type = 0;
				tv_type0.setTextColor(getResources().getColor(
						R.color.common_head_background));
				tv_type1.setTextColor(getResources().getColor(R.color.black));
				tv_type2.setTextColor(getResources().getColor(R.color.black));
				tv_type3.setTextColor(getResources().getColor(R.color.black));
				tv_type4.setTextColor(getResources().getColor(R.color.black));
				tv_type5.setTextColor(getResources().getColor(R.color.black));
				tv_type6.setTextColor(getResources().getColor(R.color.black));
				isLoadMore = false;
				currentPage = 1;
				loadData();
				break;
			case 1:
				type = 1;
				tv_type0.setTextColor(getResources().getColor(R.color.black));
				tv_type1.setTextColor(getResources().getColor(
						R.color.common_head_background));
				tv_type2.setTextColor(getResources().getColor(R.color.black));
				tv_type3.setTextColor(getResources().getColor(R.color.black));
				tv_type4.setTextColor(getResources().getColor(R.color.black));
				tv_type5.setTextColor(getResources().getColor(R.color.black));
				tv_type6.setTextColor(getResources().getColor(R.color.black));
				isLoadMore = false;
				currentPage = 1;
				loadData();
				break;
			case 2:
				type = 2;
				tv_type0.setTextColor(getResources().getColor(R.color.black));
				tv_type1.setTextColor(getResources().getColor(R.color.black));
				tv_type2.setTextColor(getResources().getColor(
						R.color.common_head_background));
				tv_type3.setTextColor(getResources().getColor(R.color.black));
				tv_type4.setTextColor(getResources().getColor(R.color.black));
				tv_type5.setTextColor(getResources().getColor(R.color.black));
				tv_type6.setTextColor(getResources().getColor(R.color.black));
				isLoadMore = false;
				currentPage = 1;
				loadData();
				break;
			case 3:
				type = 3;
				tv_type0.setTextColor(getResources().getColor(R.color.black));
				tv_type1.setTextColor(getResources().getColor(R.color.black));
				tv_type2.setTextColor(getResources().getColor(R.color.black));
				tv_type3.setTextColor(getResources().getColor(
						R.color.common_head_background));
				tv_type4.setTextColor(getResources().getColor(R.color.black));
				tv_type5.setTextColor(getResources().getColor(R.color.black));
				tv_type6.setTextColor(getResources().getColor(R.color.black));
				isLoadMore = false;
				currentPage = 1;
				loadData();
				break;
			case 4:
				type = 4;
				tv_type0.setTextColor(getResources().getColor(R.color.black));
				tv_type1.setTextColor(getResources().getColor(R.color.black));
				tv_type2.setTextColor(getResources().getColor(R.color.black));
				tv_type3.setTextColor(getResources().getColor(R.color.black));
				tv_type4.setTextColor(getResources().getColor(
						R.color.common_head_background));
				tv_type5.setTextColor(getResources().getColor(R.color.black));
				tv_type6.setTextColor(getResources().getColor(R.color.black));
				isLoadMore = false;
				currentPage = 1;
				loadData();
				break;
			case 5:
				type = 5;
				tv_type0.setTextColor(getResources().getColor(R.color.black));
				tv_type1.setTextColor(getResources().getColor(R.color.black));
				tv_type2.setTextColor(getResources().getColor(R.color.black));
				tv_type3.setTextColor(getResources().getColor(R.color.black));
				tv_type4.setTextColor(getResources().getColor(R.color.black));
				tv_type5.setTextColor(getResources().getColor(
						R.color.common_head_background));
				tv_type6.setTextColor(getResources().getColor(R.color.black));
				isLoadMore = false;
				currentPage = 1;
				loadData();
				break;
			case 6:
				type = 6;
				tv_type0.setTextColor(getResources().getColor(R.color.black));
				tv_type1.setTextColor(getResources().getColor(R.color.black));
				tv_type2.setTextColor(getResources().getColor(R.color.black));
				tv_type3.setTextColor(getResources().getColor(R.color.black));
				tv_type4.setTextColor(getResources().getColor(R.color.black));
				tv_type5.setTextColor(getResources().getColor(R.color.black));
				tv_type6.setTextColor(getResources().getColor(
						R.color.common_head_background));
				isLoadMore = false;
				currentPage = 1;
				loadData();
				break;
			}
		};
	};

	// @Override
	// protected void onStart() {
	// super.onStart();
	// ToastUtils.showShortToast(TzmShopListActivity.this, "onStart");
	// };
	//
	// @Override
	// protected void onRestart() {
	// super.onRestart();
	// ToastUtils.showShortToast(TzmShopListActivity.this, "onRestart");
	// }
	//
	// @Override
	// protected void onResume() {
	// // TODO Auto-generated method stub
	// super.onResume();
	// ToastUtils.showShortToast(TzmShopListActivity.this, "onResume");
	// }
	//
	// @Override
	// protected void onStop() {
	// super.onStop();
	// ToastUtils.showShortToast(TzmShopListActivity.this, "onStop");
	// }
	//
	// @Override
	// protected void onDestroy() {
	// // TODO Auto-generated method stub
	// super.onDestroy();
	// ToastUtils.showShortToast(TzmShopListActivity.this, "onDestroy");
	// }

}
