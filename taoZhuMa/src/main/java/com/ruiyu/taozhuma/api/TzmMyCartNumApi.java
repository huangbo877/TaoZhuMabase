package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

/**
 * 提交订单
 * 
 * @author Eve
 * 
 */
public class TzmMyCartNumApi implements BaseApi {
	private int uid;// 会员id

	@Override
	public Map<String, String> getParamMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("uid", this.uid + "");
		return map;
	}

	@Override
	public String getUrl() {
		return AppConfig.TZM_MYCARTNUM_URL;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}


}
