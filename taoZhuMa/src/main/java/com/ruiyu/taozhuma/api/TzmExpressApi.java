package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

/**
 * 
 * 查看物流API
 * @author 
 *
 */
public class TzmExpressApi implements BaseApi {

	private int orderId;//订单id
	
	@Override
	public Map<String, String> getParamMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("orderId", this.orderId+"");
		return map;		
	}

	@Override
	public String getUrl() {
		return AppConfig.TZM_EXPRESS_URL;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

}
