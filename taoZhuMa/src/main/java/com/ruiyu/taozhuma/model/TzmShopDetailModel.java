package com.ruiyu.taozhuma.model;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * 专场详情Model
 * @author LinJianhong
 *
 */
public class TzmShopDetailModel implements Serializable {
	
//		public int productId;//产品id
//		public String name;//产品名称
//		public String image;//产品图片
//		public String price;//产品价格
//		public String sellNumber;
	public String banner;//图片链接
	public String introduction;//描述
	public String discountType;//1满减2满折
	public String discountText;//折扣描述
	public String endTime2;//时间结束
	public String currentTime;//当前时间
	public ArrayList<Product> productList;//多个商品
	 public class Product{
		   public Integer productId;//商品ID
		   public String productName; //商品名称
		   public String activePrice; // 商品价格
		   public String image;  // 商品图片
		   public Integer activityId;  // 活动id
		   public String sellPrice;  // 产品原价
		

	   }
}
