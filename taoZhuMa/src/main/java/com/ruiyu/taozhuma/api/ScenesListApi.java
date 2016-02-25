/**
 * 
 */
package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

/**
 * 首页场景套餐列表
 * 
 * @author 林尧 2015-10-27
 */
public class ScenesListApi implements BaseApi {
	private Integer pageNo;//会员id
	@Override
	public String getUrl() {
		return AppConfig.GETHOMEPGSPECIALSHOWLIST_URL;
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
