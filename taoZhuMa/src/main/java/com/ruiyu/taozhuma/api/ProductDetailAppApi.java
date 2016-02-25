package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

/**
 * API
 * 
 * @author yangfeng
 */
public class ProductDetailAppApi implements BaseApi {

	private Integer id;//

	@Override
	public String getUrl() {
		return AppConfig.PRODUCTIMAGE_URL;
	}

	@Override
	public Map<String, String> getParamMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", this.id + "");
		return map;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
