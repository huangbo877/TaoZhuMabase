/**
 * 
 */
package com.ruiyu.taozhuma.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.activity.ShanPingWebActivity;
import com.ruiyu.taozhuma.base.xUtilsImageLoader;
import com.ruiyu.taozhuma.model.SplashScreenModel;

/**
 * @author 林尧 2015-12-16
 */
public class AdFragment04 extends Fragment {

	private SplashScreenModel model;

	public AdFragment04(SplashScreenModel splashScreenModel) {
		this.model = splashScreenModel;
	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BitmapUtils bitmapUtils = new BitmapUtils(getActivity());
		bitmapUtils.configDefaultLoadingImage(R.drawable.tzmshangping);// 默认背景图片
		bitmapUtils.configDefaultLoadFailedImage(R.drawable.tzmshangping);// 加载失败图片
		View v = inflater.inflate(R.layout.adfragment04, null);
		ImageView imageView = (ImageView) v.findViewById(R.id.iv_shanping);
		imageView.setContentDescription(model.webLink);
		ImageView imageView2 = (ImageView) v.findViewById(R.id.iv_esc);
		imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// ImageView
				// imageView3=(ImageView)v.findViewById(R.id.iv_shanping);
				Intent intent = new Intent();
				intent.putExtra("link", model.webLink);
				intent.putExtra("title", model.title);
				intent.setClass(getActivity(), ShanPingWebActivity.class);
				startActivity(intent);
				getActivity().finish();
			}
		});
		imageView2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getActivity().finish();

			}
		});
		// 加载网络图片
		bitmapUtils.display(imageView, model.image_android);
		return v;
	}

	/**
	 * 获取网落图片资源
	 * 
	 * @param url
	 * @return
	 */
	/*
	 * public static Bitmap getHttpBitmap(String url){ URL myFileURL; Bitmap
	 * bitmap=null; try{ myFileURL = new URL(url); //获得连接 HttpURLConnection
	 * conn=(HttpURLConnection)myFileURL.openConnection();
	 * //设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
	 * conn.setConnectTimeout(6000); //连接设置获得数据流 conn.setDoInput(true); //不使用缓存
	 * conn.setUseCaches(false); //这句可有可无，没有影响 //conn.connect(); //得到数据流
	 * InputStream is = conn.getInputStream(); //解析得到图片 bitmap =
	 * BitmapFactory.decodeStream(is); //关闭数据流 is.close(); }catch(Exception e){
	 * e.printStackTrace(); }
	 * 
	 * return bitmap;
	 * 
	 * }
	 */

}
