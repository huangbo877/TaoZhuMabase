package com.ruiyu.taozhuma.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class UserModel implements Serializable {
	public Integer uid;// 会员id
	public Integer type;// 账户类型0.管理员1.普通用户2.供应商3.采购商,6批发商
	public String name;// 用户昵称
	public Integer judgeType;// 是否提交过分销商或供应商申请0未提交，1提交过供应商，2提交过分销商,6提交过代理商
	public String phone;// 手机号码
	public int status;// 0 未审核，1审核通过
	public int agencyId;// 批发商id，没有为0
	public String token;//融云token
}
