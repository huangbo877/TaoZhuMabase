package com.ruiyu.taozhuma.model;

import java.io.Serializable;
import java.util.List;

public class AgentOrderDetailModel implements Serializable {
	public Integer orderId;// 订单ID
	public String orderNumber;// 订单号
	public Double orderPrice;// 订单总价格
	public Integer espressPrice;// 运费
	public String deliverType;// 收货方式
	public Integer orderStatus;// 订单状态：1待付款,2已付款，待发货,3已发货,4已收货，5已确定

	public Integer isExpired;// (批发商)订单是否过期(45天前)：0否，1是
	public String remark;// (批发商)店铺留言
	public String pushStatus;// (批发商)0推送待回 1已回复确认 2. 回复无效

	public Integer shopId;// 店铺ID
	public String shopName;// 店铺名称
	public String name;// 收件人
	public String tel;// 联系电话
	public String address;// 收货地址
	public String addtime;// 生成订单时间
	public String comtime;// 成交时间
	public String fahuotime;// 发货时间
	public String refundId;// 1全部退货退款申请 0 部分或者没有
	public String shopLogo;// 商店logo
	public List<Products> product;
	public class Products implements Serializable {
		public String orderDetailId;// 订单详情ID
		public Integer productId;// 商品ID
		public String productName;// 商品名称
		public Integer productNumber;// 购买数量
		public String productImage;// 商品图片
		public float price;// 商品价格
		public String reStatus;// 退货类型(0==>未申请,1=>审核，2=>请退货，3=》退货中，4=》退货成功，5=》退货失败，6=》审核不成功)
	}

}
