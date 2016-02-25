package com.ruiyu.taozhuma.model;

import java.io.Serializable;
import java.util.ArrayList;




/**
 * 产品列表Model
 * @author FU
 * 
 */

public class ProductListModel implements Serializable{
	public Integer id;
	public String image;//图
	public String name;//
	public String subTypeId;//大分类（1为童车，2为奶嘴，3为童装，4为玩具）
	public String typeId;//小分类
	public String  model;//
	public String quantity;//起订量
	public String price;//价格
	

	

}
