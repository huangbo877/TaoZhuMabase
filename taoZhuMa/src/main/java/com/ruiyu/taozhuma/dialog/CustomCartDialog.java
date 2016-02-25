package com.ruiyu.taozhuma.dialog;

import com.ruiyu.taozhuma.R;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.content.DialogInterface;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CustomCartDialog extends Dialog {
	
	public CustomCartDialog(Context context) {
		super(context);
	}

	public CustomCartDialog(Context context, int theme) {
		super(context, theme);
	}

	public static class Builder {
		private Context context;

		private RelativeLayout rl_detail, rl_delete;
		private DialogInterface.OnClickListener detailClickListener;
		private DialogInterface.OnClickListener deleteClickListener;

		public Builder(Context context) {
			this.context = context;
		}


		/**
		 * Set the positive button resource and it's listener
		 * 
		 * @param positiveButtonText
		 * @return
		 */
		public Builder setDetailClickListener(
				DialogInterface.OnClickListener listener) {
			this.detailClickListener = listener;
			return this;
		}

		public Builder setDdeleteClickListener(
				DialogInterface.OnClickListener listener) {
			this.deleteClickListener = listener;
			return this;
		}

		public CustomCartDialog create() {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// instantiate the dialog with the custom Theme
			final CustomCartDialog dialog = new CustomCartDialog(context,
					R.style.Dialog);
			View layout = inflater.inflate(R.layout.cart_dialog_layout, null);
			dialog.addContentView(layout, new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			// set the dialog title
			rl_detail = (RelativeLayout) layout.findViewById(R.id.rl_detail);
			rl_delete = (RelativeLayout) layout.findViewById(R.id.rl_delete);

			if (detailClickListener != null) {

				rl_detail.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						detailClickListener.onClick(dialog,
								DialogInterface.BUTTON_POSITIVE);

					}
				});

			}
			if (deleteClickListener != null) {

				rl_delete.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						deleteClickListener.onClick(dialog,
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