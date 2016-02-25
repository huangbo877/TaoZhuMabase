package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

public class EditUserInfoApi implements BaseApi{
	private long uid;//会员ID
	private String name;//昵称
	private Integer sex;//性别：1-男 2-女
	private String file;//用户logo file
	private String birth;//出生日期
	private String QQ;//出生日期
	@Override
	public Map<String, String> getParamMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("uid", uid+"");
		map.put("name", name+"");
		map.put("sex", sex+"");
		map.put("file", file+"");
		map.put("birth", birth+"");
		map.put("QQ", QQ+"");
		return map;
	}

	@Override
	public String getUrl() {
		return AppConfig.TZM_EDITUSERINFO_URL;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public String getBirth() {
		return birth;
	}

	public void setBirth(String birth) {
		this.birth = birth;
	}
	public String getQQ() {
		return QQ;
	}

	public void setQQ(String QQ) {
		this.QQ = QQ;
	}

}
