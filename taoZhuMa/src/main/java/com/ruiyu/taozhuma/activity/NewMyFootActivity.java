package com.ruiyu.taozhuma.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.bitmap.PauseOnScrollListener;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.adapter.NewMyFootAdapter;
import com.ruiyu.taozhuma.api.ApiClient;
import com.ruiyu.taozhuma.api.ApiListener;
import com.ruiyu.taozhuma.api.NewMyFootApi;
import com.ruiyu.taozhuma.base.xUtilsImageLoader;
import com.ruiyu.taozhuma.model.BaseModel;
import com.ruiyu.taozhuma.model.NewMyFootModel;
import com.ruiyu.taozhuma.model.UserModel;
import com.ruiyu.taozhuma.utils.LogUtil;
import com.ruiyu.taozhuma.utils.ProgressDialogUtil;
import com.ruiyu.taozhuma.utils.ToastUtils;
import com.ruiyu.taozhuma.utils.UserInfoUtils;

public class NewMyFootActivity extends Activity {
	private String TAG="NewMyFootActivity";
	private TextView txt_head_title;
	private Button btn_head_left;
	private TextView tv_content;
	private ListView lv_foot_list;

	private NewMyFootApi myFootApi;
	private ApiClient apiClient;
	private ArrayList<NewMyFootModel> list;
	private NewMyFootAdapter adapter;

	private Boolean isLogin;
	private UserModel userModel;
	private xUtilsImageLoader bitmapUtils;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myfoot_activity);
		LogUtil.Log(TAG, "onCreate");
		bitmapUtils=new xUtilsImageLoader(this);
		initView();
		checkLogin();
		list = new ArrayList<NewMyFootModel>();
		if (isLogin) {
			loadData();
		}
	}

	private void loadData() {
		apiClient = new ApiClient(this);
		myFootApi = new NewMyFootApi();
		myFootApi.setUid(userModel.uid);
		apiClient.api(myFootApi, new ApiListener() {

			@Override
			public void onStart() {
				ProgressDialogUtil.openProgressDialog(NewMyFootActivity.this,
						"", "");

			}

			@Override
			public void onError(String error) {
				ProgressDialogUtil.closeProgressDialog();
				LogUtil.Log(error);
                ToastUtils.showShortToast(NewMyFootActivity.this, error);
			}

			@Override
			public void onException(Exception e) {
				ProgressDialogUtil.closeProgressDialog();
				LogUtil.ErrorLog(e);
			}

			@Override
			public void onComplete(String jsonStr) {
				ProgressDialogUtil.closeProgressDialog();
				Gson gson = new Gson();
				Type type = new TypeToken<BaseModel<ArrayList<NewMyFootModel>>>() {
				}.getType();
				BaseModel<ArrayList<NewMyFootModel>> base = gson.fromJson(
						jsonStr, type);
				if (base.result != null && base.result.size() > 0) {
					list = base.result;
					lv_foot_list.setVisibility(View.VISIBLE);
					tv_content.setVisibility(View.GONE);
					initOrderList();
				} else {
					lv_foot_list.setVisibility(View.GONE);
					tv_content.setVisibility(View.VISIBLE);
				}
			}

		}, true);
	}

	protected void initOrderList() {
		if (this.list != null) {
			adapter = new NewMyFootAdapter(NewMyFootActivity.this, this.list,bitmapUtils);
			lv_foot_list.setAdapter(adapter);
			lv_foot_list.setOnScrollListener(new PauseOnScrollListener(bitmapUtils.getBitmapUtils(), true, true));
		}
	}

	private void initView() {
		txt_head_title = (TextView) findViewById(R.id.txt_head_title);
		txt_head_title.setText("我的足迹");
		btn_head_left = (Button) findViewById(R.id.btn_head_left);
		btn_head_left.setOnClickListener(clickListener);
		tv_content = (TextView) findViewById(R.id.tv_content);
		lv_foot_list = (ListView) findViewById(R.id.lv_foot_list);
	}

	View.OnClickListener clickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_head_left:
				onBackPressed();
				break;
			}
		}
	};

	// 检查用户是否登陆
	private void checkLogin() {
		isLogin = UserInfoUtils.isLogin();
		if (isLogin) {
			userModel = UserInfoUtils.getUserInfo();
		}
	}
}
