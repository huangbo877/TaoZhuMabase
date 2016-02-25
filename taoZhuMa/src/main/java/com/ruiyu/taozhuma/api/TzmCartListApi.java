package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import android.os.IInterface;

import com.ruiyu.taozhuma.config.AppConfig;

/**
 * 
 * 查看购物车API
 * @author 
 *
 */
public class TzmCartListApi implements BaseApi {

	private Integer  uid;//会员id
	
	@Override
	public Map<String, String> getParamMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("uid", this.uid+"");
		return map;		
	}

	@Override
	public String getUrl() {
		return AppConfig.TZM_CART_LIST_URL;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	

}
