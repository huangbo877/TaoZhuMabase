package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;
/**
 * 
 * 产品描述图API
 * @author Fu
 *
 */
public class TzmProductDetailImgApi implements BaseApi {
	
	private Integer id;//产品id
	
	@Override
	public Map<String, String> getParamMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", id+"");
		return map;		
	}

	@Override
	public String getUrl() {
		return AppConfig.TZM_PRODETILIMG_URL;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	
	
}
