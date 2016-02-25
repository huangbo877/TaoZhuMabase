package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

/**
 * 确认订单API
 * 
 * @author yangfeng
 */
public class TzmEditOrderStatusApi implements BaseApi {

	private Integer uid;// 会员id
	private Integer oid;// 订单ID
	private Integer status;// 订单状态：1待付款,2已付款，待发货,3已发货,4已收货，5已确定

	@Override
	public String getUrl() {
		return AppConfig.TZM_EDITORDERSTATUS_URL;
	}

	@Override
	public Map<String, String> getParamMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("uid", this.uid + "");
		map.put("oid", this.oid + "");
		map.put("status", this.status + "");
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
}
