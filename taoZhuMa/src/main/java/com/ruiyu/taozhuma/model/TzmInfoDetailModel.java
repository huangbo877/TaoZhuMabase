package com.ruiyu.taozhuma.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 行业资讯详情Model
 * 
 * @author feng
 */

public class TzmInfoDetailModel implements Serializable {
	public int id;// 资讯id
	public String title;// 资讯标题
	public String content;// 资讯内容
	public String author;// 资讯发布者
	public String addTime;// 资讯发布时间
	public String createDate;
	public List<images> image;

	public class images {
		public String image;// 资讯图片
	}

}
