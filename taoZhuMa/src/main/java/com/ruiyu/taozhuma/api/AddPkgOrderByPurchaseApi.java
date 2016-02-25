package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

/**
 * 
 * 套餐直接购买提交订单
 * 
 * @author
 * 
 */
public class AddPkgOrderByPurchaseApi implements BaseApi {

	private Integer uid;// 会员id
	private Integer pkgId;// 套餐ID
	private Integer consigneeType;// 收货方式（1，快递2，自提）
	private Integer payMethod;// 1:支付宝；2：微信支付
	private Long addressId;// 我的地址ID，快递时必填
	private String buyer_message;// 买家留言
	private String couponNo;// 优惠券码

	@Override
	public Map<String, String> getParamMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("uid", this.uid + "");
		map.put("pkgId", this.pkgId + "");
		map.put("consigneeType", this.consigneeType + "");
		map.put("payMethod", this.payMethod + "");
		map.put("addressId", this.addressId + "");
		map.put("buyer_message", this.buyer_message);
		map.put("couponNo", this.couponNo);
		return map;
	}

	@Override
	public String getUrl() {
		return AppConfig.ADDPKGORDERBYPURCHASE_URL;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public Integer getPkgId() {
		return pkgId;
	}

	public void setPkgId(Integer pkgId) {
		this.pkgId = pkgId;
	}

	public Integer getConsigneeType() {
		return consigneeType;
	}

	public void setConsigneeType(Integer consigneeType) {
		this.consigneeType = consigneeType;
	}

	public Integer getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(Integer payMethod) {
		this.payMethod = payMethod;
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

	public String getCouponNo() {
		return couponNo;
	}

	public void setCouponNo(String couponNo) {
		this.couponNo = couponNo;
	}

}
