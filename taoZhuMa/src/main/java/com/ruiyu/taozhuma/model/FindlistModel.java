package com.ruiyu.taozhuma.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.R.string;

/**
 * 发现列表 Model
 * 
 * @author Fu
 * 
 */
public class FindlistModel implements Serializable {
	public int id;// 信息id
	public int pid;// 产品id
	public int type;// 1为图文信息2为文字信息
	public String image;// 图片
	public String content;// type=1可以为空 type=2不能为空
	public String time;
}
