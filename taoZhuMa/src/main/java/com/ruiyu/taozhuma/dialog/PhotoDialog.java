package com.ruiyu.taozhuma.dialog;

import com.ruiyu.taozhuma.R;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

public class PhotoDialog extends Dialog {

	public PhotoDialog(Context context) {
		super(context);
	}
	
	public PhotoDialog(Context context, int theme) {
		super(context, theme);
	}
	public static class Builder {
		private Context context;
		private View contentView;
		
		private TextView tv_photograph,tv_picture,tv_cancle;
		private DialogInterface.OnClickListener photographClickListener;
		private DialogInterface.OnClickListener pictureClickListener;
		private DialogInterface.OnClickListener negativeButtonClickListener;
		public Builder(Context context) {
			this.context=context;
		}
		public Builder setContentView(View v) {
			this.contentView=v;
			return this;
		}
		public Builder setManufacturerClickListener(
				DialogInterface.OnClickListener listener) {
			this.photographClickListener = listener;
			return this;
		}

		public Builder setConsumerClickListener(
				DialogInterface.OnClickListener listener) {
			this.pictureClickListener = listener;
			return this;
		}

		public Builder setNegativeButton(
				DialogInterface.OnClickListener listener) {
			this.negativeButtonClickListener = listener;
			return this;
		}
		public PhotoDialog create(){
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// instantiate the dialog with the custom Theme
			final PhotoDialog dialog = new PhotoDialog(context,
					R.style.Dialog);
			View layout = inflater.inflate(R.layout.photo_dialog_layout,
					null);
			dialog.addContentView(layout, new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			// set the dialog title
			tv_photograph = (TextView) layout
					.findViewById(R.id.tv_photograph);
			tv_cancle = (TextView) layout.findViewById(R.id.tv_cancle);
			tv_picture = (TextView) layout.findViewById(R.id.tv_picture);

			if (photographClickListener != null) {

				tv_photograph.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						photographClickListener.onClick(dialog,
								DialogInterface.BUTTON_POSITIVE);

					}
				});

			}
			if (pictureClickListener != null) {

				tv_picture.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						pictureClickListener.onClick(dialog,
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
