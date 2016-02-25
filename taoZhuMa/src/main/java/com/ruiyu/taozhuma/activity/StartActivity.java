package com.ruiyu.taozhuma.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.api.ApiClient;
import com.ruiyu.taozhuma.api.ApiListener;
import com.ruiyu.taozhuma.api.NavigationListApi;
import com.ruiyu.taozhuma.fragment.NewHomeFragment;
import com.ruiyu.taozhuma.model.ActivityTimeListModel;
import com.ruiyu.taozhuma.model.BaseModel;
import com.ruiyu.taozhuma.model.NavigationlistModel;
import com.ruiyu.taozhuma.utils.LogUtil;
import com.ruiyu.taozhuma.utils.ToastUtils;
import com.ruiyu.taozhuma.utils.UserInfoUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

/**
 * 启动设置
 * 
 * @author wen
 * 
 */
public class StartActivity extends Activity {
	NavigationListApi NavigationListApi;
	ApiClient client;
	public static ArrayList<NavigationlistModel> model;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start_activyty);
		model = new ArrayList<NavigationlistModel>();
		loadTimeActivity();

		String appVersion = "";
		PackageManager manager = this.getPackageManager();
		try {
			PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
			appVersion = info.versionName; // 版本名
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		TextView tv_version = (TextView) findViewById(R.id.tv_version);
		tv_version.setText("v " + appVersion);

		// if(FileUtils.hasSDCard()){
		// ToastUtils.showShortToast(this, "Has SdCard");
		//
		// File f=DiskCache.getDiskCacheDir(DiskCache.DISK_CACHE_DIR);
		// LogUtil.Log(f.toString());
		// }else{
		// ToastUtils.showShortToast(this, "No SDCard");
		// }

	}

	@Override
	protected void onDestroy() {
		handler.removeCallbacksAndMessages(null);
		super.onDestroy();
	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (UserInfoUtils.getIsFirst()) {
				startActivity(new Intent(StartActivity.this,
						TzmAdActivity.class));
				UserInfoUtils.setFirst(false);
			} else {
				startActivity(new Intent(StartActivity.this,
						MainTabActivity.class));
			}

			finish();
		}

	};

	private void loadTimeActivity() {
		NavigationListApi = new NavigationListApi();
		client = new ApiClient(StartActivity.this);

		client.api(NavigationListApi, new ApiListener() {

			@Override
			public void onStart() {
			}

			@Override
			public void onException(Exception e) {
				LogUtil.ErrorLog(e);
				ToastUtils.showToast(StartActivity.this, "网络异常,请检查！");
				new Handler().postDelayed(new Runnable() {
					public void run() {
						// execute the task
						finish();
					}
				}, 3000);

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
					handler.sendEmptyMessageDelayed(1, 2000);
				}
			}

		}, true);
	}
}
