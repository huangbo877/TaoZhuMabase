package com.ruiyu.taozhuma.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单详情Model
 * 
 * @author yangfeng
 */

@SuppressWarnings("serial")
public class TzmOrderDetailModel implements Serializable {
	public Integer orderId;// 订单ID
	public String orderNumber;// 订单号
	public String orderPrice;// 订单总价格
	public Double espressPrice;// 运费
	public String deliverType;// 收货方式
	public Integer orderStatus;// 订单状态：1待付款,2已付款，待发货,3已发货,4已收货，5已确定
	public Integer shopId;// 店铺ID
	public String shopName;// 店铺名称
	public String name;// 收件人
	public String tel;// 联系电话
	public String address;// 收货地址
	public String addtime;// 生成订单时间
	public String comtime;// 成交时间
	public String fahuotime;// 发货时间
	public String refundId;// 1审核中2全部退货中3全部成功4全部失败0有没退货退款
	public Integer num;// 总产品数量
	public Integer isExpired;// 订单是否过期(45天前):0否，1是
	public String couponNo;// 代金券号
	public List<Products> product;
    public Integer activityId;
	public class Products implements Serializable {
		public Integer orderDetailId;// 订单详情ID
		public Integer productId;// 商品ID
		public String productName;// 商品名称
		public Integer productNumber;// 购买数量
		public String productImage;// 商品图片
		public String price;// 商品价格
		public String reStatus;// 退货类型(0==>未申请,1=>审核，2=>请退货，3=》退货中，4=》退货成功，5=》退货失败，6=》审核不成功)
		//1020
		public Integer productStatus;//0-下架，1-正常
		public String skuLinkId;//产品skuid
		public String skuValue;//
		public Integer type;//0-普通商品，1-秒杀商品，2-专题商品，3-活动商品
	}
}
