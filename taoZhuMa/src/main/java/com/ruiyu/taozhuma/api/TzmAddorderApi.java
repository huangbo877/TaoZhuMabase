package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

/**
 * 提交订单
 * 
 * @author Eve
 * 
 */
public class TzmAddorderApi implements BaseApi {
	private int uid;// 会员id
	private Long addressId;// 我的地址ID
	private String cids;// 确认的购买的商品的购物车ID
	// private String buyer_message;// 买家留言
	private int consigneeType;// 收货方式（1，快递2，自提）
	private int payMethod;// 支付方式： 1:支付宝；2：微信支付（默认1）
	private String messages;
	private String couponNo;// 优惠券码
	private int wallet;// 0-不使用钱包1-使用钱包

	@Override
	public Map<String, String> getParamMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("uid", this.uid + "");
		map.put("addressId", this.addressId + "");
		map.put("cids", this.cids + "");
		// map.put("buyer_message", this.buyer_message + "");
		map.put("consigneeType", this.consigneeType + "");
		map.put("payMethod", this.payMethod + "");
		map.put("messages", this.messages);
		map.put("couponNo", this.couponNo);
		map.put("wallet", this.wallet + "");
		return map;
	}

	@Override
	public String getUrl() {
		return AppConfig.TZM_ADDORDER_URL;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public Long getAddressId() {
		return addressId;
	}

	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}

	public String getCids() {
		return cids;
	}

	public void setCids(String cids) {
		this.cids = cids;
	}

	// public String getBuyer_message() {
	// return buyer_message;
	// }
	//
	// public void setBuyer_message(String buyer_message) {
	// this.buyer_message = buyer_message;
	// }

	public int getConsigneeType() {
		return consigneeType;
	}

	public void setConsigneeType(int consigneeType) {
		this.consigneeType = consigneeType;
	}

	public int getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(int payMethod) {
		this.payMethod = payMethod;
	}

	public String getMessages() {
		return messages;
	}

	public void setMessages(String messages) {
		this.messages = messages;
	}

	public String getCouponNo() {
		return couponNo;
	}

	public void setCouponNo(String couponNo) {
		this.couponNo = couponNo;
	}

	public int getWallet() {
		return wallet;
	}

	public void setWallet(int wallet) {
		this.wallet = wallet;
	}

}
