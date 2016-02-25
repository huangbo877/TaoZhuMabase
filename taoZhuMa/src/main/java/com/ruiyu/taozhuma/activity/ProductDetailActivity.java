package com.ruiyu.taozhuma.activity;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jauker.widget.BadgeView;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.adapter.ProductSKUAColordapter;
import com.ruiyu.taozhuma.adapter.ProductSKUAFormatdapter;
import com.ruiyu.taozhuma.adapter.TzmCommentListAdapter;
import com.ruiyu.taozhuma.adapter.ViewPagerAdapter2;
import com.ruiyu.taozhuma.adapter.ViewpagerImageAdapter;
import com.ruiyu.taozhuma.api.AddfavoritesApi;
import com.ruiyu.taozhuma.api.ApiClient;
import com.ruiyu.taozhuma.api.ApiListener;
import com.ruiyu.taozhuma.api.DelSingleFavoriteApi;
import com.ruiyu.taozhuma.api.DelfavoriteApi;
import com.ruiyu.taozhuma.api.ProductSkuLinkApi;
import com.ruiyu.taozhuma.api.ProductSkuListApi;
import com.ruiyu.taozhuma.api.SearchFavoriteApi;
import com.ruiyu.taozhuma.api.TzmAddcartApi;
import com.ruiyu.taozhuma.api.TzmMyCartNumApi;
import com.ruiyu.taozhuma.api.TzmProductDetailApi;
import com.ruiyu.taozhuma.api.TzmProductDetailCommentApi;
import com.ruiyu.taozhuma.api.TzmProductFocusImgApi;
import com.ruiyu.taozhuma.base.BaseApplication;
import com.ruiyu.taozhuma.base.xUtilsImageLoader;
import com.ruiyu.taozhuma.config.AppConfig;
import com.ruiyu.taozhuma.fragment.ProductDetailCommentFragment;
import com.ruiyu.taozhuma.fragment.ProductDetailFragmentBottom;
import com.ruiyu.taozhuma.fragment.ProductDetailFragmentTop;
import com.ruiyu.taozhuma.model.BaseModel;
import com.ruiyu.taozhuma.model.ProductSkuLinkModel;
import com.ruiyu.taozhuma.model.ProductSkuListModel;
import com.ruiyu.taozhuma.model.TzmMyCartNumModel;
import com.ruiyu.taozhuma.model.TzmProductDetailCommentModel;
import com.ruiyu.taozhuma.model.TzmProductDetailModel;
import com.ruiyu.taozhuma.model.UserModel;
import com.ruiyu.taozhuma.model.ProductSkuListModel.SkuValue;
import com.ruiyu.taozhuma.utils.LogUtil;
import com.ruiyu.taozhuma.utils.ProgressDialogUtil;
import com.ruiyu.taozhuma.utils.StringUtils;
import com.ruiyu.taozhuma.utils.ToastUtils;
import com.ruiyu.taozhuma.utils.UserInfoUtils;
import com.ruiyu.taozhuma.widget.CustomViewPager;
import com.ruiyu.taozhuma.widget.DragLayout;
import com.ruiyu.taozhuma.widget.FlowLayout;
import com.ruiyu.taozhuma.widget.GridViewForScrollView;
import com.ruiyu.taozhuma.widget.InnerScrollView;
import com.ruiyu.taozhuma.widget.RushBuyCountDownTimerView;
import com.ruiyu.taozhuma.widget.StickyScrollView;
import com.ruiyu.taozhuma.widget.WebViewForViewPager;
import com.ruiyu.taozhuma.widget.DragLayout.ShowNextPageNotifier;
import com.ruiyu.taozhuma.widget.RushBuyCountDownTimerView.TimeOverListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeConfig;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

/**
 * 商品详情（v3）
 * 
 * @author Fu
 * 
 */
public class ProductDetailActivity extends FragmentActivity {

	private String TAG = "ProductDetailActivity";
	private Boolean isLogin;
	private UserModel userModel;
	@ViewInject(R.id.txt_head_title)
	private TextView txt_head_title;

	private Integer id;// 商品id
	private Integer activityId;// 商品活动id,默认0-普通商品,活动商品时有值，平时默认0,大于0时为活动

	@ViewInject(R.id.rl_bottom)
	private RelativeLayout rl_bottom;

	private ProductDetailFragmentTop fragmentTop;
	private ProductDetailFragmentBottom fragmentBottom;
	@ViewInject(R.id.draglayout)
	private DragLayout draglayout;

	private TzmProductDetailModel productDetailModel;
	private TzmProductDetailApi tzmProductDetailApi;
	private ApiClient client;

	@ViewInject(R.id.tv_all_price)
	private TextView tv_all_price;

	public Integer skuFormatId;// 规格id
	public Integer skuColorId;// 颜色id

	private BadgeView badgeView;
	@ViewInject(R.id.im_cart_num)
	private ImageView im_cart_num;
	// 接口调用
	private ApiClient apiClient;
	private TzmMyCartNumApi cartNumApi;
	private TzmMyCartNumModel cartNumModel;

	// popup
	private PopupWindow popupWindow;
	private PopupWindow popupWindow1;
	@ViewInject(R.id.ll_main)
	private LinearLayout ll_main;
	private ImageView iv_cancel;
	private ImageView iv_productimg;
	private TextView tv_productname;
	private TextView tv_price;
	private Button btn_affirm;
	xUtilsImageLoader imageLoader;
	private GridViewForScrollView pop_gv_sku_color;
	private GridViewForScrollView pop_gv_sku_fromat;

	private DbUtils dbUtils;

	private TzmAddcartApi tzmAddcartApi;

	// 收藏
	@ViewInject(R.id.iv_cancel_collcet)
	private ImageView iv_cancel_collcet;
	@ViewInject(R.id.iv_collcet)
	private ImageView iv_collcet;
	private SearchFavoriteApi searchFavoriteApi;
	private AddfavoritesApi addfavoritesApi;
	private DelSingleFavoriteApi delSingleFavoriteApi;
	private ApiClient client2;

	@ViewInject(R.id.tv_overact)
	private TextView tv_overact;// 结束按钮
	@ViewInject(R.id.btn_addCart)
	private Button btn_addCart;
	private Button pop2_ljjs;

	float pCmmt;
	private FlowLayout mFlowLayout;
	private FlowLayout mFlowLayout1;
	Button[] btn;// 颜色button集合
	Button[] btn1;// 规格button集合

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product_detail_activity);
		ViewUtils.inject(this);
		com.ruiyu.taozhuma.LogUtil.Log(TAG, "onCreate");
		id = getIntent().getIntExtra("id", 0);
		activityId = getIntent().getIntExtra("activityId", 0);// 大于0为活动
		checkLogin();
		initView();
		loadData();
		// 判断收藏
		searhFavorite();
		initpopupWindow2();
		dbUtils = BaseApplication.getDbUtils();

	}

	public TzmProductDetailModel getDetailModel() {
		return productDetailModel;
	}

	public void updatePrice(String priString) {
		tv_all_price.setText(priString);
	}

	@Override
	protected void onResume() {
		loadNum();// 获取购物车数量
		super.onResume();
	}

	// 倒计时结束
	public void acitivityOver() {
		btn_addCart.setVisibility(View.GONE);
		tv_overact.setVisibility(View.VISIBLE);
	}

	/**
	 * 初始化View
	 */
	private void initView() {
		txt_head_title.setText("商品详情");
		badgeView = new BadgeView(this);
		badgeView.setTargetView(im_cart_num);
		badgeView.setBackground(8, getResources().getColor(R.color.base));
		badgeView.setTextSize(10);
		apiClient = new ApiClient(this);
		cartNumApi = new TzmMyCartNumApi();

		fragmentTop = new ProductDetailFragmentTop();
		Bundle bundle = new Bundle();
		bundle.putInt("id", id);
		bundle.putInt("activityId", activityId);
		fragmentTop.setArguments(bundle);
		fragmentBottom = new ProductDetailFragmentBottom();

		getSupportFragmentManager().beginTransaction()
				.add(R.id.first, fragmentTop).add(R.id.second, fragmentBottom)
				.commit();

		ShowNextPageNotifier nextIntf = new ShowNextPageNotifier() {
			@Override
			public void onDragNext() {
				fragmentBottom.loadData(id);
			}
		};
		draglayout.setNextPageListener(nextIntf);

	}

	private void checkLogin() {
		isLogin = UserInfoUtils.isLogin();
		if (isLogin) {
			userModel = UserInfoUtils.getUserInfo();
		}
		// 如果批发商不显示底部
		if (isLogin && userModel.type == 6) {
			rl_bottom.setVisibility(View.GONE);
		} else {
			rl_bottom.setVisibility(View.VISIBLE);
		}

	}

	/**
	 * 加载产品数据
	 */
	private void loadData() {
		client = new ApiClient(this);
		tzmProductDetailApi = new TzmProductDetailApi();
		tzmProductDetailApi.setId(id);
		tzmProductDetailApi.setActivityId(activityId);
		if (isLogin) {
			tzmProductDetailApi.setUid(userModel.uid);
		}

		client.api(this.tzmProductDetailApi, new ApiListener() {
			@Override
			public void onStart() {
				ProgressDialogUtil.openProgressDialog(
						ProductDetailActivity.this, "", "");
			}

			@Override
			public void onComplete(String jsonStr) {
				ProgressDialogUtil.closeProgressDialog();
				try {
					Gson gson = new Gson();
					Type type = new TypeToken<BaseModel<TzmProductDetailModel>>() {
					}.getType();
					BaseModel<TzmProductDetailModel> base = gson.fromJson(
							jsonStr, type);

					if (base.result != null) {

						productDetailModel = base.result;
						fragmentTop.showProductDetail(productDetailModel);
						pCmmt = productDetailModel.pCmmt;
						LogUtil.Log(TAG, "pCmmt :星级平均评分: " + pCmmt);
						fragmentBottom.setfs(pCmmt);
						fragmentBottom.setComt(productDetailModel.comCount);
						tv_all_price.setText("¥ "
								+ productDetailModel.ladderPrice);
						// setView();
					} else {
						ToastUtils.showShortToast(ProductDetailActivity.this,
								base.error_msg);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			@Override
			public void onError(String error) {
				ToastUtils.showShortToast(ProductDetailActivity.this, error);
				ProgressDialogUtil.closeProgressDialog();
			}

			@Override
			public void onException(Exception e) {
				ProgressDialogUtil.closeProgressDialog();
				ToastUtils.showShortToast(ProductDetailActivity.this, "网络异常");
				LogUtil.ErrorLog(e);
			}
		}, true);

	}

	@OnClick({ R.id.iv_collcet, R.id.iv_cancel_collcet, R.id.btn_head_left,
			R.id.iv_share, R.id.btn_addCart, R.id.iv_cancel, R.id.im_cart_num })
	protected void click(View view) {
		switch (view.getId()) {
		case R.id.btn_head_left:
			onBackPressed();
			break;
		case R.id.iv_share:
			share();
			break;
		case R.id.btn_addCart:
			addCartCheck();
			break;
		case R.id.im_cart_num:
			startActivity(new Intent(ProductDetailActivity.this,
					ShoppingCartActivity.class));
			break;
		case R.id.iv_collcet:
			try {
				addFavorite(1);
			} catch (Exception e) {
				e.printStackTrace();
			}

			break;
		case R.id.iv_cancel_collcet:
			try {
				delFavorite();
			} catch (Exception e) {
				e.printStackTrace();
			}

			break;

		}
	}

	/**
	 * 检查是否登录，是否有sku
	 */
	public void addCartCheck() {
		if (!isLogin) {
			ToastUtils.showShortToast(this, "请先登录！");
			Intent intent = new Intent(new Intent(ProductDetailActivity.this,
					TzmLoginRegisterActivity.class));
			intent.putExtra("type", 0);
			startActivityForResult(intent, AppConfig.Login);

			return;
		}

		// sku判断
		if (productDetailModel.skuFlag == 1) {
			// if (skuColorId == null || skuColorId <= 0 || skuFormatId == null
			// || skuFormatId <= 0) {
			if (popupWindow == null) {
				initpopupWindow();
			} else {
				popupWindowUpdate();
			}
			WindowManager.LayoutParams lp = getWindow().getAttributes();
			lp.alpha = 0.4f;
			getWindow().setAttributes(lp);

			popupWindow.showAtLocation(ll_main, Gravity.BOTTOM, 0, 0);

			// popupWindow.showAsDropDown(rl_bottom, Gravity.NO_GRAVITY,
			// location[0], location[1]-popupWindow.getHeight());
			// } else {
			// try {
			// ProductSkuLinkModel linkModel = dbUtils.findFirst(Selector
			// .from(ProductSkuLinkModel.class)
			// .where("skuFormatId", "=", skuFormatId)
			// .and("skuColorId", "=", skuColorId)
			// .and("pid", "=", id));
			//
			// } catch (Exception e) {
			// e.printStackTrace();
			// }
			// }
		} else {
			addCart(null);
		}
	}

	/**
	 * 
	 */
	private void showpop2() {
		// TODO Auto-generated method stub
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		int height = metric.heightPixels;
		int[] location = new int[2];
		rl_bottom.getLocationInWindow(location);
		// System.out.println(location[1]+"++++++++++++"+height);
		popupWindow1.showAtLocation(ll_main, Gravity.BOTTOM, 0, height
				- location[1]);
		new Handler().postDelayed(new Runnable() {
			public void run() {
				// execute the task
				if (popupWindow1 != null && popupWindow1.isShowing()) {
					popupWindow1.dismiss();
				}
			}
		}, 10000);
	}

	/**
	 * 
	 */
	private void initpopupWindow2() {
		// TODO Auto-generated method stub
		View view = getLayoutInflater().inflate(
				R.layout.product_detail_popupwindow2, null);
		popupWindow1 = new PopupWindow(view,
				WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.WRAP_CONTENT);
		popupWindow1.setFocusable(true);
		popupWindow1.setBackgroundDrawable(new BitmapDrawable());
		popupWindow1.setAnimationStyle(R.style.AnimationFadeRight);
		popupWindow1.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				WindowManager.LayoutParams lp = getWindow().getAttributes();
				lp.alpha = 1f;
				getWindow().setAttributes(lp);

			}
		});
		pop2_ljjs = (Button) view.findViewById(R.id.pop2_ljjs);
		pop2_ljjs.setOnClickListener(clickListener);
	}

	/**
	 * 初始化
	 */
	@SuppressWarnings("deprecation")
	@SuppressLint("InflateParams")
	private void initpopupWindow() {
		View view = getLayoutInflater().inflate(
				R.layout.product_detail_popupwindow, null);
		popupWindow = new PopupWindow(view,
				WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.WRAP_CONTENT);
		popupWindow.setFocusable(true);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0x00000000);
		popupWindow.setBackgroundDrawable(dw);
		// 设置popWindow的显示和消失动画
		popupWindow.setAnimationStyle(R.style.AnimBottom);

		popupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				WindowManager.LayoutParams lp = getWindow().getAttributes();
				lp.alpha = 1f;
				getWindow().setAttributes(lp);

			}
		});
		iv_cancel = (ImageView) view.findViewById(R.id.iv_cancel);
		iv_productimg = (ImageView) view.findViewById(R.id.iv_productimg);
		tv_productname = (TextView) view.findViewById(R.id.tv_productname);
		tv_price = (TextView) view.findViewById(R.id.tv_price);
		btn_affirm = (Button) view.findViewById(R.id.btn_affirm);
		imageLoader = new xUtilsImageLoader(ProductDetailActivity.this);
		iv_cancel.setOnClickListener(clickListener);
		btn_affirm.setOnClickListener(clickListener);
		btn = new Button[20];
		btn1 = new Button[20];
		pop_gv_sku_color = (GridViewForScrollView) view
				.findViewById(R.id.gv_sku_color);
		pop_gv_sku_fromat = (GridViewForScrollView) view
				.findViewById(R.id.gv_sku_fromat);

		mFlowLayout1 = (FlowLayout) view.findViewById(R.id.flowlayout1);
		mFlowLayout = (FlowLayout) view.findViewById(R.id.flowlayout);
		try {
			imageLoader.display(iv_productimg, fragmentTop.list.get(0).image);
			tv_productname.setText(productDetailModel.name);
			tv_price.setText("¥ " + productDetailModel.ladderPrice);

			pop_gv_sku_fromat.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					SkuValue skuValue = (SkuValue) parent.getAdapter().getItem(
							position);
					if (skuValue.isclckabel) {
						skuFormatId = skuValue.optionId;
						skuValue.status = 1;
						for (int i = 0; i < fragmentTop.productSkuListModels
								.get(1).skuValue.size(); i++) {
							if (fragmentTop.productSkuListModels.get(1).skuValue
									.get(i).optionId != skuValue.optionId) {
								fragmentTop.productSkuListModels.get(1).skuValue
										.get(i).status = 0;
							}
						}
						fragmentTop.fromatAdapter.notifyDataSetChanged();
						fragmentTop.skuhandler.sendEmptyMessage(1);
					}

				}
			});
			pop_gv_sku_color.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					SkuValue skuValue = (SkuValue) parent.getAdapter().getItem(
							position);
					skuColorId = skuValue.optionId;
					fragmentTop.fromatAdapter.setSkuColorId(skuColorId);
					skuValue.status = 1;
					for (int i = 0; i < fragmentTop.productSkuListModels.get(0).skuValue
							.size(); i++) {
						if (fragmentTop.productSkuListModels.get(0).skuValue
								.get(i).optionId != skuValue.optionId) {
							fragmentTop.productSkuListModels.get(0).skuValue
									.get(i).status = 0;
						}
					}
					fragmentTop.colorAdapter.notifyDataSetChanged();
					fragmentTop.fromatAdapter.notifyDataSetChanged();
					fragmentTop.skuhandler.sendEmptyMessage(1);

				}
			});

			pop_gv_sku_color.setAdapter(fragmentTop.colorAdapter);
			pop_gv_sku_fromat.setAdapter(fragmentTop.fromatAdapter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		MarginLayoutParams lp = new MarginLayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.leftMargin = 5;
		lp.rightMargin = 5;
		lp.topMargin = 5;
		lp.bottomMargin = 5;

		for (int i = 0; i < fragmentTop.productSkuListModels.get(0).skuValue
				.size(); i++) {
			Button view1 = new Button(this);
			view1.setTextSize(13);
			view1.setGravity(Gravity.CENTER);
			// view.setPadding(10, 10, 10, 10);
			view1.setId(fragmentTop.productSkuListModels.get(0).skuValue.get(i).optionId);
			view1.setText(fragmentTop.productSkuListModels.get(0).skuValue
					.get(i).optionValue);
			view1.setTag(fragmentTop.productSkuListModels.get(0).skuValue
					.get(i).optionId);
			if (fragmentTop.productSkuListModels.get(0).skuValue.size() == 1) {
				fragmentTop.skucolorid = fragmentTop.productSkuListModels
						.get(0).skuValue.get(i).optionId.toString();
			}
			if (!fragmentTop.productSkuListModels.get(0).skuValue.get(i).optionId
					.toString().equals(fragmentTop.skucolorid)) {
				view1.setTextColor(Color.parseColor("#E73C7B"));
				view1.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.pink_stroke_corner));
			} else {
				view1.setTextColor(Color.parseColor("#ffffff"));
				view1.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.pink_solid_corner));
			}
			view1.setPadding(15, 5, 15, 5);
			view1.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					// do something
					fragmentTop.skuformatid = "";
					for (int i = 0; i < fragmentTop.productSkuListModels.get(1).skuValue
							.size(); i++) {
						try {
							ProductSkuLinkModel linkModel = dbUtils
									.findFirst(Selector
											.from(ProductSkuLinkModel.class)
											.where("skuFormatId",
													"=",
													fragmentTop.productSkuListModels
															.get(1).skuValue
															.get(i).optionId)
											.and("skuColorId", "=",
													v.getTag().toString())
											.and("pid", "=", id));
							if (linkModel != null && linkModel.status == 1) {
								btn[i].setClickable(true);
								btn[i].setTextColor(Color.parseColor("#E73C7B"));
								btn[i].setBackgroundDrawable(getResources()
										.getDrawable(
												R.drawable.pink_stroke_corner));
								btn[i].getPaint().setFlags(0);
							} else {
								btn[i].setClickable(false);
								btn[i].setBackgroundResource(R.drawable.gray_stroke_corner);
								btn[i].setTextColor(Color.parseColor("#d2d2cb"));
								btn[i].getPaint().setFlags(
										Paint.STRIKE_THRU_TEXT_FLAG); // 中划线
							}
						} catch (DbException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					// ToastUtils.showShortToast(ProductDetailActivity.this,
					// v.getId() + "");
					if (fragmentTop.skucolorid.equals("")) {
						((Button) v).setTextColor(Color.parseColor("#ffffff"));
						v.setBackgroundDrawable(getResources().getDrawable(
								R.drawable.pink_solid_corner));
					} else if (!fragmentTop.skucolorid.equals(v.getTag()
							.toString())) {
						((Button) v).setTextColor(Color.parseColor("#ffffff"));
						v.setBackgroundDrawable(getResources().getDrawable(
								R.drawable.pink_solid_corner));
						for (int i = 0; i < fragmentTop.productSkuListModels
								.get(0).skuValue.size(); i++) {
							if (btn1[i].getTag().toString()
									.equals(fragmentTop.skucolorid)) {
								btn1[i].setTextColor(Color
										.parseColor("#E73C7B"));
								btn1[i].setBackgroundDrawable(getResources()
										.getDrawable(
												R.drawable.pink_stroke_corner));
							}
						}
					}

					fragmentTop.skucolorid = v.getTag().toString();
					fragmentTop.skuhandler.sendEmptyMessage(1);
				}

			});
			btn1[i] = view1;
			mFlowLayout.addView(view1, lp);

		}

		for (int i = 0; i < fragmentTop.productSkuListModels.get(1).skuValue
				.size(); i++) {
			Button view1 = new Button(this);
			view1.setTextSize(13);
			view1.setGravity(Gravity.CENTER);

			view1.setId(fragmentTop.productSkuListModels.get(1).skuValue.get(i).optionId);
			view1.setText(fragmentTop.productSkuListModels.get(1).skuValue
					.get(i).optionValue);

			view1.setTag(fragmentTop.productSkuListModels.get(1).skuValue
					.get(i).optionId);
			if (fragmentTop.productSkuListModels.get(1).skuValue.size() == 1
					&& fragmentTop.productSkuListModels.get(0).skuValue.size() == 1) {
				fragmentTop.skuformatid = fragmentTop.productSkuListModels
						.get(1).skuValue.get(i).optionId.toString();
			}
			if (!fragmentTop.productSkuListModels.get(1).skuValue.get(i).optionId
					.toString().equals(fragmentTop.skuformatid)) {
				view1.setTextColor(Color.parseColor("#E73C7B"));
				view1.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.pink_stroke_corner));
			} else {
				view1.setTextColor(Color.parseColor("#ffffff"));
				view1.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.pink_solid_corner));
			}
			view1.setPadding(15, 5, 15, 5);
			view1.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					// do something

					if (fragmentTop.skuformatid.equals("")) {
						((Button) v).setTextColor(Color.parseColor("#ffffff"));
						v.setBackgroundDrawable(getResources().getDrawable(
								R.drawable.pink_solid_corner));
					} else if (!fragmentTop.skuformatid.equals(v.getTag()
							.toString())) {
						((Button) v).setTextColor(Color.parseColor("#ffffff"));
						v.setBackgroundDrawable(getResources().getDrawable(
								R.drawable.pink_solid_corner));
						for (int i = 0; i < fragmentTop.productSkuListModels
								.get(1).skuValue.size(); i++) {
							if (btn[i].getTag().toString()
									.equals(fragmentTop.skuformatid)) {
								btn[i].setTextColor(Color.parseColor("#E73C7B"));
								btn[i].setBackgroundDrawable(getResources()
										.getDrawable(
												R.drawable.pink_stroke_corner));
							}
						}
					}

					fragmentTop.skuformatid = v.getTag().toString();
					fragmentTop.skuhandler.sendEmptyMessage(1);
				}

			});
			btn[i] = view1;
			mFlowLayout1.addView(view1, lp);

		}
	}

	OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.iv_cancel:
				if (popupWindow != null && popupWindow.isShowing()) {
					popupWindow.dismiss();
				}
				break;
			case R.id.pop2_ljjs:
				startActivity(new Intent(ProductDetailActivity.this,
						ShoppingCartActivity.class));
				break;
			case R.id.btn_affirm:
				if (fragmentTop.skucolorid == null
						|| fragmentTop.skucolorid.length() <= 1
						|| fragmentTop.skuformatid == null
						|| fragmentTop.skuformatid.length() <= 1) {
					ToastUtils.showShortToast(ProductDetailActivity.this,
							"请选择您要的规格");
				} else {
					try {
						ProductSkuLinkModel linkModel = dbUtils
								.findFirst(Selector
										.from(ProductSkuLinkModel.class)
										.where("skuFormatId", "=",
												fragmentTop.skuformatid)
										.and("skuColorId", "=",
												fragmentTop.skucolorid)
										.and("pid", "=", id));
						addCart(linkModel.id);

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				break;
			}

		}
	};

	public void setPopPrice(String price) {
		if (tv_price != null && popupWindow != null && popupWindow.isShowing()) {
			tv_price.setText("¥ " + price);
		}

	}

	private void popupWindowUpdate() {
		fragmentTop.colorAdapter.notifyDataSetChanged();
		fragmentTop.fromatAdapter.notifyDataSetChanged();
		popupWindow.update();
	}

	/**
	 * 添加购物车
	 */
	protected void addCart(Integer skuid) {

		tzmAddcartApi = new TzmAddcartApi();
		client = new ApiClient(this);
		if (skuid != null) {
			tzmAddcartApi.setSkuLinkId(skuid);
		}
		tzmAddcartApi.setUid(userModel.uid);
		tzmAddcartApi.setPid(id);
		tzmAddcartApi.setNum(1);
		tzmAddcartApi.setActivityId(activityId);
		client.api(tzmAddcartApi, new ApiListener() {

			@Override
			public void onStart() {
				ProgressDialogUtil.openProgressDialog(
						ProductDetailActivity.this, "", "");
			}

			@Override
			public void onException(Exception e) {
				// ProgressDialogUtil.closeProgressDialog();
				LogUtil.ErrorLog(e);
			}

			@Override
			public void onError(String error) {
				ProgressDialogUtil.closeProgressDialog();
				LogUtil.Log(error);
				ToastUtils.showShortToast(ProductDetailActivity.this, error);
			}

			@Override
			public void onComplete(String jsonStr) {
				ProgressDialogUtil.closeProgressDialog();
				if (StringUtils.isNotBlank(jsonStr)) {
					try {
						JSONObject jsonObject = new JSONObject(jsonStr);
						boolean success = jsonObject.optBoolean("success");
						// int result = jsonObject.optInt("result");
						String error_msg = jsonObject.optString("error_msg");

						if (success) {
							showpop2();
							loadNum();
							if (popupWindow != null && popupWindow.isShowing()) {
								popupWindow.dismiss();
							} else {
								ToastUtils.showShortToast(
										ProductDetailActivity.this, error_msg);
							}
						}

					} catch (JSONException e) {
						e.printStackTrace();
					}

				}

			}
		}, true);

	}

	/**
	 * 分享
	 */
	private final UMSocialService mController = UMServiceFactory
			.getUMSocialService("com.umeng.share");

	protected void share() {
		// 配置SSO
		mController.getConfig().setSsoHandler(new SinaSsoHandler());
		addWXPlatform();

		UMImage urlImage = new UMImage(ProductDetailActivity.this,
				productDetailModel.image);
		// 设置微信内容
		WeiXinShareContent weixinContent = new WeiXinShareContent();
		weixinContent.setShareContent(productDetailModel.name);
		weixinContent.setTitle("淘竹马");
		weixinContent
				.setTargetUrl("http://weixinm2c.taozhuma.com/product_detail?product_id="
						+ id);
		weixinContent.setShareMedia(urlImage);
		mController.setShareMedia(weixinContent);
		// 新浪内容
		SinaShareContent sinaContent = new SinaShareContent();
		sinaContent.setTitle("淘竹马");
		sinaContent.setShareContent(productDetailModel.name
				+ ",http://weixinm2c.taozhuma.com/product_detail?product_id="
				+ id);
		sinaContent.setShareImage(urlImage);
		// sinaContent
		// .setTargetUrl("http://weixinm2c.taozhuma.com/product_detail?product_id="
		// + id);
		mController.setShareMedia(sinaContent);
		// 设置微信朋友圈分享内容
		CircleShareContent circleMedia = new CircleShareContent();
		circleMedia.setShareContent(productDetailModel.name);
		// 设置朋友圈title
		circleMedia.setTitle("淘竹马");
		circleMedia.setShareImage(urlImage);
		circleMedia
				.setTargetUrl("http://weixinm2c.taozhuma.com/product_detail?product_id="
						+ id);
		mController.setShareMedia(circleMedia);

		mController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN,
				SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA);
		mController.openShare(ProductDetailActivity.this, false);
	}

	/**
	 * @功能描述 : 添加微信平台分享
	 * @return
	 */
	private void addWXPlatform() {
		// 注意：在微信授权的时候，必须传递appSecret
		// wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
		String appId = "wx88fa4c9662539a8f";
		String appSecret = "bf5cb0a3b32076886049d61eac6cfe06";
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(ProductDetailActivity.this,
				appId, appSecret);
		wxHandler.addToSocialSDK();

		// 支持微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(
				ProductDetailActivity.this, appId, appSecret);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK
				&& requestCode == AppConfig.REQUESTCODE_LOGIN) {

			checkLogin();// 判断登陆
			try {
				searhFavorite();
			} catch (Exception e) {
				e.printStackTrace();
			}
			// searhFavorite();// 判断收藏

			// if (isLogin && userModel.type == 6) {
			// ll_bottom.setVisibility(View.GONE);
			// view_bottom.setVisibility(View.GONE);
			// } else {
			// ll_bottom.setVisibility(View.VISIBLE);
			// view_bottom.setVisibility(View.VISIBLE);
			// }
		} else {
			UMSsoHandler ssoHandler = SocializeConfig.getSocializeConfig()
					.getSsoHandler(requestCode);
			if (ssoHandler != null) {
				ssoHandler.authorizeCallBack(requestCode, resultCode, data);
			}
		}

	}

	/**
	 * 购物车数量
	 */
	private void loadNum() {
		if (!isLogin)
			return;
		apiClient = new ApiClient(this);
		cartNumApi = new TzmMyCartNumApi();
		cartNumModel = new TzmMyCartNumModel();
		cartNumApi.setUid(userModel.uid);
		apiClient.api(this.cartNumApi, new ApiListener() {
			@Override
			public void onStart() {
			}

			@Override
			public void onComplete(String jsonStr) {
				try {
					Gson gson = new Gson();
					Type type = new TypeToken<BaseModel<TzmMyCartNumModel>>() {
					}.getType();
					BaseModel<TzmMyCartNumModel> base = gson.fromJson(jsonStr,
							type);

					if (base.result != null) {
						cartNumModel = base.result;
						badgeView.setBadgeCount(cartNumModel.num);
					}
				} catch (Exception e) {
					e.printStackTrace();
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
	 * 收藏判断
	 */
	public void searhFavorite() {
		checkLogin();
		if (!isLogin) {
			return;
		}
		client2 = new ApiClient(this);
		searchFavoriteApi = new SearchFavoriteApi();
		searchFavoriteApi.setUid(userModel.uid);
		searchFavoriteApi.setFavType(1);
		searchFavoriteApi.setFavSenId(id);
		searchFavoriteApi.setactivityId(activityId);
		client2.api(searchFavoriteApi, new ApiListener() {

			@Override
			public void onStart() {
				// ProgressDialogUtil.openProgressDialog(B2bGoodsDetaliActivity.this,
				// "", "");
			}

			@Override
			public void onException(Exception e) {
				// ProgressDialogUtil.closeProgressDialog();
				LogUtil.ErrorLog(e);
			}

			@Override
			public void onError(String error) {
				// ProgressDialogUtil.closeProgressDialog();
				LogUtil.Log(error);
			}

			@Override
			public void onComplete(String jsonStr) {
				// ProgressDialogUtil.closeProgressDialog();
				if (StringUtils.isNotBlank(jsonStr)) {
					try {
						JSONObject jsonObject = new JSONObject(jsonStr);
						// boolean success = jsonObject.optBoolean("success");
						int result = jsonObject.optInt("result");
						// String error_msg = jsonObject.optString("error_msg");
						handler.sendEmptyMessage(result);

					} catch (JSONException e) {
						e.printStackTrace();
					}

				}

			}
		}, true);

	}

	/**
	 * 添加收藏
	 * 
	 * @param favType
	 */
	protected void addFavorite(final Integer favType) {

		if (!isLogin) {
			ToastUtils.showShortToast(ProductDetailActivity.this, "请先登录！");
			Intent intent = new Intent(new Intent(ProductDetailActivity.this,
					TzmLoginRegisterActivity.class));
			intent.putExtra("type", 0);
			startActivityForResult(intent, AppConfig.REQUESTCODE_LOGIN);
			return;
		}
		client2 = new ApiClient(ProductDetailActivity.this);
		addfavoritesApi = new AddfavoritesApi();
		addfavoritesApi.setUid(userModel.uid);
		addfavoritesApi.setFavSenId(id);
		addfavoritesApi.setFavType(favType);
		addfavoritesApi.setactivityId(activityId);
		client2.api(addfavoritesApi, new ApiListener() {

			@Override
			public void onStart() {
				ProgressDialogUtil.openProgressDialog(
						ProductDetailActivity.this, "", "");
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
				ToastUtils.showShortToast(ProductDetailActivity.this, error);
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
						ToastUtils.showShortToast(ProductDetailActivity.this,
								error_msg);
						if (success && result == 1 && favType != 3) {
							handler.sendEmptyMessage(0);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}

			}
		}, true);

	}

	/**
	 * 删除收藏
	 */
	protected void delFavorite() {
		if (!isLogin) {
			return;
		}
		delSingleFavoriteApi = new DelSingleFavoriteApi();
		delSingleFavoriteApi.setUid(userModel.uid);
		delSingleFavoriteApi.setFavType(1);
		delSingleFavoriteApi.setFavSenId(id);
		delSingleFavoriteApi.setactivityId(activityId);
		client2.api(delSingleFavoriteApi, new ApiListener() {

			@Override
			public void onStart() {
				ProgressDialogUtil.openProgressDialog(
						ProductDetailActivity.this, "", "");
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
				ToastUtils.showShortToast(ProductDetailActivity.this, error);
			}

			@Override
			public void onComplete(String jsonStr) {
				ProgressDialogUtil.closeProgressDialog();
				if (StringUtils.isNotBlank(jsonStr)) {
					try {
						JSONObject jsonObject = new JSONObject(jsonStr);
						boolean success = jsonObject.optBoolean("success");
						String error_msg = jsonObject.optString("error_msg");
						ToastUtils.showShortToast(ProductDetailActivity.this,
								error_msg);
						if (success) {
							handler.sendEmptyMessage(1);
						}

					} catch (JSONException e) {
						e.printStackTrace();
					}

				}

			}
		}, true);

	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				iv_collcet.setVisibility(View.GONE);
				iv_cancel_collcet.setVisibility(View.VISIBLE);
				break;
			case 1:
				iv_cancel_collcet.setVisibility(View.GONE);
				iv_collcet.setVisibility(View.VISIBLE);
				break;
			}
		};
	};

	@Override
	protected void onDestroy() {
		handler.removeCallbacksAndMessages(null);
		mController.getConfig().cleanListeners();
		super.onDestroy();

	}

}
