package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

/**
 * 
 * 商品SKU信息API
 * 
 * @author Fu
 * 
 */
public class ProductSkuLinkApi implements BaseApi {
	private Integer pid;// 产品id
	private int activityId;// 产品id

	@Override
	public String getUrl() {
		return AppConfig.GETPRODUCTSKULINK_URL;
	}

	@Override
	public Map<String, String> getParamMap() {
		// TODO Auto-generated method stub
		Map<String, String> map = new HashMap<String, String>();
		map.put("pid", pid + "");
		map.put("activityId", activityId + "");
		return map;
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public int getActivityId() {
		return activityId;
	}

	public void setActivityId(int activityId) {
		this.activityId = activityId;
	}
	

}
