package com.ruiyu.taozhuma.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 产品信息Model
 * 
 * @author Toby
 **/
@SuppressWarnings("serial")
public class ProductModel implements Serializable {
	public int id;// 产品id
	public String name;// 产品名称
	public String video;// 视频链接
	public String url;// 微淘链接
	public String slates;// 月销量
	public String inventory;// 库存量
	public ArrayList<Attribute> attribute;// 产品详细属性
	public String[] images;// 产品相册
	public String imageName;// 产品缩略图
	public String itemNum;// 产品型号
	public int typeId;// 产品分类
	public int productId;// 产品id
	public String productNumber;// 购买数量
	public String allPrice;// 当前购物车总价
	public String price;// 商品价格
	public Integer shopId;// 店铺id
	public String productImage;// 商品图片
	public Integer cartId;// 购物车ID
	public String productName;// 商品名称
	public Integer cid;// 购物车ID
	// 1021
	public String skuLinkId;// 产品skuid
	public String skuValue;//
	public String activityId;// 商品活动id
	public String type;// 0-普通商品，1-秒杀商品，2-专题商品，3-活动商品

	public class Attribute implements Serializable {
		public String title;
		public String value;
	}
}