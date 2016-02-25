package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

public class TzmEvaluationApi implements BaseApi{
	private Integer uid;
	private Integer oid;
	@Override
	public Map<String, String> getParamMap() {
		Map<String, String> map =new HashMap<String, String>();
		map.put("uid", this.uid + "");
		map.put("oid", this.oid + "");
		return map;
	}

	@Override
	public String getUrl() {
		return AppConfig.TZM_EVALUATION_URL;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public Integer getOid() {
		return oid;
	}

	public void setOid(Integer oid) {
		this.oid = oid;
	}

}
