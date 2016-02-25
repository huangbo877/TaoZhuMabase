package com.ruiyu.taozhuma.adapter;

import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.model.TzmMyCartModel.Cart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/*
 * fu
 * 
 * */
public class TzmCartChildListAdapter extends BaseAdapter {
	private LayoutInflater layoutInflater;
	public List<Cart> list;
	private Context c;
	//private BitmapUtils bitmapUtils;

	public TzmCartChildListAdapter(Context c, List<Cart> list,
			int fatherPosition) {
		this.list = list;
		this.c = c;
//		bitmapUtils = new BitmapUtils(c);
//		bitmapUtils.configDefaultLoadingImage(R.drawable.tzm_defult_img);
//		bitmapUtils.configDiskCacheEnabled(true);
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
			view = layoutInflater.inflate(R.layout.tzm_cart_child_item, null);
			viewHolder = new ViewHolder();
			viewHolder.tv_price = (TextView) view.findViewById(R.id.tv_price);
			viewHolder.tv_product_name = (TextView) view
					.findViewById(R.id.tv_product_name);
			viewHolder.et_num = (EditText) view.findViewById(R.id.et_num);
			viewHolder.iv_picture = (ImageView) view
					.findViewById(R.id.iv_picture);
			viewHolder.checkbox = (CheckBox) view.findViewById(R.id.checkbox);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		final Cart info = this.list.get(position);
		viewHolder.tv_product_name.setText(info.name);
		viewHolder.tv_price.setText("￥" + info.price);
		viewHolder.et_num.setText(info.number + "");
		// BaseApplication.getInstance().getImageWorker().loadBitmap(info.image,
		// viewHolder.iv_picture);
		ImageLoader.getInstance().displayImage(info.image, viewHolder.iv_picture);
//		if (!StringUtils.isEmpty(info.image)) {
//			BaseApplication.getInstance().getImageWorker()
//					.loadBitmap(info.image, viewHolder.iv_picture);
//		}
		//bitmapUtils.display(viewHolder.iv_picture, info.image);
		// BitmapUtils bitmapUtils = new BitmapUtils(c);
		// bitmapUtils.display(viewHolder.im_shoplist, info.image);
		// final OrderDetailModel info = this.list.get(position);
//		viewHolder.checkbox
//				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//					@Override
//					public void onCheckedChanged(CompoundButton buttonView,
//							boolean isChecked) {
//						if (isChecked) {
//							list.get(index).childCheck = true;
//							// 发布事件，在后台线程发的事件
//							EventBus.getDefault().post(
//									new CartChildEven(fatherPosition, index,
//											true));
//						} else {
//							list.get(index).childCheck = false;
//							// 发布事件，在后台线程发的事件
//							EventBus.getDefault().post(
//									new CartChildEven(fatherPosition, index,
//											false));
//						}
//
//					}
//				});
//		if (info.childCheck) {
//			viewHolder.checkbox.setChecked(true);
//		} else {
//			viewHolder.checkbox.setChecked(false);
//		}
		// LinearListOrderAffirmAdapter adapter = new
		// LinearListOrderAffirmAdapter(c, info.products);
		// viewHolder.llv_product.setAdapter(adapter);
		// viewHolder.hp_product_image.setOnClickListener(new OnClickListener()
		// {
		//
		// @Override
		// public void onClick(View paramView) {
		// // TODO Auto-generated method stub
		// Intent intent = new Intent(c, GoodsDetaliActivity.class);
		// intent.putExtra("id", info.id);
		// c.startActivity(intent);
		//
		// }
		// });

		return view;
	}

	/**
	 * 
	 * 
	 * @author Toby
	 * 
	 */
	private class ViewHolder {
		TextView tv_product_name, tv_price;
		ImageView iv_picture;
		EditText et_num;
		CheckBox checkbox;
		// LinearLayoutForListView llv_product;
	}
}
