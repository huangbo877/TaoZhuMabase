package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

/**
 * 
 * 新品快订API
 * @author 
 *
 */
public class TzmWalletActivityApi implements BaseApi {

	private int pageNo;//页码
	
	@Override
	public Map<String, String> getParamMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("pageNo", this.pageNo+"");
		return map;		
	}

	@Override
	public String getUrl() {
		return AppConfig.WALLETACTIVITY_URL;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}


}
