package com.ruiyu.taozhuma.adapter;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.activity.AgentOrdersDetailActicity;
import com.ruiyu.taozhuma.api.ApiClient;
import com.ruiyu.taozhuma.api.ApiListener;
import com.ruiyu.taozhuma.api.TzmsetAgencyPushApi;
import com.ruiyu.taozhuma.base.xUtilsImageLoader;
import com.ruiyu.taozhuma.model.TzmPushOrderModel;
import com.ruiyu.taozhuma.utils.LogUtil;
import com.ruiyu.taozhuma.utils.StringUtils;
import com.ruiyu.taozhuma.utils.ToastUtils;

public class TzmPushOrderListAdapter extends BaseAdapter {
	private LayoutInflater layoutInflater;
	private ArrayList<TzmPushOrderModel> list;
	private Context context;
	private int j = 0;
	View convertView[] = null;
	private ApiClient apiClient;
	private TzmsetAgencyPushApi api;
	xUtilsImageLoader imageLoader;

	// int mark;

	public TzmPushOrderListAdapter(Context context,
			ArrayList<TzmPushOrderModel> list) {
		this.list = list;
		this.context = context;
		// this.mark=mark;
		layoutInflater = LayoutInflater.from(context);
		imageLoader = new xUtilsImageLoader(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return this.list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public void removeItem(int position) {
		this.list.remove(position);
		this.notifyDataSetChanged();// 刷新ListView
	}

	public void changeStatus(int position, int status) {
		// this.list.get(position).orderStatus = 4;
		this.list.get(position).pushStatus = status;
		this.notifyDataSetChanged();// 刷新ListView
	}

	// 取消订单
	// public void cancelOrder(int oid, final int index) {
	// tzmCancelOrderApi = new TzmCancelOrderApi();
	// apiClient2 = new ApiClient(context);
	// tzmCancelOrderApi.setOid(oid);
	// apiClient2.api(this.tzmCancelOrderApi, new ApiListener() {
	// @Override
	// public void onStart() {
	// }
	//
	// @Override
	// public void onComplete(String jsonStr) {
	// if (StringUtils.isNotBlank(jsonStr)) {
	// try {
	// JSONObject jsonObject = new JSONObject(jsonStr);
	// boolean success = jsonObject.optBoolean("success");
	// Integer result = jsonObject.optInt("result");
	// if (success) {
	// if (result == 1) {
	// // String success_msg = "删除成功";
	// // ToastUtils.showShortToast(context,success_msg);
	// removeItem(index);
	// } else {
	// String success_msg = "删除失败";
	// ToastUtils.showShortToast(context, success_msg);
	// }
	// }
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	// }
	// }
	//
	// @Override
	// public void onError(String error) {
	// ToastUtils.showShortToast(context, R.string.msg_list_null);
	// }
	//
	// @Override
	// public void onException(Exception e) {
	// LogUtil.ErrorLog(e);
	// }
	// }, true);
	//
	// }

	// 确认订单
	// public void comfireOrder(int oid, int uid, final int index2) {
	// tzmEditOrderStatusApi = new TzmEditOrderStatusApi();
	// apiClient = new ApiClient(context);
	// tzmEditOrderStatusApi.setUid(uid);
	// tzmEditOrderStatusApi.setOid(oid);
	// tzmEditOrderStatusApi.setStatus(4);
	// apiClient.api(this.tzmEditOrderStatusApi, new ApiListener() {
	// @Override
	// public void onStart() {
	// }
	//
	// @Override
	// public void onComplete(String jsonStr) {
	// if (StringUtils.isNotBlank(jsonStr)) {
	// try {
	// JSONObject jsonObject = new JSONObject(jsonStr);
	// boolean success = jsonObject.optBoolean("success");
	// Integer result = jsonObject.optInt("result");
	// if (success) {
	// if (result == 1) {
	// changeStatus(index2);
	// } else {
	// String success_msg = "确认失败";
	// ToastUtils.showShortToast(context, success_msg);
	// }
	// }
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	// }
	// }
	//
	// @Override
	// public void onError(String error) {
	// ToastUtils.showShortToast(context, R.string.msg_list_null);
	// }
	//
	// @Override
	// public void onException(Exception e) {
	// LogUtil.ErrorLog(e);
	// }
	// }, true);
	//
	// }

	private void getorder(Integer orderId, final Integer agencyId,
			final int position) {
		api = new TzmsetAgencyPushApi();
		apiClient = new ApiClient(context);
		api.setAgencyId(agencyId);
		api.setOrderId(orderId);
		apiClient.api(this.api, new ApiListener() {
			@Override
			public void onStart() {
			}

			@Override
			public void onComplete(String jsonStr) {
				if (StringUtils.isNotBlank(jsonStr)) {
					try {
						JSONObject jsonObject = new JSONObject(jsonStr);
						boolean success = jsonObject.optBoolean("success");
						Integer result = jsonObject.optInt("result");
						String error_msg = jsonObject.optString("error_msg");
						if (success) {
							int status = 2;
							if (result == 0) {
								status = 1;
							} else if (result == 1) {
								status = 2;
							} else if (result == 2) {
								status = 1;
							}
							changeStatus(position, status);
							// success_msg = "确认失败";
							ToastUtils.showShortToast(context, error_msg);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}

			@Override
			public void onError(String error) {
				ToastUtils.showShortToast(context, error);
			}

			@Override
			public void onException(Exception e) {
				LogUtil.ErrorLog(e);
			}
		}, true);
	}

	@Override
	public View getView(final int position, View view, ViewGroup viewGroup) {
		ViewHolder viewHolder;
		final TzmPushOrderModel orderItem = this.list.get(position);
		if (view == null) {
			view = layoutInflater.inflate(R.layout.tzm_push_order_list_item,
					null);
			viewHolder = new ViewHolder();
			viewHolder.ll_orderDetail = (LinearLayout) view
					.findViewById(R.id.ll_orderDetail);
			viewHolder.tv_shopName = (TextView) view
					.findViewById(R.id.tv_shopName);
			viewHolder.tv_orderPrice = (TextView) view
					.findViewById(R.id.tv_orderPrice);
			viewHolder.btn_status1 = (Button) view
					.findViewById(R.id.btn_status1);
			viewHolder.btn_status2 = (Button) view
					.findViewById(R.id.btn_status2);
			viewHolder.btn_status3 = (Button) view
					.findViewById(R.id.btn_status3);
			// viewHolder.btn_daifahuo = (Button)
			// view.findViewById(R.id.btn_daifahuo);
			// viewHolder.btn_comfire = (Button)
			// view.findViewById(R.id.btn_comfire);
			// viewHolder.btn_yiping = (Button)
			// view.findViewById(R.id.btn_yiping);
			// viewHolder.btn_fukuang = (Button)
			// view.findViewById(R.id.btn_fukuang);
			viewHolder.ll_orderDetail = (LinearLayout) view
					.findViewById(R.id.ll_orderDetail);
			viewHolder.btn_status1.setTag(position);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}

		if (orderItem.pushStatus == 0) {
			viewHolder.btn_status1.setVisibility(View.VISIBLE);
			viewHolder.btn_status2.setVisibility(View.GONE);
			viewHolder.btn_status3.setVisibility(View.GONE);
		} else if (orderItem.pushStatus == 1) {
			viewHolder.btn_status1.setVisibility(View.GONE);
			viewHolder.btn_status2.setVisibility(View.VISIBLE);
			viewHolder.btn_status3.setVisibility(View.GONE);
		} else if (orderItem.pushStatus == 2) {
			viewHolder.btn_status1.setVisibility(View.GONE);
			viewHolder.btn_status2.setVisibility(View.GONE);
			viewHolder.btn_status3.setVisibility(View.VISIBLE);
		}
		viewHolder.tv_shopName.setText(orderItem.shopName + "");
		viewHolder.tv_shopName.setTag(orderItem.shopId);

		viewHolder.tv_orderPrice.setText("小计：¥ " + orderItem.orderPrice);

		if (viewHolder.ll_orderDetail.getChildCount() > 0) {
			viewHolder.ll_orderDetail.removeAllViews();
		}
		if (orderItem.carts.size() != 0) {
			for (int index = 0; index < orderItem.carts.size(); index++) {
				convertView = new View[orderItem.carts.size()];
				convertView[index] = (View) layoutInflater.inflate(
						R.layout.tzm_order_detail_item, null);
				j = index;
				convertView[index].setId(j);
				viewHolder.tv_productName = (TextView) convertView[index]
						.findViewById(R.id.tv_productName);
				viewHolder.iv_productImage = (ImageView) convertView[index]
						.findViewById(R.id.iv_productImage);
				viewHolder.tv_price = (TextView) convertView[index]
						.findViewById(R.id.tv_price);
				viewHolder.tv_num = (TextView) convertView[index]
						.findViewById(R.id.tv_num);
				imageLoader.display(viewHolder.iv_productImage,
						orderItem.carts.get(index).productImage);
				viewHolder.tv_productName
						.setText(orderItem.carts.get(index).productName);
				viewHolder.tv_price.setText("¥ "
						+ orderItem.carts.get(index).price);
				viewHolder.tv_num
						.setText(orderItem.carts.get(index).productNumber + "件");
				/**
				 * 测试代码 convertView[index].setOnClickListener(new
				 * OnClickListener() { public void onClick(View v) { // TODO
				 * Auto-generated method stub
				 * System.out.println(convertView[j].getId());
				 * Toast.makeText(context, "第"+position+"组 第"+j+"行",
				 * Toast.LENGTH_SHORT).show(); } });
				 */
				viewHolder.ll_orderDetail.addView(convertView[index]);
			}
		}

		System.out.println("count:=====:"
				+ viewHolder.ll_orderDetail.getChildCount());
		if (viewHolder.ll_orderDetail.getChildCount() >= 2) {
			TextView tv1, tv2;
			tv1 = (TextView) viewHolder.ll_orderDetail.getChildAt(
					viewHolder.ll_orderDetail.getChildCount() - 1)
					.findViewById(R.id.tv_productName);
			tv2 = (TextView) viewHolder.ll_orderDetail.getChildAt(
					viewHolder.ll_orderDetail.getChildCount() - 2)
					.findViewById(R.id.tv_productName);
			System.out.println("count:=====:" + tv1.getText().toString());
			System.out.println("count:=====:" + tv2.getText().toString());
		}

		OnClickListener clickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// // TODO Auto-generated method stub
				switch (v.getId()) {
				case R.id.btn_status1:
					// final int index = (Integer) v.getTag();
					AlertDialog.Builder builder = new Builder(context);
					builder.setMessage("确认抢下该订单？");
					builder.setTitle("提示");
					builder.setPositiveButton("确认",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									getorder(orderItem.orderId,
											orderItem.agencyId, position);
								}

							});
					builder.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();
								}
							});
					builder.create().show();
					break;
				// case R.id.btn_comment:
				// //评价订单
				// Intent commentIntent = new
				// Intent(context,TzmCommentActivity.class);
				// commentIntent.putExtra("orderId", orderItem.orderId);
				// // commentIntent.putExtra("mark", mark);
				// context.startActivity(commentIntent);
				// break;
				case R.id.ll_orderDetail:
					// 订单详情
					Intent orderDetailIntent = new Intent(context,
							AgentOrdersDetailActicity.class);
					orderDetailIntent.putExtra("oid", orderItem.orderId);
					orderDetailIntent
							.putExtra("oStatus", orderItem.orderStatus);
					context.startActivity(orderDetailIntent);
					break;
				// case R.id.btn_wuliu:
				// //查看物流
				// Intent wuliuIntent = new
				// Intent(context,TzmExpressActivity.class);
				// wuliuIntent.putExtra("orderId", orderItem.orderId);
				// context.startActivity(wuliuIntent);
				// break;
				// case R.id.btn_comfire:
				// //确认订单
				// final int index2 = (Integer) v.getTag();
				// AlertDialog.Builder builder2 = new Builder(context);
				// builder2.setMessage("确认收货？");
				// builder2.setTitle("提示");
				// builder2.setPositiveButton("确认", new
				// DialogInterface.OnClickListener() {
				// @Override
				// public void onClick(DialogInterface dialog,
				// int which) {
				// isLogin = UserInfoUtils.isLogin();
				// if (isLogin) {
				// userModel = UserInfoUtils.getUserInfo();
				// }
				// comfireOrder(orderItem.orderId,userModel.uid,index2);
				// }
				// });
				// builder2.setNegativeButton("取消", new
				// DialogInterface.OnClickListener() {
				// @Override
				// public void onClick(DialogInterface dialog, int which) {
				// dialog.cancel();
				// }
				// });
				// builder2.create().show();
				// break;
				// case R.id.tv_shopName:
				// Intent intent = new Intent(context,
				// TzmShopDetailActivity.class);
				// intent.putExtra("id",
				// Integer.parseInt(v.getTag().toString()));
				// intent.putExtra("shopName",((TextView)v).getText());
				// context.startActivity(intent);
				//
				// break;
				//
				}// switch
			}// onclick
		};
		viewHolder.ll_orderDetail.setOnClickListener(clickListener);
		// viewHolder.tv_shopName.setOnClickListener(clickListener);
		viewHolder.btn_status1.setOnClickListener(clickListener);
		return view;
	}

	private class ViewHolder {
		TextView tv_shopName, tv_productName, tv_orderPrice,
				tv_price, tv_num;
		ImageView iv_productImage;
		Button btn_status1, btn_status2, btn_status3;
		LinearLayout ll_orderDetail;
	}

}
