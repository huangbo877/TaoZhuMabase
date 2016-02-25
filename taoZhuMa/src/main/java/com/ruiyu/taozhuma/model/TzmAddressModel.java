package com.ruiyu.taozhuma.model; 

import java.io.Serializable;

/** 
 * @author LinJianhong 
 * @version 创建时间：2015年4月27日 上午11:34:07 
 * 地址管理Model
 */
@SuppressWarnings("serial")
public class TzmAddressModel implements Serializable {
	public Long addId;//地址ID
	public String address;//地址详情
	public String name;//收货人名字
	public String postcode;//邮编
	public String tel;//收货人电话
	public int isDefault;//是否为默认地址1是0否
	public int isRemote;//偏远0-否，1-是
	
}
 