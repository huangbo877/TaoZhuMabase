package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

/**
 * 
 * 行业资讯详情API
 * @author 
 *
 */
public class TzmInfoListDetailApi implements BaseApi {

	private int id;//资讯id
	
	@Override
	public Map<String, String> getParamMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", this.id+"");
		return map;		
	}

	@Override
	public String getUrl() {
		return AppConfig.TZM_INFO_DETAIL_URL;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
