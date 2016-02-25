package com.ruiyu.taozhuma.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.adapter.SelectProductAdapter;
import com.ruiyu.taozhuma.api.AgencyProductIdsApi;
import com.ruiyu.taozhuma.api.AgencyProductListsApi;
import com.ruiyu.taozhuma.api.ApiClient;
import com.ruiyu.taozhuma.api.ApiListener;
import com.ruiyu.taozhuma.api.ToSetAgencyProductApi;
import com.ruiyu.taozhuma.dialog.CustomDialog.Builder;
import com.ruiyu.taozhuma.dialog.CustomDialog;
import com.ruiyu.taozhuma.dialog.CustomDialog2;
import com.ruiyu.taozhuma.model.AgencyProductIdsModel;
import com.ruiyu.taozhuma.model.AgencyProductListsModle;
import com.ruiyu.taozhuma.model.BaseModel;
import com.ruiyu.taozhuma.model.UserModel;
import com.ruiyu.taozhuma.utils.LogUtil;
import com.ruiyu.taozhuma.utils.ProgressDialogUtil;
import com.ruiyu.taozhuma.utils.StringUtils;
import com.ruiyu.taozhuma.utils.ToastUtils;
import com.ruiyu.taozhuma.utils.UserInfoUtils;

public class SelectProductActivity extends Activity {
	private TextView txt_head_title;
	private Button btn_head_left;

	private GridView consumeProduct;
	private TextView tv_content;

	private RelativeLayout rl_bottom;
//	private CheckBox cb_allcheck;
	private Button btn_sumbit,btn_selectAll,btn_unSelectAll;

	// 使用mPullRefreshGridView
	private PullToRefreshGridView mPullRefreshGridView;
	private int currentPage = 1;// 当前页数
	private boolean isLoadMore = false;// 是否是加载更多
	private int listPosition = 0;

	private AgencyProductListsApi agencyProductListsApi;
	private ApiClient client,client2,client3;
	private ToSetAgencyProductApi agencyProductApi;
	private AgencyProductIdsApi agencyProductIdsApi;
	
	private ArrayList<AgencyProductListsModle> productListsModles;
	private AgencyProductIdsModel productIdsModel;
	private AgencyProductListsModle productListsModle;
	private SelectProductAdapter selectProductAdapter;

	// 用户信息
	private Boolean isLogin;
	private UserModel userModel;

	public static HashMap<Integer, Boolean> isSelected;
	public static HashMap<Integer, Integer> isSelect;
//	private List<Integer> pids;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_product_activity);

		initView();
		isSelected = new HashMap<Integer, Boolean>();
		isSelect = new HashMap<Integer, Integer>();
//		pids = new ArrayList<Integer>();
		client = new ApiClient(this);
		client2 = new ApiClient(this);
		client3 = new ApiClient(this);
		agencyProductListsApi = new AgencyProductListsApi();
		productListsModles = new ArrayList<AgencyProductListsModle>();
		productIdsModel=new AgencyProductIdsModel();
		checkLogin();
		mPullRefreshGridView = (PullToRefreshGridView) findViewById(R.id.gv_select_product);
		mPullRefreshGridView
				.setOnRefreshListener(new OnRefreshListener2<GridView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<GridView> refreshView) {
						String label = DateUtils.formatDateTime(
								SelectProductActivity.this
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
							PullToRefreshBase<GridView> refreshView) {
						isLoadMore = true;
						currentPage++;
						loadData();

					}
				});
		consumeProduct = mPullRefreshGridView.getRefreshableView();
		consumeProduct.setSelector(new ColorDrawable(Color.TRANSPARENT));
		consumeProduct.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int positon,
					long id) {
				if (isSelected.get(positon)) {
					isSelected.put(positon, false);
					isSelect.put(positon, 0);
				} else {
					isSelected.put(positon, true);
					isSelect.put(positon, 1);
				}

				selectProductAdapter.setIsSelected(isSelected);
				selectProductAdapter.notifyDataSetChanged();
			}
		});
		loadData();

	}

	private void initView() {
		btn_head_left = (Button) this.findViewById(R.id.btn_head_left);
		btn_head_left.setOnClickListener(clickListener);
		txt_head_title = (TextView) this.findViewById(R.id.txt_head_title);
		txt_head_title.setText("勾选产品");
		tv_content = (TextView) findViewById(R.id.tv_content);
		rl_bottom = (RelativeLayout) findViewById(R.id.rl_bottom);
		btn_sumbit = (Button) findViewById(R.id.btn_sumbit);
		btn_sumbit.setOnClickListener(clickListener);
		btn_selectAll = (Button) findViewById(R.id.btn_selectAll);
		btn_selectAll.setOnClickListener(clickListener);
		btn_unSelectAll = (Button) findViewById(R.id.btn_unSelectAll);
		btn_unSelectAll.setOnClickListener(clickListener);
//		cb_allcheck = (CheckBox) findViewById(R.id.cb_allcheck);
//		cb_allcheck.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//			@Override
//			public void onCheckedChanged(CompoundButton compoundButton,
//					boolean b) {
//				if (b) {// 全选
//					for (int i = 0; i < productListsModles.size(); i++) {
//						isSelected.put(i, true);
//						selectProductAdapter.setIsSelected(isSelected);
//					}
//					selectProductAdapter.notifyDataSetChanged();
//				} else {// 取消全选
//					for (int i = 0; i < productListsModles.size(); i++) {
//						isSelected.put(i, false);
//						selectProductAdapter.setIsSelected(isSelected);
//					}
//					selectProductAdapter.notifyDataSetChanged();
//				}
//			}
//		});
	}

	View.OnClickListener clickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_head_left:
				onBackPressed();
				break;
			case R.id.btn_sumbit:
//				getPids();
//				productListsModles.get(i).id
				if(productListsModles.size()>0){
					String str_pid = productListsModles.get(0).id +":"+isSelect.get(0)+"";
					for (int i = 1; i < productListsModles.size(); i++) {
						str_pid =str_pid+","+productListsModles.get(i).id +":"+isSelect.get(i);
					}
					setAgencyProduct(str_pid);
				}else{
					ToastUtils.showShortToast(SelectProductActivity.this, "您还未选任何商品");
				}
				break;
			case R.id.btn_selectAll:
				dialog("您确定要全选吗？",R.id.btn_selectAll);
				break;
			case R.id.btn_unSelectAll:
				dialog("您确定取消全选吗？",R.id.btn_unSelectAll);
				break;
			}

		}
	};

	// 检查用户是否登陆
	private void checkLogin() {
		isLogin = UserInfoUtils.isLogin();
		if (isLogin) {
			userModel = UserInfoUtils.getUserInfo();
		}
	}

	protected void loadData() {
		if (!isLogin) {
			return;
		}
		agencyProductListsApi.setUid(userModel.uid);
		agencyProductListsApi.setPageNo(currentPage);
		client.api(this.agencyProductListsApi, new ApiListener() {

			@Override
			public void onStart() {
				ProgressDialogUtil.openProgressDialog(
						SelectProductActivity.this, "", "");
			}

			@Override
			public void onComplete(String jsonStr) {
				ProgressDialogUtil.closeProgressDialog();
				Gson gson = new Gson();
				Type type = new TypeToken<BaseModel<ArrayList<AgencyProductListsModle>>>() {
				}.getType();
				BaseModel<ArrayList<AgencyProductListsModle>> base = gson
						.fromJson(jsonStr, type);
				if (isLoadMore) {
					// 加载更多
					listPosition = productListsModles.size();
					if (base.result != null && base.result.size() > 0) {
						productListsModles.addAll(base.result);
					} else {
						ToastUtils.showShortToast(SelectProductActivity.this,
								R.string.msg_list_null);
					}

				} else {
					// 刷新
					listPosition = 0;
					productListsModles.clear();
					if (base.result != null && base.result.size() > 0) {
						productListsModles = base.result;
						tv_content.setVisibility(View.GONE);
						rl_bottom.setVisibility(View.VISIBLE);
					} else {
						// 返回数据为空
						ToastUtils.showShortToast(SelectProductActivity.this,
								R.string.msg_list_null);
						tv_content.setVisibility(View.VISIBLE);
						rl_bottom.setVisibility(View.GONE);
					}
				}
				initProductList();
			}

			@Override
			public void onError(String error) {
				ProgressDialogUtil.closeProgressDialog();
				mPullRefreshGridView.onRefreshComplete();
				ToastUtils.showShortToast(SelectProductActivity.this,
						R.string.msg_list_null);
			}

			@Override
			public void onException(Exception e) {
				ProgressDialogUtil.closeProgressDialog();
				mPullRefreshGridView.onRefreshComplete();
				LogUtil.ErrorLog(e);
			}
		}, true);

	}

	protected void initProductList() {
		if (this.productListsModles != null) {
			initData();
			
			selectProductAdapter = new SelectProductAdapter(
					SelectProductActivity.this, this.productListsModles,
					isSelected);
			consumeProduct.setAdapter(selectProductAdapter);
			consumeProduct.setSelection(listPosition);
			mPullRefreshGridView.onRefreshComplete();
		}
	}

	// 初始化isSelected的数据
	private void initData() {
		for (int i = 0; i < productListsModles.size(); i++) {
			if (productListsModles.get(i).checked==1) {
				this.isSelect.put(i, 1);
				this.isSelected.put(i, true);
			} else {
				this.isSelect.put(i, 0);
				this.isSelected.put(i, false);
			}
		}
	}
	
	//遍历产品ID
//	private void getPids(){
//		pids.clear();
//		for (int i = 0; i < productListsModles.size(); i++) {
//			boolean isClick = selectProductAdapter.getIsSelected().get(i);
//			if(isClick){
//				pids.add(productListsModles.get(i).id);
//			}
//		}
//	}
	
	private void getAgencyProductIds() {
		agencyProductIdsApi=new AgencyProductIdsApi();
		agencyProductIdsApi.setUid(productListsModles.get(0).manufacturerUid);
		client3.api(agencyProductIdsApi, new ApiListener() {
			
			@Override
			public void onStart() {
			}
			@Override
			public void onError(String error) {
				super.onError(error);
			}
			@Override
			public void onException(Exception e) {
				super.onException(e);
			}
			@Override
			public void onComplete(String jsonStr) {
				Gson gson = new Gson();
				Type type = new TypeToken<BaseModel<AgencyProductIdsModel>>() {
				}.getType();
				BaseModel<AgencyProductIdsModel> base = gson.fromJson(jsonStr,
						type);
				if (base.success) {
					productIdsModel=base.result;
					String[] list = productIdsModel.ids.split(",");
					String str_pid =list[0] +":"+1+"";
					for (int i = 1; i < list.length; i++) {
						str_pid =str_pid+","+list[i] +":"+1;
					}
					System.out.println(str_pid);
					setAgencyProduct(str_pid);
//					setAgencyProduct(productIdsModel.ids);
				}
			}
		}, true);
	}
	
	private void setAgencyProduct(String str) {
		agencyProductApi=new ToSetAgencyProductApi();
		agencyProductApi.setAgencyId(productListsModles.get(0).agencyId);
		agencyProductApi.setPids(str);
		client2.api(agencyProductApi, new ApiListener() {
			
			@Override
			public void onStart() {
				ProgressDialogUtil.openProgressDialog(
						SelectProductActivity.this, "", "正在提交...");
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
				ToastUtils.showShortToast(SelectProductActivity.this, error);
			}

			@Override
			public void onComplete(String jsonStr) {
				ProgressDialogUtil.closeProgressDialog();
				if (StringUtils.isNotBlank(jsonStr)) {
					try {
						JSONObject json = new JSONObject(jsonStr);
						ToastUtils.showShortToast(SelectProductActivity.this,
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
	
	private void dialog(String title,final Integer type) {
//		CustomDialog.Builder builder = new Builder(getActivity());
		CustomDialog2.Builder builder = new CustomDialog2.Builder(SelectProductActivity.this,title);
		builder.setPositiveClickListener(new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				if(type==R.id.btn_selectAll){
					if(productListsModles.size()>0){
						getAgencyProductIds();
					}
					dialog.dismiss();
				}else if(type==R.id.btn_unSelectAll){
					String str_pid = productListsModles.get(0).id +":"+0+"";
					for (int i = 1; i < productListsModles.size(); i++) {
						str_pid =str_pid+","+productListsModles.get(i).id +":"+0;
					}
					setAgencyProduct(str_pid);
					dialog.dismiss();
				}
				dialog.dismiss();
			}
		});
		builder.setNegativeClickListener(new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}
	
}
