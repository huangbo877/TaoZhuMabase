/**
 * 
 */
package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

/**
 * @author 林尧
 * 2015-11-7
 * 用户退货  ,查看货物 物流的 api
 */
public class TzmRefuseProductExpressApi implements BaseApi {

	private int orderId;//订单id
	
	@Override
	public Map<String, String> getParamMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("oid", this.orderId+"");
		return map;		
	}

	@Override
	public String getUrl() {
		return AppConfig.REFUNDEXPRESS_URL;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

}