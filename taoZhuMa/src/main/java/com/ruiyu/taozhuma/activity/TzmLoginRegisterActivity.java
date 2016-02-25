package com.ruiyu.taozhuma.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.ruiyu.taozhuma.LogUtil;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.fragment.TzmLoginFragment;
import com.ruiyu.taozhuma.fragment.TzmRegisterFragment;

public class TzmLoginRegisterActivity extends Activity {
	private String TAG = "TzmLoginRegisterActivity";
	private int type;
	private ImageView img_close, btn_change;
	private TzmLoginFragment tzmLoginFragment;
	private TzmRegisterFragment tzmRegisterFragment;
	private FragmentManager fragmentManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tzm_login_register_activity);
		LogUtil.Log(TAG, "onCreate");
		type = getIntent().getIntExtra("type", 0);// type==0 为登陆 type==1为注册
		fragmentManager = getFragmentManager();
		initView();
	}

	private void initView() {
		img_close = (ImageView) findViewById(R.id.img_close);
		btn_change = (ImageView) findViewById(R.id.btn_change);
		if (type == 0) {
			setTabSelection(0);
			btn_change.setImageResource(R.drawable.tab_login);
		} else {
			setTabSelection(1);
			btn_change.setImageResource(R.drawable.tab_regist);
		}
		img_close.setOnClickListener(clickListener);
		btn_change.setOnClickListener(clickListener);

	}

	View.OnClickListener clickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.img_close:
				onBackPressed();
				TzmLoginRegisterActivity.this.finish();
				break;
			case R.id.btn_change:
				if (type == 0) {
					setTabSelection(1);
					type = 1;
					btn_change.setImageResource(R.drawable.tab_regist);
				} else {
					setTabSelection(0);
					type = 0;
					btn_change.setImageResource(R.drawable.tab_login);
				}

				break;

			}
		}
	};

	private void setTabSelection(int index) {
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		hideFragments(transaction);
		switch (index) {
		case 0:

			if (tzmLoginFragment == null) {
				tzmLoginFragment = new TzmLoginFragment();
				transaction.add(R.id.flayout_content, tzmLoginFragment);
			} else {
				transaction.show(tzmLoginFragment);
			}
			break;
		case 1:
			if (tzmRegisterFragment == null) {
				tzmRegisterFragment = new TzmRegisterFragment();
				transaction.add(R.id.flayout_content, tzmRegisterFragment);
			} else {
				transaction.show(tzmRegisterFragment);
			}
			break;
		}
		transaction.commit();
	}

	private void hideFragments(FragmentTransaction transaction) {
		if (tzmLoginFragment != null) {
			transaction.hide(tzmLoginFragment);
		}
		if (tzmRegisterFragment != null) {
			transaction.hide(tzmRegisterFragment);
		}
	}
}
