package com.ruiyu.taozhuma.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ruiyu.taozhuma.R;
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
import com.ruiyu.taozhuma.widget.GridViewForScrollView;

public class TzmCommentActivity extends Activity {
	private TextView txt_head_title;
	private Button btn_head_left;
	private Integer orderId;
	private UserModel userInfo;
	private Button comment_btn_submit;
	private TzmEvaluationApi tzmEvaluationApi;
	private TzmCommentApi tzmCommentApi;
	private ApiClient apiClient, apiClient1;
	private ArrayList<TzmEvaluationModel> tzmEvaluationModel;
	private TzmEvaluationAdapter tzmEvaluationAdapter;

	private GridViewForScrollView gridView;

	// int mark;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tzm_comment_activity);
		orderId = getIntent().getIntExtra("orderId", 0);
		// mark = getIntent().getIntExtra("mark", 0);
		userInfo = BaseApplication.getInstance().getLoginUser();
		initView();

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
				ToastUtils.showShortToast(TzmCommentActivity.this,
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
				if (base.result != null) {
					tzmEvaluationModel = base.result;
				} else {
					ToastUtils.showShortToast(TzmCommentActivity.this,
							R.string.msg_list_null);
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
	}

	private void initCommentList() {
		if (this.tzmEvaluationModel != null) {
			tzmEvaluationAdapter = new TzmEvaluationAdapter(
					TzmCommentActivity.this, this.tzmEvaluationModel);
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
			if (StringUtils.isNotEmpty(comment_content.getText().toString())) {
				key = true;
				if (StringUtils.isEmpty(content)) {
					content = comment_content.getTag() + ":"
							+ comment_content.getText().toString();
				} else {
					content = content + "," + comment_content.getTag() + ":"
							+ comment_content.getText().toString();
				}
				break;
			} else {
				comment_btn_submit.setClickable(true);
				ToastUtils.showShortToast(this, "请至少填一条评价...");
				key = false;
			}
		}
		if (key) {
			tzmCommentApi.setUid(userInfo.uid);
			tzmCommentApi.setOid(orderId);
			tzmCommentApi.setOrderIds(content);
			apiClient1.api(tzmCommentApi, new ApiListener() {
				@Override
				public void onStart() {
					ProgressDialogUtil.openProgressDialog(
							TzmCommentActivity.this, "", "正在提交...");
				}

				@Override
				public void onError(String error) {
					comment_btn_submit.setClickable(true);
					ProgressDialogUtil.closeProgressDialog();
					ToastUtils.showShortToast(TzmCommentActivity.this, error);
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
							ToastUtils.showShortToast(TzmCommentActivity.this,
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
}
