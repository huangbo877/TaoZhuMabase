package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

/**
 * 审核API
 * 
 * @author 
 */
public class TzmOrderCheskApi implements BaseApi {
	private Integer uid;
	private Integer oid;// 订单ID

	@Override
	public String getUrl() {
		return AppConfig.REFUNDDETAILS_URL;
	}

	@Override
	public Map<String, String> getParamMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("oid", this.oid + "");
		map.put("uid", this.uid + "");
		return map;
	}

	public Integer getOid() {
		return oid;
	}

	public void setOid(Integer oid) {
		this.oid = oid;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}
}
