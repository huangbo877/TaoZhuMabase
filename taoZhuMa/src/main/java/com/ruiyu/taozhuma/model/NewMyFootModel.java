package com.ruiyu.taozhuma.model;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class NewMyFootModel implements Serializable{
	public String viewTime;//时间
	public Integer num;//数量
	public ArrayList<Products> products;//访问商品
	public class Products {
		public String sellingPrice;//销售价格
		public Integer sellNumber;//月销量
		public String image;//图片
		public String productId;//产品id
		public String productName;//产品名称
		public String distributionPrice;
		public Integer proStatus;////0未开始 1进行中 ,2 已经结束
	    public Integer	activityId;//专场活动ID
		
	}
}
