package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

/**
 * 
 * 秒杀活动时间列表返回API
 * 
 * @author Fu
 * 
 */
public class ActivityTimeListApi implements BaseApi {

	@Override
	public String getUrl() {
		return AppConfig.ACTIVITYTIMELIST_URL;
	}

	@Override
	public Map<String, String> getParamMap() {
		// TODO Auto-generated method stub
		Map<String, String> map = new HashMap<String, String>();
		return map;
	}

}
