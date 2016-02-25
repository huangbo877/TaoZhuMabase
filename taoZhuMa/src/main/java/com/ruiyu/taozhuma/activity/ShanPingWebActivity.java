/**
 * 加载闪屏web的 页面
 */
package com.ruiyu.taozhuma.activity;

import java.lang.reflect.Field;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.config.AppConfig;
import com.ruiyu.taozhuma.utils.LogUtil;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ZoomButtonsController;

/**
 * @author 林尧
 * 2015-12-17
 */
public class ShanPingWebActivity extends Activity {
	
	private String TAG="ShangPingWebActivity";
	@ViewInject(R.id.txt_head_title)
	private TextView txt_head_title;
	@ViewInject(R.id.btn_head_left)
	private Button btn_head_left;
	@ViewInject(R.id.wv_invite)
	private WebView wv_invite;
	private String link;
	private String title;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.invite_web_activity);
		ViewUtils.inject(this);
		LogUtil.Log(TAG, "onCreate");
		title = getIntent().getStringExtra("title");
		txt_head_title.setText(title);
		link = getIntent().getStringExtra("link");
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
		settings.setSupportZoom(true); //支持缩放
		settings.setBuiltInZoomControls(true); //启用内置缩放装置
	//    settings.setDisplayZoomControls(true);
	    settings.setJavaScriptEnabled(true);

	//    setZoomControlGone(wv_invite);  
	   
		
		
		
		wv_invite.loadUrl(link);
	/*	wv_invite.setWebViewClient(new WebViewClient() {
			// 当点击链接时,希望覆盖而不是打开新窗口
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
			//	share();
				return true; // 返回true,代表事件已处理,事件流到此终止
			}
		});*/
	}
	
	
}
