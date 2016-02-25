package com.ruiyu.taozhuma.api;

import java.util.HashMap;
import java.util.Map;

import com.ruiyu.taozhuma.config.AppConfig;

public class TzmSearchAllApi implements BaseApi{
	private String sorting;//排序 （1为销量降序，11为销量升序，2为价格升序，22为价格降序， 4为点击数降序，44为点击数升序，为空ID降序）
	private Integer pageNo;//分页
	private String name;//搜索
	
	@Override
	public Map<String, String> getParamMap() {
		Map<String, String> map=new HashMap<String, String>();
		map.put("sorting", sorting+"");
		map.put("pageNo", pageNo+"");
		map.put("name", name+"");
		return map;
	}

	@Override
	public String getUrl() {
		return AppConfig.TZM_SEARCHALL_URL;
	}

	public String getSorting() {
		return sorting;
	}

	public void setSorting(String sorting) {
		this.sorting = sorting;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
