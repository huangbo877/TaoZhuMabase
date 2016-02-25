package com.ruiyu.taozhuma.model;

public class MyInfoModel {
	public Integer uid;// 会员id
	public String userName;// 账户
	public String image;// 头像图片
	public Integer proFavorites;// 商品收藏数量
	public Integer shopFavorites;// 品牌收藏数量
	public Integer myFootprint;// 我的足迹数量
	public Integer status;// 状态 0 未审核，1审核通过
	public String waitPay;// (普通会员)待付款数量
	public String waitReceive;// (批发商)待接受数量
	public String waitDeliver;// 待发货
	public String waitReceipt;// 待收货
	public String waitComment;// 待评价
	public String couponNum;// 可以使用的优惠劵数量
	public String age;// 年龄
	public Integer firstOrder;// 0-未下过单，1-已下过单
	public Integer newUser;// 邀请码资格1：可被邀请；2：老用户；3：已被邀请
	public Float balance;// 钱包余额
}
