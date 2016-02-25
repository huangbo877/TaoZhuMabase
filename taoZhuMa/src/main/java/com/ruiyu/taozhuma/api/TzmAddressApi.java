package com.ruiyu.taozhuma.api; 

import java.util.HashMap;

import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;
public class TzmAddressApi implements BaseApi{

	private Integer uid;//会员id
	private	Integer province;//省份
	private	Integer city;//城市
	private	Integer area;//区
	private	String tel;//手机号码
	private	String name;//收货人
	private	String address;//详细地址
	private	String postCode;//邮编
	private	Integer isDefault;//默认地址（取业务字典：0否1是）
	
	@Override
	public Map<String, String> getParamMap() {
		Map<String,String> map = new HashMap<String, String>();
		map.put("uid", this.uid+"");
		map.put("province", this.province+"");
		map.put("city", this.city+"");
		map.put("area", this.area+"");
		map.put("tel", this.tel+"");
		map.put("name", this.name+"");
		map.put("address", this.address+"");
		map.put("postCode", this.postCode+"");
		map.put("isDefault", this.isDefault+"");
		return map;
	}

	@Override
	public String getUrl() {
		return AppConfig.TZM_ADDADDRESS_URL;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public Integer getProvince() {
		return province;
	}

	public void setProvince(Integer province) {
		this.province = province;
	}

	public Integer getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Integer isDefault) {
		this.isDefault = isDefault;
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

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	

}
 