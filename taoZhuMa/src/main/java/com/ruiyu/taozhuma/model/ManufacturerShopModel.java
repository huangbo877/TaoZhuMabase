package com.ruiyu.taozhuma.model;

import java.io.Serializable;

public class ManufacturerShopModel implements Serializable{
	public Integer id;//店铺id
	public Integer userId;//用户id
	public String image;//店铺logo
	public String  name;//店铺名称
	public String  address;//地址
	public String  content;//店铺介绍
	public String  main_category;//主营产品
	public String  sales_area;//主要销售市场
	public String  phone;//手机
	public Integer  main_category_id;//主营产品id
	public String  main_product;//公司主营产品
}
