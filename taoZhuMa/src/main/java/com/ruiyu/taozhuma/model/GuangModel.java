package com.ruiyu.taozhuma.model;

import java.io.Serializable;
import java.util.ArrayList;




/**
 * 随便逛逛Model
 * @author FU
 * 
 */

public class GuangModel implements Serializable{
	public Integer userid;//店铺id
	public String storeName;//店铺名称
	public String logo;//店铺LOGO
	public ArrayList<Product> product;//店铺产品
	public class Product {
		public Integer productId;//产品ID
		public String image;//产品图片
		public String name;//产品名称
		
	}
	

}
