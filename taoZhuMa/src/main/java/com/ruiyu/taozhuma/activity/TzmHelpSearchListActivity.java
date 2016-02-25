package com.ruiyu.taozhuma.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.adapter.ExAdapter;
import com.ruiyu.taozhuma.adapter.TzmHelpCenterListAdapter;
import com.ruiyu.taozhuma.api.ApiClient;
import com.ruiyu.taozhuma.api.ApiListener;
import com.ruiyu.taozhuma.api.TzmHelpCenterApi;
import com.ruiyu.taozhuma.api.TzmHelpCenterListApi;
import com.ruiyu.taozhuma.api.TzmSearchHelpApi;
import com.ruiyu.taozhuma.model.BaseModel;
import com.ruiyu.taozhuma.model.ExhibitionInformationModel;
import com.ruiyu.taozhuma.model.TzmHelpCenterListModel;
import com.ruiyu.taozhuma.model.TzmHelpCenterModel;
import com.ruiyu.taozhuma.utils.ProgressDialogUtil;
import com.ruiyu.taozhuma.utils.ToastUtils;
import com.ruiyu.taozhuma.widget.AnimatedExpandableListView;

public class TzmHelpSearchListActivity extends Activity {

	private ApiClient apiClient;
	private TzmSearchHelpApi api;
	private List<TzmHelpCenterListModel> list;
	private TzmHelpCenterListAdapter adapter;
	private ListView lv_help_list;
	private Button btn_head_left;
	private TextView txt_head_title;
	private int id;
	private String name;
	// 搜索框
	private EditText et_content;
	private ImageView im_help_search;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		System.out.println("888888888");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tzm_help_seacrh_list_activity);

		name = getIntent().getExtras().getString("name");
		lv_help_list = (ListView) findViewById(R.id.lv_help_list);
		txt_head_title = (TextView) this.findViewById(R.id.txt_head_title);
		txt_head_title.setText("帮助中心");
		btn_head_left = (Button) this.findViewById(R.id.btn_head_left);
		btn_head_left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();

			}
		});
		et_content = (EditText) findViewById(R.id.et_content);
		im_help_search = (ImageView) findViewById(R.id.im_help_search);
		im_help_search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				name = et_content.getText().toString();
				loadData();
			}

		});
		apiClient = new ApiClient(this);
		list = new ArrayList<TzmHelpCenterListModel>();
		api = new TzmSearchHelpApi();
		loadData();
		lv_help_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				TzmHelpCenterListModel model  =(TzmHelpCenterListModel) parent.getAdapter().getItem(position);
				Intent intent = new Intent(TzmHelpSearchListActivity.this,
						TzmInfoHelpActivity.class);
				intent.putExtra("id", model.id);
				TzmHelpSearchListActivity.this.startActivity(intent);
			}
		});
	}

	private void loadData() {
		api.setName(name);
		apiClient.api(api, new ApiListener() {

			@Override
			public void onStart() {
				ProgressDialogUtil.openProgressDialog(
						TzmHelpSearchListActivity.this, "", "");

			}

			@Override
			public void onComplete(String jsonStr) {
				ProgressDialogUtil.closeProgressDialog();
				Gson gson = new Gson();
				Type type = new TypeToken<BaseModel<List<TzmHelpCenterListModel>>>() {
				}.getType();
				BaseModel<List<TzmHelpCenterListModel>> base = gson.fromJson(
						jsonStr, type);
				if (base.result != null && base.result.size() > 0) {
					list = base.result;
					adapter = new TzmHelpCenterListAdapter(
							TzmHelpSearchListActivity.this, list);
					lv_help_list.setAdapter(adapter);
				} else {
					ToastUtils.showShortToast(TzmHelpSearchListActivity.this,
							base.error_msg);
				}

			}
		}, true);

	}

}
