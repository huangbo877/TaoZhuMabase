package com.ruiyu.taozhuma.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.model.TzmAddressModel;

/*
 * fu
 * 
 * */
public class TzmAddressListAdapter2 extends BaseAdapter {
	private LayoutInflater layoutInflater;
	private List<TzmAddressModel> list;
	private Context context;
	//public static HashMap<Integer, Boolean> isSelected; 
	//private TzmSelectAddressApi tzmSelectAddressApi;
	//private ApiClient client;
	
	public TzmAddressListAdapter2(Context context, List<TzmAddressModel> list,Handler handler) {
		this.list = list;
		this.context = context;
		layoutInflater = LayoutInflater.from(context);
		//tzmSelectAddressApi=new TzmSelectAddressApi();
		//client=new ApiClient(context);
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

	@SuppressLint("InflateParams") @Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		final ViewHolder viewHolder;
		if (view == null) {
			view = layoutInflater.inflate(R.layout.tzm_address_list_item2, null);
			viewHolder = new ViewHolder();
			viewHolder.tv_username = (TextView) view.findViewById(R.id.tv_username);
			viewHolder.tv_address = (TextView) view.findViewById(R.id.tv_address);
			viewHolder.tv_phone = (TextView) view.findViewById(R.id.tv_phone);
			viewHolder.iv_tag_default=(ImageView) view.findViewById(R.id.iv_tag_default);
			viewHolder.grayline=(ImageView) view.findViewById(R.id.grayline);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag(); 
		}
		final TzmAddressModel info = this.list.get(position);
		viewHolder.tv_phone.setText(info.tel);
		viewHolder.tv_username.setText(info.name);
		viewHolder.tv_address.setText(info.address);
        if(position == list.size()-1){
        	viewHolder.grayline.setVisibility(View.GONE);	
        }else{
        	viewHolder.grayline.setVisibility(View.VISIBLE);	
        }
		if(info.isDefault == 0){
			viewHolder.iv_tag_default.setVisibility(View.GONE);
		}
		if(info.isDefault == 1){
			viewHolder.iv_tag_default.setVisibility(View.VISIBLE);
		}
//		viewHolder.tv_edit.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent(context,
//						TzmAddAdressActivity.class);
//				Bundle bundle = new Bundle();
//				bundle.putInt("addId", new Long(info.addId).intValue());//addId ==-1为添加地址  其他的为编辑地址
//				intent.putExtras(bundle);
//				context.startActivity(intent);
//			}
//		});
//		viewHolder.cb_isDefault.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				if (info.isDefault == 1) {
//					viewHolder.cb_isDefault.setChecked(true);
//				} else {
//					viewHolder.cb_isDefault.setChecked(false);
//					tzmSelectAddressApi.setAddId(info.addId);
//					tzmSelectAddressApi.setUid(UserInfoUtils.getUserInfo().uid);
//					client.api(tzmSelectAddressApi, new ApiListener() {
//						@Override
//						public void onStart() {
//						}
//
//						@Override
//						public void onError(String error) {
//							super.onError(error);
//						}
//
//						@Override
//						public void onException(Exception e) {
//							super.onException(e);
//						}
//
//						@Override
//						public void onComplete(String jsonStr) {
//							LogUtil.Log("jsonStr    " + jsonStr);
//							handler.sendEmptyMessage(1);
//						}
//					}, true);
//				}
//			}
//		});
		return view;
	}
	
	/**
	 * 
	 * 
	 * @author Toby
	 * 
	 */
	private class ViewHolder {
		TextView tv_username,tv_phone,tv_address;
		ImageView iv_tag_default,grayline;
	}
	
	
	/****
	 * 更新list
	 * @param ll
	 */
	public void updateList(List<TzmAddressModel> ll){
		this.list = ll;
		this.notifyDataSetChanged();
	}
}
