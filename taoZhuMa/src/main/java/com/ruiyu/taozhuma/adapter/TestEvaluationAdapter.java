package com.ruiyu.taozhuma.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.lidroid.xutils.BitmapUtils;

import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.SelectPicActivity;
import com.ruiyu.taozhuma.R.drawable;
import com.ruiyu.taozhuma.R.id;
import com.ruiyu.taozhuma.R.layout;
import com.ruiyu.taozhuma.model.TzmEvaluationModel;
import com.ruiyu.taozhuma.model.TzmSearchAllModel.Shop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TestEvaluationAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<TzmEvaluationModel> list;
	private LayoutInflater layoutInflater;
	private BitmapUtils bitmapUtils;

	public TestEvaluationAdapter(Context context,
			ArrayList<TzmEvaluationModel> list) {
		this.context = context;
		this.list = list;
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
		final ViewHolder viewHolder;
		if (view == null) {
			view = layoutInflater.inflate(R.layout.act_item, null);
			viewHolder = new ViewHolder();
			// viewHolder.productName = (TextView)
			// view.findViewById(R.id.product_name);
			viewHolder.tv_p_name = (TextView) view
					.findViewById(R.id.tv_p_name);
			viewHolder.productImage = (ImageView) view
					.findViewById(R.id.product_image);
			viewHolder.product_image1 = (ImageView) view
					.findViewById(R.id.product_image1);
			viewHolder.product_image2 = (ImageView) view
					.findViewById(R.id.product_image2);
			viewHolder.product_image3 = (ImageView) view
					.findViewById(R.id.product_image3);
			viewHolder.rl_image1 = (RelativeLayout) view
					.findViewById(R.id.rl_image1);
			viewHolder.rl_image2 = (RelativeLayout) view
					.findViewById(R.id.rl_image2);
			viewHolder.rl_image3 = (RelativeLayout) view
					.findViewById(R.id.rl_image3);
			viewHolder.comment_content = (EditText) view
					.findViewById(R.id.comment_content);
			viewHolder.comment_content.setTag(list.get(position).detailId);
			viewHolder.tv_p_name.setText(list.get(position).productName);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		final TzmEvaluationModel info = this.list.get(position);
		// viewHolder.productName.setText(info.productName);

		OnClickListener clickListener = new OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				
				Intent intent1 = new Intent(context, SelectPicActivity.class);
				intent1.putExtra("imgId", v.getId());
				((Activity) context).startActivityForResult(intent1, v.getId());
				switch(v.getId())
				{
				case R.id.product_image1:					
					viewHolder.rl_image2.setVisibility(View.VISIBLE);
					viewHolder.product_image1.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.icon_default_logo)); 
					break;
				case R.id.product_image2:
					viewHolder.rl_image3.setVisibility(View.VISIBLE);
					viewHolder.product_image2.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.icon_default_logo)); 
					break;
				case R.id.product_image3:
					viewHolder.rl_image1.setVisibility(View.VISIBLE);
					viewHolder.product_image3.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.icon_default_logo)); 
					break;
				
				}
				//TestActivity.selectPic(v.getId());
			}
		};
		viewHolder.product_image1.setOnClickListener(clickListener);
		viewHolder.product_image2.setOnClickListener(clickListener);
		viewHolder.product_image3.setOnClickListener(clickListener);
		bitmapUtils.display(viewHolder.productImage, "" + info.productImage);
		return view;
	}

//	private void selectPic(int imgId) {
//		Intent intent1 = new Intent(context, SelectPicActivity.class);
//		intent1.putExtra("imgId", imgId);
//		((Activity) context).startActivityForResult(intent1, imgId);
//	}

	private class ViewHolder {
		TextView productName,tv_p_name;
		RelativeLayout rl_image1,rl_image2,rl_image3;
		ImageView productImage, product_image1,product_image2,product_image3;
		EditText comment_content;
	}
}
