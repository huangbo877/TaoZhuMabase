package com.ruiyu.taozhuma.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.adapter.GetFocusActivityProductAdapter;
import com.ruiyu.taozhuma.api.ApiClient;
import com.ruiyu.taozhuma.api.ApiListener;
import com.ruiyu.taozhuma.api.GetFocusActivityProductApi;
import com.ruiyu.taozhuma.base.xUtilsImageLoader;
import com.ruiyu.taozhuma.model.BaseModel;
import com.ruiyu.taozhuma.model.GetFocusActivityProductModel;
import com.ruiyu.taozhuma.model.GetFocusActivityProductModel.Products;
import com.ruiyu.taozhuma.utils.LogUtil;
import com.ruiyu.taozhuma.utils.ProgressDialogUtil;
import com.ruiyu.taozhuma.utils.ToastUtils;
import com.ruiyu.taozhuma.widget.GridViewForScrollView;

public class GetFocusActivityProductActivity extends Activity {

	private String TAG = "TzmShopDetailActivity";
	private TextView text_head_title;
	private GridViewForScrollView gridView;
	private int id;
	// 列表
	private PullToRefreshScrollView pullToRefreshScrollView;
	private GetFocusActivityProductAdapter ChishilistAdapter;
	// 接口调用
	private ApiClient client;
	private GetFocusActivityProductApi ChishilistApi;

	private int currentPage = 1;// 当前页数
	private boolean isLoadMore = false;// 是否是加载更多
	// 收藏
	private ImageView collect, cancel_collect;
	ArrayList<Products> products;
	//
	private int source = 4;// 产品销量(1降序11升序)、金额(2降序22升序)、点击率(4降序44升序)

	private TextView tv_popular, tv_price, tv_sales;
	private ImageView iv_popular, iv_price, iv_sales;
	private RelativeLayout rl_popular, rl_price, rl_sales;

	private ScrollView scrollView;
	Button btn_head_left;
	private ImageView convenientBanner;
	private xUtilsImageLoader bitmapUtils;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate");
		setContentView(R.layout.tzm_focusproduct_list_activity);
		id = getIntent().getIntExtra("id", 0);
		// shopName = getIntent().getStringExtra("shopName");
		bitmapUtils = new xUtilsImageLoader(this);
		bitmapUtils.configDefaultLoadFailedImage(R.drawable.loading_long);
		bitmapUtils.configDefaultLoadingImage(R.drawable.loading_long);
		initView();
		client = new ApiClient(this);
		ChishilistApi = new GetFocusActivityProductApi();
		pullToRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.ptrf);
		pullToRefreshScrollView
				.setOnRefreshListener(new OnRefreshListener2<ScrollView>() {
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ScrollView> refreshView) {
						String label = DateUtils.formatDateTime(
								GetFocusActivityProductActivity.this
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
							PullToRefreshBase<ScrollView> refreshView) {
						isLoadMore = true;
						currentPage++;
						loadData();
					}
				});

		gridView = (GridViewForScrollView) findViewById(R.id.prl_shop_list);
		scrollView = pullToRefreshScrollView.getRefreshableView();
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int positon,
					long id) {
				Products product = (Products) arg0.getAdapter().getItem(positon);
				Intent intent = new Intent(
						GetFocusActivityProductActivity.this,
						ProductDetailActivity.class);
				intent.putExtra("id",
						product.productId);
				startActivity(intent);
			}
		});
		gridView.setFocusable(false);
		loadData();
	}

	private void initView() {
		text_head_title = (TextView) findViewById(R.id.txt_head_title);
		convenientBanner = (ImageView) findViewById(R.id.convenientBanner1);
		btn_head_left = (Button) findViewById(R.id.btn_head_left);
		btn_head_left.setOnClickListener(clickListener);
		tv_popular = (TextView) findViewById(R.id.tv_popular);
		tv_price = (TextView) findViewById(R.id.tv_price);
		tv_sales = (TextView) findViewById(R.id.tv_sales);
		iv_popular = (ImageView) findViewById(R.id.iv_popular);
		iv_price = (ImageView) findViewById(R.id.iv_price);
		iv_sales = (ImageView) findViewById(R.id.iv_sales);
		rl_popular = (RelativeLayout) findViewById(R.id.rl_popular);
		rl_price = (RelativeLayout) findViewById(R.id.rl_price);
		rl_sales = (RelativeLayout) findViewById(R.id.rl_sales);
		rl_popular.setOnClickListener(clickListener);
		rl_price.setOnClickListener(clickListener);
		rl_sales.setOnClickListener(clickListener);

	}

	protected void loadData() {
		ChishilistApi.setId(id);
		ChishilistApi.setSource(source);
		ChishilistApi.setPageNo(currentPage);
		client.api(this.ChishilistApi, new ApiListener() {
			@Override
			public void onStart() {
				if(!isLoadMore)
				ProgressDialogUtil.openProgressDialog(
						GetFocusActivityProductActivity.this, "", "");
			}

			@Override
			public void onComplete(String jsonStr) {
				pullToRefreshScrollView.onRefreshComplete();
				ProgressDialogUtil.closeProgressDialog();
				Gson gson = new Gson();
				Type type = new TypeToken<BaseModel<GetFocusActivityProductModel>>() {
				}.getType();
				BaseModel<GetFocusActivityProductModel> base = gson.fromJson(
						jsonStr, type);

				if (isLoadMore) {
					// 加载更多
					if (base.result != null &&base.result.products.size()>0) {
						products.addAll(base.result.products);
						ChishilistAdapter.notifyDataSetChanged();
					} else {
						ToastUtils.showShortToast(
								GetFocusActivityProductActivity.this,
								R.string.msg_list_null);
					}
				} else {
					// 刷新
					text_head_title.setText(base.result.title);
					bitmapUtils.display(convenientBanner, base.result.banner);
					if (base.result != null&&base.result.products.size()>0) {
						products = base.result.products;
						
					} else {
						// 返回数据为空
						ToastUtils.showShortToast(
								GetFocusActivityProductActivity.this,
								R.string.msg_list_null);
					}
					initProductList();
				}
			}

			@Override
			public void onError(String error) {
				pullToRefreshScrollView.onRefreshComplete();
				ToastUtils.showShortToast(GetFocusActivityProductActivity.this,
						error);
				ProgressDialogUtil.closeProgressDialog();
			}

			@Override
			public void onException(Exception e) {
				pullToRefreshScrollView.onRefreshComplete();
				ProgressDialogUtil.closeProgressDialog();
				ToastUtils.showShortToast(GetFocusActivityProductActivity.this,
						"网络异常");
				LogUtil.ErrorLog(e);
			}
		}, true);
	}

	protected void initProductList() {
		if (this.products != null) {
			ChishilistAdapter = new GetFocusActivityProductAdapter(
					GetFocusActivityProductActivity.this, this.products);
			gridView.setAdapter(ChishilistAdapter);
			pullToRefreshScrollView.onRefreshComplete();
			scrollView.scrollTo(0, 0);
		}
	}

	OnClickListener clickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_head_left:
				onBackPressed();
				break;
			case R.id.rl_popular:
				handler.sendEmptyMessage(4);
				break;
			case R.id.rl_price:
				handler.sendEmptyMessage(5);
				break;
			case R.id.rl_sales:
				handler.sendEmptyMessage(6);
				break;
			}
		}
	};

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				collect.setVisibility(View.GONE);
				cancel_collect.setVisibility(View.VISIBLE);
				break;
			case 1:
				cancel_collect.setVisibility(View.GONE);
				collect.setVisibility(View.VISIBLE);
				break;
			case 4:
				tv_popular.setTextColor(getResources().getColor(R.color.base));
				tv_price.setTextColor(getResources()
						.getColor(R.color.tzm_black));
				tv_sales.setTextColor(getResources()
						.getColor(R.color.tzm_black));
				iv_price.setImageResource(R.drawable.tzm_287);
				iv_sales.setImageResource(R.drawable.tzm_287);
				if (source == 44) {
					source = 4;
					iv_popular.setImageResource(R.drawable.tzm_286);
				} else {
					source = 44;
					iv_popular.setImageResource(R.drawable.tzm_285);
				}
				isLoadMore = false;
				currentPage = 1;
				loadData();
				break;
			case 5:
				tv_price.setTextColor(getResources().getColor(R.color.base));
				tv_popular.setTextColor(getResources().getColor(
						R.color.tzm_black));
				tv_sales.setTextColor(getResources()
						.getColor(R.color.tzm_black));
				iv_popular.setImageResource(R.drawable.tzm_287);
				iv_sales.setImageResource(R.drawable.tzm_287);
				if (source == 22) {
					source = 2;
					iv_price.setImageResource(R.drawable.tzm_286);
				} else {
					source = 22;
					iv_price.setImageResource(R.drawable.tzm_285);
				}
				isLoadMore = false;
				currentPage = 1;
				loadData();
				break;
			case 6:
				tv_sales.setTextColor(getResources().getColor(R.color.base));
				tv_popular.setTextColor(getResources().getColor(
						R.color.tzm_black));
				tv_price.setTextColor(getResources()
						.getColor(R.color.tzm_black));
				iv_popular.setImageResource(R.drawable.tzm_287);
				iv_price.setImageResource(R.drawable.tzm_287);

				if (source == 11) {
					source = 1;
					iv_sales.setImageResource(R.drawable.tzm_286);
				} else {
					source = 11;
					iv_sales.setImageResource(R.drawable.tzm_285);
				}
				isLoadMore = false;
				currentPage = 1;
				loadData();
				break;

			}
		};
	};

}
