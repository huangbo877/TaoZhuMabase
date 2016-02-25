package com.ruiyu.taozhuma.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Fu
 * 
 *         全国省市区model
 */
public class CitiesModel implements Serializable {
	public Integer provinceId;// 省id
	public String provincState;// 省名称
	public List<City> cities;// 城市列表

	public class City {
		public Integer cityId;// 城市id
		public String cityState;// 城市名称
		public List<Area> areas;// 城市列表

	}

	public class Area {
		public Integer areaId;// 区id
		public String areaState;// 区名称
	}
}
