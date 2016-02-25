package com.ruiyu.taozhuma.model;

import java.io.Serializable;
import java.util.ArrayList;

public class TzmSearchAllModel implements Serializable{
	public ArrayList<Product> product;
	public ArrayList<Shop> shop;
	public class Product{
		public int pId; //产品id
		public int activityId; //活动id
		public Integer sellNumber;//产品销量
		public String distributionPrice;//产品分销价格
		public String productImage;//产品图片
		public String productName;//产品名称
	}
	public class Shop{
		public String shopName;//店铺名称
		public String shopImage;//店铺LOGO
		public Integer sId;//店铺id
		public String mainCategory;//主营类型
		public String address ;//公司地址
	}
}
