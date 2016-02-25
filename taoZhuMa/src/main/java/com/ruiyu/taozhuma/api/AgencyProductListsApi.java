package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

public class AgencyProductListsApi implements BaseApi{
	private int pageNo;//页码
	private int uid;//批发商用户ID

	@Override
	public Map<String, String> getParamMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("pageNo", this.pageNo+"");
		map.put("uid", this.uid+"");
		return map;
	}

	@Override
	public String getUrl() {
		return AppConfig.TZM_AGENCYPRODUCTLISTS_URL;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

}
