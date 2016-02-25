package com.ruiyu.taozhuma.adapter;

import java.io.Serializable;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.activity.OrdersDetailActivity;
import com.ruiyu.taozhuma.activity.TzmLogisticsActivity;
import com.ruiyu.taozhuma.activity.TzmOrderCheckActivity;
import com.ruiyu.taozhuma.activity.TzmOrderCheckingActivity;
import com.ruiyu.taozhuma.activity.TzmReturnExpressLogisticsActivity;
import com.ruiyu.taozhuma.activity.TzmReturnLogisticsActivity;
import com.ruiyu.taozhuma.activity.TzmShopDetailActivity;
import com.ruiyu.taozhuma.base.BaseApplication;
import com.ruiyu.taozhuma.base.xUtilsImageLoader;
import com.ruiyu.taozhuma.model.TzmMyRefundModel;
import com.ruiyu.taozhuma.utils.StringUtils;

/**
 *
 *
 *
 * @author huangbo
 * 2015-11-10
 * 上午9:57:03
 */
public class TzmMyRefundAdapter extends BaseAdapter {
	private LayoutInflater layoutInflater;
	private Context context;
	private ArrayList<TzmMyRefundModel> list;
	View convertView[] = null;
	private xUtilsImageLoader imageLoader;

	public TzmMyRefundAdapter(Context context,
			ArrayList<TzmMyRefundModel> list, xUtilsImageLoader imageLoader) {
		this.context = context;
		this.list = list;
		layoutInflater = LayoutInflater.from(context);
		this.imageLoader = imageLoader;
	}

	@Override
	public int getCount() {
		return list.size();
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
	public View getView(final int position, View view, ViewGroup viewGroup) {
		ViewHolder viewHolder;
		final TzmMyRefundModel refundModel = this.list.get(position);
		if (view == null) {
			view = layoutInflater.inflate(R.layout.tzm_refund_list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.tv_shopName = (TextView) view
					.findViewById(R.id.tv_shopName);
			viewHolder.ll_refundDetail = (LinearLayout) view
					.findViewById(R.id.ll_refundDetail);
			viewHolder.tv_orderPrice = (TextView) view
					.findViewById(R.id.tv_orderPrice);
			viewHolder.iv_logo = (ImageView) view.findViewById(R.id.iv_logo);
			viewHolder.tv_orderStatus = (TextView) view
					.findViewById(R.id.tv_orderStatus);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		viewHolder.tv_shopName.setText(refundModel.shopName);
		viewHolder.tv_shopName.setTag(refundModel.shopId);

		// viewHolder.tv_orderPrice.setText("订单总价：¥ "+refundModel.orderPrice);
		// if(StringUtils.isNotEmpty(refundModel.shopLogo)){
		// imageLoader.display(viewHolder.iv_logo, refundModel.shopLogo);
		// }
		if (viewHolder.ll_refundDetail.getChildCount() > 0) {
			viewHolder.ll_refundDetail.removeAllViews();
		}
		// 退货状态，0=>未申请，1=>审核，2=>请退货，3=>退货中，4=>退货成功，5=>退货失败，6=>审核不成功，7=>退款成功
		if (refundModel.reStatus == 1) {
			viewHolder.tv_orderStatus.setText("审核中");
		} else if (refundModel.reStatus == 2) {
			viewHolder.tv_orderStatus.setText("请退货");
		} else if (refundModel.reStatus == 3) {
			viewHolder.tv_orderStatus.setText("退货中");
		} else if (refundModel.reStatus == 4) {
			viewHolder.tv_orderStatus.setText("退货成功");
		} else if (refundModel.reStatus == 5) {
			viewHolder.tv_orderStatus.setText("退货失败");
		} else if (refundModel.reStatus == 6) {
			viewHolder.tv_orderStatus.setText("申请失败");
		} else if (refundModel.reStatus == 7) {
			viewHolder.tv_orderStatus.setText("退款成功");
		}

		// if (refundModel.orderStatus == 1) {
		// viewHolder.tv_orderStatus.setText("待付款");
		// } else if (refundModel.orderStatus == 2) {
		// viewHolder.tv_orderStatus.setText("已付款");
		// } else if (refundModel.orderStatus == 3) {
		// viewHolder.tv_orderStatus.setText("已发货");
		// } else if (refundModel.orderStatus == 4) {
		// viewHolder.tv_orderStatus.setText("已收货");
		// } else if (refundModel.orderStatus == 5) {
		// viewHolder.tv_orderStatus.setText("已确定");
		// }
		if (refundModel.carts.size() != 0) {
			for (int index = 0; refundModel.carts.size() > index; index++) {
				convertView = new View[refundModel.carts.size()];
				convertView[index] = (View) layoutInflater.inflate(
						R.layout.tzm_refund_detail_item, null);
				convertView[index].setId(index);
				viewHolder.tv_productName = (TextView) convertView[index]
						.findViewById(R.id.tv_productName);
				viewHolder.tv_num = (TextView) convertView[index]
						.findViewById(R.id.tv_num);
				viewHolder.iv_productImage = (ImageView) convertView[index]
						.findViewById(R.id.iv_productImage);
				viewHolder.tv_product_price = (TextView) convertView[index]
						.findViewById(R.id.tv_product_price);
				viewHolder.tv_skuvalue = (TextView) convertView[index]
						.findViewById(R.id.tv_skuvalue);

				if (!StringUtils
						.isEmpty(refundModel.carts.get(index).productImage)) {
					imageLoader.display(viewHolder.iv_productImage,
							refundModel.carts.get(index).productImage);
				}
				viewHolder.tv_productName
						.setText(refundModel.carts.get(index).productName);

				viewHolder.tv_skuvalue
						.setText(refundModel.carts.get(index).skuValue);
				viewHolder.tv_product_price.setText("¥ "
						+ refundModel.carts.get(index).Price);
				viewHolder.tv_num.setText("x"
						+ refundModel.carts.get(index).productNumber);
				viewHolder.ll_refundDetail.addView(convertView[index]);
				viewHolder.ll_refundDetail.setTag(R.id.tv_orderPrice,
						refundModel.carts.get(index).productId);
				viewHolder.ll_refundDetail.setTag(R.id.tv_orderStatus,
						refundModel.carts.get(index).detailId);
			}
		}
		OnClickListener clickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.tv_shopName:
//					Intent intent = new Intent(context,
//							TzmShopDetailActivity.class);
//					intent.putExtra("id",
//							Integer.parseInt(v.getTag().toString()));
//					intent.putExtra("shopName", ((TextView) v).getText());
//					context.startActivity(intent);
					break;
				case R.id.ll_refundDetail:
					switch (refundModel.reStatus) {
					case 1:
						Intent orderDetailIntent1 = new Intent(context,
								TzmOrderCheckingActivity.class);
						orderDetailIntent1.putExtra("reStatus",
								refundModel.reStatus);
						orderDetailIntent1.putExtra("oid", Integer.parseInt(v
								.getTag(R.id.tv_orderStatus).toString()));
						orderDetailIntent1.putExtra("productId", Integer
								.parseInt(v.getTag(R.id.tv_orderPrice)
										.toString()));
						context.startActivity(orderDetailIntent1);
						break;
					case 2:
						Intent intent6 = new Intent(context,
								TzmReturnLogisticsActivity.class);

						intent6.putExtra("oid", Integer.parseInt(v.getTag(
								R.id.tv_orderStatus).toString()));
						context.startActivity(intent6);

						break;
					case 3:
						Intent intent7 = new Intent(context,
								TzmReturnExpressLogisticsActivity.class);

						intent7.putExtra("orderId", Integer.parseInt(v.getTag(
								R.id.tv_orderStatus).toString()));
						context.startActivity(intent7);

						break;
					case 4:
					case 5:
					case 6:
					case 7:
						Intent orderDetailIntent = new Intent(context,
								TzmOrderCheckActivity.class);
						orderDetailIntent.putExtra("reStatus",
								refundModel.reStatus);
						orderDetailIntent.putExtra("oid", Integer.parseInt(v
								.getTag(R.id.tv_orderStatus).toString()));
						orderDetailIntent.putExtra("productId", Integer
								.parseInt(v.getTag(R.id.tv_orderPrice)
										.toString()));
						context.startActivity(orderDetailIntent);
						break;

					default:
						break;
					}

					break;
				}

			}
		};
		viewHolder.tv_shopName.setOnClickListener(clickListener);
		viewHolder.ll_refundDetail.setOnClickListener(clickListener);
		return view;
	}

	private class ViewHolder {
		TextView tv_shopName, tv_productName, tv_num, tv_orderPrice,
				tv_orderStatus, tv_product_price, tv_skuvalue;
		ImageView iv_productImage, iv_logo;
		LinearLayout ll_refundDetail;
	}
}
