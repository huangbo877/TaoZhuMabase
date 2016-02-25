package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

/**
 * 
 * 帮助中心—常见问题列表API
 * 
 * @author Fu
 * 
 */
public class TzmHelpCenterApi implements BaseApi {

	@Override
	public Map<String, String> getParamMap() {
		// TODO Auto-generated method stub
		Map<String, String> map = new HashMap<String, String>();
		return map;
	}

	@Override
	public String getUrl() {
		return AppConfig.TZM_HELPCENTER_URL;
	}

}
