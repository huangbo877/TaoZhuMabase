package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

public class TzmReSetPassWordApi implements BaseApi{
	private String phone;
	private String passWord;
	private String pass;
	private String sign;//加密验证
	@Override
	public Map<String, String> getParamMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("phone", this.phone+"");
		map.put("pass", this.pass+"");
		map.put("passWork", this.passWord+"");
		map.put("sign", this.sign);
		return map;		
	}

	@Override
	public String getUrl() {
		return AppConfig.TZM_RESETPASSWORD_URL;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	
}
