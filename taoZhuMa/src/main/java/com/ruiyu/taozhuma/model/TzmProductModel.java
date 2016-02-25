package com.ruiyu.taozhuma.model;

import java.io.Serializable;

/**
 * 产品Model
 * @author Fu
 *
 */

public class TzmProductModel implements Serializable{
	public int id;//产品id
	public String price;//产品价格
	public Integer sellNumber;//销售数量
	public String name;//名称
	public String image;//图片
	public String subTypeId;//产品分类
	public Integer activityId;//活动专场ID
}
