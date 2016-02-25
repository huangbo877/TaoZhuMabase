package com.ruiyu.taozhuma.activity;

import java.lang.reflect.Type;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.api.ApiClient;
import com.ruiyu.taozhuma.api.ApiListener;
import com.ruiyu.taozhuma.api.TzmInfoListDetailApi;
import com.ruiyu.taozhuma.model.BaseModel;
import com.ruiyu.taozhuma.model.TzmInfoDetailModel;
import com.ruiyu.taozhuma.utils.LogUtil;
import com.ruiyu.taozhuma.utils.ToastUtils;
/**
 * 咨询详情
 * @author Fu
 *
 */
public class InfoDetailActivity extends Activity {

	private Button btn_head_left;
	private TextView txt_head_title, tv_title, tv_author;
	private WebView wv_content;
	private ImageView iv_image;

	private Integer id;

	private ApiClient client;
	private TzmInfoListDetailApi tzmInfoListDetailApi;
	private List<TzmInfoDetailModel> tzmInfoDetailModels;

	// private TzmInfoImagesAdapter adapter;
	// private ListViewForScrollView lv_image;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tzm_info_detail_activity);
		id = getIntent().getExtras().getInt("id");
		client = new ApiClient(this);
		tzmInfoListDetailApi = new TzmInfoListDetailApi();
		tzmInfoListDetailApi.setId(2);

		client.api(tzmInfoListDetailApi, new ApiListener() {

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
					ToastUtils.showShortToast(InfoDetailActivity.this,
							base.error_msg);
				}

			}
		}, true);
	}

	private void init() {
		txt_head_title = (TextView) findViewById(R.id.txt_head_title);
		txt_head_title.setText("资讯详情");
		tv_title = (TextView) findViewById(R.id.tv_title);
		wv_content = (WebView) findViewById(R.id.wv_content);
		wv_content.getSettings().setJavaScriptEnabled(true);
		iv_image = (ImageView) findViewById(R.id.img_image);
		btn_head_left = (Button) findViewById(R.id.btn_head_left);
		btn_head_left.setOnClickListener(clickListener);
		tv_author = (TextView) findViewById(R.id.tv_author);
		// lv_image= (ListViewForScrollView) findViewById(R.id.lv_image);
		if (tzmInfoDetailModels != null && tzmInfoDetailModels.size() > 0) {
			tv_author.setText("作者：" + tzmInfoDetailModels.get(0).author);
			tv_title.setText(tzmInfoDetailModels.get(0).title);
			tv_title.setTextColor(getResources().getColor(R.color.common_head_background));
			wv_content.loadData(tzmInfoDetailModels.get(0).content,
					"text/html; charset=UTF-8", "utf-8");
			// adapter = new TzmInfoImagesAdapter(TzmInfoDetailActivity.this,
			// tzmInfoDetailModels.get(0).image);
			// lv_image.setAdapter(adapter);
		}
		/**
		 * if (!StringUtils.isEmpty(tzmInfoDetailModel.image)) {
		 * BaseApplication.getInstance().getImageWorker()
		 * .loadBitmap(tzmInfoDetailModel.image, iv_image); }
		 */

		// if (!StringUtils.isEmpty(tzmInfoDetailModel.title)) {
		// tv_title.setText(tzmInfoDetailModel.title);
		// }

		// if (!StringUtils.isEmpty(tzmInfoDetailModel.content)) {
		// tv_content.loadDataWithBaseURL(null, tzmInfoDetailModel.content,
		// "text/html", "utf-8", null);
		// tv_content.setWebChromeClient(new WebChromeClient());
		// }

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
