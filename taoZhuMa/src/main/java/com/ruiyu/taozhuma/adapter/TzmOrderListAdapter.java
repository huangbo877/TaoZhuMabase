package com.ruiyu.taozhuma.adapter;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
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
import com.ruiyu.taozhuma.activity.OrdersDetailActivity;
import com.ruiyu.taozhuma.activity.TzmCommentActivity;
import com.ruiyu.taozhuma.activity.TzmExpressActivity;
import com.ruiyu.taozhuma.activity.TzmNewCommentsActivity;
import com.ruiyu.taozhuma.activity.TzmShopDetailActivity;
import com.ruiyu.taozhuma.api.ApiClient;
import com.ruiyu.taozhuma.api.ApiListener;
import com.ruiyu.taozhuma.api.TzmCancelOrderApi;
import com.ruiyu.taozhuma.api.TzmEditOrderStatusApi;
import com.ruiyu.taozhuma.base.xUtilsImageLoader;
import com.ruiyu.taozhuma.even.RePayEven;
import com.ruiyu.taozhuma.model.TzmMyorderModel;
import com.ruiyu.taozhuma.model.UserModel;
import com.ruiyu.taozhuma.utils.LogUtil;
import com.ruiyu.taozhuma.utils.StringUtils;
import com.ruiyu.taozhuma.utils.ToastUtils;
import com.ruiyu.taozhuma.utils.UserInfoUtils;

import de.greenrobot.event.EventBus;

public class TzmOrderListAdapter extends BaseAdapter {
	private LayoutInflater layoutInflater;
	private ArrayList<TzmMyorderModel> list;
	private Context context;
	private int j = 0;
	View convertView[] = null;
	private ApiClient apiClient, apiClient2;
	private TzmCancelOrderApi tzmCancelOrderApi;
	private TzmEditOrderStatusApi tzmEditOrderStatusApi;
	private Boolean isLogin;
	private UserModel userModel;
	private xUtilsImageLoader imageLoader;

	// int mark;

	public TzmOrderListAdapter(Context context,
			ArrayList<TzmMyorderModel> list, xUtilsImageLoader imageLoader) {
		this.list = list;
		this.context = context;
		layoutInflater = LayoutInflater.from(context);
		this.imageLoader = imageLoader;
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

	public void changeStatus(int position) {
		// this.list.get(position).orderStatus = 4;
		this.list.remove(position);
		this.notifyDataSetChanged();// 刷新ListView
	}

	// 取消订单
	public void cancelOrder(int oid, final int index) {
		tzmCancelOrderApi = new TzmCancelOrderApi();
		apiClient2 = new ApiClient(context);
		tzmCancelOrderApi.setOid(oid);
		apiClient2.api(this.tzmCancelOrderApi, new ApiListener() {
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
							if (result == 1) {
								ToastUtils.showShortToast(context, error_msg);
								removeItem(index);
							} else {
								ToastUtils.showShortToast(context, error_msg);
							}
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}

			@Override
			public void onError(String error) {
				ToastUtils.showShortToast(context, R.string.msg_list_null);
			}

			@Override
			public void onException(Exception e) {
				LogUtil.ErrorLog(e);
			}
		}, true);

	}

	// 确认订单
	public void comfireOrder(int oid, int uid, final int index2) {
		tzmEditOrderStatusApi = new TzmEditOrderStatusApi();
		apiClient = new ApiClient(context);
		tzmEditOrderStatusApi.setUid(uid);
		tzmEditOrderStatusApi.setOid(oid);
		tzmEditOrderStatusApi.setStatus(4);
		apiClient.api(this.tzmEditOrderStatusApi, new ApiListener() {
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
						if (success) {
							if (result == 1) {
								changeStatus(index2);
							} else {
								String success_msg = "确认失败";
								ToastUtils.showShortToast(context, success_msg);
							}
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}

			@Override
			public void onError(String error) {
				ToastUtils.showShortToast(context, R.string.msg_list_null);
			}

			@Override
			public void onException(Exception e) {
				LogUtil.ErrorLog(e);
			}
		}, true);

	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(final int position, View view, ViewGroup viewGroup) {
		ViewHolder viewHolder;
		final TzmMyorderModel orderItem = this.list.get(position);
		if (view == null) {
			view = layoutInflater.inflate(R.layout.tzm_order_list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.rl_orderDetail = (LinearLayout) view
					.findViewById(R.id.rl_orderDetail);
			viewHolder.tv_shopName = (TextView) view
					.findViewById(R.id.tv_shopName);
			viewHolder.tv_orderPrice = (TextView) view
					.findViewById(R.id.tv_orderPrice);
			viewHolder.btn_delete = (Button) view.findViewById(R.id.btn_delete);
			viewHolder.btn_comment = (Button) view
					.findViewById(R.id.btn_comment);
			viewHolder.btn_wuliu = (Button) view.findViewById(R.id.btn_wuliu);
			viewHolder.btn_daifahuo = (Button) view
					.findViewById(R.id.btn_daifahuo);
			viewHolder.btn_comfire = (Button) view
					.findViewById(R.id.btn_comfire);
			viewHolder.btn_yiping = (Button) view.findViewById(R.id.btn_yiping);
			viewHolder.btn_fukuang = (Button) view
					.findViewById(R.id.btn_fukuang);
			viewHolder.tv_orderStatus = (TextView) view
					.findViewById(R.id.tv_orderStatus);
			viewHolder.rl_orderDetail = (LinearLayout) view
					.findViewById(R.id.rl_orderDetail);
			viewHolder.tv_allnum = (TextView) view.findViewById(R.id.tv_allnum);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		viewHolder.tv_allnum.setText("共" + orderItem.num + "件产品");
		viewHolder.tv_shopName.setText(orderItem.shopName + "");
		viewHolder.tv_shopName.setTag(orderItem.shopId);
		// 接口返回:订单状态：1待付款,2已付款，待发货,3已发货,4已收货，5已确定
		String orderStatus = "";
		viewHolder.btn_delete.setVisibility(View.GONE);
		viewHolder.btn_fukuang.setVisibility(View.GONE);
		viewHolder.btn_comment.setVisibility(View.GONE);
		viewHolder.btn_wuliu.setVisibility(View.GONE);
		viewHolder.btn_comfire.setVisibility(View.GONE);
		viewHolder.btn_yiping.setVisibility(View.GONE);
		viewHolder.btn_daifahuo.setVisibility(View.GONE);
		switch (orderItem.refundId) {
		case 0:
			switch (orderItem.orderStatus) {
			case 1:
				orderStatus = "等待买家付款";
				viewHolder.btn_delete.setVisibility(View.VISIBLE);
				viewHolder.btn_fukuang.setVisibility(View.VISIBLE);
				break;
			case 2:
				orderStatus = "买家已付款";
				viewHolder.btn_daifahuo.setVisibility(View.VISIBLE);
				break;
			case 3:
				orderStatus = "卖家已发货";
				viewHolder.btn_wuliu.setVisibility(View.VISIBLE);
				viewHolder.btn_comfire.setVisibility(View.VISIBLE);
				break;
			case 4:
				orderStatus = "交易成功";
				viewHolder.btn_wuliu.setVisibility(View.VISIBLE);
				viewHolder.btn_comment.setVisibility(View.VISIBLE);
				break;
			case 5:
				orderStatus = "交易成功";
				viewHolder.btn_delete.setVisibility(View.VISIBLE);
				viewHolder.btn_yiping.setVisibility(View.VISIBLE);
				break;
			}
			break;
		case 1:
			switch (orderItem.orderStatus) {
			case 1:
				// orderStatus = "等待买家付款";
				break;
			case 2:
				orderStatus = "售后申请审核中";
				break;
			case 3:
				orderStatus = "售后申请审核中";
				viewHolder.btn_wuliu.setVisibility(View.VISIBLE);
				viewHolder.btn_comfire.setVisibility(View.VISIBLE);
				break;
			case 4:
				orderStatus = "售后申请审核中";
				viewHolder.btn_wuliu.setVisibility(View.VISIBLE);
				break;
			case 5:
				// orderStatus = "交易成功";
				break;
			}
			break;
		case 2:
			switch (orderItem.orderStatus) {
			case 1:
				// orderStatus = "等待买家付款";
				break;
			case 2:
				// orderStatus = "售后申请审核中";
				break;
			case 3:
				orderStatus = "售后申请处理中";
				viewHolder.btn_wuliu.setVisibility(View.VISIBLE);
				viewHolder.btn_comfire.setVisibility(View.VISIBLE);
				break;
			case 4:
				orderStatus = "售后申请处理中";
				viewHolder.btn_wuliu.setVisibility(View.VISIBLE);
				break;
			case 5:
				// orderStatus = "交易成功";
				break;
			}
			break;
		case 3:
			switch (orderItem.orderStatus) {
			case 1:
				// orderStatus = "等待买家付款";
				break;
			case 2:
				orderStatus = "订单关闭";
				viewHolder.btn_delete.setVisibility(View.VISIBLE);
				break;
			case 3:
				orderStatus = "订单关闭";
				viewHolder.btn_delete.setVisibility(View.VISIBLE);
				viewHolder.btn_wuliu.setVisibility(View.VISIBLE);
				break;
			case 4:
				orderStatus = "订单关闭";
				viewHolder.btn_delete.setVisibility(View.VISIBLE);
				viewHolder.btn_wuliu.setVisibility(View.VISIBLE);
				break;
			case 5:
				// orderStatus = "交易成功";
				break;
			}
			break;
		case 4:
			switch (orderItem.orderStatus) {
			case 1:
				// orderStatus = "等待买家付款";
				break;
			case 2:
				orderStatus = "售后申请失败";
				viewHolder.btn_daifahuo.setVisibility(View.VISIBLE);
				break;
			case 3:
				orderStatus = "售后申请失败";
				viewHolder.btn_wuliu.setVisibility(View.VISIBLE);
				viewHolder.btn_comfire.setVisibility(View.VISIBLE);
				break;
			case 4:
				orderStatus = "售后申请失败";
				viewHolder.btn_wuliu.setVisibility(View.VISIBLE);
				viewHolder.btn_delete.setVisibility(View.VISIBLE);
				break;
			case 5:
				// orderStatus = "交易成功";
				break;
			}
			break;
		}
		viewHolder.tv_orderStatus.setText(orderStatus);
		//
		// if (orderItem.refundId == 0) {
		// if (orderItem.orderStatus == 1) {
		//
		// } else if (orderItem.orderStatus == 2) {
		//
		// if (orderItem.consigneeType.equals("快递")) {
		// orderStatus = "买家已付款";
		// } else {
		// orderStatus = "货到付款";
		// }
		// } else if (orderItem.orderStatus == 3) {
		// orderStatus = "卖家已发货";
		// } else {
		// orderStatus = "交易成功";
		// }
		//
		// } else if (orderItem.refundId == 1) {
		// orderStatus = "售后申请审核中";
		// } else if (orderItem.refundId == 2) {
		// orderStatus = "售后申请处理中";
		// } else if (orderItem.refundId == 3) {
		// orderStatus = "订单关闭";
		// } else if (orderItem.refundId == 4) {
		// orderStatus = "售后申请失败";
		// }
		// viewHolder.tv_orderStatus.setText(orderStatus);
		//
		// if (orderItem.orderStatus == 1 || orderItem.orderStatus == 5) {
		// viewHolder.btn_delete.setVisibility(View.VISIBLE);
		// } else {
		// viewHolder.btn_delete.setVisibility(View.GONE);
		// }
		// if (orderItem.orderStatus == 4) {
		// if (orderItem.refundId == 1) {
		// viewHolder.btn_comment.setVisibility(View.GONE);
		// } else {
		// viewHolder.btn_comment.setVisibility(View.VISIBLE);
		//
		// }
		// } else {
		// viewHolder.btn_comment.setVisibility(View.GONE);
		// }
		// if (orderItem.orderStatus == 3 || orderItem.orderStatus == 5
		// || orderItem.orderStatus == 4) {
		// if (orderItem.consigneeType.equals("快递")) {
		// viewHolder.btn_wuliu.setVisibility(View.VISIBLE);
		// }
		// if (orderItem.consigneeType.equals("自提")) {
		// viewHolder.btn_wuliu.setVisibility(View.GONE);
		// }
		// } else {
		// viewHolder.btn_wuliu.setVisibility(View.GONE);
		// }
		// if (orderItem.orderStatus == 2) {
		// viewHolder.btn_daifahuo.setVisibility(View.VISIBLE);
		// viewHolder.btn_wuliu.setVisibility(View.GONE);
		// } else {
		// viewHolder.btn_daifahuo.setVisibility(View.GONE);
		// }
		// if (orderItem.orderStatus == 3) {
		//
		// viewHolder.btn_comfire.setVisibility(View.VISIBLE);
		//
		// } else {
		//
		// viewHolder.btn_comfire.setVisibility(View.GONE);
		//
		// }
		// if (orderItem.orderStatus == 1) {
		// viewHolder.btn_wuliu.setVisibility(View.GONE);
		// viewHolder.btn_delete.setVisibility(View.VISIBLE);
		// viewHolder.btn_fukuang.setVisibility(View.VISIBLE);
		// } else {
		// viewHolder.btn_fukuang.setVisibility(View.GONE);
		// }
		// if (orderItem.orderStatus == 5) {
		// viewHolder.btn_yiping.setVisibility(View.VISIBLE);
		// viewHolder.btn_comment.setVisibility(View.GONE);
		// } else {
		// viewHolder.btn_yiping.setVisibility(View.GONE);
		// }

		viewHolder.tv_orderPrice.setText("¥ " + orderItem.orderPrice + "  (含运费"
				+ orderItem.espressPrice + ")");

		if (viewHolder.rl_orderDetail.getChildCount() > 0) {
			viewHolder.rl_orderDetail.removeAllViews();
		}
		if (orderItem.carts.size() != 0) {
			for (int index = 0; index < orderItem.carts.size(); index++) {
				convertView = new View[orderItem.carts.size()];
				convertView[index] = (View) layoutInflater.inflate(
						R.layout.tzm_order_detail_item, null);
				j = index;
				convertView[index].setId(j);
				viewHolder.tv_skuvalue = (TextView) convertView[index]
						.findViewById(R.id.tv_skuvalue);
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
				viewHolder.tv_num.setText("x"
						+ orderItem.carts.get(index).productNumber);
				viewHolder.tv_skuvalue
						.setText(orderItem.carts.get(index).skuValue);
				viewHolder.rl_orderDetail.addView(convertView[index]);
			}
		}

		OnClickListener clickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.btn_delete:
					// 取消订单
					final int index = (Integer) v.getTag();
					AlertDialog.Builder builder = new Builder(context);
					builder.setMessage("确认取消订单？");
					builder.setTitle("提示");
					builder.setPositiveButton("确认",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									cancelOrder(orderItem.orderId, index);
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
				case R.id.btn_comment:
					// 评价订单
					Intent commentIntent = new Intent(context,
							TzmNewCommentsActivity.class);
					commentIntent.putExtra("orderId", orderItem.orderId);
					// commentIntent.putExtra("mark", mark);
					context.startActivity(commentIntent);
					break;
				case R.id.rl_orderDetail:
					// 订单详情
					Intent orderDetailIntent = new Intent(context,
							OrdersDetailActivity.class);
					orderDetailIntent.putExtra("oid", orderItem.orderId);
					orderDetailIntent
							.putExtra("oStatus", orderItem.orderStatus);
					context.startActivity(orderDetailIntent);
					break;
				case R.id.btn_wuliu:
					// 查看物流
					Intent wuliuIntent = new Intent(context,
							TzmExpressActivity.class);
					wuliuIntent.putExtra("orderId", orderItem.orderId);
					context.startActivity(wuliuIntent);
					break;
				case R.id.btn_comfire:
					// 确认订单
					final int index2 = (Integer) v.getTag();
					AlertDialog.Builder builder2 = new Builder(context);
					builder2.setMessage("确认收货？");
					builder2.setTitle("提示");
					builder2.setPositiveButton("确认",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									isLogin = UserInfoUtils.isLogin();
									if (isLogin) {
										userModel = UserInfoUtils.getUserInfo();
									}
									comfireOrder(orderItem.orderId,
											userModel.uid, index2);
								}
							});
					builder2.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();
								}
							});
					builder2.create().show();
					break;
				case R.id.tv_shopName:
					// Intent intent = new Intent(context,
					// TzmShopDetailActivity.class);
					// intent.putExtra("id",
					// Integer.parseInt(v.getTag().toString()));
					// intent.putExtra("shopName", ((TextView) v).getText());
					// context.startActivity(intent);

					break;
				case R.id.btn_fukuang:
					AlertDialog.Builder builder1 = new Builder(context);
					builder1.setMessage("订单付款？");
					builder1.setTitle("提示");
					builder1.setPositiveButton("确认",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();
									EventBus.getDefault().post(
											new RePayEven(
													orderItem.orderNumber,
													orderItem.orderPrice,
													orderItem.isWallet));
								}
							});
					builder1.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();
								}
							});
					builder1.create().show();
					break;

				}// switch
			}// onclick
		};
		viewHolder.btn_delete.setTag(position);
		viewHolder.btn_comfire.setTag(position);
		viewHolder.btn_delete.setOnClickListener(clickListener);
		viewHolder.btn_wuliu.setOnClickListener(clickListener);
		viewHolder.btn_comfire.setOnClickListener(clickListener);
		viewHolder.rl_orderDetail.setOnClickListener(clickListener);
		viewHolder.btn_comment.setOnClickListener(clickListener);
		viewHolder.tv_shopName.setOnClickListener(clickListener);
		viewHolder.btn_fukuang.setOnClickListener(clickListener);
		return view;
	}

	private class ViewHolder {
		TextView tv_shopName, tv_productName, tv_orderPrice, tv_price, tv_num,
				tv_allnum, tv_orderStatus;
		ImageView iv_productImage;
		Button btn_delete, btn_comment, btn_wuliu, btn_daifahuo, btn_comfire,
				btn_yiping, btn_fukuang;
		LinearLayout rl_orderDetail;

		TextView tv_skuvalue;
	}

}
