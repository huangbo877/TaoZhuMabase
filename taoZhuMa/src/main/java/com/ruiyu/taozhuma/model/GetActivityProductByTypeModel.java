package com.ruiyu.taozhuma.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class GetActivityProductByTypeModel implements Serializable {
	public Integer activityId;// 变成专场后 添加的ID(活动ID)
	public String type;// 活动类型
	public Integer productId;//
	public String image;// 产品图片
	public String productName;// 产品名称
	public Integer sorting;// 排序
	public String price;// 价格
	public Integer sellNumber;// 销量
	public String recommendation;// 推荐语
	public String discount;// 折扣
	public String sellingPrice;// 产品原价
	public int gid;// 标志id

}
