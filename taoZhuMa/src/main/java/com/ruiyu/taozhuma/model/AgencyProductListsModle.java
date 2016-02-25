package com.ruiyu.taozhuma.model;

import java.io.Serializable;

public class AgencyProductListsModle implements Serializable{
	public Integer agencyUserId;//批发商用户ID
	public Integer agencyId;//批发商ID
	public Integer manufacturerUid;//代理商户的ID
	
	public String manufacturerName;//代理商户名称
	public Integer id;//产品id
	public String image;//图片
	public String name;//名称
	public Integer price;//产品价格
	public Integer AgencyStock;//库存
	public Integer checked;//是否选中
}
