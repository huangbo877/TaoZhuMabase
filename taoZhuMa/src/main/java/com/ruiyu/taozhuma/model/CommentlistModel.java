package com.ruiyu.taozhuma.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.R.string;

/**
 *	评论列表 Model
 * @author Fu
 *
 */
public class CommentlistModel implements Serializable{
	public String name;
	public String comment;
	public String time;
	public String[] images;
	public int num;//无图数量
	public int nun;//有图图数量
}
