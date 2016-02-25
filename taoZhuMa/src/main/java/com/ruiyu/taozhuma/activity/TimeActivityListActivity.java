package com.ruiyu.taozhuma.activity;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.adapter.TimeActivityProductAdapter;
import com.ruiyu.taozhuma.adapter.TimeActivityStartProductAdapter;
import com.ruiyu.taozhuma.api.ActivityGoodsApi;
import com.ruiyu.taozhuma.api.ActivityTimeListApi;
import com.ruiyu.taozhuma.api.ApiClient;
import com.ruiyu.taozhuma.api.ApiListener;
import com.ruiyu.taozhuma.base.xUtilsImageLoader;
import com.ruiyu.taozhuma.model.ActivityGoodsModel;
import com.ruiyu.taozhuma.model.ActivityTimeListModel;
import com.ruiyu.taozhuma.model.BaseModel;
import com.ruiyu.taozhuma.utils.LogUtil;
import com.ruiyu.taozhuma.utils.ProgressDialogUtil;
import com.ruiyu.taozhuma.utils.ToastUtils;
import com.ruiyu.taozhuma.widget.ListViewForScrollView;
import com.ruiyu.taozhuma.widget.RushBuyCountDownTimerView;
import com.ruiyu.taozhuma.widget.RushBuyCountDownTimerView.TimeOverListener;

import android.os.Bundle;
import android.os.Handler;
import android.annotation.SuppressLint;
import android.support.v4.app.FragmentActivity;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 限时秒杀
 * 
 * @author Fu
 * 
 */
public class TimeActivityListActivity extends FragmentActivity implements
		OnClickListener {
	@ViewInject(R.id.btn_head_left)
	private Button btn_head_left;
	@ViewInject(R.id.txt_head_title)
	private TextView txt_head_title;
	// 背景
	@ViewInject(R.id.rl_activity1)
	private RelativeLayout rl_activity1;
	@ViewInject(R.id.rl_activity2)
	private RelativeLayout rl_activity2;
	@ViewInject(R.id.rl_activity3)
	private RelativeLayout rl_activity3;
	@ViewInject(R.id.rl_activity4)
	private RelativeLayout rl_activity4;
	// 标题
	@ViewInject(R.id.tv_tittle1)
	private TextView tv_tittle1;
	@ViewInject(R.id.tv_tittle2)
	private TextView tv_tittle2;
	@ViewInject(R.id.tv_tittle3)
	private TextView tv_tittle3;
	@ViewInject(R.id.tv_tittle4)
	private TextView tv_tittle4;
	// 提示
	@ViewInject(R.id.tv_tips1)
	private TextView tv_tips1;
	@ViewInject(R.id.tv_tips2)
	private TextView tv_tips2;
	@ViewInject(R.id.tv_tips3)
	private TextView tv_tips3;
	@ViewInject(R.id.tv_tips4)
	private TextView tv_tips4;

	private ApiClient apiClient;
	private List<ActivityTimeListModel> list;
	private ActivityTimeListApi activityTimeListApi;

	RelativeLayout[] relativeLayouts = new RelativeLayout[4];
	TextView[] title = new TextView[4];
	TextView[] tips = new TextView[4];

	//
	@ViewInject(R.id.time)
	private RushBuyCountDownTimerView timerView;
	@ViewInject(R.id.tv_time_status)
	private TextView tv_time_status;
	@ViewInject(R.id.tv_tip)
	private TextView tv_tip;
	@ViewInject(R.id.iv_banner)
	private ImageView iv_banner;
	@ViewInject(R.id.progressBar)
	private ProgressBar progressBar;
	@ViewInject(R.id.lv_product)
	private ListViewForScrollView lv_product;
	//
	xUtilsImageLoader imageLoader;

	private ActivityGoodsApi activityGoodsApi;
	// private ApiClient apiClient;
	private TimeActivityStartProductAdapter activityStartProductAdapter;
	private TimeActivityProductAdapter productAdapter;

	private int tag = 1;// 标示符，防止复用浪费资源

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.time_activity_list_activity);
		ViewUtils.inject(this);
		imageLoader = new xUtilsImageLoader(this);
		imageLoader.configDefaultLoadFailedImage(R.drawable.loading_long);
		imageLoader.configDefaultLoadingImage(R.drawable.loading_long);
		initView();
		loadTimeActivity();
	}

	@Override
	protected void onDestroy() {
		timerView.stop();
		handler.removeCallbacksAndMessages(null);// 防止泄露
		super.onDestroy();
	}

	private void initView() {
		btn_head_left.setOnClickListener(this);
		txt_head_title.setText(getString(R.string.event_title));
		relativeLayouts[0] = rl_activity1;
		relativeLayouts[1] = rl_activity2;
		relativeLayouts[2] = rl_activity3;
		relativeLayouts[3] = rl_activity4;
		title[0] = tv_tittle1;
		title[1] = tv_tittle2;
		title[2] = tv_tittle3;
		title[3] = tv_tittle4;
		tips[0] = tv_tips1;
		tips[1] = tv_tips2;
		tips[2] = tv_tips3;
		tips[3] = tv_tips4;

	}

	/**
	 * 加载秒杀活动时间数据
	 */
	private void loadTimeActivity() {
		activityTimeListApi = new ActivityTimeListApi();
		apiClient = new ApiClient(this);
		apiClient.api(activityTimeListApi, new ApiListener() {

			@Override
			public void onStart() {
				ProgressDialogUtil.openProgressDialog(
						TimeActivityListActivity.this, "", "");
			}

			@Override
			public void onException(Exception e) {
				ProgressDialogUtil.closeProgressDialog();
				ToastUtils
						.showShortToast(TimeActivityListActivity.this, "网络异常");
				LogUtil.ErrorLog(e);
			}

			@Override
			public void onError(String error) {
				// ToastUtils.showToast(getActivity(), error);
				ProgressDialogUtil.closeProgressDialog();
				LogUtil.Log(error);
			}

			@Override
			public void onComplete(String jsonStr) {
				ProgressDialogUtil.closeProgressDialog();
				try {
					Gson gson = new Gson();
					Type type = new TypeToken<BaseModel<ArrayList<ActivityTimeListModel>>>() {
					}.getType();
					BaseModel<ArrayList<ActivityTimeListModel>> base = gson
							.fromJson(jsonStr, type);
					if (base.result != null && base.result.size() > 0) {
						list = base.result;
						switchClickable();
					} else {
						ToastUtils.showShortToast(
								TimeActivityListActivity.this, base.error_msg);
					}

				} catch (Exception e) {
					// TODO: handle exception
				}

			}
		}, true);
	}

	// 解锁点击事件
	public void switchClickable() {
		try {

			for (int i = 0; i < list.size(); i++) {
				relativeLayouts[i].setOnClickListener(this);
				tips[i].setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
				title[i].setVisibility(View.VISIBLE);
				title[i].setText(list.get(i).title);
				if (list.get(i).status == 1) {
					title[i].setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
					tips[i].setText("抢购中");
				} else if (list.get(i).status == 0) {
					title[i].setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
					tips[i].setText("即将开抢");
				}
			}
			// 初始加载第一个
			timerView.turnBlackStyle();
			initContentView(0);

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 活动内容view
	 * 
	 * @param i
	 * @throws Exception
	 */
	private void initContentView(int i) throws Exception {
		imageLoader.display(iv_banner, list.get(i).banner);
		if (list.get(i).status == 1) {
			tv_time_status.setText("抢购中 " + list.get(i).beginTime + "开抢");
			tv_tip.setText("距结束");
		} else if (list.get(i).status == 0) {
			tv_time_status.setText("即将开始 " + list.get(i).beginTime + "开抢");
			tv_tip.setText("距开始");
		}
		startTimeView(i);
	}

	/**
	 * 开始倒计时
	 * 
	 * @param model
	 * @throws ParseException
	 */
	@SuppressLint("SimpleDateFormat")
	protected void startTimeView(int i) throws ParseException {
		timerView.stop();
		// 活动时间格式
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-ddHH:mm");
		// 当前时间
		Date nowdate = new Date();
		// 开始时间//结束时间
		Date time = null;

		// 时间差距
		long temp = 0;// 相差毫秒
		if (list.get(i).status == 1) {
			// 进行中
			time = dateFormat.parse(list.get(i).activityDate
					+ list.get(i).endTime);
			temp = time.getTime() - dateFormat.parse(list.get(i).activityDate
					+ list.get(i).currentTime).getTime();

		} else if (list.get(i).status == 0) {
			// 尚未开始
			time = dateFormat.parse(list.get(i).activityDate
					+ list.get(i).beginTime);
			temp = time.getTime() - dateFormat.parse(list.get(i).activityDate
					+ list.get(i).currentTime).getTime();

		}
		long hours = temp / 1000 / 3600; // 相差小时数
		long temp2 = temp % (1000 * 3600);
		long mins = temp2 / 1000 / 60; // 相差分钟数
		long sec = temp / 1000 % 60;// 相差秒
		timerView.setTime(hours, mins, sec);
		timerView.setOnTimeOverListener(new TimeOverListener() {

			@Override
			public void isTimeOver(boolean over) {
				try {
					ToastUtils.showShortToast(TimeActivityListActivity.this,
							"活动结束");
					finish();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		timerView.start();
		// LogUtil.Log(dateFormat.format(date));
		loadListData(i);

	}

	/**
	 * 加载活动数据
	 * 
	 * @param i
	 */
	private void loadListData(final int i) {
		lv_product.setFocusable(false);
		activityGoodsApi = new ActivityGoodsApi();
		activityGoodsApi.setTimeId(list.get(i).timeId);
		lv_product.setVisibility(View.GONE);
		progressBar.setVisibility(View.VISIBLE);
		apiClient.api(activityGoodsApi, new ApiListener() {

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
				// ProgressDialogUtil.closeProgressDialog();
				LogUtil.Log(error);
			}

			@Override
			public void onComplete(String jsonStr) {
				Gson gson = new Gson();
				Type type = new TypeToken<BaseModel<ActivityGoodsModel>>() {
				}.getType();
				BaseModel<ActivityGoodsModel> base = gson.fromJson(jsonStr,
						type);
				if (base.result != null && base.result.products.size() > 0) {
					progressBar.setVisibility(View.GONE);
					lv_product.setVisibility(View.VISIBLE);
					if (list.get(i).status == 1) {
						activityStartProductAdapter = new TimeActivityStartProductAdapter(
								TimeActivityListActivity.this,
								base.result.products, list.get(i).timeId);
						lv_product.setAdapter(activityStartProductAdapter);
					} else if (list.get(i).status == 0) {
						productAdapter = new TimeActivityProductAdapter(
								TimeActivityListActivity.this,
								base.result.products);
						lv_product.setAdapter(productAdapter);
					}

					iv_banner.requestFocus();
				} else {
					ToastUtils.showShortToast(TimeActivityListActivity.this,
							base.error_msg);
				}
			}

		}, true);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_head_left:
			onBackPressed();
			break;
		case R.id.rl_activity1:
			if (tag != 1) {
				handler.sendEmptyMessage(1);
			}
			break;
		case R.id.rl_activity2:
			if (tag != 2) {
				handler.sendEmptyMessage(2);
			}
			break;
		case R.id.rl_activity3:
			if (tag != 3) {
				handler.sendEmptyMessage(3);
			}
			break;
		case R.id.rl_activity4:
			if (tag != 4) {
				handler.sendEmptyMessage(4);
			}
			break;

		default:
			break;
		}

	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@SuppressWarnings("deprecation")
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				rl_activity1.setBackgroundResource(R.color.base);
				rl_activity2.setBackgroundDrawable(null);
				rl_activity3.setBackgroundDrawable(null);
				rl_activity4.setBackgroundDrawable(null);
				try {
					initContentView(0);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				tag = 1;
				break;
			case 2:
				rl_activity1.setBackgroundDrawable(null);
				rl_activity2.setBackgroundResource(R.color.base);
				rl_activity3.setBackgroundDrawable(null);
				rl_activity4.setBackgroundDrawable(null);
				try {
					initContentView(1);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				tag = 2;
				break;
			case 3:
				rl_activity1.setBackgroundDrawable(null);
				rl_activity2.setBackgroundDrawable(null);
				rl_activity3.setBackgroundResource(R.color.base);
				rl_activity4.setBackgroundDrawable(null);
				try {
					initContentView(2);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				tag = 3;
				break;
			case 4:
				rl_activity1.setBackgroundDrawable(null);
				rl_activity2.setBackgroundDrawable(null);
				rl_activity3.setBackgroundDrawable(null);
				rl_activity4.setBackgroundResource(R.color.base);
				try {
					initContentView(3);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				tag = 4;
				break;

			default:
				break;
			}
		};
	};

}
