package com.ruiyu.taozhuma.fragment;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.bigkoo.convenientbanner.CBViewHolderCreator;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.ConvenientBanner.Transformer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.activity.ProductDetailActivity;
import com.ruiyu.taozhuma.activity.TzmLoginRegisterActivity;
import com.ruiyu.taozhuma.activity.TzmShopDetailActivity;
import com.ruiyu.taozhuma.adapter.ProductSKUAColordapter;
import com.ruiyu.taozhuma.adapter.ProductSKUAFormatdapter;
import com.ruiyu.taozhuma.api.AddfavoritesApi;
import com.ruiyu.taozhuma.api.ApiClient;
import com.ruiyu.taozhuma.api.ApiListener;
import com.ruiyu.taozhuma.api.DelSingleFavoriteApi;
import com.ruiyu.taozhuma.api.DelfavoriteApi;
import com.ruiyu.taozhuma.api.ProductSkuLinkApi;
import com.ruiyu.taozhuma.api.ProductSkuListApi;
import com.ruiyu.taozhuma.api.SearchFavoriteApi;
import com.ruiyu.taozhuma.api.TzmProductFocusImgApi;
import com.ruiyu.taozhuma.base.BaseApplication;
import com.ruiyu.taozhuma.base.xUtilsImageLoader;
import com.ruiyu.taozhuma.config.AppConfig;
import com.ruiyu.taozhuma.model.BaseModel;
import com.ruiyu.taozhuma.model.ProductSkuLinkModel;
import com.ruiyu.taozhuma.model.ProductSkuListModel;
import com.ruiyu.taozhuma.model.TzmProductDetailModel;
import com.ruiyu.taozhuma.model.UserModel;
import com.ruiyu.taozhuma.model.ProductSkuListModel.SkuValue;
import com.ruiyu.taozhuma.utils.LogUtil;
import com.ruiyu.taozhuma.utils.StringUtils;
import com.ruiyu.taozhuma.utils.ToastUtils;
import com.ruiyu.taozhuma.utils.UserInfoUtils;
import com.ruiyu.taozhuma.view.ProductDetailFocusImageHolderView;

import com.ruiyu.taozhuma.widget.FlowLayout;
import com.ruiyu.taozhuma.widget.GridViewForScrollView;
import com.ruiyu.taozhuma.widget.RushBuyCountDownTimerViewWithDay;
import com.ruiyu.taozhuma.widget.RushBuyCountDownTimerViewWithDay.TimeOverListener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 商品详情页（上）
 * 
 * @author Fu
 * 
 */
public class ProductDetailFragmentTop extends Fragment implements
		OnClickListener {

	@ViewInject(R.id.tv_sellingPrice)
	private TextView tv_sellingPrice;// 原价
	@ViewInject(R.id.tv_tips)
	private TextView tv_tips;// 折扣
	@ViewInject(R.id.ll_score)
	private LinearLayout ll_score;// 店铺评分
	@ViewInject(R.id.tv_productCmmt)
	private TextView tv_productCmmt;
	@ViewInject(R.id.tv_deliverCmmt)
	private TextView tv_deliverCmmt;
	@ViewInject(R.id.tv_logisticsCmmt)
	private TextView tv_logisticsCmmt;
	@ViewInject(R.id.iv_shop_image)
	private ImageView iv_shop_image;
	@ViewInject(R.id.tv_price)
	private TextView tv_price;
	@ViewInject(R.id.tv_shop_name)
	private TextView tv_shop_name;
	@ViewInject(R.id.tv_product_name)
	private TextView tv_product_name;

	@ViewInject(R.id.tv_product_num)
	private TextView tv_product_num;
	@ViewInject(R.id.tv_ispower)
	private TextView tv_ispower;
	@ViewInject(R.id.tv_pack)
	private TextView tv_pack;
	@ViewInject(R.id.tv_weight)
	private TextView tv_weight;
	@ViewInject(R.id.tv_location)
	private TextView tv_location;
	@ViewInject(R.id.tv_texture)
	private TextView tv_texture;
	@ViewInject(R.id.rl_shop_detail)
	private RelativeLayout rl_shop_detail;
	@ViewInject(R.id.img4)
	private ImageView img4;

	private Integer id;// 商品id
	private Integer activityId;// 活动id

	// 焦点图
	private ApiClient client;
	private TzmProductFocusImgApi tzmProductFocusImgApi;
	public ArrayList<TzmProductDetailModel> list;
	@SuppressWarnings("rawtypes")
	@ViewInject(R.id.convenientBanner)
	private ConvenientBanner convenientBanner;// 顶部广告栏控件
	@ViewInject(R.id.pb_banner)
	private ProgressBar pb_banner;

	private Boolean isLogin;
	private UserModel userModel;

	private ProductDetailActivity activity;

	@ViewInject(R.id.ll_sku)
	private LinearLayout ll_sku;
	// sku
	@ViewInject(R.id.gv_sku_color)
	private GridViewForScrollView gv_sku_color;
	@ViewInject(R.id.gv_sku_fromat)
	private GridViewForScrollView gv_sku_fromat;
	private ProductSkuLinkApi productSkuLinkApi;
	private ProductSkuListApi productSkuListApi;
	private ApiClient apiClient3, apiClient2;
	public List<ProductSkuLinkModel> productSkuLinkModels;
	public List<ProductSkuListModel> productSkuListModels;
	public ProductSKUAColordapter colorAdapter;
	public ProductSKUAFormatdapter fromatAdapter;

	private DbUtils dbUtils;

	// 下标
	@ViewInject(R.id.tv_num_tag)
	private TextView tv_num_tag;

	// 活动倒计时
	@ViewInject(R.id.timeview)
	private RushBuyCountDownTimerViewWithDay timerView;
	xUtilsImageLoader imageLoader;
	@ViewInject(R.id.tv_act_tips)
	private TextView tv_act_tips;// 活动提示

	@ViewInject(R.id.ll_ispower)
	private LinearLayout ll_ispower;// 电动
	@ViewInject(R.id.ll_texture)
	private LinearLayout ll_texture;// 材料
	@ViewInject(R.id.ll_pack)
	private LinearLayout ll_pack;// 包装
	long days = 0;// 天数，专场

	private FlowLayout mFlowLayout;
	private FlowLayout mFlowLayout1;
	public String skucolorid = "";
	public String skuformatid = "";
	private Button bbb;
	private Button ccc;
	Button[] btn;
	@ViewInject(R.id.rl_sku)
	private RelativeLayout rl_sku;

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View v = inflater.inflate(R.layout.product_detail_fragment_top, null);
		ViewUtils.inject(this, v);
		mFlowLayout1 = (FlowLayout) v.findViewById(R.id.flowlayout1);
		mFlowLayout = (FlowLayout) v.findViewById(R.id.flowlayout);
		btn = new Button[20];
		imageLoader = new xUtilsImageLoader(getActivity());
		id = getArguments().getInt("id");
		activityId = getArguments().getInt("activityId");
		dbUtils = BaseApplication.getDbUtils();
		rl_sku.setOnClickListener(this);
		activity = (ProductDetailActivity) getActivity();
		loadImg();
		return v;
	}

	/**
	 * @param view
	 * @param lp
	 */
	private void addView(Button view, MarginLayoutParams lp) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDestroyView() {
		try {
			if (productSkuLinkModels != null && productSkuLinkModels.size() > 0) {
				dbUtils.deleteAll(productSkuLinkModels);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onDestroyView();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_shop_detail:
			try {
				Intent shopDetail_intent = new Intent(getActivity(),
						TzmShopDetailActivity.class);
				shopDetail_intent.putExtra("activityId", activityId + "");
				shopDetail_intent.putExtra("name",
						activity.getDetailModel().shopName);
				startActivity(shopDetail_intent);
			} catch (Exception e) {
				e.printStackTrace();
			}

			break;
		case R.id.rl_sku:

			ProductDetailActivity ProductDetailActivity = (ProductDetailActivity) getActivity();
			ProductDetailActivity.addCartCheck();
			;

			break;
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK
				&& requestCode == AppConfig.REQUESTCODE_LOGIN) {

			activity.searhFavorite();// 判断收藏

		}
	}

	/**
	 * 外部调用
	 * 
	 * @param productDetailModel
	 */
	public void showProductDetail(TzmProductDetailModel productDetailModel) {
		// 名称显示，有跟没有活动
		// 秒杀活动显示 加标示
		String str = productDetailModel.acTitle + productDetailModel.name;
		SpannableStringBuilder mSpannableStringBuilder = new SpannableStringBuilder(
				str);
		mSpannableStringBuilder.setSpan(new ForegroundColorSpan(getResources()
				.getColor(R.color.base)), 0, productDetailModel.acTitle
				.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		tv_product_name.setText(mSpannableStringBuilder);
		rl_shop_detail.setOnClickListener(this);
		if (productDetailModel.isActive == 1) {
			tv_act_tips.setVisibility(View.VISIBLE);
			if (productDetailModel.acType == 1) {
				// 秒杀
				tv_act_tips.setText("距本场结束");

			}
			// 如果有活动顺便开启倒计时
			timerView.setVisibility(View.VISIBLE);
			// 开始倒计时
			try {
				startTimeView(productDetailModel);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (productDetailModel.isActive == 2) {
			// 活动结束
			try {
				tv_act_tips.setText("抢购已结束");
				timerView.setVisibility(View.GONE);
				rl_shop_detail.setOnClickListener(null);
				img4.setVisibility(View.INVISIBLE);
				activity.acitivityOver();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			tv_act_tips.setText("活动未开始");
			timerView.setVisibility(View.GONE);
			rl_shop_detail.setOnClickListener(null);
			img4.setVisibility(View.INVISIBLE);
		}
		// 商品原价
		if (StringUtils.isNotEmpty(productDetailModel.sellingPrice + "")) {
			tv_sellingPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
			tv_sellingPrice.setText("¥ " + productDetailModel.sellingPrice);
		}
		// 打折
		if (StringUtils.isNotEmpty(productDetailModel.discount + "")) {
			tv_tips.setVisibility(View.VISIBLE);
			tv_tips.setText(productDetailModel.discount + "折");
		}
		imageLoader.display(iv_shop_image, productDetailModel.shopImage);
		tv_price.setText("¥ " + productDetailModel.ladderPrice);

		tv_shop_name.setText(productDetailModel.shopName);
		if (productDetailModel.acType == 1 || productDetailModel.acType == 2) {
			// 限量购
			rl_shop_detail.setOnClickListener(null);
			img4.setVisibility(View.INVISIBLE);
		}
		// 店铺评分
		if (StringUtils.isNotEmpty(productDetailModel.productCmmt)) {
			ll_score.setVisibility(View.VISIBLE);
			tv_productCmmt.setText(productDetailModel.productCmmt);
			tv_deliverCmmt.setText(productDetailModel.deliverCmmt);
			tv_logisticsCmmt.setText(productDetailModel.logisticsCmmt);
		}
		// 宝贝详情
		tv_product_num.setText(productDetailModel.model);//
		tv_ispower.setText(productDetailModel.isPower);
		tv_pack.setText(productDetailModel.pack);
		// tv_age.setText(productDetailModel.age);
		tv_weight.setText(productDetailModel.weight + "kg");
		tv_location.setText(productDetailModel.location);
		tv_texture.setText(productDetailModel.texture);
		if (StringUtils.isEmpty(productDetailModel.isPower)) {
			ll_ispower.setVisibility(View.GONE);
		}
		if (StringUtils.isEmpty(productDetailModel.texture)) {
			ll_texture.setVisibility(View.GONE);
		}
		if (StringUtils.isEmpty(productDetailModel.pack)) {
			ll_pack.setVisibility(View.GONE);
		}
		// sku判断
		if (productDetailModel.skuFlag == 1) {
			loadSku();
		}

	}

	/**
	 * sku模块
	 */
	private void loadSku() {
		gv_sku_fromat.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				SkuValue skuValue = (SkuValue) parent.getAdapter().getItem(
						position);
				if (skuValue.isclckabel) {
					activity.skuFormatId = skuValue.optionId;
					skuValue.status = 1;
					for (int i = 0; i < productSkuListModels.get(1).skuValue
							.size(); i++) {
						if (productSkuListModels.get(1).skuValue.get(i).optionId != skuValue.optionId) {
							productSkuListModels.get(1).skuValue.get(i).status = 0;
						}
					}
					fromatAdapter.notifyDataSetChanged();
					skuhandler.sendEmptyMessage(1);
				}

			}
		});
		gv_sku_color.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				SkuValue skuValue = (SkuValue) parent.getAdapter().getItem(
						position);
				activity.skuColorId = skuValue.optionId;
				fromatAdapter.setSkuColorId(activity.skuColorId);
				skuValue.status = 1;
				for (int i = 0; i < productSkuListModels.get(0).skuValue.size(); i++) {
					if (productSkuListModels.get(0).skuValue.get(i).optionId != skuValue.optionId) {
						productSkuListModels.get(0).skuValue.get(i).status = 0;
					}
				}
				colorAdapter.notifyDataSetChanged();
				fromatAdapter.notifyDataSetChanged();
				skuhandler.sendEmptyMessage(1);

			}
		});
		loadSkuLink();

	}

	@SuppressLint("HandlerLeak")
	public Handler skuhandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				activity.skuFormatId = null;// 选中切换状态不可选时清空
				break;
			case 1:// 更改价格显示
				if (!skucolorid .equals("")
						&& skucolorid.length() > 0
						&& !skuformatid .equals("")
						&& skuformatid.length() > 0) {
					try {
						ProductSkuLinkModel linkModel = dbUtils
								.findFirst(Selector
										.from(ProductSkuLinkModel.class)
										.where("skuFormatId", "=",
												skuformatid)
										.and("skuColorId", "=",
												skucolorid)
										.and("pid", "=", id));
						tv_price.setText("¥ " + linkModel.price);
						activity.updatePrice("¥ " + linkModel.price);
						tv_tips.setText(linkModel.discount);
						activity.setPopPrice(linkModel.price);// 库存弹窗
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
				break;
			}

		};
	};

	private void loadSkuLink() {
		apiClient3 = new ApiClient(getActivity());
		productSkuLinkApi = new ProductSkuLinkApi();
		productSkuLinkApi.setPid(id);
		productSkuLinkApi.setActivityId(activityId);
		apiClient3.api(productSkuLinkApi, new ApiListener() {

			@Override
			public void onStart() {
			}

			@Override
			public void onException(Exception e) {
				LogUtil.ErrorLog(e);
			}

			@Override
			public void onError(String error) {
				LogUtil.Log(error);
			}

			@Override
			public void onComplete(String jsonStr) {
				try {
					Gson gson = new Gson();
					Type type = new TypeToken<BaseModel<List<ProductSkuLinkModel>>>() {
					}.getType();
					BaseModel<List<ProductSkuLinkModel>> base = gson.fromJson(
							jsonStr, type);
					if (base.success) {
						productSkuLinkModels = base.result;
						dbUtils.saveOrUpdateAll(productSkuLinkModels);
						loadSkuList();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}, true);
	}

	private void loadSkuList() {
		gv_sku_fromat.setSelector(new ColorDrawable(Color.TRANSPARENT));
		gv_sku_color.setSelector(new ColorDrawable(Color.TRANSPARENT));
		gv_sku_color.setFocusable(false);
		gv_sku_fromat.setFocusable(false);
		rl_sku.setVisibility(View.VISIBLE);
		apiClient2 = new ApiClient(getActivity());
		productSkuListApi = new ProductSkuListApi();
		productSkuListApi.setPid(id);
		productSkuListApi.setActivityId(activityId);
		apiClient2.api(productSkuListApi, new ApiListener() {

			@Override
			public void onStart() {
			}

			@Override
			public void onException(Exception e) {
				LogUtil.ErrorLog(e);
			}

			@Override
			public void onError(String error) {
				LogUtil.Log(error);
			}

			@Override
			public void onComplete(String jsonStr) {
				try {
					Gson gson = new Gson();
					Type type = new TypeToken<BaseModel<List<ProductSkuListModel>>>() {
					}.getType();
					BaseModel<List<ProductSkuListModel>> base = gson.fromJson(
							jsonStr, type);
					if (base.success) {
						productSkuListModels = base.result;
						colorAdapter = new ProductSKUAColordapter(
								getActivity(),
								productSkuListModels.get(0).skuValue);

						fromatAdapter = new ProductSKUAFormatdapter(
								getActivity(),
								productSkuListModels.get(1).skuValue, id,
								skuhandler);

						gv_sku_color.setAdapter(colorAdapter);
						gv_sku_fromat.setAdapter(fromatAdapter);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}, true);
	}

	/**
	 * 开始倒计时
	 * 
	 * @param model
	 * @throws ParseException
	 */
	@SuppressLint("SimpleDateFormat")
	protected void startTimeView(final TzmProductDetailModel productDetailModel)
			throws Exception {
		// 当天日期
		SimpleDateFormat tdateFormat = new SimpleDateFormat("yyyy-MM-dd");
		// 活动时间格式
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		// 当前时间
		Date nowdate = new Date();
		// 开始时间//结束时间
		Date time = null;
		// 时间差距
		long temp = 0;// 相差毫秒
		if (productDetailModel.acType == 1) {
			time = dateFormat.parse(tdateFormat.format(nowdate) + " "
					+ productDetailModel.endTime + ":00");
		} else {
			// 进行中
			time = dateFormat.parse(productDetailModel.endTime);
		}
		// time = dateFormat.parse("2015-12-10 11:58:59");
		temp = time.getTime() - nowdate.getTime();

		days = temp / 1000 / 3600 / 24;// 相差天数
		long hours = temp % (1000 * 3600 * 24) / 1000 / 3600; // 相差小时数
		long temp2 = temp % (1000 * 3600);
		long mins = temp2 / 1000 / 60; // 相差分钟数
		long sec = temp / 1000 % 60;// 相差秒
		if (productDetailModel.acType == 4 || productDetailModel.acType == 2) {
			if (days > 0) {
				tv_act_tips.setText("剩" + days + "天");
			} else {
				tv_act_tips.setText("剩");
			}
		}

		timerView.setTime(hours, mins, sec);
		timerView.setOnTimeOverListener(new TimeOverListener() {

			@Override
			public void isTimeOver(boolean over) {
				if (productDetailModel.acType == 4) {
					// 专场活动，有天数
					if (days > 0) {
						days--;
						timerView.setTime(24, 00, 00);
						timerView.start();
						if (days > 0) {
							tv_act_tips.setText("剩" + days + "天");
						} else {
							tv_act_tips.setText("剩");
						}

					} else {
						// 活动结束
						activity.acitivityOver();
						timerView.setVisibility(View.GONE);
						tv_act_tips.setText("抢购已结束");
						rl_shop_detail.setOnClickListener(null);
						img4.setVisibility(View.INVISIBLE);
					}

				} else {
					// 活动结束
					activity.acitivityOver();
					timerView.setVisibility(View.GONE);
					tv_act_tips.setText("抢购已结束");
					rl_shop_detail.setOnClickListener(null);
					img4.setVisibility(View.INVISIBLE);
				}

			}
		});
		timerView.start();
		// LogUtil.Log(dateFormat.format(date));
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// this.activity = (ProductDetailActivity) activity;
		// try {
		// mListener = (OnFragmentListener) activity;
		// } catch (ClassCastException e) {
		// throw new ClassCastException(activity.toString()
		// + " must implement OnFragmentListener");
		// }
	}

	private void checkLogin() {
		isLogin = UserInfoUtils.isLogin();
		if (isLogin) {
			userModel = UserInfoUtils.getUserInfo();
		}

	}

	/*
	 * 产品焦点图
	 */
	private void loadImg() {
		client = new ApiClient(getActivity());
		tzmProductFocusImgApi = new TzmProductFocusImgApi();
		tzmProductFocusImgApi.setId(id);
		client.api(this.tzmProductFocusImgApi, new ApiListener() {
			@Override
			public void onStart() {
			}

			@SuppressWarnings("unchecked")
			@SuppressLint("InflateParams")
			@Override
			public void onComplete(String jsonStr) {
				Gson gson = new Gson();
				Type type = new TypeToken<BaseModel<ArrayList<TzmProductDetailModel>>>() {
				}.getType();
				final BaseModel<ArrayList<TzmProductDetailModel>> base = gson
						.fromJson(jsonStr, type);

				if (base.result != null && base.result.size() > 0) {
					pb_banner.setVisibility(View.GONE);
					tv_num_tag.setText("1/" + base.result.size());
					list = base.result;
					convenientBanner
							.setPages(
									new CBViewHolderCreator<ProductDetailFocusImageHolderView>() {
										@Override
										public ProductDetailFocusImageHolderView createHolder() {
											return new ProductDetailFocusImageHolderView();
										}
									}, list)
							.setPageTransformer(Transformer.ZoomInTransformer)
							.setPageIndicatorAlign(
									ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
					// .setPageIndicator(
					// new int[] { R.drawable.dian2,
					// R.drawable.dianshi })
					;
					convenientBanner
							.setOnPageChangeListener(new OnPageChangeListener() {

								@Override
								public void onPageSelected(int arg0) {
									tv_num_tag.setText(arg0 + 1 + "/"
											+ base.result.size());

								}

								@Override
								public void onPageScrolled(int arg0,
										float arg1, int arg2) {
									// TODO Auto-generated method stub

								}

								@Override
								public void onPageScrollStateChanged(int arg0) {
									// TODO Auto-generated method stub

								}
							});
					;
				} else {
					ToastUtils.showShortToast(getActivity(), base.error_msg);
				}

			}

			@Override
			public void onError(String error) {
				LogUtil.Log(error);
			}

			@Override
			public void onException(Exception e) {
				LogUtil.ErrorLog(e);
				ToastUtils.showShortToast(getActivity(), "图片加载异常");
			}
		}, true);

	}

	@Override
	public void onDestroy() {
		skuhandler.removeCallbacksAndMessages(null);
		try {
			timerView.stop();
		} catch (Exception e) {
			// TODO: handle exception
		}
		super.onDestroy();
	}

}
