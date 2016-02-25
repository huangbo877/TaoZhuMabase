package com.ruiyu.taozhuma.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.lidroid.xutils.BitmapUtils;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.model.TzmEvaluationModel;
import com.ruiyu.taozhuma.model.TzmSearchAllModel.Shop;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class TzmEvaluationAdapter extends BaseAdapter{
	private Context context;
	private ArrayList<TzmEvaluationModel> list;
	private LayoutInflater layoutInflater;
	private BitmapUtils bitmapUtils;
	public TzmEvaluationAdapter(Context context,ArrayList<TzmEvaluationModel> list) {
		this.context=context;
		this.list=list;
		layoutInflater = LayoutInflater.from(context);
		bitmapUtils = new BitmapUtils(context);
		bitmapUtils.configDefaultLoadFailedImage(R.drawable.tzm_defult_img);
		bitmapUtils.configDefaultLoadingImage(R.drawable.tzm_defult_img);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return this.list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		ViewHolder viewHolder;
		if (view == null) {
			view = layoutInflater.inflate(R.layout.tzm_evaluation_item, null);
			viewHolder = new ViewHolder();
			viewHolder.productName = (TextView) view.findViewById(R.id.product_name);
			viewHolder.productImage = (ImageView) view.findViewById(R.id.product_image);
			viewHolder.comment_content=(EditText) view.findViewById(R.id.comment_content);
			viewHolder.comment_content.setTag(list.get(position).detailId);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		final TzmEvaluationModel info = this.list.get(position);
		viewHolder.productName.setText(info.productName);
		bitmapUtils.display(viewHolder.productImage, "" + info.productImage);
		return view;
	}
	private class ViewHolder {
		TextView productName;
		ImageView productImage;
		EditText comment_content;	
	}
}
