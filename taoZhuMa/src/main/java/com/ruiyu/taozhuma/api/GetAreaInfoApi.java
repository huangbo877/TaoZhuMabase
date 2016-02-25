package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

/**
 * 
 * @author Fu
 *
 */
public class GetAreaInfoApi implements BaseApi {

	@Override
	public String getUrl() {
		return AppConfig.GETAREAINFO_URL;
	}

	@Override
	public Map<String, String> getParamMap() {
		// TODO Auto-generated method stub
		Map<String, String> map = new HashMap<String, String>();
		return map;
	}

}
