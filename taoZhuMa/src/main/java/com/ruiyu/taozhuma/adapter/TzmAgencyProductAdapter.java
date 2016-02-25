package com.ruiyu.taozhuma.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
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
import android.widget.TextView;

import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.base.xUtilsImageLoader;
import com.ruiyu.taozhuma.model.AgencyProductsModel;
import com.ruiyu.taozhuma.utils.StringUtils;

public class TzmAgencyProductAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private Context context;
	private ArrayList<AgencyProductsModel> list;

	// public static HashMap<Integer, Integer> number;

	private List<Map<String, String>> mData;
	public Map<Integer, EditText> editorValue = new HashMap<Integer, EditText>();//
	private String etValue = "value";
	String strNum = "1";
	xUtilsImageLoader imageLoader;

	public TzmAgencyProductAdapter(Context context,
			ArrayList<AgencyProductsModel> list, List<Map<String, String>> mData) {
		this.list = list;
		this.context = context;
		inflater = LayoutInflater.from(context);
		// this.number = number;
		this.mData = mData;
		imageLoader = new xUtilsImageLoader(context);
		// initData();
	}

	// private void initData() {
	// for (AgencyProductsModel model : list) {
	// mData.add(new HashMap<String, String>());
	// }
	// }

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
	public View getView(final int position, View view, ViewGroup viewGroup) {
		final ViewHolder viewHolder;
		if (view == null) {
			view = inflater.inflate(R.layout.agency_product_item, null);
			viewHolder = new ViewHolder();
			viewHolder.tv_product_name = (TextView) view
					.findViewById(R.id.tv_product_name);
			viewHolder.tv_price = (TextView) view.findViewById(R.id.tv_price);
			viewHolder.iv_picture = (ImageView) view
					.findViewById(R.id.iv_picture);
			viewHolder.im_reduce = (ImageView) view
					.findViewById(R.id.im_reduce);
			viewHolder.im_add = (ImageView) view.findViewById(R.id.im_add);

			viewHolder.et_num = (EditText) view.findViewById(R.id.et_num);

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
						int position = (Integer) mHolder.et_num.getTag();
						mData.get(position).put(etValue, s.toString()); //
						Log.i("afterTextChanged", "position" + position);
					}
				}
			}

			viewHolder.et_num.addTextChangedListener(new MyTextWatcher(
					viewHolder));
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		viewHolder.et_num.setTag(position);
		final AgencyProductsModel info = this.list.get(position);
		viewHolder.tv_product_name.setText(info.name);
		viewHolder.tv_price.setText("¥ " + info.price);
		viewHolder.et_num.setText(mData.get(position).get(etValue) + "");
		imageLoader.display(viewHolder.iv_picture, info.image);
		viewHolder.im_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// number.put(position, number.get(position) + 1);
				// viewHolder.et_num.setText(number.get(index) + "");

				editorValue.get(position).requestFocus();
				strNum = mData.get(position).get(etValue);
				// strNum = editorValue.get(position).getText().toString();
				Log.i("edittext", position + "");
				if (StringUtils.isPositiveNum(strNum)) {
					int intnum = Integer.parseInt(strNum);
					intnum++;
					editorValue.get(position).setText("" + intnum);
				}
			}
		});
		String value = mData.get(position).get(etValue);
		if (!TextUtils.isEmpty(value)) {
			viewHolder.et_num.setText(value);
		} else {
			viewHolder.et_num.setText("0");
		}
		editorValue.put(position, viewHolder.et_num);
		viewHolder.im_reduce.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				strNum = editorValue.get(position).getText().toString();
				if (StringUtils.isPositiveNum(strNum)) {
					int intnum = Integer.parseInt(strNum);
					intnum--;
					if (StringUtils.isPositiveNum(intnum + "")) {
						editorValue.get(position).setText("" + intnum);
					}
				}
			}
		});
		return view;
	}

	private class ViewHolder {
		TextView tv_product_name, tv_price;
		ImageView iv_picture, im_reduce, im_add;
		EditText et_num;
	}

}
