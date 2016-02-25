package com.ruiyu.taozhuma.fragment;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.CBViewHolderCreator;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.OnItemClickListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.activity.BigCardsActivity;
import com.ruiyu.taozhuma.activity.InfoDetailActivity;
import com.ruiyu.taozhuma.activity.TimeActivityListActivity;
import com.ruiyu.taozhuma.activity.TzmActivity;
import com.ruiyu.taozhuma.activity.TzmCollectActivity2;
import com.ruiyu.taozhuma.activity.TzmLoginRegisterActivity;
import com.ruiyu.taozhuma.activity.TenNewActivity;
import com.ruiyu.taozhuma.activity.TzmProductListActivity;
import com.ruiyu.taozhuma.activity.TzmShopDetailActivity;
import com.ruiyu.taozhuma.activity.TzmWalletActivity;
import com.ruiyu.taozhuma.adapter.ScenesListAdapter;
import com.ruiyu.taozhuma.api.ActivityTimeListApi;
import com.ruiyu.taozhuma.api.ApiClient;
import com.ruiyu.taozhuma.api.ApiListener;
import com.ruiyu.taozhuma.api.HomePageIconApi;
import com.ruiyu.taozhuma.api.HomePageTop4ListApi;
import com.ruiyu.taozhuma.api.NavigationListApi;
import com.ruiyu.taozhuma.api.ScenesListApi;
import com.ruiyu.taozhuma.api.TzmFocusApi;
import com.ruiyu.taozhuma.base.xUtilsImageLoader;
import com.ruiyu.taozhuma.config.AppConfig;
import com.ruiyu.taozhuma.dialog.PiFaShangDialog;
import com.ruiyu.taozhuma.even.ScrollEven;
import com.ruiyu.taozhuma.model.ActivityTimeListModel;
import com.ruiyu.taozhuma.model.BaseModel;
import com.ruiyu.taozhuma.model.HomePageTop4ListModel;
import com.ruiyu.taozhuma.model.NavigationlistModel;
import com.ruiyu.taozhuma.model.ScenesListModel;
import com.ruiyu.taozhuma.model.TzmFocusModel;
import com.ruiyu.taozhuma.model.TzmHomeIconListModel;
import com.ruiyu.taozhuma.utils.LogUtil;
import com.ruiyu.taozhuma.utils.ProgressDialogUtil;
import com.ruiyu.taozhuma.utils.StringUtils;
import com.ruiyu.taozhuma.utils.ToastUtils;
import com.ruiyu.taozhuma.utils.UserInfoUtils;
import com.ruiyu.taozhuma.view.NetworkImageHolderView;
import com.ruiyu.taozhuma.widget.ListViewForScrollView;
import com.ruiyu.taozhuma.widget.RushBuyCountDownTimerView;
import com.ruiyu.taozhuma.widget.RushBuyCountDownTimerView.TimeOverListener;
import com.tzm.pulltorefresh.library.PullToRefreshBase;
import com.tzm.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.tzm.pulltorefresh.library.PullToRefreshScrollView;

import de.greenrobot.event.EventBus;

/**
 * 首页产品//备份：功能：场景，套餐，
 */
public class NewProductListFragment extends Fragment implements OnTouchListener {

	private String TAG = "NewProductListFragment";

	private LinearLayout ll_collect, ll_shop_list, ll_new_product;

	private TzmFocusApi tzmFocusApi;
	private HomePageTop4ListApi homePageTop4ListApi;
	private ArrayList<TzmFocusModel> tzmFocusModels;
	private ArrayList<HomePageTop4ListModel> top4ListModels;
	private ApiClient apiClient, apiClient2, apiClient3;
	NavigationListApi NavigationListApi;
	ApiClient client6;
	public static ArrayList<NavigationlistModel> model;
	xUtilsImageLoader imageLoader, imageLoaderLong;
	HomePageIconApi HomePageIconApi;
	@ViewInject(R.id.im_id806)
	private ImageView im_id806;
	@ViewInject(R.id.im_id807)
	private ImageView im_id807;
	@ViewInject(R.id.im_id808)
	private ImageView im_id808;
	@ViewInject(R.id.im_id423)
	private ImageView im_id423;
	@ViewInject(R.id.ll_ten)
	private LinearLayout ll_ten;
	@ViewInject(R.id.ll_top)
	private LinearLayout ll_top;

	// 列表
	@ViewInject(R.id.ptrf_sv)
	private PullToRefreshScrollView pullToRefreshScrollView;
	private ScrollView scrollView;
	@ViewInject(R.id.iv_gotop)
	private ImageView iv_gotop;

	private Boolean isLogin;
	// 限时秒杀
	@ViewInject(R.id.timeview)
	private RushBuyCountDownTimerView timerView;
	@ViewInject(R.id.tv_tip)
	private TextView tv_tip;// 秒杀
	@ViewInject(R.id.tv_title)
	private TextView tv_title;// 秒杀标题

	private ActivityTimeListApi activityTimeListApi;
	private ApiClient client;

	@ViewInject(R.id.iv_ten)
	// 每日十件
	private ImageView iv_ten;
	@ViewInject(R.id.tv_ten)
	private TextView tv_ten;

	@ViewInject(R.id.iv_top)
	// TOP排行
	private ImageView iv_top;
	@ViewInject(R.id.tv_top)
	private TextView tv_top;

	@ViewInject(R.id.iv_collect)
	// 关注
	private ImageView iv_collect;
	@ViewInject(R.id.tv_collect)
	private TextView tv_collect;

	@ViewInject(R.id.iv_new_product)
	// 新品快订
	private ImageView iv_new_product;
	@ViewInject(R.id.tv_new_product)
	private TextView tv_new_product;

	@SuppressWarnings("rawtypes")
	@ViewInject(R.id.convenientBanner)
	private ConvenientBanner convenientBanner;// 顶部广告栏控件

	// 现在改为专场列表 (原来为首页场景套餐列表)
	@ViewInject(R.id.elistView)
	private ListViewForScrollView expandableListView;
	private ScenesListApi scenesListApi;
	private List<ScenesListModel> scenesListModels;
	private ScenesListAdapter scenesListAdapter;
	private int currentPage = 1;// 当前页数
	private boolean isLoadMore = false;// 是否是加载更多

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.new_product_list_fragment, null);
		ViewUtils.inject(this, view);
		EventBus.getDefault().register(this);
		LogUtil.Log(TAG, TAG + "--------111-------onCreateView");
		initView(view);
		tzmFocusApi = new TzmFocusApi();
		scenesListApi = new ScenesListApi();
		homePageTop4ListApi = new HomePageTop4ListApi();
		tzmFocusModels = new ArrayList<TzmFocusModel>();
		top4ListModels = new ArrayList<HomePageTop4ListModel>();
		apiClient = new ApiClient(getActivity());
		apiClient2 = new ApiClient(getActivity());
		apiClient3 = new ApiClient(getActivity());

		imageLoader = new xUtilsImageLoader(getActivity());
		imageLoaderLong = new xUtilsImageLoader(getActivity());
		imageLoaderLong.configDefaultLoadFailedImage(R.drawable.loading_long);
		imageLoaderLong.configDefaultLoadingImage(R.drawable.loading_long);

		pullToRefreshScrollView
				.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

					@Override
					public void onRefresh(
							PullToRefreshBase<ScrollView> refreshView) {
						currentPage = 1;
						isLoadMore = false;
						loadData();
						syzclb(currentPage);
						timerView.stop();
						zcActivity();
						loadTimeActivity();
						loadicon();
						NewHomeFragment.stopThread = false;
					}
				});
		scrollView = pullToRefreshScrollView.getRefreshableView();
		scrollView.setOnTouchListener(new OnTouchListener() {

			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP
						|| event.getAction() == MotionEvent.ACTION_SCROLL) {
					if (scrollView.getScrollY() > 1500) {
						showView();
					} else {
						hideView();
					}
					int scrollY = v.getScrollY();
					int height = v.getHeight();
					int scrollViewMeasuredHeight = scrollView.getChildAt(0)
							.getMeasuredHeight();
					if ((scrollY + height) == scrollViewMeasuredHeight) {
						currentPage++;
						syzclb(currentPage);
						isLoadMore = true;

					}
				} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
					// int scrollY=v.getScrollY();
					// int height=v.getHeight();
					// int
					// scrollViewMeasuredHeight=scrollView.getChildAt(0).getMeasuredHeight();
					// if(scrollY==0){
					// System.out.println("滑动到了顶端 view.getScrollY()="+scrollY);
					// }
					// if((scrollY+height)==scrollViewMeasuredHeight){
					// currentPage++;
					// syzclb(currentPage);
					// isLoadMore = true;
					// System.out.println("滑动到了底部 scrollY="+scrollY);
					// System.out.println("滑动到了底部 height="+height);
					// System.out.println("滑动到了底部 scrollViewMeasuredHeight="+scrollViewMeasuredHeight);
					// }
				}
				return false;
			}
		});
		iv_gotop.setOnClickListener(clickListener);
		model = new ArrayList<NavigationlistModel>();
		zcActivity();
		loadData();
		syzclb(currentPage);
		loadTimeActivity();
		loadicon();

		return view;
	}

	/**
	 * 首页图标列表
	 */
	private void loadicon() {
		// TODO Auto-generated method stub
		HomePageIconApi = new HomePageIconApi();
		client = new ApiClient(getActivity());
		client.api(HomePageIconApi, new ApiListener() {

			@Override
			public void onStart() {
			}

			@Override
			public void onException(Exception e) {
				LogUtil.ErrorLog(e);
			}

			@Override
			public void onError(String error) {
				// ToastUtils.showToast(getActivity(), error);
				LogUtil.Log(error);
			}

			@Override
			public void onComplete(String jsonStr) {
				Gson gson = new Gson();
				Type type = new TypeToken<BaseModel<ArrayList<TzmHomeIconListModel>>>() {
				}.getType();
				BaseModel<ArrayList<TzmHomeIconListModel>> base = gson
						.fromJson(jsonStr, type);
				if (base.result != null && base.result.size() > 0) {
					try {
						tv_ten.setText(base.result.get(0).title);
						imageLoader.display(iv_ten, base.result.get(0).image);
						tv_top.setText(base.result.get(1).title);
						imageLoader.display(iv_top, base.result.get(1).image);
						tv_collect.setText(base.result.get(2).title);
						imageLoader.display(iv_collect,
								base.result.get(2).image);
						tv_new_product.setText(base.result.get(3).title);
						imageLoader.display(iv_new_product,
								base.result.get(3).image);
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}

		}, true);

	}

	/**
	 * 加载秒杀活动数据
	 */
	private void loadTimeActivity() {
		activityTimeListApi = new ActivityTimeListApi();
		client = new ApiClient(getActivity());
		im_id807.setClickable(false);
		client.api(activityTimeListApi, new ApiListener() {

			@Override
			public void onStart() {
			}

			@Override
			public void onException(Exception e) {
				LogUtil.ErrorLog(e);
			}

			@Override
			public void onError(String error) {
				// ToastUtils.showToast(getActivity(), error);
				LogUtil.Log(error);
			}

			@Override
			public void onComplete(String jsonStr) {
				Gson gson = new Gson();
				Type type = new TypeToken<BaseModel<ArrayList<ActivityTimeListModel>>>() {
				}.getType();
				BaseModel<ArrayList<ActivityTimeListModel>> base = gson
						.fromJson(jsonStr, type);
				if (base.result != null && base.result.size() > 0) {
					timerView.setVisibility(View.VISIBLE);
					ActivityTimeListModel model = base.result.get(0);
					try {
						startTimeView(model);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					tv_title.setText("活动尚未开始");
					timerView.stop();
					timerView.setVisibility(View.GONE);
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
	@SuppressWarnings("deprecation")
	@SuppressLint("SimpleDateFormat")
	protected void startTimeView(ActivityTimeListModel model)
			throws ParseException {
		// 活动时间格式
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-ddHH:mm");
		// 标题时间格式
		SimpleDateFormat titleformat = new SimpleDateFormat("HH:mm");
		// 当前时间
		Date nowdate = new Date();
		// 开始时间//结束时间
		Date time = null;

		// 时间差距
		long temp = 0;// 相差毫秒
		if (model.status == 1) {
			im_id807.setClickable(true);
			// 进行中
			tv_title.setText(titleformat.parse(model.title).getHours() + "点场");
			time = dateFormat.parse(model.activityDate + model.endTime);
			temp = time.getTime()
					- dateFormat.parse(model.activityDate + model.currentTime)
							.getTime();

		} else if (model.status == 0) {
			// 尚未开始
			tv_title.setText("距开始");
			time = dateFormat.parse(model.activityDate + model.beginTime);
			temp = time.getTime()
					- dateFormat.parse(model.activityDate + model.currentTime)
							.getTime();

		}
		long hours = temp / 1000 / 3600; // 相差小时数
		long temp2 = temp % (1000 * 3600);
		long mins = temp2 / 1000 / 60; // 相差分钟数
		long sec = temp / 1000 % 60;// 相差秒
		try {
			timerView.setTime(hours, mins, sec);
		} catch (Exception e) {
			// TODO: handle exception
		}
		timerView.setOnTimeOverListener(new TimeOverListener() {

			@Override
			public void isTimeOver(boolean over) {
				loadTimeActivity();

			}
		});
		timerView.start();
		// LogUtil.Log(dateFormat.format(date));

	}

	private void showView() {
		if (iv_gotop.getVisibility() == View.GONE) {
			iv_gotop.setVisibility(View.VISIBLE);
			viewhandler.sendEmptyMessageDelayed(0, 5000);
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

	public void onEventMainThread(ScrollEven event) {
		scrollView.fullScroll(ScrollView.FOCUS_UP);
		// mImageSwitcher.requestFocus();
	}

	private void loadData() {
		// 加载轮播图片
		apiClient.api(tzmFocusApi, new ApiListener() {
			@Override
			public void onStart() {

				ProgressDialogUtil.openProgressDialog(getActivity(), "",
						"正在加载...");
			}

			@Override
			public void onException(Exception e) {
				ProgressDialogUtil.closeProgressDialog();
				LogUtil.ErrorLog(e);
				// ToastUtils.showToast(getActivity(), "网络异常");
			}

			@Override
			public void onError(String error) {
				ProgressDialogUtil.closeProgressDialog();
				ToastUtils.showToast(getActivity(), error);
				LogUtil.Log(error);
			}

			@SuppressWarnings("unchecked")
			@Override
			public void onComplete(String jsonStr) {
				ProgressDialogUtil.closeProgressDialog();
				Gson gson = new Gson();
				Type type = new TypeToken<BaseModel<ArrayList<TzmFocusModel>>>() {
				}.getType();
				BaseModel<ArrayList<TzmFocusModel>> base = gson.fromJson(
						jsonStr, type);
				if (base.result != null && base.result.size() > 0) {
					LogUtil.Log("base.success True");
					tzmFocusModels = base.result;
					convenientBanner
							.setPages(
									new CBViewHolderCreator<NetworkImageHolderView>() {
										@Override
										public NetworkImageHolderView createHolder() {
											return new NetworkImageHolderView();
										}
									}, tzmFocusModels)
							.setPageTransformer(
									ConvenientBanner.Transformer.ZoomOutTranformer)
							.setPageIndicatorAlign(
									ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
							.setPageIndicator(
									new int[] { R.drawable.dian2,
											R.drawable.dianshi });
					convenientBanner
							.setOnItemClickListener(new OnItemClickListener() {

								@Override
								public void onItemClick(int position) {

									if ((tzmFocusModels.get(position).subType) == 1) {

									} else if ((tzmFocusModels.get(position).subType) == 2) {
										Intent intent2 = new Intent(
												getActivity(),
												TzmShopDetailActivity.class);
										intent2.putExtra("activityId",
												tzmFocusModels.get(position).id
														.toString());
										intent2.putExtra(
												"name",
												tzmFocusModels.get(position).title);
										getActivity().startActivity(intent2);
									} else if (tzmFocusModels.get(position).subType == 3) {

									} else if ((tzmFocusModels.get(position).subType) == 4) {
										// 咨询内容
										Intent intent4 = new Intent(
												getActivity(),
												InfoDetailActivity.class);
										intent4.putExtra("id",
												tzmFocusModels.get(position).id);
										getActivity().startActivity(intent4);
									}

								}
							});
					convenientBanner.startTurning(24000);

				} else {
					ToastUtils.showShortToast(getActivity(), base.error_msg);
				}
				pullToRefreshScrollView.onRefreshComplete();
			}
		}, true);

		// 首页顶部活动推送
		apiClient3.api(homePageTop4ListApi, new ApiListener() {

			@Override
			public void onStart() {
			}

			@Override
			public void onException(Exception e) {
				LogUtil.ErrorLog(e);
				// ToastUtils.showToast(getActivity(), "网络异常");
			}

			@Override
			public void onError(String error) {
				ToastUtils.showToast(getActivity(), error);
				LogUtil.Log(error);
			}

			@Override
			public void onComplete(String jsonStr) {
				Gson gson = new Gson();

				Type type = new TypeToken<BaseModel<ArrayList<HomePageTop4ListModel>>>() {
				}.getType();
				BaseModel<ArrayList<HomePageTop4ListModel>> base = gson
						.fromJson(jsonStr, type);
				if (base.result != null) {
					top4ListModels = base.result;

					if (top4ListModels != null) {
						for (int i = 0; i < top4ListModels.size(); i++) {
							switch (top4ListModels.get(i).sorting) {
							case 1:
								if (StringUtils.isNotEmpty(top4ListModels
										.get(i).image)) {
									imageLoader.display(im_id807,
											top4ListModels.get(i).image);
									im_id807.setTag(top4ListModels.get(i));
								}
								break;
							case 2:
								if (StringUtils.isNotEmpty(top4ListModels
										.get(i).image)) {
									imageLoader.display(im_id808,
											top4ListModels.get(i).image);
									im_id808.setTag(top4ListModels.get(i));
								}
								break;
							case 3:
								if (StringUtils.isNotEmpty(top4ListModels
										.get(i).image)) {
									imageLoader.display(im_id806,
											top4ListModels.get(i).image);
									im_id806.setTag(top4ListModels.get(i));
								}
								break;
							case 4:
								if (StringUtils.isNotEmpty(top4ListModels
										.get(i).image)) {
									imageLoader.display(im_id423,
											top4ListModels.get(i).image);
									im_id423.setTag(top4ListModels.get(i));
								}
								break;

							}

						}
					}
				} else {
					ToastUtils.showShortToast(getActivity(), base.error_msg);
				}
				pullToRefreshScrollView.onRefreshComplete();

			}
		}, true);

	}

	/**
	 * 
	 */
	private void syzclb(int page) {
		// TODO Auto-generated method stub
		/**
		 * 首页场景套餐列表
		 */
		scenesListApi.setPageNo(page);
		apiClient2.api(scenesListApi, new ApiListener() {

			@Override
			public void onStart() {
			}

			@Override
			public void onException(Exception e) {
				pullToRefreshScrollView.onRefreshComplete();
				LogUtil.ErrorLog(e);
				ToastUtils.showShortToast(getActivity(), "网络异常");
			}

			@Override
			public void onError(String error) {
				pullToRefreshScrollView.onRefreshComplete();
				ToastUtils.showShortToast(getActivity(), error);
				LogUtil.Log(error);
			}

			@Override
			public void onComplete(String jsonStr) {
				Gson gson = new Gson();

				Type type = new TypeToken<BaseModel<List<ScenesListModel>>>() {
				}.getType();
				BaseModel<List<ScenesListModel>> base = gson.fromJson(jsonStr,
						type);
				// if (base.result != null && base.result.size() > 0) {
				// scenesListModels = base.result;
				// scenesListAdapter = new ScenesListAdapter(getActivity(),
				// scenesListModels);
				// expandableListView.setFocusable(false);//
				// 去掉listview的焦点，否则无法设置scrollview的起始位置
				// expandableListView.setAdapter(scenesListAdapter);
				//
				// } else {
				// ToastUtils.showShortToast(getActivity(), base.error_msg);
				// }
				// pullToRefreshScrollView.onRefreshComplete();
				if (isLoadMore) {
					// 加载更多
					// listPosition = expandableListView.getCount();
					if (base.result != null && base.result.size() > 0) {
						scenesListModels.addAll(base.result);
						// tzmShopDetailAdapter.setList(tzmShopDetailModel);
						scenesListAdapter.notifyDataSetChanged();
					} else {
						ToastUtils.showShortToast(getActivity(),
								R.string.msg_list_null);
					}

				} else {
					// 刷新
					// listPosition = 0;
					// tzmShopDetailModel.clear();
					if (base.result != null && base.result.size() > 0) {
						scenesListModels = base.result;
					} else {
						// 返回数据为空
						ToastUtils.showShortToast(getActivity(), "专场已结束！");
					}

				}
				scenesListAdapter = new ScenesListAdapter(getActivity(),
						scenesListModels);
				expandableListView.setFocusable(false);// 去掉listview的焦点，否则无法设置scrollview的起始位置
				expandableListView.setAdapter(scenesListAdapter);
				pullToRefreshScrollView.onRefreshComplete();
			}
		}, true);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			checkLogin();
		}
	}

	@SuppressLint("ClickableViewAccessibility")
	public boolean onTouch(View v, MotionEvent event) {

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			v.getParent().requestDisallowInterceptTouchEvent(true);
			break;
		}
		case MotionEvent.ACTION_CANCEL:
			v.getParent().requestDisallowInterceptTouchEvent(false);
			break;
		}
		return true;

	}

	private void initView(View view) {
		// 关注
		ll_collect = (LinearLayout) view.findViewById(R.id.ll_collect);
		ll_collect.setOnClickListener(clickListener);
		// 品牌街
		ll_shop_list = (LinearLayout) view.findViewById(R.id.ll_shop_list);
		ll_shop_list.setOnClickListener(clickListener);
		// 新品快订
		ll_new_product = (LinearLayout) view.findViewById(R.id.ll_new_product);
		ll_new_product.setOnClickListener(clickListener);
		im_id806.setOnClickListener(clickListener);
		im_id807.setOnClickListener(clickListener);
		im_id808.setOnClickListener(clickListener);
		im_id423.setOnClickListener(clickListener);
		ll_ten.setOnClickListener(clickListener);
		ll_top.setOnClickListener(clickListener);
	}

	private void checkLogin() {
		isLogin = UserInfoUtils.isLogin();
	}

	View.OnClickListener clickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			// 点击回到顶端
			case R.id.iv_gotop:
				scrollView.fullScroll(ScrollView.FOCUS_UP);
				hideView();
				break;
			// 去除品牌街功能
			/*
			 * case R.id.ll_shop_list:// 品牌街 Intent intent_shop_list = new
			 * Intent(getActivity(), TzmShopListActivity.class);
			 * startActivity(intent_shop_list); break;
			 */
			case R.id.ll_collect:// 钱包专区
				// checkLogin();
				// if (!isLogin) {
				// Intent intent = new Intent(new Intent(getActivity(),
				// TzmLoginRegisterActivity.class));
				// intent.putExtra("type", 0);
				// startActivityForResult(intent, AppConfig.Login);
				// return;
				// } else if (UserInfoUtils.getUserInfo().type.equals(6)) {
				// show_pifashang_dialog();
				// } else {
				// Intent intent_collect = new Intent(getActivity(),
				// TzmCollectActivity2.class);
				// intent_collect.putExtra("status", 1);
				// startActivity(intent_collect);
				// }
				Intent intent_good_product = new Intent(getActivity(),
						TzmWalletActivity.class);

				startActivity(intent_good_product);

				break;
			case R.id.ll_new_product:// 大牌驾到
				LogUtil.Log(TAG, "进入新品快订");
				Intent intent = new Intent(getActivity(),
						BigCardsActivity.class);
				intent.putExtra("type", 4);
				startActivity(intent);
				break;
			case R.id.iv_hot_product:// 热销产品
				Intent intent_hot_product = new Intent(getActivity(),
						TzmActivity.class);
				intent_hot_product.putExtra("type", 2);
				startActivity(intent_hot_product);
				break;
			case R.id.im_recommend_product:// 竹马推荐
				Intent intent_recomment_product = new Intent(getActivity(),
						TzmActivity.class);
				intent_recomment_product.putExtra("type", 3);
				startActivity(intent_recomment_product);
				break;
			case R.id.eday_new_list:// 每日新品
				Intent intent_eday_new_list = new Intent(getActivity(),
						TzmActivity.class);
				intent_eday_new_list.putExtra("type", 1);
				startActivity(intent_eday_new_list);
				break;
			case R.id.im_good_product:// 家有好货
				// Intent intent_good_product = new Intent(getActivity(),
				// TzmActivity.class);
				// intent_good_product.putExtra("type", 5);
				// startActivity(intent_good_product);
				break;
			case R.id.im_id806://
				LogUtil.Log(TAG, "im_id806");
				if (v.getTag() != null) {
					Intent intent3 = new Intent(getActivity(),
							TzmProductListActivity.class);
					intent3.putExtra("typeId",
							((HomePageTop4ListModel) v.getTag()).typeId);
					intent3.putExtra("title",
							((HomePageTop4ListModel) v.getTag()).title);
					startActivity(intent3);
				}
				break;

			case R.id.im_id807:
				// 限时秒杀
				Intent intent13 = new Intent(getActivity(),
						TimeActivityListActivity.class);
				startActivity(intent13);
				break;
			case R.id.im_id808:// 开学季
				LogUtil.Log(TAG, "开学季专场");
				if (v.getTag() != null) {
					Intent intent14 = new Intent(getActivity(),
							TzmProductListActivity.class);
					intent14.putExtra("typeId",
							((HomePageTop4ListModel) v.getTag()).typeId);
					intent14.putExtra("title",
							((HomePageTop4ListModel) v.getTag()).title);
					startActivity(intent14);
				}
				break;
			case R.id.im_id423:// 益智积木专场
				LogUtil.Log(TAG, "益智积木专场");
				if (v.getTag() != null) {
					Intent intent4 = new Intent(getActivity(),
							TzmProductListActivity.class);
					intent4.putExtra("typeId",
							((HomePageTop4ListModel) v.getTag()).typeId);
					intent4.putExtra("title",
							((HomePageTop4ListModel) v.getTag()).title);

					startActivity(intent4);
				}
				break;
			case R.id.ll_ten:
				LogUtil.Log(TAG, "每日十件");
				Intent intentten = new Intent(getActivity(),
						TenNewActivity.class);
				startActivity(intentten);
				break;
			case R.id.ll_top: // top排行
				LogUtil.Log(TAG, "进入top排行");
				Intent intenttop = new Intent(getActivity(), TzmActivity.class);
				intenttop.putExtra("type", 7);
				startActivity(intenttop);
				break;

			}
		}
	};

	private void zcActivity() {
		NavigationListApi = new NavigationListApi();
		client = new ApiClient(getActivity());

		client.api(NavigationListApi, new ApiListener() {

			@Override
			public void onStart() {
			}

			@Override
			public void onException(Exception e) {
				LogUtil.ErrorLog(e);
			}

			@Override
			public void onError(String error) {
				// ToastUtils.showToast(getActivity(), error);
				LogUtil.Log(error);
			}

			@Override
			public void onComplete(String jsonStr) {
				Gson gson = new Gson();
				Type type = new TypeToken<BaseModel<ArrayList<NavigationlistModel>>>() {
				}.getType();
				BaseModel<ArrayList<NavigationlistModel>> base = gson.fromJson(
						jsonStr, type);
				if (base.result != null && base.result.size() > 0) {

					model = base.result;
					NewHomeFragment.model = model;

				}
			}

		}, true);
	}

	@Override
	public void onResume() {
		if (tzmFocusModels != null && tzmFocusModels.size() > 0) {
			convenientBanner.startTurning(24000);
		}

		super.onResume();
	}

	@Override
	public void onPause() {
		convenientBanner.stopTurning();
		super.onPause();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		EventBus.getDefault().unregister(this);
		timerView.stop();
	}

	// 当用户是批发商时点击 关注弹出对话框
	public void show_pifashang_dialog() {
		PiFaShangDialog myDialog = new PiFaShangDialog(this.getActivity());

		// myDialog.setDialogCallback(dialogcallback);
		myDialog.show();
	}
}
