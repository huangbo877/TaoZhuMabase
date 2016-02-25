/**
 * 
 */
package com.ruiyu.taozhuma.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.adapter.TzmSpecialColletionAdapter;
import com.ruiyu.taozhuma.api.ApiClient;
import com.ruiyu.taozhuma.api.ApiListener;
import com.ruiyu.taozhuma.api.TzmCollectApi;
import com.ruiyu.taozhuma.api.TzmDelfavoriteApi;
import com.ruiyu.taozhuma.model.BaseModel;
import com.ruiyu.taozhuma.model.TzmCollectModel;
import com.ruiyu.taozhuma.model.UserModel;
import com.ruiyu.taozhuma.utils.LogUtil;
import com.ruiyu.taozhuma.utils.ProgressDialogUtil;
import com.ruiyu.taozhuma.utils.StringUtils;
import com.ruiyu.taozhuma.utils.ToastUtils;
import com.ruiyu.taozhuma.utils.UserInfoUtils;

/**
 * @author 林尧 2015-12-1 专场收藏
 */
public class TzmSpecialCollectionActivity extends Activity implements
		OnClickListener {

	private String TAG = "TzmSpecialCollectionActivity";
	// 返回
	@ViewInject(R.id.btn_head_left_about_special_colleciton)
	private ImageView btn_head_left_about_special_colleciton;
	// 删除
	@ViewInject(R.id.tv_delete_special_collection)
	private TextView tv_delete_special_collection;
	// 编辑,完成
	@ViewInject(R.id.tv_edit_special_collection)
	private TextView tv_edit_special_collection;

	// 下拉刷新
	@ViewInject(R.id.lv_special_collection_list)
	private PullToRefreshListView lv_special_collection_list;
	private int currentPage = 1;// 当前页数
	private boolean isLoadMore = false;// 是否是加载更多
	private int listPosition = 0;
	private ListView lv_special_collection;

	// 适配器 ---专场收藏适配器
	private TzmSpecialColletionAdapter tzmSpecialColletionAdapter;
	private List<TzmCollectModel> tzmCollectModel;
	// 是否box显示 --用于多个删除
	public static boolean isBox = false;
	//
	// public static List<Map<Object, Object>> delobj = new
	// ArrayList<Map<Object, Object>>(); //存放被选择的子项
	private ApiClient client;
	private TzmCollectApi tzmCollectApi;

	// 用户信息
	private Boolean isLogin;
	private UserModel userModel;

	public static String str2=new String();
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.tzm_special_collection);
		ViewUtils.inject(this);
		LogUtil.Log(TAG, "onCreate");
		checkLogin();
		client = new ApiClient(this);
		isBox = false;
		tzmCollectModel = new ArrayList<TzmCollectModel>();
		tv_delete_special_collection.setVisibility(View.GONE);

		tv_delete_special_collection.setOnClickListener(this);
		tv_edit_special_collection.setOnClickListener(this);
		btn_head_left_about_special_colleciton.setOnClickListener(this);

		initSpecialCollection();
	}

	// 初始化 PullToRefreshListView 控件
	private void initSpecialCollection() {

		lv_special_collection_list
				.setOnRefreshListener(new OnRefreshListener2<ListView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// TODO 下拉刷新
						String label = DateUtils.formatDateTime(
								TzmSpecialCollectionActivity.this
										.getApplicationContext(), System
										.currentTimeMillis(),
								DateUtils.FORMAT_SHOW_TIME
										| DateUtils.FORMAT_SHOW_DATE
										| DateUtils.FORMAT_ABBREV_ALL);

						refreshView.getLoadingLayoutProxy()
								.setLastUpdatedLabel(label);

						isLoadMore = false;
						currentPage = 1;
						loadData();
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						isLoadMore = true;
						currentPage++;
						loadData();
					}
				});
		lv_special_collection = lv_special_collection_list.getRefreshableView();
		lv_special_collection.setSelector(new ColorDrawable(Color.TRANSPARENT));
		loadData();
	}

	// 专场收藏 网络请求数据
	protected void loadData() {

		if (!isLogin) {
			return;
		}

		tzmCollectApi = new TzmCollectApi();
		tzmCollectApi.setFavType(4);
		tzmCollectApi.setUid(userModel.uid);
		// handler.sendEmptyMessage(1);// 设置是否选中
		tzmCollectApi.setPageNo(currentPage);
		client.api(this.tzmCollectApi, new ApiListener() {
			@Override
			public void onStart() {
				ProgressDialogUtil.openProgressDialog(
						TzmSpecialCollectionActivity.this, "", "");
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
					listPosition = tzmCollectModel.size();
					if (base.result != null && base.result.size() > 0) {
						tzmCollectModel.addAll(base.result);
					} else {
						ToastUtils.showShortToast(
								TzmSpecialCollectionActivity.this,
								R.string.msg_list_null);
					}
				}
				if (!isLoadMore) {
					// 刷新
					listPosition = 0;
					tzmCollectModel.clear();
					if (base.result != null && base.result.size() > 0) {
						tzmCollectModel = base.result;
					}
				}
				// 填充数据 到适配器
				initSpecialCollectionList();

			}

			@Override
			public void onError(String error) {
				lv_special_collection_list.onRefreshComplete();
				ToastUtils.showShortToast(TzmSpecialCollectionActivity.this,
						R.string.msg_list_null);
				ProgressDialogUtil.closeProgressDialog();
			}

			@Override
			public void onException(Exception e) {
				lv_special_collection_list.onRefreshComplete();
				ProgressDialogUtil.closeProgressDialog();
				LogUtil.ErrorLog(e);
			}
		}, true);
	}

	// 专场收藏 适配器的填充
	protected void initSpecialCollectionList() {
		if (this.tzmCollectModel != null) {

			tzmSpecialColletionAdapter = new TzmSpecialColletionAdapter(
					TzmSpecialCollectionActivity.this, this.tzmCollectModel);
			lv_special_collection.setAdapter(tzmSpecialColletionAdapter);
			lv_special_collection.setSelection(listPosition);
			lv_special_collection_list.onRefreshComplete();
		}
	}

	// onRestart
	// 当处于非栈顶状态的活动需要再次返回栈顶，展现给用户的时候，触发该方法。
	// 也就是说执行了onStop()且没有执行onDestroy()的Activity被重新激活时，就会调用onRestart()方法。
/*	protected void onRestart() {
		super.onRestart();
		currentPage = 1;
		isLoadMore = false;
		loadData();
	};*/

	private void checkLogin() {
		isLogin = UserInfoUtils.isLogin();
		if (isLogin) {
			userModel = UserInfoUtils.getUserInfo();
		}

	}

	// 给头部的控件 添加 事件
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 返回
		case R.id.btn_head_left_about_special_colleciton:
			onBackPressed();
			break;
		// 删除
		case R.id.tv_delete_special_collection:
			tzmSpecialColletionAdapter.ClickResult(this);
			break;
		// 编辑/完成
		case R.id.tv_edit_special_collection:
			if (!isBox) {
				isBox = true;
				tv_delete_special_collection.setVisibility(View.VISIBLE);
				tzmSpecialColletionAdapter.notifyDataSetChanged();
				tv_edit_special_collection.setText("完成");

			} else {
				isBox = false;
				tv_delete_special_collection.setVisibility(View.GONE);
				tzmSpecialColletionAdapter.notifyDataSetChanged();
				tv_edit_special_collection.setText("编辑");
			}

			break;

		default:
			break;
		}

	}

	// 多个删除
	public  void deletaFavorite(StringBuilder str) {

		
		// 实际删除
		TzmDelfavoriteApi delfavoriteApi = new TzmDelfavoriteApi();
		ApiClient client2 = new ApiClient(TzmSpecialCollectionActivity.this);
		delfavoriteApi.setUid(userModel.uid);
		delfavoriteApi.setFavType(4);
		delfavoriteApi.setCids(str.toString());
		client2.api(delfavoriteApi, new ApiListener() {

			@Override
			public void onStart() {

			}

			@Override
			public void onError(String error) {
				lv_special_collection_list.onRefreshComplete();
				ToastUtils.showShortToast(TzmSpecialCollectionActivity.this,
						R.string.msg_list_null);
			}

			@Override
			public void onException(Exception e) {
				lv_special_collection_list.onRefreshComplete();
				LogUtil.ErrorLog(e);
			}

			@Override
			public void onComplete(String jsonStr) {
				if (StringUtils.isNotBlank(jsonStr)) {
					try {
						JSONObject json = new JSONObject(jsonStr);
						if (json.getBoolean("success")) {

							tzmSpecialColletionAdapter.notifyDataSetChanged();
							Map<Integer, Boolean> map = tzmSpecialColletionAdapter.getCheckMap();
							int count = tzmSpecialColletionAdapter.getCount();
							int position;
							// 表面删除
							for (int i = 0; i < count; i++) {
								// 因为List的特性,删除了2个item,则3变成2,所以这里要进行这样的换算,才能拿到删除后真正的position
								position = i - (count - tzmSpecialColletionAdapter.getCount());
								if (map.get(i) != null && map.get(i)) {
									TzmCollectModel bean = (TzmCollectModel) tzmSpecialColletionAdapter
											.getItem(position);
									if (bean.isCanRemove()) {
										tzmSpecialColletionAdapter.getCheckMap().remove(i);
										tzmSpecialColletionAdapter.remove(position);
										
									} else {
										map.put(position, false);
									}

								}
							}
							ToastUtils.showShortToast(TzmSpecialCollectionActivity.this,
									"删除成功");
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}, true);
	}
}
