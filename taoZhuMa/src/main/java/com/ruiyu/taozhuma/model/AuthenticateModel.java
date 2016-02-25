package com.ruiyu.taozhuma.model;

/**
 * 收藏夹Model
 * @author Boo
 *
 */

public class AuthenticateModel {
	public int uid;// 用户id
	public int type;//认证的类型：1，个人认证    2，企业认证
	public String image;//认证图片
	public int authenticate;//认证状态：0（尚未认证）1（认证成功）2（审核中）3（认证失败）
	
}
