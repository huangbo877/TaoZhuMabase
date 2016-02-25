package com.ruiyu.taozhuma.fragment;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.activity.MainTabActivity;
import com.ruiyu.taozhuma.activity.StartActivity;
import com.ruiyu.taozhuma.activity.TzmSearchActivity;
import com.ruiyu.taozhuma.activity.TzmShopListActivity;

import com.ruiyu.taozhuma.api.ActivityTimeListApi;
import com.ruiyu.taozhuma.api.ApiClient;
import com.ruiyu.taozhuma.api.ApiListener;
import com.ruiyu.taozhuma.api.NavigationListApi;
import com.ruiyu.taozhuma.even.ScrollEven;
import com.ruiyu.taozhuma.model.ActivityTimeListModel;
import com.ruiyu.taozhuma.model.BaseModel;
import com.ruiyu.taozhuma.model.NavigationlistModel;
import com.ruiyu.taozhuma.model.TzmShopListModel;
import com.ruiyu.taozhuma.utils.LogUtil;
import com.ruiyu.taozhuma.utils.ProgressDialogUtil;
import com.ruiyu.taozhuma.utils.ToastUtils;
import com.ruiyu.taozhuma.utils.UserInfoUtils;
import com.ruiyu.taozhuma.widget.NoScrollViewPager;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.viewpagerindicator.TabPageIndicator;

import de.greenrobot.event.EventBus;

/**
 * 新改主界面
 * 
 * @author toby
 * 
 */
public class NewHomeFragment extends Fragment {
	private String TAG = "NewHomeFragment";
	private NoScrollViewPager viewPager;
	private ImageView img_search, imageViewtop;
	public static ArrayList<NavigationlistModel> model; //顶部导航条的数据
	private Handler mHandler = new Handler();
	NavigationListApi NavigationListApi;
	ApiClient client;
	static boolean stopThread = true;
	private String[] STR = { "上新", "0-3岁", "3-6岁", "6岁以上", "遥控", "早教", "过家家",
			"童车", "益智", "其他" };
	@ViewInject(R.id.tabGuide)
	private TabPageIndicator indicator;
	View view;
	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.ding_bu_dao_hang_lang_activity, null);
		ViewUtils.inject(this, view);
		initView(view);
		LogUtil.Log(TAG, TAG+"----->>>onCreateView");
		return view;
	}

	private void initView(View view) {

		img_search = (ImageView) view.findViewById(R.id.img_search);
		img_search.setOnClickListener(clickListener);
		imageViewtop = (ImageView) view.findViewById(R.id.imageViewtop);
		imageViewtop.setOnClickListener(clickListener);
		
		viewPager = (NoScrollViewPager) view.findViewById(R.id.viewpage);
		viewPager.setVisibility(View.GONE);
		viewPager.setNoScroll(false);// 设置可左右滑动
		ArrayList<Fragment> fragmentArray = new ArrayList<Fragment>();
		// 上新
		fragmentArray.add(new NewProductListFragment());
		int i;
		List<String> list = new ArrayList<String>();
		list.add("上新");
		try {
			viewPager.setVisibility(View.VISIBLE);

			for (i = 0; i < model.size(); i++) {
				list.add(model.get(i).name);
			}

			final int size = list.size();
			STR = (String[]) list.toArray(new String[size]);
			for (i = 0; i < model.size(); i++) {
				AppActivityListFragment fragment7 = new AppActivityListFragment();
				Bundle data7 = new Bundle();
				data7.putInt("type", model.get(i).level);
				data7.putInt("id", model.get(i).categoryId);
				fragment7.setArguments(data7);
				fragmentArray.add(fragment7);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		viewPager.setAdapter(new MyAdapter(getChildFragmentManager(),
				fragmentArray));
		// 设置当前显示的页面
		indicator.setViewPager(viewPager);
		indicator.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});

	}

	/**
	 * viewpager适配器
	 * 
	 * @author c_fei
	 * 
	 */
	class MyAdapter extends FragmentPagerAdapter {
		List<Fragment> list;

		public MyAdapter(FragmentManager fm, List<Fragment> list) {
			super(fm);
			this.list = list;
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int arg0) {
			return list.get(arg0);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			// TODO Auto-generated method stub
			return STR[position];
		}

	}

	View.OnClickListener clickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.img_search:
				Intent sintent = new Intent(getActivity(),
						TzmSearchActivity.class);
				startActivity(sintent);

				break;

			case R.id.imageViewtop:
				EventBus.getDefault().post(new ScrollEven());
				break;
			}
		}
	};

	@Override
	public void onResume() {

		super.onResume();
		
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	public void onPause() {
		super.onPause();
	}
}
