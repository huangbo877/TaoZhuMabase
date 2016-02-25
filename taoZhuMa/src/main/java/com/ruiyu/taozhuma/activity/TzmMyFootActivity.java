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
import com.ruiyu.taozhuma.adapter.TzmMyFootAdapter;
import com.ruiyu.taozhuma.api.ApiClient;
import com.ruiyu.taozhuma.api.ApiListener;
import com.ruiyu.taozhuma.api.TzmMyFootApi;
import com.ruiyu.taozhuma.model.BaseModel;
import com.ruiyu.taozhuma.model.TzmMyFootModel;
import com.ruiyu.taozhuma.model.UserModel;
import com.ruiyu.taozhuma.utils.LogUtil;
import com.ruiyu.taozhuma.utils.ProgressDialogUtil;
import com.ruiyu.taozhuma.utils.ToastUtils;
import com.ruiyu.taozhuma.utils.UserInfoUtils;

public class TzmMyFootActivity extends Activity {
	private TextView txt_head_title;
	private Button btn_head_left;
	private GridView gridView;
	private TextView tv_content;

	private PullToRefreshGridView mPullRefreshGridView;
	private TzmMyFootAdapter tzmMyFootAdapter;

	private TzmMyFootApi myFootApi;
	private ApiClient apiClient;
	private List<TzmMyFootModel> list;
	private int currentPage = 1;// 当前页数
	private boolean isLoadMore = false;// 是否是加载更多
	private int listPosition = 0;

	private Boolean isLogin;
	private UserModel userModel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tzm_myfoot_activity);
		initView();
		checkLogin();
		list = new ArrayList<TzmMyFootModel>();
		mPullRefreshGridView = (PullToRefreshGridView) findViewById(R.id.prgl_list);
		mPullRefreshGridView
				.setOnRefreshListener(new OnRefreshListener2<GridView>() {
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<GridView> refreshView) {
						String label = DateUtils.formatDateTime(
								TzmMyFootActivity.this.getApplicationContext(),
								System.currentTimeMillis(),
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
		loadData();
	}

	private void initView() {
		txt_head_title = (TextView) findViewById(R.id.txt_head_title);
		txt_head_title.setText("我的足迹");
		btn_head_left = (Button) findViewById(R.id.btn_head_left);
		btn_head_left.setOnClickListener(clickListener);
		tv_content=(TextView) findViewById(R.id.tv_content);

	}

	protected void loadData() {
		apiClient = new ApiClient(this);
		myFootApi = new TzmMyFootApi();
		myFootApi.setUid(userModel.uid);
		myFootApi.setPageNo(currentPage);
		apiClient.api(myFootApi, new ApiListener() {
			
			@Override
			public void onStart() {
				ProgressDialogUtil.openProgressDialog(
						TzmMyFootActivity.this, "", "");
			}
			@Override
			public void onError(String error) {
				mPullRefreshGridView.onRefreshComplete();
//				ToastUtils.showShortToast(TzmMyFootActivity.this,
//						R.string.msg_list_null);
				ProgressDialogUtil.closeProgressDialog();
			}

			@Override
			public void onException(Exception e) {
				mPullRefreshGridView.onRefreshComplete();
				ProgressDialogUtil.closeProgressDialog();
				LogUtil.ErrorLog(e);
			}
			@Override
			public void onComplete(String jsonStr) {
				ProgressDialogUtil.closeProgressDialog();
				Gson gson = new Gson();
				Type type = new TypeToken<BaseModel<ArrayList<TzmMyFootModel>>>() {
				}.getType();
				BaseModel<ArrayList<TzmMyFootModel>> base = gson.fromJson(
						jsonStr, type);

				if (isLoadMore) {
					// 加载更多
					listPosition = gridView.getCount();
					if (base.result != null && base.result.size() > 0) {
						list.addAll(base.result);
					} else {
						ToastUtils.showShortToast(TzmMyFootActivity.this,
								R.string.msg_list_null);
					}

				} else {
					// 刷新
					listPosition = 0;
					list.clear();
					if (base.result != null && base.result.size() > 0) {
						list = base.result;
						tv_content.setVisibility(View.GONE);
					} else {
						// 返回数据为空
						tv_content.setVisibility(View.VISIBLE);
//						ToastUtils.showShortToast(TzmMyFootActivity.this,
//								R.string.msg_list_null);
					}
				}

				initProductList();
			}
		}, true);
	}

	protected void initProductList() {
		tzmMyFootAdapter = new TzmMyFootAdapter(TzmMyFootActivity.this,
				list);
		gridView.setAdapter(tzmMyFootAdapter);
		gridView.setSelection(listPosition);
		mPullRefreshGridView.onRefreshComplete();
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

	// 检查用户是否登陆
	private void checkLogin() {
		isLogin = UserInfoUtils.isLogin();
		if (isLogin) {
			userModel = UserInfoUtils.getUserInfo();
		}
	}
}
