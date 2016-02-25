package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

/**
 * 订单详情API
 * 
 * @author yangfeng
 */
public class AgentOrderDetailApi implements BaseApi {

	private Integer oid;// 订单ID
	private Integer agencyId;// 批发商id

	@Override
	public String getUrl() {
		return AppConfig.TZM_ORDERDETAILFORAGENCY_URL;
	}

	@Override
	public Map<String, String> getParamMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("oid", this.oid + "");
		map.put("agencyId", this.agencyId + "");
		return map;
	}

	public Integer getOid() {
		return oid;
	}

	public void setOid(Integer oid) {
		this.oid = oid;
	}

	public Integer getAgencyId() {
		return agencyId;
	}

	public void setAgencyId(Integer agencyId) {
		this.agencyId = agencyId;
	}

}
