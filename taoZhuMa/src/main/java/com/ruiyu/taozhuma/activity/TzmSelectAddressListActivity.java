package com.ruiyu.taozhuma.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.adapter.TzmAddressListAdapter;
import com.ruiyu.taozhuma.api.ApiClient;
import com.ruiyu.taozhuma.api.ApiListener;
import com.ruiyu.taozhuma.api.TzmAddressApi;
import com.ruiyu.taozhuma.model.BaseModel;
import com.ruiyu.taozhuma.model.TzmAddressModel;
import com.ruiyu.taozhuma.model.UserModel;
import com.ruiyu.taozhuma.utils.ProgressDialogUtil;
import com.ruiyu.taozhuma.utils.ToastUtils;
import com.ruiyu.taozhuma.utils.UserInfoUtils;

/**
 * 选择地址
 * 
 * @author Fu
 * 
 */
public class TzmSelectAddressListActivity extends Activity {

	private TextView txt_head_title;
	private Button btn_head_left;
	private ListView lv_address_list;
	private TextView tv_add_address;

	// 列表
	private TzmAddressListAdapter tzmAddressListAdapter;

	// 接口调用
	private ApiClient client;
	private TzmAddressApi tzmAddressApi;
	private List<TzmAddressModel> tzmAddressModel;
	// 用户信息
	private Boolean isLogin;
	private UserModel userModel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tzm_address_list_activity);
		txt_head_title = (TextView) findViewById(R.id.txt_head_title);
		txt_head_title.setText("地址列表");
		btn_head_left = (Button) findViewById(R.id.btn_head_left);
		btn_head_left.setOnClickListener(clickListener);// 返回按钮事件侦听
		tv_add_address = (TextView) findViewById(R.id.tv_add_address);
		tv_add_address.setOnClickListener(clickListener);// “新增地址”事件侦听
		checkLogin();
		client = new ApiClient(this);
		tzmAddressApi = new TzmAddressApi();
		tzmAddressModel = new ArrayList<TzmAddressModel>();
		lv_address_list = (ListView) findViewById(R.id.lv_address_list);//
		lv_address_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View v,
					int position, long id) {
				TzmAddressModel selectitem = tzmAddressModel.get(position);
				Intent intent = new Intent();
				intent.putExtra("data", selectitem);
				setResult(RESULT_OK, intent);
				finish();

			}
		});
		loadData();

	}

	protected void loadData() {
		tzmAddressApi.setUid(userModel.uid);
		client.api(this.tzmAddressApi, new ApiListener() {
			@Override
			public void onStart() {
				ProgressDialogUtil.openProgressDialog(
						TzmSelectAddressListActivity.this, "", "");
			}

			@Override
			public void onComplete(String jsonStr) {
				ProgressDialogUtil.closeProgressDialog();
				Gson gson = new Gson();
				Type type = new TypeToken<BaseModel<ArrayList<TzmAddressModel>>>() {
				}.getType();
				BaseModel<ArrayList<TzmAddressModel>> base = gson.fromJson(
						jsonStr, type);
				if (base.result != null && base.result.size() > 0) {
					tzmAddressModel = base.result;
				} else {
					ToastUtils.showShortToast(
							TzmSelectAddressListActivity.this, base.error_msg);
				}
				initProductList();
			}
		}, true);

	}

	protected void initProductList() {
		if (this.tzmAddressModel != null) {
			tzmAddressListAdapter = new TzmAddressListAdapter(
					TzmSelectAddressListActivity.this, this.tzmAddressModel);

			lv_address_list.setAdapter(tzmAddressListAdapter);

		}
	}

	OnClickListener clickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_head_left:
				onBackPressed();
				break;
			case R.id.tv_add_address:
				Intent intent = new Intent(TzmSelectAddressListActivity.this,
						TzmAddAdressActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("addId", -1);//addId ==-1为添加地址  其他的为编辑地址
				intent.putExtras(bundle);
				startActivity(intent);
			}
		}
	};

	private void checkLogin() {
		isLogin = UserInfoUtils.isLogin();
		if (isLogin) {
			userModel = UserInfoUtils.getUserInfo();
		}

	}

}