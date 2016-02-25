package com.ruiyu.taozhuma.model;

import java.io.Serializable;

public class TzmCommentListsModel implements Serializable{
	public Integer id;//评论id
	public String orderId;//订单id
	public Integer productId;//产品id
	public Integer detailId;//订单详情ID
	public String orderNo;//订单号
	public String productImage;//产品图片
	public String productName;//产品名称
	public String comment;//评价内容
	public String create_date;//评价时间
	public String name;//用户昵称

}
