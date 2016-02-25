package com.ruiyu.taozhuma.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 消息Model
 * @author feng
 */

public class TzmClientInfoModel implements Serializable{
	public int id;//信息id
	public String author;//作者
	public String title;//标题
	public String content;//内容
	public String createDate;//创建时间
}
