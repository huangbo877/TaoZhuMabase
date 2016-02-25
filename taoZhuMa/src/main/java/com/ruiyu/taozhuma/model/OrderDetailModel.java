package com.ruiyu.taozhuma.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 订单详情Model
 * 
 * @author lie
 * 
 */

public class OrderDetailModel implements Serializable {
	public int id;// 购物车ID
	public int productId;// 商品ID
	public String productName;// 商品名称
	public int productNumber;// 购买数量
	public String productImage;// 商品图片
	public float allPrice;// 该商品总价
	public int orderId;// 订单ID
	public String orderNumber;// 订单号
	public Double orderPrice;// 订单价格
	public int orderStatus;// 订单状态：0待付款,1已付款，待发货,2已发货,3已收货
	public int storeId;// 商铺ID
	public String storeName;// 商铺名称
	public String name;// 收件人
	public int tel;// 联系电话
	public String address;// 收货地址
	public ArrayList<Cart> carts;
	public ArrayList<Cart> products;
	public Integer comment;//0为未评价1为已评价
	public String addtime;//生成订单时间
	public String comtime;//成交时间
	public String deltime;//发货时间
	public String settime;//确认时间
	
	public class Cart {
		public int id;// 购物车ID
		public int product_id;// 商品ID
		public String product_name;// 商品名称
		public int product_number;// 购买数量
		public String product_image;// 商品图片
		public Double allPrice;// 该商品总价
		public int shopping_number;// 购买数量

	}

}