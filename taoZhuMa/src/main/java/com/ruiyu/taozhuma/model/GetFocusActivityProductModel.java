package com.ruiyu.taozhuma.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 秒杀活动产品列表返回Model
 * 
 * @author FU
 * 
 */
@SuppressWarnings("serial")
public class GetFocusActivityProductModel implements Serializable {
	public String id;
	public String title;
	public String banner;
	public ArrayList<Products> products;

	public class Products {
		public int productId;// 产品id
		public String productName;// 产品名称
		public String image;// 产品图片
		public String price;// 产品价格
		public String sellNumber;

	}

}
