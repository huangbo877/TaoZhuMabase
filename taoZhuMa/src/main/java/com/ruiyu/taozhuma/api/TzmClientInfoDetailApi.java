package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

/**
 * 
 * 消息详情API
 * 
 * @author yangfeng
 * 
 */
public class TzmClientInfoDetailApi implements BaseApi {

	private int id;// 消息ID

	@Override
	public Map<String, String> getParamMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", this.id + "");
		return map;
	}

	@Override
	public String getUrl() {
		return AppConfig.TZM_CLIENTINFODETAIL_URL;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
