package com.ruiyu.taozhuma.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.utils.StringUtils;

/*
 性别选择对话框
 */
public class UserInfoSexDialog extends Dialog {

	public UserInfoSexDialog(Context context) {
		super(context);
	}

	public UserInfoSexDialog(Context context, int theme) {
		super(context, theme);
	}

	public static class Builder {
		private Context context;
		private View contentView;

		private RelativeLayout rl_sex1, rl_sex2;
		private LinearLayout ll_cancel;
		private DialogInterface.OnClickListener sex1ClickListener;
		private DialogInterface.OnClickListener sex2ClickListener;
		private DialogInterface.OnClickListener cancelClickListener;
		private TextView tv_male, tv_female;
		private String male, famale;

		public void setMale(String male) {
			this.male = male;
		}

		public void setFamale(String famale) {
			this.famale = famale;
		}

		public Builder(Context context) {
			this.context = context;
		}

		public Builder setContentView(View v) {
			this.contentView = v;
			return this;
		}

		/**
		 * Set the positive button resource and it's listener
		 * 
		 * @param positiveButtonText
		 * @return
		 */
		public Builder setSex1ClickListener(
				DialogInterface.OnClickListener listener) {
			this.sex1ClickListener = listener;
			return this;
		}

		public Builder setSex2ClickListener(
				DialogInterface.OnClickListener listener) {
			this.sex2ClickListener = listener;
			return this;
		}

		public Builder setCancelClickListener(
				DialogInterface.OnClickListener listener) {
			this.cancelClickListener = listener;
			return this;
		}

		public UserInfoSexDialog create() {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// instantiate the dialog with the custom Theme
			final UserInfoSexDialog dialog = new UserInfoSexDialog(context,
					R.style.DialogStyleBottom);
			View layout = inflater.inflate(R.layout.user_info_sex_dialog, null);
			dialog.addContentView(layout, new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			// set the dialog title
			rl_sex1 = (RelativeLayout) layout.findViewById(R.id.rl_sex1);
			rl_sex2 = (RelativeLayout) layout.findViewById(R.id.rl_sex2);
			ll_cancel = (LinearLayout) layout.findViewById(R.id.ll_cancel);
			tv_male = (TextView) layout.findViewById(R.id.tv_male);
			tv_female = (TextView) layout.findViewById(R.id.tv_female);
			if (StringUtils.isNotEmpty(male)) {
				tv_male.setText(male);
			}
			if (StringUtils.isNotEmpty(famale)) {
				tv_female.setText(famale);
			}
			if (sex1ClickListener != null) {

				rl_sex1.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						sex1ClickListener.onClick(dialog,
								DialogInterface.BUTTON_POSITIVE);

					}
				});

			}
			if (sex2ClickListener != null) {

				rl_sex2.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						sex2ClickListener.onClick(dialog,
								DialogInterface.BUTTON_NEGATIVE);

					}
				});

			}
			if (cancelClickListener != null) {

				ll_cancel.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						cancelClickListener.onClick(dialog,
								DialogInterface.BUTTON_NEGATIVE);

					}
				});

			}

			dialog.setContentView(layout);
			dialog.setCanceledOnTouchOutside(true);
			return dialog;
		}

	}
}
