package com.ruiyu.taozhuma.model;

import java.util.List;

/**
 * 查看钱包明细Model
 * 
 * @author Fu
 * 
 */

public class UserWelletDetailModel {
	public Double balance;// 钱包余额
	public List<source> detailList;// 三个图片组成的数组
	public class source {
		public String receive;// 获取的金额
		public String source;// 来源名称
		public String addTime;// 添加时间
		public String digest;// 来源明细
		public int type;
	}

}
