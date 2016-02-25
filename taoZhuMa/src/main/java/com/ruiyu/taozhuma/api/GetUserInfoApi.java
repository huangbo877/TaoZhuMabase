package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

public class GetUserInfoApi implements BaseApi{
	private long uid;//会员id
	@Override
	public Map<String, String> getParamMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("uid", uid+"");
		return map;
	}

	@Override
	public String getUrl() {
		return AppConfig.TZM_GETUSERINFO_URL;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}
	

}
