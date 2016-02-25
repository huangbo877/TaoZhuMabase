package com.ruiyu.taozhuma.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 产品详情Model
 * 
 * @author FU
 * 
 */

@SuppressWarnings("serial")
public class TzmProductDetailModel implements Serializable {
	public Integer sid;// 店铺id
	public String name;// 产品名称
	public String description;// 产品详情
	public String shopName;// 店铺名称
	public String shopImage;// 店铺LOGO
	public String sellNumber;// 销量
	public String image;// 产品主图
	public String isPower;// 是否电动
	public String pack;// 包装方式
	public String age;// 适用年龄
	public String postageType;// 是否包邮
	public String weight;// 重量
	public String location;// 产地
	public String texture;// 产品材质
	public String comCount;// 产品评价数量
	public ArrayList<String> images;// 产品焦点图
	public String model;// 产品型号
	public String status;// 产品审核状态
	public String brand;// 品牌
	public String price;// 单价
	public String sellingPrice;// 商品原价
	public String lowest_price;// 最低零售价
	public String ladderPrice;// 价格
	public String productCmmt;// 商品评分
	public String deliverCmmt;// 发货评分
	public String logisticsCmmt;// 物流评分
	public String endTime;// 活动结束时间
	public String discount;// 折扣

	public int isActive;// 是否活动商品，1-是;0-否
	public int skuFlag;// 是否有sku,0-否，1-是

	public int acType;// 活动类型，0-普通，1-秒杀，2-钱包专区，3-场景4-专场
	public String acTitle;// 活动标题
	public float pCmmt;// 商品满意度

	public ArrayList<Attrs> attribute;

	public class Attrs {
		public String price;// 商品价格
		public String image;// 商品图片
		public String name;// 商品名称
		public Integer productId;// 商品ID
		public Integer activityId;// 活动id

	}

}
