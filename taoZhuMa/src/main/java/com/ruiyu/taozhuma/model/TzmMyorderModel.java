package com.ruiyu.taozhuma.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 我的订单Model
 * 
 * @author yangfeng
 */

@SuppressWarnings("serial")
public class TzmMyorderModel implements Serializable {
	public Integer orderId;// 订单ID
	public String orderNumber;// 订单号
	public Double orderPrice;// 订单总价格
	public Double espressPrice;// 运费
	public Integer num;// 总产品数量
	public Integer orderStatus;// 订单状态：1待付款,2已付款，待发货,3已发货,4已收货，5已确定
	public Integer shopId;// 店铺ID
	public String shopName;// 店铺名称
	public String consigneeType;// 收货方式
	public int refundId;// 1审核中2全部退货中3全部成功4全部失败0有没退货退款
	public String shopLogo;// 商店logo
	public String isWallet;// 是否为钱包商品（0-为普通商品 或者 普通商品与钱包商品2者混合；1-为钱包商品）
	public List<Carts> carts;

	public class Carts {
		public Integer productId;// 商品ID
		public String productName;// 商品名称
		public Integer productNumber;// 购买数量
		public String productImage;// 商品图片
		public Double price;// 商品价格

		public String skuValue;// 颜色:xx;尺寸:xxx
		public Integer type;// 0-普通商品，1-秒杀商品，2-专题商品，3-活动商品

	}
}
