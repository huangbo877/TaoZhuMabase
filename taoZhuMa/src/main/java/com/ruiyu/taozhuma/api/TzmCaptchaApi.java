package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

/**
 * 
 * 发送手机验证码API
 * @author 
 *
 */
public class TzmCaptchaApi implements BaseApi {

	private String phone;//验证手机号码
	private int source;//1=注册2=修改密码
	private String sign;//加密验证
	//图形验证码二次验证
	private String geetest_challenge;
	private String geetest_validate;
	private String geetest_seccode;
	
	
	
	@Override
	public Map<String, String> getParamMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("phone", this.phone+"");
		map.put("source", this.source+"");
		map.put("sign", this.sign);
		map.put("geetest_challenge", this.geetest_challenge);
		map.put("geetest_validate", this.geetest_validate);
		map.put("geetest_seccode", this.geetest_seccode);
		return map;		
	}

	@Override
	public String getUrl() {
		return AppConfig.TZM_CAPTCHA_URL;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getSource() {
		return source;
	}

	public void setSource(int source) {
		this.source = source;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getGeetest_challenge() {
		return geetest_challenge;
	}

	public void setGeetest_challenge(String geetest_challenge) {
		this.geetest_challenge = geetest_challenge;
	}

	public String getGeetest_validate() {
		return geetest_validate;
	}

	public void setGeetest_validate(String geetest_validate) {
		this.geetest_validate = geetest_validate;
	}

	public String getGeetest_seccode() {
		return geetest_seccode;
	}

	public void setGeetest_seccode(String geetest_seccode) {
		this.geetest_seccode = geetest_seccode;
	}

	
}
