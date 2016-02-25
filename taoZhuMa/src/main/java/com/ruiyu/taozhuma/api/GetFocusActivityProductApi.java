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
public class GetFocusActivityProductApi implements BaseApi {

	private int id;//活动id
	private int pageNo;//产品分页
	private int source;//产品销量(1降序11升序)、金额(2降序22升序)、点击率(3降序33升序)
	
	
	@Override
	public Map<String, String> getParamMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", this.id+"");
		map.put("pageNo", this.pageNo+"");
		map.put("sorting", this.source+"");
		return map;		
	}

	public String getUrl() {
		return AppConfig.TZM_FOCUS_ACT_URL;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
