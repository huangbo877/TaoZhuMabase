package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

public class TzmCommentApi implements BaseApi{
	private Integer uid;//会员id
	private Integer oid;//订单id
	private String orderIds;//订单详情id,评论内容
	private Integer scoreService;//服务态度、
	private Integer scoreSspeed;//发货速度
	@Override
	public Map<String, String> getParamMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("uid", this.uid + "");
		map.put("oid", this.oid + "");
		map.put("orderIds", this.orderIds + "");
		map.put("scoreService", this.scoreService + "");
		map.put("scoreSspeed", this.scoreSspeed + "");
		return map;
	}

	@Override
	public String getUrl() {
		return AppConfig.TZM_COMMENT_URL;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public Integer getOid() {
		return oid;
	}

	public void setOid(Integer oid) {
		this.oid = oid;
	}

	public String getOrderIds() {
		return orderIds;
	}

	public void setOrderIds(String orderIds) {
		this.orderIds = orderIds;
	}
	public float getscoreService() {
		return scoreService;
	}

	public void setscoreService(Integer scoreService) {
		this.scoreService = scoreService;
	}
	public float getscoreSspeed() {
		return scoreSspeed;
	}

	public void setscoreSspeed(Integer currentRating2) {
		this.scoreSspeed = currentRating2;
	}

}
