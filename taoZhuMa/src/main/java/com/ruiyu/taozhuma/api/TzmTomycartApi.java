package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

/**
 * 
 * b2c购物车API
 * @author 
 *
 */
public class TzmTomycartApi implements BaseApi {

	private int uid;//会员id
	
	@Override
	public Map<String, String> getParamMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("uid", this.uid+"");
		return map;		
	}

	@Override
	public String getUrl() {
		return AppConfig.TZM_TOMYCART;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}
	

}
