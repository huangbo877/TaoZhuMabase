package com.ruiyu.taozhuma.activity;

import java.lang.reflect.Type;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.api.ApiClient;
import com.ruiyu.taozhuma.api.ApiListener;
import com.ruiyu.taozhuma.api.TzmInfoHelpApi;
import com.ruiyu.taozhuma.model.BaseModel;
import com.ruiyu.taozhuma.model.TzmInfoDetailModel;
import com.ruiyu.taozhuma.model.TzmInfoHeplModel;
import com.ruiyu.taozhuma.utils.LogUtil;
import com.ruiyu.taozhuma.utils.ToastUtils;

public class TzmStaticHelpDetailActivity extends Activity{
	private Button btn_head_left;
	private TextView txt_head_title,tv_title;
	private WebView wv_content;
	
	private Integer id;
	
	private TzmInfoHelpApi tzmInfoHelpApi;
	private ApiClient apiClient;
	private List<TzmInfoHeplModel> tzmInfoHeplModels;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tzm_info_hepl_activity);
		id = getIntent().getExtras().getInt("id");
		loadData();
		
	}

	private void loadData() {
		tzmInfoHelpApi=new TzmInfoHelpApi();
		apiClient=new ApiClient(this);
		tzmInfoHelpApi.setId(id);
		apiClient.api(tzmInfoHelpApi, new ApiListener() {
			
			@Override
			public void onStart() {
				
			}
			@Override
			public void onError(String error) {
				LogUtil.Log(error);
			}
			@Override
			public void onException(Exception e) {
				LogUtil.ErrorLog(e);
			}
			@Override
			public void onComplete(String jsonStr) {
				Gson gson = new Gson();
				Type type = new TypeToken<BaseModel<List<TzmInfoHeplModel>>>() {
				}.getType();
				BaseModel<List<TzmInfoHeplModel>> base = gson.fromJson(
						jsonStr, type);
				if (base.success) {
					if (base.result != null) {
						tzmInfoHeplModels = base.result;
						init();
					}
				} else {
					ToastUtils.showShortToast(TzmStaticHelpDetailActivity.this,
							base.error_msg);
				}
			}
		}, true);
		
	}

	private void init() {
		txt_head_title = (TextView) findViewById(R.id.txt_head_title);
		btn_head_left=(Button) findViewById(R.id.btn_head_left);
		btn_head_left.setOnClickListener(clickListener);
		tv_title=(TextView) findViewById(R.id.tv_title);
		tv_title.setVisibility(View.GONE);
		wv_content = (WebView) findViewById(R.id.wv_content);
		WebSettings settings = wv_content.getSettings(); 
		settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN); 
		wv_content.getSettings().setJavaScriptEnabled(true);
		if (tzmInfoHeplModels != null && tzmInfoHeplModels.size() > 0) {
			txt_head_title.setText(tzmInfoHeplModels.get(0).title);
			txt_head_title.setTextColor(getResources().getColor(R.color.common_head_background));
			wv_content.loadData(tzmInfoHeplModels.get(0).content,
					"text/html; charset=UTF-8", "utf-8");
		}
	}
	View.OnClickListener clickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_head_left:
				onBackPressed();
				finish();
				break;
			}
		}
	};
	
}
