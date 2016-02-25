/**
 * 
 */
package com.ruiyu.taozhuma.test;

import java.text.NumberFormat;
import java.util.List;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.base.xUtilsImageLoader;

/**
 * @author 林尧 2015-10-30
 */
public class ProductImageAdapter extends BaseAdapter {
	private String TAG="ProductImageAdapter";
	private LayoutInflater layoutInflater;
	private List<ProductImageModel> list;
	private xUtilsImageLoader imageLoader;
	private Context c;

	public ProductImageAdapter(Context c, List<ProductImageModel> list) {
		this.list = list;
		this.c = c;
		this.imageLoader=new xUtilsImageLoader(c);
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
			view = layoutInflater.inflate(R.layout.lv_imglist_item, null);
			viewHolder = new ViewHolder();
			viewHolder.img = (ImageView) view
					.findViewById(R.id.iv_show_picture_detail);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		final ProductImageModel info = this.list.get(position);
			int height=info.height;
		int width=info.width;
		Log.i(TAG,"原始图片的宽:"+width+"原始图片的高:"+height);
		// 创建一个数值格式化对象  
		NumberFormat numberFormat = NumberFormat.getInstance();  
		// 设置精确到小数点后2位  
		numberFormat.setMaximumFractionDigits(2);  
        //比例值
		String result = numberFormat.format((float) height / (float) width);  
		Log.i(TAG,"宽高比例"+result);
	    //获取屏幕的宽 像素
		DisplayMetrics dm =c.getResources().getDisplayMetrics();  
        int showWidth =dm.widthPixels-20;  
        //设置图片最终的高度
        float showHeight=showWidth*Float.parseFloat(result);
        
        LayoutParams lp; 
        lp=viewHolder.img.getLayoutParams();
        lp.width=showWidth;
        lp.height=(int)showHeight; 
        viewHolder.img.setLayoutParams(lp);
        Log.i(TAG, "图片的最终宽度"+showWidth+"图片的最终高度:"+showHeight);
        imageLoader.display(viewHolder.img, info.image);
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

	}
}
