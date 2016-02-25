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
public class TzmAppActivityListApi implements BaseApi {

	private Integer id;// 类型（1为遥控/电动玩具；2为早教/音乐玩具	/ ；3为过家家玩具；4为童车玩具；5为益智玩具；6为其他玩具）
	private Integer type;//级别
	private Integer pageNo;// 页码

	@Override
	public String getUrl() {
		return AppConfig.GETSPECIALSHOWLISTAPI_URL;
	}

	@Override
	public Map<String, String> getParamMap() {
		// TODO Auto-generated method stub
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", this.id + "");
		map.put("type", this.type + "");
		map.put("pageNo", this.pageNo + "");
		return map;
	}

	public Integer getid() {
		return id;
	}

	public void setid(Integer id) {
		this.id = id;
	}
	
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

}
