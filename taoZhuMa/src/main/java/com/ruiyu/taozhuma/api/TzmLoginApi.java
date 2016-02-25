package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

/**
 * 
 * 会员登录API
 * 
 * @author
 * 
 */
public class TzmLoginApi implements BaseApi {

	private String phone;// 会员登录账号
	private String pass;// 登录密码
	private String baidu_uid;// 百度id//友盟token
	private String sign;//加密验证

	@Override
	public Map<String, String> getParamMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("phone", this.phone + "");
		map.put("pass", this.pass + "");
		map.put("baidu_uid", this.baidu_uid);
		map.put("sign", this.sign);
		return map;
	}

	@Override
	public String getUrl() {
		// return AppConfig.TZM_LOGIN_URL;
		return AppConfig.TZM_AGENTLOGIN_URL;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getBaidu_uid() {
		return baidu_uid;
	}

	public void setBaidu_uid(String baidu_uid) {
		this.baidu_uid = baidu_uid;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
	

}
