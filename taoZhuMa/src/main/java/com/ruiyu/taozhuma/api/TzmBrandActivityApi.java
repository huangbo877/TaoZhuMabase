package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

/**
 * 
 * 大牌驾到API
 * @author 
 *
 */
public class TzmBrandActivityApi implements BaseApi {

	private int pageNo;//页码
	//private int type;//(每日新品:1,热销产品:2,竹马推荐:3,新品快定:4,家有好货:5)
	
	@Override
	public Map<String, String> getParamMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("pageNo", this.pageNo+"");
		//map.put("type", this.type+"");
		return map;		
	}

	@Override
	public String getUrl() {
		return AppConfig.BRANDACTIVITYLIST_URL;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

//	public int getType() {
//		return type;
//	}
//
//	public void setType(int type) {
//		this.type = type;
//	}

}
