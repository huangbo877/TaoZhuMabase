package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

public class TzmProductListsApi implements BaseApi{
	private Integer pageNo;//页码
	private Integer uid;//会员ID
	@Override
	public Map<String, String> getParamMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("pageNo", pageNo+"");
		map.put("uid", uid+"");
		return map;
	}

	@Override
	public String getUrl() {
		return AppConfig.TZM_PRODUCTLISTS_URL;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

}
