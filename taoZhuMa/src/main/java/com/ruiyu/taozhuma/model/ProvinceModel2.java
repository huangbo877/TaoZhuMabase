package com.ruiyu.taozhuma.model;

import java.util.List;

public class ProvinceModel2 {
	private String name;
	private List<CityModel> cityList;
	
	public ProvinceModel2() {
		super();
	}

	public ProvinceModel2(String name, List<CityModel> cityList) {
		super();
		this.name = name;
		this.cityList = cityList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<CityModel> getCityList() {
		return cityList;
	}

	public void setCityList(List<CityModel> cityList) {
		this.cityList = cityList;
	}

	@Override
	public String toString() {
		return "ProvinceModel [name=" + name + ", cityList=" + cityList + "]";
	}
	
}
