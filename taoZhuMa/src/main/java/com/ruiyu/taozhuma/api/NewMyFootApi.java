package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

public class NewMyFootApi implements BaseApi{
	private Integer uid;
	@Override
	public Map<String, String> getParamMap() {
		Map<String, String> map= new HashMap<String, String>();
		map.put("uid", uid+"");
		return map;
	}

	@Override
	public String getUrl() {
		return AppConfig.TZM_FOOTNEW_URL;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}
	
}