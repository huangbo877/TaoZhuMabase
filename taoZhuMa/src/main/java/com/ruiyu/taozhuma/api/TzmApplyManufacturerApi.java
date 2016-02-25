package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

public class TzmApplyManufacturerApi implements BaseApi{
	private long uid;//会员id
	private String companyName;//公司名称
	private Integer mainCategory;//主营类目
	private String brand;//自营品牌
	private Integer groups;//运营总人数
	private String salesArea;//销售地区
	private String contact;//联系人
	private String duty;//联系人职务
	private String email;//联系人邮箱
	private String QQ;//联系人QQ
//	private String tel;//联系电话
	private String phone;//手机号码
	private String fax;//传真号码
	private String webSite;//网店URL
	private String mainProduct;//主营产品
	private String content;//公司简介
	private String user3cImage;//3C认证
	private String attestationImage;//营业执照
	private String address;//公司地址
	@Override
	public Map<String, String> getParamMap() {
		Map<String, String> map=new HashMap<String, String>();
		map.put("uid", this.uid+"");
		map.put("companyName", this.companyName+"");
		map.put("mainCategory", this.mainCategory+"");
		map.put("brand", this.brand+"");
		map.put("groups", this.groups+"");
		map.put("salesArea", this.salesArea+"");
		map.put("contact", this.contact+"");
		map.put("duty", this.duty+"");
		map.put("email", this.email+"");
		map.put("QQ", this.QQ+"");
		map.put("phone", this.phone+"");
		map.put("fax", this.fax+"");
		map.put("webSite", this.webSite+"");
		map.put("mainProduct", this.mainProduct+"");
		map.put("content", this.content+"");
		map.put("user3cImage", this.user3cImage+"");
		map.put("attestationImage", this.attestationImage+"");
		map.put("address", this.address+"");
		return map;
	}

	@Override
	public String getUrl() {
		return AppConfig.TZM_APPLYMANUFACTURE_URL;
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

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public Integer getGroups() {
		return groups;
	}

	public void setGroups(Integer groups) {
		this.groups = groups;
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

	public String getWebSite() {
		return webSite;
	}

	public void setWebSite(String webSite) {
		this.webSite = webSite;
	}

	public String getMainProduct() {
		return mainProduct;
	}

	public void setMainProduct(String mainProduct) {
		this.mainProduct = mainProduct;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUser3cImage() {
		return user3cImage;
	}

	public void setUser3cImage(String user3cImage) {
		this.user3cImage = user3cImage;
	}

	public String getAttestationImage() {
		return attestationImage;
	}

	public void setAttestationImage(String attestationImage) {
		this.attestationImage = attestationImage;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
