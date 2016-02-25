package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

/**
 * 
 * 行业资讯API
 * @author 
 *
 */
public class TzmInfoListApi implements BaseApi {

	private int pageNo;//页码
	//private int type;//默认为1
	
	@Override
	public Map<String, String> getParamMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("pageNo", this.pageNo+"");
		//map.put("type", this.type+"");
		return map;		
	}

	@Override
	public String getUrl() {
		return AppConfig.TZM_INFO_LIST_URL;
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
