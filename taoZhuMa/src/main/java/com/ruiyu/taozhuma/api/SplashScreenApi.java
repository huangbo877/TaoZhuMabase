package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;
/**
 * 
 * @author 林尧
 * 2015-12-15
 * 访问活动闪屏接口参数
 */
public class SplashScreenApi implements BaseApi {


	@Override
	public Map<String, String> getParamMap() {
	
		Map<String, String> map = new HashMap<String, String>();
		
		return map;		
	}

	@Override
	public String getUrl() {
		return AppConfig.ACTIVITYSPLASHSCREEN;
	}

	
	
}
