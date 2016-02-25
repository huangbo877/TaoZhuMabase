package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

/**
 * 立即购买提交订单
 * 
 * @author Eve
 * 
 */
public class TzmAddOrderByPurchaseApi implements BaseApi {
	private int uid;// 会员id
	private Long addressId;// 我的地址ID
	private Integer pid;// 商品ID
	private String buyer_message;// 买家留言
	private int consigneeType;// 收货方式（1，快递2，自提）
	private int payMethod;// 支付方式： 1:支付宝；2：微信支付（默认1）
	private Integer num;// 商品数量
	private String couponNo;// 优惠券码

	@Override
	public Map<String, String> getParamMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("uid", this.uid + "");
		map.put("addressId", this.addressId + "");
		map.put("pid", this.pid + "");
		map.put("buyer_message", this.buyer_message + "");
		map.put("consigneeType", this.consigneeType + "");
		map.put("payMethod", this.payMethod + "");
		map.put("num", this.num + "");
		map.put("couponNo", this.couponNo);
		return map;
	}

	@Override
	public String getUrl() {
		return AppConfig.TZM_ADDORDERBYPURCHASE_URL;
	}

	public String getCouponNo() {
		return couponNo;
	}

	public void setCouponNo(String couponNo) {
		this.couponNo = couponNo;
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

	public String getBuyer_message() {
		return buyer_message;
	}

	public void setBuyer_message(String buyer_message) {
		this.buyer_message = buyer_message;
	}

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

	public Integer getpid() {
		return pid;
	}

	public void setpid(Integer pid) {
		this.pid = pid;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

}
