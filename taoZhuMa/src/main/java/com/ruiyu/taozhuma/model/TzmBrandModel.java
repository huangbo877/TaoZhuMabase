package com.ruiyu.taozhuma.model;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * 专场详情Model
 * @author LinJianhong
 *
 */
public class TzmBrandModel implements Serializable {
	
//		public int productId;//产品id
//		public String name;//产品名称
//		public String image;//产品图片
//		public String price;//产品价格
//		public String sellNumber;
	public String banner;//图片链接
	
	public ArrayList<brandlist> brandlist;//多个活动
	 public class brandlist{
		   
		   public String activityName; // 活动名
		   public String image;  // 活动图片
		   public Integer activityId;  // 活动id
		   
		

	   }
}
