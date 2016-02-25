package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

/**
 * 
 * 订单付款API
 * @author Fu
 *
 */
public class PayOrderApi implements BaseApi {

	private Integer uid;//会员id
	private	String orderNo;//订单号
	private	Integer payMethod;//支付方式：	1:支付宝 ；2：微信支付

	@Override
	public String getUrl() {
		return AppConfig.PAYORDER_URL;
	}

	@Override
	public Map<String, String> getParamMap() {
		// TODO Auto-generated method stub
		Map<String, String> map = new HashMap<String, String>();
		map.put("uid", this.uid+"");
		map.put("payMethod", this.payMethod+"");
		map.put("orderNo", this.orderNo);
		return map;
	}

	public Integer getUid() {
		return uid;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Integer getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(Integer payMethod) {
		this.payMethod = payMethod;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	

}
