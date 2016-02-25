package com.ruiyu.taozhuma.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.api.ApiClient;
import com.ruiyu.taozhuma.api.ApiListener;
import com.ruiyu.taozhuma.api.InvitationCode23Api;
import com.ruiyu.taozhuma.model.BaseModel;
import com.ruiyu.taozhuma.model.InvitationCodeModel;

import com.ruiyu.taozhuma.test.ProductImageAdapter;
import com.ruiyu.taozhuma.test.ProductImageApi;
import com.ruiyu.taozhuma.test.ProductImageModel;
import com.ruiyu.taozhuma.utils.ProgressDialogUtil;
import com.ruiyu.taozhuma.widget.ListViewForScrollView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class InvitationCode3Activity extends Activity {

	// 接口调用
	private ApiClient client;
	private InvitationCode23Api InvitationCode23Api;
	private InvitationCodeModel InvitationCodeModel;
	int id;
	@ViewInject(R.id.tv_prompt2)
	private TextView tv_prompt2;

	@ViewInject(R.id.bt_share)
	private Button bt_share;
	@ViewInject(R.id.txt_head_title)
	private TextView txt_head_title;
	@ViewInject(R.id.btn_head_left)
	private Button btn_head_left;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.invitation_code3_activity);
		ViewUtils.inject(this);
		txt_head_title.setText("输入邀请码");
		id = getIntent().getExtras().getInt("id");
		client = new ApiClient(this);
		InvitationCode23Api = new InvitationCode23Api();
		load();
		btn_head_left.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		bt_share.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intentw1 = new Intent(new Intent(InvitationCode3Activity.this,
						InviteWebActivity.class));
				intentw1.putExtra("id", id);
				startActivity(intentw1);
				finish();
			}
		});
	}

	private void load() {
		// TODO Auto-generated method stub
		try {

			InvitationCode23Api.setUid(id);
		} catch (Exception e) {
			// TODO: handle exception
		}
		client.api(this.InvitationCode23Api, new ApiListener() {
			@Override
			public void onStart() {

			}

			@Override
			public void onComplete(String jsonStr) {
				ProgressDialogUtil.closeProgressDialog();
				Gson gson = new Gson();
				Type type = new TypeToken<BaseModel<InvitationCodeModel>>() {
				}.getType();
				BaseModel<InvitationCodeModel> base = gson.fromJson(jsonStr,
						type);
				try {

					InvitationCodeModel = base.result;
					SpannableString sp = new SpannableString(
							"      亲爱哒，你已经是淘竹马的用户啦。只有新用户注册才能使用邀请码兑现哦~快将您的邀请码"
									+ InvitationCodeModel.inviteCode
									+ "分享给其他的小伙伴来获取奖金吧！邀请越多，您的奖金越多哦~");
					sp.setSpan(
							new ForegroundColorSpan(getResources().getColor(
									R.color.base)), 47,
							47 + InvitationCodeModel.inviteCode.length(),
							Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					tv_prompt2.setText(sp);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}

			@Override
			public void onError(String error) {

			}

			@Override
			public void onException(Exception e) {

			}
		}, true);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}