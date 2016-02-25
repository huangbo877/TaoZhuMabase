package com.ruiyu.taozhuma.model;

import java.io.Serializable;
/**
 * 我的地址详情Model
 *
 */
public class TzmAddressDetailModel implements Serializable{
	public Integer addId; //地址id
	public Integer uid; //用户id
	public Integer province; //省份(广东省)	
	public Integer city; //城市(汕头)
	public Integer area; //区
	public String address; //详细地址
	public String name; //收货人
	public String tel; //手机号码
	public String postcodes; //邮编
	public Integer defaultStatus; //是否为默认地址1是0否
	public String provinceName; //省份(广东省)
	public String cityName; //城市(汕头)
	public String areaName; //区
}
