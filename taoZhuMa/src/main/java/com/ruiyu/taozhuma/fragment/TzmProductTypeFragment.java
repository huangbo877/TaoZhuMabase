package com.ruiyu.taozhuma.fragment;

/*
 import java.lang.reflect.Type;

 import com.baidu.location.LocationClient;
 import com.google.gson.Gson;
 import com.google.gson.reflect.TypeToken;
 import com.ruiyu.taozhuma.R;
 import com.ruiyu.taozhuma.api.ApiClient;
 import com.ruiyu.taozhuma.api.ApiListener;
 import com.ruiyu.taozhuma.api.AuthenticateApi;
 import com.ruiyu.taozhuma.api.OrdernumApi;
 import com.ruiyu.taozhuma.base.BaseApplication;
 import com.ruiyu.taozhuma.config.AppConfig;
 import com.ruiyu.taozhuma.model.AuthenticateModel;
 import com.ruiyu.taozhuma.model.BaseModel;
 import com.ruiyu.taozhuma.model.OrdernumModel;
 import com.ruiyu.taozhuma.model.UserModel;
 import com.ruiyu.taozhuma.utils.ImageUtils;
 import com.ruiyu.taozhuma.utils.LogUtil;
 import com.ruiyu.taozhuma.utils.PhoneUtil;
 import com.ruiyu.taozhuma.utils.ProgressDialogUtil;
 import com.ruiyu.taozhuma.utils.StringUtils;
 import com.ruiyu.taozhuma.utils.UserInfoUtils;

 import android.app.Activity;
 import android.content.Context;
 import android.content.Intent;
 import android.os.Build;
 import android.os.Bundle;
 import android.support.v4.app.Fragment;
 import android.support.v4.app.FragmentManager;
 import android.view.ContextMenu;
 import android.view.KeyEvent;
 import android.view.LayoutInflater;
 import android.view.MotionEvent;
 import android.view.View;
 import android.view.ContextMenu.ContextMenuInfo;
 import android.view.View.OnClickListener;
 import android.view.View.OnCreateContextMenuListener;
 import android.view.View.OnLongClickListener;
 import android.view.View.OnTouchListener;
 import android.view.ViewGroup;
 import android.webkit.WebView;
 import android.widget.BaseAdapter;
 import android.widget.Button;
 import android.widget.ImageButton;
 import android.widget.ImageView;
 import android.widget.LinearLayout;
 import android.widget.RadioButton;
 import android.widget.TextView;

 public class ProductTypeFragment extends Fragment 
 {
 // 定位相关
 private LocationClient locationClient = null;

 private FragmentManager manager;
 private LinearLayout llHome;
 private ImageView btnLogo;
 private ImageView imgLogo;
 private ImageView imgSearch;
 private Button btnBarcode;
 private RadioButton tab_rb_style;



 @Override
 public View onCreateView(LayoutInflater inflater, ViewGroup container,
 Bundle savedInstanceState) {
 //ProgressDialogUtil.closeProgressDialog();
 //LogUtil.Log("onCreateView");
 View view = inflater.inflate(R.layout.product_type_activity, null);


 return view;
 }





 View.OnClickListener clickListener = new View.OnClickListener() {

 @Override
 public void onClick(View v) {
 switch (v.getId()) {
 case R.id.im_change:
 break;
 }
 }
 };
 }*/

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.adapter.TzmProductTypeExAdapter;
import com.ruiyu.taozhuma.api.ApiClient;
import com.ruiyu.taozhuma.api.ApiListener;
import com.ruiyu.taozhuma.api.TzmProductTypeApi;
import com.ruiyu.taozhuma.model.BaseModel;
import com.ruiyu.taozhuma.model.TzmProductTypeModel;
import com.ruiyu.taozhuma.utils.ProgressDialogUtil;
import com.ruiyu.taozhuma.widget.AnimatedExpandableListView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.ExpandableListView.OnGroupClickListener;

/**
 * 产品类型
 * 
 * @author fu
 * 
 */

public class TzmProductTypeFragment extends Fragment {

	private String TAG = "TzmProductTypeFragment";
	@ViewInject(R.id.exlx_product_type)
	private AnimatedExpandableListView listView;
	@ViewInject(R.id.txt_head_title)
	private TextView txt_head_title;

	// 接口调用
	private ApiClient client;
	private TzmProductTypeApi api;
	private List<TzmProductTypeModel> tzmProductTypeModels;
	private TzmProductTypeExAdapter adapter;

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, TAG + "onCreateView");
		View v = inflater.inflate(R.layout.tzm_product_type_activity, null);
		ViewUtils.inject(this, v);
		txt_head_title.setText("商品分类");
		loadData();
		return v;
	}

	private void loadData() {
		client = new ApiClient(getActivity());
		api = new TzmProductTypeApi();
		tzmProductTypeModels = new ArrayList<TzmProductTypeModel>();
		client.api(this.api, new ApiListener() {
			@Override
			public void onStart() {
				ProgressDialogUtil.openProgressDialog(getActivity(), "",
						"正在加载...");
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
					adapter = new TzmProductTypeExAdapter(getActivity(),
							tzmProductTypeModels);
					listView.setAdapter(adapter);
					listView.setOnGroupClickListener(new OnGroupClickListener() {

						@Override
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

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
