package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

/**
 * 
 * 套餐直接购买
 * @author 
 *
 */
public class AddPkgPurchaseApi implements BaseApi {

	private Integer uid;//会员id
	private Integer pkgId;//套餐ID
	
	@Override
	public Map<String, String> getParamMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("uid", this.uid+"");
		map.put("pkgId", this.pkgId+"");
		return map;		
	}

	@Override
	public String getUrl() {
		return AppConfig.ADDPKGPURCHASE_URL;
	}


	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public Integer getPkgId() {
		return pkgId;
	}

	public void setPkgId(Integer pkgId) {
		this.pkgId = pkgId;
	}



}
