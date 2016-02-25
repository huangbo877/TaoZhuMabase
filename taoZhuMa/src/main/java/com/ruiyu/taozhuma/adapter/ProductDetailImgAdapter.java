package com.ruiyu.taozhuma.adapter;

import java.util.List;

import com.lidroid.xutils.BitmapUtils;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.base.xUtilsImageLoader;
import com.ruiyu.taozhuma.model.ProductDetailImageModel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class ProductDetailImgAdapter extends BaseAdapter {
	private LayoutInflater layoutInflater;
	private List<ProductDetailImageModel> list;
	private Context c;
	private int width;// 屏幕宽度
	BitmapUtils bitmapUtils;

	public ProductDetailImgAdapter(Context c,
			List<ProductDetailImageModel> list, int width) {
		this.list = list;
		this.c = c;
		this.width = width;
		layoutInflater = LayoutInflater.from(c);
		bitmapUtils = new BitmapUtils(c);
		bitmapUtils
				.configDefaultLoadingImage(R.drawable.loading_long_transparent);// 默认背景图片
		bitmapUtils
				.configDefaultLoadFailedImage(R.drawable.loading_long_transparent);// 加载失败图片
//		bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);// 设置图片压缩类型
//		bitmapUtils.configMemoryCacheEnabled(true);
//		bitmapUtils.configDiskCacheEnabled(true);
		bitmapUtils.configDefaultShowOriginal(true);
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

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		ViewHolder viewHolder;

		if (view == null) {
			view = layoutInflater.inflate(R.layout.product_detail_img_item,
					null);
			viewHolder = new ViewHolder();
			viewHolder.img = (ImageView) view.findViewById(R.id.im_im);
			viewHolder.ll_parent = (LinearLayout) view
					.findViewById(R.id.ll_parent);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		final ProductDetailImageModel info = list.get(position);

		// Log.i("=================", "宽:" + width + "高:" + width
		// * info.height / info.width);
		//

		try {
			LayoutParams lp = (LayoutParams) viewHolder.ll_parent
					.getLayoutParams();
			lp.height = width * info.height / info.width;
			viewHolder.ll_parent.setLayoutParams(lp);
			bitmapUtils.display(viewHolder.img, info.image);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return view;
	}

	/**
	 * 
	 * 
	 * @author Toby
	 * 
	 */
	private class ViewHolder {

		ImageView img;
		LinearLayout ll_parent;

	}
}
