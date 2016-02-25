package com.ruiyu.taozhuma.model;

import java.util.ArrayList;

import org.w3c.dom.Text;

/**
 * 品牌产品列表Model
 * @author Fu
 *
 */

public class ProductbrandModel {
	public int id;//产品id
	public String image;//图片
	public String name;//名称
	public String price;//产品价格
	public int subTypeId;//产品分类
	public String priceOld;//原价
	public String distance;//距离
	public String sell_number;//产品销量
	public Integer status;//是否同城（同城须显示绿色）1为同城2为官方3为其他地区
}
