package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

public class ToSetAgencyProductApi implements BaseApi{
	private Integer agencyId;//批发商ID
	private String pids;//批发商产品对应id字符串数组
	@Override
	public Map<String, String> getParamMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("agencyId", agencyId+"");
		map.put("pids", pids+"");
		return map;
	}

	@Override
	public String getUrl() {
		return AppConfig.TZM_TOSETAGENCYPRODUCT_URL;
	}

	public Integer getAgencyId() {
		return agencyId;
	}

	public void setAgencyId(Integer agencyId) {
		this.agencyId = agencyId;
	}

	public String getPids() {
		return pids;
	}

	public void setPids(String pids) {
		this.pids = pids;
	}

}
