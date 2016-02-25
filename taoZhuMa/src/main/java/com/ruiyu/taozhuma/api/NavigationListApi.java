/**
 * 
 */
package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

/**
 * 首页场景套餐列表
 * 
 * @author 林尧 2015-10-27
 */
public class NavigationListApi implements BaseApi {
	
	@Override
	public String getUrl() {
		return AppConfig.GETNAVIGATIONLISTAPI_URL;
	}

	@Override
	public Map<String, String> getParamMap() {
		// TODO Auto-generated method stub
		Map<String, String> map = new HashMap<String, String>();
		
		return map;
	}
	
}
