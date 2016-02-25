package com.ruiyu.taozhuma.adapter;

import java.util.List;

import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.activity.ProductDetailActivity;
import com.ruiyu.taozhuma.activity.TzmLoginRegisterActivity;
import com.ruiyu.taozhuma.activity.TzmShopDetailActivity;
import com.ruiyu.taozhuma.base.BaseApplication;
import com.ruiyu.taozhuma.base.xUtilsImageLoader;
import com.ruiyu.taozhuma.model.TzmOrderProduct1Model;
import com.ruiyu.taozhuma.model.TzmOrderProductModel;
import com.ruiyu.taozhuma.utils.LogUtil;
import com.ruiyu.taozhuma.utils.StringUtils;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 选择地址产品列表 fu
 * 
 * */
public class TzmSelectAddressAdapter extends BaseAdapter {
	private String TAG="TzmSelectAddressAdapter";
	private LayoutInflater layoutInflater;
	private List<TzmOrderProduct1Model> list;
	private Context c;
	// private BitmapUtils bitmapUtils;
	View convertView[] = null;
	private xUtilsImageLoader imageLoader;

	public TzmSelectAddressAdapter(Context c, List<TzmOrderProduct1Model> orderProduct1) {
		this.list = orderProduct1;
		this.c = c;
		layoutInflater = LayoutInflater.from(c);
		imageLoader = new xUtilsImageLoader(c);
		// bitmapUtils = new BitmapUtils(c);
		// bitmapUtils.configDefaultLoadingImage(R.drawable.tzm_defult_img);//
		// 默认背景图片
		// bitmapUtils.configDefaultLoadFailedImage(R.drawable.tzm_defult_img);//
		// 加载失败图片
	}

	@Override
	public int getCount() {
		return this.list.size();

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
        LogUtil.Log(TAG, "getView");
		if (view == null) {
			view = layoutInflater.inflate(R.layout.tzm_select_product_item,
					null);
			viewHolder = new ViewHolder();
			viewHolder.tv_shopname = (TextView) view
					.findViewById(R.id.tv_shopname);
			viewHolder.et_message = (EditText) view
					.findViewById(R.id.et_message);
			viewHolder.ll_product = (LinearLayout) view
					.findViewById(R.id.ll_product);
			viewHolder.rl_topview=(RelativeLayout) view
			.findViewById(R.id.rl_topview);
			class MyTextWatcher implements TextWatcher {
				public MyTextWatcher(ViewHolder holder) {
					mHolder = holder;
				}

				/**
				 * 这里其实是缓存了一屏数目的viewholder， 也就是说一屏能显示10条数据，那么内存中就会有10个viewholder
				 * 在这的作用是通过edittext的tag拿到对应的position，用于储存edittext的值
				 */
				private ViewHolder mHolder;

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
				}

				@Override
				public void afterTextChanged(Editable s) {
					if (!TextUtils.isEmpty(s.toString())) {
						// 当文本发生变化时，就保存值到list对应的position中
						int position = (Integer) mHolder.et_message.getTag();
						list.get(position).content = s.toString();
						Log.i("afterTextChanged", "position" + position);
					}
				}
			}
			
			viewHolder.et_message.addTextChangedListener(new MyTextWatcher(
					viewHolder));

			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}

		viewHolder.et_message.setTag(position);

		final TzmOrderProduct1Model info = this.list.get(position);
		String value = info.content;
		if (!TextUtils.isEmpty(value)) {
			viewHolder.et_message.setText(value);
		} else {
			viewHolder.et_message.setText(null);
		}
		viewHolder.tv_shopname.setText(info.shopName);
		viewHolder.rl_topview.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				LogUtil.Log(TAG, "头部点击-"+"活动名称:"+info.shopName+"活动ID:"+info.shopId);
//				Intent intent = new Intent(c,
//						TzmShopDetailActivity.class);
//				intent.putExtra("name", info.shopName);
//				intent.putExtra("activityId", info.shopId);
//				c.startActivity(intent);
				
			}
		});
		// if (!StringUtils.isEmpty(info.shopLogo)) {
		// BaseApplication.getInstance().getImageWorker()
		// .loadBitmap(info.shopLogo, viewHolder.im_shoplogo);
		// }
		if (viewHolder.ll_product.getChildCount() > 0) {
			viewHolder.ll_product.removeAllViews();
		}
		if (info.products != null && info.products.size() != 0) {
			for (int i = 0; i < info.products.size();i++) {
				convertView = new View[info.products.size()];
				convertView[i] = (View) layoutInflater.inflate(
						R.layout.tzm_select_child_item, null);
				viewHolder.tv_product_name = (TextView) convertView[i]
						.findViewById(R.id.tv_product_name);
				viewHolder.iv_picture = (ImageView) convertView[i]
						.findViewById(R.id.iv_picture);
				viewHolder.tv_price = (TextView) convertView[i]
						.findViewById(R.id.tv_price);
				viewHolder.tv_number = (TextView) convertView[i]
						.findViewById(R.id.tv_number);
				viewHolder.tv_skuvalue = (TextView) convertView[i]
						.findViewById(R.id.tv_skuvalue);
			
				viewHolder.ll_main = (LinearLayout) convertView[i]
						.findViewById(R.id.ll_main);
				imageLoader.display(viewHolder.iv_picture,
						info.products.get(i).productImage);
				viewHolder.tv_product_name
						.setText(info.products.get(i).productName);
				viewHolder.tv_price.setText("¥ " + info.products.get(i).price);
				viewHolder.tv_number.setText("x"
						+ info.products.get(i).productNumber);
				viewHolder.tv_skuvalue.setText(info.products.get(i).skuValue);
				viewHolder.ll_product.addView(convertView[i]);
				//添加点击跳转 --跳转到商品详情页
				final int id=info.products.get(i).productId;
				final int activityid=Integer.parseInt(info.products.get(i).activityId);
				
				viewHolder.ll_main.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						LogUtil.Log(TAG, "子项点击:"+"商品ID:"+id+"活动ID:"+activityid);
						Intent intent = new Intent(c,
								ProductDetailActivity.class);
						intent.putExtra("id",id );
						intent.putExtra("activityId", activityid);
						c.startActivity(intent);
					}
				});
			}

		}

		return view;
	}

	public List<TzmOrderProduct1Model> getList() {
		return this.list;
	}

	/**
	 * 
	 * 
	 * @author Toby
	 * 
	 */
	private class ViewHolder {
		TextView tv_shopname;
		EditText et_message;
		LinearLayout ll_product,ll_main;
		ImageView iv_picture;
		TextView tv_product_name, tv_price, tv_number, tv_skuvalue;
		RelativeLayout rl_topview;
	}

}
