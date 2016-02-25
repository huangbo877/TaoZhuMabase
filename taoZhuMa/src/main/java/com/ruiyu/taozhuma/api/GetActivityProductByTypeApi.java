package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

public class GetActivityProductByTypeApi implements BaseApi{
	private int pageNo;//页码
	private Integer type;//活动类型，1-每日10件，2-限时秒杀，3-大牌驾到
	@Override
	public Map<String, String> getParamMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("pageNo", this.pageNo+"");
		map.put("type", type+"");
		return map;
	}

	@Override
	public String getUrl() {
		return AppConfig.TZM_GETACTIVITYPRODUCTBYTYPE_URL;
	}
	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

}
