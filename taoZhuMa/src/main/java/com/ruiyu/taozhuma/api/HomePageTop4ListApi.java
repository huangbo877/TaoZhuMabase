package com.ruiyu.taozhuma.api;

import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;
/*
 * 首页分类活动推送
 * 
*/
public class HomePageTop4ListApi implements BaseApi{

	@Override
	public Map<String, String> getParamMap() {
		return null;
	}

	@Override
	public String getUrl() {
		// TODO Auto-generated method stub
		return AppConfig.TZM_HOMEPAGETOPLIST_URL;
	}
	
}
