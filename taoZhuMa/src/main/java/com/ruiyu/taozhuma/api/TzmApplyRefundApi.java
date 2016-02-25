package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

public class TzmApplyRefundApi implements BaseApi{
	public Integer uid;//会员id
	public Integer oid;//订单详情id
	public Integer sid;//店铺Id
	public Integer type;//1为退货2为退款3为售后
	public Integer refundType;//原因1为不喜欢2为质量不好3为尺码不对4为颜色不对5为其他
	public Integer num;//退回数量
	public String image;//凭证
	public String refundReason;//说明
	@Override
	public Map<String, String> getParamMap() {
		Map<String, String> map =new HashMap<String, String>();
		map.put("uid", uid+"");
		map.put("oid", oid+"");
		map.put("sid", sid+"");
		map.put("type", type+"");
		map.put("refundType", refundType+"");
		map.put("num", num+"");
		map.put("image", image+"");
		map.put("refundReason", refundReason+"");
		return map;
	}

	@Override
	public String getUrl() {
		return AppConfig.TZM_APPLYREFUND_URL;
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

	public Integer getSid() {
		return sid;
	}

	public void setSid(Integer sid) {
		this.sid = sid;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getRefundType() {
		return refundType;
	}

	public void setRefundType(Integer refundType) {
		this.refundType = refundType;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getRefundReason() {
		return refundReason;
	}

	public void setRefundReason(String refundReason) {
		this.refundReason = refundReason;
	}

}
