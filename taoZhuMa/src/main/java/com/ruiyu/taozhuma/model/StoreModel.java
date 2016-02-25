package com.ruiyu.taozhuma.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 店铺信息Model
 * 
 * @author fu
 * 
 */

public class StoreModel implements Serializable {
	public int storeId;// 店铺id
	public String storeName;// 店铺名称
	public String logo;// 店铺logo
	public String storelocation;// 店铺地址
	public String distance;// 距离
	public int entity;//是否有实体店（0为否，1为是）

}