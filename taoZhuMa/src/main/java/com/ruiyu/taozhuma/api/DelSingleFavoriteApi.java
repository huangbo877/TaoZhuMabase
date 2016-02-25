package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

/**
 * 
 * 删除收藏API
 *
 *
 */
public class DelSingleFavoriteApi implements BaseApi {

	private Integer uid;//会员id
	private	Integer favType;//收藏的类型：1=产品；2=店铺；3=分销产品库
	private	Integer favSenId;//产品或者店铺id
	private	Integer activityId;//专场id
	@Override
	public String getUrl() {
		return AppConfig.DELSINGLEFAVORITE_URL;
	}

	@Override
	public Map<String, String> getParamMap() {
		// TODO Auto-generated method stub
		Map<String, String> map = new HashMap<String, String>();
		map.put("uid", this.uid+"");
		map.put("favType", this.favType+"");
		map.put("favSenId", this.favSenId+"");
		map.put("activityId", this.activityId+"");
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

	public Integer getFavSenId() {
		return favSenId;
	}

	public void setFavSenId(Integer favSenId) {
		this.favSenId = favSenId;
	}
	public Integer getactivityId() {
		return activityId;
	}

	public void setactivityId(Integer activityId) {
		this.activityId = activityId;
	}

}
