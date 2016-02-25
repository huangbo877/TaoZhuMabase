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
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.adapter.TzmCommentListAdapter;
import com.ruiyu.taozhuma.adapter.ViewPagerAdapter2;
import com.ruiyu.taozhuma.adapter.ViewpagerImageAdapter;
import com.ruiyu.taozhuma.api.AddfavoritesApi;
import com.ruiyu.taozhuma.api.ApiClient;
import com.ruiyu.taozhuma.api.ApiListener;
import com.ruiyu.taozhuma.api.DelfavoriteApi;
import com.ruiyu.taozhuma.api.SearchFavoriteApi;
import com.ruiyu.taozhuma.api.TzmMyCartNumApi;
import com.ruiyu.taozhuma.api.TzmProductDetailApi;
import com.ruiyu.taozhuma.api.TzmProductDetailCommentApi;
import com.ruiyu.taozhuma.api.TzmProductFocusImgApi;
import com.ruiyu.taozhuma.base.xUtilsImageLoader;
import com.ruiyu.taozhuma.config.AppConfig;
import com.ruiyu.taozhuma.model.BaseModel;
import com.ruiyu.taozhuma.model.TzmMyCartNumModel;
import com.ruiyu.taozhuma.model.TzmProductDetailCommentModel;
import com.ruiyu.taozhuma.model.TzmProductDetailModel;
import com.ruiyu.taozhuma.model.UserModel;
import com.ruiyu.taozhuma.utils.LogUtil;
import com.ruiyu.taozhuma.utils.ProgressDialogUtil;
import com.ruiyu.taozhuma.utils.StringUtils;
import com.ruiyu.taozhuma.utils.ToastUtils;
import com.ruiyu.taozhuma.utils.UserInfoUtils;
import com.ruiyu.taozhuma.widget.CustomViewPager;
import com.ruiyu.taozhuma.widget.InnerScrollView;
import com.ruiyu.taozhuma.widget.RushBuyCountDownTimerView;
import com.ruiyu.taozhuma.widget.StickyScrollView;
import com.ruiyu.taozhuma.widget.WebViewForViewPager;
import com.ruiyu.taozhuma.widget.RushBuyCountDownTimerView.TimeOverListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

/*
 * 秒杀活动商品详情
 * fu
 */
public class TzmTimeActivityProductDetailActivity extends FragmentActivity {
	private RelativeLayout rl_title;
	private LinearLayout ll_sticky;
	private StickyScrollView sticky_scrollview;
	private int height;
	// private TextView tv_sale_count;
	private TextView tv_price, tv_price2;
	private TextView txt_head_title, tv_product_name, tv_shop_name;
	private ImageView iv_shop_image;
	private ImageView btn_head_left;
	// 接口调用
	private ApiClient client, apiClient;
	private TzmProductDetailModel productDetailModel;
	private TzmProductDetailApi tzmProductDetailApi;

	private Boolean isLogin;
	private UserModel userModel;

	private Integer id;
	// 宝贝详情
	private TextView tv_pname, tv_product_num, tv_ispower, tv_pack,
			tv_location, tv_texture, tv_weight, tv_lowest_price;

	private RelativeLayout rl_shop_detail;
	// 猜你喜欢
	private ImageView im_like01, im_like02, im_like03, im_like04;
	private TextView tv_like_name01, tv_like_name02, tv_like_name03,
			tv_like_name04;
	private TextView tv_like_price01, tv_like_price02, tv_like_price03,
			tv_like_price04;
	private RelativeLayout rl_like01, rl_like02, rl_like03, rl_like04;

	// 焦点图
	private ApiClient client2, client3;
	private TzmProductFocusImgApi tzmProductFocusImgApi;
	private ArrayList<TzmProductDetailModel> list;

	private LinearLayout linearLayout;
	private ViewPager viewPager1;
	private LayoutInflater inflater;
	private ImageView[] tips;
	private List<View> item;
	private String[] urls;
	public ViewpagerImageAdapter adapter1;
	// private BitmapUtils bitmapUtils;
	private xUtilsImageLoader imageLoader;
	// 图文详情 买家口碑
	private TextView viewpager_t1, viewpager_t2;
	private CustomViewPager viewPager2;
	private ArrayList<View> listViews;
	private View image_list, comment_list;
	private WebViewForViewPager wv_imglist;
	private TextView tv_content;
	private InnerScrollView scrollview;
	// private LinearLayout image_list_llayout;

	private TzmCommentListAdapter tzmHotProductAdapter;
	private ListView listView;
	private ApiClient client4;
	private TzmProductDetailCommentApi api;
	private List<TzmProductDetailCommentModel> commentModels;

	PointF downP = new PointF();
	/** 触摸时当前的点 **/
	PointF curP = new PointF();

	private float downY;

	boolean status = false;
	boolean isTouch = false;

	// 收藏
	private ImageView iv_cancel_collcet, iv_collcet;
	private SearchFavoriteApi searchFavoriteApi;
	private AddfavoritesApi addfavoritesApi;
	private DelfavoriteApi delfavoriteApi;

	private LinearLayout ll_bottom;
	private View view_bottom;

	private RelativeLayout rl_cart;
	private TextView tv_count;
	private TzmMyCartNumApi cartNumApi;
	private TzmMyCartNumModel cartNumModel;

	@ViewInject(R.id.ll_score)
	private LinearLayout ll_score;
	@ViewInject(R.id.tv_productCmmt)
	private TextView tv_productCmmt;
	@ViewInject(R.id.tv_deliverCmmt)
	private TextView tv_deliverCmmt;
	@ViewInject(R.id.tv_logisticsCmmt)
	private TextView tv_logisticsCmmt;
	@ViewInject(R.id.iv_gotop)
	private ImageView iv_gotop;
	@ViewInject(R.id.tv_tips)
	private TextView tv_tips;
	@ViewInject(R.id.timeview)
	private RushBuyCountDownTimerView timerView;
	@ViewInject(R.id.tv_sellingPrice)
	private TextView tv_sellingPrice;// 原价

	// 立即购买
	@ViewInject(R.id.btn_buy)
	private Button btn_buy;

	@ViewInject(R.id.iv_share)
	private ImageView iv_share;// 分享

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tzm_product_time_activity_detail_activity);
		id = getIntent().getIntExtra("id", 0);
		imageLoader = new xUtilsImageLoader(this);
		ViewUtils.inject(this);
		initview();
		checkLogin();
		if (isLogin && userModel.type == 6) {
			ll_bottom.setVisibility(View.GONE);
			view_bottom.setVisibility(View.GONE);
		} else {
			ll_bottom.setVisibility(View.VISIBLE);
			view_bottom.setVisibility(View.VISIBLE);
		}
		// 判断收藏
		searhFavorite();
		// 加载数据
		loadData();
		// 加载焦点图
		loadImg();

		// 图文详情 买家口碑
		loadImageComment();
		// 加载购物车数量
		loadNum();

	}

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
				Gson gson = new Gson();
				Type type = new TypeToken<BaseModel<TzmMyCartNumModel>>() {
				}.getType();
				BaseModel<TzmMyCartNumModel> base = gson
						.fromJson(jsonStr, type);

				if (base.result != null) {
					cartNumModel = base.result;
					if (cartNumModel.num > 0) {
						tv_count.setVisibility(View.VISIBLE);
						tv_count.setText(cartNumModel.num + "");
					} else {
						tv_count.setVisibility(View.GONE);
					}
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
	 * 产品详情
	 */
	@SuppressLint({ "ClickableViewAccessibility", "InflateParams" })
	@SuppressWarnings("deprecation")
	protected void initview() {
		WindowManager wm = (WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE);

		height = wm.getDefaultDisplay().getHeight();
		rl_cart = (RelativeLayout) findViewById(R.id.rl_cart);
		rl_cart.setOnClickListener(clickListener);
		tv_count = (TextView) findViewById(R.id.tv_count);
		rl_title = (RelativeLayout) findViewById(R.id.rl_title);
		ll_sticky = (LinearLayout) findViewById(R.id.ll_sticky);
		sticky_scrollview = (StickyScrollView) findViewById(R.id.sticky_scrollview);
		// btn_addfenxiao = (Button) findViewById(R.id.btn_addfenxiao);
		// btn_addfenxiao.setOnClickListener(clickListener);
		btn_buy.setOnClickListener(clickListener);
		// 初始化
		client = new ApiClient(this);
		client2 = new ApiClient(this);
		tzmProductDetailApi = new TzmProductDetailApi();
		productDetailModel = new TzmProductDetailModel();
		tzmProductFocusImgApi = new TzmProductFocusImgApi();
		list = new ArrayList<TzmProductDetailModel>();
		tv_price = (TextView) findViewById(R.id.tv_price);
		tv_price2 = (TextView) findViewById(R.id.tv_price2);
		txt_head_title = (TextView) findViewById(R.id.txt_head_title);
		txt_head_title.setText("商品详情");
		tv_product_name = (TextView) findViewById(R.id.tv_product_name);
		btn_head_left = (ImageView) findViewById(R.id.btn_head_left);
		btn_head_left.setOnClickListener(clickListener);
		iv_shop_image = (ImageView) findViewById(R.id.iv_shop_image);
		tv_shop_name = (TextView) findViewById(R.id.tv_shop_name);

		tv_pname = (TextView) findViewById(R.id.tv_pname);
		tv_product_num = (TextView) findViewById(R.id.tv_product_num);
		tv_ispower = (TextView) findViewById(R.id.tv_ispower);
		tv_pack = (TextView) findViewById(R.id.tv_pack);
		tv_location = (TextView) findViewById(R.id.tv_location);
		tv_texture = (TextView) findViewById(R.id.tv_texture);
		tv_weight = (TextView) findViewById(R.id.tv_weight);
		tv_lowest_price = (TextView) findViewById(R.id.tv_lowest_price);
		rl_shop_detail = (RelativeLayout) findViewById(R.id.rl_shop_detail);
		linearLayout = (LinearLayout) findViewById(R.id.viewGroup);
		viewPager1 = (ViewPager) findViewById(R.id.viewpager1);
		viewPager1.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					sticky_scrollview.requestDisallowInterceptTouchEvent(false);
				} else {
					sticky_scrollview.requestDisallowInterceptTouchEvent(true);
				}
				return false;
			}
		});
		viewPager1.setOffscreenPageLimit(5);
		item = new ArrayList<View>();
		inflater = LayoutInflater.from(this);

		iv_cancel_collcet = (ImageView) findViewById(R.id.iv_cancel_collcet);
		iv_collcet = (ImageView) findViewById(R.id.iv_collcet);
		iv_cancel_collcet.setOnClickListener(clickListener);
		iv_collcet.setOnClickListener(clickListener);

		viewPager2 = (CustomViewPager) findViewById(R.id.viewpager2);
		listViews = new ArrayList<View>();

		image_list = inflater.inflate(R.layout.image_list_layout, null);
		comment_list = inflater.inflate(R.layout.comment_list_layout, null);

		scrollview = (InnerScrollView) image_list.findViewById(R.id.scrollview);
		// image_list_llayout = (LinearLayout) image_list
		// .findViewById(R.id.image_list_llayout);
		im_like01 = (ImageView) image_list.findViewById(R.id.im_like01);
		im_like02 = (ImageView) image_list.findViewById(R.id.im_like02);
		im_like03 = (ImageView) image_list.findViewById(R.id.im_like03);
		im_like04 = (ImageView) image_list.findViewById(R.id.im_like04);
		tv_like_name01 = (TextView) image_list
				.findViewById(R.id.tv_like_name01);
		tv_like_name02 = (TextView) image_list
				.findViewById(R.id.tv_like_name02);
		tv_like_name03 = (TextView) image_list
				.findViewById(R.id.tv_like_name03);
		tv_like_name04 = (TextView) image_list
				.findViewById(R.id.tv_like_name04);
		tv_like_price01 = (TextView) image_list
				.findViewById(R.id.tv_like_price01);
		tv_like_price02 = (TextView) image_list
				.findViewById(R.id.tv_like_price02);
		tv_like_price03 = (TextView) image_list
				.findViewById(R.id.tv_like_price03);
		tv_like_price04 = (TextView) image_list
				.findViewById(R.id.tv_like_price04);
		rl_like01 = (RelativeLayout) image_list.findViewById(R.id.rl_like01);
		rl_like02 = (RelativeLayout) image_list.findViewById(R.id.rl_like02);
		rl_like03 = (RelativeLayout) image_list.findViewById(R.id.rl_like03);
		rl_like04 = (RelativeLayout) image_list.findViewById(R.id.rl_like04);

		listViews.add(image_list);
		listViews.add(comment_list);
		viewPager2.setAdapter(new ViewPagerAdapter2(listViews));
		viewPager2.setCurrentItem(0);
		viewPager2.setOnPageChangeListener(new MyOnPageChangeListener(2));
		viewpager_t1 = (TextView) findViewById(R.id.viewpager_t1);
		viewpager_t2 = (TextView) findViewById(R.id.viewpager_t2);
		viewpager_t1.setOnClickListener(clickListener);
		viewpager_t2.setOnClickListener(clickListener);

		ll_bottom = (LinearLayout) findViewById(R.id.ll_bottom);
		view_bottom = (View) findViewById(R.id.view_bottom);
		scrollview.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP)
					sticky_scrollview.requestDisallowInterceptTouchEvent(false);
				else if (sticky_scrollview.getIsTop()) {
					sticky_scrollview.requestDisallowInterceptTouchEvent(true);
				} else {
					sticky_scrollview.requestDisallowInterceptTouchEvent(false);
				}
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					curP.x = event.getX();
					curP.y = event.getY();
					if (sticky_scrollview.getIsTop()) {
						sticky_scrollview
								.requestDisallowInterceptTouchEvent(true);
					} else {
						sticky_scrollview
								.requestDisallowInterceptTouchEvent(false);
					}
					break;
				case MotionEvent.ACTION_MOVE:
					float lastY = event.getY(event.getPointerCount() - 1);
					// if (scrollview.isBottom())// 如果到达底部，先设置为不能滚动
					// sticky_scrollview.requestDisallowInterceptTouchEvent(false);
					// // 如果到达底部，但开始向上滚动，那么webview可以滚动
					// if (scrollview.isBottom() && (curP.y - lastY < 0))
					// sticky_scrollview.requestDisallowInterceptTouchEvent(true);
					if (scrollview.isTop())// 滑到顶部不能再滑
						sticky_scrollview
								.requestDisallowInterceptTouchEvent(false);
					if (scrollview.isTop() && (curP.y - lastY > 0))// 滑动到顶部，向下滑，可以滑到
						sticky_scrollview
								.requestDisallowInterceptTouchEvent(true);
					break;
				case MotionEvent.ACTION_UP:
					sticky_scrollview.requestDisallowInterceptTouchEvent(false);
					if (scrollview.getScrollY() > 100) {
						showView();
					} else {
						hideView();
					}
					break;

				}
				return false;
			}
		});

		wv_imglist = (WebViewForViewPager) image_list
				.findViewById(R.id.wv_imglist);

		tv_content = (TextView) comment_list.findViewById(R.id.tv_content);
		listView = (ListView) comment_list.findViewById(R.id.lv_commentlist);
		listView.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					curP.x = event.getX();
					curP.y = event.getY();
					sticky_scrollview.requestDisallowInterceptTouchEvent(true);
					break;
				case MotionEvent.ACTION_MOVE:
					float lastY = event.getY(event.getPointerCount() - 1);
					if (listView.getLastVisiblePosition() == (listView
							.getCount() - 1))
						sticky_scrollview
								.requestDisallowInterceptTouchEvent(false);
					else if (listView.getLastVisiblePosition() == (listView
							.getCount() - 1) && (curP.y - lastY < 0))
						sticky_scrollview
								.requestDisallowInterceptTouchEvent(true);
					else if (listView.getFirstVisiblePosition() == 0)
						sticky_scrollview
								.requestDisallowInterceptTouchEvent(false);
					else if (listView.getFirstVisiblePosition() == 0
							&& (curP.y - lastY > 0))// 滑动到顶部，向下滑，可以滑到
						sticky_scrollview
								.requestDisallowInterceptTouchEvent(true);
					break;
				case MotionEvent.ACTION_UP:
					sticky_scrollview.requestDisallowInterceptTouchEvent(false);
					break;
				}
				return false;
			}
		});

		RelativeLayout.LayoutParams linearParams2 = (RelativeLayout.LayoutParams) listView
				.getLayoutParams();
		linearParams2.height = height
				- (rl_title.getHeight() + ll_sticky.getHeight());
		listView.setLayoutParams(linearParams2);

		client4 = new ApiClient(this);
		api = new TzmProductDetailCommentApi();
		commentModels = new ArrayList<TzmProductDetailCommentModel>();

		sticky_scrollview.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP
						|| event.getAction() == MotionEvent.ACTION_SCROLL) {
					if (sticky_scrollview.getScrollY() > 1500) {
						showView();
					} else {
						hideView();
					}
				}
				return false;
			}
		});
		iv_gotop.setOnClickListener(clickListener);
		iv_share.setOnClickListener(clickListener);
	}

	private void showView() {
		if (iv_gotop.getVisibility() == View.GONE) {
			iv_gotop.setVisibility(View.VISIBLE);
			viewhandler.sendEmptyMessageDelayed(0, 4000);
		}
	}

	private void hideView() {
		if (iv_gotop.getVisibility() == View.VISIBLE) {
			iv_gotop.setVisibility(View.GONE);
		}
	}

	@SuppressLint("HandlerLeak")
	private Handler viewhandler = new Handler() {
		public void handleMessage(Message msg) {
			hideView();
		};
	};

	// 计算dp转px
	public int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	protected void setView() {
		if (StringUtils.isNotEmpty(productDetailModel.productCmmt)) {
			ll_score.setVisibility(View.VISIBLE);
			tv_productCmmt.setText(productDetailModel.productCmmt);
			tv_deliverCmmt.setText(productDetailModel.deliverCmmt);
			tv_logisticsCmmt.setText(productDetailModel.logisticsCmmt);
		}
		imageLoader.display(iv_shop_image, productDetailModel.shopImage);
		// 加标示
		String str = "今日秒杀" + productDetailModel.name;
		SpannableStringBuilder mSpannableStringBuilder = new SpannableStringBuilder(
				str);
		mSpannableStringBuilder.setSpan(new ForegroundColorSpan(getResources()
				.getColor(R.color.base)), 0, 4,
				Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		tv_product_name.setText(mSpannableStringBuilder);
		tv_price.setText("¥ " + productDetailModel.ladderPrice);
		tv_price2.setText("¥ " + productDetailModel.ladderPrice);

		tv_shop_name.setText(productDetailModel.shopName);
		rl_shop_detail.setOnClickListener(clickListener);
		// 宝贝详情
		tv_pname.setText(productDetailModel.name);
		tv_ispower.setText(productDetailModel.isPower);
		tv_pack.setText(productDetailModel.pack);
		// tv_age.setText(productDetailModel.age);
		tv_weight.setText(productDetailModel.weight + "kg");
		tv_location.setText(productDetailModel.location);
		tv_texture.setText(productDetailModel.texture);
		tv_product_num.setText(productDetailModel.model);
		tv_lowest_price.setText(productDetailModel.lowest_price + "");
		// 商品原价
		tv_sellingPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		tv_sellingPrice.setText("¥ " + productDetailModel.sellingPrice);
		// 打折
		tv_tips.setText(productDetailModel.discount + "折");
		timerView.turnBlackStyle();
		// 开始倒计时
		try {
			startTimeView();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 猜你喜欢
		if (productDetailModel.attribute != null) {
			int size = productDetailModel.attribute.size();
			if (size > 0) {
				rl_like01.setVisibility(View.VISIBLE);
				imageLoader.display(im_like01,
						productDetailModel.attribute.get(0).image);
				tv_like_name01
						.setText(productDetailModel.attribute.get(0).name);
				tv_like_price01.setText("¥ "
						+ productDetailModel.attribute.get(0).price);
				// rl_like01.setOnClickListener(clickListener);
				rl_like01.setOnTouchListener(touchListener);
				rl_like01.setTag(0);
			}
			if (size > 1) {
				rl_like02.setVisibility(View.VISIBLE);
				imageLoader.display(im_like02,
						productDetailModel.attribute.get(1).image);
				tv_like_name02
						.setText(productDetailModel.attribute.get(1).name);
				tv_like_price02.setText("¥ "
						+ productDetailModel.attribute.get(1).price);
				// rl_like02.setOnClickListener(clickListener);
				rl_like02.setOnTouchListener(touchListener);
				rl_like02.setTag(1);
			}
			if (size > 2) {
				rl_like03.setVisibility(View.VISIBLE);
				imageLoader.display(im_like03,
						productDetailModel.attribute.get(2).image);
				tv_like_name03
						.setText(productDetailModel.attribute.get(2).name);
				tv_like_price03.setText("¥ "
						+ productDetailModel.attribute.get(2).price);
				// rl_like03.setOnClickListener(clickListener);
				rl_like03.setOnTouchListener(touchListener);
				rl_like03.setTag(2);
			}
			if (size > 3) {
				rl_like04.setVisibility(View.VISIBLE);
				imageLoader.display(im_like04,
						productDetailModel.attribute.get(3).image);
				tv_like_name04
						.setText(productDetailModel.attribute.get(3).name);
				tv_like_price04.setText("¥ "
						+ productDetailModel.attribute.get(3).price);
				// rl_like04.setOnClickListener(clickListener);
				rl_like04.setOnTouchListener(touchListener);
				rl_like04.setTag(3);
			}
		}
	}

	/**
	 * 开始倒计时
	 * 
	 * @param model
	 * @throws ParseException
	 */
	@SuppressLint("SimpleDateFormat")
	protected void startTimeView() throws ParseException {
		if (productDetailModel.isActive == 1) {
			// 活动时间格式
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"yyyy-MM-ddHH:mm");
			// 当天日期
			SimpleDateFormat tdateFormat = new SimpleDateFormat("yyyy-MM-dd");
			// 当前时间
			Date nowdate = new Date();
			// 开始时间//结束时间
			Date time = null;
			String today = tdateFormat.format(nowdate);
			// 时间差距
			long temp = 0;// 相差毫秒
			// 进行中
			time = dateFormat.parse(today + productDetailModel.endTime);
			temp = time.getTime() - nowdate.getTime();

			long hours = temp / 1000 / 3600; // 相差小时数
			long temp2 = temp % (1000 * 3600);
			long mins = temp2 / 1000 / 60; // 相差分钟数
			long sec = temp / 1000 % 60;// 相差秒
			timerView.setTime(hours, mins, sec);
			timerView.setOnTimeOverListener(new TimeOverListener() {

				@Override
				public void isTimeOver(boolean over) {
					finish();
				}
			});
			timerView.start();
			// LogUtil.Log(dateFormat.format(date));
		}
	}

	View.OnTouchListener touchListener = new View.OnTouchListener() {

		@SuppressLint("ClickableViewAccessibility")
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				downY = event.getY();
				sticky_scrollview.requestDisallowInterceptTouchEvent(true);
				viewPager2.requestDisallowInterceptTouchEvent(true);
				break;
			case MotionEvent.ACTION_UP:
				float lastY = event.getY();
				if (downY == lastY) {
					Intent intent4 = new Intent(
							TzmTimeActivityProductDetailActivity.this,
							TzmTimeActivityProductDetailActivity.class);
					intent4.putExtra("id",
							productDetailModel.attribute.get(Integer.parseInt(v
									.getTag().toString())).productId);
					startActivity(intent4);
				}
				break;

			}
			return true;
		}
	};

	/*
	 * 加载图片详情，买家口碑
	 */
	@SuppressLint("SetJavaScriptEnabled")
	private void loadImageComment() {
		WebSettings settings = wv_imglist.getSettings();
		settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		wv_imglist.getSettings().setJavaScriptEnabled(true);
		wv_imglist.loadUrl(AppConfig.TZM_PRODUCTDETAILHTML_URL + "?id=" + id);

		api.setId(id);
		if (isLogin) {
			api.setUid(userModel.uid);
		}
		client4.api(this.api, new ApiListener() {
			@Override
			public void onStart() {
				// ProgressDialogUtil.openProgressDialog(
				// NewFragment.this.getActivity(), "", "正在加载...");
			}

			@Override
			public void onComplete(String jsonStr) {
				// ProgressDialogUtil.closeProgressDialog();
				Gson gson = new Gson();
				Type type = new TypeToken<BaseModel<ArrayList<TzmProductDetailCommentModel>>>() {
				}.getType();
				BaseModel<ArrayList<TzmProductDetailCommentModel>> base = gson
						.fromJson(jsonStr, type);

				if (base.result != null && base.result.size() > 0) {
					listView.setVisibility(View.VISIBLE);
					tv_content.setVisibility(View.GONE);
					commentModels.addAll(base.result);
					tzmHotProductAdapter = new TzmCommentListAdapter(
							TzmTimeActivityProductDetailActivity.this,
							commentModels);
					listView.setAdapter(tzmHotProductAdapter);
					viewpager_t2.setText("买家口碑（" + base.result.size() + "）");
				} else {
					// ToastUtils.showShortToast(
					// TzmProductDetailActivity2.this, base.error_msg);
					listView.setVisibility(View.GONE);
					tv_content.setText("暂无评论！");
					tv_content.setVisibility(View.VISIBLE);
					viewpager_t2.setText("买家口碑（0）");
				}

			}

			@Override
			public void onError(String error) {
				ToastUtils.showShortToast(
						TzmTimeActivityProductDetailActivity.this,
						R.string.msg_list_null);
				// ProgressDialogUtil.closeProgressDialog();
			}

			@Override
			public void onException(Exception e) {
				// ProgressDialogUtil.closeProgressDialog();
				LogUtil.ErrorLog(e);
			}
		}, true);
		// } else {
		// tv_content.setText("请登录！");
		// }
	}

	/*
	 * 加载产品数据
	 */
	private void loadData() {

		tzmProductDetailApi.setId(id);
		if (isLogin) {
			tzmProductDetailApi.setUid(userModel.uid);
		}

		client.api(this.tzmProductDetailApi, new ApiListener() {
			@Override
			public void onStart() {
				ProgressDialogUtil.openProgressDialog(
						TzmTimeActivityProductDetailActivity.this, "", "");
			}

			@Override
			public void onComplete(String jsonStr) {
				ProgressDialogUtil.closeProgressDialog();
				Gson gson = new Gson();
				Type type = new TypeToken<BaseModel<TzmProductDetailModel>>() {
				}.getType();
				BaseModel<TzmProductDetailModel> base = gson.fromJson(jsonStr,
						type);

				if (base.result != null) {
					productDetailModel = base.result;
					setView();
				} else {
					ToastUtils.showShortToast(
							TzmTimeActivityProductDetailActivity.this,
							base.error_msg);
				}

			}

			@Override
			public void onError(String error) {
				ToastUtils.showShortToast(
						TzmTimeActivityProductDetailActivity.this,
						R.string.msg_list_null);
				ProgressDialogUtil.closeProgressDialog();
			}

			@Override
			public void onException(Exception e) {
				ProgressDialogUtil.closeProgressDialog();
				LogUtil.ErrorLog(e);
			}
		}, true);

	}

	/*
	 * 产品焦点图
	 */
	private void loadImg() {
		tzmProductFocusImgApi.setId(id);
		client2.api(this.tzmProductFocusImgApi, new ApiListener() {
			@Override
			public void onStart() {
			}

			@SuppressLint("InflateParams")
			@Override
			public void onComplete(String jsonStr) {
				Gson gson = new Gson();
				Type type = new TypeToken<BaseModel<ArrayList<TzmProductDetailModel>>>() {
				}.getType();
				BaseModel<ArrayList<TzmProductDetailModel>> base = gson
						.fromJson(jsonStr, type);

				if (base.result != null) {
					list = base.result;
					urls = new String[list.size()];
					for (int i = 0; i < list.size(); i++) {
						urls[i] = list.get(i).image;
					}

					tips = new ImageView[list.size()];
					View v;
					for (int i = 0; i < list.size(); i++) {
						ImageView mImageView = new ImageView(
								TzmTimeActivityProductDetailActivity.this);
						tips[i] = mImageView;
						LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
								new ViewGroup.LayoutParams(
										LayoutParams.WRAP_CONTENT,
										LayoutParams.MATCH_PARENT));
						layoutParams.rightMargin = 8;
						layoutParams.leftMargin = 8;

						mImageView.setBackgroundResource(R.drawable.dian2);
						linearLayout.addView(mImageView, layoutParams);

						v = inflater.inflate(R.layout.viewpager_item, null);
						item.add(v);
					}

					adapter1 = new ViewpagerImageAdapter(item, urls,
							imageLoader);
					viewPager1.setAdapter(adapter1);
					viewPager1
							.setOnPageChangeListener(new MyOnPageChangeListener(
									1));
					setImageBackground(0);
				} else {
					ToastUtils.showShortToast(
							TzmTimeActivityProductDetailActivity.this,
							base.error_msg);
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

	public class MyOnPageChangeListener implements OnPageChangeListener {
		int type;

		public MyOnPageChangeListener(int type) {
			this.type = type;
		}

		@Override
		public void onPageSelected(int index) {
			if (type == 1) {
				setImageBackground(index);
			}
			if (type == 2) {
				switch (index) {
				case 0:
					viewpager_t1.setTextColor(Color.parseColor("#e61d58"));
					viewpager_t2.setTextColor(Color.parseColor("#888889"));
					break;
				case 1:
					viewpager_t1.setTextColor(Color.parseColor("#888889"));
					viewpager_t2.setTextColor(Color.parseColor("#e61d58"));
					break;

				}
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

	private void setImageBackground(int selectItems) {
		for (int i = 0; i < tips.length; i++) {
			if (i == selectItems) {
				tips[i].setBackgroundResource(R.drawable.dianshi);
			} else {
				tips[i].setBackgroundResource(R.drawable.dian2);
			}
		}
	}

	private void checkLogin() {
		isLogin = UserInfoUtils.isLogin();
		if (isLogin) {
			userModel = UserInfoUtils.getUserInfo();
		}

	}

	/*
	 * 判断收藏
	 */
	private void searhFavorite() {

		if (!isLogin) {
			return;
		}
		client3 = new ApiClient(this);
		searchFavoriteApi = new SearchFavoriteApi();
		// uid = userInfo.uid;
		searchFavoriteApi.setUid(userModel.uid);
		searchFavoriteApi.setFavType(1);
		searchFavoriteApi.setFavSenId(id);
		client3.api(searchFavoriteApi, new ApiListener() {

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
			case 10:

				break;
			case 100:

				break;
			}
		};
	};

	/*
	 * 添加收藏
	 */
	protected void addFavorite(final Integer favType) {

		if (!isLogin) {
			ToastUtils.showShortToast(this, "请先登录！");
			Intent intent = new Intent(new Intent(
					TzmTimeActivityProductDetailActivity.this,
					TzmLoginRegisterActivity.class));
			intent.putExtra("type", 0);
			startActivityForResult(intent, AppConfig.REQUESTCODE_LOGIN);
			return;
		}
		client = new ApiClient(this);
		addfavoritesApi = new AddfavoritesApi();
		addfavoritesApi.setUid(userModel.uid);
		addfavoritesApi.setFavSenId(id);
		addfavoritesApi.setFavType(favType);
		client.api(addfavoritesApi, new ApiListener() {

			@Override
			public void onStart() {
				ProgressDialogUtil.openProgressDialog(
						TzmTimeActivityProductDetailActivity.this, "", "");
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
				ToastUtils.showShortToast(
						TzmTimeActivityProductDetailActivity.this, error);
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
								TzmTimeActivityProductDetailActivity.this,
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

	/*
	 * 删除收藏
	 */
	protected void delFavorite() {
		delfavoriteApi = new DelfavoriteApi();
		delfavoriteApi.setUid(userModel.uid);
		delfavoriteApi.setFavType(1);
		delfavoriteApi.setCids(id + "");
		client.api(delfavoriteApi, new ApiListener() {

			@Override
			public void onStart() {
				ProgressDialogUtil.openProgressDialog(
						TzmTimeActivityProductDetailActivity.this, "", "");
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
				ToastUtils.showShortToast(
						TzmTimeActivityProductDetailActivity.this, error);
			}

			@Override
			public void onComplete(String jsonStr) {
				ProgressDialogUtil.closeProgressDialog();
				if (StringUtils.isNotBlank(jsonStr)) {
					try {
						JSONObject jsonObject = new JSONObject(jsonStr);
						boolean success = jsonObject.optBoolean("success");
						//int result = jsonObject.optInt("result");
						String error_msg = jsonObject.optString("error_msg");
						ToastUtils.showShortToast(
								TzmTimeActivityProductDetailActivity.this,
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

	/*
	 * 立即购买
	 */

	/**
	 * 分享
	 */
	private final UMSocialService mController = UMServiceFactory
			.getUMSocialService("com.umeng.share");

	protected void share() {
		// 配置SSO
		mController.getConfig().setSsoHandler(new SinaSsoHandler());
		addWXPlatform();

		UMImage urlImage = new UMImage(
				TzmTimeActivityProductDetailActivity.this,
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
		mController.openShare(TzmTimeActivityProductDetailActivity.this, false);
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
		UMWXHandler wxHandler = new UMWXHandler(
				TzmTimeActivityProductDetailActivity.this, appId, appSecret);
		wxHandler.addToSocialSDK();

		// 支持微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(
				TzmTimeActivityProductDetailActivity.this, appId, appSecret);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();

	}

	OnClickListener clickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.iv_share:
				share();
				break;
			case R.id.iv_gotop:
				sticky_scrollview.fullScroll(ScrollView.FOCUS_UP);
				hideView();
				break;
			case R.id.rl_cart:
				startActivity(new Intent(
						TzmTimeActivityProductDetailActivity.this,
						ShoppingCartActivity.class));
				break;
			case R.id.btn_head_left:
				onBackPressed();
				break;
			case R.id.rl_like01:
				Intent intent = new Intent(
						TzmTimeActivityProductDetailActivity.this,
						TzmTimeActivityProductDetailActivity.class);
				intent.putExtra("id",
						productDetailModel.attribute.get(0).productId);
				startActivity(intent);
				break;
			case R.id.rl_like02:
				Intent intent2 = new Intent(
						TzmTimeActivityProductDetailActivity.this,
						TzmTimeActivityProductDetailActivity.class);
				intent2.putExtra("id",
						productDetailModel.attribute.get(1).productId);
				startActivity(intent2);
				break;
			case R.id.rl_like03:
				Intent intent3 = new Intent(
						TzmTimeActivityProductDetailActivity.this,
						TzmTimeActivityProductDetailActivity.class);
				intent3.putExtra("id",
						productDetailModel.attribute.get(2).productId);
				startActivity(intent3);
				break;
			case R.id.rl_like04:
				Intent intent4 = new Intent(
						TzmTimeActivityProductDetailActivity.this,
						TzmTimeActivityProductDetailActivity.class);
				intent4.putExtra("id",
						productDetailModel.attribute.get(3).productId);
				startActivity(intent4);
				break;
			case R.id.iv_collcet:
				addFavorite(1);
				break;
			case R.id.iv_cancel_collcet:
				delFavorite();
				break;
			case R.id.btn_buy:
				if (!isLogin) {
					// ToastUtils.showShortToast(this, "请先登录！");
					// Intent intent = new Intent(new Intent(
					// TzmProductDetailActivity2.this, TzmLoginActivity.class));
					// startActivityForResult(intent, AppConfig.Login);
					Intent lintent = new Intent(new Intent(
							TzmTimeActivityProductDetailActivity.this,
							TzmLoginRegisterActivity.class));
					lintent.putExtra("type", 0);
					startActivityForResult(lintent, AppConfig.Login);

					break;
				}
				Intent buyintent = new Intent(
						TzmTimeActivityProductDetailActivity.this,
						TzmSelctAddressActivity.class);
				buyintent.putExtra("TAG", "addPurchase");
				buyintent.putExtra("pid", id);
				startActivity(buyintent);
				finish();
				break;
			case R.id.rl_shop_detail:
				Intent shopDetail_intent = new Intent(
						TzmTimeActivityProductDetailActivity.this,
						TzmShopDetailActivity.class);
				shopDetail_intent.putExtra("id", productDetailModel.sid);
				shopDetail_intent.putExtra("shopName",
						productDetailModel.shopName);
				startActivity(shopDetail_intent);
				break;
			case R.id.viewpager_t1:
				viewPager2.setCurrentItem(0);
				break;
			case R.id.viewpager_t2:
				viewPager2.setCurrentItem(1);
				break;
			}
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == AppConfig.REQUESTCODE_LOGIN) {

				checkLogin();// 判断登陆
				searhFavorite();// 判断收藏

				if (isLogin && userModel.type == 6) {
					ll_bottom.setVisibility(View.GONE);
					view_bottom.setVisibility(View.GONE);
				} else {
					ll_bottom.setVisibility(View.VISIBLE);
					view_bottom.setVisibility(View.VISIBLE);
				}
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 设置stickyView下面的布局的高度
		LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) viewPager2
				.getLayoutParams();
		if (ll_bottom.getVisibility() == View.GONE) {
			linearParams.height = height - dip2px(this, 85);
		} else {
			linearParams.height = height - dip2px(this, 135);
		}
		viewPager2.setLayoutParams(linearParams);
		scrollview.setLayoutParams(linearParams);
		loadNum();// 获取购物车数量
	}

}
