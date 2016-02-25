/**
 * 
 */
package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

/**
 * @author 林尧
 * 2015-12-23
 */
public class ScenesFragmentApi implements BaseApi {
	private Integer pageNo;//页数
	@Override
	public String getUrl() {
		return AppConfig.GETSCENELIST_URL;
	}

	@Override
	public Map<String, String> getParamMap() {
		// TODO Auto-generated method stub
		Map<String, String> map = new HashMap<String, String>();
		map.put("pageNo", this.pageNo+"");
		return map;
	}
	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}
}
