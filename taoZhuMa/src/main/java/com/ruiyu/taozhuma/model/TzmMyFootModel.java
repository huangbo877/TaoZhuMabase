package com.ruiyu.taozhuma.model;

import java.io.Serializable;

public class TzmMyFootModel implements Serializable{
	public Float sellingPrice;//销售价格
	public Integer sellNumber;//销量
	public String shopName;//店铺名称
	public Integer distributionPrice;//建议零售价
	public Integer lowestPrice;//最低零售价
	public String image;//图片
	public String productId;//产品id
	public String productName;//产品名称
	public Integer proStatus;//1有效，0下架
}
