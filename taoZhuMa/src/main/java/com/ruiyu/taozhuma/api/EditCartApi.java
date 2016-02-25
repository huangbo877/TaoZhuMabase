package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

/**
 * 
 * 修改购物车API
 * @author Fu
 *
 */
public class EditCartApi implements BaseApi {

	private int uid;//用户ID
	private String cids;//购物车Id和数量字符串数组

	
	@Override
	public Map<String, String> getParamMap() {
		// TODO Auto-generated method stub
		Map<String, String> map = new HashMap<String, String>();
		map.put("cids", this.cids+"");
		map.put("uid", this.uid+"");
		return map;		
	}

	@Override
	public String getUrl() {
		return AppConfig.TZM_EDITCART_URL;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getCids() {
		return cids;
	}

	public void setCids(String cids) {
		this.cids = cids;
	}

	
	
}
