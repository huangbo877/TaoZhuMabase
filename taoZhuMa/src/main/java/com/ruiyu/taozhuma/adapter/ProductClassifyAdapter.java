package com.ruiyu.taozhuma.adapter;

import java.util.ArrayList;

import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.activity.TzmProductListActivity;
import com.ruiyu.taozhuma.model.TzmProductTypeModel.Type;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
/*产品类型
 * fu
 * 
 * */ 
public class ProductClassifyAdapter extends BaseAdapter {
	private LayoutInflater layoutInflater;
	private ArrayList<Type> list;
	private Context c;

	public ProductClassifyAdapter(Context c, ArrayList<Type> list) {
		this.list = list;
		this.c = c;
		layoutInflater = LayoutInflater.from(c);
	}

	@Override
	public int getCount() {
		return this.list.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
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
			view = layoutInflater.inflate(R.layout.product_type_item, null);
			viewHolder = new ViewHolder();
			viewHolder.tv_type = (TextView) view.findViewById(R.id.tv_type);
			viewHolder.rl_detail = (RelativeLayout) view.findViewById(R.id.rl_detail);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		final Type info = this.list.get(position);
		
//		if(!StringUtils.isEmpty(info.image)){
//			BaseApplication.getInstance().getImageWorker()
//			.loadBitmap(info.image, viewHolder.hp_product_image );
//		}	
		viewHolder.tv_type.setText(info.name);
		viewHolder.rl_detail.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View paramView) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(c, TzmProductListActivity.class);
				intent.putExtra("typeId", info.id);
				intent.putExtra("title", info.name);
				c.startActivity(intent);
			}
		});
		return view;
	}

	/**
	 * 
	 * 
	 * @author Toby
	 * 
	 */
	private class ViewHolder {
		TextView tv_type;
		RelativeLayout rl_detail;
	}
}
