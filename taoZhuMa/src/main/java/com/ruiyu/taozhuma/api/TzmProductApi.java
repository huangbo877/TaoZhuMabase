package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

/**
 * 商品列表
 * 
 * @author Eve
 * 
 */
public class TzmProductApi implements BaseApi {
	private Integer uid;// 会员id
	private int pageNo;// 
	private Integer subTypeId;// （0为所有；1为遥控/电动玩具；2为早教/音乐玩具
								// ；3为过家家玩具；4为童车玩具；5为益智玩具；6为其他玩具）
	private Integer typeId;// 小分类
	private Integer brandid;// 品牌id
	private Integer sorting;// (0为热门(降序)，33为热门（升序），1为销售数量（降序），11为销售数量（升序），2为价格从高到低，22为价格从低到高,4为新品排序)
	private Integer display;// 是否新品：0=》否；1=》是
	private String keys;// 搜索关键字（产品名称）

	@Override
	public Map<String, String> getParamMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("uid", this.uid + "");
		map.put("pageNo", this.pageNo + "");
		map.put("subTypeId", this.subTypeId + "");
		map.put("typeId", this.typeId + "");
		map.put("brandid", this.brandid + "");
		map.put("sorting", this.sorting + "");
		map.put("display", this.display + "");
		map.put("keys", this.keys + "");
		return map;
	}

	@Override
	public String getUrl() {
		return AppConfig.TZM_PRODUCT_URL;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public Integer getSubTypeId() {
		return subTypeId;
	}

	public void setSubTypeId(Integer subTypeId) {
		this.subTypeId = subTypeId;
	}

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public Integer getBrandid() {
		return brandid;
	}

	public void setBrandid(Integer brandid) {
		this.brandid = brandid;
	}

	public Integer getSorting() {
		return sorting;
	}

	public void setSorting(Integer sorting) {
		this.sorting = sorting;
	}

	public Integer getDisplay() {
		return display;
	}

	public void setDisplay(Integer display) {
		this.display = display;
	}

	public String getKeys() {
		return keys;
	}

	public void setKeys(String keys) {
		this.keys = keys;
	}

}
