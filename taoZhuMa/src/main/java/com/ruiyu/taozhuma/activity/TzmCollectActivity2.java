package com.ruiyu.taozhuma.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class TzmCollectActivity2 extends Activity {
	private String TAG = "TzmCollectActivity2";
	private TextView txt_head_title;
	private Button btn_head_left, btn_head_right, btn_head_delete;
	private GridView gv_collect;
	private TextView tv_content;
	CheckBox cb_sp;
	// 列表
	private PullToRefreshGridView mPullRefreshGridView;
	private TzmCollectPorductAdapter tzmCollectPorductAdapter;// 宝贝收藏Adapter
	// 接口调用
	private ApiClient client;
	private TzmCollectApi tzmCollectApi;
	private List<TzmCollectModel> tzmCollectModel;
	private int currentPage = 1;// 当前页数
	private boolean isLoadMore = false;// 是否是加载更多

	private int listPosition = 0;
	public static boolean isBox = false;// 是否box显示
	// 用户信息
	private Boolean isLogin;
	private UserModel userModel;

	private xUtilsImageLoader imageLoader;

	// 删除收藏
	private ApiClient client2;
	private TzmDelfavoriteApi delfavoriteApi;

	/**
	 * 版本修改, 改为专场后去掉 品牌收藏
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tzm_collect_activity2);
		Log.i(TAG, "onCreate");
		isBox = false;
		txt_head_title = (TextView) findViewById(R.id.txt_head_title);
		txt_head_title.setText("商品收藏");
		btn_head_delete = (Button) findViewById(R.id.btn_head_delete);
		btn_head_delete.setOnClickListener(clickListener);
		btn_head_delete.setVisibility(View.GONE);
		btn_head_right = (Button) findViewById(R.id.btn_head_right);
		cb_sp = (CheckBox) findViewById(R.id.cb_sp);
		btn_head_right.setOnClickListener(clickListener);// 编辑事件侦听
		btn_head_right.setVisibility(View.VISIBLE);
		btn_head_right.setText("编辑");
		btn_head_left = (Button) findViewById(R.id.btn_head_left);
		btn_head_left.setOnClickListener(clickListener);// 回退图标事件侦听
		tv_content = (TextView) findViewById(R.id.tv_content);

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
								TzmCollectActivity2.this
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
		gv_collect = mPullRefreshGridView.getRefreshableView();
		gv_collect.setSelector(new ColorDrawable(Color.TRANSPARENT));
		gv_collect.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Intent intent = new Intent(TzmCollectActivity2.this,
						ProductDetailActivity.class);
				intent.putExtra("id", tzmCollectModel.get(position).favoriteId);
				intent.putExtra("activityId", tzmCollectModel.get(position).activityId);
				LogUtil.Log(TAG, "onCreate--onItemClick");
				LogUtil.Log(TAG, "activityId:"
						+ tzmCollectModel.get(position).activityId);
				LogUtil.Log(TAG, "favoriteId:"
						+ tzmCollectModel.get(position).favoriteId);
				TzmCollectActivity2.this.startActivity(intent);
			}
		});
		// 长按删除单个
		gv_collect.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					final int position, long arg3) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						TzmCollectActivity2.this);
				builder.setTitle("操作")
						.setPositiveButton("删除",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										// 删除单个
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
		checkLogin();
		loadData();
	}

	// 收藏宝贝Date
	protected void loadData() {
		if (!isLogin) {
			return;
		}
		tzmCollectApi = new TzmCollectApi();
		tzmCollectApi.setFavType(1);
		tzmCollectApi.setUid(userModel.uid);
		tzmCollectApi.setPageNo(currentPage);
		client.api(this.tzmCollectApi, new ApiListener() {
			@Override
			public void onStart() {
				ProgressDialogUtil.openProgressDialog(TzmCollectActivity2.this,
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
						ToastUtils.showShortToast(TzmCollectActivity2.this,
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
					}
				}

				initProductList();
			}

			@Override
			public void onError(String error) {
				mPullRefreshGridView.onRefreshComplete();
				ToastUtils.showShortToast(TzmCollectActivity2.this,
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
					TzmCollectActivity2.this, this.tzmCollectModel, imageLoader);
			gv_collect.setAdapter(tzmCollectPorductAdapter);
			gv_collect.setSelection(listPosition);
			mPullRefreshGridView.onRefreshComplete();
		}
	}

	// 多个删除
	OnClickListener clickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_head_left:
				onBackPressed();
				break;
			case R.id.btn_head_delete:
				// 删除算法最复杂,拿到checkBox选择寄存map
				/*
				 * Map<Integer, Boolean> map = tzmCollectPorductAdapter
				 * .getCheckMap(); // 获取当前的数据数量 int count =
				 * tzmCollectPorductAdapter.getCount(); // 进行遍历 int j = 0; //
				 * 删除情况判读的标志 ,在没有删除项目的情况下 ,j=0, 否则, j!=0 for (int i = 0; i <
				 * count; i++) { //
				 * 因为List的特性,删除了2个item,则3变成2,所以这里要进行这样的换算,才能拿到删除后真正的position int
				 * position = i - (count - tzmCollectPorductAdapter.getCount());
				 * if (map.get(i) != null && map.get(i)) { TzmCollectModel bean
				 * = (TzmCollectModel) tzmCollectPorductAdapter
				 * .getItem(position);
				 * 
				 * if (bean.isCanRemove()) {
				 * tzmCollectPorductAdapter.getCheckMap().remove(i); j++;
				 * deletaFavorite(position); } else { map.put(position, false);
				 * }
				 * 
				 * } } if (j == 0) { Toast.makeText(TzmCollectActivity2.this,
				 * ("请选择,再点击删除！"), Toast.LENGTH_SHORT).show(); } if (j != 0) {
				 * Toast.makeText(TzmCollectActivity2.this, ("删除成功"),
				 * Toast.LENGTH_SHORT).show(); }
				 */

				AlertDialog.Builder builder = new AlertDialog.Builder(
						TzmCollectActivity2.this);
				builder.setTitle("确定要删除多个吗?")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										StringBuilder stringBuilder = new StringBuilder();
										for (Map.Entry<TzmCollectModel, Integer> entry : tzmCollectPorductAdapter.final_choose
												.entrySet()) {
											stringBuilder.append(entry
													.getValue() + ",");
											LogUtil.Log(TAG,
													"最终被删除的对象对应的 favID:"
															+ stringBuilder);
											
										}
										deletaFavoriteMany(stringBuilder);

									}
								})
						.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										
										dialog.cancel();
										loadData();
									}
								});
				AlertDialog alert = builder.create();
				alert.show();

				break;
			case R.id.btn_head_right:

				if (!isBox) {
					isBox = true;
					btn_head_delete.setVisibility(View.VISIBLE);
					tzmCollectPorductAdapter.notifyDataSetChanged();
					btn_head_right.setText("完成");
				} else {
					isBox = false;
					btn_head_delete.setVisibility(View.GONE);
					tzmCollectPorductAdapter.notifyDataSetChanged();
					btn_head_right.setText("编辑");
				}
				break;

			}
		}
	};

	// 取消收藏,删除单个
	protected void deletaFavorite(final int position) {
		client2 = new ApiClient(TzmCollectActivity2.this);
		delfavoriteApi = new TzmDelfavoriteApi();
		delfavoriteApi.setUid(userModel.uid);
		delfavoriteApi.setFavType(1);
		delfavoriteApi.setCids(tzmCollectModel.get(position).favId + "");
		LogUtil.Log(
				TAG,
				"deletaFavorite--" + userModel.uid
						+ tzmCollectModel.get(position).favId);
		client2.api(delfavoriteApi, new ApiListener() {

			@Override
			public void onStart() {

			}

			@Override
			public void onError(String error) {
				mPullRefreshGridView.onRefreshComplete();
				ToastUtils.showShortToast(TzmCollectActivity2.this,
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

						if (json.getBoolean("success")) {
							tzmCollectModel.remove(position);
							if (tzmCollectModel.size() == 0) {
								tv_content.setVisibility(View.VISIBLE);
							} else {
								tv_content.setVisibility(View.GONE);
							}
							tzmCollectPorductAdapter.notifyDataSetChanged();
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

	// 删除多个
	protected void deletaFavoriteMany(StringBuilder stringBuilder) {
		client2 = new ApiClient(TzmCollectActivity2.this);
		delfavoriteApi = new TzmDelfavoriteApi();
		delfavoriteApi.setUid(userModel.uid);
		delfavoriteApi.setFavType(1);
		delfavoriteApi.setCids(stringBuilder.toString());
		client2.api(delfavoriteApi, new ApiListener() {

			@Override
			public void onStart() {

			}

			@Override
			public void onError(String error) {
				mPullRefreshGridView.onRefreshComplete();
				ToastUtils.showShortToast(TzmCollectActivity2.this,
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

						if (json.getBoolean("success")) {
							tzmCollectPorductAdapter.notifyDataSetChanged();
							ToastUtils.showShortToast(TzmCollectActivity2.this,
									"删除成功");
							loadData();
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}, true);
	}
}