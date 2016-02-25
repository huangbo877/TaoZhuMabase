package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

/**
 * 
 * 产品详情API
 * @author 
 *
 */
public class TzmProductDetailApi implements BaseApi {

	private Integer id;//产品id
	private Integer uid;// 用户户id
	private int activityId;// 活动id
	
	@Override
	public Map<String, String> getParamMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", this.id+"");
		map.put("uid", this.uid+"");
		map.put("activityId", this.activityId+"");
		return map;		
	}

	@Override
	public String getUrl() {
		return AppConfig.TZM_PRODUCT_DETAIL_URL;
	}

	public int getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public int getActivityId() {
		return activityId;
	}

	public void setActivityId(int activityId) {
		this.activityId = activityId;
	}

	
}
