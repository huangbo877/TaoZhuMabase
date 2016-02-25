package com.ruiyu.taozhuma.adapter;

import java.util.List;

import com.lidroid.xutils.BitmapUtils;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.adapter.GroupAdapter.ViewHolder;
import com.ruiyu.taozhuma.base.xUtilsImageLoader;
import com.ruiyu.taozhuma.model.TzmProductModel;
import com.ruiyu.taozhuma.utils.LogUtil;
import com.ruiyu.taozhuma.utils.StringUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author Fu 类说明：产品Adapter类
 */
public class TzmProductAdapter extends BaseAdapter {
    private String TAG="TzmProductAdapter";
	private LayoutInflater layoutInflater;
	private List<TzmProductModel> list;
	private Context c;
	private xUtilsImageLoader imageLoader;

	public TzmProductAdapter(Context c, List<TzmProductModel> list,xUtilsImageLoader imageLoader) {
		this.list = list;
		this.c = c;
		layoutInflater = LayoutInflater.from(c);
		this.imageLoader = imageLoader;
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
			view = layoutInflater.inflate(R.layout.tzm_shop_detail_item, null);
			viewHolder = new ViewHolder();
			viewHolder.tv_name = (TextView) view.findViewById(R.id.tv_name);
			viewHolder.tv_price = (TextView) view.findViewById(R.id.tv_price);
			viewHolder.tv_sellNumber = (TextView) view.findViewById(R.id.tv_sellNumber);
			viewHolder.iv_picture = (ImageView) view
					.findViewById(R.id.iv_picture);
			// viewHolder.llv_product = (LinearLayoutForListView)
			// view.findViewById(R.id.llv_product);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		final TzmProductModel info = this.list.get(position);
		viewHolder.tv_name.setText(info.name);
		viewHolder.tv_price.setText("¥ " + info.price);
		viewHolder.tv_sellNumber.setText("销量:" + info.sellNumber);
		if(StringUtils.isNotEmpty(info.image)){
			imageLoader.display(viewHolder.iv_picture,info.image);
		}
		LogUtil.Log("TAG", "-专场活动ID:" +info.activityId);
		return view;
	}

	private class ViewHolder {
		TextView tv_name, tv_price,tv_sellNumber;
		ImageView iv_picture;
		// LinearLayoutForListView llv_product;
	}
}
