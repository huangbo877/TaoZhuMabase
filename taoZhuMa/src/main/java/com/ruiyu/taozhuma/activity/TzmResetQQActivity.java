package com.ruiyu.taozhuma.activity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.utils.StringUtils;
import com.ruiyu.taozhuma.utils.ToastUtils;

/**
 *
 *
 *
 * @author huangbo
 * 2015-11-10
 * 上午9:58:11
 */
public class TzmResetQQActivity extends Activity {

	public static final String USER_NAME = "userName";//

	private TextView txt_head_title,textView1;
	private Button btn_head_left, btn_head_right;
	private EditText et_name;
	private String userName = "";

	private Intent resultIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tzm_reset_name_activity);
		resultIntent = getIntent();
		if (resultIntent.getStringExtra(USER_NAME) != null) {
			userName = resultIntent.getStringExtra(USER_NAME);
		}
		initView();
	}

	private void initView() {
		textView1 = (TextView) findViewById(R.id.textView1);
		textView1.setText("限输入QQ号码");
		txt_head_title = (TextView) findViewById(R.id.txt_head_title);
		txt_head_title.setText("修改QQ号码");
		btn_head_left = (Button) findViewById(R.id.btn_head_left);
		btn_head_left.setOnClickListener(clickListener);
		btn_head_right = (Button) findViewById(R.id.btn_head_right);
		btn_head_right.setOnClickListener(clickListener);
		btn_head_right.setTextColor(Color.parseColor("#e61e58"));
		btn_head_right.setText("保存");
		btn_head_right.setVisibility(View.VISIBLE);
		et_name = (EditText) findViewById(R.id.et_name);
		et_name.setText(userName);et_name.setHint("请输入QQ号码");
		et_name.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
	}

	OnClickListener clickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_head_left:
				onBackPressed();
				break;
			case R.id.btn_head_right:

				if(et_name.getText().toString().length()<4||et_name.getText().toString().length()>20){
					ToastUtils.showShortToast(TzmResetQQActivity.this,
							"QQ号码长度不符合要求！");
					return;
				}
				if(!StringUtils.isNum(et_name.getText().toString())){
					ToastUtils.showShortToast(TzmResetQQActivity.this,
							"QQ号码不符合要求！");
					return;
				}
				if (StringUtils.isNotEmpty(et_name.getText().toString())) {
					userName = et_name.getText().toString();
					resultIntent.putExtra(USER_NAME, userName);
					System.out.println("user_name  "+userName);
					setResult(Activity.RESULT_OK, resultIntent);
					finish();
				} else {
					ToastUtils.showShortToast(TzmResetQQActivity.this,
							"请输入QQ号码！");
				}
				break;
			}
		}
	};

	
	
}
