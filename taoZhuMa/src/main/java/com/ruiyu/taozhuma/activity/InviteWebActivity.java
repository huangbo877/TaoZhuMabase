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
import com.ruiyu.taozhuma.config.AppConfig;
import com.ruiyu.taozhuma.model.BaseModel;
import com.ruiyu.taozhuma.model.InvitationCodeModel;

import com.ruiyu.taozhuma.test.ProductImageAdapter;
import com.ruiyu.taozhuma.test.ProductImageApi;
import com.ruiyu.taozhuma.test.ProductImageModel;
import com.ruiyu.taozhuma.utils.ProgressDialogUtil;
import com.ruiyu.taozhuma.widget.ListViewForScrollView;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class InviteWebActivity extends Activity {

	// 接口调用

	int id;
	@ViewInject(R.id.tv_prompt2)
	private TextView tv_prompt2;
	@ViewInject(R.id.wv_invite)
	private WebView wv_invite;
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
		setContentView(R.layout.invite_web_activity);
		ViewUtils.inject(this);

		txt_head_title.setText("邀请好友");
		id = getIntent().getExtras().getInt("id");
		load();
		btn_head_left.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	private void load() {
		// TODO Auto-generated method stub
		WebSettings settings = wv_invite.getSettings();
		settings.setSupportZoom(true); // 支持缩放
		settings.setBuiltInZoomControls(true); // 启用内置缩放装置
		settings.setJavaScriptEnabled(true);

		wv_invite.loadUrl(AppConfig.HOST + "/goInviteFriend.do?uid=" + id);
		wv_invite.setWebViewClient(new WebViewClient() {
			// 当点击链接时,希望覆盖而不是打开新窗口
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				share();
				return true; // 返回true,代表事件已处理,事件流到此终止
			}
		});
	}
	/**
	 * 分享
	 */
	private final UMSocialService mController = UMServiceFactory
			.getUMSocialService("com.umeng.share");

	protected void share() {
		// 配置SSO
		mController.getConfig().setSsoHandler(new SinaSsoHandler());
		addWXPlatform();
		Resources resource = getResources();
		Bitmap bitmap=BitmapFactory.decodeResource(resource, R.drawable.icon180);
		UMImage urlImage = new UMImage(InviteWebActivity.this,
				bitmap);
		// 设置微信内容
		WeiXinShareContent weixinContent = new WeiXinShareContent();
		weixinContent.setShareContent("现金使用无门槛！加入邀请计划，大把现金赚不停。玩具特卖，尽在淘竹马~");
		weixinContent.setTitle("淘竹马大批现金来袭送不停！    ");
		weixinContent
				.setTargetUrl(AppConfig.HOST+"/goShare.do?uid="
						+ id);
		weixinContent.setShareMedia(urlImage);
		mController.setShareMedia(weixinContent);
		// 新浪内容
		SinaShareContent sinaContent = new SinaShareContent();
		sinaContent.setTitle("淘竹马大批现金来袭送不停！  ");
		sinaContent.setShareContent("现金使用无门槛！加入邀请计划，大把现金赚不停。玩具特卖，尽在淘竹马~");
		sinaContent.setShareImage(urlImage);
		 sinaContent
		 .setTargetUrl(AppConfig.HOST+"/goShare.do?uid="
					+ id);
		mController.setShareMedia(sinaContent);
		// 设置微信朋友圈分享内容
		CircleShareContent circleMedia = new CircleShareContent();
		circleMedia.setShareContent("现金使用无门槛！加入邀请计划，大把现金赚不停。玩具特卖，尽在淘竹马~");
		// 设置朋友圈title
		circleMedia.setTitle("淘竹马大批现金来袭送不停！ ");
		circleMedia.setShareImage(urlImage);
		circleMedia
				.setTargetUrl(AppConfig.HOST+"/goShare.do?uid="
						+ id);
		mController.setShareMedia(circleMedia);

		mController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN,
				SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA);
		mController.openShare(InviteWebActivity.this, false);
	}

	/**
	 * @功能描述 : 添加微信平台分享
	 * @return
	 */
	private void addWXPlatform() {
		// 注意：在微信授权的时候，必须传递appSecret
		// wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
		String appId = "wx88fa4c9662539a8f";
		String appSecret = "bf5cb0a3b32076886049d61eac6cfe06";
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(InviteWebActivity.this,
				appId, appSecret);
		wxHandler.addToSocialSDK();

		// 支持微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(
				InviteWebActivity.this, appId, appSecret);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();

	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}