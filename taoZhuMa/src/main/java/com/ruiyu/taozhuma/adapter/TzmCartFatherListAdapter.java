package com.ruiyu.taozhuma.adapter;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.lidroid.xutils.BitmapUtils;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.activity.ProductDetailActivity;
import com.ruiyu.taozhuma.activity.TzmSelctAddressActivity;
import com.ruiyu.taozhuma.activity.TzmShopDetailActivity;
import com.ruiyu.taozhuma.api.ApiClient;
import com.ruiyu.taozhuma.api.ApiListener;
import com.ruiyu.taozhuma.api.EditCartApi;
import com.ruiyu.taozhuma.api.TzmDelCartApi;
import com.ruiyu.taozhuma.even.CartChildEven;
import com.ruiyu.taozhuma.even.CartEven;
import com.ruiyu.taozhuma.even.CartFatherEven;
import com.ruiyu.taozhuma.even.CartChangeEven;
import com.ruiyu.taozhuma.model.TzmMyCartModel;
import com.ruiyu.taozhuma.utils.LogUtil;
import com.ruiyu.taozhuma.utils.ProgressDialogUtil;
import com.ruiyu.taozhuma.utils.StringUtils;
import com.ruiyu.taozhuma.utils.ToastUtils;
import com.ruiyu.taozhuma.utils.UserInfoUtils;

import de.greenrobot.event.EventBus;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

/*
 * fu
 * 
 * */
public class TzmCartFatherListAdapter extends BaseAdapter {
	private LayoutInflater layoutInflater;
	public List<TzmMyCartModel> list;
	private Context c;
	private BitmapUtils bitmapUtils;

	// private BitmapUtils bitmapUtils;

	// private TzmCartChildListAdapter adapter;
	private TzmDelCartApi delCartApi;
	private ApiClient apiClient;
	private EditCartApi editCartApi;

	public TzmCartFatherListAdapter(Context c, List<TzmMyCartModel> list,
			BitmapUtils bitmapUtils) {
		this.list = list;
		this.c = c;
		layoutInflater = LayoutInflater.from(c);
		this.bitmapUtils = bitmapUtils;
		this.bitmapUtils.configDefaultShowOriginal(true);
		// bitmapUtils = new BitmapUtils(c, null, (float) 0.3);
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

	@SuppressLint("InflateParams")
	@Override
	public View getView(final int position, View view, ViewGroup viewGroup) {
		final ViewHolder viewHolder;
		final int index = position;
		if (view == null) {
			view = layoutInflater.inflate(R.layout.tzm_cart_father_item, null);
			viewHolder = new ViewHolder();
			viewHolder.tv_shopname = (TextView) view
					.findViewById(R.id.tv_shopname);
			viewHolder.ll_bottomview = (LinearLayout) view
					.findViewById(R.id.ll_bottomview);
			viewHolder.ll_midview = (LinearLayout) view
					.findViewById(R.id.ll_midview);
			viewHolder.ll_topview = (LinearLayout) view
					.findViewById(R.id.ll_topview);
			viewHolder.tv_count = (TextView) view.findViewById(R.id.tv_count);
			viewHolder.tv_price = (TextView) view.findViewById(R.id.tv_price);
			viewHolder.checkboxtop = (CheckBox) view
					.findViewById(R.id.checkboxtop);
			viewHolder.checkboxmid = (CheckBox) view
					.findViewById(R.id.checkboxmid);
			viewHolder.tv_product_name = (TextView) view
					.findViewById(R.id.tv_product_name);
			viewHolder.tv_allprice = (TextView) view
					.findViewById(R.id.tv_allprice);
			viewHolder.iv_picture = (ImageView) view
					.findViewById(R.id.iv_picture);
			viewHolder.et_num = (EditText) view.findViewById(R.id.et_num);
			viewHolder.im_add = (ImageView) view.findViewById(R.id.im_add);
			viewHolder.im_reduce = (ImageView) view
					.findViewById(R.id.im_reduce);
			viewHolder.iv_delete = (ImageView) view
					.findViewById(R.id.iv_delete);
			viewHolder.tv_skuvalue = (TextView) view
					.findViewById(R.id.tv_skuvalue);
			viewHolder.iv_tag_status = (ImageView) view
					.findViewById(R.id.iv_tag_status);
			viewHolder.v_above = (View) view.findViewById(R.id.v_above);
			viewHolder.tv_discountTextStr = (TextView) view
					.findViewById(R.id.tv_discountTextStr);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		final TzmMyCartModel info = this.list.get(position);
		OnClickListener clickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.tv_shopname:
					// Intent intent = new Intent(c,
					// TzmShopDetailActivity.class);
					// intent.putExtra("activityId",
					// (list.get(Integer.parseInt(v
					// .getTag().toString())).shopId).toString());
					// //
					// System.out.println((list.get(Integer.parseInt(v.getTag().toString())).shopId).toString());
					// intent.putExtra(
					// "name",
					// list.get(Integer.parseInt(v.getTag().toString())).shopName);
					// c.startActivity(intent);
					break;
				case R.id.tv_product_name:

					Intent intent2 = new Intent(c, ProductDetailActivity.class);
					intent2.putExtra("id", info.productId);
					intent2.putExtra("activityId", info.activityId);
					c.startActivity(intent2);

					break;
				case R.id.im_add:
					if (list.get(position).type == 1
							&& list.get(position).number >= 3) {
						// 秒杀商品
						showActivityDialog();
					} else {
						// 普通商品
						String cids = list.get(position).cid + ":"
								+ (list.get(position).number + 1);
						apiClient = new ApiClient(c);
						editCartApi = new EditCartApi();
						editCartApi.setUid(UserInfoUtils.getUserInfo().uid);
						// editCartApi.setUid(2);
						editCartApi.setCids(cids);
						apiClient.api(editCartApi, new ApiListener() {

							@Override
							public void onStart() {
								ProgressDialogUtil
										.openProgressDialog(c, "", "");
							}

							@Override
							public void onException(Exception e) {
								ProgressDialogUtil.closeProgressDialog();
								LogUtil.ErrorLog(e);
								ToastUtils.showShortToast(c, "网络异常");
							}

							@Override
							public void onError(String error) {
								ProgressDialogUtil.closeProgressDialog();
								LogUtil.Log(error);
								ToastUtils.showShortToast(c, error);
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
										if (success && result == 1) {
											list.get(position).number = list
													.get(position).number + 1;
											viewHolder.et_num.setText(list
													.get(position).number + "");
											EventBus.getDefault().post(
													new CartFatherEven(index,
															list));
											EventBus.getDefault()
													.post(new CartEven(
															list.get(position).cartType,
															list));
										} else {
											ToastUtils.showShortToast(c,
													error_msg);
										}

									} catch (JSONException e) {
										e.printStackTrace();
									}

								}

							}
						}, true);

					}

					break;
				case R.id.im_reduce:
					if (list.get(position).number > 1) {
						String cids = list.get(position).cid + ":"
								+ (list.get(position).number - 1);
						apiClient = new ApiClient(c);
						editCartApi = new EditCartApi();
						editCartApi.setUid(UserInfoUtils.getUserInfo().uid);
						// editCartApi.setUid(2);
						editCartApi.setCids(cids);
						apiClient.api(editCartApi, new ApiListener() {

							@Override
							public void onStart() {
								ProgressDialogUtil
										.openProgressDialog(c, "", "");
							}

							@Override
							public void onException(Exception e) {
								ProgressDialogUtil.closeProgressDialog();
								LogUtil.ErrorLog(e);
								ToastUtils.showShortToast(c, "网络异常");
							}

							@Override
							public void onError(String error) {
								ProgressDialogUtil.closeProgressDialog();
								LogUtil.Log(error);
								ToastUtils.showShortToast(c, error);
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
										if (success && result == 1) {
											list.get(position).number = list
													.get(position).number - 1;
											viewHolder.et_num.setText(list
													.get(position).number + "");
											EventBus.getDefault().post(
													new CartFatherEven(index,
															list));
											EventBus.getDefault()
													.post(new CartEven(
															list.get(position).cartType,
															list));
										} else {
											ToastUtils.showShortToast(c,
													error_msg);
										}

									} catch (JSONException e) {
										e.printStackTrace();
									}

								}

							}
						}, true);

					}

					break;

				}

			}
		};
		if (info.tag == 0) {
			viewHolder.ll_topview.setVisibility(View.VISIBLE);
			viewHolder.ll_midview.setVisibility(View.GONE);
			viewHolder.ll_bottomview.setVisibility(View.GONE);
			viewHolder.tv_shopname.setText(info.shopName);
			viewHolder.tv_shopname.setTag(position);
			viewHolder.tv_shopname.setOnClickListener(clickListener);
			viewHolder.checkboxtop
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {
						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							list.get(index).isCheck = isChecked;
							EventBus.getDefault().post(
									new CartChildEven(info.cartType, info.tag,
											isChecked));
						}
					});
			if (info.isCheck) {
				viewHolder.checkboxtop.setChecked(true);
			} else {
				viewHolder.checkboxtop.setChecked(false);
			}

		} else if (info.tag == 1) {
			viewHolder.ll_topview.setVisibility(View.GONE);
			viewHolder.ll_midview.setVisibility(View.VISIBLE);
			viewHolder.ll_bottomview.setVisibility(View.GONE);
			// bitmapUtils.display(viewHolder.iv_picture, info.image);
			// 下架 ，活动结束
			if (info.status == 0 || info.isActive == 2 || info.isActive == 0) {
				viewHolder.iv_tag_status.setVisibility(View.VISIBLE);
				if (info.status == 0) {
					// 下架
					viewHolder.iv_tag_status
							.setImageResource(R.drawable.acstatusover);
				} else if (info.isActive == 2 || info.isActive == 0) {
					// 活动结束
					viewHolder.iv_tag_status
							.setImageResource(R.drawable.acover);
				}
				viewHolder.tv_product_name.setOnClickListener(null);
				viewHolder.checkboxmid.setVisibility(View.INVISIBLE);
				viewHolder.v_above.setVisibility(View.VISIBLE);// 遮盖层
				viewHolder.im_add.setOnClickListener(null);
				viewHolder.im_reduce.setOnClickListener(null);
				viewHolder.tv_product_name.setTextColor(c.getResources()
						.getColor(R.color.tzm_text_sys_default_light));
				viewHolder.tv_price.setTextColor(c.getResources().getColor(
						R.color.tzm_text_sys_default_light));
			} else {
				viewHolder.iv_tag_status.setVisibility(View.GONE);
				viewHolder.tv_product_name.setOnClickListener(clickListener);
				viewHolder.checkboxmid.setVisibility(View.VISIBLE);
				viewHolder.v_above.setVisibility(View.GONE);
				viewHolder.im_add.setOnClickListener(clickListener);
				viewHolder.im_reduce.setOnClickListener(clickListener);
				viewHolder.tv_product_name.setTextColor(c.getResources()
						.getColor(R.color.tzm_black));
				viewHolder.tv_price.setTextColor(c.getResources().getColor(
						R.color.tzm_black));
			}
			// 限量购标志
			if (info.type == 1) {
				String str = c.getString(R.string.event_title) + info.name;
				SpannableStringBuilder mSpannableStringBuilder = new SpannableStringBuilder(
						str);
				mSpannableStringBuilder.setSpan(new ForegroundColorSpan(c
						.getResources().getColor(R.color.base)), 0, 3,
						Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
				viewHolder.tv_product_name.setText(mSpannableStringBuilder);

			} else if (info.type == 5) {
				SpannableStringBuilder mSpannableStringBuilder = new SpannableStringBuilder(
						"专场活动" + info.name);
				mSpannableStringBuilder.setSpan(new ForegroundColorSpan(c
						.getResources().getColor(R.color.base)), 0, 4,
						Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
				viewHolder.tv_product_name.setText(mSpannableStringBuilder);
			} else if (info.type == 3) {
				SpannableStringBuilder mSpannableStringBuilder = new SpannableStringBuilder(
						"钱包专区" + info.name);
				mSpannableStringBuilder.setSpan(new ForegroundColorSpan(c
						.getResources().getColor(R.color.base)), 0, 4,
						Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
				viewHolder.tv_product_name.setText(mSpannableStringBuilder);
			} else {
				viewHolder.tv_product_name.setText(info.name);
			}

			viewHolder.et_num.setText(info.number + "");
			if (StringUtils.isNotEmpty(info.skuValue)) {
				viewHolder.tv_skuvalue.setText(info.skuValue);
			} else {
				viewHolder.tv_skuvalue.setText(null);
			}
			// viewHolder.tv_product_name.setText(info.name);
			viewHolder.tv_price.setText("¥ " + info.price);
			// if (!StringUtils.isEmpty(info.image)) {
			bitmapUtils.display(viewHolder.iv_picture, info.image);

			// BaseApplication.getInstance().getImageWorker()
			// .loadBitmap(info.image, viewHolder.iv_picture);
			// }
			viewHolder.checkboxmid
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {
						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							if (isChecked) {
								list.get(index).isCheck = true;
								// EventBus.getDefault().post(
								// new CartFatherEven(index, true));
							} else {
								list.get(index).isCheck = false;
								// EventBus.getDefault().post(
								// new CartFatherEven(index, false));
							}
							EventBus.getDefault().post(
									new CartFatherEven(index, list));
						}
					});
			if (info.isCheck) {
				viewHolder.checkboxmid.setChecked(true);
			} else {
				viewHolder.checkboxmid.setChecked(false);
			}
			viewHolder.iv_delete.setTag(position);
			viewHolder.iv_delete.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Deleta((Integer) v.getTag(),
							list.get((Integer) v.getTag()).cid);

				}
			});
		} else {
			viewHolder.ll_topview.setVisibility(View.GONE);
			viewHolder.ll_midview.setVisibility(View.GONE);
			viewHolder.ll_bottomview.setVisibility(View.VISIBLE);
			viewHolder.tv_allprice.setText("¥ " + info.holeprice);
			viewHolder.tv_count.setText("小计(共" + info.count + "件)：");
			viewHolder.tv_discountTextStr.setText(info.discountTextStr);
		}

		return view;
	}

	protected void Deleta(final int position, final int cid) {
		AlertDialog.Builder builder = new AlertDialog.Builder(c);
		builder.setTitle("操作")
				.setPositiveButton("删除", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						delCartApi = new TzmDelCartApi();
						apiClient = new ApiClient(c);
						delCartApi.setUid(UserInfoUtils.getUserInfo().uid);
						delCartApi.setCids(cid);
						apiClient.api(delCartApi, new ApiListener() {

							@Override
							public void onStart() {
								ProgressDialogUtil
										.openProgressDialog(c, "", "");
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
										ToastUtils.showShortToast(c, error_msg);
										if (success && result == 0) {
											EventBus.getDefault().post(
													new CartChangeEven(true));

										}
									} catch (JSONException e) {
										e.printStackTrace();
									}

								}

							}
						}, true);
						dialog.dismiss();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();

	}

	/**
	 * 秒杀活动商品限制提示框
	 */
	private void showActivityDialog() {
		AlertDialog.Builder builder = new Builder(c);
		builder.setMessage("活动秒杀限购3件，不能贪哦");
		builder.setTitle("提示");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();

			}
		});

		builder.create().show();
	}

	/**
	 * 
	 * 
	 * @author Toby
	 * 
	 */
	private class ViewHolder {
		TextView tv_shopname, tv_count, tv_price, tv_product_name, tv_allprice;
		CheckBox checkboxtop, checkboxmid;
		LinearLayout ll_bottomview, ll_midview, ll_topview;
		ImageView iv_picture, im_add, im_reduce, iv_delete;
		EditText et_num;
		TextView tv_skuvalue, tv_discountTextStr;
		ImageView iv_tag_status;
		View v_above;
	}

}
