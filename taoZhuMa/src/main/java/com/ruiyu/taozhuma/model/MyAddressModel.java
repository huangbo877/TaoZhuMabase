package com.ruiyu.taozhuma.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 我的地址Model
 * @author Fu
 *
 */

@SuppressWarnings("serial")
public class MyAddressModel implements Serializable{
	public Integer id;//地址id
	public Integer uid;//用户id
	public String address;//地址
	public String name;//收货人
	public String tel;//手机号码
	public String postcodes;//邮编
	
	public Integer defaultStatus;//是否默认地址 是->1,否->0;
	
}
