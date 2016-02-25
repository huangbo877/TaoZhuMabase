package com.ruiyu.taozhuma.model;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * 查看购物车Model
 * @author Fu
 *
 */

public class MyCartModel implements Serializable{
	public int shop_id;//代理商id
	public int table_type;//0=(阿里巴巴商家)b2bstore,1=(淘宝商家)b2cstore
	public String shop_name;//门店名称
	public String agent_name;//代理商名称
	public String status;
	
	public ArrayList<Product> products;//购物车产品
	public class Product {
		public int id;//购物车id
		public int product_id;//产品ID
		public String product_name;//产品名称
		public String product_price;//产品价格
		public int shopping_number;//产品数量
		public String product_image;//产品图片
}
}