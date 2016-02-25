/**
 * 
 */
package com.ruiyu.taozhuma.test;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.api.BaseApi;
import com.ruiyu.taozhuma.config.AppConfig;

/**
 * @author 林尧 2015-10-30
 */
public class ProductImageApi implements BaseApi {
	private Integer id;

	@Override
	public Map<String, String> getParamMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", id + "");

		return map;
	}

	@Override
	public String getUrl() {
		return AppConfig.PRODUCTIMAGE_URL;
	}

	public Integer getUid() {
		return id;
	}

	public void setUid(Integer uid) {
		this.id = uid;
	}

	
}