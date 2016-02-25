package com.ruiyu.taozhuma.model;

import java.io.Serializable;
import java.util.ArrayList;






/**
 * 主题馆/主题馆详情Model
 * @author FU
 * 
 */

public class PromotionsNewDetailModel implements Serializable{
	public Integer id;//主题编号
	public String title;//主题名称
	public String image1;//主题头部背景图
	public String image;//主题头部背景图
	public String end_time;//截止日期
	public ArrayList<HotProductModel> promo;//热门商品/返回获取到产品ID的数组

}
