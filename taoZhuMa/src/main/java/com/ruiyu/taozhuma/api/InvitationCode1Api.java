package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

public class InvitationCode1Api implements BaseApi {
	private Integer uid;
	private String inviteCode;
	private String sign;// 加密验证

	@Override
	public Map<String, String> getParamMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("uid", this.uid + "");
		map.put("inviteCode", this.inviteCode + "");
		map.put("sign", this.sign);
		return map;
	}

	@Override
	public String getUrl() {
		return AppConfig.SUBMITINVITECODE_URL;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public String getinviteCode() {
		return inviteCode;
	}

	public void setinviteCode(String inviteCode) {
		this.inviteCode = inviteCode;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

}
