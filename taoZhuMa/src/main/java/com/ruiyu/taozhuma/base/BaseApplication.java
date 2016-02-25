package com.ruiyu.taozhuma.base;


import com.lidroid.xutils.DbUtils;
import com.ruiyu.taozhuma.activity.MainTabActivity;
import com.ruiyu.taozhuma.activity.TzmPushOrderListActivity;
import com.ruiyu.taozhuma.model.UserModel;
import com.ruiyu.taozhuma.utils.SharedPreferencesUtils;
import com.ruiyu.taozhuma.utils.UserInfoUtils;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengNotificationClickHandler;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;

import com.umeng.message.entity.UMessage;

public class BaseApplication extends Application {

	/**
	 * Singleton pattern
	 */
	private static BaseApplication instance;
	public SharedPreferencesUtils sp;
	private UserModel loginUser;
	private static DbUtils dbUtils;
	/**
	 * 图片缓存
	 */
	//private ImageWorker mImageWorker;

	public static BaseApplication getInstance() {
		if (null == instance) {
			instance = new BaseApplication();
		}
		return instance;
	}

	public static DbUtils getDbUtils() {
		if (null == dbUtils) {
			dbUtils = DbUtils.create(getInstance());
		}
		return dbUtils;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		PushAgent mPushAgent = PushAgent.getInstance(this);
		// mPushAgent.enable();
//		int mImageThumbSize = getResources().getDimensionPixelSize(
//				R.dimen.image_thumbnail_size);
//
//		ImageCacheParams cacheParams = new ImageCacheParams();
//		cacheParams.reqHeight = mImageThumbSize;
//		cacheParams.reqWidth = mImageThumbSize;
//		// cacheParams.clearDiskCacheOnStart = true;
//		cacheParams.memCacheSize = (1024 * 1024 * ImageUtils
//				.getMemoryClass(this)) / 5;
//
//		cacheParams.memoryCacheEnabled = AppConfig.memoryCacheEnabled;
//		cacheParams.diskCacheEnabled = FileUtils.hasSDCard() ? AppConfig.diskCacheEnabled
//				: false;

		// cacheParams.clearDiskCacheOnStart = true;
//		mImageWorker = ImageWorker.newInstance(this);
//		mImageWorker.addParams("ImageCache", cacheParams);
//		mImageWorker.setLoadingImage(R.drawable.icon_empty_photo);

		sp = SharedPreferencesUtils.getInstance(this);

		loginUser = UserInfoUtils.getUserInfo();

		/* 自定义点击通知事件 */
		UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
			@Override
			public void dealWithCustomAction(final Context context,
					final UMessage msg) {
				// Toast.makeText(context, msg.custom,
				// Toast.LENGTH_LONG).show();
				if (loginUser!=null && loginUser.type == 6) {

					Intent start = new Intent(context,
							TzmPushOrderListActivity.class);
					start.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(start);
				} else {

					Intent start = new Intent(context, MainTabActivity.class);
					start.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(start);
				}

			}
		};
		mPushAgent.setNotificationClickHandler(notificationClickHandler);

	}

//	public static String getCurProcessName(Context context) {
//		int pid = android.os.Process.myPid();
//		ActivityManager activityManager = (ActivityManager) context
//				.getSystemService(Context.ACTIVITY_SERVICE);
//		for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
//				.getRunningAppProcesses()) {
//			if (appProcess.pid == pid) {
//				return appProcess.processName;
//			}
//		}
//		return null;
//	}

	public UserModel getLoginUser() {
		if (this.loginUser == null) {
			this.loginUser = UserInfoUtils.getUserInfo();
		}
		return loginUser;
	}

	public void setLoginUser(UserModel u) {
		this.loginUser = u;
	}

//	public ImageWorker getImageWorker() {
//		return this.mImageWorker;
//	}

	/**
	 * 是否已经登录
	 * 
	 * @return
	 */
	public boolean isLogin() {

		return true;
	}
	
	

}
