package com.ruiyu.taozhuma.model;

import java.util.ArrayList;

/**
 * 店铺街Model
 * @author fu
 *
 */

public class TzmProductTypeModel{
	public String name;//名称
	public Integer pid;//分类id
	public String image;
	public ArrayList<Type> type;
	public class Type{
		public String name;//名称
		public Integer id;//分类id
	}
}
