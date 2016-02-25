package com.ruiyu.taozhuma.fragment;


import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.activity.MainTabActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;

public class AdFragment031 extends Fragment implements OnTouchListener {

	@ViewInject(R.id.btn_go)
	private ImageView button;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View v = inflater.inflate(R.layout.adfragment031, null);
		ViewUtils.inject(this, v);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(getActivity(), MainTabActivity.class));
				getActivity().finish();

			}
		});
		return v;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
//		switch (event.getAction()) {
//		case MotionEvent.ACTION_DOWN: {
//			// 手指按下的X坐标
//			downX = event.getX();
//			v.getParent().requestDisallowInterceptTouchEvent(true);
//			break;
//		}
//		case MotionEvent.ACTION_UP: {
//			float lastX = event.getX();
//			if (lastX < downX) {
//				startActivity(new Intent(getActivity(), MainTabActivity.class));
//				getActivity().finish();
//			}
//		}
//		case MotionEvent.ACTION_CANCEL:
//			v.getParent().requestDisallowInterceptTouchEvent(false);
//			break;
//
//		}
		return false;
	}
}
