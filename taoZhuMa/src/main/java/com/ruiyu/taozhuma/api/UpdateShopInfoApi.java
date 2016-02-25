package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

public class UpdateShopInfoApi implements BaseApi{
	private Integer id;//店铺id
	private Integer uid;//用户id
	private String image;//店铺logo
	private String  name;//店铺名称
	private String  address;//地址
	private String  content;//店铺介绍
	private String  salesArea;//主要销售市场
	private String  phone;//手机
	private Integer  mainCategory;//主营产品id
	private String  mainProduct;//公司主营产品
	@Override
	public Map<String, String> getParamMap() {
		Map<String, String> map=new HashMap<String, String>();
		map.put("id", id+"");
		map.put("uid", uid+"");
		map.put("image", image+"");
		map.put("name", name+"");
		map.put("address", address+"");
		map.put("content", content+"");
		map.put("salesArea", salesArea+"");
		map.put("phone", phone+"");
		map.put("mainCategory", mainCategory+"");
		map.put("mainProduct", mainProduct+"");
		return map;
	}

	@Override
	public String getUrl() {
		return AppConfig.TZM_UPDATESHOPINFO_URL;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSalesArea() {
		return salesArea;
	}

	public void setSalesArea(String salesArea) {
		this.salesArea = salesArea;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getMainCategory() {
		return mainCategory;
	}

	public void setMainCategory(Integer mainCategory) {
		this.mainCategory = mainCategory;
	}

	public String getMainProduct() {
		return mainProduct;
	}

	public void setMainProduct(String mainProduct) {
		this.mainProduct = mainProduct;
	}

}
