package com.ruiyu.taozhuma.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ruiyu.taozhuma.R;

public class MemberExclusiveActivity extends Activity {
	@ViewInject(R.id.btn_head_left)
	private Button btn_head_left;
	@ViewInject(R.id.txt_head_title)
	private TextView txt_head_title;
	@ViewInject(R.id.iv_back)
	private ImageView iv_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.member_exclusive_activity);
		ViewUtils.inject(this);
		txt_head_title.setText("会员专享");
		btn_head_left.setOnClickListener(clickListener);
		iv_back.setOnClickListener(clickListener);
	}

	OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_head_left:
				onBackPressed();
				break;
			case R.id.iv_back:
				onBackPressed();
				break;

			}
		}
	};

}
