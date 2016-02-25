package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

/**
 * 订单可用优惠券列表
 * 
 * @author Fu
 * 
 */
public class ValidUserCouponApi implements BaseApi {
	private Integer uid;
	private float price;

	@Override
	public Map<String, String> getParamMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("uid", uid + "");
		map.put("price", price + "");
		return map;
	}

	@Override
	public String getUrl() {
		return AppConfig.GETVALIDUSERCOUPON_URL;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}


}
