package com.ruiyu.taozhuma.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 店铺详情Model
 * @author Fu
 *
 */

public class BusinessDetailModel implements Serializable{
	public Integer id;//商家详情id
	public Integer type;//512->供应商，256->经销商，128->C店店家，64->顾客
	public String storeName;//企业名称
	public String logo;//企业logo
	public String address;//地址
	public String telephone;//电话
	public String totalpro;//产品总数
	public Double lng;//经度
	public Double lat;//纬度
	public String mainProducts;//主营
	public Integer userid;
	public String fax;
	public String contact;
	public String province;
	public String email;
	public String qq;
	
	
	public ArrayList<images> images;//三个图片组成的数组
	public class images {
		public Integer id;//产品id
		public String name;//产品名称
		public String itemPrice;//产品价
		public String imageName;//产品图片
	}
	
}
