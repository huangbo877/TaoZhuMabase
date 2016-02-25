package com.ruiyu.taozhuma.api;

import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

public class HomePageFloorListApi implements BaseApi{

	@Override
	public Map<String, String> getParamMap() {
		return null;
	}

	@Override
	public String getUrl() {
		// TODO Auto-generated method stub
		return AppConfig.TZM_HOMEPAGEFLOORLIST_URL;
	}

}
