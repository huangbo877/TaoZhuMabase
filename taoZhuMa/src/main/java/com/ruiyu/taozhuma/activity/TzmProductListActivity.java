package com.ruiyu.taozhuma.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.adapter.TzmProductAdapter;
import com.ruiyu.taozhuma.api.ApiClient;
import com.ruiyu.taozhuma.api.ApiListener;
import com.ruiyu.taozhuma.api.TzmProductApi;
import com.ruiyu.taozhuma.base.xUtilsImageLoader;
import com.ruiyu.taozhuma.model.BaseModel;
import com.ruiyu.taozhuma.model.TzmProductModel;
import com.ruiyu.taozhuma.utils.LogUtil;
import com.ruiyu.taozhuma.utils.ProgressDialogUtil;
import com.ruiyu.taozhuma.utils.StringUtils;
import com.ruiyu.taozhuma.utils.ToastUtils;

/**
 * 产品列表
 * 
 * @author Fu
 * 
 */
public class TzmProductListActivity extends Activity {

	private String TAG="TzmProductListActivity";
	private TextView txt_head_title;
	private Button btn_head_left;
	private GridView gridView;
	// 列表
	private PullToRefreshGridView mPullRefreshGridView;
	private TzmProductAdapter tzmProductAdapter;
	// 接口调用
	private ApiClient client;
	private TzmProductApi api;
	private List<TzmProductModel> list;
	private int currentPage = 1;// 当前页数
	private boolean isLoadMore = false;// 是否是加载更多
	// 查询条件
	private Integer subTypeId;// （0为所有；1为遥控/电动玩具；2为早教/音乐玩具
								// ；3为过家家玩具；4为童车玩具；5为益智玩具；6为其他玩具）
	private Integer typeId;// 小分类
	private Integer brandid;// 品牌id
	private int sorting = 4;// (0为热门(降序)，33为热门（升序），1为销售数量（降序），11为销售数量（升序），2为价格从高到低，22为价格从低到高,4为新品排序)
	private Integer display;// 是否新品：0=》否；1=》是
	private String keys;// 搜索关键字（产品名称）

	private TextView tv_popular, tv_price, tv_sales, tv_new;
	private ImageView iv_popular, iv_price, iv_sales, iv_gotop;
	private RelativeLayout rl_popular, rl_price, rl_sales, rl_new;

	// private ImageView imbt_head_right;


	private xUtilsImageLoader imageLoader;

	private float y = 0;

	private boolean isTop = true;

	private String title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tzm_product_list_activity);
		LogUtil.Log(TAG, "onCreate");
		//checkLogin();
		subTypeId = getIntent().getIntExtra("subTypeId", 0);
		typeId = getIntent().getIntExtra("typeId", 0);
		brandid = getIntent().getIntExtra("brandid", 0);
		display = getIntent().getIntExtra("display", 3);
		keys = getIntent().getExtras().getString("keys");
		title = getIntent().getExtras().getString("title");
		initView();
		client = new ApiClient(this);
		api = new TzmProductApi();
		list = new ArrayList<TzmProductModel>();
		imageLoader = new xUtilsImageLoader(this);
		mPullRefreshGridView = (PullToRefreshGridView) findViewById(R.id.prgl_product_list);
		mPullRefreshGridView
				.setOnRefreshListener(new OnRefreshListener2<GridView>() {
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<GridView> refreshView) {
						String label = DateUtils.formatDateTime(
								TzmProductListActivity.this
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
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int positon,
					long id) {
				TzmProductModel model = list.get(positon);
				Intent intent = new Intent(TzmProductListActivity.this,
						ProductDetailActivity.class);
				intent.putExtra("id", model.id);
				intent.putExtra("activityId", model.activityId);
				LogUtil.Log("TAG", "商品专场活动ID:" +model.activityId);
				startActivity(intent);
			}
		});

		gridView.setOnTouchListener(new OnTouchListener() {

			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					y = event.getY();
					break;
				case MotionEvent.ACTION_MOVE:

					break;
				case MotionEvent.ACTION_UP:
					if (event.getY() - y > 100 && !isTop) {
						// ToastUtils.showToast(TzmProductListActivity.this,
						// "上滑");
						showView();
					} else if (y - event.getY() > 100) {
						// ToastUtils.showToast(TzmProductListActivity.this,
						// "下滑");
						hideView();
					}
					break;

				}
				return false;
			}
		});
		gridView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {
				case OnScrollListener.SCROLL_STATE_IDLE:
					if (view.getFirstVisiblePosition() == 0) {
						isTop = true;
						hideView();
					} else {
						isTop = false;
					}
					break;

				}

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

			}
		});
		loadData();
	}

	private void showView() {
		if (iv_gotop.getVisibility() == View.GONE) {
			iv_gotop.setVisibility(View.VISIBLE);
		}
	}

	private void hideView() {
		if (iv_gotop.getVisibility() == View.VISIBLE) {
			iv_gotop.setVisibility(View.GONE);
		}
	}

	private void initView() {
		txt_head_title = (TextView) findViewById(R.id.txt_head_title);
		if (StringUtils.isNotEmpty(title)) {
			txt_head_title.setText(title);
		} else {
			txt_head_title.setText("产品列表");
		}

		btn_head_left = (Button) findViewById(R.id.btn_head_left);
		btn_head_left.setOnClickListener(clickListener);
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
		iv_gotop = (ImageView) findViewById(R.id.iv_gotop);
		iv_gotop.setOnClickListener(clickListener);
		// imbt_head_right=(ImageView) findViewById(R.id.imbt_head_right);
		// if(isLogin&&userModel.type==6){
		// imbt_head_right.setVisibility(View.GONE);
		// }else{
		// imbt_head_right.setVisibility(View.VISIBLE);
		// imbt_head_right.setBackgroundResource(R.drawable.cart);
		// imbt_head_right.setOnClickListener(clickListener);
		// }
	}


	protected void loadData() {
		api = new TzmProductApi();
		api.setSubTypeId(subTypeId);
		if (typeId != 0) {
			api.setTypeId(typeId);
		}
		if (brandid != 0) {
			api.setBrandid(brandid);
		}
		if (display != 3) {
			api.setDisplay(display);
		}
		api.setSorting(sorting);

		if (StringUtils.isNotEmpty(keys)) {
			api.setKeys(keys);
		}
		api.setPageNo(currentPage);
		client.api(this.api, new ApiListener() {
			@Override
			public void onStart() {
				ProgressDialogUtil.openProgressDialog(
						TzmProductListActivity.this, "", "");
			}

			@Override
			public void onComplete(String jsonStr) {
				ProgressDialogUtil.closeProgressDialog();
				Gson gson = new Gson();
				Type type = new TypeToken<BaseModel<List<TzmProductModel>>>() {
				}.getType();
				BaseModel<List<TzmProductModel>> base = gson.fromJson(
						jsonStr, type);

				if (isLoadMore) {
					// 加载更多
					if (base.result != null && base.result.size() > 0) {
						list.addAll(base.result);
						tzmProductAdapter.notifyDataSetChanged();//有bug，如果新增数据只有一个，更新后无法显示，只有上下滑动后才出现（待解决）
					} else {
						ToastUtils.showShortToast(TzmProductListActivity.this,
								R.string.msg_list_null);
					}
					mPullRefreshGridView.onRefreshComplete();

				} else {
					// 刷新
					list.clear();
					if (base.result != null && base.result.size() > 0) {
						list = base.result;
					} else {
						// 返回数据为空
						ToastUtils.showShortToast(TzmProductListActivity.this,
								"没有数据");
					}

					initProductList();
					mPullRefreshGridView.onRefreshComplete();
				}

			}

			@Override
			public void onError(String error) {
				mPullRefreshGridView.onRefreshComplete();
				ToastUtils.showShortToast(TzmProductListActivity.this,
						error);
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
		// LogUtil.Log("Size:" + this.TzmHotProductModels.size() +
		// "listPosition:"
		// + listPosition);
		// if (productGridViewAdapter == null) {
		// name();
		tzmProductAdapter = new TzmProductAdapter(TzmProductListActivity.this,
				list, imageLoader);
		gridView.setAdapter(tzmProductAdapter);
	}

	// private void name() {
	// for (int i = 0; i < 30; i++) {
	// TzmProductModel model = new TzmProductModel();
	// model.name = i + "产品";
	// list.add(model);
	// }
	// }

	OnClickListener clickListener = new View.OnClickListener() {

		public void onClick(View v) {
			switch (v.getId()) {

			case R.id.iv_gotop:
				// gridView.setSelection(0);
				gridView.smoothScrollToPosition(0);
				break;
			case R.id.btn_head_left:
				onBackPressed();
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
			// case R.id.imbt_head_right:
			// startActivity(new Intent(TzmProductListActivity.this,
			// TzmShopActivity.class));
			// break;
			}
		}
	};

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				tv_popular.setTextColor(getResources().getColor(
						R.color.base));
				tv_price.setTextColor(getResources().getColor(R.color.tzm_black));
				tv_sales.setTextColor(getResources().getColor(R.color.tzm_black));
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
				tv_price.setTextColor(getResources().getColor(
						R.color.base));
				tv_popular.setTextColor(getResources().getColor(R.color.tzm_black));
				tv_sales.setTextColor(getResources().getColor(R.color.tzm_black));
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
				tv_sales.setTextColor(getResources().getColor(
						R.color.base));
				tv_popular.setTextColor(getResources().getColor(R.color.tzm_black));
				tv_price.setTextColor(getResources().getColor(R.color.tzm_black));
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
				tv_new.setTextColor(getResources().getColor(
						R.color.base));
				tv_sales.setTextColor(getResources().getColor(R.color.tzm_black));
				tv_popular.setTextColor(getResources().getColor(R.color.tzm_black));
				tv_price.setTextColor(getResources().getColor(R.color.tzm_black));
				iv_popular.setImageResource(R.drawable.tzm_266);
				iv_price.setImageResource(R.drawable.tzm_266);
				iv_sales.setImageResource(R.drawable.tzm_266);
				sorting = 4;
				isLoadMore = false;
				currentPage = 1;
				loadData();
				break;
			}
		};
	};
}
