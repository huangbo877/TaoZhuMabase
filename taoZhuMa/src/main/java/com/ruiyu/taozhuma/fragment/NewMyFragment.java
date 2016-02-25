package com.ruiyu.taozhuma.fragment;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ruiyu.taozhuma.R;
import com.ruiyu.taozhuma.activity.InvitationCode1Activity;
import com.ruiyu.taozhuma.activity.InvitationCode2Activity;
import com.ruiyu.taozhuma.activity.InvitationCode3Activity;
import com.ruiyu.taozhuma.activity.InviteWebActivity;
import com.ruiyu.taozhuma.activity.MineWalletActivity;
import com.ruiyu.taozhuma.activity.NewMyFootActivity;
import com.ruiyu.taozhuma.activity.TzmAddDataActivity;
import com.ruiyu.taozhuma.activity.TzmAddressListActivity;
import com.ruiyu.taozhuma.activity.TzmAgencyProductListActivity;
import com.ruiyu.taozhuma.activity.TzmAgentOrderActivity;
import com.ruiyu.taozhuma.activity.TzmCollectActivity2;
import com.ruiyu.taozhuma.activity.TzmEditUserInfoActivity;
import com.ruiyu.taozhuma.activity.TzmHelpTypeListActivity;
import com.ruiyu.taozhuma.activity.TzmLoginRegisterActivity;
import com.ruiyu.taozhuma.activity.TzmMyRefundActivity;
import com.ruiyu.taozhuma.activity.TzmOrderListActivity;
import com.ruiyu.taozhuma.activity.TzmPushOrderListActivity;
import com.ruiyu.taozhuma.activity.TzmSettingActivity;
import com.ruiyu.taozhuma.activity.TzmSpecialCollectionActivity;
import com.ruiyu.taozhuma.activity.UserCouponListActivity;
import com.ruiyu.taozhuma.api.ApiClient;
import com.ruiyu.taozhuma.api.ApiListener;
import com.ruiyu.taozhuma.api.MyInfoApi;
import com.ruiyu.taozhuma.config.AppConfig;
import com.ruiyu.taozhuma.model.BaseModel;
import com.ruiyu.taozhuma.model.MyInfoModel;
import com.ruiyu.taozhuma.model.UserModel;
import com.ruiyu.taozhuma.utils.LogUtil;
import com.ruiyu.taozhuma.utils.PackageUtils;
import com.ruiyu.taozhuma.utils.PhoneUtils;
import com.ruiyu.taozhuma.utils.ProgressDialogUtil;
import com.ruiyu.taozhuma.utils.StringUtils;
import com.ruiyu.taozhuma.utils.ToastUtils;
import com.ruiyu.taozhuma.utils.UserInfoUtils;
import com.ruiyu.taozhuma.widget.CircularImage;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class NewMyFragment extends Fragment {
	// 用户信息
	private Boolean isLogin;
	private UserModel userModel;
	private String[] strs = new String[] { "管理员", "普通会员", "供应商", "采购商", "批发商" };

	private CircularImage img_user_photo;
	private ImageView img_setting;
	private Button btn_login, btn_register;
	private TextView tv_collect_product_num, tv_collect_brand_num,
			tv_collect_footmark_num;
	private TextView tv_collect_product, tv_collect_brand, tv_collect_footmark;
	private TextView tv_user_type, tv_name;
	private RelativeLayout rlayout_1;
	private LinearLayout llayout_1, llayout_2;
	private RelativeLayout rl_daifu, rl_daifahuo, rl_yifahuo, rl_daipingjia;

	private MyInfoApi infoApi;
	private ApiClient apiClient;
	private MyInfoModel infoModel;

	private Integer ADDDATA = 33;

	private ImageView iv_order;
	private TextView tv_order;
	private TextView tv_number1, tv_number2, tv_number3, tv_number4;

	private BitmapUtils imageLoader;
	@ViewInject(R.id.tv_receive)
	private TextView tv_receive;
	@ViewInject(R.id.tv_confirm)
	private TextView tv_confirm;
	// 我的售后
	@ViewInject(R.id.rl_service)
	private RelativeLayout rl_service;
	@ViewInject(R.id.iv_line1)
	private ImageView iv_line1;
	@ViewInject(R.id.iv_line)
	private ImageView iv_line;
	@ViewInject(R.id.grayline01)
	private ImageView grayline01;
	@ViewInject(R.id.tv_number5)
	private TextView tv_number5;
	@ViewInject(R.id.tv_vouchers)
	private TextView tv_vouchers;

	@ViewInject(R.id.rl_order_list)
	private RelativeLayout rl_order_list;
	@ViewInject(R.id.rl_bj)
	private RelativeLayout rl_bj;
	@ViewInject(R.id.rl_address)
	private RelativeLayout rl_address;
	@ViewInject(R.id.rl_myrefund)
	private RelativeLayout rl_myrefund;
	@ViewInject(R.id.rl_adddata)
	private RelativeLayout rl_adddata;
//	@ViewInject(R.id.rl_select)
//	private RelativeLayout rl_select;
	@ViewInject(R.id.rl_product)
	private RelativeLayout rl_product;
	// 代金卷
	@ViewInject(R.id.rl_coupon)
	private RelativeLayout rl_coupon;

	@ViewInject(R.id.rl_wallet)
	private RelativeLayout rl_wallet;
	@ViewInject(R.id.rl_invite)
	private RelativeLayout rl_invite;
	@ViewInject(R.id.rl_invitation)
	private RelativeLayout rl_invitation;
	@ViewInject(R.id.tv_number_coupon)
	private TextView tv_number_coupon;

	@ViewInject(R.id.tv_baby_age)
	private TextView tv_baby_age;// 宝宝岁数

	@ViewInject(R.id.rl_customer_service)
	private RelativeLayout rl_customer_service;// 联系客服

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.tzm_userinfo_activity, null);
		ViewUtils.inject(this, view);
		checkLogin();
		imageLoader = new BitmapUtils(getActivity());
		imageLoader.configDefaultLoadingImage(R.drawable.loding_defult);// 默认背景图片
		imageLoader.configDefaultLoadFailedImage(R.drawable.loding_defult);// 加载失败图片
		imageLoader.configDefaultBitmapConfig(Bitmap.Config.RGB_565);// 设置图片压缩类型
		imageLoader.configMemoryCacheEnabled(true);
		imageLoader.configDiskCacheEnabled(true);
		initView(view);
		
		//// getMyInfo();
		// setView();
		return view;
	}

	private void initView(View view) {
		tv_number1 = (TextView) view.findViewById(R.id.tv_number1);
		tv_number2 = (TextView) view.findViewById(R.id.tv_number2);
		tv_number3 = (TextView) view.findViewById(R.id.tv_number3);
		tv_number4 = (TextView) view.findViewById(R.id.tv_number4);
		iv_order = (ImageView) view.findViewById(R.id.iv_order);
		tv_order = (TextView) view.findViewById(R.id.tv_order);
		img_setting = (ImageView) view.findViewById(R.id.img_setting);
		img_setting.setOnClickListener(clickListener);
		btn_login = (Button) view.findViewById(R.id.btn_login);
		btn_register = (Button) view.findViewById(R.id.btn_register);
		btn_login.setOnClickListener(clickListener);
		btn_register.setOnClickListener(clickListener);

		tv_collect_product_num = (TextView) view
				.findViewById(R.id.tv_collect_product_num);
		tv_collect_product_num.setOnClickListener(clickListener);
		tv_collect_brand_num = (TextView) view
				.findViewById(R.id.tv_collect_brand_num);
		tv_collect_brand_num.setOnClickListener(clickListener);
		tv_collect_footmark_num = (TextView) view
				.findViewById(R.id.tv_collect_footmark_num);
		tv_collect_footmark_num.setOnClickListener(clickListener);
		tv_collect_product = (TextView) view
				.findViewById(R.id.tv_collect_product);
		tv_collect_product.setOnClickListener(clickListener);
		tv_collect_brand = (TextView) view.findViewById(R.id.tv_collect_brand);
		tv_collect_brand.setOnClickListener(clickListener);
		tv_collect_footmark = (TextView) view
				.findViewById(R.id.tv_collect_footmark);
		tv_collect_footmark.setOnClickListener(clickListener);
		img_user_photo = (CircularImage) view.findViewById(R.id.img_user_photo);
		img_user_photo.setOnClickListener(clickListener);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		tv_user_type = (TextView) view.findViewById(R.id.tv_user_type);
		rlayout_1 = (RelativeLayout) view.findViewById(R.id.rlayout_1);
		llayout_1 = (LinearLayout) view.findViewById(R.id.llayout_1);
		llayout_2 = (LinearLayout) view.findViewById(R.id.llayout_2);

		rl_daifu = (RelativeLayout) view.findViewById(R.id.rl_daifu);
		rl_daifahuo = (RelativeLayout) view.findViewById(R.id.rl_daifahuo);
		rl_yifahuo = (RelativeLayout) view.findViewById(R.id.rl_yifahuo);
		rl_daipingjia = (RelativeLayout) view.findViewById(R.id.rl_daipingjia);

		rl_wallet = (RelativeLayout) view.findViewById(R.id.rl_wallet);
		rl_invite = (RelativeLayout) view.findViewById(R.id.rl_invite);
		rl_invitation = (RelativeLayout) view.findViewById(R.id.rl_invitation);

		rl_daifu.setOnClickListener(clickListener);
		rl_daifahuo.setOnClickListener(clickListener);
		rl_yifahuo.setOnClickListener(clickListener);
		rl_daipingjia.setOnClickListener(clickListener);
		//
		rl_wallet.setOnClickListener(clickListener);
		rl_invite.setOnClickListener(clickListener);
		rl_invitation.setOnClickListener(clickListener);
		apiClient = new ApiClient(getActivity());
		// 我的售后
		rl_service.setOnClickListener(clickListener);
		// 查看全部订单
		rl_order_list.setOnClickListener(clickListener);
		rl_address.setOnClickListener(clickListener);
		rl_myrefund.setOnClickListener(clickListener);
		rl_adddata.setOnClickListener(clickListener);
//		rl_select.setOnClickListener(clickListener);
		rl_product.setOnClickListener(clickListener);
		rl_coupon.setOnClickListener(clickListener);
		rl_customer_service.setOnClickListener(clickListener);
	}

	@SuppressWarnings("deprecation")
	private void setView() {
		checkLogin();
		if (isLogin) {
			// rl_customer_service.setVisibility(View.VISIBLE);// 联系客服
			llayout_1.setVisibility(View.GONE);
			rlayout_1.setVisibility(View.VISIBLE);
			tv_name.setText(userModel.name);
			img_user_photo.setVisibility(View.VISIBLE);

			if (userModel.type == 6) {
				rl_service.setVisibility(View.GONE);
				tv_receive.setText("待确认");
				tv_confirm.setText("已确认");
				tv_user_type.setText(strs[4]);
				llayout_2.setVisibility(View.GONE);
				rl_address.setVisibility(View.GONE);
				rl_invitation.setVisibility(View.GONE);
				rl_invite.setVisibility(View.GONE);
				iv_line.setVisibility(View.GONE);
				iv_line1.setVisibility(View.GONE);
				rl_bj.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.tzm_240));
				if (userModel.status == 0) {
					rl_adddata.setVisibility(View.VISIBLE);
					rl_coupon.setVisibility(View.GONE);
//					rl_select.setVisibility(View.GONE);
					// grayline01.setVisibility(View.GONE);
					rl_product.setVisibility(View.GONE);
				} else {
					rl_adddata.setVisibility(View.GONE);
					rl_coupon.setVisibility(View.GONE);
//					rl_select.setVisibility(View.VISIBLE);
					// grayline01.setVisibility(View.VISIBLE);
					rl_product.setVisibility(View.VISIBLE);
				}
			} else {
				rl_service.setVisibility(View.VISIBLE);
				tv_receive.setText("待收货");
				tv_confirm.setText("待评价");
				rl_adddata.setVisibility(View.GONE);
//				rl_select.setVisibility(View.GONE);
				// grayline01.setVisibility(View.GONE);
				rl_product.setVisibility(View.GONE);
				llayout_2.setVisibility(View.VISIBLE);
				rl_address.setVisibility(View.VISIBLE);
				iv_line.setVisibility(View.VISIBLE);
				iv_line1.setVisibility(View.VISIBLE);
				rl_coupon.setVisibility(View.VISIBLE);
				rl_invite.setVisibility(View.VISIBLE);
				rl_invitation.setVisibility(View.VISIBLE);
				if (userModel.type == 0) {
					tv_user_type.setText(strs[0]);
				} else if (userModel.type == 1) {
					tv_user_type.setText(strs[1]);
				} else if (userModel.type == 2) {
					tv_user_type.setText(strs[2]);
				} else if (userModel.type == 3) {
					tv_user_type.setText(strs[3]);
				}
			}

		} else {
			// rl_customer_service.setVisibility(View.GONE);// 联系客服
			tv_baby_age.setVisibility(View.GONE);
			rl_service.setVisibility(View.GONE);
			iv_line.setVisibility(View.GONE);
			iv_line1.setVisibility(View.GONE);
			tv_receive.setText("待收货");
			tv_confirm.setText("待评价");
			llayout_1.setVisibility(View.VISIBLE);
			llayout_2.setVisibility(View.VISIBLE);
			rlayout_1.setVisibility(View.INVISIBLE);
			img_user_photo.setVisibility(View.INVISIBLE);
			rl_address.setVisibility(View.VISIBLE);
//			rl_select.setVisibility(View.GONE);
			// grayline01.setVisibility(View.GONE);
			rl_product.setVisibility(View.GONE);
			rl_adddata.setVisibility(View.GONE);
			rl_invitation.setVisibility(View.GONE);
			rl_invite.setVisibility(View.GONE);
			tv_collect_product_num.setText("0");
			tv_collect_brand_num.setText("0");
			tv_collect_footmark_num.setText("0");
			rl_bj.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.tzm_211));
		}

	}

	// 检查用户是否登陆
	private void checkLogin() {

		isLogin = UserInfoUtils.isLogin();
		if (isLogin) {
			userModel = UserInfoUtils.getUserInfo();
		}
	}

	/**
	 * 建立与融云服务器的连接
	 * 
	 * @param token
	 */
	// private void connect(String token) {
	//
	// if (getActivity().getApplicationInfo().packageName
	// .equals(BaseApplication.getCurProcessName(getActivity()))) {
	//
	// /**
	// * IMKit SDK调用第二步,建立与服务器的连接
	// */
	// RongIM.connect(token, new RongIMClient.ConnectCallback() {
	//
	// /**
	// * Token 错误，在线上环境下主要是因为 Token 已经过期，您需要向 App Server 重新请求一个新的
	// * Token
	// */
	// @Override
	// public void onTokenIncorrect() {
	// ProgressDialogUtil.closeProgressDialog();
	// ToastUtils
	// .showShortToast(getActivity(), "Token 已经过期,请重新登录");
	// }
	//
	// /**
	// * 连接融云成功
	// *
	// * @param userid
	// * 当前 token
	// */
	// @Override
	// public void onSuccess(String userid) {
	// ProgressDialogUtil.closeProgressDialog();
	// Log.d("LoginActivity", "--onSuccess" + userid);
	// if (RongIM.getInstance() != null) {
	// RongIM.getInstance().startCustomerServiceChat(
	// getActivity(), "KEFU1442032031885", "在线客服");
	// }
	// }
	//
	// /**
	// * 连接融云失败
	// *
	// * @param errorCode
	// * 错误码，可到官网 查看错误码对应的注释
	// */
	// @Override
	// public void onError(RongIMClient.ErrorCode errorCode) {
	// ProgressDialogUtil.closeProgressDialog();
	// Log.d("LoginActivity", "--onError" + errorCode);
	// }
	// });
	// }
	// }

	View.OnClickListener clickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			// 联系客服
			case R.id.rl_customer_service:
				displaySelectionList();
				break;
			// 代金卷
			case R.id.rl_coupon:
				checkLogin();
				if (isLogin) {
					startActivity(new Intent(getActivity(),
							UserCouponListActivity.class));
				} else {
					ToastUtils
							.showShortToast(getActivity(), R.string.login_tip);
					Intent intent = new Intent(new Intent(getActivity(),
							TzmLoginRegisterActivity.class));
					intent.putExtra("type", 0);
					startActivityForResult(intent, AppConfig.Login);
				}
				break;

			case R.id.img_setting:
				Intent intent_btn_Setting = new Intent(getActivity(),
						TzmSettingActivity.class);
				startActivityForResult(intent_btn_Setting,
						AppConfig.TZM_LOGIN_OUT_REQUEST);
				break;
			case R.id.btn_register:
				Intent intent_btn_register = new Intent(getActivity(),
						TzmLoginRegisterActivity.class);
				intent_btn_register.putExtra("type", 1);
				startActivityForResult(intent_btn_register,
						AppConfig.REQUESTCODE_REGISTER);
				break;
			case R.id.btn_login:
				Intent intent_btn_login = new Intent(getActivity(),
						TzmLoginRegisterActivity.class);
				intent_btn_login.putExtra("type", 0);
				startActivityForResult(intent_btn_login,
						AppConfig.REQUESTCODE_LOGIN);
				break;

			case R.id.rl_daifu:
				checkLogin();
				if (isLogin) {
					if (userModel.type == 6) {
						Intent qiangdanIntent = new Intent(getActivity(),
								TzmPushOrderListActivity.class);
						// daifuIntent.putExtra("mark", 1);
						startActivity(qiangdanIntent);
					} else {
						Intent daifuIntent = new Intent(getActivity(),
								TzmOrderListActivity.class);
						daifuIntent.putExtra("mark", 1);
						startActivity(daifuIntent);
					}

				} else {
					ToastUtils
							.showShortToast(getActivity(), R.string.login_tip);
					Intent intent = new Intent(new Intent(getActivity(),
							TzmLoginRegisterActivity.class));
					intent.putExtra("type", 0);
					startActivityForResult(intent, AppConfig.Login);
				}
				break;
			case R.id.rl_daifahuo:
				checkLogin();
				if (isLogin) {
					if (userModel.type == 6) {
						Intent intent2 = new Intent(getActivity(),
								TzmAgentOrderActivity.class);
						intent2.putExtra("mark", 2);
						startActivity(intent2);
					} else {
						Intent daifahuoIntent = new Intent(getActivity(),
								TzmOrderListActivity.class);
						daifahuoIntent.putExtra("mark", 2);
						startActivity(daifahuoIntent);
					}
				} else {
					ToastUtils
							.showShortToast(getActivity(), R.string.login_tip);
					Intent intent = new Intent(new Intent(getActivity(),
							TzmLoginRegisterActivity.class));
					intent.putExtra("type", 0);
					startActivityForResult(intent, AppConfig.Login);
				}
				break;
			case R.id.rl_yifahuo:
				checkLogin();
				if (isLogin) {
					if (userModel.type == 6) {
						Intent intent3 = new Intent(getActivity(),
								TzmAgentOrderActivity.class);
						intent3.putExtra("mark", 3);
						startActivity(intent3);
					} else {
						Intent yifahuoIntent = new Intent(getActivity(),
								TzmOrderListActivity.class);
						yifahuoIntent.putExtra("mark", 3);
						startActivity(yifahuoIntent);
					}
				} else {
					ToastUtils
							.showShortToast(getActivity(), R.string.login_tip);
					Intent intent = new Intent(new Intent(getActivity(),
							TzmLoginRegisterActivity.class));
					intent.putExtra("type", 0);
					startActivityForResult(intent, AppConfig.Login);
				}
				break;
			case R.id.rl_daipingjia:
				checkLogin();
				if (isLogin) {
					if (userModel.type == 6) {
						Intent intent4 = new Intent(getActivity(),
								TzmAgentOrderActivity.class);
						intent4.putExtra("mark", 4);
						startActivity(intent4);
					} else {
						Intent daipingjiaIntent = new Intent(getActivity(),
								TzmOrderListActivity.class);
						daipingjiaIntent.putExtra("mark", 4);
						startActivity(daipingjiaIntent);
					}
				} else {
					ToastUtils
							.showShortToast(getActivity(), R.string.login_tip);
					Intent intent = new Intent(new Intent(getActivity(),
							TzmLoginRegisterActivity.class));
					intent.putExtra("type", 0);
					startActivityForResult(intent, AppConfig.Login);
				}
				break;
			// 查看全部订单
			case R.id.rl_order_list:
				checkLogin();
				if (isLogin) {
					if (userModel.type == 6) {
						Intent intent0 = new Intent(getActivity(),
								TzmAgentOrderActivity.class);
						intent0.putExtra("mark", 0);
						startActivity(intent0);
					} else {
						Intent intent_order_list = new Intent(getActivity(),
								TzmOrderListActivity.class);
						startActivity(intent_order_list);
					}
				} else {
					ToastUtils
							.showShortToast(getActivity(), R.string.login_tip);
					Intent intent = new Intent(new Intent(getActivity(),
							TzmLoginRegisterActivity.class));
					intent.putExtra("type", 0);
					startActivityForResult(intent, AppConfig.Login);
				}
				break;
			case R.id.rl_address:
				checkLogin();
				if (isLogin) {
					Intent intent = new Intent(getActivity(),
							TzmAddressListActivity.class);
					startActivity(intent);
				} else {
					ToastUtils
							.showShortToast(getActivity(), R.string.login_tip);
					Intent intent = new Intent(new Intent(getActivity(),
							TzmLoginRegisterActivity.class));
					intent.putExtra("type", 0);
					startActivityForResult(intent, AppConfig.Login);
				}
				break;
			case R.id.rl_myrefund:
				startActivity(new Intent(getActivity(),
						TzmHelpTypeListActivity.class));
				break;
			case R.id.img_user_photo:
				checkLogin();
				if (isLogin) {
					if (userModel.type != 6) {
						Intent intent_UserInfo = new Intent(getActivity(),
								TzmEditUserInfoActivity.class);
						startActivityForResult(intent_UserInfo,
								AppConfig.TZM_EDIT_USER_INFO);
					}

				} else {
					ToastUtils
							.showShortToast(getActivity(), R.string.login_tip);
					Intent intent = new Intent(new Intent(getActivity(),
							TzmLoginRegisterActivity.class));
					intent.putExtra("type", 0);
					startActivityForResult(intent, AppConfig.Login);
				}
				break;
			case R.id.rl_adddata:
				checkLogin();
				if (isLogin) {
					Intent intent_add_data = new Intent(getActivity(),
							TzmAddDataActivity.class);
					startActivityForResult(intent_add_data, ADDDATA);
				} else {
					ToastUtils
							.showShortToast(getActivity(), R.string.login_tip);
					Intent intent = new Intent(new Intent(getActivity(),
							TzmLoginRegisterActivity.class));
					intent.putExtra("type", 0);
					startActivityForResult(intent, AppConfig.Login);
				}

				break;
//			case R.id.rl_select:
//				checkLogin();
//				if (isLogin) {
//					Intent intent_select = new Intent(getActivity(),
//							SelectProductActivity.class);
//					startActivity(intent_select);
//				} else {
//					ToastUtils
//							.showShortToast(getActivity(), R.string.login_tip);
//					Intent intent = new Intent(new Intent(getActivity(),
//							TzmLoginRegisterActivity.class));
//					intent.putExtra("type", 0);
//					startActivityForResult(intent, AppConfig.Login);
//				}
//				break;
			// 我的售后点击操作
			case R.id.rl_service:
				checkLogin();
				if (isLogin) {
					Intent intent_btn_myrefund = new Intent(getActivity(),
							TzmMyRefundActivity.class);
					startActivity(intent_btn_myrefund);
				} else {
					ToastUtils
							.showShortToast(getActivity(), R.string.login_tip);
					Intent intent = new Intent(new Intent(getActivity(),
							TzmLoginRegisterActivity.class));
					intent.putExtra("type", 0);
					startActivityForResult(intent, AppConfig.Login);
				}
				break;
			case R.id.rl_product:
				checkLogin();
				if (isLogin) {
					Intent intent_product = new Intent(getActivity(),
							TzmAgencyProductListActivity.class);
					startActivity(intent_product);
				} else {
					ToastUtils
							.showShortToast(getActivity(), R.string.login_tip);
					Intent intent = new Intent(new Intent(getActivity(),
							TzmLoginRegisterActivity.class));
					intent.putExtra("type", 0);
					startActivityForResult(intent, AppConfig.Login);
				}
				break;
			// 商品收藏的数量
			case R.id.tv_collect_product_num:
				checkLogin();
				if (isLogin) {
					Intent intent_collect = new Intent(getActivity(),
							TzmCollectActivity2.class);
					startActivity(intent_collect);
				} else {
					ToastUtils
							.showShortToast(getActivity(), R.string.login_tip);
					Intent intent = new Intent(new Intent(getActivity(),
							TzmLoginRegisterActivity.class));
					intent.putExtra("type", 0);
					startActivityForResult(intent, AppConfig.Login);
				}
				break;
			// 商品收藏
			case R.id.tv_collect_product:
				checkLogin();
				if (isLogin) {
					Intent intent_collect = new Intent(getActivity(),
							TzmCollectActivity2.class);
					startActivity(intent_collect);
				} else {
					ToastUtils
							.showShortToast(getActivity(), R.string.login_tip);
					Intent intent = new Intent(new Intent(getActivity(),
							TzmLoginRegisterActivity.class));
					intent.putExtra("type", 0);
					startActivityForResult(intent, AppConfig.Login);
				}
				break;
			// 品牌收藏 版本修改,将品牌收藏 换为 专场收藏 --专场收藏的数量
			case R.id.tv_collect_brand_num:
				checkLogin();
				if (isLogin) {
					Intent intent_collect = new Intent(getActivity(),
							TzmSpecialCollectionActivity.class);
					startActivity(intent_collect);
				} else {
					ToastUtils
							.showShortToast(getActivity(), R.string.login_tip);
					Intent intent = new Intent(new Intent(getActivity(),
							TzmLoginRegisterActivity.class));
					intent.putExtra("type", 0);
					startActivityForResult(intent, AppConfig.Login);
				}
				break;
			// 品牌收藏 版本修改,将品牌收藏 换为 专场收藏
			case R.id.tv_collect_brand:
				checkLogin();
				if (isLogin) {

					Intent intent_collect = new Intent(getActivity(),
							TzmSpecialCollectionActivity.class);

					startActivity(intent_collect);
				} else {
					ToastUtils
							.showShortToast(getActivity(), R.string.login_tip);
					Intent intent = new Intent(new Intent(getActivity(),
							TzmLoginRegisterActivity.class));
					intent.putExtra("type", 0);
					startActivityForResult(intent, AppConfig.Login);
				}
				break;
			case R.id.tv_collect_footmark_num:
				checkLogin();
				if (isLogin) {
					Intent intent_ll_foot = new Intent(getActivity(),
							NewMyFootActivity.class);
					startActivity(intent_ll_foot);
				} else {
					ToastUtils
							.showShortToast(getActivity(), R.string.login_tip);
					Intent intent = new Intent(new Intent(getActivity(),
							TzmLoginRegisterActivity.class));
					intent.putExtra("type", 0);
					startActivityForResult(intent, AppConfig.Login);
				}
				break;
			// 我的足迹
			case R.id.tv_collect_footmark:
				checkLogin();
				if (isLogin) {
					Intent intent_ll_foot = new Intent(getActivity(),
							NewMyFootActivity.class);
					startActivity(intent_ll_foot);
				} else {
					ToastUtils
							.showShortToast(getActivity(), R.string.login_tip);
					Intent intent = new Intent(new Intent(getActivity(),
							TzmLoginRegisterActivity.class));
					intent.putExtra("type", 0);
					startActivityForResult(intent, AppConfig.Login);
				}
				break;
			// 我的钱包
			case R.id.rl_wallet:
				checkLogin();
				if (isLogin) {
					Intent intenwa = new Intent(new Intent(getActivity(),
							MineWalletActivity.class));
					startActivity(intenwa);
				} else {
					ToastUtils
							.showShortToast(getActivity(), R.string.login_tip);
					Intent intent = new Intent(new Intent(getActivity(),
							TzmLoginRegisterActivity.class));
					intent.putExtra("type", 0);
					startActivityForResult(intent, AppConfig.Login);
				}
				break;
			// 邀请好友
			case R.id.rl_invite:
				Intent intentw1 = new Intent(new Intent(getActivity(),
						InviteWebActivity.class));
				intentw1.putExtra("id", infoModel.uid);
				startActivity(intentw1);
				break;
			// 输入邀请码
			case R.id.rl_invitation:
				try {
					switch (infoModel.newUser) {
					// 可被邀请
					case 1:
						Intent intentc1 = new Intent(new Intent(getActivity(),
								InvitationCode1Activity.class));
						intentc1.putExtra("id", infoModel.uid);
						startActivity(intentc1);
						break;
					// 老用户
					case 2:
						Intent intentc3 = new Intent(new Intent(getActivity(),
								InvitationCode3Activity.class));
						intentc3.putExtra("id", infoModel.uid);
						startActivity(intentc3);
						break;
					// 已被邀请
					default:
						Intent intent = new Intent(new Intent(getActivity(),
								InvitationCode2Activity.class));
						intent.putExtra("id", infoModel.uid);
						startActivity(intent);
						break;
					}
				} catch (Exception e) {
					// TODO: handle exception
					ToastUtils.showShortToast(getActivity(), "网络异常");
				}
				break;
			}

		}
	};

	private void getMyInfo() {
		tv_number1.setVisibility(View.GONE);
		tv_number2.setVisibility(View.GONE);
		tv_number3.setVisibility(View.GONE);
		tv_number4.setVisibility(View.GONE);
		tv_number5.setVisibility(View.GONE);
		tv_number_coupon.setVisibility(View.GONE);
		checkLogin();
		if (isLogin) {
			infoApi = new MyInfoApi();
			infoApi.setUid(userModel.uid);
			apiClient.api(infoApi, new ApiListener() {
				@Override
				public void onStart() {
					ProgressDialogUtil.openProgressDialog(getActivity(), null,
							null);
				}

				@Override
				public void onException(Exception e) {
					ToastUtils.showShortToast(getActivity(), "网络异常");
					ProgressDialogUtil.closeProgressDialog();
					LogUtil.ErrorLog(e);
				}

				@Override
				public void onError(String error) {
					ProgressDialogUtil.closeProgressDialog();
					LogUtil.Log(error);
					ToastUtils.showShortToast(getActivity(), error);
				}

				@Override
				public void onComplete(String jsonStr) {
					ProgressDialogUtil.closeProgressDialog();
					try {
						Gson gson = new Gson();
						Type type = new TypeToken<BaseModel<MyInfoModel>>() {
						}.getType();
						BaseModel<MyInfoModel> base = gson.fromJson(jsonStr,
								type);
						if (base.success) {
							infoModel = base.result;
							if (userModel.type == 1) {
								tv_baby_age.setVisibility(View.VISIBLE);
								tv_baby_age.setText("宝宝年龄:" + infoModel.age);
							}
							// 代金卷
							if (StringUtils.isNotEmpty(infoModel.couponNum)) {
								tv_number_coupon.setVisibility(View.VISIBLE);

								tv_number_coupon.setText(infoModel.couponNum);

							} else {
								tv_number_coupon.setVisibility(View.GONE);
							}
							if (StringUtils.isNotEmpty(infoModel.waitPay)) {
								tv_number1.setVisibility(View.VISIBLE);

								tv_number1.setText(infoModel.waitPay);

							} else {
								tv_number1.setVisibility(View.GONE);
							}
							if (StringUtils.isNotEmpty(infoModel.waitDeliver)) {
								tv_number2.setVisibility(View.VISIBLE);
								tv_number2.setText(infoModel.waitDeliver);
							} else {
								tv_number2.setVisibility(View.GONE);
							}
							if (StringUtils.isNotEmpty(infoModel.waitReceipt)) {
								tv_number3.setVisibility(View.VISIBLE);
								tv_number3.setText(infoModel.waitReceipt);
							} else {
								tv_number3.setVisibility(View.GONE);
							}
							if (StringUtils.isNotEmpty(infoModel.waitComment)) {
								tv_number4.setVisibility(View.VISIBLE);
								tv_number4.setText(infoModel.waitComment);
							} else {
								tv_number4.setVisibility(View.GONE);
							}
							if (StringUtils.isNotEmpty(infoModel.waitReceive)) {
								tv_number5.setVisibility(View.VISIBLE);
								tv_number5.setText(infoModel.waitReceive);
							} else {
								tv_number5.setVisibility(View.GONE);
							}
							if (StringUtils.isNotEmpty(infoModel.image)
									&& userModel.type != 6) {
								imageLoader.display(img_user_photo,
										infoModel.image);
							} else {
								img_user_photo
										.setBackgroundResource(R.drawable.tzm_223);
							}
							userModel.status = infoModel.status;
							UserInfoUtils.updateUserInfo(userModel);
							tv_collect_product_num
									.setText(infoModel.proFavorites + "");
							tv_collect_brand_num
									.setText(infoModel.shopFavorites + "");
							tv_collect_footmark_num
									.setText(infoModel.myFootprint + "");
							if (userModel.type == 6) {
								iv_order.setImageDrawable(getResources()
										.getDrawable(R.drawable.tzm_order_push));
								tv_order.setText("抢单");
							} else {
								iv_order.setImageDrawable(getResources()
										.getDrawable(R.drawable.icon_obligation));
								tv_order.setText(R.string.app_name6);
							}
						} else {
							LogUtil.Log(base.error_msg);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}, true);
		}
	}

	/**
	 * 联系客服
	 */
	protected void displaySelectionList() {
		final String[] items = new String[] {
				getActivity().getString(R.string.consumer_service_item1),
				getActivity().getString(R.string.consumer_service_item2) };
		new AlertDialog.Builder(getActivity())
				.setTitle("联系客服")
				.setItems(items, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method
						// stub
						switch (which) {
						case 0:
							PhoneUtils.callPhone(getActivity(),
									getString(R.string.consumer_hotline));
							break;
						case 1:
							if (PackageUtils.isApkInstalled(getActivity(),
									"com.tencent.mobileqq")) {
								String url = "mqqwpa://im/chat?chat_type=wpa&uin="
										+ getString(R.string.consumer_hotline);
								startActivity(new Intent(Intent.ACTION_VIEW,
										Uri.parse(url)));
							} else {
								ToastUtils.showShortToast(getActivity(),
										"您尚未安装手机QQ哦");
							}
							break;
						}
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();

					}
				}).show();

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// getMyInfo();
		// setView();

	}

	@Override
	public void onResume() {
		super.onResume();
		getMyInfo();
		setView();
	};
}
