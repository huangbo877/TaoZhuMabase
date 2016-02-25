package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

/**
 * 
 * 发送手机验证码API
 * 
 * @author
 * 
 */
public class TzmSearchHelpApi implements BaseApi {

	private String name;// 搜索名称（为空搜索全部）

	@Override
	public Map<String, String> getParamMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("name", name + "");
		return map;
	}

	@Override
	public String getUrl() {
		return AppConfig.TZM_SEARCHHELP_URL;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
