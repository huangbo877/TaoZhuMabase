package com.ruiyu.taozhuma.model;

import java.util.List;

/**
 * 商品SKU颜色/规格列表返回
 * 
 * @author Fu
 * 
 */
public class ProductSkuListModel {
	public int skuType;// 1-颜色 ；2-规格
	public String skuTitle;// sku名称
	public List<SkuValue> skuValue;//

	public class SkuValue {
		public Integer optionId;// skuid
		public String optionValue;// sku值
		public boolean isclckabel;// 是否可选
		public int status;// 状态 1 选中 0未选中
	}

}
