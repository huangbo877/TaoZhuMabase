package com.ruiyu.taozhuma.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.R.integer;

import com.lidroid.xutils.db.annotation.Id;

/**
 * 查看购物车Model
 * 
 * @author Fu
 * 
 */

@SuppressWarnings("serial")
public class TzmMyCartModel implements Serializable {
	public Integer cartType;
	public Integer shopId;// 店铺id
	public String shopName;// 店铺名称
	public float price;// 产品价格
	public Integer status;// 是否下架
	public String name;// 产品名称
	public String image;// 产品图片
	public Integer number;// 产品数量
	public Integer cid;// 购物车id
	public Integer productId;// 产品id
	public int count;
	public String holeprice;
	public List<Cart> carts;
	public Boolean isCheck;
	public Integer tag;// 0上1中2下；
	// 1020
	public String sellingPrice;// 产品原价
	public String discount;// 折扣
	public int isActive;// 0-没有活动，1-活动进行中，2-活动结束
	public String skuLinkId;// 产品skuid
	public Integer activityId;// 商品活动id
	public int type;//0-普通商品，1-活动商品，2-秒杀，3-钱包专区，4-场景，5-专场
	public String skuValue;// 颜色:xx;尺寸:xxx
	// 1208
	public int discountType;//0-未设置，1-满减，2-满折
	public List<DiscountText> discountText;
	public String discountTextStr;//折扣标语

	public class Cart {
		public float price;// 产品价格
		public Integer status;// 是否下架0下架
		public String name;// 产品名称
		public Integer shopId;// 店铺id
		public String shopName;// 店铺名称
		public String image;// 产品图片
		public Integer number;// 产品数量
		public Integer cid;// 购物车id
		public Integer productId;// 产品id
		// 1020
		public String sellingPrice;// 产品原价
		public String discount;// 折扣
		public int isActive;// 0-没有活动，1-活动进行中，2-活动结束
		public String skuLinkId;// 产品skuid
		public Integer activityId;// 商品活动id
		public int type;// 0-普通商品，1-秒杀商品，2-专题商品，3-活动商品
		public String skuValue;// 颜色:xx;尺寸:xxx
	}

	public class DiscountText {
		public String m;
		public String om;
	}

}