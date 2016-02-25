package com.ruiyu.taozhuma.utils;

import com.ruiyu.taozhuma.R;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 显示Toast的工具类
 * 
 * @author wuzx
 * 
 */
public class ToastUtils {
	private static Toast mToast;

	public static void showToast(Context context, String str) {

		View view = view = View.inflate(context, R.layout.toast, null);
		TextView txt = ((TextView) view.findViewById(R.id.txt_toast));
		if (mToast == null) {
			mToast = new Toast(context);
			txt.setText(str);
			mToast.setView(view);
			mToast.setDuration(Toast.LENGTH_LONG);
			mToast.setGravity(Gravity.CENTER, 0, 0);
		} else {
			txt.setText(str);
			mToast.setView(view);
		}
		mToast.show();

	}

	public static void showToast(Context context, int stringId) {
		View view = view = View.inflate(context, R.layout.toast, null);
		TextView txt = ((TextView) view.findViewById(R.id.txt_toast));
		if (mToast == null) {
			mToast = new Toast(context);
			txt.setText(context.getString(stringId));
			mToast.setView(view);
			mToast.setDuration(Toast.LENGTH_LONG);
			mToast.setGravity(Gravity.CENTER, 0, 0);
		} else {
			txt.setText(context.getString(stringId));
			mToast.setView(view);
		}
		mToast.show();
	}

	public static void showShortToast(Context context, String str) {
		View view = view = View.inflate(context, R.layout.toast, null);
		TextView txt = ((TextView) view.findViewById(R.id.txt_toast));
		if (mToast == null) {
			mToast = new Toast(context);
			txt.setText(str);
			mToast.setView(view);
			mToast.setDuration(Toast.LENGTH_SHORT);
			mToast.setGravity(Gravity.CENTER, 0, 0);
		} else {
			txt.setText(str);
			mToast.setView(view);
		}
		mToast.show();
	}

	public static void showShortToast(Context context, int stringId) {
		View view = view = View.inflate(context, R.layout.toast, null);
		TextView txt = ((TextView) view.findViewById(R.id.txt_toast));
		if (mToast == null) {
			mToast = new Toast(context);
			txt.setText(context.getString(stringId));
			mToast.setView(view);
			mToast.setDuration(Toast.LENGTH_SHORT);
			mToast.setGravity(Gravity.CENTER, 0, 0);
		} else {
			txt.setText(context.getString(stringId));
			mToast.setView(view);
		}
		mToast.show();
	}

}
