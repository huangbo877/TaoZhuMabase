/**
 * 
 */
package com.ruiyu.taozhuma.activity;

import java.util.ArrayList;
import java.util.List;

import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.adapter.TzmHelpCenterListAdapter;
import com.ruiyu.taozhuma.model.TzmHelpCenterListModel;
import com.ruiyu.taozhuma.model.TzmHelpCenterModel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @author 林尧 2015-10-13 帮助中心 -----常见问题的 list 显示
 */
public class ShowHelpChildListActivity extends Activity {

	public static TzmHelpCenterModel tzmHelpCenterModel; // 常见问题
	private List<TzmHelpCenterListModel> list;
	private TzmHelpCenterListAdapter adapter;
	private ListView lv_help_list;
	private TextView txt_head_title;
	private Button btn_head_left;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help_e_list_item);
		lv_help_list = (ListView) findViewById(R.id.lv_help_list);
		txt_head_title = (TextView) this.findViewById(R.id.txt_head_title);
		txt_head_title.setText("常见问题");
		System.out.println(tzmHelpCenterModel.son.size());
		list = new ArrayList<TzmHelpCenterListModel>();
		for (TzmHelpCenterModel.Son son : tzmHelpCenterModel.son) {
			TzmHelpCenterListModel model = new TzmHelpCenterListModel();
			model.id = son.sonId;
			model.title = son.sonName;
			list.add(model);
		}
		btn_head_left = (Button) this.findViewById(R.id.btn_head_left);
		btn_head_left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();

			}
		});
		adapter = new TzmHelpCenterListAdapter(ShowHelpChildListActivity.this,
				list);
		lv_help_list.setAdapter(adapter);
		lv_help_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				TzmHelpCenterListModel model  =(TzmHelpCenterListModel) parent.getAdapter().getItem(position);
				Intent intent2 = new Intent(ShowHelpChildListActivity.this, TzmHelpListActivity.class);
	        	intent2.putExtra("id", model.id );
	        	intent2.putExtra("title",model.title);
				startActivity(intent2);
			}
		});

	}

}
