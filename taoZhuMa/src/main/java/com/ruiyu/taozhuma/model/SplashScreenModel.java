package com.ruiyu.taozhuma.model;

import java.io.Serializable;

/**
 * 
 * @author 林尧 2015-12-15 29.1 活动闪屏接口
 */
public class SplashScreenModel implements Serializable{

	
	public String title;// 标题
	public String webLink;// Web链接
	public String image_ios4;// 图片（iPhone4屏幕尺寸）
	public String image_ios5;// 图片（iPhone5屏幕尺寸）
	public String image_android;// 图片（安卓屏幕尺寸）

}