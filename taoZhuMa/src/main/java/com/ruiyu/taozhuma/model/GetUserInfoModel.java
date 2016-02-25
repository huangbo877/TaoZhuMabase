package com.ruiyu.taozhuma.model;

import java.io.Serializable;

public class GetUserInfoModel implements Serializable {
	public String name;// 昵称
	public Integer sex;// 性别：1-男 2-女
	public String image;// 用户logo
	public String birth;// 出生日期
	public int babySex;// baby性别
	public String babySexName;// baby性别名称
	public String babyBirthday;// baby生日
	public String QQ;
}
