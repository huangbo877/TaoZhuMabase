package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

public class TzmDeliveryApi implements BaseApi{
	private Integer uid;//批发商id
	private Integer oid;//订单id
	private String logisticsNum;//物流单号
	private String logisticsName;//物流名称
	
	@Override
	public Map<String, String> getParamMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("uid", uid+"");
		map.put("oid", oid+"");
		map.put("logisticsNum", logisticsNum+"");
		map.put("logisticsName", logisticsName+"");
		return map;
	}

	@Override
	public String getUrl() {
		return AppConfig.TZM_DELIVERY_URL;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public Integer getOid() {
		return oid;
	}

	public void setOid(Integer oid) {
		this.oid = oid;
	}

	public String getLogisticsNum() {
		return logisticsNum;
	}

	public void setLogisticsNum(String logisticsNum) {
		this.logisticsNum = logisticsNum;
	}

	public String getLogisticsName() {
		return logisticsName;
	}

	public void setLogisticsName(String logisticsName) {
		this.logisticsName = logisticsName;
	}

}
