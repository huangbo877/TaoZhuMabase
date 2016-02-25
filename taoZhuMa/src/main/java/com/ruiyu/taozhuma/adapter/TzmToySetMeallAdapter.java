///**
// * 
// */
//package com.ruiyu.taozhuma.adapter;
//
//import java.util.List;
//
//import android.content.Context;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.ruiyu.taozhuma.R;
//
//import com.ruiyu.taozhuma.base.xUtilsImageLoader;
//import com.ruiyu.taozhuma.model.TzmShopDetailModel;
//
///**
// * @author 林尧
// * 2015-10-24
// */
//public class TzmToySetMeallAdapter extends BaseAdapter {
//    
//	private String TAG="TzmToySetMeallAdapter";
//	private LayoutInflater layoutInflater;
//	private List<TzmShopDetailModel> list;
//	private Context c;
//	private xUtilsImageLoader bitmapUtils;
//
//	public TzmToySetMeallAdapter(Context c, List<TzmShopDetailModel> list) {
//		Log.i(TAG, TAG+"---->>TzmToySetMeallAdapter");
//		this.list = list;
//		this.c = c;
//		bitmapUtils = new xUtilsImageLoader(c);
//		layoutInflater = LayoutInflater.from(c);
//	}
//
//	public void setList(List<TzmShopDetailModel> list) {
//		this.list = list;
//
//	}
//
//	@Override
//	public int getCount() {
//		return this.list.size();
//	}
//
//	@Override
//	public Object getItem(int position) {
//		return this.list.get(position);
//	}
//
//	@Override
//	public long getItemId(int position) {
//		return position;
//	}
//
//	@Override
//	public View getView(int position, View view, ViewGroup viewGroup) {
//		ViewHolder viewHolder;
//		if (view == null) {
//			view = layoutInflater.inflate(R.layout.tzm_toy_set_meal_item, null);
//			viewHolder = new ViewHolder();
//			viewHolder.tv_name = (TextView) view.findViewById(R.id.tv_name);
//			viewHolder.tv_price = (TextView) view.findViewById(R.id.tv_price);
//			viewHolder.iv_picture = (ImageView) view
//					.findViewById(R.id.iv_picture);
//			//viewHolder.tv_sellNumber= (TextView) view.findViewById(R.id.tv_sellNumber);
//			// viewHolder.llv_product = (LinearLayoutForListView)
//			// view.findViewById(R.id.llv_product);
//			view.setTag(viewHolder);
//		} else {
//			viewHolder = (ViewHolder) view.getTag();
//		}
//		final TzmShopDetailModel info = this.list.get(position);
//		viewHolder.tv_name.setText(info.name);
//		viewHolder.tv_price.setText("¥ " + info.price);
//		bitmapUtils.display(viewHolder.iv_picture, "" + info.image);
//		//viewHolder.tv_sellNumber.setText("销量:"+info.sellNumber);
//		return view;
//	}
//
//	private class ViewHolder {
//		TextView tv_name, tv_price;
//		ImageView iv_picture;
//		// LinearLayoutForListView llv_product;
//	}
//}
