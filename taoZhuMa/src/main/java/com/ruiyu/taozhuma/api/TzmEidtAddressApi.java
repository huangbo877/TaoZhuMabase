package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

public class TzmEidtAddressApi implements BaseApi {
	private Integer uid;// 会员id
	private Long addId;// 地址id
	private Integer province;// 省份(广东省)
	private Integer city;// 城市(汕头)
	private String tel;// 手机
	private String name;// 收货人
	private Integer area;// 区
	private String address;// 详细地址
	private String postCode;// 邮编
	private Integer isDefault;// 默认地址（取业务字典：0否1是）

	@Override
	public Map<String, String> getParamMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("uid", this.uid + "");
		map.put("addId", this.addId + "");
		map.put("province", this.province + "");
		map.put("city", this.city + "");
		map.put("tel", this.tel + "");
		map.put("name", this.name + "");
		map.put("area", this.area + "");
		map.put("address", this.address + "");
		map.put("postCode", this.postCode + "");
		map.put("isDefault", this.isDefault + "");
		return map;
	}

	@Override
	public String getUrl() {
		return AppConfig.TZM_EIDTADDRESS_URL;
	}

	public Integer getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Integer isDefault) {
		this.isDefault = isDefault;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public Long getAddId() {
		return addId;
	}

	public void setAddId(Long addId) {
		this.addId = addId;
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

	public Integer getArea() {
		return area;
	}

	public void setArea(Integer area) {
		this.area = area;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPostcode() {
		return postCode;
	}

	public void setPostcode(String postcode) {
		this.postCode = postcode;
	}

}
