package com.ruiyu.taozhuma.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.activity.TzmShopDetailActivity.TimeCount;
import com.ruiyu.taozhuma.adapter.BrandAdapter;
import com.ruiyu.taozhuma.adapter.SelectShopAdapter;
import com.ruiyu.taozhuma.adapter.TzmShopDetailAdapter;
import com.ruiyu.taozhuma.api.ApiClient;
import com.ruiyu.taozhuma.api.ApiListener;
import com.ruiyu.taozhuma.api.GetActivityProductByTypeApi;
import com.ruiyu.taozhuma.api.TzmActivityApi;
import com.ruiyu.taozhuma.api.TzmBrandActivityApi;
import com.ruiyu.taozhuma.api.TzmShopListApi;
import com.ruiyu.taozhuma.base.xUtilsImageLoader;
import com.ruiyu.taozhuma.model.BaseModel;
import com.ruiyu.taozhuma.model.TzmBrandModel;
import com.ruiyu.taozhuma.model.TzmBrandModel.brandlist;
import com.ruiyu.taozhuma.model.TzmShopDetailModel;
import com.ruiyu.taozhuma.model.TzmShopListModel;
import com.ruiyu.taozhuma.model.TzmShopDetailModel.Product;
import com.ruiyu.taozhuma.utils.LogUtil;
import com.ruiyu.taozhuma.utils.ProgressDialogUtil;
import com.ruiyu.taozhuma.utils.ToastUtils;
import com.ruiyu.taozhuma.widget.AutofitTextView;
import com.ruiyu.taozhuma.widget.GridViewForScrollView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class BigCardsActivity extends Activity {

	@ViewInject(R.id.btn_head_left)
	private Button btn_head_left;
	@ViewInject(R.id.txt_head_title)
	private TextView txt_head_title;

	@ViewInject(R.id.tzm_big_cards_list)
	private GridViewForScrollView tzm_big_cards_list;
	@ViewInject(R.id.ptrf_sv)
	private PullToRefreshScrollView pullToRefreshScrollView;
	@ViewInject(R.id.ll_img)
	private ImageView ll_img;
	private BrandAdapter selectShopAdapter;
	private xUtilsImageLoader bitmapUtils;
	protected boolean isLoadMore = false;// 是否是加载更多
	protected int currentPage = 1;// 当前页数
	private ApiClient client;
	protected int listPosition;
	private TzmBrandActivityApi activityApi;
	private TzmBrandModel tzmShopListModels;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.big_cards_activity);
		ViewUtils.inject(this);

		initView();
		loadData();
	}

	private void initView() {

		client = new ApiClient(this);
		activityApi = new TzmBrandActivityApi();
		tzmShopListModels = new TzmBrandModel();
		bitmapUtils = new xUtilsImageLoader(this);
		bitmapUtils.configDefaultLoadFailedImage(R.drawable.loading_long);
		bitmapUtils.configDefaultLoadingImage(R.drawable.loading_long);
		btn_head_left.setOnClickListener(clickListener);
		txt_head_title.setText("大牌驾到");
		pullToRefreshScrollView
				.setOnRefreshListener(new OnRefreshListener2<ScrollView>() {
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ScrollView> refreshView) {
						String label = DateUtils.formatDateTime(
								BigCardsActivity.this.getApplicationContext(),
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
							PullToRefreshBase<ScrollView> refreshView) {
						isLoadMore = true;
						currentPage++;
						loadData();
					}
				});
		tzm_big_cards_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int positon,
					long id) {
				// 事件
				brandlist model = (TzmBrandModel.brandlist) arg0.getAdapter()
						.getItem(positon);
				Intent intent = new Intent(BigCardsActivity.this,
						TzmShopDetailActivity.class);
				// Intent intent = new Intent(c, TzmToySetMealActivity.class);
				intent.putExtra("activityId", model.activityId.toString());
				intent.putExtra("name", model.activityName);
				startActivity(intent);
			}
		});
		tzm_big_cards_list.setFocusable(false);
	}

	/**
	 * 
	 */
	protected void loadData() {

		activityApi.setPageNo(currentPage);
		client.api(this.activityApi, new ApiListener() {
			@Override
			public void onStart() {
				ProgressDialogUtil.openProgressDialog(BigCardsActivity.this,
						"", "");
			}

			@Override
			public void onComplete(String jsonStr) {
				ProgressDialogUtil.closeProgressDialog();
				Gson gson = new Gson();
				Type type = new TypeToken<BaseModel<TzmBrandModel>>() {
				}.getType();
				BaseModel<TzmBrandModel> base = gson.fromJson(jsonStr, type);
				bitmapUtils.display(ll_img, base.result.banner);
				if (isLoadMore) {
					// 加载更多
					listPosition = tzm_big_cards_list.getCount();
					if (base.result != null && base.result.brandlist.size() > 0) {
						tzmShopListModels.brandlist
								.addAll(base.result.brandlist);
						
					} else {
						ToastUtils.showShortToast(BigCardsActivity.this,
								R.string.msg_list_null);
					}

				} else {
					// 刷新
					listPosition = 0;
					//tzmShopListModels.brandlist.clear();
					if (base.result != null && base.result.brandlist.size() > 0) {
						tzmShopListModels.brandlist = base.result.brandlist;
					} else {
						// 返回数据为空
						ToastUtils.showShortToast(BigCardsActivity.this,
								R.string.msg_list_null);
					}
				}

				initProductList();
			}

			@Override
			public void onError(String error) {
				pullToRefreshScrollView.onRefreshComplete();
				ToastUtils.showShortToast(BigCardsActivity.this,
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

	/**
	 * 
	 */
	protected void initProductList() {
		if (this.tzmShopListModels != null) {
			selectShopAdapter = new BrandAdapter(BigCardsActivity.this,
					this.tzmShopListModels.brandlist);
			tzm_big_cards_list.setAdapter(selectShopAdapter);
			tzm_big_cards_list.setSelection(listPosition);
			pullToRefreshScrollView.onRefreshComplete();
		}
	}

	View.OnClickListener clickListener = new OnClickListener() {

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
	protected void onDestroy() {
		super.onDestroy();
	}

}
