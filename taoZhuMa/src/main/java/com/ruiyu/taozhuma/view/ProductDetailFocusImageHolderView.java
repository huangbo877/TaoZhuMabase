package com.ruiyu.taozhuma.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.CBPageAdapter;
import com.ruiyu.taozhuma.base.xUtilsImageLoader;
import com.ruiyu.taozhuma.model.TzmProductDetailModel;

/**
 * bannaer
 * 
 * @author Fu
 * 
 */
public class ProductDetailFocusImageHolderView implements
		CBPageAdapter.Holder<TzmProductDetailModel> {
	private ImageView imageView;
	private xUtilsImageLoader bitmapUtils;

	@Override
	public View createView(Context context) {
		// 你可以通过layout文件来创建，也可以像我一样用代码创建，不一定是Image，任何控件都可以进行翻页
		imageView = new ImageView(context);
		imageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		bitmapUtils = new xUtilsImageLoader(context);
		return imageView;
	}

	@Override
	public void UpdateUI(Context context, int position,
			TzmProductDetailModel data) {
		bitmapUtils.display(imageView, data.image);
	}
}
