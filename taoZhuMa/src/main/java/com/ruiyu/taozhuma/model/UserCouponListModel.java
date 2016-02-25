package com.ruiyu.taozhuma.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 优惠卷
 * 
 * @author Fu
 * 
 * 
 */
@SuppressWarnings("serial")
public class UserCouponListModel implements Serializable {
	public String couponNo;// 优惠券码
	public String couponName;// 优惠券名
	public int overdue;// 过期状态，0-否，1-是
	public String om;// 订单金额满om
	public String m;// 减掉金额m
	public String validStime;// 有效开始时间
	public int used;// 使用状态，0-否，1-是
	public int couponId;// 优惠券类型id
	public String useTime;// 使用时间
	public String validEtime;// 有效结束时间
	public int source;// 来源：1-新人礼包、2-首单礼包、3-节日活动、4-互动活动、5-用户绑定

}
