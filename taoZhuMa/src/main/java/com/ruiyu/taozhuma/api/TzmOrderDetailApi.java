package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

/**
 * 订单详情API
 * 
 * @author yangfeng
 */
public class TzmOrderDetailApi implements BaseApi {

	private Integer oid;// 订单ID

	@Override
	public String getUrl() {
		return AppConfig.TZM_ORDERDETAIL_URL;
	}

	@Override
	public Map<String, String> getParamMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("oid", this.oid + "");
		return map;
	}

	public Integer getOid() {
		return oid;
	}

	public void setOid(Integer oid) {
		this.oid = oid;
	}

}
