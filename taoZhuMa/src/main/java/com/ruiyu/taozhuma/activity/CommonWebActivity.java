package com.ruiyu.taozhuma.activity;

import java.lang.reflect.Type;
import java.net.URLDecoder;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.config.AppConfig;
import com.ruiyu.taozhuma.model.ScenesWebUrlModel;
import com.ruiyu.taozhuma.utils.StringUtils;
import com.ruiyu.taozhuma.utils.ToastUtils;
import com.ruiyu.taozhuma.utils.UserInfoUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 
 * @author 林尧 2015-12-23 公用的 webview 页面
 */
public class CommonWebActivity extends Activity {

	@ViewInject(R.id.common_webView)
	private WebView webView;
	@ViewInject(R.id.common_progressBar)
	private ProgressBar progressBar;

	
	@ViewInject(R.id.txt_head_title)
	private TextView txt_head_title;

	@OnClick(R.id.btn_head_left)
	public void imbt_backClick(View v) {
		onBackPressed();
	}

	
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.common_web_activity);

		ViewUtils.inject(this);
		String id = getIntent().getStringExtra("id");
		final int activityID = getIntent().getIntExtra("activityID", 0);
		int type = getIntent().getIntExtra("type", 0);// 1-场景，2-套餐
		if (type == 1) {
			txt_head_title.setText("场景推荐");
			webView.loadUrl(AppConfig.WebViewHost + "/goSceneProductWeb.do?id="
					+ id);
			// Log.i("===============>url",
			// "http://testb2c.taozhuma.com/goSceneProductWeb.do?id=" + id);

		} else if (type == 2) {
			txt_head_title.setText("套餐搭配");
			webView.loadUrl(AppConfig.WebViewHost
					+ "/goPackageProductWeb.do?id=" + id);
			// Log.i("===============>url",
			// "http://testb2c.taozhuma.com/goSceneProductWeb.do?id=" + id);
		} else {
			ToastUtils
					.showShortToast(CommonWebActivity.this, "出错了,请稍后再试");

		}

		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true); // 支持js
		webSettings.setDefaultTextEncodingName("UTF-8");
		webView.setScrollBarStyle(0); // 滚动风格
		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				if (newProgress == 100) {
					progressBar.setVisibility(View.INVISIBLE);
				} else {
					progressBar.setProgress(newProgress);
				}
				super.onProgressChanged(view, newProgress);
			}
		});
		webView.setWebViewClient(new WebViewClient() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * android.webkit.WebViewClient#shouldOverrideUrlLoading(android
			 * .webkit.WebView, java.lang.String)
			 */
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				// 拦截超链接

				try {
					String output = URLDecoder.decode(url, "UTF-8");
					String spStr[] = output.split(".com/");
					Gson gson = new Gson();
					Type type = new TypeToken<ScenesWebUrlModel>() {
					}.getType();
					ScenesWebUrlModel base = gson.fromJson(spStr[1], type);

					// Log.i("==========json", base.toString());
					if (base.detail.equals("1")) {
						// 进入详情
						Intent intent = new Intent(
								CommonWebActivity.this,
								ProductDetailActivity.class);
						intent.putExtra("id", Integer.parseInt(base.productId));

						intent.putExtra("activityId", activityID);

						startActivity(intent);
					} else if (base.detail.equals("0")) {
						if (UserInfoUtils.isLogin()) {
							// 直接购买
							Intent intent = new Intent(
									CommonWebActivity.this,
									TzmSelctAddressActivity.class);
							intent.putExtra("pkgId",
									Integer.parseInt(base.sceneId));
							intent.putExtra("TAG", "addPkgPurchase");
							startActivity(intent);
						} else {
							Intent intent = new Intent(new Intent(
									CommonWebActivity.this,
									TzmLoginRegisterActivity.class));
							intent.putExtra("type", 0);
							startActivityForResult(intent, AppConfig.Login);
						}

					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return true;
				// return super.shouldOverrideUrlLoading(view, url);
			}
		});

	}

}
