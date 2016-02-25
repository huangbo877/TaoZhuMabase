package com.ruiyu.taozhuma.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.adapter.UserCouponlListAdapter;
import com.ruiyu.taozhuma.api.AddUserCouponApi;
import com.ruiyu.taozhuma.api.ApiClient;
import com.ruiyu.taozhuma.api.ApiListener;
import com.ruiyu.taozhuma.api.UserCouponListApi;
import com.ruiyu.taozhuma.api.ValidUserCouponApi;
import com.ruiyu.taozhuma.model.BaseModel;
import com.ruiyu.taozhuma.model.UserCouponListModel;
import com.ruiyu.taozhuma.model.UserModel;
import com.ruiyu.taozhuma.utils.LogUtil;
import com.ruiyu.taozhuma.utils.ProgressDialogUtil;
import com.ruiyu.taozhuma.utils.StringUtils;
import com.ruiyu.taozhuma.utils.ToastUtils;
import com.ruiyu.taozhuma.utils.UserInfoUtils;

/**
 * 优惠卷
 * 
 * @author Fu
 * 
 */
public class UserCouponListActivity extends Activity implements OnClickListener {

	@ViewInject(R.id.btn_head_left)
	private Button btn_head_left;
	@ViewInject(R.id.txt_head_title)
	private TextView txt_head_title;
	@ViewInject(R.id.btn_head_right)
	private Button btn_head_right;
	@ViewInject(R.id.et_add)
	private EditText et_add;
	@ViewInject(R.id.btn_add)
	private Button btn_add;
	private AddUserCouponApi addUserCouponApi;

	private Boolean isLogin;
	private UserModel userModel;

	private ApiClient apiClient;
	private UserCouponListApi couponListApi;
	private List<UserCouponListModel> list;
	private UserCouponlListAdapter adapter;

	private ListView listView;
	@ViewInject(R.id.prgl_coupon_list)
	private PullToRefreshListView pullToRefreshListView;
	@ViewInject(R.id.rl_empty)
	private RelativeLayout rl_empty;

	private boolean isLoadMore = false;
	private int currentPage = 1;

	private int tag = 1;// 1列表2选择可用列表
	// 订单可用优惠券列表
	private ValidUserCouponApi validUserCouponApi;
	private float price;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_coupon_list_activity);
		ViewUtils.inject(this);
		tag = getIntent().getIntExtra("tag", 1);
		price = getIntent().getFloatExtra("price", 0);
		checkLogin();
		initView();
		// 判断加载哪个列表
		if (tag == 1) {
			loadData();
		} else if (tag == 2) {
			loadValidData();
		}

	}

	/**
	 * 可用优惠卷列表
	 */
	private void loadValidData() {

		apiClient = new ApiClient(this);
		validUserCouponApi = new ValidUserCouponApi();
		validUserCouponApi.setUid(userModel.uid);
		validUserCouponApi.setPrice(price);
		apiClient.api(this.validUserCouponApi, new ApiListener() {
			@Override
			public void onStart() {
				ProgressDialogUtil.openProgressDialog(
						UserCouponListActivity.this, "", "");
			}

			@Override
			public void onComplete(String jsonStr) {
				ProgressDialogUtil.closeProgressDialog();
				pullToRefreshListView.onRefreshComplete();
				try {
					Gson gson = new Gson();
					Type type = new TypeToken<BaseModel<ArrayList<UserCouponListModel>>>() {
					}.getType();
					BaseModel<ArrayList<UserCouponListModel>> base = gson
							.fromJson(jsonStr, type);
					rl_empty.setVisibility(View.GONE);
					pullToRefreshListView.setVisibility(View.VISIBLE);
					listView.setVisibility(View.VISIBLE);
					if (base.result != null && base.result.size() > 0) {
						list = base.result;
					} else {
						// 返回数据为空
						ToastUtils.showShortToast(UserCouponListActivity.this,
								R.string.msg_list_null);
					}

					initList();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			@Override
			public void onError(String error) {
				pullToRefreshListView.onRefreshComplete();
				ProgressDialogUtil.closeProgressDialog();
				rl_empty.setVisibility(View.VISIBLE);
				pullToRefreshListView.setVisibility(View.GONE);

			}

			@Override
			public void onException(Exception e) {
				pullToRefreshListView.onRefreshComplete();
				ToastUtils.showShortToast(UserCouponListActivity.this,
						R.string.msg_network_anomaly);
				ProgressDialogUtil.closeProgressDialog();
				LogUtil.ErrorLog(e);
			}
		}, true);

	}

	private void initView() {
		btn_add.setOnClickListener(this);
		txt_head_title.setText("代金券");
		btn_head_left.setOnClickListener(this);
		btn_head_right.setOnClickListener(this);
		list = new ArrayList<UserCouponListModel>();
		if (tag == 1) {
			pullToRefreshListView.setMode(Mode.BOTH);
		} else if (tag == 2) {
			pullToRefreshListView.setMode(Mode.PULL_FROM_START);
		}

		pullToRefreshListView
				.setOnRefreshListener(new OnRefreshListener2<ListView>() {
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						String label = DateUtils.formatDateTime(
								UserCouponListActivity.this
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
						// 判断加载哪个列表
						if (tag == 1) {
							loadData();
						} else if (tag == 2) {
							loadValidData();
						}

					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						isLoadMore = true;
						currentPage++;
						loadData();
					}
				});
		listView = pullToRefreshListView.getRefreshableView();
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int positon,
					long id) {
				if (tag == 2) {
					UserCouponListModel model = (UserCouponListModel) arg0
							.getAdapter().getItem(positon);
					if (model.used == 0 && model.overdue == 0) {
						Intent intent = new Intent();
						intent.putExtra("model", model);
						setResult(RESULT_OK, intent);
						finish();
					}
				}

			}
		});

	}

	/**
	 * 代金卷列表
	 */
	protected void loadData() {
		apiClient = new ApiClient(this);
		couponListApi = new UserCouponListApi();
		couponListApi.setUid(userModel.uid);
		couponListApi.setPageNo(currentPage);
		apiClient.api(this.couponListApi, new ApiListener() {
			@Override
			public void onStart() {
				ProgressDialogUtil.openProgressDialog(
						UserCouponListActivity.this, "", "");
			}

			@Override
			public void onComplete(String jsonStr) {
				ProgressDialogUtil.closeProgressDialog();
				pullToRefreshListView.onRefreshComplete();
				try {
					Gson gson = new Gson();
					Type type = new TypeToken<BaseModel<ArrayList<UserCouponListModel>>>() {
					}.getType();
					BaseModel<ArrayList<UserCouponListModel>> base = gson
							.fromJson(jsonStr, type);

					if (isLoadMore) {
						// 加载更多
						if (base.result != null && base.result.size() > 0) {
							list.addAll(base.result);
							adapter.notifyDataSetChanged();
						} else {
							ToastUtils.showShortToast(
									UserCouponListActivity.this,
									R.string.msg_list_null);
						}

					} else {
						// 刷新
						list.clear();
						rl_empty.setVisibility(View.GONE);
						pullToRefreshListView.setVisibility(View.VISIBLE);
						listView.setVisibility(View.VISIBLE);
						if (base.result != null && base.result.size() > 0) {
							list = base.result;
						} else {
							// 返回数据为空
							ToastUtils.showShortToast(
									UserCouponListActivity.this,
									R.string.msg_list_null);
						}

						initList();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			@Override
			public void onError(String error) {
				pullToRefreshListView.onRefreshComplete();
				ProgressDialogUtil.closeProgressDialog();
				if (isLoadMore) {
					ToastUtils.showShortToast(UserCouponListActivity.this,
							R.string.msg_list_null);
				} else {
					rl_empty.setVisibility(View.VISIBLE);
					pullToRefreshListView.setVisibility(View.GONE);
				}

			}

			@Override
			public void onException(Exception e) {
				pullToRefreshListView.onRefreshComplete();
				ToastUtils.showShortToast(UserCouponListActivity.this,
						R.string.msg_network_anomaly);
				ProgressDialogUtil.closeProgressDialog();
				LogUtil.ErrorLog(e);
			}
		}, true);
	}

	protected void initList() {
		adapter = new UserCouponlListAdapter(UserCouponListActivity.this, list);
		listView.setAdapter(adapter);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_head_right:
			Intent intent = new Intent(UserCouponListActivity.this,
					CommonHelpDetailActivity.class);
			intent.putExtra("id", 29);
			startActivity(intent);
			break;
		case R.id.btn_head_left:
			onBackPressed();
			break;
		case R.id.btn_add:
			addUserCoupon();
			break;
		}
	}

	/**
	 * 添加代金卷
	 */
	private void addUserCoupon() {
		String string = et_add.getText().toString();
		if (StringUtils.isEmpty(string)) {
			ToastUtils.showShortToast(UserCouponListActivity.this, "请输入代金卷号码！");
			return;
		}
		apiClient = new ApiClient(this);
		addUserCouponApi = new AddUserCouponApi();
		addUserCouponApi.setUid(userModel.uid);
		addUserCouponApi.setCouponNo(string);
		apiClient.api(this.addUserCouponApi, new ApiListener() {
			@Override
			public void onStart() {
				ProgressDialogUtil.openProgressDialog(
						UserCouponListActivity.this, "", "");
			}

			@Override
			public void onComplete(String jsonStr) {
				ProgressDialogUtil.closeProgressDialog();
				if (StringUtils.isNotBlank(jsonStr)) {
					try {
						JSONObject jsonObject = new JSONObject(jsonStr);
						boolean success = jsonObject.optBoolean("success");
						String error_msg = jsonObject.optString("error_msg");
						ToastUtils.showShortToast(UserCouponListActivity.this,
								error_msg);
						if (success) {
							isLoadMore = false;
							currentPage = 1;
							if (tag == 1) {
								loadData();
							} else if (tag == 2) {
								loadValidData();
							}
						}

					} catch (JSONException e) {
						e.printStackTrace();
					}

				}
			}

			@Override
			public void onError(String error) {
				ToastUtils.showShortToast(UserCouponListActivity.this, error);
				ProgressDialogUtil.closeProgressDialog();
			}

			@Override
			public void onException(Exception e) {
				ProgressDialogUtil.closeProgressDialog();
				ToastUtils.showShortToast(UserCouponListActivity.this,
						R.string.msg_network_anomaly);
				LogUtil.ErrorLog(e);
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
