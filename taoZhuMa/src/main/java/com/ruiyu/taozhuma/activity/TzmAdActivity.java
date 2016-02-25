package com.ruiyu.taozhuma.activity;

import java.util.ArrayList;
import java.util.List;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.fragment.AdFragment01;
import com.ruiyu.taozhuma.fragment.AdFragment02;
import com.ruiyu.taozhuma.fragment.AdFragment03;
import com.ruiyu.taozhuma.fragment.AdFragment031;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/**
 * 广告页面
 * 
 * @author Administrator
 * 
 */

public class TzmAdActivity extends FragmentActivity implements OnTouchListener {

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
		fragments.add(new AdFragment01());
		fragments.add(new AdFragment02());
		fragments.add(new AdFragment03());
		fragments.add(new AdFragment031());
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
		pager.setOnTouchListener(this);
		pager.setAdapter(mAdapter);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			// 手指按下的X坐标
			downX = event.getX();
			// v.getParent().requestDisallowInterceptTouchEvent(true);
			break;
		}
		case MotionEvent.ACTION_UP: {
			float lastX = event.getX();
			// 抬起的时候的X坐标大于按下的时候就显示上一张图片
			if ((lastX < downX && next)||(lastX == downX && next)) {
				startActivity(new Intent(TzmAdActivity.this,
						MainTabActivity.class));
				finish();
			}

		}

			break;
		case MotionEvent.ACTION_CANCEL:
			// v.getParent().requestDisallowInterceptTouchEvent(false);
			break;
		}
		return false;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
}
