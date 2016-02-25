package com.ruiyu.taozhuma.adapter;

import java.util.List;

import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.model.InformationMessageModel;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class NoticeListAdapter2 extends BaseAdapter {
	private LayoutInflater layoutInflater;
	private List<InformationMessageModel> list;
	private Context c;

	public NoticeListAdapter2(Context c, List<InformationMessageModel> list) {
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
			view = layoutInflater.inflate(R.layout.notice_list_item, null);
			viewHolder = new ViewHolder();
//			viewHolder.img_image = (ImageView) view
//					.findViewById(R.id.img_image);
			viewHolder.tv_title = (TextView) view.findViewById(R.id.tv_title);	
			viewHolder.tv_addtime = (TextView) view.findViewById(R.id.tv_addtime);
			viewHolder.tv_content = (TextView) view.findViewById(R.id.tv_content);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		InformationMessageModel info = this.list.get(position);

//		if(!StringUtils.isEmpty(info.image)){
//			BaseApplication.getInstance().getImageWorker()
//			.loadBitmap(info.image, viewHolder.img_image );
//		}	

		viewHolder.tv_title.setText(info.title);
		viewHolder.tv_addtime.setText(info.addtime);
		viewHolder.tv_content.setText(info.content);
		
		return view;
	}

	/**
	 * 
	 * 
	 * @author Toby
	 * 
	 */
	private class ViewHolder {
		TextView tv_title,tv_addtime,tv_content;//
		
	}
}
