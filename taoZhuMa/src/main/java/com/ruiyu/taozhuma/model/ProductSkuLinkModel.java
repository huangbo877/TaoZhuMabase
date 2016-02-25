package com.ruiyu.taozhuma.model;

import com.lidroid.xutils.db.annotation.Id;

/**
 * 商品SKU信息
 * 
 * @author Fu
 * 
 */
public class ProductSkuLinkModel {
	@Id
	private long kid;
	public Integer id;// sku id
	public Integer skuFormatId;// 规格id
	public Integer skuColorId;// 颜色id
	public Integer status;// 可选状态：0-不可选，1-可选
	public String formatValue;// 规格值
	public String colorValue;// 颜色值
	public Integer pid;// 商品id
	public String price;//sku对应价格
	public String discount;//折扣
}
