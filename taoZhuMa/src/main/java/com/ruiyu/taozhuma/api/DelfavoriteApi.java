package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

/**
 * 
 * 删除收藏API
 * @author Fu
 *
 */
public class DelfavoriteApi implements BaseApi {

	private Integer uid;//会员id
	private	Integer favType;//收藏的类型：1=产品；2=店铺；3=分销产品库
	private	String cids;//收藏的id（例如产品id/c店铺ID/ B店铺ID）
	@Override
	public String getUrl() {
		return AppConfig.DELFAVORITE_URL;
	}

	@Override
	public Map<String, String> getParamMap() {
		// TODO Auto-generated method stub
		Map<String, String> map = new HashMap<String, String>();
		map.put("uid", this.uid+"");
		map.put("favType", this.favType+"");
		map.put("cids", this.cids+"");
		return map;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public Integer getFavType() {
		return favType;
	}

	public void setFavType(Integer favType) {
		this.favType = favType;
	}

	public String getCids() {
		return cids;
	}

	public void setCids(String cids) {
		this.cids = cids;
	}

	


}
