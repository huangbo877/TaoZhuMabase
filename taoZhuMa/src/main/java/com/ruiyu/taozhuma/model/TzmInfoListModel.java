package com.ruiyu.taozhuma.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 行业资讯Model
 * @author feng
 */

public class TzmInfoListModel implements Serializable{
	public int id;//资讯id
	public String title;//资讯标题
	public String content;//资讯部分内容
	public String image;//资讯图片
	public String addTime;//发布时间
}
