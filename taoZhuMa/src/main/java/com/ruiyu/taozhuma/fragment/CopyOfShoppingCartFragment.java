package com.ruiyu.taozhuma.fragment;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.ruiyu.taozhuma.activity.ProductDetailActivity;
import com.ruiyu.taozhuma.activity.TzmActivity;
import com.ruiyu.taozhuma.activity.TzmSelctAddressActivity;
import com.ruiyu.taozhuma.activity.TzmShopDetailActivity;
import com.ruiyu.taozhuma.adapter.TzmCartFatherListAdapter;
import com.ruiyu.taozhuma.api.ApiClient;
import com.ruiyu.taozhuma.api.ApiListener;
import com.ruiyu.taozhuma.api.EditCartApi;
import com.ruiyu.taozhuma.api.TzmDelCartApi;
import com.ruiyu.taozhuma.api.TzmTomycartApi;
import com.ruiyu.taozhuma.even.CartChangeEven;
import com.ruiyu.taozhuma.even.CartChildEven;
import com.ruiyu.taozhuma.even.CartEven;
import com.ruiyu.taozhuma.even.CartFatherEven;
import com.ruiyu.taozhuma.even.ShoppingCartEven;
import com.ruiyu.taozhuma.model.BaseModel;
import com.ruiyu.taozhuma.model.TzmMyCartModel;
import com.ruiyu.taozhuma.model.TzmMyCartModel.Cart;
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
public class CopyOfShoppingCartFragment extends Fragment {

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
	private ShoppingCartAdapter adapter;
	private EditCartApi editCartApi;
	// 用户信息
	private Boolean isLogin;
	private UserModel userModel;
	private List<TzmMyCartModel> list, newlist;

	private DecimalFormat df;

	private String cid;

	private BitmapUtils bitmapUtils;

	private TzmDelCartApi delCartApi;
	private ApiClient apiClient3;

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// ProgressDialogUtil.closeProgressDialog();
		// LogUtil.Log("onCreateView");
		// ToastUtils.showShortToast(getActivity(), "onCreateView");
		View view = inflater.inflate(R.layout.tzm_shop_fragment, null);
		ViewUtils.inject(this, view);
		btn_head_left.setVisibility(View.GONE);
		bitmapUtils = new BitmapUtils(getActivity());
		bitmapUtils.configDefaultLoadFailedImage(R.drawable.loding_defult);// 加载失败图片
		bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);// 设置图片压缩类型
		bitmapUtils.configMemoryCacheEnabled(true);
		df = new DecimalFormat("#.##");
		initView();
		// loadData();
		// EventBus.getDefault().register(this);
		return view;

	}

	/**
	 * 商品数量改变
	 * 
	 * @param event
	 */
	// public void onEventMainThread(CartEven event) {
	// if (newlist == null)
	// return;
	// // list.get(event.getFatherPosition()).fatherCheck = event.getCheck();
	// newlist = event.getList();
	// int j = 0;
	// Double hole = 0.00;
	// for (TzmMyCartModel cartModel : newlist) {
	// if (cartModel.cartType == event.getType() && cartModel.tag == 1) {
	// if (cartModel.status != 0 && cartModel.isActive != 2) {
	// // 过滤掉下架和过期商品
	// j = j + cartModel.number;
	// hole = hole + cartModel.price * cartModel.number;
	// }
	// }
	// if (cartModel.cartType == event.getType() && cartModel.tag == 2) {
	// cartModel.count = j;
	// cartModel.holeprice = df.format(hole);
	// }
	// }
	// // for (TzmMyCartModel cartModel : newlist) {
	// //
	// // }
	// adapter.list = newlist;
	// adapter.notifyDataSetChanged();
	// }

	/**
	 * 单个商品选中改变
	 * 
	 * @param event
	 */
	// public void onEventMainThread(CartFatherEven event) {
	// if (newlist == null)
	// return;
	// // list.get(event.getFatherPosition()).fatherCheck = event.getCheck();
	// this.newlist = event.getList();
	// // adapter.notifyDataSetChanged();
	// getAllPrice();
	// }

	/**
	 * 底部总价显示
	 */
	// private void getAllPrice() {
	// if (newlist != null && newlist.size() > 0) {
	// Double all = 0.0;
	// for (TzmMyCartModel tzmMyCartModel : newlist) {
	// if (tzmMyCartModel.tag == 1 && tzmMyCartModel.isCheck == true) {
	// all = all + tzmMyCartModel.price * tzmMyCartModel.number;
	// }
	// }
	// String allPrice = df.format(all);
	// tv_holeprice.setText("￥" + allPrice);
	// }
	//
	// }

	/**
	 * 线上数据改变
	 * 
	 * @param even
	 */
	public void onEventMainThread(CartChangeEven even) {
		if (even.getIsChange()) {
			loadData();
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
		apiClient = new ApiClient(getActivity());
		api = new TzmTomycartApi();
		list = new ArrayList<TzmMyCartModel>();
		api.setUid(userModel.uid);
		apiClient.api(this.api, new ApiListener() {
			@Override
			public void onStart() {
				ProgressDialogUtil.openProgressDialog(getActivity(), "", "");
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

				} else {
					rl_bg.setVisibility(View.VISIBLE);
					rl_bottom.setVisibility(View.GONE);
					lv_cart_list.setVisibility(View.GONE);
					// ToastUtils.showShortToast(getActivity(), base.error_msg);
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
			for (Cart cart : list.get(i).carts) {
				// if (cart.status != 0 && cart.isActive != 2
				// && cart.isActive != 0) {
				// // 过滤
				// j = j + cart.number;
				// hole = hole + cart.price * cart.number;
				// }
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
			// cartModel.count = j;
			cartModel2.holeprice = 0 + "";// 总价初始化
			cartModel2.isCheck = false;
			cartModel2.cartType = type;
			cartModel2.discountType = cartModel.discountType;
			// cartModel2.discountText = cartModel.discountText;
			newlist.add(cartModel2);
			type = type + 1;
		}
		ProgressDialogUtil.closeProgressDialog();
		setAdapter();
		tv_holeprice.setText("￥" + 0);
	}

	protected void setAdapter() {
		adapter = new ShoppingCartAdapter();
		lv_cart_list.setAdapter(adapter);
		// lv_cart_list.setOnScrollListener(new
		// PauseOnScrollListener(bitmapUtils,
		// false, true));
	}

	private void initView() {

		cb_allcheck.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton compoundButton,
					boolean isCheck) {
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
					adapter.notifyDataSetChanged();
				}
			}
		});
		btn_sumbit.setOnClickListener(clickListener);
		btn.setOnClickListener(clickListener);
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
					// if (tzmMyCartModel.status == 0) {
					// ToastUtils.showShortToast(getActivity(), "已下架的商品不能购买。");
					// return;
					// }
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
				ToastUtils.showShortToast(getActivity(), "网络异常");
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
						if (success && result == 1) {
							Intent intent = new Intent(getActivity(),
									TzmSelctAddressActivity.class);
							intent.putExtra("cids", cid);
							intent.putExtra("buymethod", 1);
							startActivity(intent);
						} else {
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
			case R.id.btn:
				Intent intent = new Intent(getActivity(), TzmActivity.class);
				intent.putExtra("type", 7);
				startActivity(intent);
				break;
			// case R.id.btn_guang:
			// Intent pintent = new Intent(getActivity(),
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
		// ToastUtils.showShortToast(getActivity(), "onStart");
	}

	@Override
	public void onStop() {
		super.onStop();
		// ToastUtils.showShortToast(getActivity(), "onStop");

	}

	@Override
	public void onResume() {
		super.onResume();
		loadData();
		cb_allcheck.setChecked(false);
		// ToastUtils.showShortToast(getActivity(), "onResume");

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// ToastUtils.showShortToast(getActivity(), "onDestroy");
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		// ToastUtils.showShortToast(getActivity(), "onDestroyView");
		// EventBus.getDefault().unregister(this);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ToastUtils.showShortToast(getActivity(), "onCreate");
	}

	/**
	 * 局部数值改变
	 * 
	 * @param shoppingCartEven
	 */
	public void onEventMainThread(ShoppingCartEven shoppingCartEven) {

		// 计算总价
		if (newlist != null && newlist.size() > 0) {
			Double all = 0.0;
			Double partprice = 0.0;
			int partnum = 0;
			for (TzmMyCartModel tzmMyCartModel : newlist) {
				if (tzmMyCartModel.tag == 1 && tzmMyCartModel.isCheck == true) {
					all = all + tzmMyCartModel.price * tzmMyCartModel.number;
					if (tzmMyCartModel.cartType == shoppingCartEven.cartType) {
						// 计算该店铺全部已勾选总价
						partprice = partprice + tzmMyCartModel.price
								* tzmMyCartModel.number;
						partnum = partnum + tzmMyCartModel.number;
					}
				}
				if (tzmMyCartModel.tag == 2
						&& tzmMyCartModel.cartType == shoppingCartEven.cartType) {
					tzmMyCartModel.holeprice = df.format(partprice);
					tzmMyCartModel.count = partnum;
				}
			}
			adapter.notifyDataSetChanged();
			tv_holeprice.setText("￥" + df.format(all));
		}

	}

	/**
	 * 店铺所属全选
	 * 
	 * @param event
	 */
	private void onTopCheckbox(int cartType, boolean check) {
		if (newlist == null)
			return;
		Double all = 0.0;
		Double partprice = 0.0;
		int partnum = 0;
		adapter.isliten = false;
		for (TzmMyCartModel cartModel : newlist) {
			if (cartModel.tag == 1 && cartModel.cartType == cartType) {
				if (cartModel.status != 0 && cartModel.isActive != 2
						&& cartModel.isActive != 0) {
					// 过滤掉下架和过期商品
					cartModel.isCheck = check;
				}

			}
		}

		for (TzmMyCartModel tzmMyCartModel : newlist) {
			if (tzmMyCartModel.tag == 1 && tzmMyCartModel.isCheck == true) {
				all = all + tzmMyCartModel.price * tzmMyCartModel.number;
				if (tzmMyCartModel.cartType == cartType) {
					// 计算该店铺全部已勾选总价
					partprice = partprice + tzmMyCartModel.price
							* tzmMyCartModel.number;
					partnum = partnum + tzmMyCartModel.number;
				}
			}
			if (tzmMyCartModel.tag == 2 && tzmMyCartModel.cartType == cartType) {
				tzmMyCartModel.holeprice = df.format(partprice);
				tzmMyCartModel.count = partnum;
			}
		}
		adapter.notifyDataSetChanged();
		tv_holeprice.setText("￥" + df.format(all));
		adapter.isliten = true;
		// list.get(event.getFatherPosition()).carts.get(event.getChildPosition()).childCheck
		// = event
		// .getCheck();
		// getAllPrice();
	}

	/**
	 * 单选选
	 * 
	 * @param event
	 */
	private void onMidCheckbox(int cartType) {
		if (newlist == null)
			return;
		Double all = 0.0;
		Double partprice = 0.0;
		int partnum = 0;

		for (TzmMyCartModel tzmMyCartModel : newlist) {
			if (tzmMyCartModel.tag == 1 && tzmMyCartModel.isCheck == true) {
				all = all + tzmMyCartModel.price * tzmMyCartModel.number;
				if (tzmMyCartModel.cartType == cartType) {
					// 计算该店铺全部已勾选总价
					partprice = partprice + tzmMyCartModel.price
							* tzmMyCartModel.number;
					partnum = partnum + tzmMyCartModel.number;
				}
			}
			if (tzmMyCartModel.tag == 2 && tzmMyCartModel.cartType == cartType) {
				tzmMyCartModel.holeprice = df.format(partprice);
				tzmMyCartModel.count = partnum;
			}
		}
		adapter.notifyDataSetChanged();
		tv_holeprice.setText("￥" + df.format(all));
		// list.get(event.getFatherPosition()).carts.get(event.getChildPosition()).childCheck
		// = event
		// .getCheck();
		// getAllPrice();
	}

	/**
	 * 监听器
	 * 
	 * @author Administrator
	 * 
	 */
	private class ShoppingCartAdapter extends BaseAdapter {
		// private BitmapUtils bitmapUtils;

		// private TzmCartChildListAdapter adapter;
		public boolean isliten = true;// 单个是否监听

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return newlist.size();

		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return newlist.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@SuppressLint("InflateParams")
		@Override
		public View getView(final int position, View view, ViewGroup viewGroup) {
			final ViewHolder viewHolder;
			final int index = position;
			if (view == null) {
				view = getActivity().getLayoutInflater().inflate(
						R.layout.tzm_cart_father_item, null);
				viewHolder = new ViewHolder();
				viewHolder.tv_shopname = (TextView) view
						.findViewById(R.id.tv_shopname);
				viewHolder.ll_bottomview = (LinearLayout) view
						.findViewById(R.id.ll_bottomview);
				viewHolder.ll_midview = (LinearLayout) view
						.findViewById(R.id.ll_midview);
				viewHolder.ll_topview = (LinearLayout) view
						.findViewById(R.id.ll_topview);
				viewHolder.tv_count = (TextView) view
						.findViewById(R.id.tv_count);
				viewHolder.tv_price = (TextView) view
						.findViewById(R.id.tv_price);
				viewHolder.checkboxtop = (CheckBox) view
						.findViewById(R.id.checkboxtop);
				viewHolder.checkboxmid = (CheckBox) view
						.findViewById(R.id.checkboxmid);
				viewHolder.tv_product_name = (TextView) view
						.findViewById(R.id.tv_product_name);
				viewHolder.tv_allprice = (TextView) view
						.findViewById(R.id.tv_allprice);
				viewHolder.iv_picture = (ImageView) view
						.findViewById(R.id.iv_picture);
				viewHolder.et_num = (EditText) view.findViewById(R.id.et_num);
				viewHolder.im_add = (ImageView) view.findViewById(R.id.im_add);
				viewHolder.im_reduce = (ImageView) view
						.findViewById(R.id.im_reduce);
				viewHolder.iv_delete = (ImageView) view
						.findViewById(R.id.iv_delete);
				viewHolder.tv_skuvalue = (TextView) view
						.findViewById(R.id.tv_skuvalue);
				viewHolder.iv_tag_status = (ImageView) view
						.findViewById(R.id.iv_tag_status);
				viewHolder.v_above = (View) view.findViewById(R.id.v_above);

				viewHolder.dis_count = (TextView) view
						.findViewById(R.id.dis_count);
				viewHolder.dis_price = (TextView) view
						.findViewById(R.id.dis_price);
				view.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) view.getTag();
			}
			final TzmMyCartModel info = newlist.get(position);
			bitmapUtils.display(viewHolder.iv_picture, info.image);
			OnClickListener clickListener = new OnClickListener() {
				@Override
				public void onClick(View v) {
					switch (v.getId()) {
					case R.id.tv_shopname:
						Intent intent = new Intent(getActivity(),
								TzmShopDetailActivity.class);
						intent.putExtra("activityId", (newlist.get(Integer
								.parseInt(v.getTag().toString())).shopId)
								.toString());
						// System.out.println((list.get(Integer.parseInt(v.getTag().toString())).shopId).toString());
						intent.putExtra("name", newlist.get(Integer.parseInt(v
								.getTag().toString())).shopName);
						getActivity().startActivity(intent);
						break;
					case R.id.tv_product_name:

						Intent intent2 = new Intent(getActivity(),
								ProductDetailActivity.class);
						intent2.putExtra("id", info.productId);
						intent2.putExtra("activityId", info.activityId);
						getActivity().startActivity(intent2);

						break;

					}

				}
			};
			if (info.tag == 0) {
				viewHolder.ll_topview.setVisibility(View.VISIBLE);
				viewHolder.ll_midview.setVisibility(View.GONE);
				viewHolder.ll_bottomview.setVisibility(View.GONE);
				viewHolder.tv_shopname.setText(info.shopName);
				viewHolder.tv_shopname.setTag(position);
				viewHolder.tv_shopname.setOnClickListener(clickListener);
				viewHolder.checkboxtop
						.setOnCheckedChangeListener(new OnCheckedChangeListener() {
							@Override
							public void onCheckedChanged(
									CompoundButton buttonView, boolean isChecked) {
								newlist.get(index).isCheck = isChecked;
								onTopCheckbox(newlist.get(index).cartType,
										isChecked);
								// EventBus.getDefault().post(
								// new CartChildEven(info.cartType, info.tag,
								// isChecked));
								// EventBus.getDefault().post(new
								// ShoppingCartEven(info.cartType));
							}
						});
				if (info.isCheck) {
					viewHolder.checkboxtop.setChecked(true);
				} else {
					viewHolder.checkboxtop.setChecked(false);
				}

			} else if (info.tag == 1) {
				viewHolder.ll_topview.setVisibility(View.GONE);
				viewHolder.ll_midview.setVisibility(View.VISIBLE);
				viewHolder.ll_bottomview.setVisibility(View.GONE);
				// bitmapUtils.display(viewHolder.iv_picture, info.image);
				// 下架 ，活动结束
				if (info.status == 0 || info.isActive == 2
						|| info.isActive == 0) {
					viewHolder.iv_tag_status.setVisibility(View.VISIBLE);
					if (info.status == 0) {
						// 下架
						viewHolder.iv_tag_status
								.setImageResource(R.drawable.acstatusover);
					} else if (info.isActive == 2 || info.isActive == 0) {
						// 活动结束
						viewHolder.iv_tag_status
								.setImageResource(R.drawable.acover);
					}
					viewHolder.tv_product_name.setOnClickListener(null);
					viewHolder.checkboxmid.setVisibility(View.INVISIBLE);
					viewHolder.v_above.setVisibility(View.VISIBLE);// 遮盖层
					viewHolder.im_add.setOnClickListener(null);
					viewHolder.im_reduce.setOnClickListener(null);
					viewHolder.tv_product_name.setTextColor(getActivity()
							.getResources().getColor(
									R.color.tzm_text_sys_default_light));
					viewHolder.tv_price.setTextColor(getActivity()
							.getResources().getColor(
									R.color.tzm_text_sys_default_light));
				} else {
					viewHolder.iv_tag_status.setVisibility(View.GONE);
					viewHolder.tv_product_name
							.setOnClickListener(clickListener);
					viewHolder.checkboxmid.setVisibility(View.VISIBLE);
					viewHolder.v_above.setVisibility(View.GONE);
					viewHolder.im_add.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (newlist.get(position).type == 1
									&& newlist.get(position).number >= 3) {
								// 秒杀商品
								showActivityDialog();
							} else {
								// 普通商品
								newlist.get(position).number = newlist
										.get(position).number + 1;
								viewHolder.et_num.setText(newlist.get(position).number
										+ "");
							}

						}
					});
					viewHolder.im_reduce
							.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									if (newlist.get(position).number > 1) {
										newlist.get(position).number = newlist
												.get(position).number - 1;
										viewHolder.et_num.setText(newlist
												.get(position).number + "");
									}

								}
							});
					viewHolder.tv_product_name.setTextColor(getActivity()
							.getResources().getColor(R.color.tzm_black));
					viewHolder.tv_price.setTextColor(getActivity()
							.getResources().getColor(R.color.tzm_black));
				}
				// 限量购标志
				if (info.type == 1) {
					String str = getActivity().getString(R.string.event_title)
							+ info.name;
					SpannableStringBuilder mSpannableStringBuilder = new SpannableStringBuilder(
							str);
					mSpannableStringBuilder.setSpan(
							new ForegroundColorSpan(getActivity()
									.getResources().getColor(R.color.base)), 0,
							3, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
					viewHolder.tv_product_name.setText(mSpannableStringBuilder);

				} else if (info.type == 5) {
					SpannableStringBuilder mSpannableStringBuilder = new SpannableStringBuilder(
							"专场活动" + info.name);
					mSpannableStringBuilder.setSpan(
							new ForegroundColorSpan(getActivity()
									.getResources().getColor(R.color.base)), 0,
							4, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
					viewHolder.tv_product_name.setText(mSpannableStringBuilder);
				}

				else {
					viewHolder.tv_product_name.setText(info.name);
				}

				viewHolder.et_num.setText(info.number + "");
				if (StringUtils.isNotEmpty(info.skuValue)) {
					viewHolder.tv_skuvalue.setText(info.skuValue);
				} else {
					viewHolder.tv_skuvalue.setText(null);
				}
				// viewHolder.tv_product_name.setText(info.name);
				viewHolder.tv_price.setText("￥" + info.price);
				// if (!StringUtils.isEmpty(info.image)) {
				// BaseApplication.getInstance().getImageWorker()
				// .loadBitmap(info.image, viewHolder.iv_picture);
				// }
				viewHolder.checkboxmid
						.setOnCheckedChangeListener(new OnCheckedChangeListener() {
							@Override
							public void onCheckedChanged(
									CompoundButton buttonView, boolean isChecked) {

								if (isliten) {
									newlist.get(index).isCheck = isChecked;
									onMidCheckbox(info.cartType);
								}

								// EventBus.getDefault().post(
								// new CartFatherEven(index, true));
								// EventBus.getDefault().post(
								// new CartFatherEven(index, list));
								// EventBus.getDefault().post(
								// new ShoppingCartEven(
								// list.get(index).cartType));
							}
						});
				if (info.isCheck) {
					viewHolder.checkboxmid.setChecked(true);
				} else {
					viewHolder.checkboxmid.setChecked(false);
				}
				viewHolder.iv_delete.setTag(position);
				viewHolder.iv_delete
						.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View v) {
								Deleta((Integer) v.getTag(),
										newlist.get((Integer) v.getTag()).cid);

							}
						});
			} else {
				viewHolder.ll_topview.setVisibility(View.GONE);
				viewHolder.ll_midview.setVisibility(View.GONE);
				viewHolder.ll_bottomview.setVisibility(View.VISIBLE);
				viewHolder.tv_allprice.setText("￥" + info.holeprice);
				viewHolder.tv_count.setText("小计(共" + info.count + "件)：");
				//viewHolder.dis_count.setText(info.disom);
				//viewHolder.dis_price.setText(info.disprice);
			}

			return view;
		}

		/**
		 * 
		 * 
		 * @author Toby
		 * 
		 */
		private class ViewHolder {
			TextView tv_shopname, tv_count, tv_price, tv_product_name,
					tv_allprice, dis_count, dis_price;
			CheckBox checkboxtop, checkboxmid;
			LinearLayout ll_bottomview, ll_midview, ll_topview;
			ImageView iv_picture, im_add, im_reduce, iv_delete;
			EditText et_num;
			TextView tv_skuvalue;
			ImageView iv_tag_status;
			View v_above;
		}

	}

	/**
	 * 秒杀活动商品限制提示框
	 */
	private void showActivityDialog() {
		AlertDialog.Builder builder = new Builder(getActivity());
		builder.setMessage("活动秒杀限购3件，不能贪哦");
		builder.setTitle("提示");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();

			}
		});

		builder.create().show();
	}

	protected void Deleta(final int position, final int cid) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("操作")
				.setPositiveButton("删除", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						delCartApi = new TzmDelCartApi();
						apiClient3 = new ApiClient(getActivity());
						delCartApi.setUid(UserInfoUtils.getUserInfo().uid);
						delCartApi.setCids(cid);
						apiClient.api(delCartApi, new ApiListener() {

							@Override
							public void onStart() {
								ProgressDialogUtil.openProgressDialog(
										getActivity(), "", "");
							}

							@Override
							public void onComplete(String jsonStr) {
								ProgressDialogUtil.closeProgressDialog();
								if (StringUtils.isNotBlank(jsonStr)) {
									try {
										JSONObject jsonObject = new JSONObject(
												jsonStr);
										boolean success = jsonObject
												.optBoolean("success");
										int result = jsonObject
												.optInt("result");
										String error_msg = jsonObject
												.optString("error_msg");
										ToastUtils.showShortToast(
												getActivity(), error_msg);
										if (success && result == 0) {
											loadData();
											// EventBus.getDefault().post(
											// new CartChangeEven(true));

										}
									} catch (JSONException e) {
										e.printStackTrace();
									}

								}

							}
						}, true);
						dialog.dismiss();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();

	}
}
