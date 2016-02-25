package com.ruiyu.taozhuma.dialog;


import com.ruiyu.taozhuma.R;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.content.DialogInterface;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

public class CustomCommonDialog extends Dialog {

	public CustomCommonDialog(Context context) {
		super(context);
	}

	public CustomCommonDialog(Context context, int theme) {
		super(context, theme);
	}

	public static class Builder {
		private Context context;
		private View contentView;
		private String title;
		private String positiveButtonText;
		private String negativeButtonText;

		private TextView tv_tips;
		private Button btn_negativeButton, btn_positiveButton;
		private DialogInterface.OnClickListener positiveButtonClickListener;
		private DialogInterface.OnClickListener negativeButtonClickListener;

		public Builder(Context context) {
			this.context = context;
		}

		public Builder setContentView(View v) {
			this.contentView = v;
			return this;
		}

		public Builder setTitle(String title) {
			this.title = title;
			return this;
		}

		public Builder setPositiveButtonText(String positiveButtonText) {
			this.positiveButtonText = positiveButtonText;
			return this;
		}

		public Builder setNegativeButtonText(String negativeButtonText) {
			this.negativeButtonText = negativeButtonText;
			return this;
		}

		/**
		 * Set the positive button resource and it's listener
		 * 
		 * @param positiveButtonText
		 * @return
		 */
		public Builder setPositiveButton(
				DialogInterface.OnClickListener listener) {
			this.positiveButtonClickListener = listener;
			return this;
		}

		public Builder setNegativeButton(
				DialogInterface.OnClickListener listener) {
			this.negativeButtonClickListener = listener;
			return this;
		}

		public CustomCommonDialog create() {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// instantiate the dialog with the custom Theme
			final CustomCommonDialog dialog = new CustomCommonDialog(context,
					R.style.Dialog);
			View layout = inflater.inflate(
					R.layout.custom_common_dialog_layout, null);
			dialog.addContentView(layout, new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			// set the dialog title
			tv_tips = (TextView) layout.findViewById(R.id.tv_tips);
			btn_negativeButton = (Button) layout
					.findViewById(R.id.btn_negativeButton);
			btn_positiveButton = (Button) layout
					.findViewById(R.id.btn_positiveButton);

			if (positiveButtonClickListener != null) {

				btn_positiveButton
						.setOnClickListener(new View.OnClickListener() {
							public void onClick(View v) {
								positiveButtonClickListener.onClick(dialog,
										DialogInterface.BUTTON_POSITIVE);

							}
						});

			}
			if (negativeButtonClickListener != null) {

				btn_negativeButton
						.setOnClickListener(new View.OnClickListener() {
							public void onClick(View v) {
								negativeButtonClickListener.onClick(dialog,
										DialogInterface.BUTTON_NEGATIVE);

							}
						});

			}

			if (positiveButtonText != null) {
				btn_positiveButton.setText(positiveButtonText);
			}
			if (negativeButtonText != null) {
				btn_negativeButton.setText(negativeButtonText);
			}
			if (title != null) {
				tv_tips.setText(title);
			}

			dialog.setContentView(layout);
			dialog.setCanceledOnTouchOutside(true);
			return dialog;
		}

	}
}