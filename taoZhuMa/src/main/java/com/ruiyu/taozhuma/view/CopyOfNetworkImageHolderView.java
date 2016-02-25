package com.ruiyu.taozhuma.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.CBPageAdapter;
import com.lidroid.xutils.BitmapUtils;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.activity.GetFocusActivityProductActivity;
import com.ruiyu.taozhuma.activity.InfoDetailActivity;
import com.ruiyu.taozhuma.activity.ProductDetailActivity;
import com.ruiyu.taozhuma.activity.TzmShopDetailActivity;
import com.ruiyu.taozhuma.model.SplashScreenModel;
import com.ruiyu.taozhuma.model.TzmFocusModel;
/**
 * bannaer
 * @author Fu
 *
 */
public class CopyOfNetworkImageHolderView implements
		CBPageAdapter.Holder<SplashScreenModel> {
	private ImageView imageView;
	private BitmapUtils bitmapUtils;

	@Override
	public View createView(Context context) {
		// 你可以通过layout文件来创建，也可以像我一样用代码创建，不一定是Image，任何控件都可以进行翻页
		imageView = new ImageView(context);
		bitmapUtils = new BitmapUtils(context);
		bitmapUtils.configDefaultLoadingImage(R.drawable.loading_long);// 默认背景图片
		bitmapUtils.configDefaultLoadFailedImage(R.drawable.loading_long);// 加载失败图片
		bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);// 设置图片压缩类型
		bitmapUtils.configMemoryCacheEnabled(true);
		bitmapUtils.configDiskCacheEnabled(true);
		imageView.setScaleType(ImageView.ScaleType.FIT_XY);
		
		//imageView.setFocusable(false);
		return imageView;
	}

	@Override
	public void UpdateUI( Context context, int position,
		 SplashScreenModel data) {
		bitmapUtils.display(imageView, data.image_ios4);
//		imageView.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				if ((data.subType) == 1) {
//					Intent intent1 = new Intent(context,
//							ProductDetailActivity.class);
//					intent1.putExtra("id", data.id);
//					context.startActivity(intent1);
//				} else if ((data.subType) == 2) {
//					Intent intent2 = new Intent(context,
//							TzmShopDetailActivity.class);
//					intent2.putExtra("id", data.id);
//					intent2.putExtra("shopName", "店铺详情");
//					context.startActivity(intent2);
//				}else if (data.subType == 3) {
//				//详情
//					Intent intent3 = new Intent(context,
//							GetFocusActivityProductActivity.class);
//					intent3.putExtra("id", data.id);
//					context.startActivity(intent3);
//				}
//				else if ((data.subType) == 4) {
//					//咨询内容
//					Intent intent4 = new Intent(context,
//							InfoDetailActivity.class);
//					intent4.putExtra("id", data.id);
//					context.startActivity(intent4);
//				}
//			}
//		});
	}
}
