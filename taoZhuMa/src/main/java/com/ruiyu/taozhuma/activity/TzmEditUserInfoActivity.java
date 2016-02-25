package com.ruiyu.taozhuma.activity;

import java.io.File;
import java.lang.reflect.Type;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.api.ApiClient;
import com.ruiyu.taozhuma.api.ApiListener;
import com.ruiyu.taozhuma.api.EditUserInfoApi;
import com.ruiyu.taozhuma.api.GetUserInfoApi;
import com.ruiyu.taozhuma.base.BaseApplication;
import com.ruiyu.taozhuma.base.xUtilsImageLoader;
import com.ruiyu.taozhuma.dialog.UserInfoSexDialog;
import com.ruiyu.taozhuma.dialog.UserInfoSexDialog.Builder;
import com.ruiyu.taozhuma.model.BaseModel;
import com.ruiyu.taozhuma.model.GetUserInfoModel;
import com.ruiyu.taozhuma.model.UserModel;
import com.ruiyu.taozhuma.utils.LogUtil;
import com.ruiyu.taozhuma.utils.PictureUtil;
import com.ruiyu.taozhuma.utils.ProgressDialogUtil;
import com.ruiyu.taozhuma.utils.StringUtils;
import com.ruiyu.taozhuma.utils.ToastUtils;
import com.ruiyu.taozhuma.utils.UserInfoUtils;
import com.ruiyu.taozhuma.widget.CircularImage;
import com.ruiyu.taozhuma.widget.TimeWheelView;

/**
 * 备份//QQ
 * 
 * @author Fu
 * 
 */
public class TzmEditUserInfoActivity extends Activity {
	private TextView txt_head_title;
	private Button btn_head_left, btn_head_right;
	private TextView tv_user_name, tv_user_sex;
	private CircularImage img_user_photo;// 圆形头像
	private ImageView imageView1, imageView3, imageView4;

	private String imagePath = "";// 图片路径

	private String[] sex_str = { "请选择", "宝爸", "宝妈" };
	private GetUserInfoApi getUserInfoApi;
	private EditUserInfoApi editUserInfoApi;
	private ApiClient apiClient;
	private GetUserInfoModel getUserInfoModel;

	private UserModel userInfo;
	private xUtilsImageLoader imageLoader;
	private int sex = 0;// 性别
	private String user_name = "";
	private String QQ = "";
	private String babyBirthday = "";
	private Integer dateYear, dateMonth, dateDay;
	@ViewInject(R.id.rl_baby_birth)
	private RelativeLayout rl_baby_birth;// 宝宝生日
	@ViewInject(R.id.tv_birthday_baby)
	private TextView tv_birthday_baby;
	@ViewInject(R.id.tv_qq)
	private TextView tv_qq;
	@ViewInject(R.id.rl_baby_sex)
	private RelativeLayout rl_baby_sex;// baby性别
	@ViewInject(R.id.rl_name)
	private RelativeLayout rl_name;
	@ViewInject(R.id.rl_qq)
	private RelativeLayout rl_qq;
	private int babySex = 0;// baby性别 1为王子 2为公主
	@ViewInject(R.id.tv_babysex)
	private TextView tv_babysex;

	private String[] baby_sex_str = { "请选择", "王子", "公主" };

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				tv_user_sex.setText("宝爸");
				break;
			case 2:
				tv_user_sex.setText("宝妈");
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tzm_edit_user_info_activity);
		ViewUtils.inject(this);
		initView();
		userInfo = BaseApplication.getInstance().getLoginUser();
		getUserInfoApi = new GetUserInfoApi();
		editUserInfoApi = new EditUserInfoApi();
		apiClient = new ApiClient(this);
		imageLoader = new xUtilsImageLoader(this);
		loadData();

	}

	private void initView() {
		txt_head_title = (TextView) findViewById(R.id.txt_head_title);
		txt_head_title.setText("个人资料");
		btn_head_left = (Button) findViewById(R.id.btn_head_left);
		btn_head_left.setOnClickListener(clickListener);
		btn_head_right = (Button) findViewById(R.id.btn_head_right);
		btn_head_right.setOnClickListener(clickListener);
		btn_head_right.setTextColor(Color.parseColor("#e61e58"));
		btn_head_right.setText("保存");
		btn_head_right.setVisibility(View.VISIBLE);
		img_user_photo = (CircularImage) findViewById(R.id.img_user_photo);
		tv_user_name = (TextView) findViewById(R.id.tv_user_name);
		tv_user_sex = (TextView) findViewById(R.id.tv_user_sex);
		imageView1 = (ImageView) findViewById(R.id.imageView1);
		imageView3 = (ImageView) findViewById(R.id.imageView3);
		imageView4 = (ImageView) findViewById(R.id.imageView4);
		img_user_photo.setOnClickListener(clickListener);
		tv_user_sex.setOnClickListener(clickListener);
		imageView1.setOnClickListener(clickListener);
		imageView3.setOnClickListener(clickListener);
		imageView4.setOnClickListener(clickListener);
		rl_baby_birth.setOnClickListener(clickListener);
		rl_baby_sex.setOnClickListener(clickListener);
		rl_name.setOnClickListener(clickListener);
		rl_qq.setOnClickListener(clickListener);
	}

	// 初始化数据
	private void loadData() {
		getUserInfoApi.setUid(userInfo.uid);
		apiClient.api(getUserInfoApi, new ApiListener() {

			@Override
			public void onStart() {
				ProgressDialogUtil.openProgressDialog(
						TzmEditUserInfoActivity.this, "", "正在提交...");
			}

			@Override
			public void onError(String error) {
				ProgressDialogUtil.closeProgressDialog();
				LogUtil.Log(error);
				ToastUtils.showShortToast(TzmEditUserInfoActivity.this, error);
			}

			@Override
			public void onException(Exception e) {
				ProgressDialogUtil.closeProgressDialog();
				LogUtil.ErrorLog(e);
			}

			@Override
			public void onComplete(String jsonStr) {
				ProgressDialogUtil.closeProgressDialog();
				if (StringUtils.isNotBlank(jsonStr)) {
					try {
						Gson gson = new Gson();
						Type type = new TypeToken<BaseModel<GetUserInfoModel>>() {
						}.getType();
						BaseModel<GetUserInfoModel> base = gson.fromJson(
								jsonStr, type);
						if (base.success) {
							getUserInfoModel = base.result;
							if (StringUtils.isNotEmpty(getUserInfoModel.image)) {
								imageLoader.display(img_user_photo,
										getUserInfoModel.image);
							}
							tv_user_name.setText(getUserInfoModel.name);
							user_name = getUserInfoModel.name;
							tv_user_sex.setText(sex_str[getUserInfoModel.sex]);
							sex = getUserInfoModel.sex;
							tv_babysex
									.setText(baby_sex_str[getUserInfoModel.babySex]);
							tv_birthday_baby
									.setText(getUserInfoModel.babyBirthday);
							tv_qq.setText(getUserInfoModel.QQ);

						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}, true);
	}

	// 选择图片
	private void selectPic(int imgId) {
		Intent intent1 = new Intent(this, SelectPicActivity.class);
		intent1.putExtra("imgId", imgId);
		startActivityForResult(intent1, imgId);
	}

	// 点击监听
	OnClickListener clickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {

			switch (v.getId()) {
			case R.id.btn_head_left:
				onBackPressed();
				break;
			case R.id.btn_head_right:
				submitData();
				break;
			case R.id.img_user_photo:
				selectPic(R.id.img_user_photo);
				break;
			case R.id.imageView1:
				selectPic(R.id.img_user_photo);
				break;
			case R.id.rl_name:
				user_name = tv_user_name.getText().toString();
				Intent intent2 = new Intent(TzmEditUserInfoActivity.this,
						TzmResetNameActivity.class);
				intent2.putExtra("userName", user_name);
				startActivityForResult(intent2, R.id.tv_user_name);
				break;
			case R.id.rl_qq:
				QQ = tv_qq.getText().toString();
				Intent intent4 = new Intent(TzmEditUserInfoActivity.this,
						TzmResetQQActivity.class);
				intent4.putExtra("userName", QQ);
				startActivityForResult(intent4, R.id.tv_qq);
				break;

			case R.id.tv_user_sex:
				setDialog();
				break;
			case R.id.imageView3:
				setDialog();
				break;
			case R.id.rl_baby_birth:
				datePickerShow(tv_birthday_baby);
				break;
			case R.id.rl_baby_sex:
				setBaBySexDialog();
				break;
			}
		}
	};

	// 宝宝性别选择
	protected void setBaBySexDialog() {
		UserInfoSexDialog.Builder builder = new Builder(
				TzmEditUserInfoActivity.this);
		builder.setMale("王子");
		builder.setFamale("公主");
		builder.setSex1ClickListener(new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				babySex = 1;
				tv_babysex.setText("王子");
				dialog.dismiss();
			}
		});
		builder.setSex2ClickListener(new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				babySex = 2;
				tv_babysex.setText("公主");
				dialog.dismiss();
			}
		});
		builder.setCancelClickListener(new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		builder.create().show();
	}

	// 性别选择
	protected void setDialog() {
		UserInfoSexDialog.Builder builder = new Builder(
				TzmEditUserInfoActivity.this);
		builder.setSex1ClickListener(new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Message message = handler.obtainMessage();
				sex = 1;
				message.what = 1;
				handler.sendMessage(message);
				dialog.dismiss();
			}
		});
		builder.setSex2ClickListener(new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Message message = handler.obtainMessage();
				sex = 2;
				message.what = 2;
				handler.sendMessage(message);
				dialog.dismiss();
			}
		});
		builder.setCancelClickListener(new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	// 时间选择
	protected void datePickerShow(final TextView textView) {
		final Dialog dialog2 = new Dialog(this, R.style.DialogStyleBottom);
		dialog2.setContentView(R.layout.time_wheelview_layout);

		TimeWheelView timeWheelView = (TimeWheelView) dialog2
				.findViewById(R.id.datepicker_layout);
		TextView tv_confirm = (TextView) dialog2.findViewById(R.id.tv_confirm);
		TextView tv_cancel = (TextView) dialog2.findViewById(R.id.tv_cancel);
		timeWheelView.setOnChangeListener(new TimeWheelView.OnChangeListener() {
			@Override
			public void onChange(int year, int month, int day, int day_of_week) {
				dateYear = year;
				dateMonth = month;
				dateDay = day;

			}
		});
		String m = timeWheelView.getMonth() < 10 ? "0"
				+ timeWheelView.getMonth() : "" + timeWheelView.getMonth();
		String d = timeWheelView.getDay() < 10 ? "0" + timeWheelView.getDay()
				: "" + timeWheelView.getDay();
		final String td = timeWheelView.getYear() + m + d;

		tv_confirm.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				String cm = dateMonth < 10 ? "0" + dateMonth : "" + dateMonth;
				String cd = dateDay < 10 ? "0" + dateDay : "" + dateDay;
				String ctd = dateYear + cm + cd;
				if (Integer.parseInt(ctd) > Integer.parseInt(td)) {
					ToastUtils.showToast(TzmEditUserInfoActivity.this,
							"请选择正确的日期");
				} else {

					if (dateMonth < 10 && dateDay < 10) {
						babyBirthday = dateYear + "-0" + dateMonth + "-0"
								+ dateDay;
					} else if (dateMonth >= 10 && dateDay < 10) {
						babyBirthday = dateYear + "-" + dateMonth + "-0"
								+ dateDay;
					} else if (dateMonth < 10 && dateDay >= 10) {
						babyBirthday = dateYear + "-0" + dateMonth + "-"
								+ dateDay;
					} else {
						babyBirthday = dateYear + "-" + dateMonth + "-"
								+ dateDay;
					}
					textView.setText(babyBirthday);
					dialog2.dismiss();
				}
			}
		});

		tv_cancel.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View view) {
				dialog2.dismiss();
			}
		});
		dialog2.setCancelable(false);
		dialog2.setOnKeyListener(new android.content.DialogInterface.OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				switch (keyCode) {
				case KeyEvent.KEYCODE_BACK:
					dialog2.dismiss();
					return true;
				}
				return false;
			}
		});
		dialog2.show();

	}

	private void submitData() {
		RequestParams params = new RequestParams();
		params.setHeader("User-Agent", "XieYeAndroidSDK");
//		params.setHeader("Accept-Encoding", "gzip, deflate");
//		params.setHeader("Accept", "textml,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
//		params.setHeader("Accept-Language", "en-us");
//		params.setHeader("Connection", "keep-alive");
//		params.setHeader("Cache-Control", "max-age=0");
		params.addBodyParameter("uid", userInfo.uid + "");
		if (StringUtils.isNotBlank(imagePath)) {
			params.addBodyParameter("file", new File(imagePath));
		}
		user_name = tv_user_name.getText().toString();
		if (StringUtils.isNotBlank(user_name)) {
			params.addBodyParameter("name", user_name + "");
		}
		// if (user_name) {
		// ToastUtils.showShortToast(TzmEditUserInfoActivity.this,
		// "昵称长度不符合要求！");
		// }
		if (sex != 0) {
			params.addBodyParameter("sex", sex + "");
		}
		if (StringUtils.isNotBlank(babyBirthday)) {
			params.addBodyParameter("babyBirthday", babyBirthday);
		}
		QQ = tv_qq.getText().toString();
		if (StringUtils.isEmpty(QQ)) {
			ToastUtils
					.showShortToast(TzmEditUserInfoActivity.this, "QQ号码不能为空！");

			return;
		}
		params.addBodyParameter("QQ", QQ);
		if (babySex != 0) {
			params.addBodyParameter("babySex", babySex + "");
		}
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.POST, editUserInfoApi.getUrl(), params,
				new RequestCallBack<String>() {

					@Override
					public void onStart() {
						ProgressDialogUtil.openProgressDialog(
								TzmEditUserInfoActivity.this, "", "正在提交...");
					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						ProgressDialogUtil.closeProgressDialog();
						if (StringUtils.isNotBlank(responseInfo.result)) {
							try {
								JSONObject json = new JSONObject(
										responseInfo.result);
								ToastUtils.showShortToast(
										TzmEditUserInfoActivity.this,
										json.getString("error_msg"));
								if (json.getBoolean("success")) {
									userInfo.name = user_name;
									UserInfoUtils.updateUserInfo(userInfo);
									Intent mIntent = new Intent();
									TzmEditUserInfoActivity.this.setResult(
											Activity.RESULT_OK, mIntent);
									finish();

								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						ToastUtils.showShortToast(TzmEditUserInfoActivity.this,
								error.getExceptionCode() + ":" + msg);
						ProgressDialogUtil.closeProgressDialog();
					}
				});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == R.id.img_user_photo) {
				ImageView img = (ImageView) findViewById(requestCode);
				img.setImageBitmap(null);
				String picPath = data
						.getStringExtra(SelectPicActivity.KEY_PHOTO_PATH);
				LogUtil.Log(picPath);

				if (picPath != null && !StringUtils.isBlank(picPath)) {
					LogUtil.Log(requestCode + "最终选择的图片=" + picPath);
					Bitmap bm = PictureUtil.getSmallBitmap(picPath);
					img.setImageBitmap(bm);
					imagePath = picPath;
				}

			}
			if (requestCode == R.id.tv_user_name) {
				user_name = data.getStringExtra(TzmResetNameActivity.USER_NAME);
				tv_user_name.setText(user_name);
			}
			if (requestCode == R.id.tv_qq) {
				user_name = data.getStringExtra(TzmResetQQActivity.USER_NAME);
				tv_qq.setText(user_name);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}
