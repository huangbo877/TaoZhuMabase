package com.ruiyu.taozhuma.model;

import java.util.ArrayList;

/**
 * 产品分类Model
 * @author Boo
 *
 */

public class ProductTypeModel {
	public int id;//类型id
	public String name;//名称
	public ArrayList<SubProductTypeModel> subcategory;//子类型
	public class SubProductTypeModel{
		public int id;
		public String name;
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		
	}
}
