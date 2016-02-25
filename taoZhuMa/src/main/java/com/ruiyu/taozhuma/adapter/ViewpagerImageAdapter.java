package com.ruiyu.taozhuma.adapter;

import java.util.List;

import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lidroid.xutils.BitmapUtils;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.base.xUtilsImageLoader;
import com.ruiyu.taozhuma.utils.AsyncImageLoaderUtil;
import com.ruiyu.taozhuma.utils.StringUtils;
import com.ruiyu.taozhuma.utils.AsyncImageLoaderUtil.ImageCallback;

public class ViewpagerImageAdapter extends PagerAdapter {
	private String[] urls;
	private List<View> list;
//	private AsyncImageLoaderUtil asyncImageLoader;
//	BitmapUtils bitmapUtils;
	private xUtilsImageLoader imageLoader;
	
	private ImageView image;

	public ViewpagerImageAdapter(List<View> list, String[] urls,xUtilsImageLoader imageLoader) {
		this.list = list;
		this.urls = urls;
//		asyncImageLoader = new AsyncImageLoaderUtil();
		this.imageLoader=imageLoader;
	}

	/**
	 * Return the number of views available.
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.list.size();
	}

	/**
	 * Remove a page for the given position. 滑动过后就销毁 ，销毁当前页的前一个的前一个的页！
	 * instantiateItem(View container, int position) This method was deprecated
	 * in API level . Use instantiateItem(ViewGroup, int)
	 */
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		container.removeView(this.list.get(position));

	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}

	/**
	 * Create the page for the given position.
	 */
	@Override
	public Object instantiateItem(final ViewGroup container, final int position) {

		
//		Drawable cachedImage = asyncImageLoader.loadDrawable(urls[position],
//				new ImageCallback() {
//
//					public void imageLoaded(Drawable imageDrawable,
//							String imageUrl) {
//
//						View view = list.get(position);
//						image = ((ImageView) view.findViewById(R.id.im_image));
//
//						if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//							image.setBackgroundDrawable(imageDrawable);
//						} else {
//							image.setBackground(imageDrawable);
//						}
//						container.removeView(list.get(position));
//						container.addView(list.get(position));
//						// adapter.notifyDataSetChanged();
//
//					}
//				});
//
		View view = list.get(position);
		image = ((ImageView) view.findViewById(R.id.im_image));
//		if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//			image.setBackgroundDrawable(cachedImage);
//		} else {
//			image.setBackground(cachedImage);
//		}
		if(StringUtils.isNotEmpty(urls[position])){
			imageLoader.display(image,
					urls[position]);
		}
		container.removeView(list.get(position));
		container.addView(list.get(position));
		// adapter.notifyDataSetChanged();

		return list.get(position);

	}

}