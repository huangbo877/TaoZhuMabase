package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

/**
 * 
 * 删除购物车产品API
 * 
 * @author Fu
 * 
 */
public class TzmDelCartApi implements BaseApi {

	private Integer uid;// 会员id
	private Integer cids;// 删除购物车id

	@Override
	public String getUrl() {
		return AppConfig.TZM_DELCART_URL;
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

	public Integer getCids() {
		return cids;
	}

	public void setCids(Integer cids) {
		this.cids = cids;
	}

}
