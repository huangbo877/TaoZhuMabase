package com.ruiyu.taozhuma.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.adapter.TenNewAdapter;
import com.ruiyu.taozhuma.api.ApiClient;
import com.ruiyu.taozhuma.api.ApiListener;
import com.ruiyu.taozhuma.api.GetActivityProductByTypeApi;
import com.ruiyu.taozhuma.base.xUtilsImageLoader;
import com.ruiyu.taozhuma.model.BaseModel;
import com.ruiyu.taozhuma.model.GetActivityProductByTypeModel;
import com.ruiyu.taozhuma.utils.LogUtil;
import com.ruiyu.taozhuma.utils.ProgressDialogUtil;
import com.ruiyu.taozhuma.utils.ToastUtils;

/**
 * 每日十件
 * 
 * @author Fu //备份
 * 
 */
public class TenNewActivity extends Activity {

	private String TAG = "TenNewActivity";
	private TextView txt_head_title;
	private Button btn_head_left;
	private TextView tv_content;
	private ListView listView;
	// 列表
	private TenNewAdapter activityProductByTypeAdapter;
	// 接口调用
	private ApiClient client;

	private xUtilsImageLoader imageLoader;// 加载图片

	private GetActivityProductByTypeApi activityProductByTypeApi;
	private List<GetActivityProductByTypeModel> activityProductByTypeModels;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ten_new_activity);
		txt_head_title = (TextView) findViewById(R.id.txt_head_title);
		tv_content = (TextView) findViewById(R.id.tv_content);
		btn_head_left = (Button) findViewById(R.id.btn_head_left);
		btn_head_left.setOnClickListener(clickListener);

		txt_head_title.setText("每日十件");

		imageLoader = new xUtilsImageLoader(this);

		client = new ApiClient(this);
		activityProductByTypeApi = new GetActivityProductByTypeApi();
		activityProductByTypeModels = new ArrayList<GetActivityProductByTypeModel>();

		listView = (ListView) findViewById(R.id.prl_hot_product_list);
		listView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int positon,
					long id) {
				GetActivityProductByTypeModel model = activityProductByTypeModels
						.get(positon);
				Intent intent = new Intent(TenNewActivity.this,
						ProductDetailActivity.class);
				intent.putExtra("id", model.productId);
				intent.putExtra("activityId", model.activityId);
				LogUtil.Log(TAG, "onCreate----onItemClick"
						+ "改为专场的版本后 要传个 活动ID 到商品详情页" + model.activityId);
				startActivity(intent);
			}
		});
		loadData(1);
	}

	// 网络加载数据
	protected void loadData(int type) {
		activityProductByTypeApi.setType(type);
		client.api(this.activityProductByTypeApi, new ApiListener() {
			@Override
			public void onStart() {
				ProgressDialogUtil.openProgressDialog(TenNewActivity.this,
						null, null);
			}

			@Override
			public void onComplete(String jsonStr) {
				ProgressDialogUtil.closeProgressDialog();
				Gson gson = new Gson();
				Type type = new TypeToken<BaseModel<ArrayList<GetActivityProductByTypeModel>>>() {
				}.getType();
				BaseModel<ArrayList<GetActivityProductByTypeModel>> base = gson
						.fromJson(jsonStr, type);

				if (base.result != null && base.result.size() > 0) {
					for (int i = 0; i < base.result.size(); i++) {
						base.result.get(i).gid = i + 1;
					}
					activityProductByTypeModels = base.result;
					listView.setVisibility(View.VISIBLE);
					tv_content.setVisibility(View.GONE);
					initProductList();
				} else {
					// 返回数据为空
					ToastUtils.showShortToast(TenNewActivity.this,
							base.error_msg);
					tv_content.setVisibility(View.VISIBLE);
					listView.setVisibility(View.GONE);
				}

			}

			@Override
			public void onError(String error) {
				ProgressDialogUtil.closeProgressDialog();
				ToastUtils.showShortToast(TenNewActivity.this, error);
				tv_content.setVisibility(View.VISIBLE);
				listView.setVisibility(View.GONE);
			}

			@Override
			public void onException(Exception e) {
				ProgressDialogUtil.closeProgressDialog();
				LogUtil.ErrorLog(e);
				ToastUtils.showShortToast(TenNewActivity.this, "网络异常");
			}
		}, true);

	}

	// 初始化数据
	protected void initProductList() {
		if (this.activityProductByTypeModels != null) {
			activityProductByTypeAdapter = new TenNewAdapter(
					TenNewActivity.this, this.activityProductByTypeModels,
					imageLoader);
			listView.setAdapter(activityProductByTypeAdapter);
		}
	}

	OnClickListener clickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_head_left:
				onBackPressed();
				break;
			// case R.id.imbt_head_right:
			// startActivity(new Intent(TzmNewActivity.this,
			// TzmShopActivity.class));
			// break;
			}
		}
	};
}
