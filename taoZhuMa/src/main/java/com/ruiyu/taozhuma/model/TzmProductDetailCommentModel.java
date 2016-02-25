package com.ruiyu.taozhuma.model;

/**
 * 产品评价详情Model
 * 
 * @author Boo
 * 
 */

public class TzmProductDetailCommentModel {
	public int id;// 评价ID
	public String time;// 评价时间
	public String userName;// 评价人
	public String comment;// 评价内容
	public float productScore;// 评分
	public String skuValue;// sku

	public int score;// 评价状态1差2中3好

}
