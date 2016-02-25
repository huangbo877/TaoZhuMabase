package com.ruiyu.taozhuma.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.adapter.TzmConsumerProductAdapter;
import com.ruiyu.taozhuma.api.ApiClient;
import com.ruiyu.taozhuma.api.ApiListener;
import com.ruiyu.taozhuma.api.DelfavoriteApi;
import com.ruiyu.taozhuma.api.TzmCollectApi;
import com.ruiyu.taozhuma.model.BaseModel;
import com.ruiyu.taozhuma.model.TzmCollectModel;
import com.ruiyu.taozhuma.model.UserModel;
import com.ruiyu.taozhuma.utils.LogUtil;
import com.ruiyu.taozhuma.utils.ProgressDialogUtil;
import com.ruiyu.taozhuma.utils.StringUtils;
import com.ruiyu.taozhuma.utils.ToastUtils;
import com.ruiyu.taozhuma.utils.UserInfoUtils;

import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

public class TzmConsumerProductActivity extends Activity {

	private TextView txt_head_title;
	private Button btn_head_left, btn_head_right, btn_head_right_cancel,
			btn_head_right_delete;
	private GridView consumeProduct;
	private TextView tv_content;

	// 使用mPullRefreshGridView
	private PullToRefreshGridView mPullRefreshGridView;
	private int currentPage = 1;// 当前页数
	private boolean isLoadMore = false;// 是否是加载更多
	private int listPosition = 0;

	private ApiClient client, client2;
	private TzmCollectApi tzmCollectApi;
	private ArrayList<TzmCollectModel> tzmCollectModels;
	private TzmCollectModel tzmCollectModel;
	private TzmConsumerProductAdapter tzmConsumerProductAdapter;

	private DelfavoriteApi delfavoriteApi;

	// private ArrayList<Integer> cidsArr;
	private String cids = "";// 存储待删除的id

	// 用户信息
	private Boolean isLogin;
	private UserModel userModel;
	//
	public static HashMap<Integer, Boolean> isSelected;
	public static HashMap<Integer, Boolean> isClickable;
	private Boolean isVISIBLE = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tzm_consumer_product_activity);
		initView();

		isSelected = new HashMap<Integer, Boolean>();
		isClickable = new HashMap<Integer, Boolean>();
		client = new ApiClient(this);
		client2 = new ApiClient(this);
		tzmCollectApi = new TzmCollectApi();
		tzmCollectModels = new ArrayList<TzmCollectModel>();
		// cidsArr = new ArrayList<Integer>();
		checkLogin();
		mPullRefreshGridView = (PullToRefreshGridView) findViewById(R.id.gv_consume_product);
		mPullRefreshGridView
				.setOnRefreshListener(new OnRefreshListener2<GridView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<GridView> refreshView) {
						String label = DateUtils.formatDateTime(
								TzmConsumerProductActivity.this
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
				if (isVISIBLE) {
					if (isSelected.get(positon)) {
						isSelected.put(positon, false);
					} else {
						isSelected.put(positon, true);
					}

					tzmConsumerProductAdapter.setIsSelected(isSelected);
					tzmConsumerProductAdapter.notifyDataSetChanged();
				} else {
					if(isClickable.get(positon)){
						TzmCollectModel model = tzmCollectModels.get(positon);
						Intent intent = new Intent(TzmConsumerProductActivity.this,
								ProductDetailActivity.class);
						intent.putExtra("id", model.favoriteId);
						startActivity(intent);
					}
				}

			}
		});
		/***
		 * 暂时注销 consumeProduct.setOnItemClickListener(new OnItemClickListener()
		 * {
		 * 
		 * @Override public void onItemClick(AdapterView<?> parent, View view,
		 *           int position, long id) { // TODO Auto-generated method stub
		 *           btn_head_right.setVisibility(View.VISIBLE);//显示"确定"
		 *           btn_head_right_cancel.setVisibility(View.VISIBLE);//显示"取消"
		 *           tzmCollectModel = (TzmCollectModel)
		 *           parent.getItemAtPosition(position); boolean isClick =
		 *           TzmConsumerProductAdapter.getIsSelected().get(position);
		 *           if(isClick){
		 *           TzmConsumerProductAdapter.getIsSelected().put(position,
		 *           false); dataChanged(); }else{
		 *           TzmConsumerProductAdapter.getIsSelected().put(position,
		 *           true); dataChanged(); } int favId =
		 *           tzmCollectModel.favId;//获取收藏id
		 *           //ToastUtils.showShortToast(TzmConsumerProductActivity
		 *           .this,favId+"<fuck"); if(cidsArr.size()==0){
		 *           cidsArr.add(favId);//第一次为空; }else{ for(int
		 *           i=0;i<cidsArr.size();i++){
		 *           if(cidsArr.get(i).intValue()==favId){ return; } }
		 *           cidsArr.add(favId); return; } } });
		 */
		loadData();

	}

	private void initView() {
		txt_head_title = (TextView) findViewById(R.id.txt_head_title);
		txt_head_title.setText("我的分销商品库");
		// txt_head_title.layout(l, t, r, b);
		btn_head_left = (Button) findViewById(R.id.btn_head_left);
		btn_head_right = (Button) findViewById(R.id.btn_head_right);
		btn_head_right_cancel = (Button) findViewById(R.id.btn_head_right_cancel);
		btn_head_right.setOnClickListener(clickListener);
		btn_head_right_cancel.setOnClickListener(clickListener);
		btn_head_left.setOnClickListener(clickListener);
		btn_head_right_delete = (Button) findViewById(R.id.btn_head_right_delete);
		btn_head_right_delete.setOnClickListener(clickListener);
		tv_content=(TextView) findViewById(R.id.tv_content);
	}

	// 检查用户是否登陆
	private void checkLogin() {
		isLogin = UserInfoUtils.isLogin();
		if (isLogin) {
			userModel = UserInfoUtils.getUserInfo();
		}
	}

	// 通知Adapter刷新啦
	public void dataChanged() {
		consumeProduct.setAdapter(tzmConsumerProductAdapter);
		tzmConsumerProductAdapter.notifyDataSetChanged();
	}

	protected void loadData() {
		if (!isLogin) {
			return;
		}
		tzmCollectApi.setUid(userModel.uid);
		tzmCollectApi.setFavType(3);
		tzmCollectApi.setPageNo(currentPage);
		client.api(this.tzmCollectApi, new ApiListener() {

			@Override
			public void onStart() {
				ProgressDialogUtil.openProgressDialog(
						TzmConsumerProductActivity.this, "", "");
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
					listPosition = tzmCollectModels.size();
					if (base.result != null && base.result.size() > 0) {
						tzmCollectModels.addAll(base.result);
					} else {
						ToastUtils.showShortToast(
								TzmConsumerProductActivity.this,
								R.string.msg_list_null);
					}

				} else {
					// 刷新
					listPosition = 0;
					tzmCollectModels.clear();
					if (base.result != null && base.result.size() > 0) {
						tzmCollectModels = base.result;
						tv_content.setVisibility(View.GONE);
					} else {
						// 返回数据为空
						ToastUtils.showShortToast(
								TzmConsumerProductActivity.this,
								R.string.msg_list_null);
						tv_content.setVisibility(View.VISIBLE);
					}
				}
				initDistributionProductList();
			}

			@Override
			public void onError(String error) {
				mPullRefreshGridView.onRefreshComplete();
				ToastUtils.showShortToast(TzmConsumerProductActivity.this,
						R.string.msg_list_null);
			}

			@Override
			public void onException(Exception e) {
				mPullRefreshGridView.onRefreshComplete();
				LogUtil.ErrorLog(e);
			}
		}, true);

	}

	protected void initDistributionProductList() {
		if (this.tzmCollectModels != null) {
			initData();
			tzmConsumerProductAdapter = new TzmConsumerProductAdapter(
					TzmConsumerProductActivity.this, this.tzmCollectModels,
					isSelected,isVISIBLE);
			consumeProduct.setAdapter(tzmConsumerProductAdapter);
			consumeProduct.setSelection(listPosition);
			mPullRefreshGridView.onRefreshComplete();
		}
	}

	// 初始化isSelected的数据
	private void initData() {
		for (int i = 0; i < tzmCollectModels.size(); i++) {
			this.isSelected.put(i, false);
			System.out.println("tzmCollectModels.get(i).status    "+tzmCollectModels.get(i).status);
			if(tzmCollectModels.get(i).status==0){
				this.isClickable.put(i, false);//下架
			}else{
				this.isClickable.put(i, true);//非下架
			}
		}
	}

	// 删除分销产品
	public void delFavorite() {
		delfavoriteApi = new DelfavoriteApi();
		delfavoriteApi.setUid(userModel.uid);
		delfavoriteApi.setFavType(3);
		delfavoriteApi.setCids(cids);
		client.api(delfavoriteApi, new ApiListener() {

			@Override
			public void onStart() {
				ProgressDialogUtil.openProgressDialog(
						TzmConsumerProductActivity.this, "", "");
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
				ToastUtils.showShortToast(TzmConsumerProductActivity.this,
						error);
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
						ToastUtils.showShortToast(
								TzmConsumerProductActivity.this, error_msg);
						if (success) {
							loadData();
						}

					} catch (JSONException e) {
						e.printStackTrace();
					}

				}

			}
		}, true);

	}

	private void getCids() {
		if (isSelected.size() > 0) {
			for (int i = 0; i < tzmCollectModels.size(); i++) {
				if (isSelected.get(i)) {
					if (StringUtils.isEmpty(cids)) {
						cids = tzmCollectModels.get(i).favoriteId + "";
					} else {
						cids = cids + "," + tzmCollectModels.get(i).favoriteId;
					}
				}
			}
		}
	}

	OnClickListener clickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_head_left:
				onBackPressed();
				break;
			case R.id.btn_head_right_cancel:
				btn_head_right.setVisibility(View.VISIBLE);
				btn_head_right_delete.setVisibility(View.GONE);
				btn_head_right_cancel.setVisibility(View.GONE);
				isVISIBLE = false;
				tzmConsumerProductAdapter.setViewStatus(isVISIBLE);
				tzmConsumerProductAdapter.notifyDataSetChanged();
				break;
			case R.id.btn_head_right_delete:
				getCids();
				if (StringUtils.isEmpty(cids)) {
					ToastUtils.showShortToast(TzmConsumerProductActivity.this,
							"未勾选任何商品！");
					break;
				}
				isLoadMore =false;
				currentPage = 1;
				delFavorite();
				isVISIBLE = false;
				btn_head_right.setVisibility(View.VISIBLE);
				btn_head_right_delete.setVisibility(View.GONE);
				btn_head_right_cancel.setVisibility(View.GONE);
				cids = "";
				break;
			case R.id.btn_head_right:
				btn_head_right.setVisibility(View.GONE);
				btn_head_right_delete.setVisibility(View.VISIBLE);
				btn_head_right_cancel.setVisibility(View.VISIBLE);
				isVISIBLE = true;
				initData();
				tzmConsumerProductAdapter.setViewStatus(isVISIBLE);
				tzmConsumerProductAdapter.setIsSelected(isSelected);
				tzmConsumerProductAdapter.notifyDataSetChanged();

				// 确定删除
				// if (cidsArr != null) {
				// for (int index = 0; index < cidsArr.size(); index++) {
				// cids += cidsArr.get(index).intValue() + ",";
				// }
				// }
				// if (!cids.equals("")) {
				// cids = cids.substring(0, cids.length() - 1);// 去掉最后一个多余的","
				// }
				// ToastUtils.showShortToast(TzmConsumerProductActivity.this,
				// cids
				// + "<fuck>");
				// // delFavorite();
				// cidsArr.clear();// 每次点击确定删除之后清空
				// cids = "";// 每次点击确定删除之后清空
				break;
			}
		}
	};

}