package com.ruiyu.taozhuma.activity;

import java.lang.reflect.Type;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.json.JSONException;
import org.json.JSONObject;

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
import android.widget.ImageButton;
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
import com.ruiyu.taozhuma.adapter.TzmShopDetailAdapter;
import com.ruiyu.taozhuma.api.AddfavoritesApi;
import com.ruiyu.taozhuma.api.ApiClient;
import com.ruiyu.taozhuma.api.ApiListener;
import com.ruiyu.taozhuma.api.DelSingleFavoriteApi;
import com.ruiyu.taozhuma.api.SearchFavoriteApi;
import com.ruiyu.taozhuma.api.TzmShopDetailApi;
import com.ruiyu.taozhuma.base.xUtilsImageLoader;
import com.ruiyu.taozhuma.config.AppConfig;
import com.ruiyu.taozhuma.model.BaseModel;
import com.ruiyu.taozhuma.model.TzmShopDetailModel;
import com.ruiyu.taozhuma.model.TzmShopDetailModel.Product;
import com.ruiyu.taozhuma.model.UserModel;
import com.ruiyu.taozhuma.utils.LogUtil;
import com.ruiyu.taozhuma.utils.ProgressDialogUtil;
import com.ruiyu.taozhuma.utils.StringUtils;
import com.ruiyu.taozhuma.utils.ToastUtils;
import com.ruiyu.taozhuma.utils.UserInfoUtils;
import com.ruiyu.taozhuma.widget.GridViewForScrollView;

/**
 * 
 * @author 林尧 加载每个专场 2016-2-19
 */
public class TzmShopDetailActivity extends Activity {

	private String TAG = "TzmShopDetailActivity";
	private TextView text_head_title;
	private ImageView btn_head_left;
	private GridViewForScrollView gridView;
	private String activityId;
	private String name;
	boolean stopThread = false;
	// 列表
	private PullToRefreshScrollView pullToRefreshScrollView;
	private TzmShopDetailAdapter tzmShopDetailAdapter;
	// 接口调用
	private ApiClient client, client3;
	private TzmShopDetailApi tzmShopDetailApi;
	private TzmShopDetailModel tzmShopDetailModel;
	private int currentPage = 1;// 当前页数
	private boolean isLoadMore = false;// 是否是加载更多
	private int listPosition = 0;
	// 收藏
	private ImageView collect, cancel_collect;
	private Boolean isLogin;
	private UserModel userModel;
	private SearchFavoriteApi searchFavoriteApi;
	private AddfavoritesApi addfavoritesApi;
	private DelSingleFavoriteApi delfavoriteApi;
	private Handler mHandler = new Handler();
	//
	private int source = 0;// 综合排序(0)、产品销量(1降序11升序)、金额(2降序22升序)、点击率(3降序33升序)
	int time = 0;// 时间差
	private TextView tv_popular, tv_price, tv_sales, tv_mj, tv_j, tv_time,
			tv_zonghe;
	private ImageView iv_popular, iv_price, iv_sales;

	private ScrollView scrollView;

	private ImageView iv_headview;
	private ImageButton imbt;
	private TextView tv_head_title;
	private RelativeLayout rl_intruduce;

	private Boolean isShow = false;
	private xUtilsImageLoader bitmapUtils;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tzm_shop_detail_activity);
		LogUtil.Log(TAG, TAG + "-------------onCreate");
		activityId = getIntent().getStringExtra("activityId");
		name = getIntent().getStringExtra("name");
		bitmapUtils = new xUtilsImageLoader(this);
		bitmapUtils.configDefaultLoadFailedImage(R.drawable.loading_long);
		bitmapUtils.configDefaultLoadingImage(R.drawable.loading_long);
		initView();
		client = new ApiClient(this);
		tzmShopDetailApi = new TzmShopDetailApi();
		tzmShopDetailModel = new TzmShopDetailModel();

		pullToRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.ptrf_sv);

		pullToRefreshScrollView
				.setOnRefreshListener(new OnRefreshListener2<ScrollView>() {
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ScrollView> refreshView) {
						String label = DateUtils.formatDateTime(
								TzmShopDetailActivity.this
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

		gridView = (GridViewForScrollView) findViewById(R.id.tzm_shop_detail_list);
		scrollView = pullToRefreshScrollView.getRefreshableView();
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int positon,
					long id) {
				Product model = (TzmShopDetailModel.Product) arg0.getAdapter()
						.getItem(positon);
				Intent intent = new Intent(TzmShopDetailActivity.this,
						ProductDetailActivity.class);
				intent.putExtra("activityId", model.activityId);
				intent.putExtra("id", model.productId);
				startActivity(intent);
			}
		});
		gridView.setFocusable(false);
		checkLogin();
		// 判断收藏
		searhFavorite();
		// loadHeadView();

		loadData();
	}

	private void initView() {
		rl_intruduce = (RelativeLayout) findViewById(R.id.rl_intruduce);
		rl_intruduce.setOnClickListener(clickListener);
		iv_headview = (ImageView) findViewById(R.id.iv_headview);
		imbt = (ImageButton) findViewById(R.id.imbt);
		imbt.setOnClickListener(clickListener);
		tv_head_title = (TextView) findViewById(R.id.tv_head_title);
		text_head_title = (TextView) findViewById(R.id.text_head_title);
		tv_j = (TextView) findViewById(R.id.tv_j);
		tv_mj = (TextView) findViewById(R.id.tv_mj);
		tv_time = (TextView) findViewById(R.id.tv_time);
		text_head_title.setText(name);
		btn_head_left = (ImageView) findViewById(R.id.btn_head_left);
		btn_head_left.setOnClickListener(clickListener);
		collect = (ImageView) findViewById(R.id.collect);
		collect.setOnClickListener(clickListener);
		cancel_collect = (ImageView) findViewById(R.id.cancel_collect);
		cancel_collect.setOnClickListener(clickListener);
		tv_popular = (TextView) findViewById(R.id.tv_popular);
		tv_price = (TextView) findViewById(R.id.tv_price);
		tv_sales = (TextView) findViewById(R.id.tv_sales);
		iv_popular = (ImageView) findViewById(R.id.iv_popular);
		iv_price = (ImageView) findViewById(R.id.iv_price);
		iv_sales = (ImageView) findViewById(R.id.iv_sales);
		tv_zonghe = (TextView) findViewById(R.id.tv_zonghe);
		tv_popular.setOnClickListener(clickListener);
		iv_popular.setOnClickListener(clickListener);
		tv_sales.setOnClickListener(clickListener);
		iv_sales.setOnClickListener(clickListener);
		tv_price.setOnClickListener(clickListener);
		iv_price.setOnClickListener(clickListener);
		tv_zonghe.setOnClickListener(clickListener);

	}

	// 倒计时
	class TimeCount implements Runnable {
		@Override
		public void run() {
			while (!stopThread) {
				while (time > 0)// 整个倒计时执行的循环
				{
					time--;
					mHandler.post(new Runnable() // 通过它在UI主线程中修改显示的剩余时间
					{
						public void run() {
							tv_time.setText(getInterval(time));// 显示剩余时间
						}
					});
					try {
						Thread.sleep(1000);// 线程休眠一秒钟 这个就是倒计时的间隔时间
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			// 下面是倒计时结束逻辑
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					// timeCount.setText("设定的时间到。");
				}
			});
		}
	}

	// 两个时间差
	@SuppressLint("SimpleDateFormat")
	public static int getTimeInterval(String data, String data1) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		int interval = 0;
		try {

			Date currentTime = dateFormat.parse(data1);
			Date beginTime = dateFormat.parse(data);
			interval = (int) ((beginTime.getTime() - currentTime.getTime()) / (1000));// 时间差
																						// 单位秒
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return interval;
	}

	public static String getDateFormat(long time) {
		String date = time + "";
		if (time < 10) {
			date = "0" + date;
		}
		return date;
	}

	/**
	 * 设定显示文字
	 */
	public static String getInterval(int time) {
		String txt = null;
		if (time >= 0) {
			long day = time / (24 * 3600);// 天
			long hour = time % (24 * 3600) / 3600;// 小时
			long minute = time % 3600 / 60;// 分钟
			long second = time % 60;// 秒

			// txt ="剩" + day + "天" + hour + "小时" + minute + "分" + second
			// + "秒";
			if (day != 0) {
				txt = "剩" + day + "天 ";

			} else if (hour != 0) {
				txt = "剩" + getDateFormat(hour) + "小时";
			} else if (minute != 0) {
				txt = "剩" + getDateFormat(minute) + "分钟";
			} else if (second != 0) {
				txt = "剩" + getDateFormat(second) + "秒";
			}
		} else {
			txt = "活动结束";
		}
		return txt;
	}

	protected void loadData() {
		tzmShopDetailApi.setactivityId(Integer.parseInt(activityId));
		tzmShopDetailApi.setSource(source);
		tzmShopDetailApi.setPageNo(currentPage);
		client.api(this.tzmShopDetailApi, new ApiListener() {
			@Override
			public void onStart() {
				ProgressDialogUtil.openProgressDialog(
						TzmShopDetailActivity.this, "", "");
			}

			@Override
			public void onComplete(String jsonStr) {
				ProgressDialogUtil.closeProgressDialog();
				Gson gson = new Gson();
				Type type = new TypeToken<BaseModel<TzmShopDetailModel>>() {
				}.getType();
				BaseModel<TzmShopDetailModel> base = gson.fromJson(jsonStr,
						type);

				bitmapUtils.display(iv_headview, base.result.banner);
				tv_head_title.setText(base.result.introduction);
				switch (Integer.parseInt(base.result.discountType)) {
				case 1:

					tv_j.setVisibility(View.VISIBLE);
					tv_j.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.tam502));
					break;
				case 2:

					tv_j.setVisibility(View.VISIBLE);
					tv_j.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.tzm503));
					break;
				default:
					tv_j.setVisibility(View.GONE);
					break;
				}

				tv_mj.setText(base.result.discountText);
				try {

					time = getTimeInterval(base.result.endTime2,
							base.result.currentTime);
					new Thread(new TimeCount()).start();
				} catch (Exception e) {
					// TODO: handle exception
				}

				// new Thread(new MyThread()).start();
				if (isLoadMore) {
					// 加载更多
					listPosition = gridView.getCount();
					if (base.result.productList.size() != 0) {
						tzmShopDetailModel.productList
								.addAll(base.result.productList);
						// tzmShopDetailAdapter.setList(tzmShopDetailModel);
						tzmShopDetailAdapter.notifyDataSetChanged();
					} else {
						ToastUtils.showShortToast(TzmShopDetailActivity.this,
								R.string.msg_list_null);
					}
					pullToRefreshScrollView.onRefreshComplete();

				} else {
					// 刷新
					listPosition = 0;
					// tzmShopDetailModel.clear();
					if (base.result.productList.size() != 0) {
						tzmShopDetailModel.productList = base.result.productList;
					} else {
						// 返回数据为空
						ToastUtils.showShortToast(TzmShopDetailActivity.this,
								"专场已结束！");
					}
					initProductList();
				}

			}

			@Override
			public void onError(String error) {
				pullToRefreshScrollView.onRefreshComplete();
				ToastUtils.showShortToast(TzmShopDetailActivity.this,
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

	protected void initProductList() {
		if (this.tzmShopDetailModel.productList != null) {
			tzmShopDetailAdapter = new TzmShopDetailAdapter(
					TzmShopDetailActivity.this,
					this.tzmShopDetailModel.productList);
			gridView.setAdapter(tzmShopDetailAdapter);
			gridView.setSelection(listPosition);

			pullToRefreshScrollView.onRefreshComplete();
			scrollView.scrollTo(0, 0);
		}
	}

	OnClickListener clickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.rl_intruduce:
				if (isShow) {
					tv_head_title.setMaxLines(2);
					imbt.setBackgroundResource(R.drawable.tzm_269);
					isShow = false;
				} else {
					tv_head_title.setMaxLines(1000);
					imbt.setBackgroundResource(R.drawable.tzm_270);
					isShow = true;
				}
				break;
			case R.id.btn_head_left:
				onBackPressed();
				break;
			case R.id.collect:
				addFavorite(4);
				break;
			case R.id.cancel_collect:
				delFavorite();
				break;
			case R.id.tv_popular:
				handler.sendEmptyMessage(4);
				break;
			case R.id.iv_popular:
				handler.sendEmptyMessage(4);
				break;
			case R.id.tv_price:
				handler.sendEmptyMessage(5);
				break;
			case R.id.iv_price:
				handler.sendEmptyMessage(5);
				break;
			case R.id.tv_sales:
				handler.sendEmptyMessage(6);
				break;
			case R.id.iv_sales:
				handler.sendEmptyMessage(6);
				break;
			case R.id.tv_zonghe:
				handler.sendEmptyMessage(7);
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

	/*
	 * 判断收藏
	 */
	private void searhFavorite() {

		if (!isLogin) {
			return;
		}
		client3 = new ApiClient(this);
		searchFavoriteApi = new SearchFavoriteApi();
		// uid = userInfo.uid;
		searchFavoriteApi.setUid(userModel.uid);
		searchFavoriteApi.setFavType(4);
		searchFavoriteApi.setFavSenId(Integer.parseInt(activityId));
		searchFavoriteApi.setactivityId(Integer.parseInt(activityId));
		client3.api(searchFavoriteApi, new ApiListener() {

			@Override
			public void onStart() {
				// ProgressDialogUtil.openProgressDialog(B2bGoodsDetaliActivity.this,
				// "", "");
			}

			@Override
			public void onException(Exception e) {
				// ProgressDialogUtil.closeProgressDialog();
				LogUtil.ErrorLog(e);
			}

			@Override
			public void onError(String error) {
				// ProgressDialogUtil.closeProgressDialog();
				LogUtil.Log(error);
			}

			@Override
			public void onComplete(String jsonStr) {
				// ProgressDialogUtil.closeProgressDialog();
				if (StringUtils.isNotBlank(jsonStr)) {
					try {
						JSONObject jsonObject = new JSONObject(jsonStr);
						// boolean success = jsonObject.optBoolean("success");
						int result = jsonObject.optInt("result");
						// String error_msg = jsonObject.optString("error_msg");
						// if (success) {
						// im_del_collect.setVisibility(View.GONE);
						// im_collect.setVisibility(View.VISIBLE);
						// } else {
						// im_collect.setVisibility(View.GONE);
						// im_del_collect.setVisibility(View.VISIBLE);
						// }
						// ToastUtils.showShortToast(TzmProductDetailActivity.this,
						// error_msg);
						handler.sendEmptyMessage(result);

					} catch (JSONException e) {
						e.printStackTrace();
					}

				}

			}
		}, true);

	}

	/*
	 * 添加收藏
	 */
	protected void addFavorite(Integer favType) {

		if (!isLogin) {
			ToastUtils.showShortToast(this, "请先登录！");
			Intent intent = new Intent(new Intent(TzmShopDetailActivity.this,
					TzmLoginRegisterActivity.class));
			intent.putExtra("type", 0);
			startActivityForResult(intent, AppConfig.Login);
			return;
		}
		client = new ApiClient(this);
		addfavoritesApi = new AddfavoritesApi();
		addfavoritesApi.setUid(userModel.uid);
		addfavoritesApi.setFavSenId(Integer.parseInt(activityId));
		addfavoritesApi.setactivityId(Integer.parseInt(activityId));
		addfavoritesApi.setFavType(favType);
		client.api(addfavoritesApi, new ApiListener() {

			@Override
			public void onStart() {
				ProgressDialogUtil.openProgressDialog(
						TzmShopDetailActivity.this, "", "");
			}

			@Override
			public void onException(Exception e) {
				ProgressDialogUtil.closeProgressDialog();
				LogUtil.ErrorLog(e);
			}

			@Override
			public void onError(String error) {
				ProgressDialogUtil.closeProgressDialog();
				LogUtil.Log(error);
				ToastUtils.showShortToast(TzmShopDetailActivity.this, error);
			}

			@Override
			public void onComplete(String jsonStr) {
				ProgressDialogUtil.closeProgressDialog();
				if (StringUtils.isNotBlank(jsonStr)) {
					try {
						JSONObject jsonObject = new JSONObject(jsonStr);
						boolean success = jsonObject.optBoolean("success");
						int result = jsonObject.optInt("result");
						String error_msg = jsonObject.optString("error_msg");
						ToastUtils.showShortToast(TzmShopDetailActivity.this,
								error_msg);
						if (success && result == 1) {
							handler.sendEmptyMessage(0);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}

			}
		}, true);

	}

	/*
	 * 删除收藏
	 */
	protected void delFavorite() {
		delfavoriteApi = new DelSingleFavoriteApi();
		delfavoriteApi.setUid(userModel.uid);
		delfavoriteApi.setFavType(4);
		delfavoriteApi.setFavSenId(Integer.parseInt(activityId));
		delfavoriteApi.setactivityId(Integer.parseInt(activityId));
		client.api(delfavoriteApi, new ApiListener() {

			@Override
			public void onStart() {
				ProgressDialogUtil.openProgressDialog(
						TzmShopDetailActivity.this, "", "");
			}

			@Override
			public void onException(Exception e) {
				ProgressDialogUtil.closeProgressDialog();
				LogUtil.ErrorLog(e);
			}

			@Override
			public void onError(String error) {
				ProgressDialogUtil.closeProgressDialog();
				LogUtil.Log(error);
				ToastUtils.showShortToast(TzmShopDetailActivity.this, error);
			}

			@Override
			public void onComplete(String jsonStr) {
				ProgressDialogUtil.closeProgressDialog();
				if (StringUtils.isNotBlank(jsonStr)) {
					try {
						JSONObject jsonObject = new JSONObject(jsonStr);
						boolean success = jsonObject.optBoolean("success");
						int result = jsonObject.optInt("result");
						String error_msg = jsonObject.optString("error_msg");
						ToastUtils.showShortToast(TzmShopDetailActivity.this,
								error_msg);
						if (success) {
							handler.sendEmptyMessage(1);
						}

					} catch (JSONException e) {
						e.printStackTrace();
					}

				}

			}
		}, true);

	}

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
				tv_popular.setTextColor(getResources().getColor(
						R.color.common_head_background));
				tv_zonghe.setTextColor(getResources().getColor(
						R.color.base_tttt));
				tv_price.setTextColor(getResources()
						.getColor(R.color.base_tttt));
				tv_sales.setTextColor(getResources()
						.getColor(R.color.base_tttt));
				iv_price.setImageResource(R.drawable.tzm_266);
				iv_sales.setImageResource(R.drawable.tzm_266);
				if (source == 33) {
					source = 3;
					iv_popular.setImageResource(R.drawable.tzm_264);
				} else {
					source = 33;
					iv_popular.setImageResource(R.drawable.tzm_265);
				}
				isLoadMore = false;
				currentPage = 1;
				loadData();
				break;
			case 5:
				tv_price.setTextColor(getResources().getColor(
						R.color.common_head_background));
				tv_zonghe.setTextColor(getResources().getColor(
						R.color.base_tttt));
				tv_popular.setTextColor(getResources().getColor(
						R.color.base_tttt));
				tv_sales.setTextColor(getResources()
						.getColor(R.color.base_tttt));
				iv_popular.setImageResource(R.drawable.tzm_266);
				iv_sales.setImageResource(R.drawable.tzm_266);
				if (source == 22) {
					source = 2;
					iv_price.setImageResource(R.drawable.tzm_264);
				} else {
					source = 22;
					iv_price.setImageResource(R.drawable.tzm_265);
				}
				isLoadMore = false;
				currentPage = 1;
				loadData();
				break;
			case 6:
				tv_sales.setTextColor(getResources().getColor(
						R.color.common_head_background));
				tv_zonghe.setTextColor(getResources().getColor(
						R.color.base_tttt));
				tv_popular.setTextColor(getResources().getColor(
						R.color.base_tttt));
				tv_price.setTextColor(getResources()
						.getColor(R.color.base_tttt));
				iv_popular.setImageResource(R.drawable.tzm_266);
				iv_price.setImageResource(R.drawable.tzm_266);

				if (source == 11) {
					source = 1;
					iv_sales.setImageResource(R.drawable.tzm_264);
				} else {
					source = 11;
					iv_sales.setImageResource(R.drawable.tzm_265);
				}
				isLoadMore = false;
				currentPage = 1;
				loadData();
				break;
			case 7:
				tv_sales.setTextColor(getResources()
						.getColor(R.color.base_tttt));
				tv_zonghe.setTextColor(getResources().getColor(
						R.color.common_head_background));
				tv_popular.setTextColor(getResources().getColor(
						R.color.base_tttt));
				tv_price.setTextColor(getResources()
						.getColor(R.color.base_tttt));
				iv_popular.setImageResource(R.drawable.tzm_266);
				iv_price.setImageResource(R.drawable.tzm_266);

				iv_sales.setImageResource(R.drawable.tzm_266);

				isLoadMore = false;
				currentPage = 1;
				source = 0;
				loadData();
				break;
			}
		};
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			checkLogin();
			// 判断收藏
			searhFavorite();
		}
	};

	@Override
	protected void onDestroy() {
		handler.removeCallbacksAndMessages(null);
		stopThread = true;
		super.onDestroy();
	}
}
