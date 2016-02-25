package com.ruiyu.taozhuma.model;

import java.util.ArrayList;



/**
 * 资讯信息Model
 * @author Boo
 *
 */

public class ExhibitionInformationModel {
	public int id;//展会id
	public String title;//展会标题
	public String image;//展会logo
	public String source;//来源
	public String content;//展会内容
	public String author;//展会发布者
	public String location;//所在地
	public String addtime;//展会发布时间
	public String exhibitionTime;//展会时间
	public ArrayList<ExhibitionInformationModel> info;
	
}
