package com.ruiyu.taozhuma.model;

import java.io.Serializable;
import java.util.ArrayList;




/**
 * 抢购活动Model
 * @author FU
 * 
 */

public class PromotionsSnapModel implements Serializable{
	public int id;//主题编号
	public String title;//主题名称
	public String image1;//主题头部背景图
	public String end_time;//截止日期
	public ArrayList<Promos> promo;//返回获取到产品ID的数组
	public ArrayList<Promos> promos;//返回获取到产品ID的数组
	public class Promos {
		public int product_id;//产品ID
		public Integer id;
		public String priceOld;
		public String price;
		public Integer number;//活动数量
		public Integer pro_price;//特价
		public String image;//产品图片
		public String name;//产品名称
		
	}

}
