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

public class WarningDialog extends Dialog {

	public WarningDialog(Context context) {
		super(context);
	}

	public WarningDialog(Context context, int theme) {
		super(context, theme);
	}

	public static class Builder {
		private Context context;
		private View contentView;

		private Button btn_clcik, btn_cancel;
		private DialogInterface.OnClickListener positiveClickListener;
		private DialogInterface.OnClickListener negativeButtonClickListener;

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
		public Builder setPositiveClickListener(
				DialogInterface.OnClickListener listener) {
			this.positiveClickListener = listener;
			return this;
		}

		public Builder setNegativeButton(
				DialogInterface.OnClickListener listener) {
			this.negativeButtonClickListener = listener;
			return this;
		}

		public WarningDialog create() {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// instantiate the dialog with the custom Theme
			final WarningDialog dialog = new WarningDialog(context,
					R.style.Dialog);
			View layout = inflater.inflate(R.layout.warn_dialog_layout, null);
			dialog.addContentView(layout, new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			// set the dialog title
			btn_clcik = (Button) layout.findViewById(R.id.btn_clcik);
			btn_cancel = (Button) layout.findViewById(R.id.btn_cancel);

			if (positiveClickListener != null) {

				btn_clcik.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						positiveClickListener.onClick(dialog,
								DialogInterface.BUTTON_POSITIVE);

					}
				});

			}

			if (negativeButtonClickListener != null) {
				btn_cancel.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						negativeButtonClickListener.onClick(dialog,
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