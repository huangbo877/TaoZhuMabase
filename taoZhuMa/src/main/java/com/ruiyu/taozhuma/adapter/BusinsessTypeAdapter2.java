package com.ruiyu.taozhuma.adapter;

import com.ruiyu.taozhuma.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

/*
 * Boo
 * 商家用户类型2
 * 2015-04-10
 * */
public class BusinsessTypeAdapter2 {
	public static int[] typeId = {0, 1,2 };
	private static ArrayAdapter<String> adapter;

	public static ArrayAdapter<String> getAdapter(Context c) {
		if (null == adapter) {
			String[] types = {c.getString(R.string.type_select), c.getString(R.string.type_personal),
					c.getString(R.string.type_enterprise)};
			
			adapter = new ArrayAdapter<String>(c,android.R.layout.simple_spinner_item,types);
			//设置下拉列表的风格  
	        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
		}
		return adapter;
	}

}
