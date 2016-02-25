/**
 * 
 */
package com.ruiyu.taozhuma.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.api.ApiClient;
import com.ruiyu.taozhuma.api.ApiListener;
import com.ruiyu.taozhuma.api.SplashScreenApi;
import com.ruiyu.taozhuma.fragment.AdFragment04;
import com.ruiyu.taozhuma.model.BaseModel;
import com.ruiyu.taozhuma.model.SplashScreenModel;
import com.ruiyu.taozhuma.utils.LogUtil;

/**
 * @author 林尧 2015-12-15
 */
public class ShanPingActivity extends FragmentActivity {

	private String TAG = "ShanPingActivity";

	@OnClick(R.id.iv_esc)
	// 退出
	private void iv_escClick(View v) {
		finish();
	}

	@ViewInject(R.id.viewpager)
	private ViewPager pager;

	private List<Fragment> fragments = new ArrayList<Fragment>();
	private FragmentPagerAdapter mAdapter;
	private boolean next = false;
	private float downX;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.tzm_ad_activity);
		ViewUtils.inject(this);
		// show_shanping();
		List<SplashScreenModel> SplashScreenModels = (List<SplashScreenModel>) getIntent()
				.getSerializableExtra("SplashScreenModels");
		for (int i = 0; i < SplashScreenModels.size(); i++) {
			fragments.add(new AdFragment04(SplashScreenModels.get(i)));
		}

		mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return fragments.size();
			}

			@Override
			public Fragment getItem(int arg0) {
				// TODO Auto-generated method stub
				return fragments.get(arg0);
			}
		};
		pager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				if (arg0 == fragments.size() - 1) {
					next = true;
				} else {
					next = false;
				}

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

		pager.setOffscreenPageLimit(SplashScreenModels.size());
		pager.setAdapter(mAdapter);
	}

	@Override
	public void onResume() {

		super.onResume();
	}

	@Override
	public void onPause() {

		super.onPause();
	}

}
