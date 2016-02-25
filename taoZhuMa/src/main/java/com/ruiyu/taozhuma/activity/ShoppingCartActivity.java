package com.ruiyu.taozhuma.activity;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.bitmap.PauseOnScrollListener;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.adapter.TzmCartFatherListAdapter;
import com.ruiyu.taozhuma.api.ApiClient;
import com.ruiyu.taozhuma.api.ApiListener;
import com.ruiyu.taozhuma.api.EditCartApi;
import com.ruiyu.taozhuma.api.TzmTomycartApi;
import com.ruiyu.taozhuma.even.CartChangeEven;
import com.ruiyu.taozhuma.even.CartChildEven;
import com.ruiyu.taozhuma.even.CartEven;
import com.ruiyu.taozhuma.even.CartFatherEven;
import com.ruiyu.taozhuma.model.BaseModel;
import com.ruiyu.taozhuma.model.TzmMyCartModel;
import com.ruiyu.taozhuma.model.TzmMyCartModel.Cart;
import com.ruiyu.taozhuma.model.TzmMyCartModel.DiscountText;
import com.ruiyu.taozhuma.model.UserModel;
import com.ruiyu.taozhuma.utils.LogUtil;
import com.ruiyu.taozhuma.utils.ProgressDialogUtil;
import com.ruiyu.taozhuma.utils.StringUtils;
import com.ruiyu.taozhuma.utils.ToastUtils;
import com.ruiyu.taozhuma.utils.UserInfoUtils;

import de.greenrobot.event.EventBus;

/**
 * 新坑爹购物车
 * 
 * @author fu
 * 
 */
public class ShoppingCartActivity extends Activity {

	@ViewInject(R.id.rl_bottom)
	private RelativeLayout rl_bottom;
	@ViewInject(R.id.rl_bg)
	private RelativeLayout rl_bg;
	@ViewInject(R.id.lv_cart_list)
	private ListView lv_cart_list;
	@ViewInject(R.id.tv_holeprice)
	private TextView tv_holeprice;
	@ViewInject(R.id.cb_allcheck)
	private CheckBox cb_allcheck;
	@ViewInject(R.id.btn_sumbit)
	private Button btn_sumbit;
	@ViewInject(R.id.btn_head_left)
	private Button btn_head_left;
	@ViewInject(R.id.btn)
	private Button btn;
	// 接口调用
	private ApiClient apiClient, apiClient2;
	private TzmTomycartApi api;
	private TzmCartFatherListAdapter adapter;
	private EditCartApi editCartApi;
	// 用户信息
	private Boolean isLogin;
	private UserModel userModel;
	private List<TzmMyCartModel> list, newlist;

	private DecimalFormat df;

	private String cid;

	private BitmapUtils bitmapUtils;

	@ViewInject(R.id.dis_holeprice)
	private TextView dis_holeprice;// 满减满折显示

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tzm_shop_fragment);
		// ProgressDialogUtil.closeProgressDialog();
		// LogUtil.Log("onCreateView");
		// ToastUtils.showShortToast(ShoppingCartActivity.this, "onCreateView");
		// View view = inflater.inflate(R.layout.tzm_shop_fragment, null);

		// ProgressDialogUtil.closeProgressDialog();
		// LogUtil.Log("onCreateView");
		// ToastUtils.showShortToast(ShoppingCartActivity.this, "onCreateView");
		ViewUtils.inject(this);
		btn_head_left.setVisibility(View.GONE);
		bitmapUtils = new BitmapUtils(this);
		bitmapUtils.configDefaultLoadingImage(R.drawable.loding_defult);// 默认背景图片
		bitmapUtils.configDefaultLoadFailedImage(R.drawable.loding_defult);// 加载失败图片
		//bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);// 设置图片压缩类型
		df = new DecimalFormat("#.#");
		initView();
		// loadData();
		EventBus.getDefault().register(this);

	}

	public void onEventMainThread(CartEven event) {
		System.out.println("1+++++++++++++++++");
		if (newlist == null)
			return;
		// list.get(event.getFatherPosition()).fatherCheck = event.getCheck();
		this.newlist = event.getList();
		int j = 0;
		Double hole = 0.00;
		for (TzmMyCartModel cartModel : newlist) {
			if (cartModel.cartType == event.getType() && cartModel.tag == 1) {
				if (cartModel.status != 0 && cartModel.isActive == 1) {
					// 过滤掉下架和过期商品,只有活动商品有效
					j = j + cartModel.number;
					hole = hole + cartModel.price * cartModel.number;
				}
			}
		}
		for (TzmMyCartModel cartModel : newlist) {
			if (cartModel.cartType == event.getType() && cartModel.tag == 2) {
				cartModel.count = j;
				cartModel.holeprice = df.format(hole);
			}
		}
		adapter.list = newlist;
		adapter.notifyDataSetChanged();
	}

	public void onEventMainThread(CartFatherEven event) {
		System.out.println("2+++++++++++++++++");
		if (newlist == null)
			return;
		// list.get(event.getFatherPosition()).fatherCheck = event.getCheck();
		this.newlist = event.getList();
		// adapter.notifyDataSetChanged();
		try {
			getAllPrice();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onEventMainThread(CartChildEven event) {
		System.out.println("3+++++++++++++++++");
		// 店铺所属全选
		if (newlist == null)
			return;
		for (TzmMyCartModel cartModel : newlist) {
			if (cartModel.tag == 1 && cartModel.cartType == event.getType()) {
				if (cartModel.status != 0 && cartModel.isActive == 1) {
					// 过滤掉下架和过期商品
					cartModel.isCheck = event.getCheck();
				}

			}
		}
		// list.get(event.getFatherPosition()).carts.get(event.getChildPosition()).childCheck
		// = event
		// .getCheck();
		adapter.list = newlist;
		adapter.notifyDataSetChanged();
		// getAllPrice();
	}

	public void onEventMainThread(CartChangeEven even) {
		System.out.println("4+++++++++++++++++");
		if (even.getIsChange()) {
			loadData();
			cb_allcheck.setChecked(true);
			
		}
	}

	private void loadData() {
		checkLogin();
		if (!isLogin) {
			rl_bg.setVisibility(View.VISIBLE);
			rl_bottom.setVisibility(View.GONE);
			lv_cart_list.setVisibility(View.GONE);
			return;
		}
		apiClient = new ApiClient(ShoppingCartActivity.this);
		api = new TzmTomycartApi();
		list = new ArrayList<TzmMyCartModel>();
		api.setUid(userModel.uid);
		apiClient.api(this.api, new ApiListener() {
			@Override
			public void onStart() {
				ProgressDialogUtil.openProgressDialog(
						ShoppingCartActivity.this, "", "");
			}

			@Override
			public void onComplete(String jsonStr) {

				Gson gson = new Gson();
				Type type = new TypeToken<BaseModel<ArrayList<TzmMyCartModel>>>() {
				}.getType();
				BaseModel<ArrayList<TzmMyCartModel>> base = gson.fromJson(
						jsonStr, type);

				if (base.result != null && base.result.size() > 0) {
					list = base.result;
					rl_bg.setVisibility(View.GONE);
					rl_bottom.setVisibility(View.VISIBLE);
					lv_cart_list.setVisibility(View.VISIBLE);
					dataRest();
					isCheck(true);
				} else {
					rl_bg.setVisibility(View.VISIBLE);
					rl_bottom.setVisibility(View.GONE);
					lv_cart_list.setVisibility(View.GONE);
					// ToastUtils.showShortToast(ShoppingCartActivity.this,
					// base.error_msg);
					ProgressDialogUtil.closeProgressDialog();
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

	/**
	 * 数据重构以适应手机购物车结构
	 */
	private void dataRest() {
		newlist = new ArrayList<TzmMyCartModel>();
		int type = 0;
		for (int i = 0; i < list.size(); i++) {
			TzmMyCartModel cartModel = new TzmMyCartModel();
			cartModel = list.get(i);
			cartModel.tag = 0;
			cartModel.isCheck = false;
			cartModel.cartType = type;
			newlist.add(cartModel);
			int j = 0;
			Double hole = 0.00;
			for (Cart cart : list.get(i).carts) {
				if (cart.status != 0 && cart.isActive == 1) {
					// 过滤
					j = j + cart.number;
					hole = hole + cart.price * cart.number;
				}
				TzmMyCartModel model = new TzmMyCartModel();
				// 1020
				model.sellingPrice = cart.sellingPrice;
				model.discount = cart.discount;
				model.isActive = cart.isActive;
				model.skuLinkId = cart.skuLinkId;
				model.activityId = cart.activityId;
				model.type = cart.type;
				model.skuValue = cart.skuValue;
				//
				model.cid = cart.cid;
				model.price = cart.price;
				model.status = cart.status;
				model.name = cart.name;
				model.image = cart.image;
				model.number = cart.number;
				model.productId = cart.productId;
				model.tag = 1;
				model.isCheck = false;
				model.cartType = type;
				newlist.add(model);
			}
			TzmMyCartModel cartModel2 = new TzmMyCartModel();
			cartModel2.tag = 2;
			cartModel2.count = j;
			cartModel2.holeprice = df.format(hole);
			cartModel2.isCheck = false;
			cartModel2.cartType = type;
			// 1210
			cartModel2.discountType = cartModel.discountType;
			cartModel2.discountTextStr = cartModel.discountTextStr;
			cartModel2.discountText = cartModel.discountText;
			newlist.add(cartModel2);
			type = type + 1;
		}
		ProgressDialogUtil.closeProgressDialog();
		setAdapter();
		tv_holeprice.setText("总计：¥ " + 0);
		dis_holeprice.setText("为您节省：¥ " + 0);
	}

	protected void setAdapter() {
		// checkbox状态初始化
		// if (list != null && list.size() > 0) {
		// for (TzmMyCartModel tzmMyCartModel : list) {
		// tzmMyCartModel.fatherCheck = false;
		// int i = 0;
		// Double hole = 0.00;
		// for (Cart cart : tzmMyCartModel.carts) {
		// i = i + 1;
		// hole = hole + cart.price * cart.number;
		// cart.childCheck = false;
		// }
		// tzmMyCartModel.count = i;
		// tzmMyCartModel.holeprice = df.format(hole);
		// }
		// }
		adapter = new TzmCartFatherListAdapter(ShoppingCartActivity.this,
				newlist, bitmapUtils);
		lv_cart_list.setAdapter(adapter);
		lv_cart_list.setOnScrollListener(new PauseOnScrollListener(bitmapUtils,
				false, true));
		// getAllPrice();
	}

	private void getAllPrice() throws Exception {
		if (newlist != null && newlist.size() > 0) {
			Double all = 0.0;
			Double disprice = 0.0;// 店铺总价
			int j = 0;// 每个店铺商品总数
			Double alldisprice = 0.0;// 全部折扣总价

			for (TzmMyCartModel tzmMyCartModel : newlist) {
				if (tzmMyCartModel.tag == 1 && tzmMyCartModel.isCheck == true) {
					disprice = disprice + tzmMyCartModel.price
							* tzmMyCartModel.number;
					j = j + tzmMyCartModel.number;
				}
				if (tzmMyCartModel.tag == 2 && disprice > 0) {
					if (tzmMyCartModel.discountType == 1) {
						// 满减
						double d = Subsidy(disprice,
								tzmMyCartModel.discountText);
						disprice = disprice - d;
						alldisprice = alldisprice + d;

					} else if (tzmMyCartModel.discountType == 2) {
						// 满折
						double d = Discount(disprice, j,
								tzmMyCartModel.discountText);
						disprice = disprice - d;
						alldisprice = alldisprice + d;

					}
					all = all + disprice;
					// 每隔一个店铺清零
					disprice = 0.0;
					j = 0;
				}
			}

			String allPrice = df.format(all);
			tv_holeprice.setText("总计：¥ " + allPrice);
			dis_holeprice.setText("为您节省：¥ " + df.format(alldisprice));
		}

	}

	/**
	 * 满折
	 * 
	 * @return 优惠的钱数
	 */
	private Double Discount(Double disprice, int num,
			List<DiscountText> discountText) {
		int j = 0;
		for (int i = 0; i < discountText.size(); i++) {
			if (num >= Integer.parseInt(discountText.get(i).om)) {
				// 满足条件记录最后一个
				j = i + 1;
			}
			;
		}
		if (j > 0) {
			return disprice
					* ((10 - Double.parseDouble(discountText.get(j - 1).m)) * 0.1);
		} else {
			return 0.0;
		}
	}

	/**
	 * 满减
	 * 
	 * @return 优惠的钱数
	 */
	private Double Subsidy(Double disprice, List<DiscountText> discountText) {
		int j = 0;
		for (int i = 0; i < discountText.size(); i++) {
			if (disprice >= Double.parseDouble(discountText.get(i).om)) {
				j = i + 1;
			}
		}
		if (j > 0) {
			return Double.parseDouble(discountText.get(j - 1).m);
		} else {
			return 0.0;
		}
	}

	private void initView() {

		cb_allcheck.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton compoundButton,
					boolean isCheck) {
				isCheck(isCheck);
			}
		});
		btn_sumbit.setOnClickListener(clickListener);
		btn.setOnClickListener(clickListener);
	}
	/**
	 * 
	 */
	private void isCheck(boolean isCheck) {
		// TODO Auto-generated method stub
		if (newlist != null && newlist.size() > 0) {
			if (isCheck) {
				for (TzmMyCartModel tzmMyCartModel : newlist) {
					if (tzmMyCartModel.tag == 1
							&& (tzmMyCartModel.status == 0
									|| tzmMyCartModel.isActive == 2 || tzmMyCartModel.isActive == 0)) {
						// 全选过滤已下架和活动结束商品
						tzmMyCartModel.isCheck = false;

					} else {
						tzmMyCartModel.isCheck = true;
					}

				}
			} else {
				for (TzmMyCartModel tzmMyCartModel : newlist) {
					tzmMyCartModel.isCheck = false;
				}
			}
			adapter.list = newlist;
			adapter.notifyDataSetChanged();
		}
	}
	/*
	 * 修改购物车数量
	 */
	protected void sumbitOrder() throws DbException {
		int i = 0;
		String cids = null;
		for (TzmMyCartModel tzmMyCartModel : newlist) {
			if (tzmMyCartModel.tag == 1) {

				if (tzmMyCartModel.isCheck) {
					if (tzmMyCartModel.status == 0) {
						ToastUtils.showShortToast(ShoppingCartActivity.this,
								"已下架的商品不能购买。");
						return;
					}
					if (i == 0) {
						cids = tzmMyCartModel.cid + ":" + tzmMyCartModel.number;
						cid = tzmMyCartModel.cid + "";
					} else {
						cid = cid + "," + tzmMyCartModel.cid;
						cids = cids + "," + tzmMyCartModel.cid + ":"
								+ tzmMyCartModel.number;
					}

					i = i + 1;
				}
			}
		}
		if (i == 0) {
			ToastUtils.showShortToast(ShoppingCartActivity.this, "您尚未勾选任何商品！");
			return;
		}
		apiClient2 = new ApiClient(ShoppingCartActivity.this);
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
				ProgressDialogUtil.openProgressDialog(
						ShoppingCartActivity.this, "", "");
			}

			@Override
			public void onException(Exception e) {
				ProgressDialogUtil.closeProgressDialog();
				LogUtil.ErrorLog(e);
				ToastUtils.showShortToast(ShoppingCartActivity.this, "网络异常");
			}

			@Override
			public void onError(String error) {
				ProgressDialogUtil.closeProgressDialog();
				LogUtil.Log(error);
				ToastUtils.showShortToast(ShoppingCartActivity.this, error);
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
						if (success && result == 1) {
							Intent intent = new Intent(
									ShoppingCartActivity.this,
									TzmSelctAddressActivity.class);
							intent.putExtra("cids", cid);
							intent.putExtra("buymethod", 1);
							startActivity(intent);
						} else {
							ToastUtils.showShortToast(
									ShoppingCartActivity.this, error_msg);
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
			case R.id.btn:
				Intent intent = new Intent(ShoppingCartActivity.this,
						TzmActivity.class);
				intent.putExtra("type", 7);
				startActivity(intent);
				break;
			// case R.id.btn_guang:
			// Intent pintent = new Intent(ShoppingCartActivity.this,
			// TzmProductListActivity.class);
			// pintent.putExtra("display", 1);
			// startActivity(pintent);
			// break;
			}
		}
	};

	private void checkLogin() {
		isLogin = UserInfoUtils.isLogin();
		if (isLogin) {
			userModel = UserInfoUtils.getUserInfo();
		}

	}

	@Override
	public void onStart() {
		super.onStart();
		// ToastUtils.showShortToast(ShoppingCartActivity.this, "onStart");
	}

	@Override
	public void onStop() {
		super.onStop();
		// ToastUtils.showShortToast(ShoppingCartActivity.this, "onStop");

	}

	@Override
	public void onResume() {
		super.onResume();
		loadData();
		cb_allcheck.setChecked(true);
		// ToastUtils.showShortToast(ShoppingCartActivity.this, "onResume");
		
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
		// ToastUtils.showShortToast(ShoppingCartActivity.this, "onDestroy");
	}

}
