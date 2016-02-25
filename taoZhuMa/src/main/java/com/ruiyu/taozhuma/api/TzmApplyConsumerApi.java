package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

public class TzmApplyConsumerApi implements BaseApi{
	private long uid;//会员id
	private String companyName;//店铺名称
	private Integer mainCategory;//主营类目
	private Integer groups;//运营总人数
	private String platform;//销售平台
	private String salesArea;//销售地区
	private String contact;//联系人
	private String duty;//联系人职务
	private String email;//联系人邮箱
	private String QQ;//联系人QQ
	private String tel;//联系电话
	private String phone;//手机号码
	private String fax;//传真号码
	private String type;//判断是网店还是实体店
	private String content;//网店或者实体店地址
	@Override
	public Map<String, String> getParamMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("uid", this.uid+"");
		map.put("companyName", this.companyName+"");
		map.put("mainCategory", this.mainCategory+"");
		map.put("groups", this.groups+"");
		map.put("platform", this.platform+"");
		map.put("salesArea", this.salesArea+"");
		map.put("contact", this.contact+"");
		map.put("duty", this.duty+"");
		map.put("email", this.email+"");
		map.put("QQ", this.QQ+"");
		map.put("tel", this.tel+"");
		map.put("phone", this.phone+"");
		map.put("fax", this.fax+"");
		map.put("type", this.type+"");
		map.put("content", this.content+"");
		return map;		
	}

	@Override
	public String getUrl() {
		return AppConfig.TZM_APPLYCONSUMER_URL;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Integer getMainCategory() {
		return mainCategory;
	}

	public void setMainCategory(Integer mainCategory) {
		this.mainCategory = mainCategory;
	}

	public Integer getGroups() {
		return groups;
	}

	public void setGroups(Integer groups) {
		this.groups = groups;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getSalesArea() {
		return salesArea;
	}

	public void setSalesArea(String salesArea) {
		this.salesArea = salesArea;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getDuty() {
		return duty;
	}

	public void setDuty(String duty) {
		this.duty = duty;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getQQ() {
		return QQ;
	}

	public void setQQ(String qQ) {
		QQ = qQ;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
