package com.ruiyu.taozhuma.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.OtherUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.adapter.ExAdapter;
import com.ruiyu.taozhuma.api.ApiClient;
import com.ruiyu.taozhuma.api.ApiListener;
import com.ruiyu.taozhuma.api.TzmHelpCenterApi;
import com.ruiyu.taozhuma.model.BaseModel;
import com.ruiyu.taozhuma.model.TzmHelpCenterModel;
import com.ruiyu.taozhuma.utils.PhoneUtils;
import com.ruiyu.taozhuma.utils.ProgressDialogUtil;
import com.ruiyu.taozhuma.utils.StringUtils;
import com.ruiyu.taozhuma.widget.AnimatedExpandableListView;

public class TzmHelpTypeListActivity extends Activity {

	private TzmHelpCenterModel helpFather1;
	private TzmHelpCenterModel helpFather2;
	private TzmHelpCenterModel helpFather3;

	private ApiClient apiClient;
	private TzmHelpCenterApi api;
	private List<TzmHelpCenterModel> list;
	// private AnimatedExpandableListView expandableListView;
	private Button btn_head_left;
	private TextView txt_head_title;

	private LinearLayout ll_call_phone, ll_Feedback;
	@ViewInject(R.id.tv_kfdh)
	private TextView tv_kfdh;// 客服

	// 常见问题,售前问题,售后问题
	@OnClick({ R.id.ll_1, R.id.ll_2, R.id.ll_3 })
	public void Onclick(View v) {
		switch (v.getId()) {
		case R.id.ll_1:
			Intent intent1 = new Intent(TzmHelpTypeListActivity.this,
					ShowHelpChildListActivity.class);
			ShowHelpChildListActivity.tzmHelpCenterModel = helpFather1;
			startActivity(intent1);
			break;
		case R.id.ll_2:
			Intent intent2 = new Intent(TzmHelpTypeListActivity.this,
					TzmHelpListActivity.class);

			intent2.putExtra("id", helpFather2.son.get(0).sonId);
			intent2.putExtra("title", helpFather2.son.get(0).sonName);
			startActivity(intent2);
			break;
		case R.id.ll_3:
			Intent intent3 = new Intent(TzmHelpTypeListActivity.this,
					TzmHelpListActivity.class);

			intent3.putExtra("id", helpFather3.son.get(0).sonId);
			intent3.putExtra("title", helpFather3.son.get(0).sonName);

			startActivity(intent3);
			break;

		default:
			break;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tzm_help_type_list_activity);
		ViewUtils.inject(this); // 注入view和事件

		// expandableListView = (AnimatedExpandableListView)
		// findViewById(R.id.exlx_help_type);
		txt_head_title = (TextView) this.findViewById(R.id.txt_head_title);
		txt_head_title.setText("客服与帮助");
		btn_head_left = (Button) this.findViewById(R.id.btn_head_left);
		// et_content = (EditText) findViewById(R.id.et_content);
		// im_help_search = (ImageView) findViewById(R.id.im_help_search);
		// im_help_search.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// name = et_content.getText().toString();
		// Intent sintent = new Intent(TzmHelpTypeListActivity.this,
		// TzmHelpSearchListActivity.class);
		// sintent.putExtra("name", name);
		// startActivity(sintent);
		// }
		//
		// });
		tv_kfdh.setText("客服电话：" + getString(R.string.consumer_hotline));
		ll_Feedback = (LinearLayout) findViewById(R.id.ll_Feedback);
		ll_Feedback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent_feedback = new Intent(
						TzmHelpTypeListActivity.this, TzmFeedbackActivity.class);
				startActivity(intent_feedback);
			}

		});
		ll_call_phone = (LinearLayout) findViewById(R.id.ll_call_phone);
		ll_call_phone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				PhoneUtils.callPhone(TzmHelpTypeListActivity.this,
						getString(R.string.consumer_hotline));
			}

		});
		btn_head_left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();

			}
		});
		apiClient = new ApiClient(this);
		list = new ArrayList<TzmHelpCenterModel>();
		api = new TzmHelpCenterApi();
		loadData();

	}

	private void loadData() {
		apiClient.api(api, new ApiListener() {

			@Override
			public void onStart() {
				ProgressDialogUtil.openProgressDialog(
						TzmHelpTypeListActivity.this, "", "");

			}

			@Override
			public void onComplete(String jsonStr) {
				ProgressDialogUtil.closeProgressDialog();
				Gson gson = new Gson();
				Type type = new TypeToken<BaseModel<List<TzmHelpCenterModel>>>() {
				}.getType();
				BaseModel<List<TzmHelpCenterModel>> base = gson.fromJson(
						jsonStr, type);
				if (base.result != null && base.result.size() > 0) {
					list = base.result;

					for (TzmHelpCenterModel tzmHelpCenterModel : list) {
						if (tzmHelpCenterModel.fatherId.equals(1)) {
							helpFather1 = tzmHelpCenterModel;
						}
						if (tzmHelpCenterModel.fatherId.equals(2)) {
							helpFather2 = tzmHelpCenterModel;
						}
						if (tzmHelpCenterModel.fatherId.equals(3)) {
							helpFather3 = tzmHelpCenterModel;
						}

					}

					// adapter = new ExAdapter(TzmHelpTypeListActivity.this,
					// list);
					// expandableListView.setAdapter(adapter);
					// expandableListView
					// .setOnGroupClickListener(new OnGroupClickListener() {
					//
					// @Override
					// public boolean onGroupClick(
					// ExpandableListView parent, View v,
					// int groupPosition, long id) {
					// // We call collapseGroupWithAnimation(int)
					// // and
					// // expandGroupWithAnimation(int) to animate
					// // group
					// // expansion/collapse.
					// if (expandableListView
					// .isGroupExpanded(groupPosition)) {
					// expandableListView
					// .collapseGroupWithAnimation(groupPosition);
					// } else {
					// expandableListView
					// .expandGroupWithAnimation(groupPosition);
					// }
					// return true;
					// }
					//
					// });
					// expandableListView.setDivider(null);
					// expandableListView.setGroupIndicator(null);
				}

			}
		}, true);

	}

}
