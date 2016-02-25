package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

/**
 * 
 * 提交注册信息API
 * 
 * @author
 * 
 */
public class TzmRegisterApi implements BaseApi {

	private String phone;// 验证手机号码
	private String captcha;// 验证码
	private String pass;// 密码
	private String regChannel;// 渠道
	private Integer type;// 选择注册类型1为分销2为供应 6为代理商
	private String baidu_uid;// 友盟机器码
	private String imei;// 手机串行号
	private String sign;// 加密验证

	@Override
	public Map<String, String> getParamMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("phone", this.phone + "");
		map.put("captcha", this.captcha + "");
		map.put("pass", this.pass + "");
		map.put("type", this.type + "");
		map.put("regChannel", this.regChannel + "");
		map.put("baidu_uid", this.baidu_uid);
		map.put("imei", this.imei);
		map.put("sign", this.sign);
		return map;
	}

	@Override
	public String getUrl() {
		return AppConfig.TZM_REGISTER_URL;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCaptcha() {
		return captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}

	public String getregChannel() {
		return regChannel;
	}

	public void setregChannel(String regChannel) {
		this.regChannel = regChannel;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getRegChannel() {
		return regChannel;
	}

	public void setRegChannel(String regChannel) {
		this.regChannel = regChannel;
	}

	public String getBaidu_uid() {
		return baidu_uid;
	}

	public void setBaidu_uid(String baidu_uid) {
		this.baidu_uid = baidu_uid;
	}

	public Integer getType() {
		return type;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

}