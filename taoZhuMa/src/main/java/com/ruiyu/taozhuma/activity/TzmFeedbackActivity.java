package com.ruiyu.taozhuma.activity;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.api.ApiClient;
import com.ruiyu.taozhuma.api.ApiListener;
import com.ruiyu.taozhuma.api.FeedbackApi;
import com.ruiyu.taozhuma.utils.LogUtil;
import com.ruiyu.taozhuma.utils.ProgressDialogUtil;
import com.ruiyu.taozhuma.utils.StringUtils;
import com.ruiyu.taozhuma.utils.ToastUtils;
import com.ruiyu.taozhuma.utils.UserInfoUtils;

@SuppressLint("ResourceAsColor")
public class TzmFeedbackActivity extends Activity {
	
	private TextView txt_head_title;
	private ImageView img_suggest, img_complain;
	private Button btn_head_left, btn_submit;
	private EditText content_text, email_text, phone_text;
	private String content;
	private String email;
	private String phone;
	private ApiClient client;
	private FeedbackApi feedbackApi;
	int type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tzm_feedback_activity);
		type = 2;
		initView();
	}

	private void initView() {

		txt_head_title = (TextView) this.findViewById(R.id.txt_head_title);
		txt_head_title.setText("投诉建议");
		//关于我们
		btn_head_left = (Button) this.findViewById(R.id.btn_head_left);
		//我的建议 tag
		img_suggest = (ImageView) this.findViewById(R.id.img_suggest);
		img_suggest.setOnClickListener(clickListener);
		//我要投诉 tag
		img_complain = (ImageView) this.findViewById(R.id.img_complain);
		img_complain.setOnClickListener(clickListener);
		
		btn_head_left.setOnClickListener(clickListener);
		
		//反馈的内容
		content_text = (EditText) this.findViewById(R.id.feedback_ed_content);
		//提交按钮
		btn_submit = (Button) this.findViewById(R.id.feedback_btn_submit);
		btn_submit.setOnClickListener(clickListener);
		
		email_text = (EditText) this.findViewById(R.id.et_email);
		phone_text = (EditText) this.findViewById(R.id.et_phone);
	}

	View.OnClickListener clickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_head_left:
				onBackPressed();
				break;
			case R.id.img_suggest:
				setTabSelection(0);
				break;
			case R.id.img_complain:
				setTabSelection(1);
				break;
			case R.id.feedback_btn_submit:
				SubmitData();

				break;

			}
		}

		private void SubmitData() {
			content = content_text.getText().toString();
			email = email_text.getText().toString();
			phone = phone_text.getText().toString();
			if (content.equals("")) {
				ToastUtils.showShortToast(TzmFeedbackActivity.this, "请填入您要提交的内容！");
			} else if (email.equals("") || phone.equals("")) {
				ToastUtils.showShortToast(TzmFeedbackActivity.this, "邮箱和联系电话都不能为空哦！");
			} else if (!StringUtils.isEmailStr(email)
					|| !StringUtils.isMobileNO(phone)) {
				ToastUtils.showShortToast(TzmFeedbackActivity.this, "再认真检查一下邮箱和联系电话吧！");
			} else {
				client = new ApiClient(TzmFeedbackActivity.this);
				feedbackApi = new FeedbackApi();
				feedbackApi.setContent(content);
				if (UserInfoUtils.isLogin()) {
					feedbackApi.setUid(UserInfoUtils.getUserInfo().uid);
				}
				feedbackApi.setType(type);
				feedbackApi.setPhone(phone);
				feedbackApi.setEmail(email);
				client.api(feedbackApi, new ApiListener() {

					@Override
					public void onStart() {
						ProgressDialogUtil.openProgressDialog(
								TzmFeedbackActivity.this, "", "正在提交...");
					}

					@Override
					public void onException(Exception e) {
						ProgressDialogUtil.closeProgressDialog();
						LogUtil.ErrorLog(e);
					}

					@Override
					public void onError(String error) {
						ProgressDialogUtil.closeProgressDialog();
						ToastUtils.showShortToast(TzmFeedbackActivity.this, error);
					}

					@Override
					public void onComplete(String jsonStr) {
						ProgressDialogUtil.closeProgressDialog();
						if (StringUtils.isNotBlank(jsonStr)) {
							try {
								JSONObject json = new JSONObject(jsonStr);
								ToastUtils.showShortToast(TzmFeedbackActivity.this,
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
	};
    /**
     * 选择我的建议 / 我要投诉两个选项
     * @param index
     */
	private void setTabSelection(int index) {
		switch (index) {
		case 0:
			type = 2;
			img_suggest.setImageResource(R.drawable.suggest1);
			img_complain.setImageResource(R.drawable.complain2);
			break;
		case 1:
			type = 1;
			img_suggest.setImageResource(R.drawable.suggest2);
			img_complain.setImageResource(R.drawable.complain1);
			break;
		}
	}

}
