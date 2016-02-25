package com.ruiyu.taozhuma.adapter;

import java.util.List;

import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.activity.TzmHelpListActivity;
import com.ruiyu.taozhuma.model.InformationMessageModel;
import com.ruiyu.taozhuma.model.TzmFocusModel;
import com.ruiyu.taozhuma.model.TzmHelpCenterModel;
import com.ruiyu.taozhuma.model.TzmHelpCenterModel.Son;
import com.ruiyu.taozhuma.utils.ToastUtils;
import com.ruiyu.taozhuma.widget.AnimatedExpandableListView.AnimatedExpandableListAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ExAdapter extends AnimatedExpandableListAdapter {
	private LayoutInflater layoutInflater;
	private List<TzmHelpCenterModel> list;
	private Context c;
	
	private int[] image={R.drawable.tzm_251,R.drawable.tzm_252,R.drawable.tzm_253,R.drawable.tzm_254,R.drawable.tzm_255};

	public ExAdapter(Context c, List<TzmHelpCenterModel> list) {
		this.list = list;
		this.c = c;
		layoutInflater = LayoutInflater.from(c);
	}

	@Override
	public int getGroupCount() {
		return this.list.size();
	}

	@Override
	public int getRealChildrenCount(int groupPosition) {
		return this.list.get(groupPosition).son.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return this.list.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return this.list.get(groupPosition).son.get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = layoutInflater.inflate(
					R.layout.tzm_help_type_parent_item, null);
			viewHolder = new ViewHolder();
			viewHolder.tv_father_name = (TextView) convertView
					.findViewById(R.id.tv_father_name);
			viewHolder.image1 = (ImageView) convertView
					.findViewById(R.id.image1);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.tv_father_name.setText(list.get(groupPosition).fatherName);
		viewHolder.image1.setBackgroundResource(image[groupPosition]);
		// 判断实例可以展开，如果可以则改变右侧的图标
		// if (isExpanded)
		// image.setBackgroundResource(R.drawable.btn_browser2);
		// else
		// image.setBackgroundResource(R.drawable.btn_browser);

		return convertView;

	}

	@Override
	public View getRealChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = layoutInflater.inflate(
					R.layout.tzm_help_type_child_item, null);
			viewHolder = new ViewHolder();
			viewHolder.tv_child_name = (TextView) convertView
					.findViewById(R.id.tv_child_name);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final Son info = list.get(groupPosition).son
				.get(childPosition);
		viewHolder.tv_child_name.setText(info.sonName);
		viewHolder.tv_child_name.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(c, TzmHelpListActivity.class);
				intent.putExtra("id", info.sonId);
				intent.putExtra("title", info.sonName);
				c.startActivity(intent);
				
			}
		});
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		//ToastUtils.showShortToast(c, groupPosition+":"+childPosition);
		return false;
	}

	/**
	 * 
	 * 
	 * @author
	 * 
	 */
	private class ViewHolder {
		RelativeLayout rl_tw, rl_t;
		TextView tv_father_name, tv_child_name;
		ImageView im_picture,image1;
	}

	

}
