package com.ruiyu.taozhuma.dialog;

import com.ruiyu.taozhuma.R;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.content.DialogInterface;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

public class CustomDialog extends Dialog {

	public CustomDialog(Context context) {
		super(context);
	}

	public CustomDialog(Context context, int theme) {
		super(context, theme);
	}

	public static class Builder {
		private Context context;
		private View contentView;

		private TextView tv_manufacturer, tv_consumer, tv_cancle;
		private DialogInterface.OnClickListener manufacturerClickListener;
		private DialogInterface.OnClickListener consumerClickListener;
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
		public Builder setManufacturerClickListener(
				DialogInterface.OnClickListener listener) {
			this.manufacturerClickListener = listener;
			return this;
		}

		public Builder setConsumerClickListener(
				DialogInterface.OnClickListener listener) {
			this.consumerClickListener = listener;
			return this;
		}

		public Builder setNegativeButton(
				DialogInterface.OnClickListener listener) {
			this.negativeButtonClickListener = listener;
			return this;
		}

		public CustomDialog create() {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// instantiate the dialog with the custom Theme
			final CustomDialog dialog = new CustomDialog(context,
					R.style.Dialog);
			View layout = inflater.inflate(R.layout.customer_dialog_layout,
					null);
			dialog.addContentView(layout, new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			// set the dialog title
			tv_manufacturer = (TextView) layout
					.findViewById(R.id.tv_manufacturer);
			tv_cancle = (TextView) layout.findViewById(R.id.tv_cancle);
			tv_consumer = (TextView) layout.findViewById(R.id.tv_consumer);

			if (manufacturerClickListener != null) {

				tv_manufacturer.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						manufacturerClickListener.onClick(dialog,
								DialogInterface.BUTTON_POSITIVE);

					}
				});

			}
			if (consumerClickListener != null) {

				tv_consumer.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						consumerClickListener.onClick(dialog,
								DialogInterface.BUTTON_POSITIVE);

					}
				});

			}

			if (negativeButtonClickListener != null) {
				tv_cancle.setOnClickListener(new View.OnClickListener() {
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