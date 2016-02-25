package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

/**
 * 
 * 添加购物车API
 * 
 * @author Fu
 * 
 */
public class TzmAddcartApi implements BaseApi {

	private Integer uid;// 会员id
	private Integer pid;// 商品ID
	private Integer num;// 商品数量
	private Integer skuLinkId;// skuId,没有sku时传空
	private int activityId;// 商品活动id,默认0-普通商品

	@Override
	public Map<String, String> getParamMap() {
		// TODO Auto-generated method stub
		Map<String, String> map = new HashMap<String, String>();
		map.put("uid", this.uid + "");
		map.put("pid", this.pid + "");
		map.put("num", this.num + "");
		map.put("skuLinkId", this.skuLinkId + "");
		map.put("activityId", this.activityId + "");
		return map;
	}

	@Override
	public String getUrl() {
		return AppConfig.TZM_ADDCART_URL;
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

	public Integer getSkuLinkId() {
		return skuLinkId;
	}

	public void setSkuLinkId(Integer skuLinkId) {
		this.skuLinkId = skuLinkId;
	}

	public int getActivityId() {
		return activityId;
	}

	public void setActivityId(int activityId) {
		this.activityId = activityId;
	}

}
