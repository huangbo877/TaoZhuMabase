package com.ruiyu.taozhuma.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ruiyu.taozhuma.PictureUtil;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.SelectPicActivity;
import com.ruiyu.taozhuma.R.id;
import com.ruiyu.taozhuma.R.layout;
import com.ruiyu.taozhuma.R.string;
import com.ruiyu.taozhuma.adapter.TestEvaluationAdapter;
import com.ruiyu.taozhuma.adapter.TzmEvaluationAdapter;
import com.ruiyu.taozhuma.api.ApiClient;
import com.ruiyu.taozhuma.api.ApiListener;
import com.ruiyu.taozhuma.api.TzmCommentApi;
import com.ruiyu.taozhuma.api.TzmEvaluationApi;
import com.ruiyu.taozhuma.base.BaseApplication;
import com.ruiyu.taozhuma.model.BaseModel;
import com.ruiyu.taozhuma.model.TzmEvaluationModel;
import com.ruiyu.taozhuma.model.UserModel;
import com.ruiyu.taozhuma.utils.LogUtil;
import com.ruiyu.taozhuma.utils.ProgressDialogUtil;
import com.ruiyu.taozhuma.utils.StringUtils;
import com.ruiyu.taozhuma.utils.ToastUtils;
import com.ruiyu.taozhuma.widget.FlexibleRatingBar;
import com.ruiyu.taozhuma.widget.GridViewForScrollView;

public class TzmNewCommentsActivity extends Activity {
	private TextView txt_head_title;
	private Button btn_head_left;
	private Integer orderId;
	private UserModel userInfo;
	private Button comment_btn_submit;

	private TzmEvaluationApi tzmEvaluationApi;
	private TzmCommentApi tzmCommentApi;
	private ApiClient apiClient, apiClient1;
	private ArrayList<TzmEvaluationModel> tzmEvaluationModel;
	private TestEvaluationAdapter tzmEvaluationAdapter;
	static Context context;
	private GridViewForScrollView gridView;
	FlexibleRatingBar rabarServer, rabarServer2;
	float currentRating;
	float currentRating2;
	float currentRating3;

	// int mark;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		orderId = getIntent().getIntExtra("orderId", 0);
		userInfo = BaseApplication.getInstance().getLoginUser();
		initView();

		context = this;
		apiClient = new ApiClient(this);
		tzmEvaluationApi = new TzmEvaluationApi();
		tzmEvaluationModel = new ArrayList<TzmEvaluationModel>();

		apiClient1 = new ApiClient(this);
		tzmCommentApi = new TzmCommentApi();
		loadData();

	}

	private void loadData() {
		tzmEvaluationApi.setUid(userInfo.uid);
		tzmEvaluationApi.setOid(orderId);
		apiClient.api(tzmEvaluationApi, new ApiListener() {
			@Override
			public void onStart() {
			}

			@Override
			public void onError(String error) {
				ToastUtils.showShortToast(TzmNewCommentsActivity.this,
						R.string.msg_list_null);
			}

			@Override
			public void onException(Exception e) {
				LogUtil.ErrorLog(e);
			}

			@Override
			public void onComplete(String jsonStr) {
				Gson gson = new Gson();
				Type type = new TypeToken<BaseModel<ArrayList<TzmEvaluationModel>>>() {
				}.getType();
				BaseModel<ArrayList<TzmEvaluationModel>> base = gson.fromJson(
						jsonStr, type);

				tzmEvaluationModel.clear();
				if (base.result != null && base.result.size() > 0) {
					tzmEvaluationModel = base.result;
				} else {
					ToastUtils.showShortToast(TzmNewCommentsActivity.this,
							base.error_msg);
					onBackPressed();
				}

				initCommentList();
			}
		}, true);
	}

	private void initView() {
		txt_head_title = (TextView) findViewById(R.id.txt_head_title);
		txt_head_title.setText("订单评价");
		btn_head_left = (Button) findViewById(R.id.btn_head_left);
		btn_head_left.setOnClickListener(clickListener);
		gridView = (GridViewForScrollView) findViewById(R.id.gv_comment);
		comment_btn_submit = (Button) findViewById(R.id.comment_btn_submit);
		comment_btn_submit.setOnClickListener(clickListener);
		rabarServer = (FlexibleRatingBar) findViewById(R.id.rabarServer);
		rabarServer2 = (FlexibleRatingBar) findViewById(R.id.rabarServer2);

	}

	private void initCommentList() {
		if (this.tzmEvaluationModel != null) {
			tzmEvaluationAdapter = new TestEvaluationAdapter(
					TzmNewCommentsActivity.this, this.tzmEvaluationModel);
			gridView.setAdapter(tzmEvaluationAdapter);// 调用Adapter的getView()绘制item
			gridView.setSelection(0);
		}
	}

	View.OnClickListener clickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_head_left:
				onBackPressed();
				break;
			case R.id.comment_btn_submit:
				// 提交
				// System.out.println("获取评分条"+rabarServer.getRating());
				SubmitContent();
				break;
			}

		}

	};

	private void SubmitContent() {
		comment_btn_submit.setClickable(false);
		String content = "";
		boolean key = false;
		for (int i = 0; i < gridView.getChildCount(); i++) {
			LinearLayout layout = (LinearLayout) gridView.getChildAt(i);// 获得子item的layout
			EditText comment_content = (EditText) layout
					.findViewById(R.id.comment_content);// 从layout中获得控件,根据其id
			FlexibleRatingBar rabarServer3 = (FlexibleRatingBar) layout
					.findViewById(R.id.rabarServer3);
			currentRating3 = rabarServer3.getRating();// listview评分
			// Toast.makeText(
			// TestActivity.this,
			// currentRating + "++++" + currentRating2 + "++++"
			// + currentRating3, Toast.LENGTH_SHORT).show();
			// if (StringUtils.isNotEmpty(comment_content.getText().toString()))
			// {
			//
			key = true;
			if (StringUtils.isEmpty(content)) {
				content = comment_content.getTag() + ":"
						+ comment_content.getText().toString() + ":"
						+ (int) currentRating3;
			} else {
				content = content + "," + comment_content.getTag() + ":"
						+ comment_content.getText().toString() + ":"
						+ (int) currentRating3;
			}

			// } else {
			// comment_btn_submit.setClickable(true);
			// Toast.makeText(this, "请至少填一条评价...", Toast.LENGTH_SHORT).show();

			// }
		}
		// Toast.makeText(
		// TzmNewCommentsActivity.this,
		// content, Toast.LENGTH_SHORT).show();
		if (key) {
			currentRating = rabarServer.getRating();
			currentRating2 = rabarServer2.getRating();
			tzmCommentApi.setUid(userInfo.uid);
			tzmCommentApi.setOid(orderId);
			tzmCommentApi.setOrderIds(content);
			tzmCommentApi.setscoreService((int) currentRating);
			tzmCommentApi.setscoreSspeed((int) currentRating2);
			apiClient1.api(tzmCommentApi, new ApiListener() {
				@Override
				public void onStart() {
					ProgressDialogUtil.openProgressDialog(
							TzmNewCommentsActivity.this, "", "正在提交...");
				}

				@Override
				public void onError(String error) {
					comment_btn_submit.setClickable(true);
					ProgressDialogUtil.closeProgressDialog();
					LogUtil.Log(error);
					ToastUtils.showShortToast(TzmNewCommentsActivity.this, error);
				}

				@Override
				public void onException(Exception e) {
					comment_btn_submit.setClickable(true);
					ProgressDialogUtil.closeProgressDialog();
					LogUtil.ErrorLog(e);
				}

				@Override
				public void onComplete(String jsonStr) {
					comment_btn_submit.setClickable(true);
					ProgressDialogUtil.closeProgressDialog();
					if (StringUtils.isNotBlank(jsonStr)) {
						try {
							JSONObject json = new JSONObject(jsonStr);
							ToastUtils.showShortToast(TzmNewCommentsActivity.this,
									json.getString("error_msg"));
							if (json.getBoolean("success")) {
								onBackPressed();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				}
			}, true);
		}

	}

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
				// FileItem f = new FileItem(picPath);
				// switch (requestCode) {
				// case R.id.iv_logo:
				// logoPatch = picPath;
				// //addApi.image.put("logo", f);
				// break;
				// case R.id.iv_img1:
				// img1Patch = picPath;
				// //addApi.image.put("img1", f);
				// break;
				// case R.id.iv_img2:
				// img2Patch = picPath;
				// //addApi.image.put("img2", f);
				// break;
				// case R.id.iv_img3:
				// img3Patch = picPath;
				// //addApi.image.put("img3", f);
				// break;
				//
				// }

			} else {
				LogUtil.Log(requestCode + "最的图片=" + picPath);
				img.setVisibility(View.GONE);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
