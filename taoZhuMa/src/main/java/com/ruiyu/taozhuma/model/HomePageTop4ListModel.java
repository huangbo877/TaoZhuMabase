package com.ruiyu.taozhuma.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class HomePageTop4ListModel implements Serializable {
	public Integer typeId;// 产品ID，列表时为”null”
	public String image;// 图片
	public String title;// 活动标题
	public Integer sorting;// 排序

}
