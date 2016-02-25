package com.ruiyu.taozhuma.dialog;

import com.ruiyu.taozhuma.R;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.content.DialogInterface;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

public class CustomDialog2 extends Dialog {

	public CustomDialog2(Context context) {
		super(context);
	}

	public CustomDialog2(Context context, int theme) {
		super(context, theme);
	}

	public static class Builder {
		private Context context;
		private View contentView;
		private String title;
		
		private TextView tv_dialog_title, tv_dialog_ok, tv_dialog_cancle;
		private DialogInterface.OnClickListener positiveClickListener;
		private DialogInterface.OnClickListener negativeClickListener;

		public Builder(Context context,String title) {
			this.context = context;
			this.title=title;
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
		public Builder setPositiveClickListener(
				DialogInterface.OnClickListener listener) {
			this.positiveClickListener = listener;
			return this;
		}

		public Builder setNegativeClickListener(
				DialogInterface.OnClickListener listener) {
			this.negativeClickListener = listener;
			return this;
		}

		public CustomDialog2 create() {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// instantiate the dialog with the custom Theme
			final CustomDialog2 dialog = new CustomDialog2(context,
					R.style.Dialog);
			View layout = inflater.inflate(R.layout.customer_dialog_layout2,
					null);
			dialog.addContentView(layout, new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			// set the dialog title
			tv_dialog_title = (TextView) layout
					.findViewById(R.id.tv_dialog_title);
			tv_dialog_title.setText(title);
			tv_dialog_ok = (TextView) layout.findViewById(R.id.tv_dialog_ok);
			tv_dialog_cancle = (TextView) layout.findViewById(R.id.tv_dialog_cancle);

			if (positiveClickListener != null) {

				tv_dialog_ok.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						positiveClickListener.onClick(dialog,
								DialogInterface.BUTTON_POSITIVE);

					}
				});

			}
			if (negativeClickListener != null) {

				tv_dialog_cancle.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						negativeClickListener.onClick(dialog,
								DialogInterface.BUTTON_POSITIVE);

					}
				});

			}

			dialog.setContentView(layout);
			dialog.setCanceledOnTouchOutside(true);
			return dialog;
		}

	}
}