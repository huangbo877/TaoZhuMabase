package com.ruiyu.taozhuma.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.config.AppConfig;
import com.ruiyu.taozhuma.dialog.CustomCommonDialog;
import com.ruiyu.taozhuma.utils.UserInfoUtils;
import com.ruiyu.taozhuma.utils.ToastUtils;
import com.ruiyu.taozhuma.utils.ProgressDialogUtil;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

public class TzmSettingActivity extends Activity {
	private Button btn_head_left, settin_btn_exit;
	private TextView txt_head_title;
	private RelativeLayout rl_feedback, rl_reSetPassWork, rl_help_centre,
			rl_aboutus, rl_clear, rl_update;

	private Boolean isLogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tzm_setting_activity);
		initView();
		checkLogin();
	}

	private void initView() {
	
		rl_update = (RelativeLayout) this.findViewById(R.id.rl_update);
		rl_update.setOnClickListener(clickListener);
		rl_clear = (RelativeLayout) this.findViewById(R.id.rl_clear);
		rl_clear.setOnClickListener(clickListener);
		btn_head_left = (Button) this.findViewById(R.id.btn_head_left);
		rl_feedback = (RelativeLayout) this
				.findViewById(R.id.setting_rl_feedback);
		txt_head_title = (TextView) this.findViewById(R.id.txt_head_title);
		btn_head_left.setOnClickListener(clickListener);
		rl_feedback.setOnClickListener(clickListener);
		settin_btn_exit = (Button) this.findViewById(R.id.settin_btn_exit);
		settin_btn_exit.setOnClickListener(clickListener);
		txt_head_title.setText("设置");
		rl_reSetPassWork = (RelativeLayout) findViewById(R.id.rl_reSetPassWork);
		rl_reSetPassWork.setOnClickListener(clickListener);
		rl_help_centre = (RelativeLayout) this
				.findViewById(R.id.rl_help_centre);
		rl_help_centre.setOnClickListener(clickListener);
		rl_aboutus = (RelativeLayout) findViewById(R.id.rl_aboutus);
		rl_aboutus.setOnClickListener(clickListener);
		checkLogin();
		if (!isLogin) {
			rl_reSetPassWork.setVisibility(View.GONE);
			settin_btn_exit.setVisibility(View.GONE);
		}
	}

	View.OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.rl_update:
				ProgressDialogUtil.openProgressDialog(TzmSettingActivity.this,
						"", "");
				UmengUpdateAgent.setUpdateAutoPopup(false);
				UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
					@Override
					public void onUpdateReturned(int updateStatus,
							UpdateResponse updateInfo) {
						ProgressDialogUtil.closeProgressDialog();
						switch (updateStatus) {
						case UpdateStatus.Yes: // has update
							UmengUpdateAgent.showUpdateDialog(
									TzmSettingActivity.this, updateInfo);
							break;
						case UpdateStatus.No: // has no update
							ToastUtils.showShortToast(TzmSettingActivity.this, "您好,已经是最新版本！");
							break;
						case UpdateStatus.NoneWifi: // none wifi
							// Toast.makeText(TzmSettingActivity.this,
							// "没有wifi连接， 只在wifi下更新",
							// Toast.LENGTH_SHORT).show();
							break;
						case UpdateStatus.Timeout: // time out
							ToastUtils.showShortToast(TzmSettingActivity.this, "连接超时,请您稍后再试");
							break;
						}
					}
				});
				UmengUpdateAgent.forceUpdate(TzmSettingActivity.this);
				break;
			case R.id.rl_clear:
				CustomCommonDialog.Builder builder2 = new CustomCommonDialog.Builder(
						TzmSettingActivity.this);
				builder2.setTitle("确定要清除所有缓存吗？");
				builder2.setNegativeButtonText("取消");
				builder2.setPositiveButtonText("清除");
				builder2.setNegativeButton(new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();

					}
				});
				builder2.setPositiveButton(new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						ProgressDialogUtil.openProgressDialog(
								TzmSettingActivity.this, "", "");

						try {
							BitmapUtils bitmapUtils = new BitmapUtils(
									TzmSettingActivity.this);
							bitmapUtils.clearDiskCache();
							UserInfoUtils.clearSearchHistory();
							ToastUtils.showShortToast(TzmSettingActivity.this,
									"缓存清理完成");
						} catch (Exception e) {
							ToastUtils.showShortToast(TzmSettingActivity.this,
									"清理失败,请稍后再试");
						}
						ProgressDialogUtil.closeProgressDialog();
						dialog.dismiss();
					}
				});
				builder2.create().show();
				break;
			case R.id.rl_help_centre:
				startActivity(new Intent(TzmSettingActivity.this,
						TzmHelpTypeListActivity.class));
				break;

			case R.id.btn_head_left:
				onBackPressed();
				break;
			case R.id.setting_rl_feedback:
				Intent intent_feedback = new Intent(TzmSettingActivity.this,
						TzmFeedbackActivity.class);
				startActivity(intent_feedback);
				break;
			case R.id.settin_btn_exit:
				AlertDialog.Builder builder = new Builder(
						TzmSettingActivity.this);
				builder.setMessage("确认退出登录吗？");
				builder.setTitle("提示");
				builder.setPositiveButton("确认",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								UserInfoUtils.signOut();
								Intent intent = new Intent();
								setResult(AppConfig.TZM_LOGIN_OUT, intent);
								finish();

							}
						});
				builder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
							}
						});
				builder.create().show();
				break;
			case R.id.rl_reSetPassWork:
				Intent intent_reSetPassWork = new Intent(
						TzmSettingActivity.this, TzmReSetPassWork.class);
				startActivity(intent_reSetPassWork);
				break;
			case R.id.rl_aboutus:
				Intent intent_aboutus = new Intent(TzmSettingActivity.this,
						TzmAboutusActivity.class);
				startActivity(intent_aboutus);
				break;
			}

		}
	};

	// 检查用户是否登陆
	private void checkLogin() {
		isLogin = UserInfoUtils.isLogin();
	}
}
