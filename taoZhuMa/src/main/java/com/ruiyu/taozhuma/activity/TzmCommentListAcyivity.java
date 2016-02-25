package com.ruiyu.taozhuma.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.adapter.TzmCommentListAdapter;
import com.ruiyu.taozhuma.adapter.TzmActivityAdapter;
import com.ruiyu.taozhuma.adapter.TzmActivityAdapter;
import com.ruiyu.taozhuma.adapter.TzmShopListAdapter;
import com.ruiyu.taozhuma.api.ApiClient;
import com.ruiyu.taozhuma.api.ApiListener;
import com.ruiyu.taozhuma.api.TzmActivityApi;
import com.ruiyu.taozhuma.api.TzmProductDetailCommentApi;
import com.ruiyu.taozhuma.model.BaseModel;
import com.ruiyu.taozhuma.model.TzmActivityModel;
import com.ruiyu.taozhuma.model.TzmProductDetailCommentModel;
import com.ruiyu.taozhuma.model.UserModel;
import com.ruiyu.taozhuma.utils.LogUtil;
import com.ruiyu.taozhuma.utils.ToastUtils;
import com.ruiyu.taozhuma.utils.UserInfoUtils;

public class TzmCommentListAcyivity extends Activity {

	private TextView txt_head_title;
	private Button btn_head_left;
	private TextView tv_content;
	// 列表
	private TzmCommentListAdapter tzmHotProductAdapter;
	private ListView listView;

	// 接口调用
	private ApiClient client;
	private TzmProductDetailCommentApi api;
	private List<TzmProductDetailCommentModel> list;

	private int id;
	private UserModel userModel;
	private Boolean isLogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tzm_comment_list_activity);
		id = getIntent().getIntExtra("id", 0);
		checkLogin();
		txt_head_title = (TextView) findViewById(R.id.txt_head_title);
		txt_head_title.setText("用户口碑");
		btn_head_left = (Button) findViewById(R.id.btn_head_left);
		btn_head_left.setOnClickListener(clickListener);

		client = new ApiClient(this);
		api = new TzmProductDetailCommentApi();
		list = new ArrayList<TzmProductDetailCommentModel>();
		tv_content=(TextView) findViewById(R.id.tv_content);
		listView = (ListView) findViewById(R.id.lv_commentlist);
		loadData();
	}

	protected void loadData() {
		if(!isLogin){
			return;
		}
		api.setId(id);
		api.setUid(userModel.uid);
		client.api(this.api, new ApiListener() {
			@Override
			public void onStart() {
				// ProgressDialogUtil.openProgressDialog(
				// NewFragment.this.getActivity(), "", "正在加载...");
			}

			@Override
			public void onComplete(String jsonStr) {
				// ProgressDialogUtil.closeProgressDialog();
				Gson gson = new Gson();
				Type type = new TypeToken<BaseModel<ArrayList<TzmProductDetailCommentModel>>>() {
				}.getType();
				BaseModel<ArrayList<TzmProductDetailCommentModel>> base = gson
						.fromJson(jsonStr, type);

				if (base.result != null && base.result.size() > 0) {
					listView.setVisibility(View.VISIBLE);
					tv_content.setVisibility(View.GONE);
					list.addAll(base.result);
					tzmHotProductAdapter = new TzmCommentListAdapter(TzmCommentListAcyivity.this, list);
					listView.setAdapter(tzmHotProductAdapter);
				} else {
					ToastUtils.showShortToast(TzmCommentListAcyivity.this,
							base.error_msg);
					listView.setVisibility(View.GONE);
					tv_content.setVisibility(View.VISIBLE);
				}

			}

			@Override
			public void onError(String error) {
				ToastUtils.showShortToast(TzmCommentListAcyivity.this,
						R.string.msg_list_null);
				// ProgressDialogUtil.closeProgressDialog();
			}

			@Override
			public void onException(Exception e) {
				// ProgressDialogUtil.closeProgressDialog();
				LogUtil.ErrorLog(e);
			}
		}, true);

	}
	
	private void checkLogin() {
		isLogin = UserInfoUtils.isLogin();
		if (isLogin) {
			userModel = UserInfoUtils.getUserInfo();
		}

	}

	OnClickListener clickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_head_left:
				onBackPressed();
				break;
			}
		}
	};

}
