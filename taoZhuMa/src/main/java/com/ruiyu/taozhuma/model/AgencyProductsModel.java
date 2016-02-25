package com.ruiyu.taozhuma.model;

import java.io.Serializable;

public class AgencyProductsModel implements Serializable{
	public Integer agencyUserId;//批发商用户ID
	public Integer id;//产品id
	public String image;//图片
	public String name;//名称
	public float price;//产品价格
	public Integer AgencyStock;//库存数量
}
