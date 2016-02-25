package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

/**
 * 
 * 店铺详情API
 * @author 
 *
 */
public class TzmShopDetailApi implements BaseApi {

	private int activityId;//店铺id
	private int pageNo;//产品分页
	private int source;//产品销量(1降序11升序)、金额(2降序22升序)、点击率(3降序33升序)
	
	
	@Override
	public Map<String, String> getParamMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("activityId", this.activityId+"");
		map.put("pageNo", this.pageNo+"");
		map.put("source", this.source+"");
		return map;		
	}

	@Override
	public String getUrl() {
		return AppConfig.GETSPECIALPRODUCTLISTAPI_URL;
	}

	public int getactivityId() {
		return activityId;
	}

	public void setactivityId(int activityId) {
		this.activityId = activityId;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getSource() {
		return source;
	}

	public void setSource(int source) {
		this.source = source;
	}

	

	

	
}
