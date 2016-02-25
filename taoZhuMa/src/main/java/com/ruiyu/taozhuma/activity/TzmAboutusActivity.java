package com.ruiyu.taozhuma.activity;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.model.Contact;
import com.ruiyu.taozhuma.utils.ProgressDialogUtil;
import com.ruiyu.taozhuma.utils.ToastUtils;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TzmAboutusActivity extends Activity {
	@ViewInject(R.id.btn_head_left)
	private Button btn_head_left;
	@ViewInject(R.id.txt_head_title)
	private TextView txt_head_title;
	@ViewInject(R.id.tv_version)
	private TextView tv_version;
	@ViewInject(R.id.rl_version)
	private RelativeLayout rl_version;
	@ViewInject(R.id.rl_agreement)
	private RelativeLayout rl_agreement;
	//@ViewInject(R.id.rl_declaration)
	//private RelativeLayout rl_declaration;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tzm_abouttzm_activity);
		ViewUtils.inject(this);
		initView();
	}

	private void initView() {
		btn_head_left.setOnClickListener(clickListener);
		rl_version.setOnClickListener(clickListener);
		rl_agreement.setOnClickListener(clickListener);
		//rl_declaration.setOnClickListener(clickListener);
		txt_head_title.setText("关于淘竹马");
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
	}

	View.OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
//			case R.id.rl_declaration:
//				Intent intentd = new Intent(TzmAboutusActivity.this,
//						TzmStaticHelpDetailActivity.class);
//				intentd.putExtra("id", 28);
//				startActivity(intentd);
//				break;
			case R.id.rl_agreement:
				Intent intent = new Intent(TzmAboutusActivity.this,
						TzmStaticHelpDetailActivity.class);
				intent.putExtra("id", 27);
				startActivity(intent);
				break;
			case R.id.btn_head_left:
				onBackPressed();
				break;
			case R.id.rl_version:
				ProgressDialogUtil.openProgressDialog(TzmAboutusActivity.this,
						"", "");
				UmengUpdateAgent.setUpdateAutoPopup(false);
				UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
					@Override
					public void onUpdateReturned(int updateStatus,
							UpdateResponse updateInfo) {
						ProgressDialogUtil.closeProgressDialog();
						switch (updateStatus) {
						case UpdateStatus.Yes: 
							UmengUpdateAgent.showUpdateDialog(
									TzmAboutusActivity.this, updateInfo);
							break;
						case UpdateStatus.No: 
							ToastUtils.showShortToast(TzmAboutusActivity.this,
									"您好,已经是最新版本！");
							break;
						case UpdateStatus.NoneWifi: 
							break;
						case UpdateStatus.Timeout: 
							ToastUtils.showShortToast(TzmAboutusActivity.this,
									"连接超时,请您稍后再试");
							break;
						}
					}
				});
				UmengUpdateAgent.forceUpdate(TzmAboutusActivity.this);
				break;

			}

		}
	};
}
