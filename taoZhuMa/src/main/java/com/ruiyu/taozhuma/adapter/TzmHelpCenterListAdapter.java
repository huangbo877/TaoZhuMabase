package com.ruiyu.taozhuma.adapter;

import java.util.ArrayList;
import java.util.List;

import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.base.BaseApplication;
import com.ruiyu.taozhuma.model.InformationMessageModel;
import com.ruiyu.taozhuma.model.TzmHelpCenterListModel;
import com.ruiyu.taozhuma.utils.StringUtils;

import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;

/**
 * 常见问题列表
 * 
 * @author fu 2015-04-17
 */
public class TzmHelpCenterListAdapter extends BaseAdapter {
	private LayoutInflater layoutInflater;
	private List<TzmHelpCenterListModel> list;
	private Context c;

	public TzmHelpCenterListAdapter(Context c, List<TzmHelpCenterListModel> list) {
		this.list = list;
		this.c = c;
		layoutInflater = LayoutInflater.from(c);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return this.list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		ViewHolder viewHolder;

		if (view == null) {
			view = layoutInflater.inflate(R.layout.tzm_help_list_item, null);
			viewHolder = new ViewHolder();

			viewHolder.tv_tittle = (TextView) view.findViewById(R.id.tv_tittle);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}

		TzmHelpCenterListModel info = this.list.get(position);
		viewHolder.tv_tittle.setText(info.title);

		return view;
	}

	/**
	 * 
	 * 
	 * @author libo
	 * 
	 */
	private class ViewHolder {
		public TextView tv_tittle;
	}
}
