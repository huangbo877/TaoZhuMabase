package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;
/**
 * 添加优惠卷
 * @author Fu
 *
 */
public class AddUserCouponApi implements BaseApi {
	private Integer uid;// 优惠券码
	private String couponNo;// 优惠券码

	@Override
	public Map<String, String> getParamMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("uid", uid + "");
		map.put("couponNo", couponNo);
		return map;
	}

	@Override
	public String getUrl() {
		return AppConfig.ADDUSERCOUPON_URL;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public String getCouponNo() {
		return couponNo;
	}

	public void setCouponNo(String couponNo) {
		this.couponNo = couponNo;
	}

}
