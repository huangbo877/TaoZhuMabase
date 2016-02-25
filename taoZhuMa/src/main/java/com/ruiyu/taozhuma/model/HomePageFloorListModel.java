package com.ruiyu.taozhuma.model;

import java.io.Serializable;
import java.util.ArrayList;

public class HomePageFloorListModel implements Serializable{
	public String type;//（1为遥控/电动玩具；2为早教/音乐玩具 ；3为过家家玩具；4为童车玩具；5为益智玩具；6为其他玩具）
	public ArrayList<Arr> arr;//返回各个分类的列表
	public class Arr implements Serializable{
		public String typeId;//产品ID，列表时为”null”
		public String image;//图片
		public String title;//活动标题
		public Integer sorting;//排序
		public Double price;//价格
		
	}

}
