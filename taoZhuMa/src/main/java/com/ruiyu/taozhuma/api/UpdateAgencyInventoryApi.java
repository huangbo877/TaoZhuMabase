package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

public class UpdateAgencyInventoryApi implements BaseApi{
	private Integer uid;//用户ID
	private String pidInventorys;//批发商产品Id和库存数量字符串数组
	
	@Override
	public Map<String, String> getParamMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("uid", uid+"");
		map.put("pidInventorys", pidInventorys+"");
		return map;
	}

	@Override
	public String getUrl() {
		return AppConfig.TZM_UPDATEAGENCYINVENTORY_URL;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public String getPidInventorys() {
		return pidInventorys;
	}

	public void setPidInventorys(String pidInventorys) {
		this.pidInventorys = pidInventorys;
	}

}
