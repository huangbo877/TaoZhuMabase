package com.ruiyu.taozhuma.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 秒杀Model
 * 
 * @author lie
 * 
 */

public class PromotionsModel implements Serializable {
	public int id;// 活动id
	public String title;// 活动标题
	public String image1;// 活动图片
	public int end_time;// 活动结束时间
	public ArrayList<Promo> promo;// 产品列表

	public class Promo {
		public int product_id;// 产品id
		public String price_old;// 产品原价
		public String image;// 产品图片
		public String name;// 产品名称
		public int number;// 限购数量
		public String price;// 秒杀价格
	}

}