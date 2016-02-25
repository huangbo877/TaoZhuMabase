package com.ruiyu.taozhuma.activity;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TabHost.TabSpec;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.api.ApiClient;
import com.ruiyu.taozhuma.api.ApiListener;
import com.ruiyu.taozhuma.api.SplashScreenApi;
import com.ruiyu.taozhuma.fragment.NewHomeFragment;
import com.ruiyu.taozhuma.fragment.NewMyFragment;
import com.ruiyu.taozhuma.fragment.ScenesFragment;
import com.ruiyu.taozhuma.fragment.ShoppingCartFragment;
import com.ruiyu.taozhuma.model.BaseModel;
import com.ruiyu.taozhuma.model.NavigationlistModel;
import com.ruiyu.taozhuma.model.SplashScreenModel;
import com.ruiyu.taozhuma.utils.LogUtil;
import com.ruiyu.taozhuma.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.umeng.update.UmengUpdateAgent;

/**
 * Tab框架
 * 
 * @author toby
 * 
 */
public class MainTabActivity extends FragmentActivity {
	private static FragmentTabHost mTabHost;
	public static Handler mHandler;// 这里将Handler声明为静态
	private RadioGroup mTabRg;

	public static ArrayList<NavigationlistModel> model;
	private ApiClient apiClient4;
	private SplashScreenApi splashScreenApi;
	// 定义数组来存放Fragment界面
	@SuppressWarnings("rawtypes")
	private Class fragmentArray[] = { NewHomeFragment.class,
			ScenesFragment.class, ShoppingCartFragment.class,
			NewMyFragment.class };

	// Tab选项卡的标示tag
	private int mTabTagArray[] = { 0, 1, 2, 3 };

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_tab_layout);
		LogUtil.Log("MainTabActivity");
		LogUtil.Log("before start work at "
				+ Calendar.getInstance().getTimeInMillis());
		// 开启推送服务
		PushAgent mPushAgent = PushAgent.getInstance(this);
		mPushAgent.enable();
		PushAgent.getInstance(this).onAppStart();
		// 获取设备的Device Token
		// String device_token = UmengRegistrar.getRegistrationId(this);
		// System.out.println(device_token);
		// 友盟更新

		model = new ArrayList<NavigationlistModel>();
		UmengUpdateAgent.setUpdateOnlyWifi(false);
		UmengUpdateAgent.update(this);
		initView();

		LogUtil.Log("after start work at "
				+ Calendar.getInstance().getTimeInMillis());

		apiClient4 = new ApiClient(MainTabActivity.this);
		splashScreenApi = new SplashScreenApi();
	}

	/**
	 * 初始化组件
	 */
	private void initView() {

		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

		int count = fragmentArray.length;

		for (int i = 0; i < count; i++) {
			// if(MwApplication.getMainTabHost().findViewWithTag(mTabTagArray[i])==null){
			// 为每一个Tab按钮设置图标、文字和内容
			TabSpec tabSpec = mTabHost.newTabSpec(mTabTagArray[i] + "")
					.setIndicator(i + "");
			// 将Tab按钮添加进Tab选项卡中
			mTabHost.addTab(tabSpec, fragmentArray[i], null);

		}

		mTabRg = (RadioGroup) findViewById(R.id.tab_rg_menu);
		mTabRg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// MainTabActivity.hideHeadViews();
				switch (checkedId) {
				case R.id.tab_rb_1:
					// viewPager.setCurrentItem(0, false);
					mTabHost.setCurrentTab(0);
					break;
				case R.id.tab_rb_2:
					// viewPager.setCurrentItem(1, false);
					mTabHost.setCurrentTab(1);
					break;
				case R.id.tab_rb_3:
					// viewPager.setCurrentItem(2, false);
					mTabHost.setCurrentTab(2);
					break;
				case R.id.tab_rb_4:
					// viewPager.setCurrentItem(3, false);
					mTabHost.setCurrentTab(3);
					break;
				}
			}
		});
		mTabHost.setCurrentTab(0);
		dingshi();
	}

	/**
	 * 搞个定时器设置10秒后才出现闪屏
	 */
	public void dingshi() {
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				show_shanping();
			}
		};
		timer.schedule(task, 3000);
	}


	public void show_shanping() {
		
		splashScreenApi.getUrl();
		apiClient4.api(splashScreenApi, new ApiListener() {

			@Override
			public void onStart() {

			}

			@Override
			public void onException(Exception e) {

				
			}

			@Override
			public void onError(String error) {

				
			}

			@Override
			public void onComplete(String jsonStr) {
				try {
					Gson gson4 = new Gson();
					Type type = new TypeToken<BaseModel<List<SplashScreenModel>>>() {
					}.getType();
					BaseModel<List<SplashScreenModel>> base = gson4.fromJson(
							jsonStr, type);

					if (base.result != null && base.result.size() > 0) {

						Intent intent_collect = new Intent(
								MainTabActivity.this, ShanPingActivity.class);
						intent_collect.putExtra("SplashScreenModels",
								(Serializable) base.result);
						startActivity(intent_collect);
					}
				} catch (Exception i) {
					i.printStackTrace();
				}
				// {"result":[],"success":false,"error_msg":""}
			}
		}, true);
	}

	@SuppressLint("InflateParams")
	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id) {
		LayoutInflater inflaterDl = LayoutInflater.from(this);
		RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(
				R.layout.main_activity_dialog, null);

		// 对话框
		final Dialog dialog = new AlertDialog.Builder(MainTabActivity.this)
				.create();
		dialog.show();
		dialog.getWindow().setContentView(layout);

		// 取消按钮
		ImageView close = (ImageView) layout.findViewById(R.id.iv_close);
		close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.cancel();
			}
		});
		//
		ImageView pic = (ImageView) layout.findViewById(R.id.iv_pic);
		pic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainTabActivity.this,
						MemberExclusiveActivity.class));
				dialog.cancel();
			}
		});

		return dialog;
	}

	/**
	 * 获取用户基本信息
	 */


	// 导航数据

	private long keyBackClick = 0;

	@SuppressWarnings("static-access")
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == event.KEYCODE_BACK) {
			long tmp = new Date().getTime();
			if (keyBackClick > 0 && tmp - keyBackClick < 2000) {
				this.finish();
			} else {
				keyBackClick = tmp;
				ToastUtils.showShortToast(this, "别按了，再按我就会离开你了");
				return false;
			}
		}
		return true;

	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		mTabRg.check(R.id.tab_rb_1);
	}

}
