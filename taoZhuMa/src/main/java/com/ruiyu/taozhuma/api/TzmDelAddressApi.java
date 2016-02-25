package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

/**
 * 
 * 删除地址API
 * 
 * @author Fu
 * 
 */
public class TzmDelAddressApi implements BaseApi {

	private Integer uid;// 会员id
	private Long addId;// 地址ID

	@Override
	public String getUrl() {
		return AppConfig.TZM_DELADDRESS_URL;
	}

	@Override
	public Map<String, String> getParamMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("uid", this.uid + "");
		map.put("addId", this.addId + "");
		return map;
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
