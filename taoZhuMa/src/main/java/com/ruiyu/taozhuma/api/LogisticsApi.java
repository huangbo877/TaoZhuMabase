package com.ruiyu.taozhuma.api;

import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

public class LogisticsApi implements BaseApi{

	@Override
	public Map<String, String> getParamMap() {
		return null;
	}

	@Override
	public String getUrl() {
		return AppConfig.TZM_LOGISTICS_URL;
	}

}
