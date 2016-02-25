package com.ruiyu.taozhuma.model;

import java.io.Serializable;
import java.util.List;

import com.ruiyu.taozhuma.model.TzmMyorderModel.Carts;

public class TzmAgentOrderModel implements Serializable{
	public Integer orderId;//订单ID
	public String orderNumber;//订单号
	public String orderPrice;//订单总价格
	public String espressPrice;//运费
	public Integer num;//产品数量
	public Integer orderStatus;//订单状态：1待付款,2已付款，待发货,3已发货,4已收货，5已确定
	public Integer shopId;//店铺ID
	public Integer isExpired;//订单是否过期(45天前)：0否，1是
	public String shopName;//店铺名称
	public String shopLogo;//店铺Logo
	public List<Carts> carts;//
	public class Carts{
		public Integer productId;//商品ID
		public String productName;//商品名称
		public Integer productNumber;//购买数量
		public String productImage;//商品图片
		public String Price;//商品价格
	}
	
	
}
