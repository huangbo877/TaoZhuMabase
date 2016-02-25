package com.ruiyu.taozhuma.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.model.UserCouponListModel;

/**
 * 代金卷
 * 
 * @author Fu
 * 
 */
public class UserCouponlListAdapter extends BaseAdapter {
	private LayoutInflater layoutInflater;
	private List<UserCouponListModel> list;
	private Context context;

	public UserCouponlListAdapter(Context context,
			List<UserCouponListModel> list) {
		this.list = list;
		this.context = context;
		layoutInflater = LayoutInflater.from(context);
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

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		ViewHolder viewHolder;
		if (view == null) {
			view = layoutInflater.inflate(R.layout.user_coupon_list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.tv_price = (TextView) view.findViewById(R.id.tv_price);
			viewHolder.tv_couponNo = (TextView) view
					.findViewById(R.id.tv_couponNo);
			viewHolder.tv_couponName = (TextView) view
					.findViewById(R.id.tv_couponName);
			viewHolder.tv_source = (TextView) view.findViewById(R.id.tv_source);
			viewHolder.tv_validEtime = (TextView) view
					.findViewById(R.id.tv_validEtime);
			viewHolder.ll_bg = (LinearLayout) view.findViewById(R.id.ll_bg);
			viewHolder.iv_status = (ImageView) view
					.findViewById(R.id.iv_status);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		UserCouponListModel model = list.get(position);
		viewHolder.tv_price.setText("¥ " + model.m);
		viewHolder.tv_couponNo.setText(model.couponNo);
		viewHolder.tv_couponName.setText("订单满" + model.om + "元可用");
		viewHolder.tv_validEtime.setText(model.validEtime);
		// 来源
		String source = null;
		switch (model.source) {
		case 1:
			source = "新人礼包";
			break;
		case 2:
			source = "首单礼包";
			break;
		case 3:
			source = "节日活动";
			break;
		case 4:
			source = "互动活动";
			break;
		case 5:
			source = "用户绑定";
			break;
		}
		viewHolder.tv_source.setText(source);
		// 可不可用
		if (model.used == 0 && model.overdue == 0) {
			viewHolder.ll_bg.setBackgroundResource(R.drawable.bg_daiji_hong);
			viewHolder.iv_status.setVisibility(View.GONE);
		} else {
			viewHolder.ll_bg.setBackgroundResource(R.drawable.bg_daijin);
			viewHolder.iv_status.setVisibility(View.VISIBLE);
			if (model.overdue == 1) {
				viewHolder.iv_status.setImageResource(R.drawable.tag_guoqi);
			} else if (model.used == 1) {
				viewHolder.iv_status.setImageResource(R.drawable.tag_used);
			}
		}
		return view;
	}

	private class ViewHolder {
		TextView tv_price, tv_couponNo, tv_couponName, tv_source,
				tv_validEtime;
		LinearLayout ll_bg;
		ImageView iv_status;
	}
}
