package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

/**
 * 
 * 添加购物车API
 * 
 * @author Fu
 * 
 */
public class TzmsetAgencyPushApi implements BaseApi {

	private Integer orderId;// 订单id
	private Integer agencyId;// 批发商ID

	@Override
	public Map<String, String> getParamMap() {
		// TODO Auto-generated method stub
		Map<String, String> map = new HashMap<String, String>();
		map.put("orderId", this.orderId + "");
		map.put("agencyId", this.agencyId + "");
		return map;
	}

	@Override
	public String getUrl() {
		return AppConfig.TZM_SETAGENCYPUSH_URL;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getAgencyId() {
		return agencyId;
	}

	public void setAgencyId(Integer agencyId) {
		this.agencyId = agencyId;
	}

}
