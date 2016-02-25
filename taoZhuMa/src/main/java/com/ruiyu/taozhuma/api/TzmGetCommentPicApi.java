package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

/**
 * 
 * 添加收藏API
 * 
 * @author Fu
 * 
 */
public class TzmGetCommentPicApi implements BaseApi {

	private Integer activityId;// 店铺ID

	@Override
	public String getUrl() {
		return AppConfig.GETSPECIALPRODUCTLISTAPI_URL;
	}

	@Override
	public Map<String, String> getParamMap() {
		// TODO Auto-generated method stub
		Map<String, String> map = new HashMap<String, String>();
		map.put("activityId", this.activityId + "");
		return map;
	}

	public Integer getactivityId() {
		return activityId;
	}

	public void setactivityId(Integer activityId) {
		this.activityId = activityId;
	}

}
