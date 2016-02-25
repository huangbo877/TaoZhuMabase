package com.ruiyu.taozhuma.model;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * 我的订单Model
 * @author lie
 *
 */

public class MyOrderModel implements Serializable{
	public int orderId;//订单ID
	public String orderNumber;//订单号
	public float orderPrice;//订单价格
	public int orderStatus;//订单状态：0待付款,1已付款，待发货,2已发货,3已收货
	public int storeId;//商铺ID
	public String storeName;//商铺名称
	
	
	
	public ArrayList<carts> carts;//购物车商品
	public class carts {
		public int id;//购物车ID
		public int productId;//商品ID
		public String productName;//商品名称
		public int productNumber;//购买数量
		public String productImage;//商品图片
		public float allPrice;//该商品总价
		public String parameter;//产品规格1
		public String parameter2;//产品规格2
		
}
}