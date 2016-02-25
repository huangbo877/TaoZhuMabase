package com.ruiyu.taozhuma.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.api.ApiClient;
import com.ruiyu.taozhuma.api.ApiListener;
import com.ruiyu.taozhuma.api.TzmInfoListApi;
import com.ruiyu.taozhuma.model.BaseModel;
import com.ruiyu.taozhuma.model.TzmInfoListModel;
import com.ruiyu.taozhuma.utils.LogUtil;
import com.ruiyu.taozhuma.utils.ToastUtils;

/**
 * 行业资讯列表
 * 
 * @author Eve
 * 
 */
public class TzmInfoLlistActivity extends Activity {

	private TextView txt_head_title;
	private Button btn_head_left;
	private ListView lv_info_list;

	private ArrayList<TzmInfoListModel> tzmInfoListModels;
	private ApiClient client;
	private TzmInfoListApi tzmInfoListApi;

	// PullToRefreshListView使用
	private PullToRefreshListView mPullRefreshListView;
	private int currentPage = 1;// 当前页数
	private boolean isLoadMore = false;// 是否是加载更多
	private int listPosition = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.tzm_info_list_activity);
		txt_head_title = (TextView) findViewById(R.id.txt_head_title);
		txt_head_title.setText("行业资讯");
		btn_head_left = (Button) findViewById(R.id.btn_head_left);
		btn_head_left.setOnClickListener(clickListener);

		client = new ApiClient(this);
		tzmInfoListApi = new TzmInfoListApi();
		tzmInfoListModels = new ArrayList<TzmInfoListModel>();

		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.lv_info_list);
		mPullRefreshListView
				.setOnRefreshListener(new OnRefreshListener2<ListView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// TODO 下拉刷新
						String label = DateUtils.formatDateTime(
								TzmInfoLlistActivity.this
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
							PullToRefreshBase<ListView> refreshView) {
						// TODO Auto-generated method stub
						isLoadMore = true;
						currentPage++;
						loadData();

					}
				});
		lv_info_list = mPullRefreshListView.getRefreshableView();
		lv_info_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				TzmInfoListModel model  =(TzmInfoListModel) parent.getAdapter().getItem(position);
				Intent intent = new Intent(TzmInfoLlistActivity.this,
						InfoDetailActivity.class);
				intent.putExtra("id", model.id);
				startActivity(intent);

			}
		});

		loadData();

	}

	// 访问接口,获取列表的数据源
	protected void loadData() {
		// tzmInfoListApi.setType(0);
		tzmInfoListApi.setPageNo(currentPage);
		client.api(this.tzmInfoListApi, new ApiListener() {
			@Override
			public void onStart() {
			}

			@Override
			public void onComplete(String jsonStr) {
				Gson gson = new Gson();
				Type type = new TypeToken<BaseModel<ArrayList<TzmInfoListModel>>>() {
				}.getType();
				BaseModel<ArrayList<TzmInfoListModel>> base = gson.fromJson(
						jsonStr, type);
				if (isLoadMore) {
					// 加载更多
					listPosition = tzmInfoListModels.size();
					if (base.result != null) {
						tzmInfoListModels.addAll(base.result);
					} else {
						ToastUtils.showShortToast(TzmInfoLlistActivity.this,
								R.string.msg_list_null);
					}

				} else {
					// 刷新
					listPosition = 0;
					tzmInfoListModels.clear();
					if (base.result != null) {
						tzmInfoListModels = base.result;
					} else {
						ToastUtils.showShortToast(TzmInfoLlistActivity.this,
								R.string.msg_list_null);
					}
				}

				initInfoLlist();
			}

			@Override
			public void onError(String error) {
				mPullRefreshListView.onRefreshComplete();
				ToastUtils.showShortToast(TzmInfoLlistActivity.this,
						R.string.msg_list_null);
			}

			@Override
			public void onException(Exception e) {
				mPullRefreshListView.onRefreshComplete();
				LogUtil.ErrorLog(e);
			}
		}, true);

	}

	// 添加适配器,适配listview的item
	protected void initInfoLlist() {
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
