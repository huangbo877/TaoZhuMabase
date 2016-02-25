package com.ruiyu.taozhuma.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.adapter.SelectShopAdapter;
import com.ruiyu.taozhuma.adapter.TzmShopListAdapter;
import com.ruiyu.taozhuma.api.ApiClient;
import com.ruiyu.taozhuma.api.ApiListener;
import com.ruiyu.taozhuma.api.TzmShopListApi;
import com.ruiyu.taozhuma.model.BaseModel;
import com.ruiyu.taozhuma.model.TzmShopListModel;
import com.ruiyu.taozhuma.utils.LogUtil;
import com.ruiyu.taozhuma.utils.ProgressDialogUtil;
import com.ruiyu.taozhuma.utils.ToastUtils;

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

public class SelectShopActivity extends Activity {
	private TextView txt_head_title;
	private Button btn_head_left;
	private GridView GridView;

	// 列表
	private PullToRefreshGridView mPullRefreshGridView;
	private SelectShopAdapter selectShopAdapter;
	// 接口调用
	private ApiClient client;
	private TzmShopListApi tzmShopListApi;
	private List<TzmShopListModel> tzmShopListModels;
	private int currentPage = 1;// 当前页数
	private boolean isLoadMore = false;// 是否是加载更多
	private int listPosition = 0;

	Intent intent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_shop_activity);
		initView();
		intent=getIntent();
		client = new ApiClient(this);
		tzmShopListApi = new TzmShopListApi();
		tzmShopListModels = new ArrayList<TzmShopListModel>();

		mPullRefreshGridView = (PullToRefreshGridView) findViewById(R.id.prl_shop_list);
		mPullRefreshGridView
				.setOnRefreshListener(new OnRefreshListener2<GridView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<GridView> refreshView) {
						// TODO 下拉刷新
						String label = DateUtils.formatDateTime(
								SelectShopActivity.this.getApplicationContext(),
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
				intent.putExtra("id", model.id);
				intent.putExtra("shopName", model.name);
				setResult(Activity.RESULT_OK, intent);
				ToastUtils.showShortToast(SelectShopActivity.this, "已选择");
				finish();
			}
		});
		loadData();
	}

	protected void loadData() {
		tzmShopListApi.setType(0);
		tzmShopListApi.setPageNo(currentPage);
		client.api(this.tzmShopListApi, new ApiListener() {
			@Override
			public void onStart() {
				ProgressDialogUtil.openProgressDialog(SelectShopActivity.this,
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
						ToastUtils.showShortToast(SelectShopActivity.this,
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
						ToastUtils.showShortToast(SelectShopActivity.this,
								R.string.msg_list_null);
					}
				}

				initProductList();
			}

			@Override
			public void onError(String error) {
				mPullRefreshGridView.onRefreshComplete();
				ToastUtils.showShortToast(SelectShopActivity.this,
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
			selectShopAdapter = new SelectShopAdapter(
					SelectShopActivity.this, this.tzmShopListModels);
			GridView.setAdapter(selectShopAdapter);
			GridView.setSelection(listPosition);
			mPullRefreshGridView.onRefreshComplete();
		}
	}

	private void initView() {
		txt_head_title = (TextView) findViewById(R.id.txt_head_title);
		txt_head_title.setText("代理品牌");
		btn_head_left = (Button) findViewById(R.id.btn_head_left);
		btn_head_left.setOnClickListener(clickListener);

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
