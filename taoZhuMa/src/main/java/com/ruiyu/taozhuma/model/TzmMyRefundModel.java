package com.ruiyu.taozhuma.model;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class TzmMyRefundModel implements Serializable {
	public Integer orderId;// 订单ID
	public String orderNumber;// 订单号
	public float orderPrice;// 订单总价格
	public Integer espressPrice;// 运费
	public Integer num;// 总产品数量
	public Integer orderStatus;// 订单状态：1待付款,2已付款，待发货,3已发货,4已收货，5已确定
	public Integer isExpired;// 订单是否过期(45天前)：0否，1是
	public Integer shopId;// 店铺ID
	public String shopName;// 店铺名称
	public String shopLogo;// 店铺logo
	public String consigneeType;// 收货方式
	public Integer refundId;// 1全部退货（退款）0有没退货退款
	public Integer reStatus;// 退货状态，0=>未申请，1=>审核，2=>请退货，3=>退货中，4=>退货成功，5=>退货失败，6=>审核不成功，7=>退款成功
	public List<Carts> carts;//

	public class Carts implements Serializable {
		public Integer productId;// 商品ID
		public String productName;// 商品名称
		public Integer productNumber;// 退货数量
		public String productImage;// 商品图片
		public float Price;// 商品价格价
		//1021
		public Integer detailId;
		public String skuLinkId;//产品skuid
		public String skuValue;//颜色:xx;尺寸:xxx
		
	}
}
