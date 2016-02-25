package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

/**
 * 我的订单API
 * 
 * @author yangfeng
 */
public class TzmMyordertApi implements BaseApi {

	private Integer uid;// 会员id
	private Integer pageNo;// 页码
	private Integer orderStatus;// 订单状态：1待付款,2已付款，待发货,3已发货,4已收货，5已评价

	@Override
	public String getUrl() {
		return AppConfig.TZM_MYORDER_URL;
	}

	@Override
	public Map<String, String> getParamMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("uid", this.uid + "");
		map.put("pageNo", this.pageNo + "");
		map.put("orderStatus", this.orderStatus + "");
		return map;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public Integer getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}

}
