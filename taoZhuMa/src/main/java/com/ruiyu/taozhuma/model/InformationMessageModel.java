package com.ruiyu.taozhuma.model;

import java.util.ArrayList;



/**
 * 资讯信息Model
 * @author Boo
 *
 */

public class InformationMessageModel {
	public int id;//资讯id
	public String title;//资讯标题
	public String image;//资讯图片
	public String source;//资讯来源
	public String content;//资讯内容
	public String author;//资讯发布者
	public String location;//所在地
	public String addtime;//资讯发布时间
	public ArrayList<InformationMessageModel> info;//新闻框  4条一框
	public int type;
	public String time;
	public int pid;
}
