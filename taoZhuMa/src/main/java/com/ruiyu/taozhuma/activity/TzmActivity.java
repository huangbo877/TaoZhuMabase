package com.ruiyu.taozhuma.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.adapter.TzmActivityAdapter;
import com.ruiyu.taozhuma.adapter.TzmActivityTopAdapter;
import com.ruiyu.taozhuma.api.ApiClient;
import com.ruiyu.taozhuma.api.ApiListener;
import com.ruiyu.taozhuma.api.TzmActivityApi;
import com.ruiyu.taozhuma.base.xUtilsImageLoader;
import com.ruiyu.taozhuma.model.BaseModel;
import com.ruiyu.taozhuma.model.TzmActivityModel;
import com.ruiyu.taozhuma.utils.LogUtil;
import com.ruiyu.taozhuma.utils.ToastUtils;

public class TzmActivity extends Activity {
    private String TAG="TzmActivity";
	private TextView txt_head_title;
	private Button btn_head_left;
	private TextView tv_content;
	private GridView GridView;
	private int type;
	// 列表
	private PullToRefreshGridView mPullRefreshGridView;
	private TzmActivityAdapter tzmActivityAdapter;
	private TzmActivityTopAdapter tzmActivityTopAdapter;
	// 接口调用
	private ApiClient client;
	private TzmActivityApi activityApi;
	private List<TzmActivityModel> tzmActivityModels;
	private int currentPage = 1;// 当前页数
	private boolean isLoadMore = false;// 是否是加载更多


	private xUtilsImageLoader imageLoader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tzm_hot_product_activity);
		LogUtil.Log(TAG, "onCreate");
		type = getIntent().getIntExtra("type", 0);
		txt_head_title = (TextView) findViewById(R.id.txt_head_title);
		btn_head_left = (Button) findViewById(R.id.btn_head_left);
		btn_head_left.setOnClickListener(clickListener);

		tv_content = (TextView) findViewById(R.id.tv_content);

		switch (type) {
		case 1:
			txt_head_title.setText("每日新品");
			break;
		case 2:
			txt_head_title.setText("热销产品");
			break;
		case 3:
			txt_head_title.setText("竹马推荐");
			break;
		case 4:
			txt_head_title.setText("新品快订");
			break;
		case 5:
			txt_head_title.setText("钱包专区");
			break;
		case 7:
			txt_head_title.setText("TOP排行");
			break;
		}

		client = new ApiClient(this);
		activityApi = new TzmActivityApi();
		tzmActivityModels = new ArrayList<TzmActivityModel>();

		imageLoader = new xUtilsImageLoader(this);

		mPullRefreshGridView = (PullToRefreshGridView) findViewById(R.id.prl_hot_product_list);
		mPullRefreshGridView
				.setOnRefreshListener(new OnRefreshListener2<GridView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<GridView> refreshView) {
						// TODO 下拉刷新
						String label = DateUtils.formatDateTime(
								TzmActivity.this.getApplicationContext(),
								System.currentTimeMillis(),
								DateUtils.FORMAT_SHOW_TIME
										| DateUtils.FORMAT_SHOW_DATE
										| DateUtils.FORMAT_ABBREV_ALL);

						// Update the LastUpdatedLabel
						refreshView.getLoadingLayoutProxy()
								.setLastUpdatedLabel(label);

						// TODO Auto-generated method stub
						isLoadMore = false;
						currentPage = 1;
						loadData(type);
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<GridView> refreshView) {
						// TODO Auto-generated method stub
						isLoadMore = true;
						currentPage++;
						loadData(type);

					}
				});
		GridView = mPullRefreshGridView.getRefreshableView();
		GridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		GridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int positon,
					long id) {
				TzmActivityModel model = tzmActivityModels.get(positon);
				Intent intent = new Intent(TzmActivity.this,
						ProductDetailActivity.class);
				intent.putExtra("id", model.id);
				intent.putExtra("activityId", model.activityId);
				LogUtil.Log(TAG, "onCreate----onItemClick"+"活动ID:"+model.activityId);
				startActivity(intent);
			}
		});
		loadData(type);
	}


	protected void loadData(final int atype) {
		activityApi.setType(atype);
		activityApi.setPageNo(currentPage);
		client.api(this.activityApi, new ApiListener() {
			@Override
			public void onStart() {
				// ProgressDialogUtil.openProgressDialog(
				// NewFragment.this.getActivity(), "", "正在加载...");
			}

			@Override
			public void onComplete(String jsonStr) {
				// ProgressDialogUtil.closeProgressDialog();
				mPullRefreshGridView.onRefreshComplete();
				Gson gson = new Gson();
				Type type = new TypeToken<BaseModel<ArrayList<TzmActivityModel>>>() {
				}.getType();
				BaseModel<ArrayList<TzmActivityModel>> base = gson.fromJson(
						jsonStr, type);

				if (isLoadMore) {
					// 加载更多
					if (base.result != null && base.result.size() > 0) {
						tzmActivityModels.addAll(base.result);
						if (atype != 7) {
							tzmActivityAdapter.notifyDataSetChanged();
						} else {
							tzmActivityTopAdapter.notifyDataSetChanged();
						}
					} else {
						ToastUtils.showShortToast(TzmActivity.this,
								R.string.msg_list_null);
					}

				} else {
					// 刷新
					tzmActivityModels.clear();
					if (base.result != null && base.result.size() > 0) {
						tzmActivityModels = base.result;
						tv_content.setVisibility(View.GONE);
					} else {
						// 返回数据为空
						tv_content.setVisibility(View.VISIBLE);
						ToastUtils.showShortToast(TzmActivity.this,
								R.string.msg_list_null);
					}
					initProductList();
				}

			}

			@Override
			public void onError(String error) {
				mPullRefreshGridView.onRefreshComplete();
				ToastUtils.showShortToast(TzmActivity.this,
						R.string.msg_list_null);
				tv_content.setVisibility(View.VISIBLE);
				// ProgressDialogUtil.closeProgressDialog();
			}

			@Override
			public void onException(Exception e) {
				mPullRefreshGridView.onRefreshComplete();
				tv_content.setVisibility(View.VISIBLE);
				// ProgressDialogUtil.closeProgressDialog();
				LogUtil.ErrorLog(e);
			}
		}, true);

	}

	protected void initProductList() {
		if (this.tzmActivityModels != null) {
			// LogUtil.Log("Size:" + this.TzmHotProductModels.size() +
			// "listPosition:"
			// + listPosition);
			// if (productGridViewAdapter == null) {
			if (type != 7) {
				tzmActivityAdapter = new TzmActivityAdapter(TzmActivity.this,
						this.tzmActivityModels, imageLoader);
				GridView.setAdapter(tzmActivityAdapter);
			} else {
				tzmActivityTopAdapter = new TzmActivityTopAdapter(
						TzmActivity.this, this.tzmActivityModels, imageLoader);
				GridView.setAdapter(tzmActivityTopAdapter);
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
}
