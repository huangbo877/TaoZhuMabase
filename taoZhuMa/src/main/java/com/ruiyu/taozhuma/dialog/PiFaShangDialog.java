/**
 * 
 */
package com.ruiyu.taozhuma.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.ruiyu.taozhuma.R;
/**
 * @author 林尧 2015-10-13
 */
public class PiFaShangDialog {
	Context context;
	Dialogcallback dialogcallback;
	Dialog dialog;
	TextView sure;
	
	

	/**
	 * init the dialog
	 * 
	 * @return
	 */
	public PiFaShangDialog(Context con) {
		this.context = con;
		dialog = new Dialog(context, R.style.Dialog);
		dialog.setContentView(R.layout.pi_fa_shang_diolog);
		
		sure = (TextView) dialog.findViewById(R.id.button1);
		
		sure.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
	}

	/**
	 * 设定一个interfack接口,使mydialog可以處理activity定義的事情
	 * 
	 * @author sfshine
	 * 
	 */
	public interface Dialogcallback {
		public void dialogdo(String string);
	}

	public void setDialogCallback(Dialogcallback dialogcallback) {
		this.dialogcallback = dialogcallback;
	}



	public void show() {
		dialog.show();
	}

	public void hide() {
		dialog.hide();
	}

	public void dismiss() {
		dialog.dismiss();
	}
}