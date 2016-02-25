package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

/**
 * 优惠卷
 * 
 * @author Fu
 * 
 */
public class UserCouponListApi implements BaseApi {
	private Integer uid;
	private Integer pageNo;

	@Override
	public Map<String, String> getParamMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("uid", uid + "");
		map.put("pageNo", pageNo + "");
		return map;
	}

	@Override
	public String getUrl() {
		return AppConfig.GETUSERCOUPONLIST_URL;
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
