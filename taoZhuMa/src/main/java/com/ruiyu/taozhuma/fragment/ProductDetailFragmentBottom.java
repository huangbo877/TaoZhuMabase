package com.ruiyu.taozhuma.fragment;

import java.util.ArrayList;
import java.util.List;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.widget.CustomViewPager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class ProductDetailFragmentBottom extends Fragment {

	@ViewInject(R.id.viewpager)
	private CustomViewPager viewpager;
	@ViewInject(R.id.radiogroup)
	private RadioGroup radiogroup;
	@ViewInject(R.id.rb_img)
	private RadioButton rb_img;
	@ViewInject(R.id.rb_comt)
	private RadioButton rb_comt;

	// private ProductDetailActivity activity;

	List<Fragment> fragments;
	ProductDetailImgFragment detailImgFragment;
	ProductDetailCommentFragment commentFragment;

	private boolean hasInited = true;

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View v = inflater
				.inflate(R.layout.product_detail_fragment_bottom, null);
		ViewUtils.inject(this, v);
		fragments = new ArrayList<Fragment>();
		detailImgFragment = new ProductDetailImgFragment();
		commentFragment = new ProductDetailCommentFragment();
		fragments.add(detailImgFragment);
		fragments.add(commentFragment);

		FragmentPagerAdapter adapter = new FragmentPagerAdapter(
				getChildFragmentManager()) {

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
		viewpager.setAdapter(adapter);
		radiogroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb_img:
					viewpager.setCurrentItem(0);
					break;
				case R.id.rb_comt:
					viewpager.setCurrentItem(1);
					break;
				}

			}
		});

		return v;
	}

	public void setComt(String count) {
		rb_comt.setText("买家口碑(" + count + ")");
	}

	public void loadData(Integer id) {
		if (hasInited) {
			// 只初始化一次
			detailImgFragment.loadData(id);
			commentFragment.loadData(id);
			hasInited = false;
		}

	}

	@Override
	public void onAttach(Activity activity) {
		// this.activity = (ProductDetailActivity) activity;
		super.onAttach(activity);
	}

	/**
	 * 星级控件平均分
	 */
	public void setfs(float pCmmt) {
		// TODO Auto-generated method stub
		commentFragment.initfs(pCmmt);
	}

}
