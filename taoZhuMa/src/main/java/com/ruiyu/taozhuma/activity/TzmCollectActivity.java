package com.ruiyu.taozhuma.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tzm.pulltorefresh.library.PullToRefreshBase;
import com.tzm.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.tzm.pulltorefresh.library.PullToRefreshGridView;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.adapter.TzmCollectAdapter;
import com.ruiyu.taozhuma.adapter.TzmCollectPorductAdapter;
import com.ruiyu.taozhuma.api.ApiClient;
import com.ruiyu.taozhuma.api.ApiListener;
import com.ruiyu.taozhuma.api.TzmCollectApi;
import com.ruiyu.taozhuma.api.TzmDelfavoriteApi;
import com.ruiyu.taozhuma.base.xUtilsImageLoader;
import com.ruiyu.taozhuma.model.BaseModel;
import com.ruiyu.taozhuma.model.TzmCollectModel;
import com.ruiyu.taozhuma.model.UserModel;
import com.ruiyu.taozhuma.utils.LogUtil;
import com.ruiyu.taozhuma.utils.ProgressDialogUtil;
import com.ruiyu.taozhuma.utils.StringUtils;
import com.ruiyu.taozhuma.utils.ToastUtils;
import com.ruiyu.taozhuma.utils.UserInfoUtils;

public class TzmCollectActivity extends Activity {

	private TextView txt_head_title;
	private Button btn_head_left;
	private GridView gv_collect;
	// private ArrayList<String> list;
	private TextView tv_collect_shop;
	private TextView tv_collect_product;
	private TextView tv_content;
	private ImageView isPorductTure;// 选中宝贝收藏时的选中红线
	private ImageView isShopTure;// 选中店铺收藏时的选中红线

	// 列表
	private PullToRefreshGridView mPullRefreshGridView;
	private TzmCollectAdapter tzmCollectAdapter;// 店铺收藏Adapter
	private TzmCollectPorductAdapter tzmCollectPorductAdapter;// 宝贝收藏Adapter
	// 接口调用
	private ApiClient client;
	private TzmCollectApi tzmCollectApi;
	private List<TzmCollectModel> tzmCollectModel;
	private int currentPage = 1;// 当前页数
	private boolean isLoadMore = false;// 是否是加载更多
	private int listPosition = 0;
	private int status = 1;
	// 用户信息
	private Boolean isLogin;
	private UserModel userModel;

	// private ImageView imbt_head_right;

	private xUtilsImageLoader imageLoader;

	// 删除收藏
	private ApiClient client2;
	private TzmDelfavoriteApi delfavoriteApi;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tzm_collect_activity);
		checkLogin();
		txt_head_title = (TextView) findViewById(R.id.txt_head_title);
		txt_head_title.setText("我的关注");
		btn_head_left = (Button) findViewById(R.id.btn_head_left);
		btn_head_left.setOnClickListener(clickListener);// 回退图标事件侦听
		tv_collect_shop = (TextView) findViewById(R.id.tv_collect_shop);
		tv_collect_shop.setOnClickListener(clickListener);// 店铺收藏事件侦听
		tv_collect_product = (TextView) findViewById(R.id.tv_collect_product);
		tv_collect_product.setOnClickListener(clickListener);// 宝贝收藏事件侦听
		isPorductTure = (ImageView) findViewById(R.id.isPorductTure);
		isShopTure = (ImageView) findViewById(R.id.isShopTure);
		tv_content = (TextView) findViewById(R.id.tv_content);

		// imbt_head_right=(ImageView) findViewById(R.id.imbt_head_right);
		// if(isLogin&&userModel.type==6){
		// imbt_head_right.setVisibility(View.GONE);
		// }else{
		// imbt_head_right.setVisibility(View.VISIBLE);
		// imbt_head_right.setBackgroundResource(R.drawable.cart);
		// imbt_head_right.setOnClickListener(clickListener);
		// }
		imageLoader = new xUtilsImageLoader(this);
		client = new ApiClient(this);
		tzmCollectApi = new TzmCollectApi();
		tzmCollectModel = new ArrayList<TzmCollectModel>();

		mPullRefreshGridView = (PullToRefreshGridView) findViewById(R.id.tzm_collect_list);
		mPullRefreshGridView
				.setOnRefreshListener(new OnRefreshListener2<GridView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<GridView> refreshView) {
						String label = DateUtils.formatDateTime(
								TzmCollectActivity.this.getApplicationContext(),
								System.currentTimeMillis(),
								DateUtils.FORMAT_SHOW_TIME
										| DateUtils.FORMAT_SHOW_DATE
										| DateUtils.FORMAT_ABBREV_ALL);

						// Update the LastUpdatedLabel
						refreshView.getLoadingLayoutProxy()
								.setLastUpdatedLabel(label);
						isLoadMore = false;
						currentPage = 1;
						loading();
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<GridView> refreshView) {
						isLoadMore = true;
						currentPage++;
						loading();
					}
				});
		gv_collect = mPullRefreshGridView.getRefreshableView();
		gv_collect.setSelector(new ColorDrawable(Color.TRANSPARENT));
		gv_collect.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				if (status == 1) {
					if (tzmCollectModel.get(position).status == 1) {// 是否下架 0为下架
						Intent intent = new Intent(TzmCollectActivity.this,
								ProductDetailActivity.class);
						intent.putExtra("id",
								tzmCollectModel.get(position).favoriteId);

						TzmCollectActivity.this.startActivity(intent);
					}
				} else if (status == 2) {
					if (tzmCollectModel.get(position).status == 1) {
						Intent intent = new Intent(TzmCollectActivity.this,
								TzmShopDetailActivity.class);
						intent.putExtra("id",
								tzmCollectModel.get(position).favoriteId);
						intent.putExtra("shopName",
								tzmCollectModel.get(position).favName);
						TzmCollectActivity.this.startActivity(intent);
					}
				}
			}
		});

		gv_collect.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					final int position, long arg3) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						TzmCollectActivity.this);
				builder.setTitle("操作")
						.setPositiveButton("删除",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										deletaFavorite(position);
									}
								})
						.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										dialog.cancel();
									}
								});
				AlertDialog alert = builder.create();
				alert.show();
				return true;
			}
		});
		loading();
	}

	// 收藏宝贝Date
	protected void loadData() {
		if (!isLogin) {
			return;
		}
		tzmCollectApi = new TzmCollectApi();
		tzmCollectApi.setFavType(1);
		tzmCollectApi.setUid(userModel.uid);
		handler.sendEmptyMessage(1);// 设置是否选中
		tzmCollectApi.setPageNo(currentPage);
		client.api(this.tzmCollectApi, new ApiListener() {
			@Override
			public void onStart() {
				ProgressDialogUtil.openProgressDialog(TzmCollectActivity.this,
						"", "");
			}

			@Override
			public void onComplete(String jsonStr) {
				ProgressDialogUtil.closeProgressDialog();
				Gson gson = new Gson();
				Type type = new TypeToken<BaseModel<ArrayList<TzmCollectModel>>>() {
				}.getType();
				BaseModel<ArrayList<TzmCollectModel>> base = gson.fromJson(
						jsonStr, type);

				if (isLoadMore) {
					// 加载更多
					listPosition = gv_collect.getCount();
					if (base.result != null && base.result.size() > 0) {
						tzmCollectModel.addAll(base.result);
					} else {
						ToastUtils.showShortToast(TzmCollectActivity.this,
								R.string.msg_list_null);
					}

				} else {
					// 刷新
					listPosition = 0;
					tzmCollectModel.clear();
					if (base.result != null && base.result.size() > 0) {
						tzmCollectModel = base.result;
						tv_content.setVisibility(View.GONE);
					} else {
						// 返回数据为空
						tv_content.setVisibility(View.VISIBLE);
						ToastUtils.showShortToast(TzmCollectActivity.this,
								R.string.msg_list_null);
					}
				}

				initProductList();
			}

			@Override
			public void onError(String error) {
				mPullRefreshGridView.onRefreshComplete();
				ToastUtils.showShortToast(TzmCollectActivity.this,
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

	// 宝贝收藏List
	protected void initProductList() {
		if (this.tzmCollectModel != null) {
			tzmCollectPorductAdapter = new TzmCollectPorductAdapter(
					TzmCollectActivity.this, this.tzmCollectModel, imageLoader);
			gv_collect.setAdapter(tzmCollectPorductAdapter);
			gv_collect.setSelection(listPosition);
			mPullRefreshGridView.onRefreshComplete();
		}
	}

	// 收藏店铺Date
	protected void loadShopData() {
		if (!isLogin) {
			return;
		}
		tzmCollectApi = new TzmCollectApi();
		tzmCollectApi.setFavType(2);
		tzmCollectApi.setUid(userModel.uid);
		handler.sendEmptyMessage(2);// 设置是否选中
		tzmCollectApi.setPageNo(currentPage);
		client.api(this.tzmCollectApi, new ApiListener() {
			@Override
			public void onStart() {
				ProgressDialogUtil.openProgressDialog(TzmCollectActivity.this,
						"", "");
			}

			@Override
			public void onComplete(String jsonStr) {
				ProgressDialogUtil.closeProgressDialog();
				Gson gson = new Gson();
				Type type = new TypeToken<BaseModel<ArrayList<TzmCollectModel>>>() {
				}.getType();
				BaseModel<ArrayList<TzmCollectModel>> base = gson.fromJson(
						jsonStr, type);

				if (isLoadMore) {
					// 加载更多
					listPosition = gv_collect.getCount();
					if (base.result != null && base.result.size() > 0) {
						tzmCollectModel.addAll(base.result);
					} else {
						ToastUtils.showShortToast(TzmCollectActivity.this,
								R.string.msg_list_null);
					}

				} else {
					// 刷新
					listPosition = 0;
					tzmCollectModel.clear();
					if (base.result != null && base.result.size() > 0) {
						tzmCollectModel = base.result;
						tv_content.setVisibility(View.GONE);
					} else {
						// 返回数据为空
						tv_content.setVisibility(View.VISIBLE);
						ToastUtils.showShortToast(TzmCollectActivity.this,
								R.string.msg_list_null);
					}
				}

				initshopList();
			}

			@Override
			public void onError(String error) {
				mPullRefreshGridView.onRefreshComplete();
				ToastUtils.showShortToast(TzmCollectActivity.this,
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

	// 店铺收藏List
	protected void initshopList() {
		if (this.tzmCollectModel != null) {
			tzmCollectAdapter = new TzmCollectAdapter(TzmCollectActivity.this,
					this.tzmCollectModel, imageLoader);
			gv_collect.setAdapter(tzmCollectAdapter);
			gv_collect.setSelection(listPosition);
			mPullRefreshGridView.onRefreshComplete();
		}
	}

	// 判断加载宝贝收藏方法还是店铺收藏方法（1=》宝贝收藏，2=》店铺收藏）
	public void loading() {
		checkLogin();
		if (status == 1) {
			loadData();
		}
		if (status == 2) {
			loadShopData();
		}
	}

	OnClickListener clickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_head_left:
				onBackPressed();
				break;
			case R.id.tv_collect_shop:
				isLoadMore = false;
				currentPage = 1;
				status = 2;
				loading();
				break;
			case R.id.tv_collect_product:
				isLoadMore = false;
				currentPage = 1;
				status = 1;
				loading();
				break;
			// case R.id.imbt_head_right:
			// startActivity(new Intent(TzmCollectActivity.this,
			// TzmShopActivity.class));
			// break;
			}
		}
	};

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			// 判断是否选中
			// 选择收藏店铺
			if (msg.what == 2) {
				isShopTure.setVisibility(View.VISIBLE);
				isPorductTure.setVisibility(View.INVISIBLE);
			}
			// 选择收藏宝贝
			if (msg.what == 1) {
				isShopTure.setVisibility(View.INVISIBLE);
				isPorductTure.setVisibility(View.VISIBLE);
			}
		};
	};

	protected void deletaFavorite(final int position) {
		client2 = new ApiClient(TzmCollectActivity.this);
		delfavoriteApi = new TzmDelfavoriteApi();
		delfavoriteApi.setUid(userModel.uid);
		delfavoriteApi.setFavType(status);
		delfavoriteApi.setCids(tzmCollectModel.get(position).favoriteId + "");
		client2.api(delfavoriteApi, new ApiListener() {

			@Override
			public void onStart() {

			}

			@Override
			public void onError(String error) {
				mPullRefreshGridView.onRefreshComplete();
				ToastUtils.showShortToast(TzmCollectActivity.this,
						R.string.msg_list_null);
			}

			@Override
			public void onException(Exception e) {
				mPullRefreshGridView.onRefreshComplete();
				LogUtil.ErrorLog(e);
			}

			@Override
			public void onComplete(String jsonStr) {
				if (StringUtils.isNotBlank(jsonStr)) {
					try {
						JSONObject json = new JSONObject(jsonStr);
						ToastUtils.showShortToast(TzmCollectActivity.this,
								json.getString("error_msg"));
						if (json.getBoolean("success")) {
							tzmCollectModel.remove(position);
							if (tzmCollectModel.size() == 0) {
								tv_content.setVisibility(View.VISIBLE);
							} else {
								tv_content.setVisibility(View.GONE);
							}
							if (status == 1) {
								tzmCollectPorductAdapter.notifyDataSetChanged();
							}
							if (status == 2) {
								tzmCollectAdapter.notifyDataSetChanged();
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}, true);
	}

	private void checkLogin() {
		isLogin = UserInfoUtils.isLogin();
		if (isLogin) {
			userModel = UserInfoUtils.getUserInfo();
		}

	}

}