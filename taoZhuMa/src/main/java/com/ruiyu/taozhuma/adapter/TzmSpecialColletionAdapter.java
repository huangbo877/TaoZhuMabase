/**
 * 
 */
package com.ruiyu.taozhuma.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lidroid.xutils.BitmapUtils;
import com.ruiyu.taozhuma.R;

import com.ruiyu.taozhuma.activity.TzmShopDetailActivity;
import com.ruiyu.taozhuma.activity.TzmSpecialCollectionActivity;
import com.ruiyu.taozhuma.base.xUtilsImageLoader;
import com.ruiyu.taozhuma.model.TzmCollectModel;
import com.ruiyu.taozhuma.utils.LogUtil;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

/**
 * @author 林尧 2015-12-1
 */
public class TzmSpecialColletionAdapter extends BaseAdapter {

	private String TAG = "TzmSpecialColletionAdapter";
	private LayoutInflater layoutInflater;
	private Context context;
	private List<TzmCollectModel> list;
	private BitmapUtils bitmapUtils;
	// private xUtilsImageLoader imageLoader;
	private Map<Integer, Boolean> isCheckMap = new HashMap<Integer, Boolean>();// 保存checkbox的状态,用于list的全选全不选

	private List<Map<Object, Object>> allcheckObj = new ArrayList<Map<Object, Object>>(); // 存放被选择的子项

	public TzmSpecialColletionAdapter(Context context,
			List<TzmCollectModel> list) {
		LogUtil.Log(TAG, "TzmSpecialColletionAdapter");
		this.context = context;
		this.list = list;
		layoutInflater = LayoutInflater.from(context);
		bitmapUtils = new BitmapUtils(context);
		bitmapUtils.configDefaultLoadFailedImage(R.drawable.loding_defult);
		bitmapUtils.configDefaultLoadingImage(R.drawable.loding_defult);
		// 初始化,默认都没有选中
		configCheckMap(false);
	}

	/**
	 * 首先,默认情况下,所有项目都是没有选中的.这里进行初始化
	 */
	public void configCheckMap(boolean bool) {

		for (int i = 0; i < list.size(); i++) {
			isCheckMap.put(i, bool);
		}

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
	public View getView(final int position, View view, ViewGroup viewGroup) {
		ViewHolder viewHolder;
		LogUtil.Log(TAG, "getView");
		if (view == null) {
			view = layoutInflater.inflate(
					R.layout.tzm_special_collection_list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.rl_main_special_collection = (RelativeLayout) view
					.findViewById(R.id.rl_main_special_collection);
			viewHolder.tv_tittle_special_colleciton = (TextView) view
					.findViewById(R.id.tv_tittle_special_colleciton);
			viewHolder.tv_status_special_collection = (TextView) view
					.findViewById(R.id.tv_status_special_collection);
			viewHolder.iv_picture_sepcial_collection = (ImageView) view
					.findViewById(R.id.iv_picture_sepcial_collection);
			viewHolder.cb_special_collection = (CheckBox) view
					.findViewById(R.id.cb_special_collection);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}

		final TzmCollectModel info = this.list.get(position);
		viewHolder.tv_tittle_special_colleciton.setText(info.favName);
		System.out.println(info.status+">>>>>>>>>");
		if (info.status.equals(1)) {
			viewHolder.tv_status_special_collection.setText("进行中");
		} else {
			viewHolder.tv_status_special_collection.setText("已结束");
		}
		bitmapUtils.display(viewHolder.iv_picture_sepcial_collection,
				info.favImage);

		if (TzmSpecialCollectionActivity.isBox) {
			viewHolder.cb_special_collection.setVisibility(View.VISIBLE);
		} else {
			viewHolder.cb_special_collection.setVisibility(View.GONE);
		}

		// 获得该item 是否允许删除
		boolean canRemove = info.isCanRemove();
		viewHolder.cb_special_collection
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						/*
						 * 将选择项加载到map里面寄存
						 */
						Log.i(TAG, "checkbox 状态变为" + isChecked);
						isCheckMap.put(position, isChecked);
						if (isChecked == true) {
							Map<Object, Object> map = new HashMap<Object, Object>();
							map.put(info.activityId, info);
							allcheckObj.add(map);
							System.out.println(allcheckObj.size());
						}
						if (isChecked == false) {
							for (int i = 0; i < allcheckObj.size(); i++) {
								if (allcheckObj.get(i).containsKey(
										info.activityId)) {
									allcheckObj.remove(i);
								}
							}
							System.out.println(allcheckObj.size());
						}
					}

				});

		if (!canRemove) {
			// 隐藏单选按钮,因为是不可删除的
			 viewHolder.cb_special_collection.setVisibility(View.GONE);
			viewHolder.cb_special_collection.setChecked(false);
		} else {
			// viewHolder.cb_special_collection.setVisibility(View.VISIBLE);

			if (isCheckMap.get(position) == null) {
				isCheckMap.put(position, false);
			}
		}
		viewHolder.cb_special_collection.setChecked(isCheckMap.get(position));

		// 跳到具体的专场
		viewHolder.rl_main_special_collection
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						Intent intent = new Intent(context,
								TzmShopDetailActivity.class);
						intent.putExtra("name", info.favName);
						intent.putExtra("activityId", info.activityId.toString());
						context.startActivity(intent);

						;

					}
				});

		return view;

	}

	// 删除操作
	public void remove(int position) {
		this.list.remove(position);

	}

	// 获取 多个checkbox 状态的 map
	public Map<Integer, Boolean> getCheckMap() {
		return this.isCheckMap;
	}

	private class ViewHolder {
		RelativeLayout rl_main_special_collection;
		TextView tv_tittle_special_colleciton, tv_status_special_collection;
		ImageView iv_picture_sepcial_collection;
		CheckBox cb_special_collection;
	}

	// 最终的选择结果 --并且进行删除操作
	public void ClickResult(final Context ctx) {

		if (allcheckObj.size() == 0) {
			AlertDialog.Builder builder = new Builder(ctx);
			builder.setMessage("没有选择删除项");
			builder.show();

		} else {
			AlertDialog.Builder builder = new Builder(ctx);
			builder.setMessage("确认删除吗？");
			builder.setTitle("提示");
			builder.setPositiveButton("确认",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// 被删除的专场对象
							StringBuilder str = new StringBuilder();
							System.out.println("最终大小:" + allcheckObj.size());
							for (int i = 0; i < allcheckObj.size(); i++) {
								for (java.util.Map.Entry<Object, Object> entry : allcheckObj
										.get(i).entrySet()) {
									TzmCollectModel model = (TzmCollectModel) entry
											.getValue();
									LogUtil.Log(TAG, "专场的属性favId:"
											+ model.favId + "--favItem:"
											+ model.favItem + "--favName:"
											+ model.favName + "--favoriteId:"
											+ model.favoriteId + "--favPrice:"
											+ model.favPrice + "--activityId:"
											+ model.activityId);
									str.append(model.favId + ",");
								}
							}
							// 请求参数
							/*
							 * String str2 = str.subSequence(0, str.length() -
							 * 1) .toString();
							 */
							LogUtil.Log(TAG, "被删除的专场的ID" + str);
							TzmSpecialCollectionActivity tzmSpecialCollectionActivity = (TzmSpecialCollectionActivity) ctx;
							tzmSpecialCollectionActivity.deletaFavorite(str);
						}
					});
			builder.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
			builder.create().show();

		}

	}

}
