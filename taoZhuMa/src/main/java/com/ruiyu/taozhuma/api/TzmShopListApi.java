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
public class TzmShopListApi implements BaseApi {

	private int pageNo;//页码
	private int type;//（0为所以；1为遥控/电动玩具；2为早教/音乐玩具 ；3为过家家玩具；4为童车玩具；5为益智玩具；6为其他玩具）默认为0
	
	@Override
	public Map<String, String> getParamMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("pageNo", this.pageNo+"");
		map.put("type", this.type+"");
		return map;		
	}

	@Override
	public String getUrl() {
		return AppConfig.TZM_SHOP_LIST_URL;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
