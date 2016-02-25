package com.ruiyu.taozhuma.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.adapter.TzmSearchAdapter;
import com.ruiyu.taozhuma.api.ApiClient;
import com.ruiyu.taozhuma.api.ApiListener;
import com.ruiyu.taozhuma.api.TzmSearchAllApi;
import com.ruiyu.taozhuma.base.xUtilsImageLoader;
import com.ruiyu.taozhuma.config.AppConfig;
import com.ruiyu.taozhuma.model.BaseModel;
import com.ruiyu.taozhuma.model.TzmCustomSearchModel;
import com.ruiyu.taozhuma.model.TzmSearchAllModel;
import com.ruiyu.taozhuma.model.TzmSearchAllModel.Product;
import com.ruiyu.taozhuma.model.TzmSearchAllModel.Shop;
import com.ruiyu.taozhuma.utils.LogUtil;
import com.ruiyu.taozhuma.utils.ProgressDialogUtil;
import com.ruiyu.taozhuma.utils.StringUtils;
import com.ruiyu.taozhuma.utils.ToastUtils;
import com.ruiyu.taozhuma.utils.UserInfoUtils;
import com.ruiyu.taozhuma.widget.BottomScrollView;
import com.ruiyu.taozhuma.widget.BottomScrollView.OnScrollToBottomListener;
import com.ruiyu.taozhuma.widget.GridViewForScrollView;

/**
 * 
 * 搜集结果显示的界面
 */
public class TzmSearchItemActivity extends Activity {

	private String TAG = "TzmSearchItemActivity";
	private Button btn_head_left;
	private TextView txt_head_title;

	private TextView tv_popular, tv_price, tv_sales, tv_new;
	private ImageView iv_popular, iv_price, iv_sales;
	private RelativeLayout rl_popular, rl_price, rl_sales, rl_new;

	private BottomScrollView bottomScrollView;
	private LinearLayout llayout, top_llayout;

	private int currentPage = 1;// 当前页数
	private boolean isLoadMore = false;// 是否是加载更多
	private int listPosition = 0;

	private int sorting = 1;// 排序（1为销量降序，11为销量升序，2为价格升序，22为价格降序，
							// 4为点击数降序，44为点击数升序，为空ID降序）

	private String keys = "";// 搜索关键字（产品名称）
	private TzmSearchAllApi tzmSearchAllApi;
	private ApiClient apiClient;
	private TzmSearchAllModel tzmSearchAllModel;
	private ArrayList<ArrayList<TzmCustomSearchModel>> arrayLists;
	private ArrayList<TzmCustomSearchModel> tzmCustomSearchModels;
	private TzmCustomSearchModel customSearchModel;
	private ArrayList<Product> product;
	private ArrayList<Shop> shop;

	private xUtilsImageLoader imageLoader;
	private GridViewForScrollView gv_search;
	private TzmSearchAdapter searchAdapter;

	private int list_position;

	private boolean isHistory = false;// 数据来源是否为历史缓存

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_item_acticity);
		LogUtil.Log(TAG, "onCreate");
		imageLoader = new xUtilsImageLoader(TzmSearchItemActivity.this);
		list_position = getIntent().getIntExtra("list_position", -1);
		isHistory = getIntent().getBooleanExtra("isHistory", false);
		if (getIntent().getStringExtra("keys") != null) {
			keys = getIntent().getStringExtra("keys");
		}
		arrayLists = UserInfoUtils.getSearchHistory();// 并获取历史记录
		initView();

		apiClient = new ApiClient(this);
		tzmSearchAllApi = new TzmSearchAllApi();
		tzmSearchAllModel = new TzmSearchAllModel();

		tzmCustomSearchModels = new ArrayList<TzmCustomSearchModel>();

		if (arrayLists == null) {
			arrayLists = new ArrayList<ArrayList<TzmCustomSearchModel>>();
		}
		customSearchModel = new TzmCustomSearchModel();

		customSearchModel.searchKey = keys;

		tzmCustomSearchModels.add(customSearchModel);
		for (int i = 0; i < arrayLists.size(); i++) {// 如果已存在该字段，则把旧的删除
			if (tzmCustomSearchModels.get(0).searchKey.equals(arrayLists.get(i)
					.get(0).searchKey)) {
				arrayLists.remove(i);
			}
		}
		arrayLists.add(tzmCustomSearchModels);
		UserInfoUtils.setSearchHistory(arrayLists);// 设置缓存
		gv_search = (GridViewForScrollView) findViewById(R.id.gv_search);
		gv_search.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int positon,
					long id) {
				// 判断数据显示来源
				if (isHistory) {// 从历史记录里获取数据
					if (arrayLists.get(list_position).get(positon).type == 1) {// type==1为商品
						Intent intent = new Intent(TzmSearchItemActivity.this,
								ProductDetailActivity.class);
						intent.putExtra("id", arrayLists.get(list_position)
								.get(positon).pId);
						intent.putExtra("activityId",
								tzmCustomSearchModels.get(positon).activityId);
						startActivity(intent);
					} else if (arrayLists.get(positon).get(0).type == 2) {// type==2为店铺
						Intent intent2 = new Intent(TzmSearchItemActivity.this,
								TzmShopDetailActivity.class);
						intent2.putExtra("id", arrayLists.get(list_position)
								.get(positon).sId);
						intent2.putExtra(
								"shopName",
								arrayLists.get(list_position).get(positon).shopName);
						startActivity(intent2);
					}
				} else {
					if (tzmCustomSearchModels.get(positon).type == 1) {
						Intent intent = new Intent(TzmSearchItemActivity.this,
								ProductDetailActivity.class);
						intent.putExtra("id",
								tzmCustomSearchModels.get(positon).pId);
						intent.putExtra("activityId",
								tzmCustomSearchModels.get(positon).activityId);
						startActivity(intent);
					} else if (tzmCustomSearchModels.get(positon).type == 2) {
						Intent intent2 = new Intent(TzmSearchItemActivity.this,
								TzmShopDetailActivity.class);
						intent2.putExtra("id",
								tzmCustomSearchModels.get(positon).sId);
						intent2.putExtra("shopName",
								tzmCustomSearchModels.get(positon).shopName);
						startActivity(intent2);
					}
				}
			}
		});
		if (isHistory) {
			top_llayout.setVisibility(View.GONE);
			tzmCustomSearchModels = arrayLists.get(list_position);
			initSearchList(isHistory);
		} else {
			top_llayout.setVisibility(View.VISIBLE);
			loadData();
		}
	}

	private void initView() {

		btn_head_left = (Button) findViewById(R.id.btn_head_left);
		btn_head_left.setOnClickListener(clickListener);
		txt_head_title = (TextView) findViewById(R.id.txt_head_title);
		txt_head_title.setText("搜索结果");
		top_llayout = (LinearLayout) findViewById(R.id.top_llayout);
		tv_popular = (TextView) findViewById(R.id.tv_popular);
		tv_price = (TextView) findViewById(R.id.tv_price);
		tv_sales = (TextView) findViewById(R.id.tv_sales);
		tv_new = (TextView) findViewById(R.id.tv_new);
		iv_popular = (ImageView) findViewById(R.id.iv_popular);
		iv_price = (ImageView) findViewById(R.id.iv_price);
		iv_sales = (ImageView) findViewById(R.id.iv_sales);
		rl_popular = (RelativeLayout) findViewById(R.id.rl_popular);
		rl_price = (RelativeLayout) findViewById(R.id.rl_price);
		rl_sales = (RelativeLayout) findViewById(R.id.rl_sales);
		rl_new = (RelativeLayout) findViewById(R.id.rl_new);

		rl_new.setOnClickListener(clickListener);
		rl_popular.setOnClickListener(clickListener);
		rl_price.setOnClickListener(clickListener);
		rl_sales.setOnClickListener(clickListener);
		llayout = (LinearLayout) findViewById(R.id.id_llayout);
		bottomScrollView = (BottomScrollView) findViewById(R.id.id_scroll);
		bottomScrollView.setOnScrollToBottomLintener(
				new OnScrollToBottomListener() {

					@Override
					public void FingerUpLinstener(boolean moveDistance) {
						if (!isHistory) {
							if (moveDistance) {
								isLoadMore = true;
								currentPage++;
								loadData();

							}
						}
					}
				}, llayout);
	}

	OnClickListener clickListener = new View.OnClickListener() {

		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_head_left:
				Intent intent = new Intent();
				setResult(11, intent);
				finish();
				break;
			case R.id.rl_popular:
				handler.sendEmptyMessage(1);
				break;
			case R.id.rl_price:
				handler.sendEmptyMessage(2);
				break;
			case R.id.rl_sales:
				handler.sendEmptyMessage(3);
				break;
			case R.id.rl_new:
				handler.sendEmptyMessage(4);
				break;
			}
		}
	};

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				tv_popular.setTextColor(getResources().getColor(R.color.base));
				tv_price.setTextColor(getResources()
						.getColor(R.color.tzm_black));
				tv_sales.setTextColor(getResources()
						.getColor(R.color.tzm_black));
				tv_new.setTextColor(getResources().getColor(R.color.tzm_black));
				iv_price.setImageResource(R.drawable.tzm_266);
				iv_sales.setImageResource(R.drawable.tzm_266);
				if (sorting == 33) {
					sorting = 0;
					iv_popular.setImageResource(R.drawable.tzm_264);
				} else {
					sorting = 33;
					iv_popular.setImageResource(R.drawable.tzm_265);
				}
				isLoadMore = false;
				currentPage = 1;
				loadData();
				break;
			case 2:
				tv_price.setTextColor(getResources().getColor(R.color.base));
				tv_popular.setTextColor(getResources().getColor(
						R.color.tzm_black));
				tv_sales.setTextColor(getResources()
						.getColor(R.color.tzm_black));
				tv_new.setTextColor(getResources().getColor(R.color.tzm_black));
				iv_popular.setImageResource(R.drawable.tzm_266);
				iv_sales.setImageResource(R.drawable.tzm_266);
				if (sorting == 22) {
					sorting = 2;
					iv_price.setImageResource(R.drawable.tzm_264);
				} else {
					sorting = 22;
					iv_price.setImageResource(R.drawable.tzm_265);
				}
				isLoadMore = false;
				currentPage = 1;
				loadData();
				break;
			case 3:
				tv_sales.setTextColor(getResources().getColor(R.color.base));
				tv_popular.setTextColor(getResources().getColor(
						R.color.tzm_black));
				tv_price.setTextColor(getResources()
						.getColor(R.color.tzm_black));
				tv_new.setTextColor(getResources().getColor(R.color.tzm_black));
				iv_popular.setImageResource(R.drawable.tzm_266);
				iv_price.setImageResource(R.drawable.tzm_266);
				if (sorting == 11) {
					sorting = 1;
					iv_sales.setImageResource(R.drawable.tzm_264);
				} else {
					sorting = 11;
					iv_sales.setImageResource(R.drawable.tzm_265);
				}
				isLoadMore = false;
				currentPage = 1;
				loadData();
				break;
			case 4:
				tv_new.setTextColor(getResources().getColor(R.color.base));
				tv_sales.setTextColor(getResources()
						.getColor(R.color.tzm_black));
				tv_popular.setTextColor(getResources().getColor(
						R.color.tzm_black));
				tv_price.setTextColor(getResources()
						.getColor(R.color.tzm_black));
				iv_popular.setImageResource(R.drawable.tzm_266);
				iv_price.setImageResource(R.drawable.tzm_266);
				iv_sales.setImageResource(R.drawable.tzm_266);
				sorting = 4;
				isLoadMore = false;
				currentPage = 1;
				loadData();
				break;
			}
		}

	};

	private void loadData() {
		if (StringUtils.isNotEmpty(keys)) {
			tzmSearchAllApi.setName(keys);
		}
		tzmSearchAllApi.setSorting(sorting + "");
		tzmSearchAllApi.setPageNo(currentPage);
		apiClient.api(tzmSearchAllApi, new ApiListener() {

			@Override
			public void onStart() {
				ProgressDialogUtil.openProgressDialog(
						TzmSearchItemActivity.this, "", "");
			}

			@Override
			public void onError(String error) {
				ProgressDialogUtil.closeProgressDialog();
				if (isLoadMore) {
					ToastUtils.showShortToast(TzmSearchItemActivity.this,
							"没有更多的数据");
				} else {
					ToastUtils.showShortToast(TzmSearchItemActivity.this,
							"没有更多的数据");
					tzmCustomSearchModels.clear();
					if (searchAdapter != null) {
						searchAdapter.notifyDataSetChanged();
					}
				}
			}

			@Override
			public void onException(Exception e) {
				ProgressDialogUtil.closeProgressDialog();
				LogUtil.ErrorLog(e);
			}

			@Override
			public void onComplete(String jsonStr) {
				ProgressDialogUtil.closeProgressDialog();
				Gson gson = new Gson();
				Type type = new TypeToken<BaseModel<TzmSearchAllModel>>() {
				}.getType();
				BaseModel<TzmSearchAllModel> base = gson
						.fromJson(jsonStr, type);
				tzmSearchAllModel = base.result;
				product = tzmSearchAllModel.product;
				shop = tzmSearchAllModel.shop;
				if (!isLoadMore) {
					if (tzmCustomSearchModels != null
							&& tzmCustomSearchModels.size() > 0) {
						tzmCustomSearchModels.clear();
					}
					listPosition = 0;
				}
				if (base.result != null) {
					for (int i = 0; i < shop.size(); i++) {
						customSearchModel = new TzmCustomSearchModel();
						customSearchModel.searchKey = keys;
						customSearchModel.type = 2;
						customSearchModel.shopName = shop.get(i).shopName;
						customSearchModel.shopImage = shop.get(i).shopImage;
						customSearchModel.sId = shop.get(i).sId;
						customSearchModel.address = shop.get(i).address;
						customSearchModel.mainCategory = shop.get(i).mainCategory;
						tzmCustomSearchModels.add(customSearchModel);
					}
					for (int i = 0; i < product.size(); i++) {
						customSearchModel = new TzmCustomSearchModel();
						customSearchModel.type = 1;
						customSearchModel.pId = product.get(i).pId;
						customSearchModel.activityId = product.get(i).activityId;
						customSearchModel.sellNumber = product.get(i).sellNumber;
						customSearchModel.distributionPrice = product.get(i).distributionPrice;
						customSearchModel.productImage = product.get(i).productImage;
						customSearchModel.productName = product.get(i).productName;
						tzmCustomSearchModels.add(customSearchModel);
					}

				}
				isHistory = false;
				// top_llayout.setVisibility(View.VISIBLE);
				// bottomScrollView.setVisibility(View.VISIBLE);
				initSearchList(isHistory);
			}
		}, true);
	};

	private void initSearchList(boolean isHistory) {

		if (false) {
			for (int i = 0; i < arrayLists.size(); i++) {// 如果已存在该字段，则把旧的删除
				if (tzmCustomSearchModels.get(0).searchKey.equals(arrayLists
						.get(i).get(0).searchKey)) {
					arrayLists.remove(i);
				}
			}

			arrayLists.add(tzmCustomSearchModels);
			UserInfoUtils.setSearchHistory(arrayLists);// 设置缓存

		}

		searchAdapter = new TzmSearchAdapter(TzmSearchItemActivity.this,
				tzmCustomSearchModels, imageLoader);
		gv_search.setAdapter(searchAdapter);
		gv_search.setSelection(listPosition);
	}

	@Override
	protected void onDestroy() {
		handler.removeCallbacksAndMessages(null);
		super.onDestroy();
	}
}
