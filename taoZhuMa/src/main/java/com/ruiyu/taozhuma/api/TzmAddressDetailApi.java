package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

public class TzmAddressDetailApi implements BaseApi{
	private Integer uid;
	private Long addId;
	@Override
	public Map<String, String> getParamMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("uid", this.uid+"");
		map.put("addId", this.addId+"");
		return map;	
	}

	@Override
	public String getUrl() {
		return AppConfig.TZM_ADDRESSDETAIL_URL;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public Long getAddId() {
		return addId;
	}

	public void setAddId(Long addId) {
		this.addId = addId;
	}

}
