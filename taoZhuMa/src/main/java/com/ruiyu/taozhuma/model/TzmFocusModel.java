package com.ruiyu.taozhuma.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 首页焦点图Model
 * 
 * @author Fu
 * 
 */

@SuppressWarnings("serial")
public class TzmFocusModel implements Serializable {
	public Integer id;// 店铺或产品ID（为空则为首页）
	public Integer subType;// 类型1为首页，2为产品，3为店铺
	public String image;// 焦点图
	public String activityId;
	public String title;

}
