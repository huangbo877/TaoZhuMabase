package com.ruiyu.taozhuma.activity;

import java.io.File;
import java.util.List;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.api.ApiClient;
import com.ruiyu.taozhuma.api.ApiListener;
import com.ruiyu.taozhuma.api.TzmApplyRefundApi;
import com.ruiyu.taozhuma.base.BaseApplication;
import com.ruiyu.taozhuma.base.xUtilsImageLoader;
import com.ruiyu.taozhuma.model.TzmOrderDetailModel;
import com.ruiyu.taozhuma.model.TzmOrderDetailModel.Products;
import com.ruiyu.taozhuma.model.UserModel;
import com.ruiyu.taozhuma.utils.LogUtil;
import com.ruiyu.taozhuma.utils.PictureUtil;
import com.ruiyu.taozhuma.utils.ProgressDialogUtil;
import com.ruiyu.taozhuma.utils.StringUtils;
import com.ruiyu.taozhuma.utils.ToastUtils;
import com.ruiyu.taozhuma.utils.UserInfoUtils;

public class TzmApplyRefundActivity extends Activity {
	private Button btn_head_left;
	private TextView txt_head_title;
	private int index, status;

	private List<Products> product;
	private TzmOrderDetailModel model;
	private LinearLayout ll_order_detail;

	private TextView tv_type1, tv_type2;
	private View view1, view2;
	private int type = 2;

	private LayoutInflater layoutInflater;
	private Spinner sp_refundType;
	private TextView tv_refundType;
	private ArrayAdapter<String> adapter;
	private String[] refundTypes = { "不喜欢", "质量不好", "尺码不对", "颜色不对", "其他" };
	private int refundType;
	private Button apply_refund_submit;
	private EditText et_num, et_refundReason;
	private String str_refundReason;
	private int num;
	private LinearLayout rl_image;
	private String imagePath;
	private UserModel userInfo;
	private TzmApplyRefundApi applyRefundApi;
	private ApiClient apiClient;

	private String str1 = "退货", str2 = "退款";

	xUtilsImageLoader imageLoader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.apply_refund_activity);
		index = Integer.parseInt(getIntent().getStringExtra("index"));
		model = (TzmOrderDetailModel) getIntent().getSerializableExtra("model");
		status = getIntent().getIntExtra("status", 0);
		userInfo = BaseApplication.getInstance().getLoginUser();
		imageLoader = new xUtilsImageLoader(this);
		product = model.product;
		initView();

	}

	private void initView() {
		btn_head_left = (Button) findViewById(R.id.btn_head_left);
		btn_head_left.setOnClickListener(clickListener);
		txt_head_title = (TextView) findViewById(R.id.txt_head_title);

		if (status == 2 || status == 3) {
			txt_head_title.setText("申请退货/退款");
			findViewById(R.id.ll_title1).setVisibility(View.VISIBLE);
			findViewById(R.id.ll_title2).setVisibility(View.VISIBLE);

			tv_type1 = (TextView) findViewById(R.id.tv_type1);
			tv_type2 = (TextView) findViewById(R.id.tv_type2);
			view1 = findViewById(R.id.view1);
			view2 = findViewById(R.id.view2);
			tv_type1.setOnClickListener(clickListener);
			tv_type2.setOnClickListener(clickListener);
		} else if (status == 4) {
			txt_head_title.setText("申请售后");
			type = 3;
			findViewById(R.id.ll_title1).setVisibility(View.GONE);
			findViewById(R.id.ll_title2).setVisibility(View.GONE);
			str1 = "售后";
			str2 = "售后";
			((TextView) findViewById(R.id.tv1)).setText("售后原因");
			((TextView) findViewById(R.id.tv2)).setText("售后数量");
			((TextView) findViewById(R.id.tv3)).setText("售后说明");

		}
		if (status == 2) {
			txt_head_title.setText("退款");
			findViewById(R.id.ll_title1).setVisibility(View.VISIBLE);
			findViewById(R.id.ll_title2).setVisibility(View.VISIBLE);
			type = 2;
			findViewById(R.id.ll_title1).setVisibility(View.GONE);
			findViewById(R.id.ll_title2).setVisibility(View.GONE);
		
		}
		tv_refundType = (TextView) findViewById(R.id.tv_refundType);
		sp_refundType = (Spinner) findViewById(R.id.sp_refundType);
		tv_refundType.setOnClickListener(clickListener);
		adapter = new ArrayAdapter<String>(TzmApplyRefundActivity.this,
				android.R.layout.simple_spinner_item, refundTypes);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_refundType.setAdapter(adapter);
		sp_refundType.setOnItemSelectedListener(new SpinnerSelectedListener());
		ll_order_detail = (LinearLayout) findViewById(R.id.ll_order_detail);
		layoutInflater = LayoutInflater.from(TzmApplyRefundActivity.this);
		et_num = (EditText) findViewById(R.id.et_num);
		et_refundReason = (EditText) findViewById(R.id.et_refundReason);
		if (status == 4) {
			tv_refundType.setText("请选择售后原因");
			et_num.setHint("请填写售后数量");

		}
		rl_image = (LinearLayout) findViewById(R.id.rl_image);
		rl_image.setOnClickListener(clickListener);
		apply_refund_submit = (Button) findViewById(R.id.apply_refund_submit);
		apply_refund_submit.setOnClickListener(clickListener);
		if (model.orderStatus == 2) {
			setTabSelection(1);
		}
		ordersDetail();

	}

	@SuppressLint("InflateParams")
	private void ordersDetail() {
		View convertView = (View) layoutInflater.inflate(
				R.layout.tzm_order_detail, null);
		TextView tv_productName = (TextView) convertView
				.findViewById(R.id.tv_productName);
		ImageView iv_productImage = (ImageView) convertView
				.findViewById(R.id.iv_productImage);
		TextView tv_price = (TextView) convertView.findViewById(R.id.tv_price);
		TextView tv_num = (TextView) convertView.findViewById(R.id.tv_num);

		if (!StringUtils.isEmpty(product.get(index).productImage)) {
			imageLoader.display(iv_productImage,
					product.get(index).productImage);
		}
		tv_productName.setText(product.get(index).productName);
		tv_price.setText("¥ " + product.get(index).price);
		tv_num.setText(product.get(index).productNumber + "件");
		ll_order_detail.addView(convertView);

	}

	View.OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_head_left:
				Intent mIntent = new Intent();
				TzmApplyRefundActivity.this.setResult(1, mIntent);
				finish();
				break;
			case R.id.tv_type1:
				setTabSelection(0);
				break;
			case R.id.tv_type2:
				setTabSelection(1);
				break;
			case R.id.tv_refundType:
				sp_refundType.performClick();
				sp_refundType.setVisibility(View.VISIBLE);
				tv_refundType.setVisibility(View.GONE);
				break;
			case R.id.apply_refund_submit:
				postSubmit();
				break;
			case R.id.rl_image:
				selectPic(R.id.img_image);
				break;

			}

		}
	};

	private void submitData() {
		str_refundReason = et_refundReason.getText().toString();
		if (tv_refundType.getVisibility() == View.VISIBLE) {
			ToastUtils.showShortToast(TzmApplyRefundActivity.this, "请选择" + str2 + "原因");
			return;
		}
		if (StringUtils.isEmpty(et_num.getText().toString())) {
			ToastUtils.showShortToast(TzmApplyRefundActivity.this, "请填写" + str1 + "数量");
			return;
		}
		num = Integer.parseInt(et_num.getText().toString());
		if (num > product.get(index).productNumber) {
			ToastUtils.showShortToast(TzmApplyRefundActivity.this, str1 + "数量不能超过购买的数量");
			return;
		}
		applyRefundApi = new TzmApplyRefundApi();
		apiClient = new ApiClient(this);
		applyRefundApi.setUid(userInfo.uid);
		applyRefundApi.setOid(product.get(index).orderDetailId);
		applyRefundApi.setSid(model.shopId);
		applyRefundApi.setType(type);
		applyRefundApi.setRefundType(refundType);
		applyRefundApi.setNum(num);
		applyRefundApi.setImage(product.get(index).productImage);
		applyRefundApi.setRefundReason(str_refundReason);
		apiClient.api(applyRefundApi, new ApiListener() {

			@Override
			public void onStart() {
				ProgressDialogUtil.openProgressDialog(
						TzmApplyRefundActivity.this, "", "正在提交...");
			}

			@Override
			public void onError(String error) {
				ProgressDialogUtil.closeProgressDialog();
				ToastUtils.showShortToast(TzmApplyRefundActivity.this, error);
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
						JSONObject json = new JSONObject(jsonStr);
						ToastUtils.showShortToast(TzmApplyRefundActivity.this,
								json.getString("error_msg"));
						if (json.getBoolean("success")) {
							Intent mIntent = new Intent();
							TzmApplyRefundActivity.this.setResult(0, mIntent);
							finish();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}, true);
	}

	private void setTabSelection(int index) {
		switch (index) {
		case 0:
			if (model.orderStatus == 2) {
				ToastUtils.showShortToast(TzmApplyRefundActivity.this, "商家尚未发货");
			} else {
				type = 2;// 退货
				view1.setBackgroundColor(Color.parseColor("#df4450"));
				view2.setBackgroundColor(Color.parseColor("#ffffff"));
			}
			break;
		case 1:
			type = 1;// 退款
			view1.setBackgroundColor(Color.parseColor("#ffffff"));
			view2.setBackgroundColor(Color.parseColor("#df4450"));
			break;
		}
	}

	class SpinnerSelectedListener implements OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			refundType = arg2;
		}

		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}

	// @Override
	// public void onBackPressed() {
	// super.onBackPressed();
	// Intent mIntent = new Intent();
	// TzmApplyRefundActivity.this.setResult(0, mIntent);
	// }
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
				imagePath = picPath;
				// FileItem f = new FileItem(picPath);
				// addCommentApi.image.put(requestCode + "", f);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void postSubmit() {
		str_refundReason = et_refundReason.getText().toString();
		if (tv_refundType.getVisibility() == View.VISIBLE) {
			ToastUtils.showShortToast(TzmApplyRefundActivity.this, "请选择" + str2 + "原因");
			return;
		}
		if (StringUtils.isEmpty(et_num.getText().toString())) {
			ToastUtils.showShortToast(TzmApplyRefundActivity.this, "请填写" + str1 + "数量");
			return;
		}
		num = Integer.parseInt(et_num.getText().toString());
		if (num > product.get(index).productNumber) {
			ToastUtils.showShortToast(TzmApplyRefundActivity.this, str1 + "数量不能超过购买的数量");
			return;
		}
		if (num == 0) {
			ToastUtils.showShortToast(TzmApplyRefundActivity.this,
					str1 + "数量不能为零哦亲，客服也是很辛苦的");
			return;
		}

		applyRefundApi = new TzmApplyRefundApi();

		RequestParams params = new RequestParams();
		params.setHeader("User-Agent", "XieYeAndroidSDK");
		params.addBodyParameter("uid", userInfo.uid + "");
		params.addBodyParameter("oid", product.get(index).orderDetailId + "");
		params.addBodyParameter("sid", model.shopId + "");
		params.addBodyParameter("type", type + "");
		params.addBodyParameter("refundType", refundType + "");
		params.addBodyParameter("num", num + "");
		if (StringUtils.isNotBlank(imagePath)) {
			params.addBodyParameter("file", new File(imagePath));
		}
		params.addBodyParameter("refundReason", str_refundReason);

		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.POST, applyRefundApi.getUrl(), params,
				new RequestCallBack<String>() {

					@Override
					public void onStart() {
						ProgressDialogUtil.openProgressDialog(
								TzmApplyRefundActivity.this, "", "正在提交...");
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
								JSONObject json = new JSONObject(
										responseInfo.result);
								ToastUtils.showShortToast(
										TzmApplyRefundActivity.this,
										json.getString("error_msg"));
								if (json.getBoolean("success")) {
									userInfo.judgeType = 1;
									UserInfoUtils.updateUserInfo(userInfo);
									// finish();
									Intent mIntent = new Intent();
									TzmApplyRefundActivity.this.setResult(0,
											mIntent);
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
						ToastUtils.showShortToast(TzmApplyRefundActivity.this,
								error.getExceptionCode() + ":" + msg);
						ProgressDialogUtil.closeProgressDialog();
					}
				});
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Intent mIntent = new Intent();
			TzmApplyRefundActivity.this.setResult(1, mIntent);
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
