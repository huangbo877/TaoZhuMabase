package com.ruiyu.taozhuma.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.R.bool;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.adapter.SearchHistoryItemAdapter;
import com.ruiyu.taozhuma.adapter.TzmProductTypeExAdapter;
import com.ruiyu.taozhuma.api.ApiClient;
import com.ruiyu.taozhuma.api.ApiListener;
import com.ruiyu.taozhuma.api.TzmProductTypeApi;
import com.ruiyu.taozhuma.model.BaseModel;
import com.ruiyu.taozhuma.model.TzmCustomSearchModel;
import com.ruiyu.taozhuma.model.TzmProductTypeModel;
import com.ruiyu.taozhuma.utils.ProgressDialogUtil;
import com.ruiyu.taozhuma.utils.StringUtils;
import com.ruiyu.taozhuma.utils.ToastUtils;
import com.ruiyu.taozhuma.utils.UserInfoUtils;
import com.ruiyu.taozhuma.widget.AnimatedExpandableListView;

public class TzmSearch2Activity extends Activity {
	private Button btn_head_left;
	private EditText et_search;
	private TextView tv_clear;
	private ImageView im_search, im_search1, im_search2;
	private LinearLayout ll_ss;
	private RelativeLayout ll_search;

	private String keys;// 搜索关键字（产品名称）
	private ArrayList<ArrayList<TzmCustomSearchModel>> arrayLists;
	boolean ss = false;
	private ListView lv_history;

	private List<String> strings;
	private SearchHistoryItemAdapter adapter;
	// private ArrayAdapter adapter;
	private int list_position = -1;
	@ViewInject(R.id.exlx_product_type)
	private AnimatedExpandableListView listView;

	// 接口调用
	private ApiClient client;
	private TzmProductTypeApi api;
	private List<TzmProductTypeModel> tzmProductTypeModels;
	private TzmProductTypeExAdapter adapter1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tzm_srarch_activity);
		arrayLists = UserInfoUtils.getSearchHistory();// 并获取历史记录
		initView();
		ViewUtils.inject(this);
		ll_search.setVisibility(View.VISIBLE);
		ll_ss.setVisibility(View.GONE);
		listView.setVisibility(View.GONE);
		lv_history.setVisibility(View.VISIBLE);
		new Handler().postDelayed(new Runnable() {
			public void run() {
				// execute the task
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}, 1000);

		// loadData();
		strings = new ArrayList<String>();// 历史记录关键字list
		tv_clear.setVisibility(View.INVISIBLE);
		// 判断是否有历史搜索记录
		if (arrayLists != null && arrayLists.size() > 0) {// 有历史记录，显示listview
			// tv_clear.setVisibility(View.VISIBLE);

			for (int i = 0; i < arrayLists.size(); i++) {// 获取每次搜索时的关键字
				strings.add(arrayLists.get(i).get(0).searchKey);
			}

			adapter = new SearchHistoryItemAdapter(TzmSearch2Activity.this,
					strings);
			lv_history.setAdapter(adapter);// 关键字作为string显示在listview上

			lv_history.setOnItemClickListener(new OnItemClickListener() {// listview的item点击事件，

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
							list_position = arg2;
							Intent intent = new Intent(TzmSearch2Activity.this,
									TzmSearchItemActivity.class);
							// intent.putExtra("list_position", arg2);
							// intent.putExtra("isHistory", false);
							intent.putExtra("keys", strings.get(arg2));
							startActivityForResult(intent, 1);
						}
					});

		} else {
			tv_clear.setVisibility(View.INVISIBLE);
		}
		if (arrayLists == null) {
			arrayLists = new ArrayList<ArrayList<TzmCustomSearchModel>>();
		}

	}

	private void initView() {
		et_search = (EditText) findViewById(R.id.et_search);
		et_search.setImeOptions(EditorInfo.IME_ACTION_SEARCH);// 显示搜索图标。
		et_search.setInputType(EditorInfo.TYPE_CLASS_TEXT);
		et_search
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						if (actionId == EditorInfo.IME_ACTION_SEND
								|| (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
							if (StringUtils.isNotEmpty(et_search.getText()
									.toString())) {
								keys = et_search.getText().toString();
								Intent intent = new Intent(
										TzmSearch2Activity.this,
										TzmSearchItemActivity.class);
								intent.putExtra("isHistory", false);
								intent.putExtra("keys", keys);
								startActivityForResult(intent, 1);
							} else {
								ToastUtils.showShortToast(
										TzmSearch2Activity.this, "请输入商品名称");
							}
						}
						return false;
					}
				});
		ll_search = (RelativeLayout) findViewById(R.id.ll_search);
		ll_ss = (LinearLayout) findViewById(R.id.ll_ss);
		ll_ss = (LinearLayout) findViewById(R.id.ll_ss);
		im_search = (ImageView) findViewById(R.id.im_search);
		im_search.setOnClickListener(clickListener);
		im_search1 = (ImageView) findViewById(R.id.im_search1);
		ll_ss.setOnClickListener(clickListener);
		im_search2 = (ImageView) findViewById(R.id.im_search2);
		im_search2.setOnClickListener(clickListener);
		btn_head_left = (Button) findViewById(R.id.btn_head_left);
		btn_head_left.setOnClickListener(clickListener);
		tv_clear = (TextView) findViewById(R.id.tv_clear);
		tv_clear.setOnClickListener(clickListener);
		lv_history = (ListView) findViewById(R.id.lv_history);
	}

	private void loadData() {
		client = new ApiClient(TzmSearch2Activity.this);
		api = new TzmProductTypeApi();
		tzmProductTypeModels = new ArrayList<TzmProductTypeModel>();
		client.api(this.api, new ApiListener() {
			@Override
			public void onStart() {
				ProgressDialogUtil.openProgressDialog(TzmSearch2Activity.this,
						"", "正在加载...");
			}

			@Override
			public void onComplete(String jsonStr) {
				ProgressDialogUtil.closeProgressDialog();
				Gson gson = new Gson();
				Type type = new TypeToken<BaseModel<ArrayList<TzmProductTypeModel>>>() {
				}.getType();
				BaseModel<ArrayList<TzmProductTypeModel>> base = gson.fromJson(
						jsonStr, type);

				if (base.result != null) {
					tzmProductTypeModels = base.result;
					adapter1 = new TzmProductTypeExAdapter(
							TzmSearch2Activity.this, tzmProductTypeModels);
					listView.setAdapter(adapter1);
					listView.setOnGroupClickListener(new OnGroupClickListener() {

						public boolean onGroupClick(ExpandableListView parent,
								View v, int groupPosition, long id) {
							// We call collapseGroupWithAnimation(int)
							// and
							// expandGroupWithAnimation(int) to animate
							// group
							// expansion/collapse.
							if (listView.isGroupExpanded(groupPosition)) {
								listView.collapseGroupWithAnimation(groupPosition);
							} else {
								listView.expandGroupWithAnimation(groupPosition);
							}
							return true;
						}

					});
					listView.setDivider(null);
					listView.setGroupIndicator(null);

				}

			}

		}, true);
	}

	OnClickListener clickListener = new View.OnClickListener() {

		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_head_left:
				if (!ss) {
					onBackPressed();
				} else {
					ll_search.setVisibility(View.GONE);
					ll_ss.setVisibility(View.VISIBLE);
					listView.setVisibility(View.VISIBLE);
					lv_history.setVisibility(View.GONE);
					ss = false;
					et_search.setText("");
					InputMethodManager imm1 = (InputMethodManager) getApplicationContext()
							.getSystemService(Context.INPUT_METHOD_SERVICE);
					imm1.hideSoftInputFromWindow(et_search.getWindowToken(), 0);
				}
				break;
			case R.id.tv_clear:// 清空历史记录
				if (arrayLists != null && arrayLists.size() > 0) {
					UserInfoUtils.clearSearchHistory();
					tv_clear.setVisibility(View.INVISIBLE);
					strings.clear();
					adapter.notifyDataSetChanged();
				}

				break;
			case R.id.im_search:
				if (StringUtils.isEmpty(et_search.getText().toString())) {
					ToastUtils.showShortToast(TzmSearch2Activity.this,
							"请输入商品名称或店铺名");
				} else {
					keys = et_search.getText().toString();
					Intent intent = new Intent(TzmSearch2Activity.this,
							TzmSearchItemActivity.class);
					intent.putExtra("isHistory", false);
					intent.putExtra("keys", keys);
					startActivityForResult(intent, 1);
				}
				break;
			case R.id.ll_ss:
				ll_search.setVisibility(View.VISIBLE);
				ll_ss.setVisibility(View.GONE);
				listView.setVisibility(View.GONE);
				lv_history.setVisibility(View.VISIBLE);
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
				ss = true;
				break;
			case R.id.im_search2:
				et_search.setText("");
				break;
			}
		}
	};

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 11) {
			arrayLists = UserInfoUtils.getSearchHistory();// 并获取历史记录
			strings.clear();
			if (arrayLists != null) {
				for (int i = 0; i < arrayLists.size(); i++) {// 获取每次搜索时的关键字
					strings.add(arrayLists.get(i).get(0).searchKey);
				}
				if (adapter != null) {
					adapter.notifyDataSetChanged();
				} else {
					adapter = new SearchHistoryItemAdapter(
							TzmSearch2Activity.this, strings);
					lv_history.setAdapter(adapter);// 关键字作为string显示在listview上
					lv_history
							.setOnItemClickListener(new OnItemClickListener() {// listview的item点击事件，

								@Override
								public void onItemClick(AdapterView<?> arg0,
										View arg1, int arg2, long arg3) {
									list_position = arg2;
									Intent intent = new Intent(
											TzmSearch2Activity.this,
											TzmSearchItemActivity.class);
									// intent.putExtra("list_position", arg2);
									// intent.putExtra("isHistory", false);
									intent.putExtra("keys", strings.get(arg2));
									startActivityForResult(intent, 1);
								}
							});
				}
			}

		}
	}

}
