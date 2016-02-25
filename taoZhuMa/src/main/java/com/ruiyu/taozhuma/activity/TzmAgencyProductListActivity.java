package com.ruiyu.taozhuma.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.adapter.TzmAgencyProductAdapter;
import com.ruiyu.taozhuma.api.AgencyProductsApi;
import com.ruiyu.taozhuma.api.ApiClient;
import com.ruiyu.taozhuma.api.ApiListener;
import com.ruiyu.taozhuma.api.UpdateAgencyInventoryApi;
import com.ruiyu.taozhuma.even.AgencyStockEven;
import com.ruiyu.taozhuma.model.AgencyProductsModel;
import com.ruiyu.taozhuma.model.BaseModel;
import com.ruiyu.taozhuma.model.UserModel;
import com.ruiyu.taozhuma.utils.LogUtil;
import com.ruiyu.taozhuma.utils.ProgressDialogUtil;
import com.ruiyu.taozhuma.utils.StringUtils;
import com.ruiyu.taozhuma.utils.ToastUtils;
import com.ruiyu.taozhuma.utils.UserInfoUtils;

import de.greenrobot.event.EventBus;

public class TzmAgencyProductListActivity extends Activity {
	private TextView txt_head_title;
	private Button btn_head_left;
	private RelativeLayout rl_bottom;
	private Button btn_sumbit;

	private ListView lv_order_list;
	private ArrayList<String> list;
	private TextView tv_content;

	private ApiClient apiClient;
	private AgencyProductsApi agencyProductsApi;
	private ArrayList<AgencyProductsModel> productsModels;
	private TzmAgencyProductAdapter productAdapter;

	private UpdateAgencyInventoryApi inventoryApi;
	private ApiClient apiClient2;

	// 使用PullToRefreshListView
	private PullToRefreshListView mPullRefreshListView;
	private int currentPage = 1;// 当前页数
	private boolean isLoadMore = false;// 是否是加载更多
	private int listPosition = 0;

	// 用户信息
	private Boolean isLogin;
	private UserModel userModel;

	// public static HashMap<Integer, Integer> number;
	private List<Map<String, String>> mData = new ArrayList<Map<String, String>>();
	private String etValue = "value";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.agency_product_activity);
		checkLogin();
		initView();
		// EventBus.getDefault().register(this);
		apiClient = new ApiClient(this);
		apiClient2 = new ApiClient(this);
		agencyProductsApi = new AgencyProductsApi();
		productsModels = new ArrayList<AgencyProductsModel>();

		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.lv_product_list);
		mPullRefreshListView
				.setOnRefreshListener(new OnRefreshListener2<ListView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// TODO 下拉刷新
						String label = DateUtils.formatDateTime(
								TzmAgencyProductListActivity.this
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
		lv_order_list = mPullRefreshListView.getRefreshableView();

		loadData();

	}

	private void initData() {

		for (AgencyProductsModel model : productsModels) {
			mData.add(new HashMap<String, String>());
		}

	}

	private void loadData() {
		checkLogin();
		agencyProductsApi.setUid(userModel.uid);
		agencyProductsApi.setPageNo(currentPage);

		apiClient.api(this.agencyProductsApi, new ApiListener() {
			@Override
			public void onStart() {
				ProgressDialogUtil.openProgressDialog(
						TzmAgencyProductListActivity.this, "", "");
			}

			@Override
			public void onComplete(String jsonStr) {
				ProgressDialogUtil.closeProgressDialog();
				Gson gson = new Gson();
				Type type = new TypeToken<BaseModel<ArrayList<AgencyProductsModel>>>() {
				}.getType();
				BaseModel<ArrayList<AgencyProductsModel>> base = gson.fromJson(
						jsonStr, type);
				if (isLoadMore) {
					// 加载更多
					listPosition = productsModels.size();
					if (base.result != null && base.result.size() > 0) {
						productsModels.addAll(base.result);
					} else {
						ToastUtils.showShortToast(
								TzmAgencyProductListActivity.this,
								R.string.msg_list_null);
					}

				} else {
					// 刷新
					listPosition = 0;
					productsModels.clear();
					if (base.result != null && base.result.size() > 0) {
						productsModels = base.result;
						tv_content.setVisibility(View.GONE);
						rl_bottom.setVisibility(View.VISIBLE);
					} else {
						tv_content.setVisibility(View.VISIBLE);
						rl_bottom.setVisibility(View.GONE);
					}
				}
				initOrderList();
			}

			@Override
			public void onError(String error) {
				ProgressDialogUtil.closeProgressDialog();
				mPullRefreshListView.onRefreshComplete();
				ToastUtils.showShortToast(TzmAgencyProductListActivity.this,
						error);
			}

			@Override
			public void onException(Exception e) {
				ProgressDialogUtil.closeProgressDialog();
				mPullRefreshListView.onRefreshComplete();
				LogUtil.ErrorLog(e);
			}
		}, true);

	}

	protected void initOrderList() {
		if (this.productsModels != null) {
			initData();
			for (int i = 0; i < this.productsModels.size(); i++) {// 库存数量初始化
			// number.put(i, productsModels.get(i).AgencyStock);
				int s = productsModels.get(i).AgencyStock;
				mData.get(i).put(etValue, s + "");
			}
			productAdapter = new TzmAgencyProductAdapter(
					TzmAgencyProductListActivity.this, this.productsModels,
					mData);
			lv_order_list.setAdapter(productAdapter);// 调用Adapter的getView()绘制item
			lv_order_list.setSelection(listPosition);
			mPullRefreshListView.onRefreshComplete();
		}
	}

	private void initView() {
		btn_head_left = (Button) this.findViewById(R.id.btn_head_left);
		btn_head_left.setOnClickListener(clickListener);
		txt_head_title = (TextView) this.findViewById(R.id.txt_head_title);
		txt_head_title.setText("产品库存管理");
		tv_content = (TextView) this.findViewById(R.id.tv_content);
		rl_bottom = (RelativeLayout) findViewById(R.id.rl_bottom);
		btn_sumbit = (Button) this.findViewById(R.id.btn_sumbit);
		btn_sumbit.setOnClickListener(clickListener);
		// number = new HashMap<Integer, Integer>();

	}

	// 检查用户是否登陆
	private void checkLogin() {
		isLogin = UserInfoUtils.isLogin();
		if (isLogin) {
			userModel = UserInfoUtils.getUserInfo();
		}
	}

	private void setInventory(String str) {
		inventoryApi = new UpdateAgencyInventoryApi();
		inventoryApi.setUid(userModel.uid);
		inventoryApi.setPidInventorys(str);
		apiClient2.api(inventoryApi, new ApiListener() {

			@Override
			public void onStart() {
				ProgressDialogUtil.openProgressDialog(
						TzmAgencyProductListActivity.this, "", "正在提交...");
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
				ToastUtils.showShortToast(TzmAgencyProductListActivity.this, error);
			}

			@Override
			public void onComplete(String jsonStr) {
				ProgressDialogUtil.closeProgressDialog();
				if (StringUtils.isNotBlank(jsonStr)) {
					try {
						JSONObject json = new JSONObject(jsonStr);
						ToastUtils.showShortToast(TzmAgencyProductListActivity.this,
								json.getString("error_msg"));
						if (json.getBoolean("success")) {
							onBackPressed();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				}

			}
		}, true);
	}

	// 遍历产品id 库存数量 拼凑字符串数组
	private String getString() {
		String str = "";
		str = productsModels.get(0).id + ":" + mData.get(0).get(etValue);
		for (int i = 1; i < productsModels.size(); i++) {
			str = str + "," + productsModels.get(i).id + ":"
					+ mData.get(i).get(etValue);
		}
		return str;

	}

	View.OnClickListener clickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_head_left:
				onBackPressed();
				break;
			case R.id.btn_sumbit:
				setInventory(getString());
				break;

			}

		}
	};

	@Override
	public void onDestroy() {
		super.onDestroy();
		// EventBus.getDefault().unregister(this);
	}
}
