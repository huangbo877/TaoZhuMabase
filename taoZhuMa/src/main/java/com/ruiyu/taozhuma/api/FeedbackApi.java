package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;
/**
 * 
 * 反馈API
 * @author Fu
 *
 */
public class FeedbackApi implements BaseApi {

	private Integer uid;
	private int type;
	private String content;
	private String email;
	private String phone;
	
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public Map<String, String> getParamMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("uid", uid+"");
		map.put("type", type+"");
		map.put("email", email+"");
		map.put("phone", phone+"");
		map.put("content", content+"");
		return map;		
	}

	@Override
	public String getUrl() {
		return AppConfig.TZM_FEEDBACK_URL;
	}
	
}
