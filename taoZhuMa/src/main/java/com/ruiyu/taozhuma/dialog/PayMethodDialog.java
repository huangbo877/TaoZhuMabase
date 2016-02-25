package com.ruiyu.taozhuma.dialog;

import com.ruiyu.taozhuma.R;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.content.DialogInterface;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PayMethodDialog extends Dialog {

	public PayMethodDialog(Context context) {
		super(context);
	}

	public PayMethodDialog(Context context, int theme) {
		super(context, theme);
	}

	public static class Builder {
		private Context context;
		private TextView tv_alipay, tv_wechat, tv_cancle, tv_wallet;
		private LinearLayout ll_wallet;
		private DialogInterface.OnClickListener method1ClickListener;
		private DialogInterface.OnClickListener method2ClickListener;
		private DialogInterface.OnClickListener cancleClickListener;
		private DialogInterface.OnClickListener walletClickListener;
		private boolean paywallet = false;

		public void setWalletPay(boolean paywallet) {
			this.paywallet = paywallet;
		}

		public Builder(Context context) {
			this.context = context;
		}

		/**
		 * Set the positive button resource and it's listener
		 * 
		 * @param positiveButtonText
		 * @return
		 */

		public Builder setALiPayClickListener(
				DialogInterface.OnClickListener listener) {
			this.method1ClickListener = listener;
			return this;
		}

		public Builder setWeChatClickListener(
				DialogInterface.OnClickListener listener) {
			this.method2ClickListener = listener;
			return this;
		}

		public Builder setcancleClickListener(
				DialogInterface.OnClickListener listener) {
			this.cancleClickListener = listener;
			return this;
		}

		public Builder setwalletClickListener(
				DialogInterface.OnClickListener listener) {
			this.walletClickListener = listener;
			return this;
		}

		@SuppressLint("InflateParams")
		public PayMethodDialog create() {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// instantiate the dialog with the custom Theme
			final PayMethodDialog dialog = new PayMethodDialog(context,
					R.style.Dialog);
			View layout = inflater.inflate(R.layout.pay_method_dialog_layout,
					null);
			dialog.addContentView(layout, new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			// set the dialog title
			tv_alipay = (TextView) layout.findViewById(R.id.tv_alipay);
			tv_wechat = (TextView) layout.findViewById(R.id.tv_wechat);
			tv_cancle = (TextView) layout.findViewById(R.id.tv_cancle);
			ll_wallet = (LinearLayout) layout.findViewById(R.id.ll_wallet);
			tv_wallet = (TextView) layout.findViewById(R.id.tv_wallet);
			if (!paywallet) {
				ll_wallet.setVisibility(View.GONE);
			}

			if (method1ClickListener != null) {

				tv_alipay.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						method1ClickListener.onClick(dialog,
								DialogInterface.BUTTON_POSITIVE);

					}
				});

			}
			if (method2ClickListener != null) {

				tv_wechat.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						method2ClickListener.onClick(dialog,
								DialogInterface.BUTTON_POSITIVE);

					}
				});

			}

			if (cancleClickListener != null) {
				tv_cancle.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						cancleClickListener.onClick(dialog,
								DialogInterface.BUTTON_NEGATIVE);
					}
				});

			}
			if (walletClickListener != null) {
				tv_wallet.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						walletClickListener.onClick(dialog,
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