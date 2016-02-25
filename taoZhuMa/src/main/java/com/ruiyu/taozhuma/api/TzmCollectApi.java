package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

public class TzmCollectApi implements BaseApi{
	private int pageNo;//页码
	private int favType;//(参数：1=产品；2=店铺；3=分销产品库)
	private int uid;//会员id

	@Override
	public Map<String, String> getParamMap() {
		Map<String,String> map = new HashMap<String, String>();
		map.put("pageNo", this.pageNo+"");
		map.put("favType", this.favType+"");
		map.put("uid", this.uid+"");
		return map;
	}

	@Override
	public String getUrl() {
		return AppConfig.TZM_COLLECT_URL;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getFavType() {
		return favType;
	}

	public void setFavType(int favType) {
		this.favType = favType;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	
	
	
}
