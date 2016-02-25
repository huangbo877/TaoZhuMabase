package com.ruiyu.taozhuma.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.ruiyu.taozhuma.model.TzmOrderDetailModel.Products;

/**
 * Push订单Model
 * 
 * @author fu
 * 
 */

public class TzmPushOrderModel implements Serializable {
	public Integer agencyUid;//批发商用户Id
	public Integer agencyId;//批发商Id
	public Integer pushStatus;//0推送待回复	1已回复确认	2.回复无效
	public Integer orderId;//订单ID
	public String orderNumber;//订单号
	public Double orderPrice;//订单总价格
	public Integer espressPrice;//运费
	public Integer num;//总产品数量
	public Integer orderStatus;//订单状态：1待付款,2已付款，待发货,3已发货,4已收货，5已确定
	public Integer shopId;//店铺ID
	public String shopName;//店铺名称
	public List<Products> carts;
	public class Products implements Serializable{
		public Integer productId;//商品ID
		public String productName;//商品名称
		public Integer productNumber;//购买数量
		public String productImage;//商品图片
		public Double price;//商品价格
	}
}
