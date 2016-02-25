package com.ruiyu.taozhuma.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 *退货单个商品详情
 *
 * @author huangbo
 *
 * 上午9:45:37
 */
public class OrderRefunModel implements Serializable {
	public int id;// 购物车ID
	public int productId;// 商品ID
	public String productName;// 商品名称
	public String refundNumber;// 退回数量
	public String productImage;// 商品图片
	public String status;// 退货状态
	public String price;// 价格
	public int shopId;// 商店id
	//public String skuLinkId;// sku id
	public String skuValue;// 颜色:xx;尺寸:xxx
	public int type;// 商品类型
	public int rtype;// 退货类型
	public String discountPrice;// 优惠金额
	public String refundPrice;// 退款金额

}