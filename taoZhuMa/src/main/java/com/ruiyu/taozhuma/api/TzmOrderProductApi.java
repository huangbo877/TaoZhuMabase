package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

/**
 * 
 * 选中提交购物车API
 * 
 * @author Fu
 * 
 */
public class TzmOrderProductApi implements BaseApi {

	private Integer uid;// 会员id
	private String cids;// 提交的购物车ID

	@Override
	public String getUrl() {
		return AppConfig.TZM_ORDERPRODUCT_URL;
	}

	@Override
	public Map<String, String> getParamMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("uid", this.uid + "");
		map.put("cids", this.cids + "");
		return map;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public String getCids() {
		return cids;
	}

	public void setCids(String cids) {
		this.cids = cids;
	}


}
