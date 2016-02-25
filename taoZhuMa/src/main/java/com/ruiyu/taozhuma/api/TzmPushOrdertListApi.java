package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

/**
 * 推送订单列表API
 * 
 * @author fu
 */
public class TzmPushOrdertListApi implements BaseApi {

	private Integer uid;// 会员id
	private Integer pageNo;// 页码

	@Override
	public String getUrl() {
		return AppConfig.TZM_AGENCYPUSH_URL;
	}

	@Override
	public Map<String, String> getParamMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("uid", this.uid + "");
		map.put("pageNo", this.pageNo + "");
		return map;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}
}
