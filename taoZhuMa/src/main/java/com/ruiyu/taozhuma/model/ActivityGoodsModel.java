package com.ruiyu.taozhuma.model;

import java.util.List;

/**
 * 秒杀活动产品列表返回Model
 * 
 * @author FU
 * 
 */
public class ActivityGoodsModel {
	public List<ActivityGoods> products;
	public class ActivityGoods {
		public String sellingPrice;// 产品原价
		public Integer activityNum;// 活动产品数量
		public Integer activityStock;// 活动剩余库存
		public Integer shopId;// 店铺id
		public String productImage;// 产品图片
		public Integer sorting;// 排序
		public String tips;// 活动语，如0.1折
		public String productPrice;// 产品价格
		public String productName;// 产品名称
		public Integer productId;// 产品id
		//1021
		public String skuLinkId;// 产品skuid
		public String skuValue;// 颜色-规格值
		//1023
		public String shopName;

	}

}
