package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

public class TzmAgentOrderApi implements BaseApi{
	private Integer uid;//会员id
	private Integer orderStatus;//订单状态：1待付款,2已付款，待发货,3已发货,4已收货，5已评价
	private Integer pageNo;//页码
	
	@Override
	public Map<String, String> getParamMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("uid", uid+"");
		map.put("orderStatus", orderStatus+"");
		map.put("pageNo", pageNo+"");
		return map;
	}

	@Override
	public String getUrl() {
		return AppConfig.TZM_AGENTORDER_URL;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public Integer getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

}
