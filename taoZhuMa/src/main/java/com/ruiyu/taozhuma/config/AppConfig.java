package com.ruiyu.taozhuma.config;

public interface AppConfig {

	public static final int TAG_ADDRESS_ADD = 1046;

	public static final int TAG_ADDRESS_SELECT = 999;
	// 申请
	public static final int APPLY_CONSUMER = 111;
	public static final int APPLY_MANUFACTURER = 222;

	public static final int TAB_NEWEST_BTN = 1;
	public static final int TAB_HOT_BTN = 2;
	public static final int TAB_SHOW_BTN = 3;
	// public static final int TAB_CONTACT_BTN=4;

	// 产品列表的类型
	public static final int PRODUCT_LIST_HOT = 0;
	public static final int PRODUCT_LIST_NEWEST = 1;
	public static final int PRODUCT_LIST_SHOW = 2;
	public static final int GET_NEWEST = 8;// 获取最新产品
	public static final int ERROR = 0;// 程序错误

	// 登录注册模块
	public static final int Login = 11;// 登录
	public static final int Forget_Pass = 12;// 忘记密码
	public static final int Register = 13;// 注册
	public static final int TZM_LOGIN_OUT = 10;// 退出登录
	public static final int TZM_LOGIN_OUT_REQUEST = 100;// 退出登录请求
	public static final int TZM_EDIT_USER_INFO = 101;// 编辑个人资料

	public static final boolean DEBUG = true;
	public static final boolean memoryCacheEnabled = true;
	public static final boolean diskCacheEnabled = true;

	public static final String FILEKEY = "image[]";// 文件上传标示
	public static final String SHARED_PREFERENCE_NAME = "USER_INFO";// SharedPreferences配置名
	public static final String WebViewHost = "http://b2c.taozhuma.com";// 场景详情webview地址
	// 图形验证设置获取id，challenge，success的URL，需替换成自己的服务器URL
	public static final String CAPTCHAURL = "http://b2c.taozhuma.com/v1.8/getValidCode.do";

	// public static final String HOST = "http://b2c.taozhuma.com";
	// public static final String HOST = "http://testb2c.taozhuma.com";// 测试地址
	// public static final String HOST = "http://b2c.taozhuma.com/v1.0"; //
	// 1.5版本地址
	// public static final String HOST = "http://testb2c.taozhuma.com/v1.0";//
	// 1.5测试版本地址
	// public static final String HOST =
	// "http://testb2c.taozhuma.com/v1.6";//1.6测试版本地址
	// public static final String HOST =
	// "http://testb2c.taozhuma.com/v1.7";//1.7测试版本地址
	// public static final String HOST = "http://b2c.taozhuma.com/v1.7";//
	// 1.7版本地址
	// public static final String HOST =
	// "http://testb2c.taozhuma.com/v1.8";//1.8测试版本地址
	public static final String HOST = "http://testb2c.taozhuma.com/v2.1";// 1.8版本地址

	// 淘竹马
	public static final String USERWELLETDETAIL_URL = HOST
			+ "/userWelletDetail.do";// 查看钱包明细接口
	public static final String TZM_GETCOMMENTPIC_URL = HOST
			+ "/getCommentPic.do";// 获取店铺主推产品图片
	public static final String TZM_APPACTIVITYLIST_URL = HOST
			+ "/appActivityList.do";// app活动列表
	public static final String TZM_DELCART_URL = HOST + "/delCart.do";// 删除购物车产品
	public static final String TZM_SEARCHHELP_URL = HOST + "/searchHelp.do";// 帮助中心—搜索帮助中心
	public static final String TZM_HELPCENTERLIST_URL = HOST
			+ "/helpCenterList.do";// 常见问题类型列表
	public static final String TZM_HELPCENTER_URL = HOST + "/helpCenter.do";// 常见问题类型
	public static final String TZM_FOCUS_URL = HOST + "/focus.do";// 首页焦点图
	public static final String TZM_PRODUCT_URL = HOST + "/product.do";// 产品列表
	public static final String TZM_ADDORDERBYPURCHASE_URL = HOST
			+ "/addOrderByPurchase.do";// 直接购买提交订单
	public static final String TZM_NEW_PRODUCT_URL = HOST + "/activity.do";// 新品快递/竹马推荐/每日♥品/家有好货
	public static final String TZM_SHOP_LIST_URL = HOST + "/shopList.do";// 店铺街
	public static final String TZM_PRODUCT_DETAIL_URL = HOST
			+ "/productDetailApi.do";// 产品详情
	public static final String TZM_PRODUCT_FOCUSIMG_URL = HOST
			+ "/productDetailFocusImg.do";// 产品焦点图
	public static final String TZM_CART_LIST_URL = HOST + "/mycart.do";// 查看购物车
	public static final String TZM_EDITCART_URL = HOST + "/toEditCart.do";// 修改购物车数量
	public static final String TZM_COLLECT_URL = HOST + "/favorites.do";// 我的收藏
	public static final String TZM_PRODETILIMG_URL = HOST
			+ "/productDetailImg.do";// 产品描述图
	public static final String TZM_PRODUCTDETAILCOMMENT_URL = HOST
			+ "/productDetailComment.do";// 产品评价详情
	public static final String SEARCHFAVORITE_URL = HOST + "/isFavorite.do";// 判断收藏是否存在
	public static final String ADDFAVORITES_URL = HOST + "/addFavorite.do";// 添加收藏
	public static final String DELFAVORITE_URL = HOST + "/delFavorite.do";// 删除收藏
	public static final String TZM_ADDPURCHASE_URL = HOST + "/addPurchase.do";// 直接购买
	public static final String TZM_ADDCART_URL = HOST + "/toAddcart.do";// 添加购物车
	public static final String TZM_ADDRESS_URL = HOST + "/myaddress.do";// 地址管理
	public static final String TZM_ORDERPRODUCT_URL = HOST + "/orderProduct.do";// 选中提交购物车
	public static final String TZM_ADDORDER_URL = HOST + "/addorder.do";// 提交订单
	public static final String TZM_SHOP_DETAIL_URL = HOST + "/shopDetail.do";// 店铺详情
	public static final String TZM_DELADDRESS_URL = HOST + "/deleteAddress.do";// 删除地址
	public static final String TZM_INFO_LIST_URL = HOST + "/newsList.do";// 行业资讯
	public static final String TZM_INFO_DETAIL_URL = HOST + "/newsDetail.do";// 行业资讯详情
	public static final String TZM_CAPTCHA_URL = HOST + "/captcha.do";// 发送手机验证码
	public static final String TZM_REGISTER_URL = HOST + "/register.do";// 提交注册信息
	public static final String TZM_LOGIN_URL = HOST + "/login.do";// 会员登录
	public static final String TZM_AGENTLOGIN_URL = HOST + "/agentlogin.do";// 会员登录
	public static final String TZM_PASSWORD_URL = HOST + "/password.do";// 修改密码
	public static final String TZM_PRODUCTTYPE_URL = HOST + "/productType.do";// 产品分类
	public static final String TZM_MYORDER_URL = HOST + "/myorder.do";// 我的订单
	public static final String TZM_CANCELORDER_URL = HOST + "/cancelOrder.do";// 取消订单
	public static final String TZM_CLIENTINFO_URL = HOST + "/clientInfo.do";// 消息
	public static final String TZM_CLIENTINFODETAIL_URL = HOST
			+ "/clientInfoDetail.do";// 消息详情
	public static final String TZM_ORDERDETAIL_URL = HOST + "/orderdetail.do";// 订单详情
	public static final String TZM_ORDERDETAILFORAGENCY_URL = HOST
			+ "/orderdetailForAgency.do";// 批发商订单详情
	public static final String TZM_EXPRESS_URL = HOST + "/express.do";// 查看物流
	public static final String TZM_FEEDBACK_URL = HOST + "/api/feedback.do";// 问题反馈
	public static final String TZM_EDITORDERSTATUS_URL = HOST
			+ "/editOrderStatus.do";// 确认订单
	public static final String TZM_RESETPASSWORD_URL = HOST
			+ "/reSetPassWord.do";// 修改密码
	public static final String TZM_ADDADDRESS_URL = HOST + "/addAddress.do";// 新增地址
	public static final String TZM_PROVINCE_URL = HOST + "/province.do";// 查询省市区名称列表
	public static final String TZM_SELECTADDRESS_URL = HOST
			+ "/selectAddress.do";// 设置默认地址
	public static final String TZM_ADDRESSDETAIL_URL = HOST
			+ "/addressDetail.do";// 我的地址详情
	public static final String TZM_EIDTADDRESS_URL = HOST + "/eidtAddress.do";// 修改地址
	public static final String TZM_FOCUS_ACT_URL = HOST
			+ "/getFocusActivityProduct.do";// 焦点活动
	public static final String TZM_APPLYCONSUMER_URL = HOST
			+ "/applyConsumerApi.do";// 填写注册类型信息（分销商）
	public static final String TZM_APPLYMANUFACTURE_URL = HOST
			+ "/applyManufacturerApi.do";// 填写注册类型信息（供应商）
	public static final String TZM_SEARCHALL_URL = HOST + "/searchAll.do";// 搜索全部
	public static final String TZM_EVALUATION_URL = HOST + "/evaluation.do";// 评价返回未申请退货退款售后订单详情数据
	public static final String TZM_COMMENT_URL = HOST + "/comment.do";// 订单评价
	public static final String TZM_APPLYREFUND_URL = HOST + "/applyRefund.do";// 申请退货、退款
																				// 、售后
	public static final String TZM_HELPCENTERDETAIL_URL = HOST
			+ "/helpCenterDetail.do";// 帮助中心—常见问题详情
	public static final String TZM_MYREFUND_URL = HOST + "/myRefund.do";// 退款维权列表
	public static final String TZM_LOGISTICS_URL = HOST + "/logistics.do";// 查询所有物流公司表
	public static final String TZM_SUBMITLOGISTICS_URL = HOST
			+ "/SubmitLogistics.do";// 填写物流信息
	public static final String TZM_DEFAULTADDRESS_URL = HOST
			+ "/defaultAddress.do";// 返回默认地址
	public static final String TZM_PRODUCTLISTS_URL = HOST + "/productLists.do";// 供应商产品列表
	public static final String TZM_MANUFACTURERCOMMENTLIST_URL = HOST
			+ "/manufacturerCommentList.do";// 产品评价列表
	public static final String TZM_MANUFACTURERSHOP_URL = HOST
			+ "/manufacturerShop.do";// 供应商店铺信息
	public static final String TZM_UPDATESHOPINFO_URL = HOST
			+ "/updateShopInfo.do";// 供应商店铺信息
	public static final String TZM_PRODUCTDETAILHTML_URL = HOST
			+ "/productDetailHtml.do";// 产品描述图HTML格式
	public static final String TZM_FOOT_URL = HOST + "/foot.do";// 我的足迹
	public static final String TZM_TOMYCART = HOST + "/tomycart.do";// b2c购物车
	public static final String TZM_MYINFO_URL = HOST + "/myInfo.do";// 我的基本信息
	public static final String TZM_APPLYAGENCYAPI_URL = HOST
			+ "/applyAgencyApi.do";// 填写注册类型信息（代理商）
	public static final String TZM_AGENCYPRODUCTLISTS_URL = HOST
			+ "/agencyProductLists.do";// 批发商的代理商家产品
	public static final String TZM_TOSETAGENCYPRODUCT_URL = HOST
			+ "/toSetAgencyProduct.do";// 设置批发商的代理商品
	public static final String TZM_AGENCYPRODUCTS_URL = HOST
			+ "/agencyProducts.do";// 批发商的代理产品
	public static final String TZM_UPDATEAGENCYINVENTORY_URL = HOST
			+ "/updateAgencyInventory.do";// 修改批发商产品库存数量
	public static final String TZM_AGENCYPRODUCTIDS_URL = HOST
			+ "/agencyProductIds.do";// 批发商的代理商家所有产品ID
	public static final String TZM_AGENCYPUSH_URL = HOST + "/agencyPush.do";// 推给批发商的订单百度推送
	public static final String TZM_SETAGENCYPUSH_URL = HOST
			+ "/setAgencyPush.do";// 添加推给批发商的订单百度推送记录
	public static final String TZM_MYCARTNUM_URL = HOST + "/myCartNum.do";// 查询购物车产品类型数量
	public static final String TZM_FOOTNEW_URL = HOST + "/footNew.do";// b2c我的足迹
	public static final String TZM_AGENTORDER_URL = HOST + "/agentOrder.do";// 批发商订单
	public static final String TZM_DELIVERY_URL = HOST + "/delivery.do";// 批发商发货填写物流信息
	public static final String TZM_GETACTIVITYPRODUCTBYTYPE_URL = HOST
			+ "/getActivityProductByType.do";// 活动产品列表返回
	public static final String TZM_HOMEPAGEFLOORLIST_URL = HOST
			+ "/homePageFloorList.do";// 首页分类活动推送
	public static final String TZM_HOMEPAGETOPLIST_URL = HOST
			+ "/homePageTop4List.do";// 首页顶部活动推送
	public static final String TZM_GETUSERINFO_URL = HOST + "/getUserInfo.do";// 查询登录用户信息
	public static final String TZM_EDITUSERINFO_URL = HOST + "/editUserInfo.do";// 编辑登录用户信息
	public static final String GETAREAINFO_URL = HOST + "/getAreaInfo.do";//
	public static final String PAYORDER_URL = HOST + "/payOrder.do";// 订单付款
	public static final String ACTIVITYTIMELIST_URL = HOST
			+ "/activityTimeList.do";// 秒杀活动时间列表返回
	public static final String ACTIVITYGOODS_URL = HOST + "/activityGoods.do";// 秒杀活动产品列表
	public static final String GETUSERCOUPONLIST_URL = HOST
			+ "/getUserCouponList.do";// 用户优惠券列表
	public static final String ADDUSERCOUPON_URL = HOST + "/addUserCoupon.do";// 绑定用户优惠券
	public static final String GETVALIDUSERCOUPON_URL = HOST
			+ "/getValidUserCoupon.do";// 订单可用优惠券列表
	public static final String GETPRODUCTSKULINK_URL = HOST
			+ "/getProductSkuLink.do";// 商品SKU信息
	public static final String GETPRODUCTSKULIST_URL = HOST
			+ "/getProductSkuList.do";// 商品SKU颜色/规格列表返回
	public static final String SCENE_ACT_URL = HOST + "/scenesList.do";// 场景/套餐列表返回
	public static final String PACKAGE_URL = HOST + "/addPkgPurchase.do";// 套餐购买
	public static final String PACKAGE_SUB_URL = HOST
			+ "/addPkgOrderByPurchase.do";// 套餐购买提交
	public static final String SCENESLIST_URL = HOST + "/api/scenesList.do";// 首页场景套餐列表
	public static final String ADDPKGPURCHASE_URL = HOST
			+ "/api/addPkgPurchase.do";// 套餐直接购买
	public static final String ADDPKGORDERBYPURCHASE_URL = HOST
			+ "/api/addPkgOrderByPurchase.do";// 套餐直接购买提交订单

	public static final String PRODUCTIMAGE_URL = HOST
			+ "/api/productDetailApp.do";// 7.10产品描述图（app）
	public static final String REFUNDDETAILS_URL = HOST + "/refundDetails.do";// 15.5退款详情
	public static final String GUESSFAVORITE_URL = HOST
			+ "/getUserFootByProduct.do";// 23.1猜你喜欢
	public static final String REFUNDEXPRESS_URL = HOST + "/refundExpress.do";// 用户退货
																				// 查看退货的物流信息
	public static final String SUBMITLOGISTICS_URL = HOST
			+ "/SubmitLogistics.do";// 用户填写退货的物流信息
	public static final String REFUNDONEDETAILS_URL = HOST
			+ "/refundOneDetails.do";// 用户填写退货的物流信息
	public static final String GETHOMEPGSPECIALSHOWLIST_URL = HOST
			+ "/getHomepgSpecialShowList.do";// 首页专场列表
	public static final String GETSPECIALPRODUCTLISTAPI_URL = HOST
			+ "/getSpecialProductListApi.do";// 专场产品列表
	public static final String DELSINGLEFAVORITE_URL = HOST
			+ "/delSingleFavorite.do";// 删除收藏(单个)
	public static final String GETSPECIALSHOWLISTAPI_URL = HOST
			+ "/getSpecialShowListApi.do";// 专场接口
	public static final String GETNAVIGATIONLISTAPI_URL = HOST
			+ "/getNavigationListApi.do";// 导航分类列表
	public static final String SUBMITINVITECODE_URL = HOST
			+ "/submitInviteCode.do";// 提交邀请码
	public static final String CHECKINVITECODE_URL = HOST
			+ "/checkInviteCode.do";// 邀请验证
	public static final String ACTIVITYSPLASHSCREEN = HOST
			+ "/activitySplashScreen.do";// 活动闪屏接口
	public static final String GETREFSELLERINFOBYORDERID_URL = HOST
			+ "/getRefSellerInfoByOrderId.do";// 获取退货商家信息
	public static final String BRANDACTIVITYLIST_URL = HOST
			+ "/brandActivityList.do";// 大牌驾到列表
	public static final String GETSCENELIST_URL = HOST + "/getSceneList.do";// 场景推荐
	public static final String HOMEPAGEICON_URL = HOST + "/homePageIcon.do";// 首页图标列表
	public static final String WALLETACTIVITY_URL = HOST + "/walletActivity.do";// 钱包专区列表
	public static final int TAB_HOME_BTN = 0;
	public static final int TAB_NEW_BTN = 1;
	public static final int TAB_MSG_BTN = 2;
	public static final int TAB_SHOP_BTN = 3;
	public static final int TAB_WODE_BTN = 4;

	// Message What
	public static final int AUTO_GALLERY = 1;
	public static final int API_HTTP_ONEXCEPTION = 0;
	public static final int API_HTTP_ONERROR = 1;
	public static final int API_HTTP_ONCOMPLETE = 2;

	public static final int REQUESTCODE_SHOP = 9;
	public static final int REQUESTCODE_WODE = 10;
	public static final int REQUESTCODE_LOGIN = 11;// 登陆成功
	public static final int REQUESTCODE_REGISTER = 12;// 注册成功

	public static final int BARCODE = 81;// 二维码
	public static final int MY_INFO = 82;// 我的基本信息
	public static final int MY_AUTHENTICATE = 83;// 我的基本信息
	public static final int ADD_PRODUCT = 84;// 添加产品

	public static final int TO_UPLOAD_FILE = 1;
	// 上传文件响应
	public static final int UPLOAD_FILE_DONE = 2;
	// 选择文件
	public static final int TO_SELECT_PHOTO = 3;
	// 上传初始化
	public static final int UPLOAD_INIT_PROCESS = 4;
	// 上传中
	public static final int UPLOAD_IN_PROCESS = 5;
	// 服务器错误
	public static final int UPLOAD_SERVER_ERROR_CODE = 6;

	public static final int MY_LOIGN = 6;
}
