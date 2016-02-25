package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

/**
 * 
 * 首页焦点图API
 * 
 * @author Fu
 * 
 */
public class TzmFocusApi implements BaseApi {

	private Integer type;//

	@Override
	public String getUrl() {
		return AppConfig.TZM_FOCUS_URL;
	}

	@Override
	public Map<String, String> getParamMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("type", this.type + "");
		return map;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

}
