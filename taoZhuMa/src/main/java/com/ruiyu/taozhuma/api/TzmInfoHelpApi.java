package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

public class TzmInfoHelpApi implements BaseApi{
	public Integer id;
	@Override
	public Map<String, String> getParamMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", id+"");
		return map;
	}

	@Override
	public String getUrl() {
		return AppConfig.TZM_HELPCENTERDETAIL_URL;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}

