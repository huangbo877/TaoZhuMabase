package com.ruiyu.taozhuma.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ActivityTimeListModel implements Serializable {
	public String title;// 活动标题
	public Integer status;// 活动状态，0-未开始；1-进行中
	public String activityDate;// 活动日期
	public String beginTime;// 活动开始时间
	public String endTime;// 活动结束时间
	public Integer timeId;// 对应活动时间表id
	public String banner;
	public String currentTime;
}
