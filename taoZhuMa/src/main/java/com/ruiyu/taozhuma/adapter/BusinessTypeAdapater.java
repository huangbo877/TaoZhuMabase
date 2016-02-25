package com.ruiyu.taozhuma.adapter;


import com.ruiyu.taozhuma.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

/*
 * Boo
 * 商家用户类型
 * 2015-04-10
 * */
public class BusinessTypeAdapater extends BaseAdapter{

	//会员账户类型(512->供应商，256->采购商，128>C店店家，64->顾客)
	private int[] types = {R.string.type_select,R.string.type_producer,R.string.type_purchaser,R.string.type_shopper,R.string.type_customer};
	private int[] typeId = {0,512,256,128,64};
	private Context c;
	
	public BusinessTypeAdapater(Context c){
		this.c = c;
	} 
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.types.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return typeId[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		// 动态生成每个下拉项对应的View，每个下拉项View由LinearLayout  
        // 中包含一个ImageView及一个TextView构成  
        // 初始化LinearLayout  
        LinearLayout ll = new LinearLayout(c);  
        ll.setOrientation(LinearLayout.HORIZONTAL);  
        // 初始化TextView  
        TextView tv = new TextView(c);  
        tv.setText(c.getResources().getText(types[position]));  
        tv.setTextSize(16);  
        ll.addView(tv);  
        return ll;  
	}

}
