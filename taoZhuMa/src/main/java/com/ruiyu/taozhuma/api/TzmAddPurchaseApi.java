package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

/**
 * 
 * 直接购买API
 * 
 * @author Fu
 * 
 */
public class TzmAddPurchaseApi implements BaseApi {

	private Integer uid;// 会员id
	private Integer pid;// 商品ID
	private Integer num;// 商品数量
	
	@Override
	public String getUrl() {
		return AppConfig.TZM_ADDPURCHASE_URL;
	}

	@Override
	public Map<String, String> getParamMap() {
		// TODO Auto-generated method stub
		Map<String, String> map = new HashMap<String, String>();
		map.put("uid", this.uid + "");
		map.put("pid", this.pid + "");
		map.put("num", this.num + "");
		return map;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

}
