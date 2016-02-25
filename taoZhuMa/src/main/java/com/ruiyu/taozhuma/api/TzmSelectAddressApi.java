package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

/**
 * 设置默认地址 api
 * 
 * @author 
 * 
 */
public class TzmSelectAddressApi implements BaseApi {
	private Integer uid;//会员id
	private long addId;//选中的地址id
	@Override
	public Map<String, String> getParamMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("uid", this.uid + "");
		map.put("addId", this.addId + "");
		return map;
	}

	@Override
	public String getUrl() {
		return AppConfig.TZM_SELECTADDRESS_URL;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	
	public long getAddId() {
		return addId;
	}

	public void setAddId(long addId) {
		this.addId = addId;
	}

}
