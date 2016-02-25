package com.ruiyu.taozhuma.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 注册提交信息Model
 * @author feng
 */

public class TzmRegisterModel implements Serializable{
	public int uid;//会员id
	public int type;//账户类型：0.管理员1.普通用户2.供应商3.采购商
	public String phone;//手机号码
	public String name;//会员名称
	public int judgeType;//是否提交过分销商或供应商申请0未提交，1提交过供应商，2提交过分销商
	public int status;//0 未审核，1审核通过
}
