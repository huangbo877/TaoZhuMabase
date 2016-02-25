package com.ruiyu.taozhuma.fragment;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.activity.TzmSelctAddressActivity;
import com.ruiyu.taozhuma.adapter.TzmCartListAdapter;
import com.ruiyu.taozhuma.api.ApiClient;
import com.ruiyu.taozhuma.api.ApiListener;
import com.ruiyu.taozhuma.api.EditCartApi;
import com.ruiyu.taozhuma.api.TzmCartListApi;
import com.ruiyu.taozhuma.even.CartEven;
import com.ruiyu.taozhuma.model.BaseModel;
import com.ruiyu.taozhuma.model.TzmMyCartModel;
import com.ruiyu.taozhuma.model.UserModel;
import com.ruiyu.taozhuma.utils.LogUtil;
import com.ruiyu.taozhuma.utils.ProgressDialogUtil;
import com.ruiyu.taozhuma.utils.StringUtils;
import com.ruiyu.taozhuma.utils.ToastUtils;
import com.ruiyu.taozhuma.utils.UserInfoUtils;

import de.greenrobot.event.EventBus;

/**
 * 店铺
 * 
 * @author wen
 * 
 */
public class ShopFragment extends Fragment {

	private RelativeLayout rl_bottom, rl_bg;
	private ListView lv_cart_list;

	// 接口调用
	private ApiClient apiClient, apiClient2;
	private TzmCartListApi api;
	private List<TzmMyCartModel> cartModels;
	private TzmCartListAdapter adapter;
	private EditCartApi editCartApi;
	// 用户信息
	private Boolean isLogin;
	private UserModel userModel;

	public DbUtils dbUtils;
	private List<TzmMyCartModel> list;

	private TextView tv_holeprice;
	private DecimalFormat df;
	private CheckBox cb_allcheck;
	private Button btn_sumbit, btn_guang;
	private String cid;

	private Integer status;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// ProgressDialogUtil.closeProgressDialog();
		// LogUtil.Log("onCreateView");
		// ToastUtils.showShortToast(getActivity(), "onCreateView");
		View view = inflater.inflate(R.layout.tzm_shop_activity, null);
//		btn_guang = (Button) view.findViewById(R.id.btn_guang);
		rl_bg = (RelativeLayout) view.findViewById(R.id.rl_bg);
		rl_bottom = (RelativeLayout) view.findViewById(R.id.rl_bottom);
		lv_cart_list = (ListView) view.findViewById(R.id.lv_cart_list);
		tv_holeprice = (TextView) view.findViewById(R.id.tv_holeprice);
//		rl_allchcek = (RelativeLayout) view.findViewById(R.id.rl_allchcek);
		cb_allcheck = (CheckBox) view.findViewById(R.id.cb_allcheck);
		btn_sumbit = (Button) view.findViewById(R.id.btn_sumbit);
//		btn_guang.setOnClickListener(clickListener);
		df = new DecimalFormat("#.#");
		dbUtils = DbUtils.create(getActivity());
		checkLogin();
		initView();
		EventBus.getDefault().register(this);
		return view;

	}

	public void onEventMainThread(CartEven event) {
		getTotalMoney();
	}

	private void loadData() {
		checkLogin();
		if (!isLogin) {
			rl_bg.setVisibility(View.VISIBLE);
			rl_bottom.setVisibility(View.GONE);
//			rl_allchcek.setVisibility(View.GONE);
			lv_cart_list.setVisibility(View.GONE);
			return;
		}
		apiClient = new ApiClient(getActivity());
		api = new TzmCartListApi();
		cartModels = new ArrayList<TzmMyCartModel>();
		api.setUid(userModel.uid);
		// api.setUid(2);

		apiClient.api(this.api, new ApiListener() {
			@Override
			public void onStart() {
			}

			@Override
			public void onComplete(String jsonStr) {
				Gson gson = new Gson();
				Type type = new TypeToken<BaseModel<ArrayList<TzmMyCartModel>>>() {
				}.getType();
				BaseModel<ArrayList<TzmMyCartModel>> base = gson.fromJson(
						jsonStr, type);

				if (base.result != null && base.result.size() > 0) {
					cartModels = base.result;
					rl_bg.setVisibility(View.GONE);
					rl_bottom.setVisibility(View.VISIBLE);
//					rl_allchcek.setVisibility(View.VISIBLE);
					lv_cart_list.setVisibility(View.VISIBLE);
					//try {
					//	setAdapter();
					//} catch (DbException e) {
				//		e.printStackTrace();
				//	}
				}

			}

			@Override
			public void onError(String error) {
			}

			@Override
			public void onException(Exception e) {
				LogUtil.ErrorLog(e);
			}
		}, true);

	}

	protected void setAdapter() throws DbException {
		// 保存数据库
//		for (TzmMyCartModel cartModel : cartModels) {
//			TzmMyCartModel cart = dbUtils.findFirst(Selector.from(
//					TzmMyCartModel.class).where("cid", "=", cartModel.cid));
//			cartModel.isCheck = false;
//			if (cart != null) {
//				cart.number = cartModel.number;
//				cart.isCheck = false;
//				dbUtils.update(cart);
//			} else {
//				dbUtils.save(cartModel);
//			}
//
//		}
//		List<TzmMyCartModel> tzmMyCartModels = dbUtils
//				.findAll(TzmMyCartModel.class);
//		adapter = new TzmCartListAdapter(getActivity(), tzmMyCartModels);
//		lv_cart_list.setAdapter(adapter);
//		getTotalMoney();
	}

	/*
	 * 计算总价
	 */
	protected void getTotalMoney() {
		Double totalPrice = 0.0;
//		try {
//			list = dbUtils.findAll(TzmMyCartModel.class);
//			for (TzmMyCartModel cartModel : list) {
//				if (cartModel.isCheck) {
//					totalPrice = totalPrice + cartModel.price
//							* cartModel.number;
//				}
//			}
//			String all = df.format(totalPrice);
//			tv_holeprice.setText("¥ " + all);
//		} catch (DbException e) {
//			e.printStackTrace();
//		}
		// ToastUtils.showShortToast(getActivity(), list.size() + "");

	}

	private void initView() {

		cb_allcheck.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton compoundButton,
					boolean isCheck) {

//				try {
//					list = dbUtils.findAll(TzmMyCartModel.class);
//					for (TzmMyCartModel cartModel : list) {
//						if (isCheck) {
//							if (!cartModel.isCheck) {
//								cartModel.isCheck = true;
//								dbUtils.update(cartModel);
//							}
//						} else {
//							if (cartModel.isCheck) {
//								cartModel.isCheck = false;
//								dbUtils.update(cartModel);
//							}
//						}
//
//					}
//					adapter.setList(list);
//				} catch (DbException e) {
//					e.printStackTrace();
//				}

				adapter.notifyDataSetChanged();
				getTotalMoney();
			}
		});
		btn_sumbit.setOnClickListener(clickListener);
	}

	/*
	 * 修改购物车数量
	 */
	protected void sumbitOrder() throws DbException {
		list = dbUtils.findAll(TzmMyCartModel.class);
		int i = 0;
		String cids = null;
//		for (TzmMyCartModel tzmMyCartModel : list) {
//			if (tzmMyCartModel.isCheck) {
//				if(tzmMyCartModel.status==0){
//					ToastUtils.showShortToast(getActivity(), "已下架的商品不能购买。");
//					return;
//				}
//				if (i == 0) {
//					cids = tzmMyCartModel.cid + ":" + tzmMyCartModel.number;
//					cid = tzmMyCartModel.cid + "";
//				} else {
//					cid = cid + "," + tzmMyCartModel.cid;
//					cids = cids + "," + tzmMyCartModel.cid + ":"
//							+ tzmMyCartModel.number;
//				}
//				
//				i = i + 1;
//			}
//		}
		if (i == 0) {
			ToastUtils.showShortToast(getActivity(), "您尚未勾选任何商品！");
			return;
		}
		apiClient2 = new ApiClient(getActivity());
		editCartApi = new EditCartApi();
		if (!isLogin) {
			return;
		}
		editCartApi.setUid(userModel.uid);
		// editCartApi.setUid(2);
		editCartApi.setCids(cids);
		apiClient2.api(editCartApi, new ApiListener() {

			@Override
			public void onStart() {
				ProgressDialogUtil.openProgressDialog(getActivity(), "", "");
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
				ToastUtils.showShortToast(getActivity(), error);
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
						if (success) {
							Intent intent = new Intent(getActivity(),
									TzmSelctAddressActivity.class);
							intent.putExtra("cids", cid);
							intent.putExtra("buymethod", 1);
							startActivity(intent);
						}else{
							ToastUtils.showShortToast(getActivity(), error_msg);
						}

					} catch (JSONException e) {
						e.printStackTrace();
					}

				}

			}
		}, true);

	}

	View.OnClickListener clickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_sumbit:
				try {
					sumbitOrder();
				} catch (DbException e) {
					e.printStackTrace();
				}
				break;
//			case R.id.btn_guang:
//				Intent pintent = new Intent(getActivity(),
//						TzmProductListActivity.class);
//				pintent.putExtra("display", 1);
//				startActivity(pintent);
//				break;
			}
		}
	};

	private void checkLogin() {
		isLogin = UserInfoUtils.isLogin();
		if (isLogin) {
			userModel = UserInfoUtils.getUserInfo();
		}

	}

	// 根据数量计算阶梯价格
	public Double getPrice(TzmMyCartModel cartModel, int num) {
		Double price = null;
//		if (cartModel.min == 0 || num <= cartModel.min) {
//			price = cartModel.minprices;
//		} else {
//			if (cartModel.mid == 0 || num <= cartModel.mid) {
//				price = cartModel.midprices;
//			} else {
//				price = cartModel.maxprices;
//			}
//		}
		return price;
	}

	@Override
	public void onStart() {
		super.onStart();
//		ToastUtils.showShortToast(getActivity(), "onStart");
	}

	@Override
	public void onStop() {
		super.onStop();
//		ToastUtils.showShortToast(getActivity(), "onStop");
//		try {
//			if (list != null) {
//				dbUtils.deleteAll(list);
//			}
//		} catch (DbException e) {
//			e.printStackTrace();
//		}
	}

	@Override
	public void onResume() {
		super.onResume();
		loadData();
//		ToastUtils.showShortToast(getActivity(), "onResume");

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
//		ToastUtils.showShortToast(getActivity(), "onDestroy");
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
//		try {
//			if (list != null) {
//				dbUtils.deleteAll(list);
//			}
//
//		} catch (DbException e) {
//			e.printStackTrace();
//		}
//		ToastUtils.showShortToast(getActivity(), "onDestroyView");
		EventBus.getDefault().unregister(this);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		ToastUtils.showShortToast(getActivity(), "onCreate");
	}
}
