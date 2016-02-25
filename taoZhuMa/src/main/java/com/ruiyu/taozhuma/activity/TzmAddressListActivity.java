package com.ruiyu.taozhuma.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.adapter.TzmAddressListAdapter2;
import com.ruiyu.taozhuma.api.ApiClient;
import com.ruiyu.taozhuma.api.ApiListener;
import com.ruiyu.taozhuma.api.TzmAddressListApi;
import com.ruiyu.taozhuma.api.TzmDelAddressApi;
import com.ruiyu.taozhuma.config.AppConfig;
import com.ruiyu.taozhuma.model.BaseModel;
import com.ruiyu.taozhuma.model.TzmAddressModel;
import com.ruiyu.taozhuma.model.UserModel;
import com.ruiyu.taozhuma.utils.ProgressDialogUtil;
import com.ruiyu.taozhuma.utils.StringUtils;
import com.ruiyu.taozhuma.utils.ToastUtils;
import com.ruiyu.taozhuma.utils.UserInfoUtils;

public class TzmAddressListActivity extends Activity implements
		OnItemLongClickListener {

	private TextView txt_head_title;
	private Button btn_head_left;
	private ListView lv_address_list;
	//private ArrayList<String> list;
	// private TextView isDefaulet;
	// private Button btn_head_right;

	// 列表
	private TzmAddressListAdapter2 tzmAddressListAdapter;

	// 接口调用
	private ApiClient client;
	private TzmAddressListApi addressListApi;
	private List<TzmAddressModel> tzmAddressModel;
	//private TzmAddressModel tzmModel;
	private TzmDelAddressApi addressApi;
	// 用户信息
	private Boolean isLogin;
	private UserModel userModel;

	private MyHandler myHandler;

	private int tag;
	@ViewInject(R.id.rl_address_empty)
	private RelativeLayout rl_address_empty;
	@ViewInject(R.id.im_add_address)
	private ImageView im_add_address;
	@ViewInject(R.id.iv_add_address)
	private ImageView iv_add_address;
	// private List<Boolean> mChecked;
	// private CheckBox cb_isDefault;
	// private TzmSelectAddressApi tzmSelectAddressApi;
	// private ApiClient client1;
	@ViewInject(R.id.rl_full)
	private RelativeLayout rl_full;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tzm_address_list_activity);
		ViewUtils.inject(this);
		tag = getIntent().getIntExtra("tag", 0);
		myHandler = new MyHandler();

		txt_head_title = (TextView) findViewById(R.id.txt_head_title);
		txt_head_title.setText("管理收货地址");
		btn_head_left = (Button) findViewById(R.id.btn_head_left);
		btn_head_left.setOnClickListener(clickListener);// 返回按钮事件侦听
		// isDefaulet = (TextView) findViewById(R.id.isDefaulet);
		// btn_head_right = (Button) findViewById(R.id.btn_head_right);
		iv_add_address.setOnClickListener(clickListener);
		im_add_address.setOnClickListener(clickListener);
		// btn_head_right.setText("新增");
		// btn_head_right.setTextColor(getResources().getColor(R.color.black));
		// btn_head_right.setVisibility(View.VISIBLE);

		client = new ApiClient(this);
		// client1 = new ApiClient(this);
		addressListApi = new TzmAddressListApi();
		// tzmSelectAddressApi =new TzmSelectAddressApi();
		tzmAddressModel = new ArrayList<TzmAddressModel>();
		lv_address_list = (ListView) findViewById(R.id.lv_address_list);//

		checkLogin();
		loadData();
		lv_address_list.setOnItemLongClickListener(this);
		lv_address_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (tag == AppConfig.TAG_ADDRESS_SELECT) {
					TzmAddressModel model = (TzmAddressModel) parent
							.getAdapter().getItem(position);
					Intent intent = new Intent();
					intent.putExtra("data", model);
					setResult(RESULT_OK, intent);
					finish();
				}else{
					TzmAddressModel model = (TzmAddressModel) parent
							.getAdapter().getItem(position);
					Intent intent = new Intent(TzmAddressListActivity.this,TzmEditAdressActivity.class);
					intent.putExtra("addId", model.addId);
					startActivityForResult(intent, 0);
				}

			}
		});

	}

	protected void loadData() {
		if (!isLogin) {
			return;
		}
		addressListApi.setUid(userModel.uid);//

		client.api(this.addressListApi, new ApiListener() {
			@Override
			public void onStart() {
				ProgressDialogUtil.openProgressDialog(
						TzmAddressListActivity.this, null, null);
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
					rl_address_empty.setVisibility(View.GONE);
					rl_full.setVisibility(View.VISIBLE);
				} else {
					rl_address_empty.setVisibility(View.VISIBLE);
					rl_full.setVisibility(View.GONE);
				}
				initProductList();
			}
		}, true);

	}

	protected void initProductList() {
		if (this.tzmAddressModel != null) {
			// mChecked = new ArrayList<Boolean>();
			tzmAddressListAdapter = new TzmAddressListAdapter2(
					TzmAddressListActivity.this, this.tzmAddressModel,
					myHandler);
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
			case R.id.iv_add_address:
				Intent intent = new Intent(TzmAddressListActivity.this,
						TzmNewAdressActivity.class);
				startActivityForResult(intent, AppConfig.TAG_ADDRESS_ADD);
				break;
			case R.id.im_add_address:
				Intent intent2 = new Intent(TzmAddressListActivity.this,
						TzmNewAdressActivity.class);
				startActivityForResult(intent2, AppConfig.TAG_ADDRESS_ADD);
				break;
			}
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
				loadData();
		}
	};

	private class MyHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 1) {
				loadData();
			}
		}

	}

	private void checkLogin() {
		isLogin = UserInfoUtils.isLogin();
		if (isLogin) {
			userModel = UserInfoUtils.getUserInfo();
		}

	}

	protected void deleteAddress(final int position) {
		addressApi = new TzmDelAddressApi();
		client = new ApiClient(this);
		addressApi.setUid(userModel.uid);
		addressApi.setAddId(tzmAddressModel.get(position).addId);
		client.api(addressApi, new ApiListener() {

			@Override
			public void onStart() {
				ProgressDialogUtil.openProgressDialog(
						TzmAddressListActivity.this, "", "");

			}

			@Override
			public void onComplete(String jsonStr) {
				ProgressDialogUtil.closeProgressDialog();
				if (StringUtils.isNotBlank(jsonStr)) {
					try {
						JSONObject jsonObject = new JSONObject(jsonStr);
						boolean success = jsonObject.optBoolean("success");
						int result = jsonObject.optInt("result");
						String error_msg = jsonObject.optString("error_msg");
						ToastUtils.showShortToast(TzmAddressListActivity.this,
								error_msg);
						if (success) {
							tzmAddressModel.remove(position);
							if (tzmAddressModel.size() == 0) {
								rl_address_empty.setVisibility(View.VISIBLE);
								rl_full.setVisibility(View.GONE);
							} else {
								rl_address_empty.setVisibility(View.GONE);
								rl_full.setVisibility(View.VISIBLE);
							}
							tzmAddressListAdapter.notifyDataSetChanged();
						}

					} catch (JSONException e) {
						e.printStackTrace();
					}

				}

			}

		}, true);

	}

	@Override
	protected void onRestart() {
		super.onRestart();
		//loadData(1);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View v,
			final int position, long id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("操作")
				.setPositiveButton("删除", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						deleteAddress(position);
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
		return true;
	}
}