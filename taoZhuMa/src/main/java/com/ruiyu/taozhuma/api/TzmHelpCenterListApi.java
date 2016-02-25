package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

/**
 * 
 * 发送手机验证码API
 * 
 * @author
 * 
 */
public class TzmHelpCenterListApi implements BaseApi {

	private Integer typeId;

	@Override
	public Map<String, String> getParamMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("typeId", typeId + "");
		return map;
	}

	@Override
	public String getUrl() {
		return AppConfig.TZM_HELPCENTERLIST_URL;
	}

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

}
