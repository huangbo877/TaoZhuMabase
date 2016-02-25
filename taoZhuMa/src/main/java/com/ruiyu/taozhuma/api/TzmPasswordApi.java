package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

/**
 * 
 * 修改密码API
 * @author 
 *
 */
public class TzmPasswordApi implements BaseApi {

	private String phone;//手机号码
	private String pass;//新密码
	private String captcha;//验证码
	private String sign;//加密验证
	
	@Override
	public Map<String, String> getParamMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("phone", this.phone+"");
		map.put("pass", this.pass+"");
		map.put("captcha", this.captcha+"");
		map.put("sign", this.sign);
		return map;		
	}

	@Override
	public String getUrl() {
		return AppConfig.TZM_PASSWORD_URL;
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

	public String getCaptcha() {
		return captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	
}
