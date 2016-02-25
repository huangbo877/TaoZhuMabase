package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

public class RefSellerInfoByOrderIdApi implements BaseApi{
	private long oid;//订单id
	@Override
	public Map<String, String> getParamMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("oid", oid+"");
		return map;
	}

	@Override
	public String getUrl() {
		return AppConfig.GETREFSELLERINFOBYORDERID_URL;
	}

	public long getUid() {
		return oid;
	}

	public void setOid(long oid) {
		this.oid = oid;
	}
	

}
