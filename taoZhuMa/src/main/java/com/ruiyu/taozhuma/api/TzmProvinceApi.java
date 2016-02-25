package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

public class TzmProvinceApi implements BaseApi {
	private Integer pid;

	@Override
	public Map<String, String> getParamMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("pid", this.pid+"");
		return map;		
	}

	@Override
	public String getUrl() {
		return AppConfig.TZM_PROVINCE_URL;
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}
	
}
