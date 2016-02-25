package com.ruiyu.taozhuma.activity;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.api.ApiClient;
import com.ruiyu.taozhuma.api.ApiListener;
import com.ruiyu.taozhuma.api.TzmClientInfoDetailApi;
import com.ruiyu.taozhuma.model.BaseModel;
import com.ruiyu.taozhuma.model.TzmInfoDetailModel;
import com.ruiyu.taozhuma.utils.LogUtil;
import com.ruiyu.taozhuma.utils.ToastUtils;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

public class TzmClientInfoDetailActivity extends Activity {

	private Button btn_head_left;
	private TextView txt_head_title, tv_client_detail_time;
	private TextView tv_title;
	private WebView wv_client_detail_content;
	private Integer id;

	private ApiClient client;
	private TzmClientInfoDetailApi tzmClientInfoDetailApi;
	private List<TzmInfoDetailModel> tzmInfoDetailModels;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		id = getIntent().getExtras().getInt("id");

		client = new ApiClient(this);
		tzmClientInfoDetailApi = new TzmClientInfoDetailApi();
		tzmClientInfoDetailApi.setId(id);

		client.api(tzmClientInfoDetailApi, new ApiListener() {

			@Override
			public void onStart() {
			}

			@Override
			public void onException(Exception e) {
				LogUtil.ErrorLog(e);
			}

			@Override
			public void onError(String error) {
				LogUtil.Log(error);
			}

			@Override
			public void onComplete(String jsonStr) {
				Gson gson = new Gson();
				Type type = new TypeToken<BaseModel<List<TzmInfoDetailModel>>>() {
				}.getType();
				BaseModel<List<TzmInfoDetailModel>> base = gson.fromJson(
						jsonStr, type);
				if (base.success) {
					if (base.result != null) {
						tzmInfoDetailModels = base.result;
						init();
					}
				} else {
					ToastUtils.showShortToast(TzmClientInfoDetailActivity.this,
							base.error_msg);
				}

			}
		}, true);
	}

	private void init() {

		setContentView(R.layout.tzm_client_info_detail);
		txt_head_title = (TextView) findViewById(R.id.txt_head_title);
		txt_head_title.setText("详情");
		btn_head_left = (Button) findViewById(R.id.btn_head_left);
		btn_head_left.setOnClickListener(clickListener);
		wv_client_detail_content = (WebView) findViewById(R.id.wv_client_detail_content);
		tv_client_detail_time = (TextView) findViewById(R.id.tv_client_detail_time);
		tv_title=(TextView) findViewById(R.id.tv_title);
		if (tzmInfoDetailModels != null && tzmInfoDetailModels.size() > 0) {
			tv_client_detail_time
					.setText(tzmInfoDetailModels.get(0).createDate);
			tv_title.setText(tzmInfoDetailModels.get(0).title);
			wv_client_detail_content.loadData(
					tzmInfoDetailModels.get(0).content,
					"text/html; charset=UTF-8", "utf-8");
		}
	}

	OnClickListener clickListener = new OnClickListener() {

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
