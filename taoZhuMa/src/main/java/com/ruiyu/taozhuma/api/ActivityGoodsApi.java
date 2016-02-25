package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

/**
 * 
 * 秒杀活动产品列表API
 * 
 * @author Fu
 * 
 */
public class ActivityGoodsApi implements BaseApi {
	private Integer timeId;

	@Override
	public String getUrl() {
		return AppConfig.ACTIVITYGOODS_URL;
	}

	@Override
	public Map<String, String> getParamMap() {
		// TODO Auto-generated method stub
		Map<String, String> map = new HashMap<String, String>();
		map.put("timeId", timeId + "");
		return map;
	}

	public Integer getTimeId() {
		return timeId;
	}

	public void setTimeId(Integer timeId) {
		this.timeId = timeId;
	}

}
