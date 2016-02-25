package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

public class ApplyAgencyApi implements BaseApi{
	private int uid; //会员id
	private String contact; //联系人
	private String tel; //联系电话
	private String phone; //手机号码
	private String fax; //传真号码
	private String addressHome; //家庭住址
	private String addressCompany; //公司地址
	private String corporation; //法人代表
	private Integer province; //省
	private Integer city; //市
	private Integer area; //区
	private Integer manufacturerId;//代理商ID（无该字段）
	private String proxyProduct; //代理批发产品编号，半角逗号分隔，格式：proxy_product=产品a的编号,产品b的编号......
	private Integer mainCategory;//主营类目（1为遥控/电动玩具；2为早教/音乐玩具 ；3为过家家玩具；4为童车玩具；5为益智玩具；6为其他玩具）
	private String company; //公司名称
	private String busLicence; //营业执照  file1
	private String email; //联系人邮箱
	private String QQ; //联系人QQ
	@Override
	public Map<String, String> getParamMap() {
		Map<String, String> map=new HashMap<String, String>();
		map.put("uid", this.uid+"");
		map.put("contact", this.contact+"");
		map.put("tel", this.tel+"");
		map.put("phone", this.phone+"");
		map.put("fax", this.fax+"");
		map.put("addressHome", this.addressHome+"");
		map.put("addressCompany", this.addressCompany+"");
		map.put("province", this.province+"");
		map.put("city", this.city+"");
		map.put("area", this.area+"");
		map.put("manufacturerId", 0+"");
		map.put("proxyProduct", this.proxyProduct+"");
		map.put("mainCategory", 0+"");
		map.put("company", this.company+"");
		map.put("busLicence", this.busLicence+"");
		map.put("email", this.email+"");
		map.put("QQ", this.QQ+"");
		return map;
	}

	@Override
	public String getUrl() {
		return AppConfig.TZM_APPLYAGENCYAPI_URL;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
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

	public String getAddressHome() {
		return addressHome;
	}

	public void setAddressHome(String addressHome) {
		this.addressHome = addressHome;
	}

	public String getAddressCompany() {
		return addressCompany;
	}

	public void setAddressCompany(String addressCompany) {
		this.addressCompany = addressCompany;
	}

	public String getCorporation() {
		return corporation;
	}

	public void setCorporation(String corporation) {
		this.corporation = corporation;
	}

	public Integer getProvince() {
		return province;
	}

	public void setProvince(Integer province) {
		this.province = province;
	}

	public Integer getCity() {
		return city;
	}

	public void setCity(Integer city) {
		this.city = city;
	}

	public Integer getArea() {
		return area;
	}

	public void setArea(Integer area) {
		this.area = area;
	}

	public Integer getManufacturerId() {
		return manufacturerId;
	}

	public void setManufacturerId(Integer manufacturerId) {
		this.manufacturerId = manufacturerId;
	}

	public String getProxyProduct() {
		return proxyProduct;
	}

	public void setProxyProduct(String proxyProduct) {
		this.proxyProduct = proxyProduct;
	}

	public Integer getMainCategory() {
		return mainCategory;
	}

	public void setMainCategory(Integer mainCategory) {
		this.mainCategory = mainCategory;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getBusLicence() {
		return busLicence;
	}

	public void setBusLicence(String busLicence) {
		this.busLicence = busLicence;
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

	
	
}
