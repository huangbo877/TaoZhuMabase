package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

/**
 * 审核API
 * 
 * @author 
 */
public class TzmOrderCheskFavoriteApi implements BaseApi {
	private Integer uid;
	private Integer id;// 商品ID

	@Override
	public String getUrl() {
		return AppConfig.GUESSFAVORITE_URL;
	}

	@Override
	public Map<String, String> getParamMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", this.id + "");
		map.put("uid", this.uid + "");
		return map;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}
}
