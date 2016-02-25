package com.ruiyu.taozhuma.activity;

import java.io.File;

import org.json.JSONObject;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.api.TzmApplyManufacturerApi;
import com.ruiyu.taozhuma.base.BaseApplication;
import com.ruiyu.taozhuma.dialog.CustomDialog;
import com.ruiyu.taozhuma.dialog.CustomDialog.Builder;
import com.ruiyu.taozhuma.model.UserModel;
import com.ruiyu.taozhuma.utils.LogUtil;
import com.ruiyu.taozhuma.utils.PictureUtil;
import com.ruiyu.taozhuma.utils.ProgressDialogUtil;
import com.ruiyu.taozhuma.utils.StringUtils;
import com.ruiyu.taozhuma.utils.ToastUtils;
import com.ruiyu.taozhuma.utils.UserInfoUtils;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

//供销
public class TzmApplyManufacturerActivity extends Activity {
	private TextView txt_head_title;
	private Button btn_head_left;
	private ArrayAdapter<String> adapter, adapter2;
	private String[] mainCategorys = { "遥控/电动玩具", "早教/音乐玩具", "过家家玩具", "童车玩具",
			"益智玩具", "其他玩具" };
	private String[] groups = { "1~5人", "6~20人", "21~50人", "51~100人", "100人以上" };
	private Spinner sp_mainCategory, sp_groups;
	private TextView tv_mainCategory, tv_groups;
	private EditText et_companyName, et_brand, et_salesArea, et_contact,
			et_duty, et_email, et_QQ, et_phone, et_fax, et_webSite,
			et_address, et_mainProduct, et_content;
	private Button applyManufacturer_submit;
	private RelativeLayout rl_attestationImage, rl_user3cImage;
	//private ImageView img_attestationImage, img_user3cImage;

	private Integer mainCategory, group;
	private long uid;
	private String companyName, brand, salesArea, contact, duty, email, QQ,
			 phone, fax, webSite, address, mainProduct, content;
	private UserModel userInfo;
	private CheckBox cb_consent;

	private TzmApplyManufacturerApi tzmApplyManufacturerApi;
	//private ApiClient apiClient;
	private String attestationImagePath, user3cImagePath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tzm_apply_manufacturer_activity);
		txt_head_title = (TextView) findViewById(R.id.txt_head_title);
		txt_head_title.setText("申请供销商");
		btn_head_left = (Button) findViewById(R.id.btn_head_left);
		btn_head_left.setOnClickListener(clickListener);
		userInfo = BaseApplication.getInstance().getLoginUser();
		initView();
	}

	private void initView() {
		sp_mainCategory = (Spinner) findViewById(R.id.sp_mainCategory);
		tv_mainCategory = (TextView) findViewById(R.id.tv_mainCategory);
		tv_mainCategory.setOnClickListener(clickListener);

		sp_groups = (Spinner) findViewById(R.id.sp_groups);
		tv_groups = (TextView) findViewById(R.id.tv_groups);
		tv_groups.setOnClickListener(clickListener);
		adapter = new ArrayAdapter<String>(TzmApplyManufacturerActivity.this,
				android.R.layout.simple_spinner_item, mainCategorys);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_mainCategory.setAdapter(adapter);
		sp_mainCategory
				.setOnItemSelectedListener(new SpinnerSelectedListener());
		sp_mainCategory.setSelection(0, true);
		adapter2 = new ArrayAdapter<String>(TzmApplyManufacturerActivity.this,
				android.R.layout.simple_spinner_item, groups);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_groups.setAdapter(adapter2);
		sp_groups.setOnItemSelectedListener(new SpinnerSelectedListener2());
		sp_groups.setSelection(0, true);

		et_companyName = (EditText) findViewById(R.id.et_companyName);
		et_brand = (EditText) findViewById(R.id.et_brand);
		et_salesArea = (EditText) findViewById(R.id.et_salesArea);
		et_contact = (EditText) findViewById(R.id.et_contact);
		et_duty = (EditText) findViewById(R.id.et_duty);
		et_email = (EditText) findViewById(R.id.et_email);
		et_QQ = (EditText) findViewById(R.id.et_QQ);
		//et_tel = (EditText) findViewById(R.id.et_tel);
		et_phone = (EditText) findViewById(R.id.et_phone);
		et_fax = (EditText) findViewById(R.id.et_fax);
		et_webSite = (EditText) findViewById(R.id.et_webSite);
		et_address = (EditText) findViewById(R.id.et_address);
		et_mainProduct = (EditText) findViewById(R.id.et_mainProduct);
		et_content = (EditText) findViewById(R.id.et_content);

		rl_attestationImage = (RelativeLayout) findViewById(R.id.rl_attestationImage);
		rl_user3cImage = (RelativeLayout) findViewById(R.id.rl_user3cImage);
		//img_attestationImage = (ImageView) findViewById(R.id.img_attestationImage);
		//img_user3cImage = (ImageView) findViewById(R.id.img_user3cImage);
		rl_attestationImage.setOnClickListener(clickListener);
		rl_user3cImage.setOnClickListener(clickListener);
		cb_consent = (CheckBox) findViewById(R.id.cb_consent);

		applyManufacturer_submit = (Button) findViewById(R.id.applyManufacturer_submit);
		applyManufacturer_submit.setOnClickListener(clickListener);
	}

	View.OnClickListener clickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_head_left:
				onBackPressed();
				break;
			case R.id.tv_mainCategory:
				sp_mainCategory.performClick();
				sp_mainCategory.setVisibility(View.VISIBLE);
				tv_mainCategory.setVisibility(View.GONE);
				break;
			case R.id.tv_groups:
				sp_groups.performClick();
				sp_groups.setVisibility(View.VISIBLE);
				tv_groups.setVisibility(View.GONE);
				break;
			case R.id.applyManufacturer_submit:
				postSubmit();
				// applyManufacturer_submit();
				break;
			case R.id.rl_attestationImage:
				// Toast.makeText(TzmApplyManufacturerActivity.this, "暂时不能上传",
				// Toast.LENGTH_SHORT).show();
				selectPic(R.id.img_attestationImage);
				break;
			case R.id.rl_user3cImage:
				// Toast.makeText(TzmApplyManufacturerActivity.this, "暂时不能上传",
				// Toast.LENGTH_SHORT).show();
				selectPic(R.id.img_user3cImage);
				break;

			default:
				break;
			}
		}
	};

	

	class SpinnerSelectedListener implements OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			mainCategory = arg2;
		}

		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}

	class SpinnerSelectedListener2 implements OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			group = arg2;
		}

		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}

//	private void applyManufacturer_submit() {
//		uid = userInfo.uid;
//		companyName = et_companyName.getText().toString();
//		brand = et_brand.getText().toString();
//		salesArea = et_salesArea.getText().toString();
//		contact = et_contact.getText().toString();
//		duty = et_duty.getText().toString();
//		email = et_email.getText().toString();
//		QQ = et_QQ.getText().toString();
//		//tel = et_tel.getText().toString();
//		phone = et_phone.getText().toString();
//		fax = et_fax.getText().toString();
//		webSite = et_webSite.getText().toString();
//		address = et_address.getText().toString();
//		mainProduct = et_mainProduct.getText().toString();
//		content = et_content.getText().toString();
//		if (StringUtils.isBlank(companyName)) {
//			Toast.makeText(TzmApplyManufacturerActivity.this, "请输入公司名称",
//					Toast.LENGTH_SHORT).show();
//			return;
//		}
//		if (tv_mainCategory.getVisibility() == View.VISIBLE) {
//			Toast.makeText(TzmApplyManufacturerActivity.this, "请选择主营类目",
//					Toast.LENGTH_SHORT).show();
//			return;
//		}
//		if (tv_groups.getVisibility() == View.VISIBLE) {
//			Toast.makeText(TzmApplyManufacturerActivity.this, "请选择运营总人数",
//					Toast.LENGTH_SHORT).show();
//			return;
//		}
//		if (StringUtils.isBlank(brand)) {
//			Toast.makeText(TzmApplyManufacturerActivity.this, "请输入自营品牌",
//					Toast.LENGTH_SHORT).show();
//			return;
//		}
//		if (StringUtils.isBlank(salesArea)) {
//			Toast.makeText(TzmApplyManufacturerActivity.this, "请输入销售地区",
//					Toast.LENGTH_SHORT).show();
//			return;
//		}
//		if (StringUtils.isBlank(contact)) {
//			Toast.makeText(TzmApplyManufacturerActivity.this, "请输入联系人",
//					Toast.LENGTH_SHORT).show();
//			return;
//		}
//		if (StringUtils.isBlank(email)) {
//			Toast.makeText(TzmApplyManufacturerActivity.this, "请输入联系人邮箱",
//					Toast.LENGTH_SHORT).show();
//			return;
//		}
//		if (!StringUtils.isEmailStr(email)) {
//			Toast.makeText(TzmApplyManufacturerActivity.this, "请检查一下邮箱",
//					Toast.LENGTH_SHORT).show();
//			return;
//		}
//		if (StringUtils.isBlank(QQ)) {
//			Toast.makeText(TzmApplyManufacturerActivity.this, "请输入联系人QQ",
//					Toast.LENGTH_SHORT).show();
//			return;
//		}
//		if (StringUtils.isBlank(phone)) {
//			Toast.makeText(TzmApplyManufacturerActivity.this, "请输入手机号码",
//					Toast.LENGTH_SHORT).show();
//			return;
//		}
//		if (!StringUtils.isMobileNO(phone)) {
//			Toast.makeText(TzmApplyManufacturerActivity.this, "请检查一下手机号码",
//					Toast.LENGTH_SHORT).show();
//			return;
//		}
//		if (StringUtils.isBlank(address)) {
//			Toast.makeText(TzmApplyManufacturerActivity.this, "请输入公司地址",
//					Toast.LENGTH_SHORT).show();
//			return;
//		}
//		if (StringUtils.isBlank(mainProduct)) {
//			Toast.makeText(TzmApplyManufacturerActivity.this, "请输入主营产品",
//					Toast.LENGTH_SHORT).show();
//			return;
//		}
//		if (StringUtils.isBlank(content)) {
//			Toast.makeText(TzmApplyManufacturerActivity.this, "请输入公司简介",
//					Toast.LENGTH_SHORT).show();
//			return;
//		}
//		if (!cb_consent.isChecked()) {
//			Toast.makeText(TzmApplyManufacturerActivity.this, "请同意供货服务条款",
//					Toast.LENGTH_SHORT).show();
//			return;
//		}
//		tzmApplyManufacturerApi = new TzmApplyManufacturerApi();
//		apiClient = new ApiClient(this);
//		tzmApplyManufacturerApi.setUid(uid);
//		tzmApplyManufacturerApi.setCompanyName(companyName);
//		tzmApplyManufacturerApi.setMainCategory(mainCategory);
//		tzmApplyManufacturerApi.setBrand(brand);
//		tzmApplyManufacturerApi.setGroups(group);
//		tzmApplyManufacturerApi.setSalesArea(salesArea);
//		tzmApplyManufacturerApi.setContact(contact);
//		tzmApplyManufacturerApi.setDuty(duty);
//		tzmApplyManufacturerApi.setEmail(email);
//		tzmApplyManufacturerApi.setQQ(QQ);
//		tzmApplyManufacturerApi.setPhone(phone);
//		tzmApplyManufacturerApi.setFax(fax);
//		tzmApplyManufacturerApi.setWebSite(webSite);
//		tzmApplyManufacturerApi.setMainProduct(mainProduct);
//		tzmApplyManufacturerApi.setContent(content);
//		tzmApplyManufacturerApi.setAddress(address);
//		tzmApplyManufacturerApi.setUser3cImage("123456");
//		tzmApplyManufacturerApi.setAttestationImage("123456");
//		apiClient.api(tzmApplyManufacturerApi, new ApiListener() {
//
//			@Override
//			public void onStart() {
//				ProgressDialogUtil.openProgressDialog(
//						TzmApplyManufacturerActivity.this, "", "正在提交...");
//			}
//
//			@Override
//			public void onError(String error) {
//				ProgressDialogUtil.closeProgressDialog();
//				LogUtil.Log(error);
//				Toast.makeText(TzmApplyManufacturerActivity.this, error,
//						Toast.LENGTH_SHORT).show();
//			}
//
//			@Override
//			public void onException(Exception e) {
//				ProgressDialogUtil.closeProgressDialog();
//				LogUtil.ErrorLog(e);
//			}
//
//			@Override
//			public void onComplete(String jsonStr) {
//				ProgressDialogUtil.closeProgressDialog();
//				if (StringUtils.isNotBlank(jsonStr)) {
//					try {
//						JSONObject json = new JSONObject(jsonStr);
//						Toast.makeText(TzmApplyManufacturerActivity.this,
//								json.getString("error_msg"), Toast.LENGTH_SHORT)
//								.show();
//						if (json.getBoolean("success")) {
//							userInfo.judgeType = 1;
//							UserInfoUtils.updateUserInfo(userInfo);
//							onBackPressed();
//						}
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		}, true);
//
//	}

	protected void dialog() {
		CustomDialog.Builder builder = new Builder(this);
		builder.setConsumerClickListener(new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(intent, 1);
				dialog.dismiss();

			}
		});
		builder.setManufacturerClickListener(new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();

			}
		});
		builder.setNegativeButton(new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();

			}
		});
		builder.create().show();
	}
	
	/**
	 * 选择图片
	 * @param imgId
	 */
	private void selectPic(int imgId) {
		Intent intent1 = new Intent(this, SelectPicActivity.class);
		intent1.putExtra("imgId", imgId);
		startActivityForResult(intent1, imgId);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			// imageView不设null, 第一次上传成功后，第二次在选择上传的时候会报错。
			ImageView img = (ImageView) findViewById(requestCode);
			img.setImageBitmap(null);
			String picPath = data
					.getStringExtra(SelectPicActivity.KEY_PHOTO_PATH);
			LogUtil.Log(picPath);

			if (picPath != null && !StringUtils.isBlank(picPath)) {
				LogUtil.Log(requestCode + "最终选择的图片=" + picPath);
				Bitmap bm = PictureUtil.getSmallBitmap(picPath);
				img.setImageBitmap(bm);
				if (requestCode == R.id.img_attestationImage) {
					attestationImagePath = picPath;
				}
				if (requestCode == R.id.img_user3cImage) {
					user3cImagePath = picPath;
				}
				// FileItem f = new FileItem(picPath);
				// addCommentApi.image.put(requestCode + "", f);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
		// if (resultCode == Activity.RESULT_OK) {
		// String sdStatus = Environment.getExternalStorageState();
		// if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
		// Log.i("TestFile",
		// "SD card is not avaiable/writeable right now.");
		// return;
		// }
		// String name = new DateFormat().format("yyyyMMdd_hhmmss",
		// Calendar.getInstance(Locale.CHINA))
		// + ".jpg";
		// Toast.makeText(this, name, Toast.LENGTH_LONG).show();
		// Bundle bundle = data.getExtras();
		// Bitmap bitmap = (Bitmap) bundle.get("data");//
		// 获取相机返回的数据，并转换为Bitmap图片格式
		//
		// FileOutputStream b = null;
		// // ???????????????????????????????为什么不能直接保存在系统相册位置呢？？？？？？？？？？？？
		// File file = new File("/sdcard/myImage/");
		// file.mkdirs();// 创建文件夹
		// String fileName = "/sdcard/myImage/" + name;
		//
		// try {
		// b = new FileOutputStream(fileName);
		// bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
		// } catch (FileNotFoundException e) {
		// e.printStackTrace();
		// } finally {
		// try {
		// b.flush();
		// b.close();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// }
		// ((ImageView) findViewById(R.id.img_attestationImage))
		// .setImageBitmap(bitmap);// 将图片显示在ImageView里
		// }
	}

	/**
	 * 调用post方法上传文件
	 */
	private void postSubmit() {
		uid = userInfo.uid;
		companyName = et_companyName.getText().toString();
		brand = et_brand.getText().toString();
		salesArea = et_salesArea.getText().toString();
		contact = et_contact.getText().toString();
		duty = et_duty.getText().toString();
		email = et_email.getText().toString();
		QQ = et_QQ.getText().toString();
		//tel = et_tel.getText().toString();
		phone = et_phone.getText().toString();
		fax = et_fax.getText().toString();
		webSite = et_webSite.getText().toString();
		address = et_address.getText().toString();
		mainProduct = et_mainProduct.getText().toString();
		content = et_content.getText().toString();
		if (StringUtils.isBlank(companyName)) {
			ToastUtils.showShortToast(TzmApplyManufacturerActivity.this, "请输入公司名称");
			return;
		}
		if (tv_mainCategory.getVisibility() == View.VISIBLE) {
			ToastUtils.showShortToast(TzmApplyManufacturerActivity.this, "请选择主营类目");
			return;
		}
		if (tv_groups.getVisibility() == View.VISIBLE) {
			ToastUtils.showShortToast(TzmApplyManufacturerActivity.this, "请选择运营总人数");
			return;
		}
		if (StringUtils.isBlank(brand)) {
			ToastUtils.showShortToast(TzmApplyManufacturerActivity.this, "请输入自营品牌");
			return;
		}
		if (StringUtils.isBlank(salesArea)) {
			ToastUtils.showShortToast(TzmApplyManufacturerActivity.this, "请输入销售地区");
			return;
		}
		if (StringUtils.isBlank(contact)) {
			ToastUtils.showShortToast(TzmApplyManufacturerActivity.this, "请输入联系人");
			return;
		}
		if (StringUtils.isBlank(email)) {
			ToastUtils.showShortToast(TzmApplyManufacturerActivity.this, "请输入联系人邮箱");
			return;
		}
		if (!StringUtils.isEmailStr(email)) {
			ToastUtils.showShortToast(TzmApplyManufacturerActivity.this, "请检查一下邮箱");
			return;
		}
		if (StringUtils.isBlank(QQ)) {
			ToastUtils.showShortToast(TzmApplyManufacturerActivity.this, "请输入联系人QQ");
			return;
		}
		if (StringUtils.isBlank(phone)) {
			ToastUtils.showShortToast(TzmApplyManufacturerActivity.this, "请输入手机号码");
			return;
		}
		if (!StringUtils.isMobileNO(phone)) {
			ToastUtils.showShortToast(TzmApplyManufacturerActivity.this, "请检查一下手机号码");
			return;
		}
		if (StringUtils.isBlank(address)) {
			ToastUtils.showShortToast(TzmApplyManufacturerActivity.this, "请输入公司地址");
			return;
		}
		if (StringUtils.isBlank(mainProduct)) {
			ToastUtils.showShortToast(TzmApplyManufacturerActivity.this, "请输入主营产品");
			return;
		}
		if (StringUtils.isBlank(content)) {
			ToastUtils.showShortToast(TzmApplyManufacturerActivity.this, "请输入公司简介");
			return;
		}
		if (!cb_consent.isChecked()) {
			ToastUtils.showShortToast(TzmApplyManufacturerActivity.this, "请同意供货服务条款");
			return;
		}
		tzmApplyManufacturerApi = new TzmApplyManufacturerApi();

		RequestParams params = new RequestParams();

		params.addBodyParameter("uid", uid + "");
		params.addBodyParameter("companyName", companyName);
		params.addBodyParameter("brand", brand);
		params.addBodyParameter("salesArea", salesArea);
		params.addBodyParameter("contact", contact);
		params.addBodyParameter("duty", duty);
		params.addBodyParameter("QQ", QQ);
		// params.addBodyParameter("tel", tel);
		params.addBodyParameter("phone", phone);
		params.addBodyParameter("fax", fax);
		params.addBodyParameter("webSite", webSite);
		params.addBodyParameter("address", address);
		params.addBodyParameter("mainProduct", mainProduct);
		params.addBodyParameter("content", content);
		params.addBodyParameter("email", email);
		params.addBodyParameter("mainCategory", mainCategory + "");
		params.addBodyParameter("groups", group + "");
		params.addBodyParameter("phone", phone + "");

		if (StringUtils.isNotBlank(user3cImagePath)) {
			params.addBodyParameter("file1", new File(user3cImagePath));
		}
		if (StringUtils.isNotBlank(attestationImagePath)) {
			params.addBodyParameter("file2", new File(attestationImagePath));
		}

		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.POST, tzmApplyManufacturerApi.getUrl(),
				params, new RequestCallBack<String>() {

					@Override
					public void onStart() {
						ProgressDialogUtil.openProgressDialog(
								TzmApplyManufacturerActivity.this, "",
								"正在提交...");
					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						if (isUploading) {
							// testTextView.setText("upload: " + current + "/" +
							// total);
						} else {
							// testTextView.setText("reply: " + current + "/" +
							// total);
						}
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						// testTextView.setText("reply: " +
						// responseInfo.result);
						
						ProgressDialogUtil.closeProgressDialog();
						if (StringUtils.isNotBlank(responseInfo.result)) {
							try {
								JSONObject json = new JSONObject(responseInfo.result);								
								ToastUtils.showShortToast(
										TzmApplyManufacturerActivity.this,
										json.getString("error_msg"));
								if (json.getBoolean("success")) {
									userInfo.judgeType = 1;
									UserInfoUtils.updateUserInfo(userInfo);
//									finish();
									Intent intent= new Intent();
									setResult(Activity.RESULT_OK, intent);
									finish();
									
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						// testTextView.setText(error.getExceptionCode() + ":" +
						// msg);
						ToastUtils.showShortToast(
								TzmApplyManufacturerActivity.this,
								error.getExceptionCode() + ":" + msg);
						ProgressDialogUtil.closeProgressDialog();
					}
				});
	}
}
