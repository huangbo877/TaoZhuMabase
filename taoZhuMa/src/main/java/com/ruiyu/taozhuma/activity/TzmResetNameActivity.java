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

import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.utils.StringUtils;
import com.ruiyu.taozhuma.utils.ToastUtils;

public class TzmResetNameActivity extends Activity {

	public static final String USER_NAME = "userName";//

	private TextView txt_head_title;
	private Button btn_head_left, btn_head_right;
	private EditText et_name;
	private String userName = "";
	String digits = "/\\:*?<>|\"\n\t";
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
		txt_head_title = (TextView) findViewById(R.id.txt_head_title);
		txt_head_title.setText("修改昵称");
		btn_head_left = (Button) findViewById(R.id.btn_head_left);
		btn_head_left.setOnClickListener(clickListener);
		btn_head_right = (Button) findViewById(R.id.btn_head_right);
		btn_head_right.setOnClickListener(clickListener);
		btn_head_right.setTextColor(Color.parseColor("#e61e58"));
		btn_head_right.setText("保存");
		btn_head_right.setVisibility(View.VISIBLE);
		et_name = (EditText) findViewById(R.id.et_name);
		et_name.setText(userName);
		//et_name.addTextChangedListener(textWatcher);
	}

	OnClickListener clickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_head_left:
				onBackPressed();
				break;
			case R.id.btn_head_right:
				
				if(et_name.getText().toString().length()<2||et_name.getText().toString().length()>16){
					ToastUtils.showShortToast(TzmResetNameActivity.this,
							"昵称长度不符合要求！");
					return;
				}
				if(!StringUtils.isnc(et_name.getText().toString())){
					ToastUtils.showShortToast(TzmResetNameActivity.this,
							"昵称不符合要求！");
					return;
				}
				if (StringUtils.isNotEmpty(et_name.getText().toString())) {
					userName =et_name.getText().toString();
					resultIntent.putExtra(USER_NAME, userName);
					System.out.println("user_name  "+userName);
					setResult(Activity.RESULT_OK, resultIntent);
					finish();
				} else {
					ToastUtils.showShortToast(TzmResetNameActivity.this,
							"请输入昵称！");
				}
				break;
			}
		}
	};

//	//限2-16个字符，支持中英文、数字、下划线
//	private TextWatcher textWatcher = new TextWatcher()  {
//		// 监听改变的文本框
//		private int editStart;
//		private int editEnd;
//		private int maxLen = 16; // the max byte
//		  
//		@Override
//		public void onTextChanged(CharSequence ss, int start, int before,
//				int count) {
//			String editable = et_name.getText().toString();
//			String str = StringFilter(editable.toString());
//			if (!editable.equals(str)) {
//				et_name.setText(str);
//				// 设置新的光标所在位置
//				et_name.setSelection(str.length());
//			}
//		}
//
//		@Override
//		public void afterTextChanged(Editable s) {
//			editStart = et_name.getSelectionStart();
//		    editEnd = et_name.getSelectionEnd();
//		    // 先去掉监听器，否则会出现栈溢出
//		    et_name.removeTextChangedListener(textWatcher);
//		    if (!TextUtils.isEmpty(et_name.getText())) {
//		      String etstring = et_name.getText().toString().trim();
//		      while (calculateLength(s.toString()) > maxLen) {
//		        s.delete(editStart - 1, editEnd);
//		        editStart--;
//		        editEnd--;
//		      }
//		    }
//
//		    et_name.setText(s);
//		    et_name.setSelection(editStart);
//
//		    // 恢复监听器
//		    et_name.addTextChangedListener(textWatcher);
//		}
//
//		@Override
//		public void beforeTextChanged(CharSequence s, int start, int count,
//				int after) {
//
//		}
//
//	};
//	private int calculateLength(String etstring) {
//	    char[] ch = etstring.toCharArray();
//
//	    int varlength = 0;
//	    for (int i = 0; i < ch.length; i++) {
//	      // changed by zyf 0825 , bug 6918，加入中文标点范围 ， TODO 标点范围有待具体化
//	      if ((ch[i] >= 0x2E80 && ch[i] <= 0xFE4F) || (ch[i] >= 0xA13F && ch[i] <= 0xAA40) || ch[i] >= 0x80) { // 中文字符范围0x4e00 0x9fbb
//	        varlength = varlength + 2;
//	      } else {
//	        varlength++;
//	      }
//	    }
//	    return varlength;
//	  }
//	// 只允许中英文、数字、下划线
//	public static String StringFilter(String str) throws PatternSyntaxException {
//		String regEx = "[^a-zA-Z0-9\u4E00-\u9FA5_]";
//		Pattern p = Pattern.compile(regEx);
//		Matcher m = p.matcher(str);
//		return m.replaceAll("").trim();
//	}
	
}
