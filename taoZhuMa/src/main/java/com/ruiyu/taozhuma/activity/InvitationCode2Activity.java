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
import com.ruiyu.taozhuma.api.InvitationCode1Api;
import com.ruiyu.taozhuma.api.InvitationCode23Api;
import com.ruiyu.taozhuma.model.BaseModel;
import com.ruiyu.taozhuma.model.InvitationCodeModel;

import com.ruiyu.taozhuma.test.ProductImageAdapter;
import com.ruiyu.taozhuma.test.ProductImageApi;
import com.ruiyu.taozhuma.test.ProductImageModel;
import com.ruiyu.taozhuma.utils.ProgressDialogUtil;
import com.ruiyu.taozhuma.utils.ToastUtils;
import com.ruiyu.taozhuma.widget.CircularImage;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class InvitationCode2Activity extends Activity {

	// 接口调用
	private ApiClient client;
	private InvitationCode23Api InvitationCode23Api;
	private InvitationCodeModel InvitationCodeModel;
	int id;
	private BitmapUtils imageLoader;
	@ViewInject(R.id.iv_photo)
	private CircularImage iv_photo;
	@ViewInject(R.id.tv_prompt)
	private TextView tv_prompt;
	@ViewInject(R.id.tv_name)
	private TextView tv_name;
	@ViewInject(R.id.tv_prompt2)
	private TextView tv_prompt2;
	@ViewInject(R.id.bt_share)
	private Button bt_share;
	@ViewInject(R.id.bt_shop)
	private Button bt_shop;
	@ViewInject(R.id.txt_head_title)
	private TextView txt_head_title;
	@ViewInject(R.id.btn_head_left)
	private Button btn_head_left;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.invitation_code2_activity);
		ViewUtils.inject(this);
		txt_head_title.setText("输入邀请码");
		id = getIntent().getExtras().getInt("id");
		imageLoader = new BitmapUtils(this);
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
				Intent intentw1 = new Intent(new Intent(
						InvitationCode2Activity.this, InviteWebActivity.class));
				intentw1.putExtra("id", id);
				startActivity(intentw1);
				finish();
			}
		});
		bt_shop.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(InvitationCode2Activity.this,
						TzmActivity.class);
				intent.putExtra("type", 7);
				startActivity(intent);
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
					imageLoader.display(iv_photo,
							InvitationCodeModel.invitorImg);
					tv_name.setText(InvitationCodeModel.invitor);
					tv_prompt.setText(InvitationCodeModel.invitor
							+ "和淘竹马一起");
					SpannableString sp = new SpannableString("      你已接受"
							+ InvitationCodeModel.invitor
							+ "的邀请成为淘竹马的一员，你现在邀请好友也能赚钱哦~快将你的邀请码"
							+ InvitationCodeModel.inviteCode + "分享给其他小伙伴，钱不怕多！");
					sp.setSpan(
							new ForegroundColorSpan(getResources().getColor(
									R.color.base)),
							InvitationCodeModel.invitor.length() + 42,
							InvitationCodeModel.invitor.length() + 42
									+ InvitationCodeModel.inviteCode.length(),
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
