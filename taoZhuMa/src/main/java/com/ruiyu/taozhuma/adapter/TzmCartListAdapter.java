package com.ruiyu.taozhuma.adapter;

import java.text.DecimalFormat;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.activity.ProductDetailActivity;
import com.ruiyu.taozhuma.api.ApiClient;
import com.ruiyu.taozhuma.api.ApiListener;
import com.ruiyu.taozhuma.api.TzmDelCartApi;
import com.ruiyu.taozhuma.dialog.CustomCartDialog;
import com.ruiyu.taozhuma.dialog.CustomCartDialog.Builder;
import com.ruiyu.taozhuma.dialog.WarningDialog;
import com.ruiyu.taozhuma.model.TzmMyCartModel;
import com.ruiyu.taozhuma.utils.ProgressDialogUtil;
import com.ruiyu.taozhuma.utils.StringUtils;
import com.ruiyu.taozhuma.utils.ToastUtils;
import com.ruiyu.taozhuma.utils.UserInfoUtils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author fu
 * @version 创建时间：2015年4月13日 下午3:29:45 类说明：购物Adapter类
 */
public class TzmCartListAdapter extends BaseAdapter {

	private LayoutInflater layoutInflater;
	private List<TzmMyCartModel> list;
	private Context c;
	private String allCartPrice;
	private Double price;
	private TzmDelCartApi delCartApi;
	private ApiClient apiClient;
	final DecimalFormat df;
	final DbUtils dbUtils;

	public TzmCartListAdapter(Context c, List<TzmMyCartModel> list) {
		this.list = list;
		this.c = c;
		layoutInflater = LayoutInflater.from(c);
		df = new DecimalFormat(".#");
		dbUtils = DbUtils.create(c);
	}

	public void setList(List<TzmMyCartModel> list) {
		this.list = list;
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
		final int index = position;
		final ViewHolder viewHolder;
		if (view == null) {
			view = layoutInflater.inflate(R.layout.tzm_shop_item2, null);
			viewHolder = new ViewHolder();
			viewHolder.tv_allprice = (TextView) view
					.findViewById(R.id.tv_allprice);
			
			viewHolder.im_reduce = (ImageView) view
					.findViewById(R.id.im_reduce);
			viewHolder.im_add = (ImageView) view.findViewById(R.id.im_add);
			// viewHolder.btn_check = (Button)
			// view.findViewById(R.id.btn_check);
			viewHolder.checkbox = (CheckBox) view.findViewById(R.id.checkbox);

			viewHolder.et_num = (EditText) view.findViewById(R.id.et_num);
			viewHolder.et_num.setKeyListener(null);
			viewHolder.ll_main = (LinearLayout) view.findViewById(R.id.ll_main);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}

		final TzmMyCartModel info = this.list.get(position);
		
		viewHolder.checkbox
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						try {
//							if (isChecked) {
//								list.get(index).isCheck = true;
//							} else {
//								list.get(index).isCheck = false;
//							}

							dbUtils.update(list.get(index));
							// 发布事件，在后台线程发的事件
						//	EventBus.getDefault().post(new CartEven());
						} catch (DbException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				});
//		if (info.isCheck) {
//			viewHolder.checkbox.setChecked(true);
//		} else {
//			viewHolder.checkbox.setChecked(false);
//		}
		// try {
		// cartModel = dbUtils.findFirst(Selector.from(TzmMyCartModel.class)
		// .where("cid", "=", info.cid));
		// info.isCheck = cartModel.isCheck;
		// } catch (DbException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
//		if(info.status==0){
//			viewHolder.tv_product_name.setText(info.name+"（已下架）");
//			viewHolder.tv_product_name.setTextColor(Color.parseColor("#cacaca"));
//		}else{
//			viewHolder.tv_product_name.setTextColor(c.getResources().getColor(R.color.black));
//			viewHolder.tv_product_name.setText(info.name);
//		}
//		viewHolder.et_num.setText(info.number + "");
//		bitmapUtils.display(viewHolder.iv_picture, info.image);
		// if (list.get(index).isCheck) {
		// viewHolder.btn_check.setBackgroundResource(R.drawable.tzm_ok);
		// } else {
		// viewHolder.btn_check.setBackgroundResource(R.drawable.tzm_empty);
		// }

		// viewHolder.btn_check.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// try {
		// cartModel = dbUtils.findFirst(Selector.from(
		// TzmMyCartModel.class).where("cid", "=", info.cid));
		// if (cartModel.isCheck) {
		// viewHolder.btn_check
		// .setBackgroundResource(R.drawable.tzm_empty);
		// } else {
		// viewHolder.btn_check
		// .setBackgroundResource(R.drawable.tzm_ok);
		// }
		// cartModel.isCheck = !cartModel.isCheck;
		// dbUtils.update(cartModel);
		// // 发布事件，在后台线程发的事件
		// EventBus.getDefault().post(new CartEven());
		// } catch (DbException e1) {
		// e1.printStackTrace();
		// }
		//
		// }
		// });
//		viewHolder.tv_price.setText("￥" + info.price);
//		// 总价四舍五入
//		allCartPrice = df.format(info.price * info.number);
		viewHolder.tv_allprice.setText("小计：￥" + allCartPrice);
		viewHolder.im_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

//				try {
					// cartModel = dbUtils.findFirst(Selector.from(
					// TzmMyCartModel.class).where("cid", "=", info.cid));
//					cartModel = info;
//					cartModel.number = cartModel.number + 1;
//					cartModel.price = getPrice(cartModel, cartModel.number);
//					dbUtils.update(cartModel);
//					viewHolder.tv_price.setText("￥" + cartModel.price);
//					// 总价四舍五入
//					allCartPrice = df
//							.format(cartModel.price * cartModel.number);
//					viewHolder.tv_allprice.setText("小计：￥" + allCartPrice);
//					viewHolder.et_num.setText(cartModel.number + "");
//					EventBus.getDefault().post(new CartEven());
//				} catch (DbException e) {
//					e.printStackTrace();
//				}

			}
		});

		viewHolder.im_reduce.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					// cartModel = dbUtils.findFirst(Selector.from(
					// TzmMyCartModel.class).where("cid", "=", info.cid));
					//cartModel = info;
//					if (cartModel.number > 1) {
//						cartModel.number = cartModel.number - 1;
//						cartModel.price = getPrice(cartModel, cartModel.number);
//						dbUtils.update(cartModel);
//						viewHolder.tv_price.setText("￥" + cartModel.price);
//						// 总价四舍五入
//						allCartPrice = df.format(cartModel.price
//								* cartModel.number);
//						viewHolder.tv_allprice.setText("小计：￥" + allCartPrice);
//						viewHolder.et_num.setText(cartModel.number + "");
//						EventBus.getDefault().post(new CartEven());
//					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		viewHolder.ll_main.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				CustomCartDialog.Builder builder = new Builder(c);
				builder.setDetailClickListener(new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(c,
								ProductDetailActivity.class);
						//intent.putExtra("id", info.productId);
						c.startActivity(intent);
						dialog.dismiss();

					}
				});
				builder.setDdeleteClickListener(new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						WarningDialog.Builder builder = new WarningDialog.Builder(
								c);
						builder.setNegativeButton(new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								dialog.dismiss();

							}
						});
						builder.setPositiveClickListener(new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								delCartApi = new TzmDelCartApi();
								apiClient = new ApiClient(c);
								delCartApi.setUid(UserInfoUtils.getUserInfo().uid);
								//delCartApi.setCids(info.cid);
								apiClient.api(delCartApi, new ApiListener() {

									@Override
									public void onStart() {
										ProgressDialogUtil.openProgressDialog(
												c, "", "");

									}

									@Override
									public void onComplete(String jsonStr) {
										 ProgressDialogUtil.closeProgressDialog();
										if (StringUtils.isNotBlank(jsonStr)) {
											try {
												JSONObject jsonObject = new JSONObject(
														jsonStr);
												boolean success = jsonObject
														.optBoolean("success");
												int result = jsonObject
														.optInt("result");
												String error_msg = jsonObject
														.optString("error_msg");
												ToastUtils.showShortToast(c,
														error_msg);
												if (success && result == 0) {
													try {
														dbUtils.delete(info);
														list.remove(position);
														TzmCartListAdapter.this
																.notifyDataSetChanged();
													} catch (DbException e) {
														// TODO Auto-generated
														// catch block
														e.printStackTrace();
													}

												}
											} catch (JSONException e) {
												e.printStackTrace();
											}

										}

									}
								}, true);
								dialog.dismiss();

							}
						});
						builder.create().show();

					}
				});
				builder.create().show();
				return false;
			}
		});
		// viewHolder.tv_product_name.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// Intent intent = new Intent(c, TzmProductDetailActivity.class);
		// intent.putExtra("id", info.productId);
		// c.startActivity(intent);
		// }
		// });
		// viewHolder.cb_check.setOnCheckedChangeListener(new
		// OnCheckedChangeListener() {
		//
		// @Override
		// public void onCheckedChanged(CompoundButton compoundButton, boolean
		// ischeck) {
		// if(ischeck){
		// //cartModel.isCheck = false;
		// list.get(index).isCheck=true;
		// }else{
		// //cartModel.isCheck = true;
		// list.get(index).isCheck=false;
		// }
		// try {
		// cartModel = dbUtils.findFirst(Selector.from(TzmMyCartModel.class)
		// .where("cid", "=", list.get(index).cid));
		// cartModel.isCheck = list.get(index).isCheck;
		// dbUtils.update(cartModel);
		// } catch (DbException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// // try {
		// // dbUtils.update(cartModel);
		// // } catch (DbException e) {
		// // // TODO Auto-generated catch block
		// // e.printStackTrace();
		// // }
		// }
		// });

		// viewHolder.cb_check.setChecked(list.get(index).isCheck);

		// //保存数据库
		//
		// try {
		// dbUtils.save(info);
		// } catch (DbException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		return view;
	}

	// 根据数量计算阶梯价格
	public Double getPrice(TzmMyCartModel cartModel, int num) {
//		Double price = null;
//		if (cartModel.min == 0 || num <= cartModel.min) {
//			price = cartModel.minprices;
//		} else {
//			if (cartModel.mid == 0 || num <= cartModel.mid) {
//				price = cartModel.midprices;
//			} else {
//				price = cartModel.maxprices;
//			}
//		}
		return price;
	}

	private class ViewHolder {
		TextView tv_allprice;
		ImageView im_reduce, im_add;
		// Button btn_check;
		EditText et_num;
		CheckBox checkbox;
		LinearLayout ll_main;
	}
}